# ⚡ AdMob Production Switch - Quick Reference

## 🚀 За 5 минут до публикации на Play Store

### Шаг 1: Получить Production IDs из AdMob Console

1. Откройте https://admob.google.com
2. Выберите ваше приложение
3. Перейдите в **App settings** → скопируйте **App ID**
4. Перейдите в **Ad units** → создайте/скопируйте:
   - **Banner Ad Unit ID** (для главного экрана)
   - **Interstitial Ad Unit ID** (для экрана Game Over)

### Шаг 2: Обновить код

#### Файл 1: `app/build.gradle.kts`

```kotlin
buildTypes {
    release {
        // ... existing code ...
        
        // Вставьте ваш production App ID сюда:
        manifestPlaceholders["admobAppId"] = "ca-app-pub-XXXXXXXXXXXXXXXX~XXXXXXXXXX"
    }
}
```

#### Файл 2: `app/src/main/java/com/snakeinfinity/game/presentation/component/AdComponents.kt`

Найдите эту часть:
```kotlin
object AdUnitIds {
    private const val BANNER_PRODUCTION = "ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX"
    private const val INTERSTITIAL_PRODUCTION = "ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX"
    // ...
}
```

Замените `XXXXXXXXXXXXXXXX` на ваши реальные Ad Unit IDs:
```kotlin
private const val BANNER_PRODUCTION = "ca-app-pub-YOUR_APP_ID/YOUR_BANNER_ID"
private const val INTERSTITIAL_PRODUCTION = "ca-app-pub-YOUR_APP_ID/YOUR_INTERSTITIAL_ID"
```

### Шаг 3: Build & Test

```bash
# Очистить старые builds
./gradlew clean

# Собрать release APK
./gradlew assembleRelease

# Проверить что используются production IDs
unzip -p app/build/outputs/apk/release/app-release.apk AndroidManifest.xml | grep -o "ca-app-pub-[^\"]*"
```

### Шаг 4: Тестирование перед публикацией

✅ **Обязательно проверьте:**
1. Объявления появляются только ПОСЛЕ игры (не во время)
2. Нет собственных кликов на объявления
3. Частота показа OK (не чаще 1 раза в 3 игры)
4. Логи нет ошибок при загрузке объявлений

### Шаг 5: Загрузить на Play Console

1. Откройте https://play.google.com/console
2. Выберите ваше приложение
3. Загрузите **app-release.aab** или **app-release.apk**
4. В разделе **Monetization** свяжите AdMob аккаунт
5. Отправьте на review

---

## 📱 Текущая конфигурация (для отладки)

### Как работает сейчас:

| Build Type | Используемые IDs | Статус |
|-----------|-----------------|--------|
| **Debug** (разработка) | Test IDs | ✅ Тестовые объявления |
| **Release** (публикация) | Production IDs | ⚠️ Нужна замена на реальные |

### Автоматическое переключение:

```kotlin
// В AdComponents.kt:
val BANNER: String
    get() = if (BuildConfig.DEBUG) BANNER_TEST else BANNER_PRODUCTION
    
// Это означает:
// - При debug build → автоматически используются TEST IDs
// - При release build → автоматически используются PRODUCTION IDs
```

---

## 🔗 Где взять IDs

| Что искать | Где найти | Формат |
|-----------|---------|--------|
| **App ID** | AdMob Console → App settings | `ca-app-pub-XXXXXXXXXXXXXXXX~XXXXXXXXXX` |
| **Banner AD Unit ID** | AdMob Console → Ad units (создать новый) | `ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX` |
| **Interstitial AD Unit ID** | AdMob Console → Ad units (создать новый) | `ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX` |

---

## ✋ Частые ошибки (ИЗБЕГАЙТЕ)

❌ **НЕПРАВИЛЬНО:**
```kotlin
// Не используйте test IDs в release builds!
manifestPlaceholders["admobAppId"] = "ca-app-pub-3940256099942544~3347511713" // ❌ for release
```

✅ **ПРАВИЛЬНО:**
```kotlin
// Используйте production IDs в release builds
manifestPlaceholders["admobAppId"] = "ca-app-pub-YOUR_REAL_ID~YOUR_ID" // ✅ for release
```

---

## 📊 После публикации на Play Store

1. **Проверьте AdMob Dashboard** через 2-4 часа
   - Должны появиться impressions и clicks
   - Проверьте нет ли "Policy Violation" alerts

2. **Если Google блокировал monetization:**
   - Проверьте email с details
   - Исправьте проблему (обычно из-за ad placement)
   - Загрузите новую версию
   - Ждите 1-3 дня на approval

3. **Монитор performance:**
   - AdMob Dashboard → Reports
   - Проверяйте Daily Revenue, Impressions, CTR

---

## 🆘 Нужна помощь?

- [AdMob Official Docs](https://support.google.com/admob/)
- [AdMob Policies](https://support.google.com/admob/topic/7383087)
- [Play Store Policies](https://play.google.com/about/monetization-ads/)

**Совет:** Держите test IDs в Debug builds даже после публикации. Это позволит вам тестировать ad flow без влияния на production metrics.

