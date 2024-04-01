plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
//    id("com.google.devtools.ksp")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.staticapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.staticapp"
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
    compileOptions {

        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {

        viewBinding = true

    }


}

kapt {
    correctErrorTypes = true
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //todo for view & text size
    implementation("com.intuit.sdp:sdp-android:1.1.0")
    implementation("com.intuit.ssp:ssp-android:1.1.0")

    //for hilt
    implementation("com.google.dagger:hilt-android:2.51")
    kapt("com.google.dagger:hilt-android-compiler:2.51")
//    kapt ("androidx.hilt:hilt-compiler:1.2.0")
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
//    annotationProcessor("android.arch.persistence.room:compiler:1.1.1")
    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$room_version")
//    ksp("androidx.room:room-compiler:2.5.0")
    implementation  ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("androidx.room:room-ktx:$room_version")
//    implementation ("com.klinkerapps:android-sms:5.2.5")
//    implementation ("ir.mtajik.android:advancedsmsmanager:1.1.0")
    implementation ("com.klinkerapps:android-smsmms:5.2.6")
    implementation ("com.ringcentral:ringcentral:2.8.4")
    implementation ("androidx.work:work-runtime-ktx:2.7.0-alpha05")
//    implementation ("org.smslib:smslib-android:5.3.0")

    // To use Kotlin Symbol Processing (KSP)
//    ksp("androidx.room:room-compiler:$room_version")
//    implementation ("androidx.recyclerview:recyclerview:1.3.2")
//    implementation ("com.twilio.sdk:twilio:8.22.0")
//    implementation ("com.twilio:voice-android:5.7.2")
//    implementation ("com.twilio.sdk:twilio:8.22.0")
//    implementation ("com.vonage.android:client-sdk:2.0.0")
}