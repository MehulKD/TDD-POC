plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.example.feature.login"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    implementation(project(":domain"))

    implementation(project(":core-common"))

    implementation(libs.androidx.lifecycle.viewmodel)

    implementation(libs.coroutines.core)

    implementation(libs.androidx.compose.ui)

    implementation(libs.androidx.compose.material3)

    implementation(libs.hilt.navigation.compose)

    testImplementation(kotlin("test"))
    testImplementation(libs.truth)

    testImplementation(project(":core-testing"))

    testImplementation(libs.mockk)

    testImplementation(libs.turbine)

    testImplementation(libs.coroutines.test)

    androidTestImplementation(libs.androidx.compose.ui.test)

    debugImplementation(libs.androidx.compose.ui.test.manifest)
}