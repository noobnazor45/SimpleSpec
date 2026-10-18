package com.simplespec;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import java.util.Set;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Constants;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.GameState;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.InventoryID;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.gameval.VarPlayerID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "SimpleSpec",
	description = "Shows how many special attacks you can use based on spec energy, weapon, and ToA Liquid Adrenaline",
	tags = {"special", "spec", "attack", "energy", "pvm", "toa", "raids"}
)
public class SpecEchelonPlugin extends Plugin
{
	/**
	 * Liquid Adrenaline active flag, treated as active while above 0.
	 * UNVERIFIED: confirm with Developer Tools > Var Inspector by drinking a dose in ToA.
	 * RuneLite's Timers and Buffs plugin uses this varbit to end its Liquid Adrenaline timer.
	 */
	static final int LIQUID_ADRENALINE_VARBIT = VarbitID.TOA_MIDRAIDLOOT_ENERGY_ACTIVE;

	/*
	 * Overly Draining has no varbit or varp. Var Inspector showed no change when toggling it,
	 * creating a party, or entering the raid (only the total raid level changes). Like the Tombs
	 * of Amascut hub plugin's invocation presets, it is read from the party screen's invocation
	 * list in the lobby and remembered for the raid.
	 */
	// Party screen scripts (no gameval constants exist): building the screen, and toggling its panel.
	private static final int SCRIPT_BUILD_TOA_PARTY_INTERFACE = 6729;
	private static final int SCRIPT_TOA_PARTY_TOGGLE_REWARD_PANEL = 6732;
	// Each invocation takes 3 children in the list, and Overly Draining is the 20th invocation.
	private static final int OVERLY_DRAINING_CHILD = 19 * 3;
	// An invocation's click handler argument at this index is 1 while it is selected.
	private static final int INVOCATION_SELECTED_ARG = 3;

	/**
	 * Tombs of Amascut instance regions. The adrenaline varbit can linger after leaving, and the
	 * remembered Overly Draining selection stays set, so both only count while in one of these.
	 */
	private static final Set<Integer> TOA_REGIONS = ImmutableSet.of(
		14160, // Nexus
		15698, // Path of Crondis
		15700, // Zebak
		14162, // Path of Scabaras
		14164, // Kephri
		15186, // Path of Apmeken
		15188, // Ba-Ba
		14674, // Path of Het
		14676, // Akkha
		15184, // Wardens, first and second phase
		15696, // Wardens, final phase
		14672  // Tomb
	);

	static final int MAX_SPECS = 9;

	// Spec energy is stored in tenths of a percent.
	private static final int MAX_ENERGY = 1000;
	// Energy restored per regen step, 10%.
	private static final int REGEN_AMOUNT = 100;
	// Ticks per regen step, halved while wearing the Lightbearer.
	private static final int SPEC_REGEN_TICKS = 50;

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private SpecEchelonOverlay overlay;

	@Inject
	private SpecEchelonAnchoredOverlay anchoredOverlay;

	@Getter
	private SpecWeapon weapon;

	/**
	 * Liquid Adrenaline, only while inside the Tombs of Amascut.
	 */
	@Getter
	private boolean adrenalineActive;

	// Raw states, before the ToA region check.
	private boolean adrenalineVarActive;
	// Overly Draining as last seen on the party screen.
	private boolean overlyDrainingSelected;

	private boolean inToa;

	/**
	 * Raw spec energy in tenths of a percent, 0 to 1000.
	 */
	private int specEnergy;

	@Getter
	private int totalSpecs;

	/**
	 * Progress toward the next spec, 0.0 to 1.0.
	 */
	@Getter
	private float partialProgress;

	// Regen cycle tracking, mirroring RuneLite's Regeneration Meter plugin.
	private int ticksSinceSpecRegen;
	private boolean wearingLightbearer;
	// Energy seen on the previous game tick, or -1 when unknown.
	private int lastTickEnergy = -1;

	/**
	 * Time until the next spec is available, such as "12s", or null when the timer is hidden.
	 */
	@Getter
	private String nextSpecText;

	@Override
	protected void startUp()
	{
		// Only one of these draws at a time, depending on the position mode.
		overlayManager.add(overlay);
		overlayManager.add(anchoredOverlay);
		// Pick up the current state so the meter is correct without re-equipping.
		clientThread.invoke(this::refreshAll);
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(overlay);
		overlayManager.remove(anchoredOverlay);
		weapon = null;
		adrenalineVarActive = false;
		overlyDrainingSelected = false;
		inToa = false;
		specEnergy = 0;
		ticksSinceSpecRegen = 0;
		wearingLightbearer = false;
		lastTickEnergy = -1;
		recompute();
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		GameState state = event.getGameState();
		if (state == GameState.LOGGED_IN)
		{
			refreshAll();
		}
		else if (state == GameState.HOPPING || state == GameState.LOGIN_SCREEN)
		{
			// Same reset as the Regeneration Meter, the regen cycle restarts on a new session.
			ticksSinceSpecRegen = 0;
			lastTickEnergy = -1;
		}
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		if (event.getContainerId() == InventoryID.WORN)
		{
			updateWeapon(event.getItemContainer());
			updateLightbearer(event.getItemContainer());
			recompute();
		}
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		if (event.getVarpId() == VarPlayerID.SA_ENERGY || event.getVarbitId() == LIQUID_ADRENALINE_VARBIT)
		{
			readVars();
			recompute();
		}
	}

	@Subscribe
	public void onScriptPostFired(ScriptPostFired event)
	{
		int scriptId = event.getScriptId();
		if (scriptId == SCRIPT_BUILD_TOA_PARTY_INTERFACE || scriptId == SCRIPT_TOA_PARTY_TOGGLE_REWARD_PANEL)
		{
			readOverlyDraining();
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		boolean nowInToa = isInToa();
		if (nowInToa != inToa)
		{
			log.debug("Tombs of Amascut region {}", nowInToa ? "entered" : "left");
			inToa = nowInToa;
		}

		specEnergy = client.getVarpValue(VarPlayerID.SA_ENERGY);
		updateRegenCycle();
		recompute();
	}

	private void refreshAll()
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		ItemContainer equipment = client.getItemContainer(InventoryID.WORN);
		updateWeapon(equipment);
		updateLightbearer(equipment);
		inToa = isInToa();
		readVars();
		recompute();
	}

	private void readVars()
	{
		specEnergy = client.getVarpValue(VarPlayerID.SA_ENERGY);
		adrenalineVarActive = client.getVarbitValue(LIQUID_ADRENALINE_VARBIT) > 0;
	}

	/**
	 * Reads whether Overly Draining is selected on the ToA party screen. Only reads the widget,
	 * it never changes it. The result is kept until the screen is shown again.
	 */
	private void readOverlyDraining()
	{
		Widget list = client.getWidget(InterfaceID.ToaPartydetails.INVOCATIONS_CONTENT);
		Widget invocation = list == null ? null : list.getChild(OVERLY_DRAINING_CHILD);
		if (invocation == null)
		{
			return;
		}

		Object[] args = invocation.getOnOpListener();
		if (args == null || args.length <= INVOCATION_SELECTED_ARG || !(args[INVOCATION_SELECTED_ARG] instanceof Integer))
		{
			return;
		}

		boolean selected = (Integer) args[INVOCATION_SELECTED_ARG] == 1;
		if (selected != overlyDrainingSelected)
		{
			log.debug("Overly Draining {}", selected ? "selected" : "deselected");
			overlyDrainingSelected = selected;
			recompute();
		}
	}

	private boolean isInToa()
	{
		Player local = client.getLocalPlayer();
		if (local == null)
		{
			return false;
		}

		// ToA is instanced, so map the instance tile back to its template region.
		WorldPoint location = WorldPoint.fromLocalInstance(client, local.getLocalLocation());
		return location != null && TOA_REGIONS.contains(location.getRegionID());
	}

	private void updateWeapon(ItemContainer equipment)
	{
		Item item = equipment == null ? null : equipment.getItem(EquipmentInventorySlot.WEAPON.getSlotIdx());
		SpecWeapon newWeapon = item == null ? null : SpecWeapon.forItemId(item.getId());
		if (newWeapon != weapon)
		{
			log.debug("Tracked weapon changed to {}", newWeapon == null ? "none" : newWeapon.getDisplayName());
			weapon = newWeapon;
		}
	}

	private void updateLightbearer(ItemContainer equipment)
	{
		Item ring = equipment == null ? null : equipment.getItem(EquipmentInventorySlot.RING.getSlotIdx());
		boolean hasLightbearer = ring != null
			&& (ring.getId() == ItemID.LIGHTBEARER || ring.getId() == ItemID.BR_LIGHTBEARER);
		if (hasLightbearer == wearingLightbearer)
		{
			return;
		}

		// Same as the Regeneration Meter: swapping the Lightbearer keeps the time until the next
		// regen if under 25 ticks remain. Taking it off always restarts the 50 tick cycle.
		ticksSinceSpecRegen = Math.max(0, ticksSinceSpecRegen - SPEC_REGEN_TICKS / 2);
		wearingLightbearer = hasLightbearer;
	}

	private int ticksPerSpecRegen()
	{
		return wearingLightbearer ? SPEC_REGEN_TICKS / 2 : SPEC_REGEN_TICKS;
	}

	/**
	 * Advances the regen cycle by one game tick, like the Regeneration Meter, and also resyncs the
	 * cycle whenever a natural regen step is seen so the timer corrects itself after logging in.
	 */
	private void updateRegenCycle()
	{
		if (specEnergy >= MAX_ENERGY)
		{
			// The recharge does not tick while at 100%.
			ticksSinceSpecRegen = 0;
		}
		else if (lastTickEnergy >= 0 && specEnergy - lastTickEnergy == REGEN_AMOUNT)
		{
			// Only a regen step adds exactly 10%, so a regen just happened. Potions restore other
			// amounts and do not move the cycle.
			ticksSinceSpecRegen = 0;
		}
		else
		{
			ticksSinceSpecRegen = (ticksSinceSpecRegen + 1) % ticksPerSpecRegen();
		}
		lastTickEnergy = specEnergy;
	}

	private void recompute()
	{
		adrenalineActive = inToa && adrenalineVarActive;

		if (weapon == null)
		{
			totalSpecs = 0;
			partialProgress = 0f;
			nextSpecText = null;
			return;
		}

		// Work in tenths of a percent like the energy varp, so a halved 25% cost stays
		// exact at 125 (12.5%) instead of rounding down to 12%.
		// Overly Draining makes every spec cost 100%, replacing the weapon's own cost.
		int baseCost = inToa && overlyDrainingSelected ? MAX_ENERGY : weapon.getBaseCost() * 10;
		// Liquid Adrenaline then halves whichever cost applies, so Overly Draining with
		// adrenaline costs 50% per spec.
		int activeCost = adrenalineActive ? baseCost / 2 : baseCost;

		int specs = specEnergy / activeCost;
		if (specs >= MAX_SPECS)
		{
			totalSpecs = MAX_SPECS;
			partialProgress = 0f;
		}
		else
		{
			totalSpecs = specs;
			partialProgress = (specEnergy % activeCost) / (float) activeCost;
		}

		updateNextSpecText(activeCost);
	}

	private void updateNextSpecText(int activeCost)
	{
		if (specEnergy >= MAX_ENERGY || totalSpecs >= MAX_SPECS)
		{
			nextSpecText = null;
			return;
		}

		int ticksPerRegen = ticksPerSpecRegen();
		// Energy still missing for the next spec, and how many 10% regen steps cover it.
		int energyNeeded = activeCost - (specEnergy % activeCost);
		int regenSteps = (energyNeeded + REGEN_AMOUNT - 1) / REGEN_AMOUNT;
		// The rest of the current step, plus every remaining full step.
		int ticks = (ticksPerRegen - ticksSinceSpecRegen) + (regenSteps - 1) * ticksPerRegen;
		// 0.6 seconds per tick, rounded up so the timer never reads 0s early.
		int seconds = (ticks * Constants.GAME_TICK_LENGTH + 999) / 1000;
		nextSpecText = seconds + "s";
	}

	@Provides
	SpecEchelonConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(SpecEchelonConfig.class);
	}
}
