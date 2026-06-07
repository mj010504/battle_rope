plugins {
    id("battlerope.android.library")
    id("battlerope.android.hilt")
}

android {
    namespace = "com.choiminjun.battlerope.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.common)
    implementation(projects.core.ble)
}
