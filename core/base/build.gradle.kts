plugins {
    id("battlerope.android.library")
    id("battlerope.android.compose")
}

android {
    namespace = "com.choiminjun.battlerope.base"
}

dependencies {
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
}
