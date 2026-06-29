# Keep line numbers and source file for Crashlytics readable stacktraces
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# R8: Libraries (Firebase Admin, MinIO, Ktor) reference SLF4J statically; not needed at runtime on Android
-dontwarn org.slf4j.impl.StaticLoggerBinder
-dontwarn org.slf4j.impl.StaticMDCBinder
-dontwarn org.slf4j.impl.StaticMarkerBinder

# Kotlin
-keepattributes *Annotation*, InnerClasses
-dontwarn kotlin.**

# kotlinx.serialization
-keep,includedescriptorclasses class com.mtv.app.shopme.**$$serializer { *; }
-keep,includedescriptorclasses class com.mtv.based.**$$serializer { *; }
-keepclassmembers class com.mtv.app.shopme.** { *** Companion; }
-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }

# Hilt / Dagger
-keepclassmembers class * extends androidx.lifecycle.ViewModel { <init>(...); }
-dontwarn dagger.**

# Compose
-keepclasseswithmembers class androidx.compose.** { *; }

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Retrofit / OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.**
-keepattributes Signature
-keepattributes Exceptions

# Gson / Moshi
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.mtv.app.shopme.data.remote.** { *; }
-keep class com.mtv.app.shopme.data.dto.** { *; }
-keep class com.mtv.app.shopme.data.mapper.** { *; }

# Firebase
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**

# Ktor
-dontwarn io.ktor.**

# Room
-dontwarn androidx.room.**

# MTV Core
-dontwarn com.mtv.based.**

# ThreeTenABP
-dontwarn com.jakewharton.threetenabp.**

# YouTube Player
-dontwarn com.pierfrancescosoffritti.androidyoutubeplayer.**