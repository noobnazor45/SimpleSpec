# SimpleSpec

A small RuneLite overlay that shows how many special attacks you can perform right now.

The meter reads your special attack energy and equipped weapon, then fills three slots.
Each slot holds up to three specs, layered by color like a fighting game super meter:

- Yellow: specs 1 to 3
- Red: specs 4 to 6
- Purple: specs 7 to 9

While the Tombs of Amascut Liquid Adrenaline buff is active, spec costs are halved and the
meter can show an outline to indicate it.

## Supported weapons

Every combat weapon whose special attack has a fixed energy cost is supported, about 90 in
total, from the Bandos godsword and Dragon claws to the Dual macuahuitl and Ivandis flail.
Ornament, poisoned, Last Man Standing, Bounty Hunter and Deadman variants are included, and
weapons whose variants cost different amounts (Granite maul with the ornate handle, Magic
shortbow (i)) use the right cost for each. The full list with costs is in `SpecWeapon.java`.

Not supported, because they have no fixed "specs left":

- Variable cost specs: Dragon and Ursine hasta, Rune thrownaxe, Soulflame horn
- Stack based specs: Soulreaper axe, Sunlight spear, Lithic sceptre
- Shield semi-specials on a cooldown, skilling tool specs, and temporary league weapons

The Dual macuahuitl spec also requires the full Blood Moon armour set, but the meter counts it
either way. The overlay hides itself when no supported weapon is equipped.

## Configuration

Tier and empty slot colors, slot size and spacing, horizontal or vertical layout, partial
progress fill, an optional count label, and the adrenaline outline can all be changed in the
plugin settings.

Under Position, the meter can either be dragged anywhere on screen, or be drawn on your
character, next to your overhead health bar by default. In on character mode the horizontal
and vertical offsets are in game units (128 is one tile), so the meter keeps its place next to
your character when you zoom in or out.

This plugin is read-only and visual only. It does not send input, change menus, or make
network requests.
