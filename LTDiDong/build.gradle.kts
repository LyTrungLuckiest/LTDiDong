// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.4" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false


}
buildscript {
    repositories {
        google()
        mavenCentral()

    }
    dependencies {
        classpath ("com.google.gms:google-services:4.4.2") // Phiên bản mới nhất của plugin Firebase

    }
}
