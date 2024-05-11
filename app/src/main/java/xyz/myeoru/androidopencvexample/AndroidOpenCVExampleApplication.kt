package xyz.myeoru.androidopencvexample

import android.app.Application
import timber.log.Timber

class AndroidOpenCVExampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}