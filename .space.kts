/**
 * JetBrains Space Automation
 * This Kotlin-script file lets you automate build activities
 * For more info, see https://www.jetbrains.com/help/space/automation.html
 */

job("Build and publish") {
    container(displayName = "Build & publish", image = "gradle") {
        kotlinScript { api ->
            api.gradle("coal:build")
            api.gradle("coal:publish")
        }
    }
    startOn {
        gitPush { enabled = true }
    }
}