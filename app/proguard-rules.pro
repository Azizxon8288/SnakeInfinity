# AdMob
-keep class com.google.android.gms.ads.** { *; }

# Kotlinx Serialization (for local leaderboard JSON)
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }
-keep,includedescriptorclasses class com.snakeinfinity.game.**$$serializer { *; }
-keepclassmembers class com.snakeinfinity.game.** { *** Companion; }
-keepclasseswithmembers class com.snakeinfinity.game.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Koin
-keep class org.koin.** { *; }
