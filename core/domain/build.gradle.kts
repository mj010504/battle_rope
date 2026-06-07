plugins {
    id("battlerope.kotlin.library")
    id("battlerope.kotlin.hilt")
}

dependencies {
    implementation(projects.core.common)
    implementation(libs.kotlinx.coroutines.core)
}
