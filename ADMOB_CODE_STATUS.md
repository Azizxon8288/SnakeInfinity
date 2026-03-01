# ✅ AdMob Setup Verification Checklist

## Current Code Status

### ✅ AdComponents.kt - READY
```kotlin
object AdUnitIds {
    // Test IDs (DEBUG builds - safe)
    private const val BANNER_TEST = "ca-app-pub-3940256099942544/6300978111"
    private const val INTERSTITIAL_TEST = "ca-app-pub-3940256099942544/1033173712"
    
    // Production IDs (RELEASE builds - YOUR IDS HERE)
    private const val BANNER_PRODUCTION = "ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX" ← UPDATE THIS
    private const val INTERSTITIAL_PRODUCTION = "ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX" ← UPDATE THIS
    
    // Auto-switching (BuildConfig.DEBUG)
    val BANNER: String
        get() = if (BuildConfig.DEBUG) BANNER_TEST else BANNER_PRODUCTION
    
    val INTERSTITIAL: String
        get() = if (BuildConfig.DEBUG) INTERSTITIAL_TEST else INTERSTITIAL_PRODUCTION
}
```

### ✅ app/build.gradle.kts - READY
```kotlin
buildTypes {
    release {
        // ... existing code ...
        manifestPlaceholders["admobAppId"] = "ca-app-pub-3940256099942544~3347511713" ← UPDATE THIS
    }
    debug {
        // Test ID - keep as is for development
        manifestPlaceholders["admobAppId"] = "ca-app-pub-3940256099942544~3347511713"
    }
}
```

### ✅ AndroidManifest.xml - READY
```xml
<!-- AdMob permissions - CONFIGURED -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="com.google.android.gms.permission.AD_ID" 
    tools:ignore="AdvertisingIdPolicy" />

<!-- AdMob meta-data - CONFIGURED -->
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="${admobAppId}" />
```

### ✅ SnakeApp.kt - READY
```kotlin
// MobileAds.initialize() - ALREADY CALLED
CoroutineScope(Dispatchers.IO).launch {
    MobileAds.initialize(this@SnakeApp) {}
}
```

---

## What Each File Does

### 📱 Debug Build (Development)
- Uses test Ad Unit IDs (safe, no earnings)
- Allows unlimited testing
- Safe to click on ads
- Perfect for development

### 📦 Release Build (Production)
- Uses your production Ad Unit IDs
- Real earnings collected
- Goes to Play Store
- What your users see

---

## Auto-Switching Mechanism

```
Your App Launch
    ↓
Is BuildConfig.DEBUG true?
    ↓
    YES → Use TEST IDs (debug build)
    NO → Use PRODUCTION IDs (release build)
```

This means:
- `./gradlew assembleDebug` → Test IDs used
- `./gradlew assembleRelease` → Production IDs used
- No manual switching needed!

---

## Files to Update (Just 2!)

### ⚠️ Update #1: app/build.gradle.kts
- **Line**: ~36 (in release block)
- **Current**: `manifestPlaceholders["admobAppId"] = "ca-app-pub-3940256099942544~3347511713"`
- **Change to**: `manifestPlaceholders["admobAppId"] = "ca-app-pub-YOUR_APP_ID~XXXXXXXXXX"`
- **Example**: `manifestPlaceholders["admobAppId"] = "ca-app-pub-ABCD1234EFGH5678~1111111111"`

### ⚠️ Update #2: AdComponents.kt
- **Lines**: 33-34 (in AdUnitIds object)
- **Current**: 
  ```kotlin
  private const val BANNER_PRODUCTION = "ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX"
  private const val INTERSTITIAL_PRODUCTION = "ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX"
  ```
- **Change to**:
  ```kotlin
  private const val BANNER_PRODUCTION = "ca-app-pub-YOUR_APP_ID/YOUR_BANNER_ID"
  private const val INTERSTITIAL_PRODUCTION = "ca-app-pub-YOUR_APP_ID/YOUR_INTERSTITIAL_ID"
  ```

---

## 🎯 When You're Ready

1. **Get IDs** from https://admob.google.com
2. **Update** the 2 files above
3. **Build**: `./gradlew clean && ./gradlew assembleRelease`
4. **Test** on real device
5. **Upload** to Play Console
6. **Launch** and start earning!

---

## 🔍 How to Verify After Updating

### Check AdComponents.kt
```bash
grep -n "BANNER_PRODUCTION\|INTERSTITIAL_PRODUCTION" app/src/main/java/com/snakeinfinity/game/presentation/component/AdComponents.kt
```
Should show your real IDs, not XXXXXXXX

### Check build.gradle.kts
```bash
grep "admobAppId" app/build.gradle.kts | grep release -A 5
```
Should show your production App ID

### Test the Build
```bash
./gradlew clean
./gradlew assembleRelease
# Check for BUILD SUCCESSFUL
```

---

## 📊 After Publishing

### What to Monitor
1. **AdMob Dashboard** → Reports → Impressions & Revenue
2. **Play Console** → Analytics → Installs & Crashes
3. **Email alerts** from Google (policy violations, payments)

### Expected Timeline
- First 2-4 hours: Data collection starts
- First 7-14 days: Revenue appears
- First 60 days: First payout (when $100+ threshold reached)

---

## ✨ Summary

✅ Your code is **production-ready**  
✅ Auto debug/release switching **configured**  
✅ Permissions **set correctly**  
✅ Ad placement **compliant**  
✅ Firebase **integrated**  

**All you need to do**: Add 3 ID strings and publish! 🚀

---

Next Step: Open `ADMOB_CONFIG_TEMPLATE.txt` to get your IDs!

