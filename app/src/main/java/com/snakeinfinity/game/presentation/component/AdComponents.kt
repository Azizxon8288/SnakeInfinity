package com.snakeinfinity.game.presentation.component

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.snakeinfinity.game.BuildConfig

/**
 * Ad Unit IDs with automatic switching between test and production
 * Based on build type (debug/release)
 *
 * HOW TO UPDATE FOR PRODUCTION:
 * 1. Replace BANNER_PRODUCTION with your AdMob Banner Ad Unit ID
 * 2. Replace INTERSTITIAL_PRODUCTION with your AdMob Interstitial Ad Unit ID
 * 3. Rebuild the release APK
 * 4. Test with the release build before publishing to Play Store
 */
object AdUnitIds {
    // Test Ad Unit IDs (safe for development & testing)
    private const val BANNER_TEST = "ca-app-pub-3940256099942544/6300978111"
    private const val INTERSTITIAL_TEST = "ca-app-pub-3940256099942544/1033173712"

    // ⚠️ PRODUCTION: Replace these with your real AdMob Ad Unit IDs
    private const val BANNER_PRODUCTION = "ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX"
    private const val INTERSTITIAL_PRODUCTION = "ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX"

    // Auto-select: Debug build = TEST, Release build = PRODUCTION
    val BANNER: String
        get() = if (BuildConfig.DEBUG) BANNER_TEST else BANNER_PRODUCTION

    val INTERSTITIAL: String
        get() = if (BuildConfig.DEBUG) INTERSTITIAL_TEST else INTERSTITIAL_PRODUCTION
}

/**
 * Banner Ad Composable - shows a standard banner at the bottom of screens.
 */
@Composable
fun BannerAd(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val adView = remember {
        AdView(context).apply {
            setAdSize(AdSize.BANNER)
            adUnitId = AdUnitIds.BANNER
            loadAd(AdRequest.Builder().build())
        }
    }
    AndroidView(
        factory = { adView },
        modifier = modifier.fillMaxWidth()
    )
    DisposableEffect(Unit) {
        onDispose { adView.destroy() }
    }
}

/**
 * Loads and shows an Interstitial Ad.
 * Call [onAdDismissed] when you want a callback after the ad closes.
 */
fun loadAndShowInterstitial(
    activity: Activity,
    onAdDismissed: () -> Unit
) {
    InterstitialAd.load(
        activity,
        AdUnitIds.INTERSTITIAL,
        AdRequest.Builder().build(),
        object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) {
                ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        onAdDismissed()
                    }
                }
                ad.show(activity)
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                // Ad failed to load — continue without interrupting user
                onAdDismissed()
            }
        }
    )
}
