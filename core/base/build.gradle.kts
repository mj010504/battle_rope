plugins {
    id("makeitall.android.library")
    id("makeitall.android.compose")
}

android {
    namespace = "com.choiminjun.makeitall.base"
}

dependencies {
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
}
