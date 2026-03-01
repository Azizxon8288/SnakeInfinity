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

// ── Test Ad Unit IDs (replace with real ones from AdMob console) ──────────────
object AdUnitIds {
    // TEST IDs — Safe to use during development
    const val BANNER = "ca-app-pub-3940256099942544/6300978111"
    const val INTERSTITIAL = "ca-app-pub-3940256099942544/1033173712"
    // PRODUCTION (uncomment and replace when publishing):
    // const val BANNER = "ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX"
    // const val INTERSTITIAL = "ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX"
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
