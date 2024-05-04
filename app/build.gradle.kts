plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
    alias(libs.plugins.googleAndroidLibrariesMapsplatformSecretsGradlePlugin)
}

android {
    namespace = "com.example.restaurantmanager"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.restaurantmanager"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "2.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
//            isMinifyEnabled = false
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
    buildFeatures {
        viewBinding = true
    }

}
//lưu nếu sử dụng thư viện ngoài
dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.play.services.maps)
    implementation("com.squareup.picasso:picasso:2.8")
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    implementation(libs.annotation)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.auth)
    //map
    implementation ("org.osmdroid:osmdroid-android:6.1.14")
    implementation ("com.android.volley:volley:1.1.1")
    //MPAndroidChart
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    //gif
    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.28")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    //anh tron
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    //add ZXing
    implementation("com.google.zxing:core:3.4.1")
    implementation("com.journeyapps:zxing-android-embedded:4.2.0")
//    implementation(libs.firebase.messaging)
    implementation("com.google.firebase:firebase-messaging:21.1.0")
    implementation(libs.firebase.inappmessaging.display)
    implementation("com.google.firebase:firebase-inappmessaging:20.0.0")
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")
    //add sqlite
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
