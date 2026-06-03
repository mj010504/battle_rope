plugins {
    id("makeitall.android.library")
    id("makeitall.android.hilt")
}

android {
    namespace = "com.choiminjun.makeitall.ble"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.domain)
}
