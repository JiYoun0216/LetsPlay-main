plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.letsplay"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.letsplay"
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

    // Retrofit, Gson, OkHttp 라이브러리
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // Retrofit 라이브러리
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // JSON 응답 처리
    implementation("com.squareup.okhttp3:okhttp:4.9.3") // OkHttp 클라이언트

}