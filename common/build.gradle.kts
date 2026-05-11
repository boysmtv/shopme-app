plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.signing)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinx.serialization)
}

import java.util.Properties

val localProps = Properties().apply {
    val localFile = rootProject.file("local.properties")
    if (localFile.exists()) {
        localFile.inputStream().use(::load)
    }
}

fun resolveConfig(localKey: String, envKey: String, fallback: String): String {
    return localProps.getProperty(localKey)
        ?.takeIf { it.isNotBlank() }
        ?: System.getenv(envKey)
            ?.takeIf { it.isNotBlank() }
        ?: fallback
}

val debugBaseUrl = resolveConfig(
    localKey = "shopme.baseUrl",
    envKey = "SHOPME_BASE_URL",
    fallback = "http://192.168.1.104:8080/"
)
val releaseBaseUrl = resolveConfig(
    localKey = "shopme.releaseBaseUrl",
    envKey = "SHOPME_RELEASE_BASE_URL",
    fallback = "https://api.prod.com/"
)
val firebaseProjectId = resolveConfig(
    localKey = "shopme.firebaseProjectId",
    envKey = "SHOPME_FIREBASE_PROJECT_ID",
    fallback = "app-movie-e85f3"
)
val firebaseDefaultCollection = resolveConfig(
    localKey = "shopme.firebaseDefaultCollection",
    envKey = "SHOPME_FIREBASE_DEFAULT_COLLECTION",
    fallback = "users"
)

android {
    namespace = "com.mtv.app.shopme.common"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        buildConfigField("String", "FIREBASE_PROJECT_ID", "\"$firebaseProjectId\"")
        buildConfigField("String", "FIREBASE_DEFAULT_COLLECTION", "\"$firebaseDefaultCollection\"")
    }

    buildTypes {
        release {
            buildConfigField("String", "BASE_URL", "\"$releaseBaseUrl\"")
            buildConfigField("Boolean", "USE_KTOR", "false")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            buildConfigField("String", "BASE_URL", "\"$debugBaseUrl\"")
            buildConfigField("Boolean", "USE_KTOR", "true")
        }
    }

    buildFeatures {
        buildConfig = true
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
}

dependencies {
    implementation(project(":core"))

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
    implementation(libs.exifinterface)

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
