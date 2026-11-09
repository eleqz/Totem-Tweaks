package totemtweaks

import me.modmuss50.mpp.PublishModTask

val publishDryRun = isPublishDryRun

// Runs before every upload, so a missing token cannot leave a release on only some versions.
tasks.register("checkPublishSecrets") {
    group = "publishing"
    description = "Fails unless the Modrinth token is set."
    val token = providers.environmentVariable("TT_MODRINTH_TOKEN")
    onlyIf { !publishDryRun }
    doLast {
        check(token.isPresent) { "Cannot publish, TT_MODRINTH_TOKEN is not set" }
    }
}

tasks.register("checkReleaseCommit") {
    group = "publishing"
    description = "Fails unless HEAD is committed and pushed to origin/main."
    val head = git("rev-parse", "HEAD")
    val changes = git("status", "--porcelain", "--untracked-files=no")
    val remoteMain = git("ls-remote", "origin", "refs/heads/main")
    onlyIf { !publishDryRun }
    doLast {
        check(changes.get().isEmpty()) { "Cannot publish with uncommitted changes:\n${changes.get()}" }
        val remote = remoteMain.get().substringBefore('\t')
        check(remote.isNotEmpty() && remote == head.get()) {
            "Cannot publish, HEAD ${head.get()} is not origin/main ${remote.ifEmpty { "(unreachable)" }}. Push it first."
        }
    }
}

// Modrinth lists the latest upload first, so the newest Minecraft versions go last, and for a range both loaders
// support Fabric goes after NeoForge. Without this, parallel execution uploads in any order.
val uploadOrder = compareBy(minecraftVersionOrder) { jar: Project -> jar.name.substringBefore('-') }
    .thenByDescending { it.name.substringAfter('-') }
subprojects.sortedWith(uploadOrder).zipWithNext { previous, next ->
    next.tasks.withType<PublishModTask>().configureEach { mustRunAfter(previous.tasks.withType<PublishModTask>()) }
}
