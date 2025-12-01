// In build.gradle.kts (Kotlin DSL)
plugins {
    alias(libs.plugins.android.application) apply false
    id("com.google.gms.google-services") version "4.4.4"
}