plugins {
    id("battlerope.android.library")
    id("battlerope.android.compose")
}

android {
    namespace = "com.choiminjun.battlerope.designsystem"
}

dependencies {
    implementation(libs.lottie.compose)
}
