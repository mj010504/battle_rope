plugins {
    id("makeitall.android.feature")
}

android {
    namespace = "com.choiminjun.makeitall.home"
}

dependencies {
    implementation(projects.core.navigation)
}
