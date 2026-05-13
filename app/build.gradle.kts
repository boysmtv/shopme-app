import com.google.firebase.appdistribution.gradle.firebaseAppDistribution

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinx.serialization)
    id("com.google.gms.google-services")
    alias(libs.plugins.firebase.appdistribution)
}

android {
    namespace = "com.mtv.app.shopme"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.mtv.app.shopme"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    val ciKeystorePath = System.getenv("SHOPME_KEYSTORE_PATH")
    val ciKeystorePassword = System.getenv("SHOPME_KEYSTORE_PASSWORD")
    val ciKeyAlias = System.getenv("SHOPME_KEY_ALIAS")
    val ciKeyPassword = System.getenv("SHOPME_KEY_PASSWORD")
    val hasCiSigning = listOf(ciKeystorePath, ciKeystorePassword, ciKeyAlias, ciKeyPassword)
        .all { !it.isNullOrBlank() }

    signingConfigs {
        if (hasCiSigning) {
            create("ciRelease") {
                storeFile = file(ciKeystorePath!!)
                storePassword = ciKeystorePassword
                keyAlias = ciKeyAlias
                keyPassword = ciKeyPassword
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            if (hasCiSigning) {
                signingConfig = signingConfigs.getByName("ciRelease")
            }
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            firebaseAppDistribution {
                artifactType = "APK"
                System.getenv("FIREBASE_APP_ID")
                    ?.takeIf { it.isNotBlank() }
                    ?.let { appId = it }
                System.getenv("FIREBASE_SERVICE_ACCOUNT_FILE")
                    ?.takeIf { it.isNotBlank() }
                    ?.let { serviceCredentialsFile = it }
                groups = System.getenv("FIREBASE_APP_DISTRIBUTION_GROUPS") ?: "internal-testers"
                releaseNotes = System.getenv("FIREBASE_RELEASE_NOTES") ?: "Shopme Android CI release"
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }

    buildFeatures {
        buildConfig = true
        compose = true
        viewBinding = true
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":common"))
    implementation(project(":core"))
    implementation(project(":nav"))
    implementation(project(":feature-customer"))
    implementation(project(":feature-seller"))
    implementation(project(":feature-auth"))
    implementation(project(":feature-firebase"))

    /* =========================
     * Core & Lifecycle
     * ========================= */
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    /* =========================
     * Kotlin Coroutines
     * ========================= */
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    /* =========================
     * Serialization
     * ========================= */
    implementation(libs.kotlinx.serialization.json)

    /* =========================
     * Networking - Retrofit / OkHttp
     * ========================= */
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.retrofit.scalars)
    implementation(libs.okhttp)

    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)

    /* =========================
     * Networking - Gson (Legacy / Optional)
     * ========================= */
    implementation(libs.gson)
    implementation(libs.retrofit.converter.gson)

    /* =========================
     * Networking - Ktor Client
     * ========================= */
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)

    /* =========================
     * Dependency Injection (Hilt)
     * ========================= */
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    /* =========================
     * Jetpack Compose
     * ========================= */
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.material.icons.extended)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.coil.compose)

    /* =========================
     * Date & Time
     * ========================= */
    implementation(libs.threetenabp)

    /* =========================
     * Firebase
     * ========================= */
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.common.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.auth.interop)
    implementation(libs.firebase.messaging.ktx)

    /* =========================
     * Internal / Maven Local Libraries
     * ========================= */

    // MTV Core
    implementation(libs.mtv.core.network)
    implementation(libs.mtv.core.provider)

    // MTV UI Components
    implementation(libs.mtv.ui.component)
    implementation(libs.mtv.ui.ui)
    implementation(libs.mtv.ui.theme)

    /* =========================
     * Testing
     * ========================= */
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}
