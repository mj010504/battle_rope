plugins {
    id("makeitall.android.application")
    id("makeitall.android.compose")
}

android {
    namespace = "com.choiminjun.makeitall"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.choiminjun.makeitall"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.designsystem)
    implementation(projects.core.domain)
    implementation(projects.core.navigation)
    implementation(projects.feature.competition)
    implementation(projects.feature.home)
    implementation(projects.feature.exercise)
    implementation(libs.timber)
}
