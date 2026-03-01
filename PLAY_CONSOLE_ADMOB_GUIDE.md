# 🎯 Google Play Console + AdMob Integration Guide

## 📋 Полный чек-лист перед публикацией

### 1️⃣ AdMob Setup (за 1-2 дня до публикации)

```
1. Создать AdMob аккаунт
   └─ https://admob.google.com
   └─ Войти с Google аккаунтом

2. Создать приложение в AdMob
   └─ Apps → "Add New App"
   └─ Platform: Android
   └─ App name: "Snake Infinity"
   └─ Store URL: Оставить пусто (заполните после публикации)

3. Получить IDs:
   ├─ App ID: ca-app-pub-XXXXXXXXXXXXXXXX~XXXXXXXXXX
   │  (находится на главной странице приложения)
   │
   └─ Ad Unit IDs: ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX
      ├─ Banners → Create Ad Unit
      │  └─ Name: "Banner Ad"
      │  └─ Ad Format: Banner (320x50)
      │  └─ Copy the ID
      │
      └─ Interstitial → Create Ad Unit
         └─ Name: "Interstitial Ad"
         └─ Ad Format: Interstitial (Full-screen)
         └─ Copy the ID
```

### 2️⃣ Обновить Код Приложения

**Файл 1: `app/build.gradle.kts`**
```kotlin
buildTypes {
    release {
        // ... existing code ...
        // Замените на ваш production App ID
        manifestPlaceholders["admobAppId"] = "ca-app-pub-XXXXXXXXXXXXXXXX~XXXXXXXXXX"
    }
}
```

**Файл 2: `app/src/main/java/com/snakeinfinity/game/presentation/component/AdComponents.kt`**
```kotlin
object AdUnitIds {
    private const val BANNER_PRODUCTION = "ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX"  // ← Ваш Banner ID
    private const val INTERSTITIAL_PRODUCTION = "ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX"  // ← Ваш Interstitial ID
    // ...
}
```

### 3️⃣ Сборка Release APK/AAB

```bash
# Очистить предыдущие builds
./gradlew clean

# Собрать Release Bundle (рекомендуется для Play Store)
./gradlew bundleRelease

# ИЛИ собрать Release APK (для тестирования на устройстве)
./gradlew assembleRelease

# Выходные файлы:
# - AAB: app/build/outputs/bundle/release/app-release.aab
# - APK: app/build/outputs/apk/release/app-release.apk
```

### 4️⃣ Play Console Setup

#### A. Создать приложение
1. Откройте https://play.google.com/console
2. Нажмите **"Create app"**
3. Заполните:
   - App name: "Snake Infinity"
   - Default language: English (или ваш язык)
   - App or game: "Game"
   - Free or Paid: "Free"

#### B. Загрузить Release Build
1. Слева: **Release** → **Create new release**
2. Нажмите **Browse files** → выберите **app-release.aab**
3. Заполните Release notes
4. Нажмите **Save**

#### C. Связать AdMob аккаунт (ВАЖНО!)
1. Слева: **Monetization** → **AdMob**
2. Нажмите **Link AdMob account**
3. Выберите ваш AdMob аккаунт
4. Выберите приложение из AdMob
5. Нажмите **Save**

#### D. Заполнить Store Listing
1. Слева: **Store presence** → **Main store listing**
2. Заполнить обязательные поля:
   - Short description
   - Full description
   - Screenshot (минимум 2)
   - Feature graphic (1024x500)
   - App icon (512x512)

#### E. Приватность и разрешения
1. Слева: **Policies** → **App content**
2. Ответить на вопросы о контенте
3. Слева: **Policies** → **Permissions**
4. Проверить permissions (должны быть только INTERNET и AD_ID)

#### F. Целевая аудитория
1. Слева: **Policies** → **Target audience**
2. Выбрать категорию (Game → Casual)
3. Выбрать age rating

#### G. Информация о приложении
1. Слева: **App content** → **App information**
2. Выбрать категорию: Games → Arcade

### 5️⃣ Отправить на Review

1. Слева: **Release** → выберите вашу **release version**
2. Нажмите **Review and roll out to production**
3. Прочитайте чек-лист и отметьте все пункты
4. Нажмите **Confirm roll out**

### 6️⃣ После отправки

**Timeline:**
- **2-4 часа**: App начнет распространяться
- **24 часов**: Большинство пользователей получат app
- **2-4 часа**: AdMob начнет собирать данные

**Что проверить:**
1. AdMob Console → Reports
   - Impressions (количество показов объявлений)
   - Clicks (количество кликов)
   - Revenue (доход)

2. Play Console → Analytics
   - Installs (количество установок)
   - Crashes (должно быть 0)
   - ANR (should be 0)

---

## ⚠️ Частые ошибки при публикации

### ❌ Error 1: "Invalid App ID"
**Причина**: Используются test IDs в release builds
**Решение**: 
- Убедитесь что в `build.gradle.kts` release { } используется ваш production App ID
- Перестройте release APK/AAB
- Загрузите новый bundle

### ❌ Error 2: "Ads are not serving"
**Причина**: Ad Unit IDs неправильные или не одобрены AdMob
**Решение**:
- Проверьте Ad Unit IDs в AdMob Console
- Убедитесь что App связано с Play Console
- Подождите 24 часа для одобрения

### ❌ Error 3: "Policy Violation - Invalid Traffic"
**Причина**: Клики на собственные объявления или click fraud
**Решение**:
- НЕ кликайте на собственные объявления
- Убедитесь что ads показываются только ПОСЛЕ игры
- Проверьте что нет click injection
- Обновите приложение и отправьте новый review

### ❌ Error 4: "App not reviewed yet"
**Причина**: Google Play еще проверяет приложение
**Решение**:
- Обычно занимает 2-4 часа
- Проверьте email на одобрение
- Если отклонено - исправьте issues и отправьте снова

---

## 📊 Мониторинг Доходов

### AdMob Dashboard
```
Reports → Overview
├─ Impressions (показы объявлений)
├─ Clicks (клики)
├─ Estimated Earnings (доход)
├─ eCPM (заработок за 1000 показов)
└─ CTR (Click Through Rate - % кликов)
```

### Play Console Analytics
```
Analytics → Overview
├─ Installs (установки)
├─ Uninstalls (удаления)
├─ Crashes (ошибки)
├─ Rating (рейтинг)
└─ Revenue (выручка)
```

---

## 💰 Выплаты

### AdMob Payments
- **Минимальная сумма для вывода**: $100 USD
- **Период выплаты**: Monthly (17-22 число)
- **Банковские реквизиты**: Установите в AdMob → Payments profile

### Play Console Payout
- AdMob автоматически связана с Play Console
- Выплаты идут в один банковский счет
- Комиссия Google: 30%

---

## 🔗 Полезные Ссылки

| Ресурс | Link |
|--------|------|
| AdMob Console | https://admob.google.com |
| Play Console | https://play.google.com/console |
| AdMob Policies | https://support.google.com/admob/topic/7383087 |
| Play Store Policies | https://play.google.com/about/monetization-ads/ |
| Android Dev Docs | https://developer.android.com/guide/playcore |

---

## ✅ Pre-Launch Checklist

Перед нажатием "Submit for Review":

- [ ] Production AdMob IDs установлены в коде
- [ ] Release APK/AAB собран успешно
- [ ] Нет синтаксических ошибок в коде
- [ ] Ads НЕ показываются во время активной игры
- [ ] Ads показываются только после Game Over
- [ ] Приватность и разрешения заполнены
- [ ] Скриншоты загружены (минимум 2)
- [ ] Описание приложения заполнено
- [ ] Контактная информация верна
- [ ] Тестовая версия загружена и протестирована
- [ ] AdMob аккаунт связан с Play Console
- [ ] Банковские реквизиты добавлены в AdMob

---

**Поздравляем! 🎉 Ваше приложение готово к публикации!**

