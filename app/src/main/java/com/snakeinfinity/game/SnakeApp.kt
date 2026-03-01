package com.snakeinfinity.game

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.snakeinfinity.game.di.appModule
import com.snakeinfinity.game.di.repositoryModule
import com.snakeinfinity.game.di.useCaseModule
import com.snakeinfinity.game.di.viewModelModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class SnakeApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@SnakeApp)
            modules(
                appModule,
                repositoryModule,
                useCaseModule,
                viewModelModule
            )
        }

        // Initialize AdMob off the main thread
        CoroutineScope(Dispatchers.IO).launch {
            MobileAds.initialize(this@SnakeApp) {}
        }
    }
}
