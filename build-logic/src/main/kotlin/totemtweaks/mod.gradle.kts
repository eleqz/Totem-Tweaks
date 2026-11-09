package totemtweaks

import me.modmuss50.mpp.PublishModTask
import me.modmuss50.mpp.ReleaseType

plugins {
    java
    id("me.modmuss50.mod-publish-plugin")
}

val modVersion = prop("mod.version")
val minecraftRange = prop("mod.mc_range")
val hasYacl = propOrNull("deps.yacl") != null

version = "$modVersion+mc$minecraftRange-$loader"
group = prop("mod.group")
base.archivesName = prop("mod.archive")

java {
    targetCompatibility = requiredJava
    sourceCompatibility = requiredJava
    toolchain {
        vendor = JvmVendorSpec.ADOPTIUM
        languageVersion = JavaLanguageVersion.of(requiredJava.majorVersion)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = Charsets.UTF_8.name()
    options.release = requiredJava.majorVersion.toInt()
}

// Exclusive, because the loader plugins add their repositories first and one 5xx from those fails the whole build.
repositories {
    exclusiveContent {
        forRepository { maven("https://maven.terraformersmc.com/releases") { name = "TerraformersMC" } }
        filter { includeGroup("com.terraformersmc") }
    }
    exclusiveContent {
        forRepository { maven("https://maven.isxander.dev/releases") { name = "Xander Maven" } }
        filter { includeGroup("dev.isxander") }
    }
    exclusiveContent {
        forRepository { maven("https://maven.shedaniel.me/") { name = "Shedaniel" } }
        filter { includeGroup("me.shedaniel.cloth") }
    }
    exclusiveContent {
        forRepository { maven("https://maven.quiltmc.org/repository/release") { name = "Quilt" } }
        filter { includeGroup("org.quiltmc.parsers") }
    }
}

dependencies {
    val compileOnly = if (isFabric) "modCompileOnly" else "compileOnly"
    val devRuntime = if (isFabric) "modLocalRuntime" else "runtimeOnly"
    val integrations = listOfNotNull(
        propOrNull("deps.yacl")?.let { "dev.isxander:yet-another-config-lib:$it-$loader" },
        propOrNull("deps.modmenu")?.let { "com.terraformersmc:modmenu:$it" },
    )
    // Both jars nest their own libraries, and resolving their poms needs repositories this build does not use.
    for (notation in integrations) {
        compileOnly(notation) { isTransitive = false }
        devRuntime(notation) { isTransitive = false }
    }
    // Only compiled against, so the game started from the IDE shows the YetAnotherConfigLib screen.
    compileOnly("me.shedaniel.cloth:cloth-config-$loader:${prop("deps.cloth")}") { isTransitive = false }
    // Loom drops the libraries a mod nests when it remaps it, which it only does before 26.1.
    if (isFabric && hasYacl && sc.current.parsed < "26.1") {
        for (library in listOf("json", "gson")) "localRuntime"("org.quiltmc.parsers:$library:0.2.1")
    }
}

tasks.processResources {
    val props = mapOf(
        "id" to prop("mod.id"),
        "name" to prop("mod.name"),
        "version" to modVersion,
        "minecraft" to prop("mod.mc_compat"),
    ) + if (isFabric) {
        mapOf(
            "loader" to prop("deps.fabric_loader"),
            "fabric_api" to if (sc.current.parsed < "1.20.2") "fabric" else "fabric-api",
        )
    } else {
        mapOf("neoforge" to prop("deps.neoforge"))
    }
    val mixinJava = "JAVA_${requiredJava.majorVersion}"
    // Before 1.20.5 NeoForge reads mods.toml and still knows displayTest.
    val legacyNeoForge = sc.current.parsed < "1.20.5"

    inputs.properties(props)
    inputs.property("yacl", hasYacl)
    inputs.property("mixinJava", mixinJava)

    if (isFabric) exclude("META-INF/neoforge.mods.toml") else exclude("fabric.mod.json")

    filesMatching("fabric.mod.json") {
        expand(props)
        if (!hasYacl) filter { line -> line.takeUnless { "yet_another_config_lib_v3" in it } }
    }
    filesMatching("META-INF/neoforge.mods.toml") {
        expand(props)
        if (legacyNeoForge) name = "mods.toml" else filter { line -> line.takeUnless { it.startsWith("displayTest") } }
    }
    // The file holds a real level so the IDE can resolve it.
    filesMatching("*.mixins.json") { filter { line -> line.replace("JAVA_17", mixinJava) } }
}

tasks.withType<Jar>().configureEach {
    from(rootProject.file("LICENSE")) { rename { "${it}_${base.archivesName.get()}" } }
}

tasks.register<Copy>("buildAndCollect") {
    group = "build"
    description = "Builds this version and copies its jar to the root build/libs."
    dependsOn("build")
    from(modJarFile)
    into(rootProject.layout.buildDirectory.dir("libs"))
}

val releaseTargets = sc.properties.rawOrNull("mod", "mc_releases")?.asList().orEmpty().map { it.toString() }
require(releaseTargets.isNotEmpty()) { "mod.mc_releases is missing for ${project.name} in stonecutter.properties.toml" }

tasks.withType<PublishModTask>().configureEach {
    dependsOn(rootProject.tasks.named("checkPublishSecrets"), rootProject.tasks.named("checkReleaseCommit"))
}

publishMods {
    dryRun = isPublishDryRun
    file = modJarFile
    version = project.version.toString()
    displayName = "${prop("mod.name")} $modVersion for ${if (isFabric) "Fabric" else "NeoForge"} $minecraftRange"
    changelog = providers.fileContents(rootProject.layout.projectDirectory.file("CHANGELOG.md")).asText
    type = if (modVersion.endsWith("-SNAPSHOT")) ReleaseType.BETA else ReleaseType.STABLE
    modLoaders.add(loader)

    modrinth {
        accessToken = providers.environmentVariable("TT_MODRINTH_TOKEN")
        projectId = "1cfO6J6t"
        minecraftVersions.addAll(releaseTargets)
        // Unset, Modrinth silently takes whatever the project page says.
        environment = CLIENT_ONLY
        if (isFabric) {
            requires("fabric-api")
            optional("modmenu")
        }
        if (hasYacl) optional("yacl")
        optional("cloth-config")
    }
}
