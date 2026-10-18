package com.simplespec;

import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

/**
 * Draws the meter attached to the local player, level with the overhead health bar by default.
 * Offsets are in game units rather than pixels, so the meter keeps its place relative to the
 * character when the camera zooms. Used when the position mode is set to on character.
 */
class SpecEchelonAnchoredOverlay extends SpecEchelonOverlay
{
	// Distance used to measure how many screen pixels one game unit covers at the player.
	private static final int SCALE_PROBE = 128;
	// Fixed height of the health bar above the ground, in game units. The player's own model
	// height is not used because it changes with held items (a halberd is taller than a dagger).
	private static final int HEALTH_BAR_HEIGHT = 210;

	@Inject
	SpecEchelonAnchoredOverlay(Client client, SpecEchelonPlugin plugin, SpecEchelonConfig config)
	{
		super(client, plugin, config);
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
		setMovable(false);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (config.positionMode() != SpecEchelonConfig.PositionMode.ON_CHARACTER)
		{
			return null;
		}

		Player local = client.getLocalPlayer();
		if (local == null)
		{
			return null;
		}

		LocalPoint coord = local.getLocalLocation();
		int worldView = local.getWorldView().getId();
		int groundZ = Perspective.getFootprintTileHeight(client, coord, local.getWorldView().getPlane(), local.getFootprintSize());
		// A world height scales with zoom just like the character does.
		int z = groundZ - (HEALTH_BAR_HEIGHT + config.characterOffsetY());

		Point anchor = Perspective.localToCanvas(client, worldView, coord.getX(), coord.getY(), z);
		Point alongX = Perspective.localToCanvas(client, worldView, coord.getX() + SCALE_PROBE, coord.getY(), z);
		Point alongY = Perspective.localToCanvas(client, worldView, coord.getX(), coord.getY() + SCALE_PROBE, z);
		if (anchor == null || alongX == null || alongY == null)
		{
			return null;
		}

		// Screen pixels per game unit at the player's distance from the camera. Using only the
		// screen x of two perpendicular ground directions keeps this independent of camera
		// rotation and pitch, so the horizontal offset grows and shrinks with zoom.
		double pixelsPerUnit = Math.hypot(alongX.getX() - anchor.getX(), alongY.getX() - anchor.getX()) / SCALE_PROBE;

		int x = anchor.getX() + (int) Math.round(config.characterOffsetX() * pixelsPerUnit);
		int y = anchor.getY() - meterHeight() / 2;

		graphics.translate(x, y);
		renderMeter(graphics);
		graphics.translate(-x, -y);
		return null;
	}
}
