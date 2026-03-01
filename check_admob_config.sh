#!/bin/bash

# 🔍 AdMob Production Configuration Checker
# Этот скрипт проверит вашу конфигурацию перед публикацией на Play Store

echo "🔍 Checking AdMob Production Configuration..."
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

ISSUES=0

# Check 1: AdComponents.kt - Production IDs
echo "📝 Check 1: AdComponents.kt production IDs"
if grep -q "ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX" app/src/main/java/com/snakeinfinity/game/presentation/component/AdComponents.kt; then
    echo -e "${YELLOW}⚠️  WARNING: Found placeholder production IDs in AdComponents.kt${NC}"
    echo "   Replace BANNER_PRODUCTION and INTERSTITIAL_PRODUCTION with real IDs"
    ISSUES=$((ISSUES + 1))
else
    echo -e "${GREEN}✅ Production IDs appear to be set${NC}"
fi
echo ""

# Check 2: build.gradle.kts - Release App ID
echo "📝 Check 2: build.gradle.kts release App ID"
RELEASE_APP_ID=$(grep -A 20 'buildTypes {' app/build.gradle.kts | grep -A 15 'release {' | grep 'manifestPlaceholders\["admobAppId"\]' | head -1)
if echo "$RELEASE_APP_ID" | grep -q "3940256099942544"; then
    echo -e "${YELLOW}⚠️  WARNING: Release build is using TEST App ID${NC}"
    echo "   Current: $RELEASE_APP_ID"
    echo "   Replace with your production AdMob App ID"
    ISSUES=$((ISSUES + 1))
else
    echo -e "${GREEN}✅ Release build appears to use production App ID${NC}"
    echo "   Current: $RELEASE_APP_ID"
fi
echo ""

# Check 3: BuildConfig usage
echo "📝 Check 3: BuildConfig debug switching"
if grep -q "BuildConfig.DEBUG" app/src/main/java/com/snakeinfinity/game/presentation/component/AdComponents.kt; then
    echo -e "${GREEN}✅ Auto-switching (debug/release) is configured${NC}"
else
    echo -e "${YELLOW}⚠️  WARNING: No auto-switching found. Ensure correct IDs for release.${NC}"
    ISSUES=$((ISSUES + 1))
fi
echo ""

# Check 4: AndroidManifest.xml - Internet permission
echo "📝 Check 4: AndroidManifest.xml permissions"
if grep -q 'android.permission.INTERNET' app/src/main/AndroidManifest.xml; then
    echo -e "${GREEN}✅ INTERNET permission is set${NC}"
else
    echo -e "${RED}❌ ERROR: INTERNET permission is missing${NC}"
    ISSUES=$((ISSUES + 1))
fi
echo ""

# Check 5: AndroidManifest.xml - AD_ID permission
if grep -q 'com.google.android.gms.permission.AD_ID' app/src/main/AndroidManifest.xml; then
    echo -e "${GREEN}✅ AD_ID permission is set${NC}"
else
    echo -e "${RED}❌ ERROR: AD_ID permission is missing${NC}"
    ISSUES=$((ISSUES + 1))
fi
echo ""

# Check 6: AndroidManifest.xml - AdMob App ID
echo "📝 Check 5: AndroidManifest.xml meta-data"
if grep -q 'com.google.android.gms.ads.APPLICATION_ID' app/src/main/AndroidManifest.xml; then
    echo -e "${GREEN}✅ AdMob APPLICATION_ID meta-data is set${NC}"
else
    echo -e "${RED}❌ ERROR: AdMob APPLICATION_ID meta-data is missing${NC}"
    ISSUES=$((ISSUES + 1))
fi
echo ""

# Summary
echo "═══════════════════════════════════════════════════════════════"
if [ $ISSUES -eq 0 ]; then
    echo -e "${GREEN}✅ All checks passed! Ready for production.${NC}"
    echo ""
    echo "Next steps:"
    echo "1. Build release APK: ./gradlew assembleRelease"
    echo "2. Test on real device with release build"
    echo "3. Upload to Play Console"
    exit 0
else
    echo -e "${RED}❌ Found $ISSUES issue(s) that need fixing${NC}"
    echo ""
    echo "Please fix the above issues before publishing:"
    echo "1. Replace placeholder IDs with real production IDs from AdMob Console"
    echo "2. Update both AdComponents.kt and build.gradle.kts"
    echo "3. Run this check again"
    exit 1
fi

