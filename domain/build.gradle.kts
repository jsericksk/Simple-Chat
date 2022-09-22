import com.kproject.simplechat.Dependencies

plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    // Modules
    implementation(project(Dependencies.Module.commom))

    // Coroutines
    implementation(Dependencies.kotlinxCoroutines)

    testImplementation(Dependencies.testJunit)
}