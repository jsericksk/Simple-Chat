buildscript {
    dependencies {
        classpath(com.kproject.simplechat.Dependencies.hiltAndroidGradlePlugin)
        classpath(com.kproject.simplechat.Dependencies.googleServices)
    }
}

plugins {
    id("com.android.application") version "7.2.2" apply false
    id("com.android.library") version "7.2.2" apply false
    id("org.jetbrains.kotlin.android") version com.kproject.simplechat.Versions.KOTLIN apply false
    id("org.jetbrains.kotlin.jvm") version com.kproject.simplechat.Versions.KOTLIN apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}