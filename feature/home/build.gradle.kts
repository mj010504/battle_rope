plugins {
    id("battlerope.android.feature")
}

android {
    namespace = "com.choiminjun.battlerope.home"
}

dependencies {
    implementation(projects.core.navigation)
}
