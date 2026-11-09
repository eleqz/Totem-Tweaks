## 1.3.0

### Added

- Added NeoForge support for Minecraft 1.20.4 through 26.3.
- Added Minecraft 26.3.
- Every supported Minecraft version, Fabric 1.19 through 26.3, now has every setting: totem size, pop size, disable
  equip animation, pop animation, pop duration, lock rotation position, disable rotations, static size and the size
  animation. Static size was missing on 1.21.6 through 1.21.11, and versions without a release before, such as 1.19
  and 1.21.9, are now covered too.

### Changed

- The settings screen now works with either YetAnotherConfigLib or Cloth Config, whichever is installed, and prefers
  YetAnotherConfigLib when both are. Both are optional, as is Mod Menu: without them the mod runs with the settings in
  `config/totemtweaks.json`, or the defaults. Existing settings are kept, they are still read from that same file.
- `/totemTweaks` opens the settings without Mod Menu installed. It needs YetAnotherConfigLib or Cloth Config.
- On Minecraft 1.19 through 1.20.1 the settings screen uses Cloth Config, because YetAnotherConfigLib does not
  support those versions.

### Removed

- The pop X and Y position, totem count, pop overlay and pop size animation settings of the 1.0.1 releases for
  Minecraft 1.21 through 1.21.5 are gone, as they already were from 1.1.0 onwards.
