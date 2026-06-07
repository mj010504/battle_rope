plugins {
    id("battlerope.android.feature")
}

android {
    namespace = "com.choiminjun.battlerope.competition"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.navigation)
    implementation(libs.lottie.compose)
}
