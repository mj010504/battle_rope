plugins {
    id("battlerope.android.feature")
}

android {
    namespace = "com.choiminjun.battlerope.exercise"
}

dependencies {
    implementation(projects.core.ble)
    implementation(projects.core.navigation)
}
