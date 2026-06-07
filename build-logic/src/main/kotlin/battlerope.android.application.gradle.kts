import com.choiminjun.build.logic.configureHiltAndroid
import com.choiminjun.build.logic.configureKotlinAndroid
import com.choiminjun.build.logic.configureTest

plugins {
    id("com.android.application")
}

configureKotlinAndroid()
configureHiltAndroid()
configureTest()
