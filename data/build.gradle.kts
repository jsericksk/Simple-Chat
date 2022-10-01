import com.kproject.simplechat.Android
import com.kproject.simplechat.Dependencies

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = Android.compileSdk

    defaultConfig {
        minSdk = Android.minSdk
        targetSdk = Android.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
    namespace = "com.kproject.simplechat.data"
}

dependencies {
    // Modules
    implementation(project(Dependencies.Module.domain))
    implementation(project(Dependencies.Module.commom))

    implementation(Dependencies.coreKtx)

    // Dagger-Hilt
    implementation(Dependencies.daggerHilt)
    implementation(Dependencies.hiltNavigationCompose)
    kapt(Dependencies.hiltAndroidCompiler)

    // Coroutines for Android and Firebase
    implementation(Dependencies.kotlinxCoroutinesAndroid)
    implementation(Dependencies.kotlinxCoroutinesPlayServices)

    // Firebase
    implementation(platform(Dependencies.Firebase.firebaseBom))
    implementation(Dependencies.Firebase.auth)
    implementation(Dependencies.Firebase.firestore)
    implementation(Dependencies.Firebase.storage)

    // Retrofit
    implementation(Dependencies.retrofit)
    implementation(Dependencies.retrofitConverterGson)

    // DataStore
    implementation(Dependencies.datastorePreferences)

    // Tests
    testImplementation(Dependencies.testJunit)
}