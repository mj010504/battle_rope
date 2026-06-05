plugins {
    id("makeitall.android.feature")
}

android {
    namespace = "com.choiminjun.makeitall.competition"
}

dependencies {
    implementation(projects.core.ble)
    implementation(projects.core.domain)
    implementation(projects.core.navigation)
    implementation(libs.lottie.compose)
}
