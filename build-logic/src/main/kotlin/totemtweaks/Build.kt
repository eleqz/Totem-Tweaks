package totemtweaks

import dev.kikugie.loomx.LoomCompatProjectExtension
import dev.kikugie.stonecutter.build.StonecutterBuildExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named

val Project.sc: StonecutterBuildExtension
    get() = extensions.getByType()

fun Project.prop(key: String): String = sc.properties.get<String>(key)

fun Project.propOrNull(key: String): String? = sc.properties.getOrNull<String>(key)

val Project.loader: String
    get() = sc.current.project.substringAfter('-')

val Project.isFabric: Boolean
    get() = loader == "fabric"

val Project.requiredJava: JavaVersion
    get() = when {
        sc.current.parsed >= "26.1" -> JavaVersion.VERSION_25
        sc.current.parsed >= "1.20.5" -> JavaVersion.VERSION_21
        else -> JavaVersion.VERSION_17
    }

val Project.modJarFile: Provider<RegularFile>
    get() = if (isFabric) {
        extensions.getByType<LoomCompatProjectExtension>().modJar.flatMap { it.archiveFile }
    } else {
        tasks.named<Jar>("jar").flatMap { it.archiveFile }
    }

val Project.isPublishDryRun: Boolean
    get() = providers.environmentVariable("TT_PUBLISH_DRY_RUN").isPresent

fun Project.git(vararg arguments: String): Provider<String> = providers.exec {
    workingDir = rootDir
    commandLine(listOf("git") + arguments)
    isIgnoreExitValue = true
}.standardOutput.asText.map { it.trim() }

val minecraftVersionOrder: Comparator<String> = Comparator { left, right ->
    val a = left.split('.').map(String::toInt)
    val b = right.split('.').map(String::toInt)
    (0 until maxOf(a.size, b.size))
        .map { a.getOrElse(it) { 0 }.compareTo(b.getOrElse(it) { 0 }) }
        .firstOrNull { it != 0 } ?: 0
}

// Decompiling Minecraft for several NeoForge versions at once exhausts memory.
interface MinecraftArtifactsLock : BuildService<BuildServiceParameters.None>
