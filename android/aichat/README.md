# AI Chat Android App

A modern AI chat assistant application built with Kotlin, Jetpack Compose, and Material 3 design.

## Features

- **Streaming Chat**: Real-time streaming responses from OpenAI-compatible APIs
- **Conversation History**: Save and manage all your conversations with Room database
- **Multiple Models**: Support for various AI models (GPT-4, GPT-3.5, Claude 3, Llama 3, Mistral, Gemini)
- **Custom API Configuration**: Configure your own API endpoint and key
- **Theme Support**: Light, Dark, and System theme options
- **Material 3 Design**: Modern, beautiful UI following Material Design 3 guidelines
- **Responsive Layout**: Works on all screen sizes

## Tech Stack

- **Kotlin**: 100% Kotlin codebase
- **Jetpack Compose**: Modern declarative UI
- **Hilt**: Dependency injection
- **Room**: Local database for conversation persistence
- **Retrofit**: Network requests with streaming support
- **OkHttp**: Network layer with SSE support
- **Material 3**: Modern design components
- **Navigation Compose**: Type-safe navigation
- **Coroutines & Flow**: Asynchronous programming

## Project Structure

```
android/aichat/
├── app/
│   ├── src/main/
│   │   ├── java/com/aiforge/chat/
│   │   │   ├── db/              # Database layer (Room)
│   │   │   ├── di/              # Dependency injection modules
│   │   │   ├── model/           # Data models
│   │   │   ├── network/         # Network layer (Retrofit, OkHttp)
│   │   │   ├── repository/      # Data repositories
│   │   │   ├── ui/              # UI components and screens
│   │   │   │   ├── components/  # Reusable UI components
│   │   │   │   ├── screens/     # App screens
│   │   │   │   └── theme/       # Theme and styling
│   │   │   ├── util/            # Utility classes
│   │   │   └── viewmodel/       # ViewModels
│   │   │   └── AIChatApp.kt    # Application class
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
   cd opensre/android/aichat
   ```

2. Open the project in Android Studio:
   - File → Open → Select the `android/aichat` directory
   - Android Studio will automatically sync Gradle

3. **Important**: The Gradle wrapper JAR is a placeholder. On first sync, Android Studio will regenerate it automatically.

### Configuration

Before running the app, you need to configure your API settings:

1. Open the app and go to **Settings**
2. Enter your API endpoint (default: `https://api.openai.com/`)
3. Enter your API key
4. Select your preferred model
5. Save the settings

## Building the App

### Debug Build
```bash
./gradlew assembleDebug
```

The APK will be generated at:
```
android/aichat/app/build/outputs/apk/debug/app-debug.apk
```

### Release Build
```bash
./gradlew assembleRelease
```

The signed APK will be at:
```
android/aichat/app/build/outputs/apk/release/app-release.apk
```

### Bundle for Play Store
```bash
./gradlew bundleRelease
```

The Android App Bundle will be at:
```
android/aichat/app/build/outputs/bundle/release/app-release.aab
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

## API Configuration

The app uses the OpenAI-compatible API format. You can use:

- **OpenAI**: `https://api.openai.com/`
- **Anthropic**: `https://api.anthropic.com/`
- **Google Gemini**: `https://generativelanguage.googleapis.com/`
- **Local LLM**: Any OpenAI-compatible local server

### Request Format

The app sends requests in the following format:
```json
{
  "model": "gpt-3.5-turbo",
  "messages": [
    {"role": "system", "content": "You are a helpful assistant."},
    {"role": "user", "content": "Hello!"}
  ],
  "temperature": 0.7,
  "stream": true
}
```

### Response Format

The app expects responses in the OpenAI format with streaming support:
```json
{
  "id": "chatcmpl-xxx",
  "object": "chat.completion.chunk",
  "created": 1234567890,
  "model": "gpt-3.5-turbo",
  "choices": [
    {
      "index": 0,
      "delta": {"content": "Hello!"},
      "finish_reason": null
    }
  ]
}
```

## Troubleshooting

### Gradle Sync Issues
If you encounter Gradle sync issues:
1. Ensure you have a stable internet connection
2. Try **File → Sync Project with Gradle Files**
3. Clean and rebuild: **Build → Clean Project**, then **Build → Rebuild Project**
4. Invalidate caches: **File → Invalidate Caches / Restart**

### Build Errors
- **Java version**: Ensure you're using Java 17 or later
- **SDK version**: Install Android SDK 35 (Android 15)
- **Kotlin version**: The project uses Kotlin 1.9.0

### Network Errors
- Check your internet connection
- Verify your API endpoint is correct
- Ensure your API key is valid
- Check if the API service is available

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
4. **Lazy loading**: Conversations are loaded lazily

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
- [Retrofit](https://square.github.io/retrofit/)
- [OkHttp](https://square.github.io/okhttp/)

## Support

For issues and questions:
- Open an issue on GitHub
- Check the [documentation](https://developer.android.com/docs)

---

Built with ❤️ using Android Studio and Kotlin
