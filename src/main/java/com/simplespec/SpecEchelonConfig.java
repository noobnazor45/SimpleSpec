package com.simplespec;

import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;
import net.runelite.client.config.Units;

@ConfigGroup(SpecEchelonConfig.GROUP)
public interface SpecEchelonConfig extends Config
{
	String GROUP = "simplespec";

	enum Orientation
	{
		HORIZONTAL,
		VERTICAL
	}

	enum PositionMode
	{
		FREE("Free (drag anywhere)"),
		ON_CHARACTER("On character");

		private final String label;

		PositionMode(String label)
		{
			this.label = label;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	@ConfigSection(
		name = "Colors",
		description = "Meter colors",
		position = 0
	)
	String colorSection = "colors";

	@ConfigSection(
		name = "Layout",
		description = "Meter size and layout",
		position = 1
	)
	String layoutSection = "layout";

	@ConfigSection(
		name = "Position",
		description = "Where the meter is drawn",
		position = 2
	)
	String positionSection = "position";

	@Alpha
	@ConfigItem(
		keyName = "tier1Color",
		name = "Tier 1 color",
		description = "Color for specs 1 to 3",
		section = colorSection,
		position = 0
	)
	default Color tier1Color()
	{
		return new Color(255, 214, 0);
	}

	@Alpha
	@ConfigItem(
		keyName = "tier2Color",
		name = "Tier 2 color",
		description = "Color for specs 4 to 6",
		section = colorSection,
		position = 1
	)
	default Color tier2Color()
	{
		return new Color(220, 40, 40);
	}

	@Alpha
	@ConfigItem(
		keyName = "tier3Color",
		name = "Tier 3 color",
		description = "Color for specs 7 to 9",
		section = colorSection,
		position = 2
	)
	default Color tier3Color()
	{
		return new Color(160, 60, 220);
	}

	@Alpha
	@ConfigItem(
		keyName = "emptyColor",
		name = "Empty slot color",
		description = "Color for slots with no spec",
		section = colorSection,
		position = 3
	)
	default Color emptyColor()
	{
		return new Color(40, 40, 40, 160);
	}

	@Range(min = 10, max = 100)
	@Units(Units.PERCENT)
	@ConfigItem(
		keyName = "opacity",
		name = "Opacity",
		description = "Opacity of the whole meter, including outlines and the label. 100% is fully solid",
		section = colorSection,
		position = 4
	)
	default int opacity()
	{
		return 100;
	}

	@Range(min = 4, max = 60)
	@ConfigItem(
		keyName = "pipWidth",
		name = "Pip width",
		description = "Width of each slot in pixels",
		section = layoutSection,
		position = 0
	)
	default int pipWidth()
	{
		return 14;
	}

	@Range(min = 4, max = 60)
	@ConfigItem(
		keyName = "pipHeight",
		name = "Pip height",
		description = "Height of each slot in pixels",
		section = layoutSection,
		position = 1
	)
	default int pipHeight()
	{
		return 20;
	}

	@Range(max = 20)
	@ConfigItem(
		keyName = "pipSpacing",
		name = "Spacing",
		description = "Gap between slots in pixels",
		section = layoutSection,
		position = 2
	)
	default int pipSpacing()
	{
		return 3;
	}

	@ConfigItem(
		keyName = "orientation",
		name = "Orientation",
		description = "Lay the slots out horizontally or vertically",
		section = layoutSection,
		position = 3
	)
	default Orientation orientation()
	{
		return Orientation.HORIZONTAL;
	}

	@ConfigItem(
		keyName = "positionMode",
		name = "Position",
		description = "Drag the meter anywhere, or attach it to your character next to the health bar",
		section = positionSection,
		position = 0
	)
	default PositionMode positionMode()
	{
		return PositionMode.FREE;
	}

	@Range(min = -500, max = 500)
	@ConfigItem(
		keyName = "characterOffsetX",
		name = "Horizontal offset",
		description = "On character only. Moves the meter right (positive) or left (negative) of your character,"
			+ " in game units (128 is one tile), so it keeps its place when you zoom",
		section = positionSection,
		position = 1
	)
	default int characterOffsetX()
	{
		return 60;
	}

	@Range(min = -500, max = 500)
	@ConfigItem(
		keyName = "characterOffsetY",
		name = "Vertical offset",
		description = "On character only. Moves the meter up (positive) or down (negative) from the health bar,"
			+ " in game units (128 is one tile), so it keeps its place when you zoom",
		section = positionSection,
		position = 2
	)
	default int characterOffsetY()
	{
		return 0;
	}

	@ConfigItem(
		keyName = "showPartialProgress",
		name = "Show partial progress",
		description = "Fill the next slot proportionally toward the next spec",
		position = 3
	)
	default boolean showPartialProgress()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showTextLabel",
		name = "Show text label",
		description = "Show the exact number of specs next to the meter",
		position = 4
	)
	default boolean showTextLabel()
	{
		return false;
	}

	@ConfigItem(
		keyName = "highlightAdrenaline",
		name = "Highlight adrenaline",
		description = "Draw an outline around the meter while Liquid Adrenaline is active",
		position = 5
	)
	default boolean highlightAdrenaline()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showNextSpecTimer",
		name = "Show next spec timer",
		description = "Show the seconds until your next special attack is available, next to the meter",
		position = 6
	)
	default boolean showNextSpecTimer()
	{
		return false;
	}

	@Alpha
	@ConfigItem(
		keyName = "nextSpecTimerColor",
		name = "Timer text color",
		description = "Color of the next spec timer text",
		position = 7
	)
	default Color nextSpecTimerColor()
	{
		return Color.WHITE;
	}
}
