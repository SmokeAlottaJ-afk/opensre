# Add project specific ProGuard rules here.
# By default, the Android Gradle plugin already adds the standard ProGuard
# rules files that are packaged with the Android SDK:
#   - android/sdk/tools/proguard/proguard-android.txt
#   - android/sdk/tools/proguard/proguard-android-optimize.txt

# Keep names of activities, services, and broadcast receivers
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver

# Keep Media3 classes
-keep class androidx.media3.** { *; }

# Keep Retrofit service interfaces and their annotations
-keep class com.videocut.editor.network.** { *; }

# Keep Room database classes
-keep class androidx.room.** { *; }
-keep class * extends androidx.room.Database { *; }
-keep class * extends androidx.room.Entity { *; }
-keep class * extends androidx.room.Dao { *; }

# Keep Hilt injection points
-keep class * implements dagger.MembersInjector { *; }
-keep class * implements javax.inject.Provider { *; }
-keep class * implements javax.inject.Inject { *; }

# Keep Compose functions
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.compose.ui.** { *; }
-keep class androidx.compose.material3.** { *; }

# Keep data classes for Gson
-keepclassmembers class ** {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Keep Kotlin coroutines
-keep class kotlinx.coroutines.** { *; }

# Keep AndroidX lifecycle
-keep class androidx.lifecycle.** { *; }

# Keep navigation
-keep class androidx.navigation.** { *; }

# Keep all activities in the application namespace
-keep class com.videocut.editor.** { *; }

# Keep R classes
-keep class com.videocut.editor.R { *; }

# Ignore warnings for unused classes
-dontwarn okhttp3.**
-dontwarn retrofit2.**
-dontwarn com.google.gson.**
-dontwarn androidx.room.**
-dontwarn dagger.**
-dontwarn javax.inject.**

# Optimizations
-optimizations !code/simplification/arithmetic
-optimizationpasses 5
-allowaccessmodification

# Keep names of classes that implement Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
}
