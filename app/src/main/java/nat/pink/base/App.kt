package nat.pink.base

import android.app.Activity
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.base.log.DebugTree
import com.example.base.log.ReleaseTree
import com.google.firebase.analytics.FirebaseAnalytics
import timber.log.Timber

class App : Application() {
    private val TAG = "FakeApp"

    //    private lateinit var appOpenAdManager: AppOpenAdManager
    private var currentActivity: Activity? = null
    var isInitialized = false

    val initializeEvent: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initTimber()
    }


    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }
    }

    fun isInitializing() = !isInitialized

    companion object {
        @Volatile
        private var instance: App? = null

        @JvmStatic
        fun getInstance(): App = instance
            ?: synchronized(this) {
            instance
                ?: App().also {
                instance = it
            }
        }

        lateinit var firebaseAnalytics: FirebaseAnalytics
    }
}