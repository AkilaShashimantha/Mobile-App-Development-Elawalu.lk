plugins {
    alias(libs.plugins.android.application)

}

android {
    namespace = "com.example.mobileapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mobileapp"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")

    implementation(platform("com.google.firebase:firebase-bom:33.8.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth:22.1.2")
    implementation("com.google.firebase:firebase-database:20.2.1")

    implementation("androidx.browser:browser:1.7.0")

    implementation("com.google.android.gms:play-services-auth:20.2.0")
    implementation ("com.airbnb.android:lottie:4.0.0")

    implementation("com.github.PayHereDevs:payhere-android-sdk:v3.0.17")
    implementation("androidx.appcompat:appcompat:1.6.0")
    implementation("com.google.code.gson:gson:2.8.0")
    implementation("com.google.android.material:material:1.9.0")


    testImplementation("junit:junit:4.12")
    androidTestImplementation("com.android.support.test:runner:1.0.1")

}