# VideoCut - Professional Video Editor

A modern, CapCut-like video editing application for Android, built with Kotlin, Jetpack Compose, and Material 3.

## Features

### Core Editing
- **Multi-track Timeline**: Edit video, audio, and text on separate tracks
- **Clip Management**: Add, delete, reorder, trim, and split video clips
- **Audio Editing**: Import and edit audio clips with fade in/out
- **Text Overlays**: Add customizable text with animations

### Effects & Enhancements
- **Video Filters**: Brightness, contrast, saturation, hue, grayscale, sepia, invert, blur, sharpen
- **Transitions**: Fade, slide, zoom, rotate transitions between clips
- **Text Animations**: Fade in/out, slide in, typewriter, zoom effects

### Professional Tools
- **Multiple Resolutions**: 480p, 720p, 1080p, 4K
- **Frame Rates**: 24fps, 30fps, 60fps
- **Aspect Ratios**: 1:1, 4:3, 16:9, 9:16, 3:4
- **Volume Control**: Adjust volume for individual clips
- **Speed Control**: Change playback speed for video clips

### Export
- **Multiple Formats**: MP4, MOV
- **Quality Settings**: Adjust bitrate and resolution
- **Progress Tracking**: Real-time export progress

## Tech Stack

- **Kotlin**: 100% Kotlin codebase
- **Jetpack Compose**: Modern declarative UI
- **Hilt**: Dependency injection
- **Room**: Local database for project persistence
- **Media3**: Video playback and processing
- **Material 3**: Modern design components
- **Navigation Compose**: Type-safe navigation
- **Coroutines & Flow**: Asynchronous programming

## Project Structure

```
android/videocut/
├── app/
│   ├── src/main/
│   │   ├── java/com/videocut/editor/
│   │   │   ├── db/              # Database layer (Room)
│   │   │   ├── di/              # Dependency injection modules
│   │   │   ├── model/           # Data models
│   │   │   ├── repository/      # Data repositories
│   │   │   ├── ui/              # UI components and screens
│   │   │   ├── util/            # Utility classes
│   │   │   └── viewmodel/       # ViewModels
│   │   │   └── VideoCutApp.kt  # Application class
│   │   │   └── MainActivity.kt # Main activity
│   │   └── res/                 # Resources
│   ├── build.gradle.kts        # App module build config
│   └── proguard-rules.pro      # ProGuard rules
├── gradle/
│   └── wrapper/                # Gradle wrapper
├── settings.gradle.kts         # Project settings
├── gradlew                     # Gradle wrapper script (Unix)
├── gradlew.bat                 # Gradle wrapper script (Windows)
└── gradle.properties           # Gradle properties
```

## Getting Started

### Prerequisites

- Android Studio Giraffe (2022.3.1) or later
- Android SDK 35 (Android 15) or later
- Java 17 or later
- Kotlin 1.9.0 or later

### Installation

1. Clone this repository:
   ```bash
   git clone https://github.com/SmokeAlottaJ-afk/opensre.git
   cd opensre/android/videocut
   ```

2. Open the project in Android Studio:
   - File → Open → Select the `android/videocut` directory
   - Android Studio will automatically sync Gradle

3. **Important**: The Gradle wrapper JAR is a placeholder. On first sync, Android Studio will regenerate it automatically.

### Required Permissions

The app requires the following permissions:
- **READ_EXTERNAL_STORAGE**: To import media files
- **WRITE_EXTERNAL_STORAGE**: To save exported videos
- **CAMERA**: To record videos directly
- **RECORD_AUDIO**: To record audio
- **INTERNET**: For optional cloud features

## Building the App

### Debug Build
```bash
./gradlew assembleDebug
```

The APK will be generated at:
```
android/videocut/app/build/outputs/apk/debug/app-debug.apk
```

### Release Build
```bash
./gradlew assembleRelease
```

The signed APK will be at:
```
android/videocut/app/build/outputs/apk/release/app-release.apk
```

### Bundle for Play Store
```bash
./gradlew bundleRelease
```

The Android App Bundle will be at:
```
android/videocut/app/build/outputs/bundle/release/app-release.aab
```

## Signing the App

To sign your release builds:

1. Create a keystore file:
   ```bash
   keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-alias
   ```

2. Add signing configuration to `app/build.gradle.kts`:
   ```kotlin
   android {
       signingConfigs {
           release {
               storeFile = file("my-release-key.jks")
               storePassword = "yourpassword"
               keyAlias = "my-alias"
               keyPassword = "yourpassword"
           }
       }
       buildTypes {
           release {
               signingConfig = signingConfigs.getByName("release")
           }
       }
   }
   ```

## Customization

### Changing the App Name
Edit `app/src/main/res/values/strings.xml`:
```xml
<string name="app_name">Your App Name</string>
```

### Changing the Package Name
Edit `app/build.gradle.kts`:
```kotlin
android {
    namespace = "com.yourpackage.name"
    defaultConfig {
        applicationId = "com.yourpackage.name"
    }
}
```

### Changing Colors
Edit `app/src/main/res/values/colors.xml` to customize the color scheme.

### Changing the App Icon
Replace the icon files in:
- `app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml`
- `app/src/main/res/mipmap-anydpi-v26/ic_launcher_round.xml`
- `app/src/main/res/drawable/ic_launcher_foreground.xml`
- `app/src/main/res/drawable/ic_launcher_background.xml`

## Media3 Integration

This app uses AndroidX Media3 for video playback and processing. Key features:

- **ExoPlayer**: Powerful media playback
- **Video Processing**: Trim, split, and merge videos
- **Audio Processing**: Mix and edit audio tracks
- **Format Support**: MP4, MOV, AVI, and more

### Adding Media3 Dependencies

The app includes:
```kotlin
implementation("androidx.media3:media3-exoplayer:1.3.1")
implementation("androidx.media3:media3-ui:1.3.1")
implementation("androidx.media3:media3-session:1.3.1")
```

## File Picker Integration

The app uses a file picker library to import media:
```kotlin
implementation("com.github.angads25:filepicker:1.4.0")
```

## Permissions Handling

The app uses Dexter for runtime permissions:
```kotlin
implementation("com.karumi:dexter:6.2.3")
```

## Export Functionality

The export feature uses Media3's ExoPlayer and MediaMuxer to:
1. Combine all video clips in the timeline
2. Apply transitions between clips
3. Mix audio tracks with video
4. Add text overlays
5. Apply filters and effects
6. Export to the selected format and quality

## Troubleshooting

### Gradle Sync Issues
1. Ensure you have a stable internet connection
2. Try **File → Sync Project with Gradle Files**
3. Clean and rebuild: **Build → Clean Project**, then **Build → Rebuild Project**
4. Invalidate caches: **File → Invalidate Caches / Restart**

### Build Errors
- **Java version**: Ensure you're using Java 17 or later
- **SDK version**: Install Android SDK 35 (Android 15)
- **Kotlin version**: The project uses Kotlin 1.9.0

### Media Playback Issues
- Ensure the device has enough storage
- Check that the media format is supported
- Verify that the file paths are correct

### Permission Issues
- Ensure all required permissions are declared in the manifest
- Use Dexter to request runtime permissions
- Check permission results before accessing media

## Testing

Run all tests:
```bash
./gradlew test
```

Run instrumentation tests:
```bash
./gradlew connectedAndroidTest
```

## Performance Tips

1. **Enable ProGuard**: Already enabled in release builds
2. **Minify resources**: Already enabled in release builds
3. **Use vector drawables**: All icons use vector drawables for better performance
4. **Lazy loading**: Projects and clips are loaded lazily
5. **Video processing**: Use background threads for heavy video processing

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- [Android Jetpack](https://developer.android.com/jetpack)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Material Design 3](https://m3.material.io/)
- [Hilt](https://dagger.dev/hilt/)
- [Room](https://developer.android.com/training/data-storage/room)
- [Media3](https://developer.android.com/media/media3)
- [FilePicker](https://github.com/angads25/filepicker)
- [Dexter](https://github.com/Karumi/Dexter)

## Support

For issues and questions:
- Open an issue on GitHub
- Check the [Media3 documentation](https://developer.android.com/media/media3)
- Check the [Android documentation](https://developer.android.com/docs)

---

Built with ❤️ using Android Studio, Kotlin, and Media3
