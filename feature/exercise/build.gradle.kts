plugins {
    id("makeitall.android.feature")
}

android {
    namespace = "com.choiminjun.makeitall.exercise"
}

dependencies {
    implementation(projects.core.ble)
    implementation(projects.core.navigation)
}
