import com.choiminjun.build.logic.configureCoroutineAndroid
import com.choiminjun.build.logic.configureHiltAndroid
import com.choiminjun.build.logic.configureKotlinAndroid
import com.choiminjun.build.logic.libs
import org.gradle.kotlin.dsl.dependencies

plugins {
    id("com.android.library")
}

configureKotlinAndroid()
configureHiltAndroid()
configureCoroutineAndroid()

dependencies {
    val libs = project.extensions.libs
    implementation(libs.findLibrary("timber").get())
}
