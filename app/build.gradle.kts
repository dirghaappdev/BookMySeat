plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.dirgha.bookmyseat"

    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.dirgha.bookmyseat"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // =========================================================
    // ENVIRONMENT FLAVORS
    // =========================================================

    flavorDimensions += "environment"

    productFlavors {

        create("dev") {
            dimension = "environment"

            buildConfigField(
                "String",
                "BASE_URL",
                "\"http://80.225.211.192:8001/\""
            )

            resValue(
                "string",
                "app_name",
                "BookMySeat DEV"
            )
        }

        create("uat") {
            dimension = "environment"

            buildConfigField(
                "String",
                "BASE_URL",
                "\"http://80.225.211.192:8002/\""
            )

            resValue(
                "string",
                "app_name",
                "BookMySeat UAT"
            )
        }

        create("prod") {
            dimension = "environment"

            buildConfigField(
                "String",
                "BASE_URL",
                "\"http://80.225.211.192:8000/\""
            )

            resValue(
                "string",
                "app_name",
                "BookMySeat"
            )
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
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
        compose = true

        // Required for custom BuildConfig fields
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui.text)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.runtime.saveable)

    implementation(libs.androidx.ui)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.runtime)
    implementation(libs.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation("androidx.navigation:navigation-compose:2.8.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")

    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    implementation("androidx.datastore:datastore-preferences:1.1.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    implementation("androidx.compose.material:material-icons-extended")

    implementation(platform("com.google.firebase:firebase-bom:34.0.0"))
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-analytics")

    implementation("com.google.code.gson:gson:2.10.1")
}

