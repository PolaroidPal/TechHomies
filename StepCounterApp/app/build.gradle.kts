plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.stepcounterapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.stepcounterapp"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Dagger and Hilt
    implementation (libs.hilt.android)
    kapt (libs.hilt.android.compiler)
    implementation (libs.androidx.hilt.navigation.compose)

    //ViewModel and Navigation
    implementation (libs.androidx.lifecycle.viewmodel.compose)
    implementation (libs.androidx.navigation.compose)
    implementation (libs.kotlinx.serialization.json)

    // Firebase
    implementation (libs.firebase.bom)
    implementation (libs.firebase.auth.ktx)
    implementation (libs.firebase.firestore.ktx)

    // Android credential
    implementation (libs.play.services.auth)
    implementation (libs.androidx.credentials)
    implementation (libs.googleid)

    // Window manager
    implementation (libs.androidx.window)
    implementation("androidx.compose.material3:material3-window-size-class:1.3.0")
    implementation("androidx.compose.material3.adaptive:adaptive:1.1.0-alpha04")
    implementation ("androidx.compose.material3.adaptive:adaptive-layout:1.1.0-alpha04")

    // Materials icons extended
    implementation ("androidx.compose.material:material-icons-extended: 1.8.0")

    // Coil image loading library
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // Datastore
    implementation(libs.androidx.datastore.preferences)

    // Work manager
    implementation(libs.androidx.work.runtime.ktx)
    implementation (libs.androidx.hilt.work)
}

kapt {
    correctErrorTypes = true
}