plugins {
    id("battlerope.android.feature")
}

android {
    namespace = "com.choiminjun.battlerope.exercise"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.navigation)
}
