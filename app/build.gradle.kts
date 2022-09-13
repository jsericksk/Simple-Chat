import com.kproject.simplechat.Versions
import com.kproject.simplechat.Dependencies
import com.kproject.simplechat.Android

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
}

android {
    compileSdk = Android.compileSdk

    defaultConfig {
        applicationId = "com.kproject.simplechat"
        minSdk = Android.minSdk
        targetSdk = Android.targetSdk
        versionCode = Android.versionCode
        versionName = Android.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
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

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.COMPOSE_COMPILER
    }

    packagingOptions {
        resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
    }
}

dependencies {
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.composeMaterial)
    implementation(Dependencies.composeUi)
    implementation(Dependencies.composeUiToolingPreview)

    implementation(Dependencies.lifecycleRuntime)
    implementation(Dependencies.lifecycleExtensions)
    implementation(Dependencies.lifecycleViewModelCompose)
    implementation(Dependencies.activityCompose)
    implementation(Dependencies.datastorePreferences)

    // Firebase
    implementation(platform(Dependencies.Firebase.firebaseBom))
    implementation(Dependencies.Firebase.auth)
    implementation(Dependencies.Firebase.firestore)
    implementation(Dependencies.Firebase.storage)
    implementation(Dependencies.Firebase.messaging)

    // Dagger-Hilt
    implementation(Dependencies.daggerHilt)
    implementation(Dependencies.hiltNavigationCompose)
    kapt(Dependencies.hiltAndroidCompiler)

    // Coroutines for Android and Firebase
    implementation(Dependencies.kotlinxCoroutinesAndroid)
    implementation(Dependencies.kotlinxCoroutinesPlayServices)

    // Navigation Compose
    implementation(Dependencies.navigationCompose)

    // Accompanist Navigation Animation and Pager
    implementation(Dependencies.accompanistNavigationAnimation)
    implementation(Dependencies.accompanistPager)

    // Coil
    implementation(Dependencies.coil)

    // Retrofit
    implementation(Dependencies.retrofit)
    implementation(Dependencies.retrofitConverterGson)

    testImplementation(Dependencies.testJunit)
    androidTestImplementation(Dependencies.androidTestJunit)
    androidTestImplementation(Dependencies.androidTestEspresso)
    androidTestImplementation(Dependencies.androidTestComposeUi)
    debugImplementation(Dependencies.composeUiTooling)
    debugImplementation(Dependencies.composeUiTestManifest)
}