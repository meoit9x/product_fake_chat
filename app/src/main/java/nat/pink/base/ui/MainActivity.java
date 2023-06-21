package nat.pink.base.ui;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

//import com.applovin.mediation.MaxAd;
//import com.applovin.mediation.MaxAdListener;
//import com.applovin.mediation.MaxAdViewAdListener;
//import com.applovin.mediation.MaxError;
//import com.applovin.mediation.ads.MaxAdView;
//import com.applovin.mediation.ads.MaxInterstitialAd;
//import com.applovin.mediation.nativeAds.MaxNativeAdListener;
//import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
//import com.applovin.mediation.nativeAds.MaxNativeAdView;
//import com.applovin.sdk.AppLovinSdk;
//import com.applovin.sdk.AppLovinSdkConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.util.Consumer;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import nat.pink.base.R;
import nat.pink.base.base.App;
import nat.pink.base.base.BaseActivityForFragment;
import nat.pink.base.databinding.ActivityMainBinding;
import nat.pink.base.model.ObjectCalling;
import nat.pink.base.service.CallingService;
import nat.pink.base.ui.home.HomeFragment;
import nat.pink.base.ui.language.LanguageFragment;
import nat.pink.base.ui.onboard.OnboardFragment;
import nat.pink.base.ui.splah.SplashFragment;
import nat.pink.base.ui.video.child.OutCommingActivity;
import nat.pink.base.ui.video.child.VideoCallActivity;
import nat.pink.base.utils.Const;
import nat.pink.base.utils.MyContextWrapper;
import nat.pink.base.utils.PreferenceUtil;

import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends BaseActivityForFragment {

    private ActivityMainBinding binding;
    private ArrayList<String> fragmentStates = new ArrayList<>();
    private FragmentManager fragmentManager;
    //    private MaxInterstitialAd interstitialAd;
//    private MaxNativeAdLoader nativeAdLoader;
//    private MaxNativeAdView nativeAdView;
//    private MaxAd nativeAd;
//    private MaxAdView bannerAd;
    private int retryAttempt;
    private boolean showInterstitial = false;
    private boolean showNativeAd = false;
    private Consumer interstitialConsumer;
//    private Consumer<MaxNativeAdView> nativeAdLoadedConsumer;

    @Override
    protected View getLayoutResource() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    protected void initData() {
        Intent intent = getIntent();
        boolean isFromNoti = intent.getBooleanExtra(Const.ACTION_FORWARD_SCREEN, false);
        if (isFromNoti) {
            Intent serviceIntent = new Intent(this, CallingService.class);
            this.stopService(serviceIntent);
        }

        if (intent.getAction() != null && intent.getAction().equals("android.intent.action.MAIN")) {
            // initView();
        }
        if (intent.getAction() != null && intent.getAction().equals(Const.ACTION_COMMING_VIDEO)) {
            Gson gson = new Gson();
            Intent mIntent = new Intent(this, VideoCallActivity.class);
            mIntent.putExtra(
                    Const.PUT_EXTRAL_OBJECT_CALL,
                    gson.fromJson(intent.getType(), ObjectCalling.class)
            );
            startActivity(mIntent);
            finish();
        } else if (intent.getAction() != null && intent.getAction().equals(Const.ACTION_CALL_VIDEO)) {
            Gson gson = new Gson();
            Intent mIntent = new Intent(this, OutCommingActivity.class);
            mIntent.putExtra(
                    Const.PUT_EXTRAL_OBJECT_CALL,
                    gson.fromJson(intent.getType(), ObjectCalling.class)
            );
            startActivity(mIntent);
            finish();
        } else if (intent.getAction() != null && intent.getAction().equals(Const.ACTION_CALL_VOICE)) {
            Gson gson = new Gson();
            Intent mIntent = new Intent(this, OutCommingActivity.class);
            mIntent.putExtra(
                    Const.PUT_EXTRAL_OBJECT_CALL,
                    gson.fromJson(intent.getType(), ObjectCalling.class)
            );
            mIntent.putExtra("show_icon_video", true);
            startActivity(mIntent);
            finish();
        } else if (intent.getAction() != null && intent.getAction().equals(Const.ACTION_COMMING_VOICE)) {
            Gson gson = new Gson();
            Intent mIntent = new Intent(this, VideoCallActivity.class);
            mIntent.putExtra(
                    Const.PUT_EXTRAL_OBJECT_CALL,
                    gson.fromJson(intent.getType(), ObjectCalling.class)
            );
            mIntent.putExtra("show_icon_video", true);
            startActivity(mIntent);
            finish();
        }
        handleIntent(intent);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            handleIntent(intent);
        }
    }

    private void handleIntent(Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(Const.ACTION_CREAT_NOTIFICATION)) {
            addFragment(new HomeFragment(), HomeFragment.TAG);
        }

    }

    protected void initView() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        fragmentManager = getSupportFragmentManager();
        createNativeAd(Const.NATIVE_ADS);

        boolean firstTime = PreferenceUtil.getBoolean(getApplicationContext(), PreferenceUtil.OPEN_APP_FIRST_TIME, true);
        if (firstTime) {
            if (PreferenceUtil.getString(getApplicationContext(), PreferenceUtil.KEY_CURRENT_LANGUAGE, "").equals("")) {
                replaceFragment(new LanguageFragment(), LanguageFragment.TAG);
            } else if (!PreferenceUtil.getBoolean(getApplicationContext(), PreferenceUtil.IS_INTRO_OPENED, false)) {
                replaceFragment(new OnboardFragment(), OnboardFragment.TAG);
            } else {
                replaceFragment(new HomeFragment(), HomeFragment.TAG);
            }
        } else {
            replaceFragment(new HomeFragment(), HomeFragment.TAG);
        }
    }

    public void showAdsFail(Consumer<String> consumer){
        this.consumerAdsFaill = consumer;
    }

    public void showAdsSuccess(Consumer<String> consumer){
        this.consumerAdsSuccess = consumer;
    }

    public void replaceFragment(Fragment fragment, String tag) {
        fragmentManager.beginTransaction()
                .replace(R.id.content, fragment, tag)
                .addToBackStack(tag)
                .commit();
        if (!fragmentStates.contains(tag))
            fragmentStates.add(tag);
    }

    public void addFragment(Fragment fragment, String tag) {
        fragmentManager.beginTransaction()
                .add(R.id.content, fragment, tag)
                .addToBackStack(tag)
                .commit();
        if (!fragmentStates.contains(tag))
            fragmentStates.add(tag);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (fragmentStates.size() == 0 || fragmentStates.get(fragmentStates.size() - 1).equals(HomeFragment.TAG) || fragmentStates.get(fragmentStates.size() - 1).equals(SplashFragment.TAG)) {
            finish();
            return;
        }
        getSupportFragmentManager().popBackStack(fragmentStates.get(fragmentStates.size() - 1), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentStates.remove(fragmentStates.size() - 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment item : getSupportFragmentManager().getFragments())
            item.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (Fragment item : getSupportFragmentManager().getFragments())
            item.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void setLoadingAdsView(Boolean visible) {
        Log.d(TAG, "LoadingAdsView: " + visible);
        binding.loadingAdsLayout.loadingAdsLayout.bringToFront();
        binding.loadingAdsLayout.loadingAdsLayout.setVisibility(View.VISIBLE == binding.loadingAdsLayout.loadingAdsLayout.getVisibility() ? View.GONE : View.VISIBLE);
        binding.content.setVisibility(View.VISIBLE == binding.content.getVisibility() ? View.GONE : View.VISIBLE);
    }

//    protected void updateAdsRequest() {
//        if (showInterstitial && interstitialAd != null && interstitialAd.isReady()) {
//            setLoadingAdsView(false);
//            interstitialAd.showAd();
//            showInterstitial = false;
//        }
//        if (showNativeAd && nativeAdView != null && nativeAd != null) {
//            nativeAdLoader.render(nativeAdView, nativeAd);
//            if (nativeAdLoadedConsumer != null) {
//                nativeAdLoadedConsumer.accept(nativeAdView);
//            }
//            showNativeAd = false;
//        }
//    }

    private Consumer<String> consumerAdsFaill, consumerAdsSuccess;
    private AdLoader adLoader;

    public void showNativeAds() {
        if (adLoader != null)
            adLoader.loadAds(new AdRequest.Builder().build(), 3);
    }


    public void createNativeAd(String keyAds) {
        adLoader = new AdLoader.Builder(this, keyAds)
                .forNativeAd(nativeAd -> {
                    consumerAdsSuccess.accept(keyAds);
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        consumerAdsFaill.accept(keyAds);
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();


//        if (nativeAdLoader == null || nativeAdLoader.getAdUnitId() != keyAds) {
//            nativeAdLoader = new MaxNativeAdLoader(keyAds, MainActivity.this);
//            nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
//                @Override
//                public void onNativeAdLoaded(@Nullable MaxNativeAdView maxNativeAdView, MaxAd maxAd) {
//                    super.onNativeAdLoaded(maxNativeAdView, maxAd);
//                    if (nativeAd != null) {
//                        nativeAdLoader.destroy(maxAd);
//                    }
//                    nativeAd = maxAd;
//                    updateAdsRequest();
//                }
//
//                @Override
//                public void onNativeAdLoadFailed(String s, MaxError maxError) {
//                    super.onNativeAdLoadFailed(s, maxError);
//                    nativeAdLoader.loadAd();
//                }
//
//                @Override
//                public void onNativeAdClicked(MaxAd maxAd) {
//                    super.onNativeAdClicked(maxAd);
//                    if (keyAds.equals(Const.KEY_ADS_LANGUAGE))
//                        App.getInstance().getFirebaseAnalytics().logEvent("ClickLanguageNative",null);
//                }
//
//                @Override
//                public void onNativeAdExpired(MaxAd maxAd) {
//                    super.onNativeAdExpired(maxAd);
//                }
//            });
//            new Handler().postDelayed(() -> nativeAdLoader.loadAd(), 2000);
//        }
    }

    public void createInterstitialAd(String keyAds) {
//        if (interstitialAd == null || interstitialAd.getAdUnitId() != keyAds) {
//            interstitialAd = new MaxInterstitialAd(keyAds, this);
//            interstitialAd.setListener(new MaxAdListener() {
//                @Override
//                public void onAdLoaded(MaxAd maxAd) {
//                    retryAttempt = 0;
//                    updateAdsRequest();
//                }
//
//                @Override
//                public void onAdDisplayed(MaxAd maxAd) {
//                }
//
//                @Override
//                public void onAdHidden(MaxAd maxAd) {
//                    interstitialConsumer.accept(null);
//                    interstitialAd.loadAd();
//                }
//
//                @Override
//                public void onAdClicked(MaxAd maxAd) {
//                    if (keyAds.equals(Const.KEY_ADS_GUIDE))
//                        App.getInstance().getFirebaseAnalytics().logEvent("ClickGuideInter",null);
//                    if (keyAds.equals(Const.KEY_ADS_CREATE_CONTACT))
//                        App.getInstance().getFirebaseAnalytics().logEvent("ClickCreateContact",null);
//                }
//
//                @Override
//                public void onAdLoadFailed(String s, MaxError maxError) {
//                    retryAttempt++;
//                    long delayMillis = TimeUnit.SECONDS.toMillis(5);
//
//                    new Handler().postDelayed(() -> interstitialAd.loadAd(), delayMillis);
//                }
//
//                @Override
//                public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {
//                    interstitialAd.loadAd();
//                }
//            });
//        }
//        // Load the first ad
//        interstitialAd.loadAd();
    }

    public void createBannerAd(String keyAds, ViewGroup rootView) {
//        bannerAd = new MaxAdView(keyAds, MainActivity.this);
//        // Stretch to the width of the screen for banners to be fully functional
//        int width = ViewGroup.LayoutParams.MATCH_PARENT;
//        // Banner height on phones and tablets is 50 and 90, respectively
//        int heightPx = getResources().getDimensionPixelSize(R.dimen.banner_height);
//        bannerAd.setLayoutParams(new FrameLayout.LayoutParams(width, heightPx));
//        // Set background or background color for banners to be fully functional
//        bannerAd.setBackgroundColor(Color.WHITE);
//        rootView.addView(bannerAd);
//        // Load the ad
//        bannerAd.loadAd();
//        bannerAd.setListener(new MaxAdViewAdListener() {
//            @Override
//            public void onAdExpanded(MaxAd maxAd) {
//
//            }
//
//            @Override
//            public void onAdCollapsed(MaxAd maxAd) {
//
//            }
//
//            @Override
//            public void onAdLoaded(MaxAd maxAd) {
//                Log.d("adsDebug", "failed");
//                bannerAd.loadAd();
//            }
//
//            @Override
//            public void onAdDisplayed(MaxAd maxAd) {
//
//            }
//
//            @Override
//            public void onAdHidden(MaxAd maxAd) {
//
//            }
//
//            @Override
//            public void onAdClicked(MaxAd maxAd) {
//
//            }
//
//            @Override
//            public void onAdLoadFailed(String s, MaxError maxError) {
//                Log.d("adsDebug", "failed");
//            }
//
//            @Override
//            public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {
//                Log.d("adsDebug", "failed");
//            }
//        });
    }


    public boolean showInterstitialAd(Consumer doneConsumer) {
        interstitialConsumer = doneConsumer;
//        if (interstitialAd != null && interstitialAd.isReady()) {
//            interstitialAd.showAd();
//            return true;
//        }
        setLoadingAdsView(true);
        showInterstitial = true;
        return false;
    }

//    public void setNativeAdView(MaxNativeAdView view, Consumer loadedConsumer) {
//        nativeAdLoadedConsumer = loadedConsumer;
//        nativeAdView = view;
//        showNativeAd = true;
//    }
}