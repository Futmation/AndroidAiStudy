package xyz.myeoru.aistudy

import android.app.Application
import timber.log.Timber

class AiStudyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}