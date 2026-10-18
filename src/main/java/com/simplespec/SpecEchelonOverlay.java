package com.simplespec;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.RoundRectangle2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.TextComponent;

class SpecEchelonOverlay extends Overlay
{
	private static final int SLOT_COUNT = 3;
	private static final int SPECS_PER_TIER = 3;
	private static final int ARC = 6;
	// Space reserved around the slots for the adrenaline outline, so toggling it does not shift the meter.
	private static final int PADDING = 3;
	private static final int LABEL_GAP = 4;
	private static final Color OUTLINE_COLOR = new Color(0, 0, 0, 200);
	private static final Color ADRENALINE_COLOR = new Color(110, 230, 140, 220);
	private static final Stroke ADRENALINE_STROKE = new BasicStroke(1.5f);

	final Client client;
	final SpecEchelonConfig config;
	private final SpecEchelonPlugin plugin;
	private final TextComponent label = new TextComponent();

	@Inject
	SpecEchelonOverlay(Client client, SpecEchelonPlugin plugin, SpecEchelonConfig config)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;
		setPosition(OverlayPosition.BOTTOM_RIGHT);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (config.positionMode() != SpecEchelonConfig.PositionMode.FREE)
		{
			return null;
		}

		return renderMeter(graphics);
	}

	/**
	 * Height of the slots plus outline padding, without the text label.
	 */
	int meterHeight()
	{
		final boolean vertical = config.orientation() == SpecEchelonConfig.Orientation.VERTICAL;
		final int h = config.pipHeight();
		return (vertical ? SLOT_COUNT * h + (SLOT_COUNT - 1) * config.pipSpacing() : h) + 2 * PADDING;
	}

	/**
	 * Draws the meter with its top left corner at the graphics origin.
	 */
	Dimension renderMeter(Graphics2D graphics)
	{
		if (client.getGameState() != GameState.LOGGED_IN || plugin.getWeapon() == null)
		{
			return null;
		}

		final int totalSpecs = plugin.getTotalSpecs();
		final float partial = config.showPartialProgress() ? plugin.getPartialProgress() : 0f;
		final Color[] tierColors = {config.tier1Color(), config.tier2Color(), config.tier3Color()};
		final boolean vertical = config.orientation() == SpecEchelonConfig.Orientation.VERTICAL;
		final int w = config.pipWidth();
		final int h = config.pipHeight();
		final int gap = config.pipSpacing();

		final int meterWidth = (vertical ? w : SLOT_COUNT * w + (SLOT_COUNT - 1) * gap) + 2 * PADDING;
		final int meterHeight = meterHeight();

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// Fade everything drawn below by the opacity setting, then restore it for other overlays.
		final Composite oldComposite = graphics.getComposite();
		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, config.opacity() / 100f));

		for (int i = 0; i < SLOT_COUNT; i++)
		{
			// Slot 0 is leftmost when horizontal and bottommost when vertical.
			int x = PADDING + (vertical ? 0 : i * (w + gap));
			int y = PADDING + (vertical ? (SLOT_COUNT - 1 - i) * (h + gap) : 0);
			Shape slot = new RoundRectangle2D.Float(x, y, w, h, ARC, ARC);

			// Higher tiers cover lower ones from the left, like a fighting game super meter.
			graphics.setColor(totalSpecs > i
				? tierColors[(totalSpecs - 1 - i) / SPECS_PER_TIER]
				: config.emptyColor());
			graphics.fill(slot);

			// The next spec lands in slot (totalSpecs % 3), drawn in its own tier color.
			if (partial > 0f && totalSpecs < SpecEchelonPlugin.MAX_SPECS && i == totalSpecs % SLOT_COUNT)
			{
				Shape oldClip = graphics.getClip();
				graphics.clip(slot);
				graphics.setColor(tierColors[totalSpecs / SPECS_PER_TIER]);
				if (vertical)
				{
					int fill = Math.round(h * partial);
					graphics.fillRect(x, y + h - fill, w, fill);
				}
				else
				{
					graphics.fillRect(x, y, Math.round(w * partial), h);
				}
				graphics.setClip(oldClip);
			}

			graphics.setColor(OUTLINE_COLOR);
			graphics.draw(slot);
		}

		if (config.highlightAdrenaline() && plugin.isAdrenalineActive())
		{
			Stroke oldStroke = graphics.getStroke();
			graphics.setStroke(ADRENALINE_STROKE);
			graphics.setColor(ADRENALINE_COLOR);
			graphics.draw(new RoundRectangle2D.Float(1, 1, meterWidth - 2, meterHeight - 2, ARC + 4, ARC + 4));
			graphics.setStroke(oldStroke);
		}

		Dimension size = new Dimension(meterWidth, meterHeight);
		final String timerText = config.showNextSpecTimer() ? plugin.getNextSpecText() : null;

		if (config.showTextLabel() || timerText != null)
		{
			graphics.setFont(FontManager.getRunescapeSmallFont());
			if (config.showTextLabel())
			{
				size = drawText(graphics, String.valueOf(totalSpecs), Color.WHITE, vertical, meterWidth, meterHeight, size);
			}
			if (timerText != null)
			{
				size = drawText(graphics, timerText, config.nextSpecTimerColor(), vertical, meterWidth, meterHeight, size);
			}
		}

		graphics.setComposite(oldComposite);
		return size;
	}

	/**
	 * Draws text after everything drawn so far, below it when vertical or to the right of it when
	 * horizontal, and returns the new overall size.
	 */
	private Dimension drawText(Graphics2D graphics, String text, Color color, boolean vertical,
		int meterWidth, int meterHeight, Dimension size)
	{
		FontMetrics metrics = graphics.getFontMetrics();
		int textWidth = metrics.stringWidth(text);

		Point position;
		Dimension newSize;
		if (vertical)
		{
			// Centered below the meter.
			position = new Point((meterWidth - textWidth) / 2, size.height + LABEL_GAP + metrics.getAscent());
			newSize = new Dimension(Math.max(size.width, textWidth), size.height + LABEL_GAP + metrics.getHeight());
		}
		else
		{
			// Vertically centered to the right of the meter.
			position = new Point(size.width + LABEL_GAP, (meterHeight + metrics.getAscent() - metrics.getDescent()) / 2);
			newSize = new Dimension(size.width + LABEL_GAP + textWidth, size.height);
		}

		label.setText(text);
		label.setColor(color);
		label.setPosition(position);
		label.render(graphics);
		return newSize;
	}
}
