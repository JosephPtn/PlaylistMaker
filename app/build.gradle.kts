plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
    id("kotlin-kapt")
    kotlin("plugin.serialization") version "1.5.0"
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.saturnnetwork.playlistmaker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.saturnnetwork.playlistmaker"
        minSdk = 29
        targetSdk = 35
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
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.room.common.jvm)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.cardview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.material.v161)
    implementation(libs.glide)
    annotationProcessor(libs.compiler)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation("io.insert-koin:koin-android:4.1.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    implementation("androidx.fragment:fragment-ktx:1.5.6")

    // Основная библиотека Room
    // @Entity
    // @Dao
    // @Database
    // сам движок SQLite
    implementation("androidx.room:room-runtime:2.7.0")
    // Компилятор аннотаций
    // генерирует код на основе @Dao, @Entity, @Query
    // без него проект не соберётся, он требует id("kotlin-kapt")
    kapt("androidx.room:room-compiler:2.7.0")
    // Kotlin extensions (coroutines)
    // Добавляет:
    // suspend функции в DAO
    // поддержку Flow
    // корутины вместо колбэков
    implementation("androidx.room:room-ktx:2.7.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

}