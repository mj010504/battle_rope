import com.choiminjun.build.logic.configureKotlin
import com.choiminjun.build.logic.configureTest
import com.choiminjun.build.logic.libs
import org.gradle.kotlin.dsl.kotlin

plugins {
    kotlin("jvm")
}

configureKotlin()
configureTest()

dependencies {
    val libs = project.extensions.libs
    "detektPlugins"(libs.findLibrary("detekt.formatting").get())
    implementation(libs.findLibrary("kotlinx-collections-immutable").get())
}
