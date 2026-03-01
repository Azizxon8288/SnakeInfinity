# 🐍 Snake Infinity — Offline Android Game

> A fully offline Snake game with local leaderboard, AdMob monetization, Kotlin, Jetpack Compose, MVVM, Clean Architecture, Koin DI, and DataStore persistence.

---

## ✅ Offline-First Design

**No network required to play.** The only internet permission is used by AdMob to serve ads.

| Feature | Storage |
|---|---|
| High score | DataStore Preferences |
| Top-10 leaderboard | DataStore (JSON via kotlinx.serialization) |
| Game settings | DataStore Preferences |
| Gameplay | 100% local, no API calls |

---

## 🏗️ Architecture

```
app/
└── src/main/java/com/snakeinfinity/game/
    ├── di/                      ← Koin DI modules (no network module)
    ├── domain/
    │   ├── model/               ← Position, Direction, SnakeState, LeaderboardEntry…
    │   ├── repository/          ← GameRepository, LeaderboardRepository (interfaces)
    │   └── usecase/             ← Get/SaveHighScore, Get/SaveSettings,
    │                                GetLeaderboard, AddLeaderboardEntry, ClearLeaderboard
    ├── data/
    │   └── repository/          ← GameRepositoryImpl + LeaderboardRepositoryImpl (DataStore)
    └── presentation/
        ├── component/           ← BannerAd, Interstitial helper
        ├── navigation/          ← NavGraph (Home → Game → Leaderboard → Settings)
        ├── screen/              ← HomeScreen, GameScreen, LeaderboardScreen, SettingsScreen
        ├── theme/               ← Neon dark theme
        └── viewmodel/           ← GameViewModel, LeaderboardViewModel, SettingsViewModel
```

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | **Kotlin** |
| UI | **Jetpack Compose** + Material 3 |
| Architecture | **MVVM** + Clean Architecture |
| DI | **Koin 3.5** |
| Local Storage | **DataStore Preferences** |
| Serialization | **kotlinx.serialization** (local leaderboard JSON) |
| Monetization | **Google AdMob** (Banner + Interstitial) |
| Navigation | **Compose Navigation** |

> ⚡ **Ktor removed** — no networking layer at all (except AdMob SDK internals).

---

## 💰 AdMob Setup (Required Before Publishing)

### 1. Create your AdMob account at [admob.google.com](https://admob.google.com)

### 2. Get your IDs
- **App ID**: `ca-app-pub-XXXXXXXXXXXXXXXX~XXXXXXXXXX`
- **Banner Ad Unit ID**: `ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX`
- **Interstitial Ad Unit ID**: `ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX`

### 3. Replace test IDs

**`app/build.gradle.kts`** (both debug & release):
```kotlin
manifestPlaceholders["admobAppId"] = "ca-app-pub-YOUR_REAL_APP_ID"
```

**`presentation/component/AdComponents.kt`**:
```kotlin
object AdUnitIds {
    const val BANNER       = "ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX"
    const val INTERSTITIAL = "ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX"
}
```

### Ad Placement (Play Store compliant)
- **Banner**: Bottom of all 4 screens, never overlapping gameplay
- **Interstitial**: Shown after every 3rd game over — within Google's frequency policy
- No ads during active gameplay
- No misleading close buttons

---

## 🎮 Game Features

- 20×20 grid canvas-rendered Snake
- Swipe gestures + D-Pad controls
- Dynamic leveling — score thresholds increase speed
- Pause / Resume
- Local top-10 leaderboard with player name, level, date
- Clear leaderboard option
- 4 game speeds: Slow / Normal / Fast / Insane
- 4 themes: Neon / Classic / Forest / Ocean
- Neon glow effects on snake head and food dot
- Animated snake body with alpha fade

---

## 🚀 Getting Started

```bash
# 1. Unzip and open in Android Studio (Hedgehog+)
# 2. Replace AdMob IDs (see above)
# 3. Run on device or emulator (API 26+)
./gradlew assembleDebug

# Release build
./gradlew assembleRelease
```

---

## 📋 Play Store Checklist

- ✅ Target SDK 35 / Min SDK 26
- ✅ INTERNET permission (AdMob only)
- ✅ AD_ID permission declared
- ✅ AdMob App ID in manifest meta-data
- ✅ ProGuard rules for AdMob, Koin, Serialization
- ✅ Portrait orientation locked
- ✅ Edge-to-edge support
- ✅ No hardcoded test IDs in release builds
- ✅ No intrusive / mid-game ads

---

*Built with ❤️ by Snake Infinity Game Studio*
