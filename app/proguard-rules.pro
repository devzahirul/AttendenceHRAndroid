# Kotlin
-keepclassmembers class kotlin.Metadata {
    *** MODULE_NAME;
}

-keepclasses class kotlin.Metadata

# Coroutines
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# Retrofit & OkHttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp3.** { *; }
-keep interface com.squareup.okhttp3.** { *; }
-dontwarn com.squareup.okhttp3.**
-dontnote com.squareup.okhttp3.**
-keep class retrofit2.** { *; }
-keepclasseswithmembers class retrofit2.** { *; }
-dontwarn retrofit2.**
-keepattributes InnerClasses

# Serialization
-keep class kotlinx.serialization.** { *; }
-keep interface kotlinx.serialization.** { *; }
-keepclasseswithmembers class ** {
    kotlinx.serialization.SerializableKind *;
}
-keepclasseswithmembers class ** {
    *** *_serializer(...);
}

# Room
-keep class androidx.room.** { *; }
-keepclasesmembers class androidx.room.** { *; }

# Hilt
-keep class dagger.hilt.** { *; }
-keepclasseswithmembernames class * {
    @dagger.hilt.* <methods>;
}
-keepclasseswithmembernames class * {
    @dagger.hilt.* <init>(...);
}

# Compose
-keep class androidx.compose.** { *; }

# Android Jetpack
-keep class androidx.** { *; }
-dontwarn androidx.**

# Data classes
-keepclassmembers class * {
    *** component1();
    *** component2();
    *** component3();
    *** component4();
    *** component5();
}

# Enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Remove logging
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}
