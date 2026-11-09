plugins {
    id("dev.kikugie.loom-back-compat")
    id("totemtweaks.mod")
}

val fabricApiVersion = sc.properties.get<String>("deps.fabric_api")

dependencies {
    minecraft("com.mojang:minecraft:${sc.current.version}")
    loomx.applyMojangMappings()

    modImplementation("net.fabricmc:fabric-loader:${sc.properties.get<String>("deps.fabric_loader")}")
    modImplementation(fabricApi.module("fabric-command-api-v2", fabricApiVersion))
    modLocalRuntime("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")
}

loom {
    runConfigs.all {
        preferGradleTask = true
        generateRunConfig = true
        runDirectory = rootProject.file("run")
    }
}
