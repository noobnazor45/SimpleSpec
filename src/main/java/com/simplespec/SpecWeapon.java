package com.simplespec;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import javax.annotation.Nullable;
import lombok.Getter;
import net.runelite.api.gameval.ItemID;

/**
 * Weapons tracked by the meter, with every equippable item ID variant and the base spec cost.
 * Costs come from the OSRS wiki Special attacks page.
 * <p>
 * Left out on purpose because "specs left" has no fixed answer for them: variable cost specs
 * (dragon and ursine hasta, rune thrownaxe, soulflame horn), stack based specs (soulreaper axe,
 * sunlight spear, lithic sceptre), and the cooldown based shield semi-specials. Skilling tool
 * specs and temporary league weapons are also left out. Uncharged, inactive and unstrung items
 * are not listed because they cannot attack.
 */
@Getter
public enum SpecWeapon
{
	ABYSSAL_BLUDGEON("Abyssal bludgeon", 50,
		ItemID.ABYSSAL_BLUDGEON
	),
	ABYSSAL_DAGGER("Abyssal dagger", 25,
		ItemID.ABYSSAL_DAGGER,
		ItemID.ABYSSAL_DAGGER_P,
		ItemID.ABYSSAL_DAGGER_P_,
		ItemID.ABYSSAL_DAGGER_P__,
		ItemID.BH_ABYSSAL_DAGGER_IMBUE,
		ItemID.BH_ABYSSAL_DAGGER_P_IMBUE,
		ItemID.BH_ABYSSAL_DAGGER_P__IMBUE,
		ItemID.BH_ABYSSAL_DAGGER_P___IMBUE
	),
	ABYSSAL_TENTACLE("Abyssal tentacle", 50,
		ItemID.ABYSSAL_TENTACLE,
		ItemID.LEAGUE_3_WHIP_TENTACLE,
		ItemID.BR_ABYSSAL_TENTACLE
	),
	ABYSSAL_WHIP("Abyssal whip", 50,
		ItemID.ABYSSAL_WHIP,
		ItemID.ABYSSAL_WHIP_LAVA,
		ItemID.ABYSSAL_WHIP_ICE,
		ItemID.LEAGUE_3_WHIP,
		ItemID.BR_ABYSSAL_WHIP
	),
	ACCURSED_SCEPTRE("Accursed sceptre", 50,
		ItemID.WILD_CAVE_ACCURSED_CHARGED,
		ItemID.WILD_CAVE_ACCURSED_CHARGED_RECOL
	),
	ANCIENT_GODSWORD("Ancient godsword", 50,
		ItemID.ANCIENT_GODSWORD,
		ItemID.BR_ANCIENT_GODSWORD
	),
	ANCIENT_MACE("Ancient mace", 100,
		ItemID.ANCIENT_GOBLIN_MACE
	),
	ARCLIGHT("Arclight", 50,
		ItemID.ARCLIGHT
	),
	ARKAN_BLADE("Arkan blade", 30,
		ItemID.ARKAN_BLADE
	),
	ARMADYL_CROSSBOW("Armadyl crossbow", 50,
		ItemID.ACB,
		ItemID.BR_ACB
	),
	ARMADYL_GODSWORD("Armadyl godsword", 50,
		ItemID.AGS,
		ItemID.AGSG,
		ItemID.BR_AGS,
		ItemID.DEADMAN_AGS,
		ItemID.DEADMAN_BLIGHTED_AGS
	),
	BANDOS_GODSWORD("Bandos godsword", 50,
		ItemID.BGS,
		ItemID.BGSG
	),
	BARRELCHEST_ANCHOR("Barrelchest anchor", 50,
		ItemID.BRAIN_ANCHOR,
		ItemID.BH_BRAIN_ANCHOR_IMBUE
	),
	BLISTERWOOD_FLAIL("Blisterwood flail", 10,
		ItemID.BLISTERWOOD_FLAIL
	),
	BLUE_MOON_SPEAR("Blue Moon spear", 50,
		ItemID.FROSTMOON_SPEAR,
		ItemID.BR_FROSTMOON_SPEAR
	),
	BONE_DAGGER("Bone dagger", 75,
		ItemID.DTTD_BONE_DAGGER,
		ItemID.DTTD_BONE_DAGGER_P,
		ItemID.DTTD_BONE_DAGGER_P_,
		ItemID.DTTD_BONE_DAGGER_P__
	),
	BRINE_SABRE("Brine sabre", 75,
		ItemID.OLAF2_BRINE_SABRE
	),
	BURNING_CLAWS("Burning claws", 35,
		ItemID.BONE_CLAWS,
		ItemID.BR_BONE_CLAWS
	),
	CRIMSON_KISTEN("Crimson kisten", 50,
		ItemID.CRIMSON_KISTEN
	),
	CRYSTAL_HALBERD("Crystal halberd", 30,
		ItemID.CRYSTAL_HALBERD,
		ItemID.CRYSTAL_HALBERD_2500,
		ItemID.CRYSTAL_HALBERD_NEW,
		ItemID.CRYSTAL_HALBERD_1000,
		ItemID.CRYSTAL_HALBERD_900,
		ItemID.CRYSTAL_HALBERD_800,
		ItemID.CRYSTAL_HALBERD_700,
		ItemID.CRYSTAL_HALBERD_600,
		ItemID.CRYSTAL_HALBERD_500,
		ItemID.CRYSTAL_HALBERD_400,
		ItemID.CRYSTAL_HALBERD_300,
		ItemID.CRYSTAL_HALBERD_200,
		ItemID.CRYSTAL_HALBERD_100,
		ItemID.NZONE_CRYSTAL_HALBERD_NEW,
		ItemID.NZONE_CRYSTAL_HALBERD_1000,
		ItemID.NZONE_CRYSTAL_HALBERD_900,
		ItemID.NZONE_CRYSTAL_HALBERD_800,
		ItemID.NZONE_CRYSTAL_HALBERD_700,
		ItemID.NZONE_CRYSTAL_HALBERD_600,
		ItemID.NZONE_CRYSTAL_HALBERD_500,
		ItemID.NZONE_CRYSTAL_HALBERD_400,
		ItemID.NZONE_CRYSTAL_HALBERD_300,
		ItemID.NZONE_CRYSTAL_HALBERD_200,
		ItemID.NZONE_CRYSTAL_HALBERD_100
	),
	DARK_BOW("Dark bow", 55,
		ItemID.DARKBOW,
		ItemID.DARKBOW_GREEN,
		ItemID.DARKBOW_BLUE,
		ItemID.DARKBOW_YELLOW,
		ItemID.DARKBOW_WHITE,
		ItemID.BR_DARKBOW,
		ItemID.BH_DARKBOW_IMBUE,
		ItemID.DEADMAN_DARKBOW,
		ItemID.DEADMAN_BLIGHTED_DARK_BOW
	),
	DARKLIGHT("Darklight", 50,
		ItemID.DARKLIGHT
	),
	DAWNBRINGER("Dawnbringer", 35,
		ItemID.VERZIK_SPECIAL_WEAPON
	),
	DINHS_BULWARK("Dinh's bulwark", 50,
		ItemID.DINHS_BULWARK,
		ItemID.DINHS_BULWARK_ORNAMENT
	),
	DORGESHUUN_CROSSBOW("Dorgeshuun crossbow", 75,
		ItemID.DTTD_BONE_CROSSBOW
	),
	DRAGON_2H_SWORD("Dragon 2h sword", 60,
		ItemID.DRAGON_2H_SWORD,
		ItemID.BR_DRAGON_2H,
		ItemID.BH_DRAGON_2H_SWORD_CORRUPTED
	),
	DRAGON_BATTLEAXE("Dragon battleaxe", 100,
		ItemID.DRAGON_BATTLEAXE,
		ItemID.BH_DRAGON_BATTLEAXE_CORRUPTED
	),
	DRAGON_CLAWS("Dragon claws", 50,
		ItemID.DRAGON_CLAWS,
		ItemID.DRAGON_CLAWS_ORNAMENT,
		ItemID.BR_DRAGON_CLAWS,
		ItemID.BH_DRAGON_CLAWS_CORRUPTED,
		ItemID.DEADMAN_BLIGHTED_DRAGON_CLAWS
	),
	DRAGON_CROSSBOW("Dragon crossbow", 60,
		ItemID.XBOWS_CROSSBOW_DRAGON,
		ItemID.BR_XBOWS_CROSSBOW_DRAGON,
		ItemID.BH_XBOWS_CROSSBOW_DRAGON_CORRUPTED
	),
	DRAGON_DAGGER("Dragon dagger", 25,
		ItemID.DRAGON_DAGGER,
		ItemID.DRAGON_DAGGER_P,
		ItemID.DRAGON_DAGGER_P_,
		ItemID.DRAGON_DAGGER_P__,
		ItemID.BR_DRAGON_DAGGER,
		ItemID.BH_DRAGON_DAGGER_CORRUPTED,
		ItemID.BH_DRAGON_DAGGER_P_CORRUPTED,
		ItemID.BH_DRAGON_DAGGER_P__CORRUPTED,
		ItemID.BH_DRAGON_DAGGER_P___CORRUPTED
	),
	DRAGON_HALBERD("Dragon halberd", 30,
		ItemID.DRAGON_HALBERD,
		ItemID.BH_DRAGON_HALBERD_CORRUPTED
	),
	DRAGON_KNIFE("Dragon knife", 25,
		ItemID.DRAGON_KNIFE,
		ItemID.DRAGON_KNIFE_P,
		ItemID.DRAGON_KNIFE_P_,
		ItemID.DRAGON_KNIFE_P__,
		ItemID.BR_DRAGON_KNIFE
	),
	DRAGON_LONGSWORD("Dragon longsword", 25,
		ItemID.DRAGON_LONGSWORD,
		ItemID.BH_DRAGON_LONGSWORD_IMBUE,
		ItemID.BH_DRAGON_LONGSWORD_CORRUPTED
	),
	DRAGON_MACE("Dragon mace", 25,
		ItemID.DRAGON_MACE,
		ItemID.BH_DRAGON_MACE_IMBUE,
		ItemID.BH_DRAGON_MACE_CORRUPTED
	),
	DRAGON_SCIMITAR("Dragon scimitar", 55,
		ItemID.DRAGON_SCIMITAR,
		ItemID.DRAGON_SCIMITAR_ORNAMENT,
		ItemID.BR_DRAGON_SCIMITAR,
		ItemID.BH_DRAGON_SCIMITAR_CORRUPTED
	),
	DRAGON_SPEAR("Dragon spear", 25,
		ItemID.DRAGON_SPEAR,
		ItemID.DRAGON_SPEAR_P,
		ItemID.DRAGON_SPEAR_P_,
		ItemID.DRAGON_SPEAR_P__,
		ItemID.TBWT_DRAGON_SPEAR_KP,
		ItemID.BH_DRAGON_SPEAR_CORRUPTED,
		ItemID.BH_DRAGON_SPEAR_P_CORRUPTED,
		ItemID.BH_DRAGON_SPEAR_P__CORRUPTED,
		ItemID.BH_DRAGON_SPEAR_P___CORRUPTED
	),
	DRAGON_SWORD("Dragon sword", 40,
		ItemID.DRAGON_SHORTSWORD,
		ItemID.BR_DRAGON_SWORD,
		ItemID.BH_DRAGON_SHORTSWORD_CORRUPTED
	),
	DRAGON_THROWNAXE("Dragon thrownaxe", 25,
		ItemID.DRAGON_THROWNAXE,
		ItemID.BR_DRAGON_THROWNAXE
	),
	DRAGON_WARHAMMER("Dragon warhammer", 50,
		ItemID.DRAGON_WARHAMMER,
		ItemID.DRAGON_WARHAMMER_ORNAMENT,
		ItemID.BR_DRAGON_WARHAMMER,
		ItemID.BH_DRAGON_WARHAMMER_CORRUPTED
	),
	// The spec also needs the full Blood Moon armour set; the meter counts it regardless.
	DUAL_MACUAHUITL("Dual macuahuitl", 25,
		ItemID.DUAL_MACUAHUITL,
		ItemID.BR_DUAL_MACUAHUITL
	),
	ECLIPSE_ATLATL("Eclipse atlatl", 50,
		ItemID.ECLIPSE_ATLATL,
		ItemID.BR_ECLIPSE_ATLATL
	),
	ELDER_MAUL("Elder maul", 50,
		ItemID.ELDER_MAUL,
		ItemID.ELDER_MAUL_ORNAMENT,
		ItemID.BR_ELDER_MAUL
	),
	ELDRITCH_NIGHTMARE_STAFF("Eldritch nightmare staff", 55,
		ItemID.NIGHTMARE_STAFF_ELDRITCH
	),
	EMBERLIGHT("Emberlight", 50,
		ItemID.EMBERLIGHT
	),
	EXCALIBUR("Excalibur", 100,
		ItemID.EXCALIBUR
	),
	EYE_OF_AYAK("Eye of Ayak", 50,
		ItemID.EYE_OF_AYAK
	),
	GRANITE_HAMMER("Granite hammer", 60,
		ItemID.GRANITE_HAMMER
	),
	GRANITE_MAUL("Granite maul", 60,
		ItemID.GRANITE_MAUL,
		ItemID.GRANITE_MAUL_PLUS,
		ItemID.BR_GRANITE_MAUL
	),
	// The ornate handle lowers the cost from 60% to 50%.
	GRANITE_MAUL_ORNATE("Granite maul (ornate handle)", 50,
		ItemID.GRANITE_MAUL_PRETTY,
		ItemID.GRANITE_MAUL_PRETTY_PLUS
	),
	HALLOWED_FLAIL("Hallowed flail", 10,
		ItemID.HALLOWED_FLAIL
	),
	HEAVY_BALLISTA("Heavy ballista", 65,
		ItemID.HEAVY_BALLISTA,
		ItemID.HEAVY_BALLISTA_ORNAMENT,
		ItemID.BR_HEAVY_BALLISTA
	),
	IVANDIS_FLAIL("Ivandis flail", 10,
		ItemID.IVANDIS_FLAIL
	),
	KERIS_PARTISAN_OF_CORRUPTION("Keris partisan of corruption", 75,
		ItemID.KERIS_PARTISAN_CORRUPTION
	),
	KERIS_PARTISAN_OF_THE_SUN("Keris partisan of the sun", 75,
		ItemID.KERIS_PARTISAN_SUN
	),
	LIGHT_BALLISTA("Light ballista", 65,
		ItemID.LIGHT_BALLISTA,
		ItemID.BR_LIGHT_BALLISTA
	),
	MAGIC_COMP_BOW("Magic comp bow", 35,
		ItemID.TRAIL_COMPOSITE_BOW_MAGIC
	),
	MAGIC_LONGBOW("Magic longbow", 35,
		ItemID.MAGIC_LONGBOW
	),
	MAGIC_SHORTBOW("Magic shortbow", 55,
		ItemID.MAGIC_SHORTBOW,
		ItemID.BR_MAGIC_BOW
	),
	// The imbued version lowers the cost from 55% to 50%.
	MAGIC_SHORTBOW_I("Magic shortbow (i)", 50,
		ItemID.MAGIC_SHORTBOW_I
	),
	MORRIGANS_JAVELIN("Morrigan's javelin", 50,
		ItemID.MORRIGANS_JAVELIN,
		ItemID.BR_MORRIGANS_JAVELIN,
		ItemID.MORRIGANS_JAVELIN_BH
	),
	MORRIGANS_THROWING_AXE("Morrigan's throwing axe", 50,
		ItemID.MORRIGANS_THROWNAXE,
		ItemID.MORRIGANS_THROWNAXE_BH
	),
	NOXIOUS_HALBERD("Noxious halberd", 50,
		ItemID.NOXIOUS_HALBERD,
		ItemID.BR_NOXIOUS_HALBERD
	),
	OSMUMTENS_FANG("Osmumten's fang", 25,
		ItemID.OSMUMTENS_FANG,
		ItemID.OSMUMTENS_FANG_ORNAMENT,
		ItemID.BR_OSMUMTENS_FANG
	),
	PURGING_STAFF("Purging staff", 25,
		ItemID.PURGING_STAFF,
		ItemID.BR_PURGING_STAFF
	),
	ROD_OF_IVANDIS("Rod of Ivandis", 10,
		ItemID.BURGH_ROD_COMMAND_FINAL_10,
		ItemID.BURGH_ROD_COMMAND_FINAL_9,
		ItemID.BURGH_ROD_COMMAND_FINAL_8,
		ItemID.BURGH_ROD_COMMAND_FINAL_7,
		ItemID.BURGH_ROD_COMMAND_FINAL_6,
		ItemID.BURGH_ROD_COMMAND_FINAL_5,
		ItemID.BURGH_ROD_COMMAND_FINAL_4,
		ItemID.BURGH_ROD_COMMAND_FINAL_3,
		ItemID.BURGH_ROD_COMMAND_FINAL_2,
		ItemID.BURGH_ROD_COMMAND_FINAL_1
	),
	ROSEWOOD_BLOWPIPE("Rosewood blowpipe", 25,
		ItemID.ROSEWOOD_BLOWPIPE
	),
	RUNE_CLAWS("Rune claws", 25,
		ItemID.RUNE_CLAWS
	),
	SARADOMIN_GODSWORD("Saradomin godsword", 50,
		ItemID.SGS,
		ItemID.SGSG
	),
	SARADOMIN_SWORD("Saradomin sword", 100,
		ItemID.SARADOMIN_SWORD
	),
	SARADOMINS_BLESSED_SWORD("Saradomin's blessed sword", 65,
		ItemID.BLESSED_SARADOMIN_SWORD_DEGRADED
	),
	SCORCHING_BOW("Scorching bow", 25,
		ItemID.SCORCHING_BOW
	),
	SEERCULL("Seercull", 100,
		ItemID.DAGANOTH_CAVE_MAGIC_SHORTBOW
	),
	STAFF_OF_BALANCE("Staff of balance", 100,
		ItemID.STAFF_OF_BALANCE
	),
	STAFF_OF_LIGHT("Staff of light", 100,
		ItemID.STAFF_OF_LIGHT
	),
	STAFF_OF_THE_DEAD("Staff of the dead", 100,
		ItemID.SOTD,
		ItemID.BR_SOTD
	),
	STATIUSS_WARHAMMER("Statius's warhammer", 35,
		ItemID.STATIUS_WARHAMMER,
		ItemID.BR_STATIUS_WARHAMMER,
		ItemID.STATIUS_WARHAMMER_BH
	),
	SUNSPEAR("Sunspear", 50,
		ItemID.SUNSPEAR
	),
	TONALZTICS_OF_RALOS("Tonalztics of Ralos", 50,
		ItemID.TONALZTICS_OF_RALOS_CHARGED
	),
	TOXIC_BLOWPIPE("Toxic blowpipe", 50,
		ItemID.TOXIC_BLOWPIPE_LOADED
	),
	TOXIC_STAFF_OF_THE_DEAD("Toxic staff of the dead", 100,
		ItemID.TOXIC_SOTD_CHARGED
	),
	URSINE_CHAINMACE("Ursine chainmace", 50,
		ItemID.WILD_CAVE_URSINE_CHARGED
	),
	VESTAS_BLIGHTED_LONGSWORD("Vesta's blighted longsword", 25,
		ItemID.BH_VESTAS_LONGSWORD
	),
	VESTAS_LONGSWORD("Vesta's longsword", 25,
		ItemID.VESTAS_LONGSWORD,
		ItemID.BR_VESTAS_LONGSWORD,
		ItemID.VESTAS_LONGSWORD_BH
	),
	VESTAS_SPEAR("Vesta's spear", 50,
		ItemID.VESTAS_SPEAR,
		ItemID.VESTAS_SPEAR_BH
	),
	VOIDWAKER("Voidwaker", 50,
		ItemID.VOIDWAKER,
		ItemID.BR_VOIDWAKER,
		ItemID.DEADMAN_VOIDWAKER,
		ItemID.DEADMAN_BLIGHTED_VOIDWAKER
	),
	VOLATILE_NIGHTMARE_STAFF("Volatile nightmare staff", 55,
		ItemID.NIGHTMARE_STAFF_VOLATILE,
		ItemID.BR_NIGHTMARE_STAFF_VOLATILE,
		ItemID.DEADMAN_NIGHTMARE_STAFF_VOLATILE,
		ItemID.DEADMAN_BLIGHTED_VOLATILE_STAFF
	),
	WEBWEAVER_BOW("Webweaver bow", 50,
		ItemID.WILD_CAVE_WEBWEAVER_CHARGED
	),
	ZAMORAK_GODSWORD("Zamorak godsword", 50,
		ItemID.ZGS,
		ItemID.ZGSG
	),
	ZAMORAKIAN_HASTA("Zamorakian hasta", 25,
		ItemID.ZAMORAK_HASTA
	),
	ZAMORAKIAN_SPEAR("Zamorakian spear", 25,
		ItemID.ZAMORAK_SPEAR
	),
	ZARYTE_CROSSBOW("Zaryte crossbow", 75,
		ItemID.ZARYTE_XBOW,
		ItemID.BR_ZARYTE_XBOW
	);

	private static final Map<Integer, SpecWeapon> BY_ITEM_ID;

	static
	{
		ImmutableMap.Builder<Integer, SpecWeapon> builder = ImmutableMap.builder();
		for (SpecWeapon weapon : values())
		{
			for (int itemId : weapon.itemIds)
			{
				builder.put(itemId, weapon);
			}
		}
		BY_ITEM_ID = builder.build();
	}

	private final String displayName;
	/**
	 * Base spec cost in whole percent, before any Liquid Adrenaline reduction.
	 */
	private final int baseCost;
	private final int[] itemIds;

	SpecWeapon(String displayName, int baseCost, int... itemIds)
	{
		this.displayName = displayName;
		this.baseCost = baseCost;
		this.itemIds = itemIds;
	}

	@Nullable
	public static SpecWeapon forItemId(int itemId)
	{
		return BY_ITEM_ID.get(itemId);
	}
}
