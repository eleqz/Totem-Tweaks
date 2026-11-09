import totemtweaks.MinecraftArtifactsLock

plugins {
    id("net.neoforged.moddev")
    id("totemtweaks.mod")
}

neoForge {
    version = sc.properties.getOrNull<String>("deps.neoforge_build") ?: sc.properties.get<String>("deps.neoforge")

    mods {
        register(sc.properties.get<String>("mod.id")) {
            sourceSet(sourceSets.main.get())
        }
    }

    runs {
        register("client") {
            client()
            gameDirectory = rootProject.file("run/neoforge")
        }
    }
}

val minecraftArtifactsLock = gradle.sharedServices.registerIfAbsent("minecraftArtifacts", MinecraftArtifactsLock::class) {
    maxParallelUsages = 1
}

tasks.named("createMinecraftArtifacts") {
    usesService(minecraftArtifactsLock)
    dependsOn("stonecutterGenerate")
}
