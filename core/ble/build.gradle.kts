plugins {
    id("battlerope.android.library")
    id("battlerope.android.hilt")
}

android {
    namespace = "com.choiminjun.battlerope.ble"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.domain)
}
