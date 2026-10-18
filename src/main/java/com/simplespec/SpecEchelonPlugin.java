package com.simplespec;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.GameState;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.gameval.InventoryID;
import net.runelite.api.gameval.VarPlayerID;
import net.runelite.api.gameval.VarbitID;
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

	static final int MAX_SPECS = 9;

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

	@Getter
	private boolean adrenalineActive;

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
		adrenalineActive = false;
		specEnergy = 0;
		recompute();
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() == GameState.LOGGED_IN)
		{
			refreshAll();
		}
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		if (event.getContainerId() == InventoryID.WORN)
		{
			updateWeapon(event.getItemContainer());
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

	private void refreshAll()
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		updateWeapon(client.getItemContainer(InventoryID.WORN));
		readVars();
		recompute();
	}

	private void readVars()
	{
		specEnergy = client.getVarpValue(VarPlayerID.SA_ENERGY);
		adrenalineActive = client.getVarbitValue(LIQUID_ADRENALINE_VARBIT) > 0;
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

	private void recompute()
	{
		if (weapon == null)
		{
			totalSpecs = 0;
			partialProgress = 0f;
			return;
		}

		// Work in tenths of a percent like the energy varp, so a halved 25% cost stays
		// exact at 125 (12.5%) instead of rounding down to 12%.
		int activeCost = weapon.getBaseCost() * 10;
		if (adrenalineActive)
		{
			// Liquid Adrenaline halves the cost, so each spec needs half the energy.
			activeCost /= 2;
		}

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
	}

	@Provides
	SpecEchelonConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(SpecEchelonConfig.class);
	}
}
