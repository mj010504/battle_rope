plugins {
    id("makeitall.android.library")
    id("makeitall.android.hilt")
}

android {
    namespace = "com.choiminjun.makeitall.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.common)
    implementation(projects.core.ble)
}
