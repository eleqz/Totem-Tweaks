# Contributing

## Compiling

All supported Minecraft versions are built from the `main` branch. [Stonecutter](https://stonecutter.kikugie.dev/)
resolves the per-version differences at build time, so there is no branch per version.

You need a JDK 21 or newer and [Git](https://git-scm.com/downloads). Gradle downloads the other Java versions it
needs.

```bash
git clone https://github.com/eleqz/Totem-Tweaks.git
cd Totem-Tweaks
./gradlew buildAndCollect
```

On Windows, run `.\gradlew buildAndCollect` instead. The jars are written to `build/libs/`, one per Minecraft range
and loader.

## Working on a single version

`src/` shows the code for one Minecraft version and loader at a time, 26.3 Fabric by default. Code for other versions
sits in `//? if` blocks and is commented out until that version is active.

- Switch the active version with the `Set active project to ...` Gradle task, and run `Reset active project` before
  committing.
- Build one version with `./gradlew :1.21.6-fabric:build` or `./gradlew :1.21.6-neoforge:build`.
- Start the game with `./gradlew :26.3-fabric:runClient` or `./gradlew :26.3-neoforge:runClient`.

## Project layout

| Path | Holds |
|------|-------|
| `stonecutter.properties.toml` | Every supported version, its Minecraft range and its dependencies. It drives the jar name, the Minecraft range in `fabric.mod.json` and `neoforge.mods.toml`, and the Modrinth release targets. |
| `settings.gradle.kts` | Which versions are built for which loader. |
| `build-logic` | The shared build: Java version, resources, dependencies, jar packaging and Modrinth publishing. |
| `build.fabric.gradle.kts`, `build.neoforge.gradle.kts` | The Minecraft and loader setup per loader. |

Adding a Minecraft version means adding it to `settings.gradle.kts` and `stonecutter.properties.toml`, then guarding
whatever changed with `//? if` blocks.

## Releasing

Releases are published to Modrinth from your machine, one Modrinth version per jar.

1. Set `mod.version` in `stonecutter.properties.toml`. A version ending in `-SNAPSHOT` is published as a beta.
2. Write the release notes in `CHANGELOG.md`, headed with that version. The file becomes the changelog of every
   Modrinth version, so it only holds the notes of the release being published.
3. Set `TT_MODRINTH_TOKEN` to a Modrinth [personal access token](https://modrinth.com/settings/pats) with
   *Create versions*, *Read versions*, *Write versions* and *Read user data*. The setup script opens that page,
   checks the token and stores it for new terminals:
   ```bash
   ./scripts/setup-publishing.sh
   ```
   ```powershell
   powershell -ExecutionPolicy Bypass -File scripts\setup-publishing.ps1
   ```
4. Commit and push to `main`. Nothing is uploaded with uncommitted changes, or when `HEAD` is not what `main` is on
   GitHub.
5. Run `./gradlew publishMods`.

The Minecraft versions each upload is tagged with come from `mod.mc_releases` in `stonecutter.properties.toml`.

### Dry run

Set `TT_PUBLISH_DRY_RUN` to any value to print what would be published without uploading anything. A dry run needs
no token.

```bash
TT_PUBLISH_DRY_RUN=1 ./gradlew publishMods
```
