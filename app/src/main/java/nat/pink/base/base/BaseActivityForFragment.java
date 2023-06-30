package nat.pink.base.base;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;

import java.util.concurrent.TimeUnit;

import nat.pink.base.R;
import nat.pink.base.utils.Const;
import nat.pink.base.utils.MyContextWrapper;
import nat.pink.base.utils.PreferenceUtil;

public abstract class BaseActivityForFragment extends AppCompatActivity {

    private InterstitialAd interstitialAdsAdmob;
    private Consumer interstitialConsumer;
    private boolean showInterstitial = false;
    private View viewLoadingAds;
    private Consumer<String> consumerAdsFaill;
    private Consumer<NativeAd> consumerAdsSuccess;
    private AdLoader adLoader;
    private boolean showNativeAd = false;
    private int retryAttempt;
    //    private MaxInterstitialAd interstitialAd;
//    private MaxNativeAdLoader nativeAdLoader;
//    private MaxNativeAdView nativeAdView;
//    private MaxAd nativeAd;
//    private MaxAdView bannerAd;
//    private Consumer<MaxNativeAdView> nativeAdLoadedConsumer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View viewContainer = getLayoutResource();
        setContentView(viewContainer);

        ViewGroup viewGroup = getWindow().getDecorView().findViewById(android.R.id.content);
        LayoutInflater vi = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewLoadingAds = vi.inflate(R.layout.loading_ads_layout, null);
        viewGroup.addView(viewLoadingAds);

        initView();
        initData();
        initEvent();
    }

    protected abstract View getLayoutResource();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initEvent();

    @Override
    protected void attachBaseContext(Context newBase) {
        String english = PreferenceUtil.getString(newBase, PreferenceUtil.KEY_CURRENT_LANGUAGE, "");
        if (!TextUtils.isEmpty(english)) {
            super.attachBaseContext(MyContextWrapper.wrap(newBase, english));
        } else {
            super.attachBaseContext(newBase);
        }
    }

    public void createInterstitialAd(String keyAds) {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, keyAds, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd mInterstitialAd) {
                interstitialAdsAdmob = mInterstitialAd;
                setCallbackInterstitial(keyAds);
                updateAdsRequest();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                interstitialAdsAdmob = null;
                retryAttempt++;
                long delayMillis = TimeUnit.SECONDS.toMillis(5);
                new Handler().postDelayed(() -> {
                    if (retryAttempt < 3) {
                        createNativeAd(keyAds);
                    }
                }, delayMillis);
            }
        });

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

    private void setCallbackInterstitial(String keyAds) {
        interstitialAdsAdmob.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdClicked() {
                if (keyAds.equals(Const.KEY_ADMOB_GUILDE)) {
                    App.getInstance().getFirebaseAnalytics().logEvent("ClickGuideInter", null);
                }
                if (keyAds.equals(Const.KEY_ADMOB_CLEAR_CHAT)) {
                    App.getInstance().getFirebaseAnalytics().logEvent("ClickClearChat", null);
                }
                if (keyAds.equals(Const.KEY_ADMOB_CREATE_USER)) {
                    App.getInstance().getFirebaseAnalytics().logEvent("ClickCreateContact", null);
                }
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, "Ad dismissed fullscreen content.");
                interstitialAdsAdmob = null;
                interstitialConsumer.accept(null);
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when ad fails to show.
                Log.e(TAG, "Ad failed to show fullscreen content.");
                interstitialAdsAdmob = null;
            }

            @Override
            public void onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.");
            }

            @Override
            public void onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.");
            }
        });
    }

    protected void updateAdsRequest() {
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

        if (showInterstitial && interstitialAdsAdmob != null) {
            setLoadingAdsView(false);
            interstitialAdsAdmob.show(this);
            showInterstitial = false;
        }
    }

    public boolean showInterstitialAd(Consumer doneConsumer) {
        interstitialConsumer = doneConsumer;
//        if (interstitialAd != null && interstitialAd.isReady()) {
//            interstitialAd.showAd();
//            return true;
//        }
        if (interstitialAdsAdmob != null) {
            interstitialAdsAdmob.show(this);
            return true;
        }
        setLoadingAdsView(true);
        showInterstitial = true;
        return false;
    }

    public void setLoadingAdsView(Boolean visible) {
        viewLoadingAds.bringToFront();
        viewLoadingAds.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void showNativeAds() {
        if (adLoader != null) adLoader.loadAds(new AdRequest.Builder().build(), 3);
    }

    public void createNativeAd(String keyAds) {
//        setLoadingAdsView(true);
        adLoader = new AdLoader.Builder(this, keyAds).forNativeAd(nativeAd -> {
            consumerAdsSuccess.accept(nativeAd);
//            setLoadingAdsView(false);
        }).withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                consumerAdsFaill.accept(keyAds);
//                setLoadingAdsView(false);
            }
        }).withNativeAdOptions(new NativeAdOptions.Builder()
                .build()).build();

        showNativeAds();

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

    protected void showAdsFail(Consumer<String> consumer) {
        this.consumerAdsFaill = consumer;
    }

    protected void showAdsSuccess(Consumer<NativeAd> consumer) {
        this.consumerAdsSuccess = consumer;
    }

    public void setNativeAdView(Consumer loadedConsumer) {
        consumerAdsSuccess = loadedConsumer;
        // nativeAdView = view;
        showNativeAd = true;
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

    //    public void setNativeAdView(MaxNativeAdView view, Consumer loadedConsumer) {
//        nativeAdLoadedConsumer = loadedConsumer;
//        nativeAdView = view;
//        showNativeAd = true;
//    }
}
