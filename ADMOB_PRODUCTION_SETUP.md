# 🚀 AdMob Production Setup Guide - Snake Game

## 📋 Шаги для публикации на Google Play Console с production AdMob

### Шаг 1️⃣: Получить Production AdMob IDs

1. **Откройте [Google AdMob Console](https://admob.google.com)**
2. **Войдите** с Google аккаунтом
3. **Создайте приложение**:
   - App name: "Snake Infinity"
   - Platform: Android
   - Store URL: Оставьте пусто (заполните позже после публикации)
4. **Получите App ID**:
   - Format: `ca-app-pub-XXXXXXXXXXXXXXXX~XXXXXXXXXX`
   - Это появится на главной странице приложения

5. **Создайте Ad Units**:
   - Нажмите **"Apps"** → Select your app
   - Click **"Ad Units"** → **"Create New Ad Unit"**
   
   **Banner Ad Unit:**
   - Name: "Banner Ad - Snake Game"
   - Ad format: **Banner**
   - Copy the Ad Unit ID: `ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX`
   
   **Interstitial Ad Unit:**
   - Name: "Interstitial Ad - Game Over"
   - Ad format: **Interstitial**
   - Copy the Ad Unit ID: `ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX`

---

### Шаг 2️⃣: Обновить код приложения

#### A. Update `app/build.gradle.kts`

Замените **ВСЕ** тестовые IDs на ваши production AdMob IDs:

```kotlin
buildTypes {
    release {
        isMinifyEnabled = true
        isShrinkResources = true
        proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
        )
        // ✅ PRODUCTION ID (from AdMob Console)
        manifestPlaceholders["admobAppId"] = "ca-app-pub-YOUR_REAL_APP_ID~XXXXXXXXXX"
    }
    debug {
        // Keep test ID for development
        manifestPlaceholders["admobAppId"] = "ca-app-pub-3940256099942544~3347511713"
    }
}
```

#### B. Update `presentation/component/AdComponents.kt`

```kotlin
object AdUnitIds {
    // DEV IDs — for testing only
    const val BANNER_DEV = "ca-app-pub-3940256099942544/6300978111"
    const val INTERSTITIAL_DEV = "ca-app-pub-3940256099942544/1033173712"
    
    // PRODUCTION IDs (from AdMob Console)
    const val BANNER = "ca-app-pub-YOUR_APP_ID/YOUR_BANNER_UNIT_ID"
    const val INTERSTITIAL = "ca-app-pub-YOUR_APP_ID/YOUR_INTERSTITIAL_UNIT_ID"
}
```

---

### Шаг 3️⃣: Безопасный переход на Production

**⚠️ ВАЖНО: Google AdMob требует соблюдения следующих правил:**

✅ **DO (Разрешено):**
- Использовать production IDs **только** в release builds
- Использовать test IDs во время разработки (debug builds)
- Показывать объявления только **после игры** (на экранах меню)
- Соблюдать частоту показа объявлений (не чаще 1 раза в 3 игры)
- Полностью раскрывать информацию об объявлениях

❌ **DON'T (Запрещено):**
- Не кликать на свои объявления (особенно с production ID)
- Не показывать объявления **во время активной игры**
- Не использовать production ID в debug/test builds
- Не использовать click injection или other fraud techniques
- Не скрывать объявления под каким-либо предлогом

---

### Шаг 4️⃣: Проверка перед публикацией

```bash
# 1. Убедитесь, что в release build используются production IDs:
./gradlew clean
./gradlew assembleRelease

# 2. Проверьте APK:
unzip -p app/build/outputs/apk/release/app-release.apk \
  AndroidManifest.xml | grep admobAppId
```

Вы должны увидеть ваш **production App ID**, а не тестовый.

---

### Шаг 5️⃣: Публикация на Google Play Console

1. **Создайте Play Console аккаунт**: https://play.google.com/console
2. **Загрузите подписанный APK/AAB**
3. **В разделе Ad mob settings**:
   - Link ваш AdMob аккаунт
   - Укажите ваш AdMob App ID
4. **Заполните все required fields** и отправьте на review

---

### Шаг 6️⃣: Мониторинг после публикации

После публикации на Play Store:

1. **Проверьте AdMob Dashboard**:
   - Должны появиться первые impression данные через несколько часов
   - Watch for **"Policy violation"** alerts (в красном цвете)

2. **Logs для debug**:
```kotlin
// В SnakeApp.kt для проверки инициализации:
override fun onCreate() {
    super.onCreate()
    
    // Initialize AdMob
    CoroutineScope(Dispatchers.IO).launch {
        MobileAds.initialize(this@SnakeApp) { status ->
            Log.d("AdMob", "MobileAds initialized. Status: ${status}")
        }
    }
}
```

3. **В случае блокировки** (Policy Violation):
   - Google отправит email с details
   - Исправьте проблему и обновите приложение
   - Обычно восстановление занимает 1-3 дня

---

## 📊 Текущая конфигурация вашего приложения

### Файлы, которые нужно обновить:

| Файл | Что менять | Текущее значение |
|------|-----------|-----------------|
| `app/build.gradle.kts` | Release manifestPlaceholders | Test ID |
| `presentation/component/AdComponents.kt` | BANNER & INTERSTITIAL constants | Test IDs |

### Размещение объявлений в вашем приложении:

```
🏠 Home Screen
  └─ Banner Ad (Bottom) ✅

🎮 Game Screen
  └─ NO ADS (During gameplay) ✅

💀 Game Over Screen
  ├─ Score Display
  └─ Interstitial Ad (Full Screen) ✅

🏆 Leaderboard
  └─ Banner Ad (Bottom) ✅
```

---

## 🔗 Полезные ссылки

- [Google AdMob Official Docs](https://support.google.com/admob/)
- [AdMob Policy Center](https://support.google.com/admob/topic/7383087)
- [Play Store Monetization Policies](https://play.google.com/about/monetization-ads/)
- [Testing with Test Ad Unit IDs](https://support.google.com/admob/answer/3052719)

---

## ⏰ Чек-лист перед публикацией

- [ ] Получены production AdMob IDs
- [ ] Обновлен `app/build.gradle.kts` с production ID
- [ ] Обновлены ad unit IDs в `AdComponents.kt`
- [ ] Release build собран и проверен
- [ ] Адаб реально не показываются во время игры
- [ ] Проверена частота показа объявлений
- [ ] Нет собственных кликов на объявления
- [ ] AdMob аккаунт связан с Play Console
- [ ] Все поля заполнены в Play Console

---

**Готово! Ваше приложение готово к публикации с monetization на AdMob!** 🚀

