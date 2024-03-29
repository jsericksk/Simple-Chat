package com.kproject.simplechat

object Dependencies {
    object Module {
        const val domain = ":domain"
        const val data = ":data"
        const val commom = ":commom"
    }

    object Plugin {
        const val androidApplication = "com.android.application"
        const val androidLibrary = "com.android.library"
        const val kotlin = "org.jetbrains.kotlin.android"
        const val kapt = "kotlin-kapt"
        const val daggerHilt = "dagger.hilt.android.plugin"
        const val googlePlayServices = "com.google.gms.google-services"
    }

    const val coreKtx = "androidx.core:core-ktx:${Versions.CORE_KTX}"
    const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.LIFECYCLE_RUNTIME_KTX}"
    const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.LIFECYCLE_EXTENSIONS}"
    const val lifecycleViewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.LIFECYCLE_VIEWMODEL_COMPOSE}"
    const val activityCompose = "androidx.activity:activity-compose:${Versions.ACTIVITY_COMPOSE}"
    const val datastorePreferences = "androidx.datastore:datastore-preferences:${Versions.DATASTORE_PREFERENCES}"

    const val composeUi = "androidx.compose.ui:ui:${Versions.COMPOSE}"
    const val composeMaterial = "androidx.compose.material:material:${Versions.COMPOSE}"
    const val composeUiToolingPreview = "androidx.compose.ui:ui-tooling-preview:${Versions.COMPOSE}"

    const val constraintLayoutCompose = "androidx.constraintlayout:constraintlayout-compose:${Versions.CONSTRAINT_LAYOUT_COMPOSE}"

    object Firebase {
        const val firebaseBom = "com.google.firebase:firebase-bom:${Versions.FIREBASE_BOM}"
        const val auth = "com.google.firebase:firebase-auth-ktx"
        const val firestore = "com.google.firebase:firebase-firestore-ktx"
        const val storage = "com.google.firebase:firebase-storage-ktx"
        const val messaging = "com.google.firebase:firebase-messaging-ktx"
        const val analytics = "com.google.firebase:firebase-analytics"
    }

    const val googleServices = "com.google.gms:google-services:${Versions.GOOGLE_SERVICES}"

    // Dagger-Hilt
    const val daggerHilt = "com.google.dagger:hilt-android:${Versions.DAGGER_HILT}"
    const val hiltNavigationCompose = "androidx.hilt:hilt-navigation-compose:${Versions.HILT_NAVIGATION_COMPOSE}"
    const val hiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:${Versions.DAGGER_HILT}"
    const val hiltAndroidGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.DAGGER_HILT}"

    // Coroutines
    const val kotlinxCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.KOTLIN_COROUTINES}"
    const val kotlinxCoroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.KOTLIN_COROUTINES}"
    const val kotlinxCoroutinesPlayServices = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.KOTLIN_COROUTINES_PLAY_SERVICES}"

    const val navigationCompose = "androidx.navigation:navigation-compose:${Versions.NAVIGATION_COMPOSE}"

    // Accompanist
    const val accompanistNavigationAnimation = "com.google.accompanist:accompanist-navigation-animation:${Versions.ACCOMPANIST}"
    const val accompanistPager = "com.google.accompanist:accompanist-pager:${Versions.ACCOMPANIST}"
    const val accompanistPagerIndicator = "com.google.accompanist:accompanist-pager-indicators:${Versions.ACCOMPANIST}"

    // Landscapist
    const val landscapist = "com.github.skydoves:landscapist-coil:${Versions.LANDSCAPIST}"

    // Retrofit
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
    const val retrofitConverterGson = "com.squareup.retrofit2:converter-gson:${Versions.RETROFIT}"

    // Gson
    const val gson = "com.google.code.gson:gson:${Versions.GSON}"

    const val splashScreen = "androidx.core:core-splashscreen:${Versions.SPLASH_SCREEN}"

    // Tests
    const val testJunit = "junit:junit:${Versions.TEST_JUNIT}"
    const val androidTestJunit = "androidx.test.ext:junit:${Versions.ANDROID_TEST_JUNIT}"
    const val androidTestEspresso = "androidx.test.espresso:espresso-core:${Versions.ANDROID_TEST_ESPRESSO_CORE}"
    const val androidTestComposeUi = "androidx.compose.ui:ui-test-junit4:${Versions.COMPOSE}"
    const val composeUiTooling = "androidx.compose.ui:ui-tooling:${Versions.COMPOSE}"
    const val composeUiTestManifest = "androidx.compose.ui:ui-test-manifest:${Versions.COMPOSE}"
}