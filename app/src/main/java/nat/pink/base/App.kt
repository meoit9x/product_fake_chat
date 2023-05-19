package nat.pink.base;

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent

import com.example.base.log.DebugTree
import com.example.base.log.ReleaseTree
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import timber.log.Timber

//class App : Application(), Application.ActivityLifecycleCallbacks, LifecycleObserver {
class App : Application() {
    private val TAG = "DictionaryApp"

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

        // Initialize In-App Purchase Helper.
//        InAppPurchase.initialize(this, object : InAppPurchaseInitializationListener {
//            override fun onInitialized() {
//                isInitialized = true
//                initializeEvent.postValue(true)
//                BaseFragment.hideAds = InAppPurchase.getInstance().isPurchaseAdsRemove()
//            }
//        })
/*
        AdSettings.setDataProcessingOptions(arrayOf<String>())
        AppLovinSdk.getInstance(this).mediationProvider = AppLovinMediationProvider.MAX
        AppLovinSdk.getInstance(this).initializeSdk {
            Log.d(TAG, "onCreate: ")
        }*/
        val bundle = Bundle()
        bundle.putString("app_open", "app_open")
        FirebaseApp.initializeApp(this)
        firebaseAnalytics = Firebase.analytics
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle)
        initFirebaseMessage()

        // Admob Initialize -> AppOpenAds
//        registerActivityLifecycleCallbacks(this)
//        MobileAds.initialize(this) {}
//        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
//        appOpenAdManager = AppOpenAdManager()
    }

//    @OnLifecycleEvent(Lifecycle.Event.ON_START)
//    fun onMoveToForeground() {
//        currentActivity?.let { appOpenAdManager.showAdIfAvailable(it) }
//    }

//    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
//    }

//    override fun onActivityStarted(activity: Activity) {
//        if (!appOpenAdManager.isShowingAd) {
//            currentActivity = activity
//        }
//    }

//    override fun onActivityResumed(activity: Activity) {}
//
//    override fun onActivityPaused(activity: Activity) {}
//
//    override fun onActivityStopped(activity: Activity) {}
//
//    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
//
//    override fun onActivityDestroyed(activity: Activity) {}

//    fun showAppOpenAds(activity: Activity, onShowAdCompleteListener: OnShowAdCompleteListener) {
//        appOpenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener)
//    }

    interface OnShowAdCompleteListener {
        fun onShowAdComplete()
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
        fun getInstance(): App = instance ?: synchronized(this) {
            instance ?: App().also {
                instance = it
            }
        }

        lateinit var firebaseAnalytics: FirebaseAnalytics
    }


    /**
     * TEST FCM
     * 1. copy: Token -> add firebase console
     */
    private fun initFirebaseMessage() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.e(TAG, "==> token:$token")
        })
    }

    // Supported class for integrate AppOpenAds
//    private inner class AppOpenAdManager {
//
//        private var appOpenAd: AppOpenAd? = null
//        private var isLoadingAd = false
//        var isShowingAd = false
//        private var loadTime: Long = 0
//
//        fun loadAd(context: Context) {
//            if (isLoadingAd || isAdAvailable()) {
//                return
//            }
//
//            isLoadingAd = true
//            val request = AdRequest.Builder().build()
//            AppOpenAd.load(
//                context,
//                BuildConfig.App_Open_Ad_Guide,
//                request,
//                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
//                object : AppOpenAd.AppOpenAdLoadCallback() {
//                    override fun onAdLoaded(ad: AppOpenAd) {
//                        appOpenAd = ad
//                        isLoadingAd = false
//                        loadTime = Date().time
//                        Log.i(TAG, "onAdLoaded.")
//                    }
//
//                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
//                        isLoadingAd = false
//                        Log.i(TAG, "onAdFailedToLoad: " + loadAdError.message)
//                    }
//                }
//            )
//        }
//
//        private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
//            val dateDifference: Long = Date().time - loadTime
//            val numMilliSecondsPerHour: Long = 3600000
//            return dateDifference < numMilliSecondsPerHour * numHours
//        }
//
//        private fun isAdAvailable(): Boolean {
//            return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)
//        }
//
//        fun showAdIfAvailable(activity: Activity) {
//            showAdIfAvailable(
//                activity,
//                object : OnShowAdCompleteListener {
//                    override fun onShowAdComplete() {
//                        //TODO: Do nothing because the user will go back to the activity that shows the ad.
//                    }
//                }
//            )
//        }
//
//        fun showAdIfAvailable(activity: Activity, onShowAdCompleteListener: OnShowAdCompleteListener) {
//            if (isShowingAd) {
//                Log.i(TAG, "The app open ad is already showing.")
//                return
//            }
//
//            if (!isAdAvailable()) {
//                Log.i(TAG, "The app open ad is not ready yet.")
//                onShowAdCompleteListener.onShowAdComplete()
//                loadAd(activity)
//                return
//            }
//
//            Log.i(TAG, "Will show ad.")
//
//            appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
//                override fun onAdDismissedFullScreenContent() {
//                    appOpenAd = null
//                    isShowingAd = false
//                    Log.i(TAG, "onAdDismissedFullScreenContent.")
//
//                    onShowAdCompleteListener.onShowAdComplete()
//                    loadAd(activity)
//                }
//
//                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
//                    appOpenAd = null
//                    isShowingAd = false
//                    Log.i(TAG, "onAdFailedToShowFullScreenContent: " + adError.message)
//
//                    onShowAdCompleteListener.onShowAdComplete()
//                    loadAd(activity)
//                }
//
//                override fun onAdShowedFullScreenContent() {
//                    Log.i(TAG, "onAdShowedFullScreenContent.")
//                }
//            }
//            isShowingAd = true
//            appOpenAd?.show(activity)
//        }
//    }
}