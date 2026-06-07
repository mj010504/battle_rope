import com.choiminjun.build.logic.configureHiltAndroid
import com.choiminjun.build.logic.libs
import org.gradle.kotlin.dsl.dependencies

plugins {
    id("battlerope.android.library")
    id("battlerope.android.compose")
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

configureHiltAndroid()

dependencies {
    implementation(project(":core:base"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:domain"))
    implementation(project(":core:common"))

    val libs = project.extensions.libs
    implementation(libs.findLibrary("hilt-navigation-compose").get())
    implementation(libs.findLibrary("androidx-navigation-compose").get())
    implementation(libs.findLibrary("androidx-activity-compose").get())
    implementation(libs.findLibrary("androidx-lifecycle-viewmodel-ktx").get())
    implementation(libs.findLibrary("androidx-lifecycle-runtime-compose").get())
}
