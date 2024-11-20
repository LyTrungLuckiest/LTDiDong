plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.btlon"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.btlon"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.navigation:navigation-fragment:2.8.4")
    implementation("androidx.navigation:navigation-ui:2.8.4")
    implementation("com.android.volley:volley:1.2.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")


    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("androidx.activity:activity:1.9.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.4")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.4")
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation("com.google.firebase:firebase-analytics")
    // Firebase Authentication
    implementation("com.google.firebase:firebase-auth:23.1.0")

// Google Sign-In
    implementation("com.google.android.gms:play-services-auth:21.2.0")

// Facebook SDK
    implementation("com.facebook.android:facebook-android-sdk:16.0.1")
    implementation ("com.facebook.android:facebook-android-sdk:[latest_version]")


    implementation ("com.facebook.android:facebook-login:16.0.1")
    implementation ("com.facebook.android:facebook-login:latest.release")
// Đảm bảo bạn thêm đúng phiên bản Facebook SDK



        implementation ("com.facebook.android:facebook-login:15.0.0")  // Đảm bảo bạn thêm đúng phiên bản Facebook SDK




}

