plugins {
    id("makeitall.android.library")
    id("makeitall.android.compose")
}

android {
    namespace = "com.choiminjun.makeitall.designsystem"
}

dependencies {
    implementation(libs.lottie.compose)
}
