/**
 * JetBrains Space Automation
 * This Kotlin-script file lets you automate build activities
 * For more info, see https://www.jetbrains.com/help/space/automation.html
 */

job("Build and publish") {
    container(displayName = "Build & publish", image = "gradle") {
        kotlinScript { api ->
            api.gradle("coal:build")
            // api.gradle("coal:publish") // cant do this currently, due to permission errors
            // This will be fixed if we begin to pay JB money
        }
    }
    startOn {
        gitPush { enabled = true }
    }
}