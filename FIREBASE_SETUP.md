# Firebase Setup Guide

## Что было добавлено:

### 1. **Gradle конфигурация** (`build.gradle.kts`)
- Добавлен Google Services плагин в корневой `build.gradle.kts`
- Применен Google Services плагин в `app/build.gradle.kts`

### 2. **Firebase зависимости** (`gradle/libs.versions.toml`)
Добавлены следующие Firebase модули:
- **Firebase Analytics** - для аналитики
- **Firebase Auth** - для аутентификации пользователей
- **Firebase Firestore** - облачная база данных
- **Firebase Storage** - хранилище файлов
- **Firebase Cloud Messaging** - push-уведомления
- **Firebase Realtime Database** - база данных в реальном времени

### 3. **google-services.json**
Файл уже присутствует с конфигурацией для проекта:
- Project ID: `snake-62b7e`
- App Package: `com.snakeinfinity.game`

## Как использовать Firebase:

### Пример 1: Firebase Analytics
```kotlin
import com.google.firebase.analytics.FirebaseAnalytics

val firebaseAnalytics = FirebaseAnalytics.getInstance(context)
val bundle = Bundle().apply {
    putString("level_name", "Level 1")
    putInt("score", 100)
}
firebaseAnalytics.logEvent("game_level_complete", bundle)
```

### Пример 2: Firebase Auth
```kotlin
import com.google.firebase.auth.FirebaseAuth

val auth = FirebaseAuth.getInstance()
auth.signInAnonymously().addOnCompleteListener { task ->
    if (task.isSuccessful) {
        val user = auth.currentUser
        // User signed in anonymously
    }
}
```

### Пример 3: Firestore Database
```kotlin
import com.google.firebase.firestore.FirebaseFirestore

val db = FirebaseFirestore.getInstance()
val userScore = hashMapOf(
    "username" to "Player",
    "score" to 1000
)
db.collection("scores").add(userScore)
```

## Следующие шаги:

1. **Синхронизируйте проект** - Android Studio автоматически синхронизирует Gradle
2. **Установите зависимости** - убедитесь, что все Firebase модули скачаны
3. **Проверьте консоль Firebase** - перейдите в [Firebase Console](https://console.firebase.google.com/) и выберите проект `snake-62b7e`
4. **Добавьте необходимые функции** - используйте нужные вам сервисы Firebase

## Ресурсы:
- [Firebase Documentation](https://firebase.google.com/docs)
- [Firebase Android Guides](https://firebase.google.com/docs/android/setup)

