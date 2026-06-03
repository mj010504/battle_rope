plugins {
    id("makeitall.kotlin.library")
    id("makeitall.kotlin.hilt")
}

dependencies {
    implementation(projects.core.common)
    implementation(libs.kotlinx.coroutines.core)
}
