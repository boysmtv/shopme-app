# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# R8: Libraries (Firebase Admin, MinIO, Ktor) reference SLF4J statically; not needed at runtime on Android
-dontwarn org.slf4j.impl.StaticLoggerBinder
-dontwarn org.slf4j.impl.StaticMDCBinder
-dontwarn org.slf4j.impl.StaticMarkerBinder

# kotlinx.serialization
-keepattributes *Annotation*, InnerClasses
-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }
-keep,includedescriptorclasses class com.mtv.app.shopme.**$$serializer { *; }
-keepclassmembers class com.mtv.app.shopme.** { *** Companion; }
-keep,includedescriptorclasses class com.mtv.based.**$$serializer { *; }

# Hilt ViewModels
-keepclassmembers class * extends androidx.lifecycle.ViewModel { <init>(...); }

# Compose
-keepclasseswithmembers class androidx.compose.** { *; }

# Coroutines - required for R8
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}