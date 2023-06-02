package nat.pink.base;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.util.Consumer;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import nat.pink.base.base.BaseFragment;
import nat.pink.base.databinding.ActivityMainBinding;
import nat.pink.base.model.ObjectCalling;
import nat.pink.base.service.CallingService;
import nat.pink.base.ui.home.HomeFragment;
import nat.pink.base.ui.language.LanguageFragment;
import nat.pink.base.ui.notification.NotificationFragment;
import nat.pink.base.ui.onboard.OnboardFragment;
import nat.pink.base.ui.splah.SplashFragment;
import nat.pink.base.ui.video.child.OutCommingActivity;
import nat.pink.base.ui.video.child.VideoCallActivity;
import nat.pink.base.utils.Const;
import nat.pink.base.utils.PreferenceUtil;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<String> fragmentStates = new ArrayList<>();
    private FragmentManager fragmentManager;
    private MaxInterstitialAd interstitialAd;
    private MaxNativeAdLoader nativeAdLoader;
    private MaxNativeAdView nativeAdView;
    private MaxAd nativeAd;
    private MaxAdView bannerAd;
    private int retryAttempt;
    private boolean showInterstitial = false;
    private boolean showNativeAd = false;
    private Consumer interstitialConsumer;
    private Consumer<MaxNativeAdView> nativeAdLoadedConsumer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fragmentManager = getSupportFragmentManager();

        //disable darkmode
        // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //use full taskbar
/*        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);*/

        //initView();
        initAds();
        initData();
        Intent intent = getIntent();

        boolean isFromNoti = intent.getBooleanExtra(Const.ACTION_FORWARD_SCREEN,false);
        if (isFromNoti){
            Intent serviceIntent = new Intent(this, CallingService.class);
            this.stopService(serviceIntent);
        }

        if (intent.getAction() != null && intent.getAction().equals("android.intent.action.MAIN")){
            initView();
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

    private void initData() {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent != null) {
            handleIntent(intent);
        }
    }

    private void handleIntent(Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(Const.ACTION_CREAT_NOTIFICATION)) {
            addFragment(new HomeFragment(), HomeFragment.TAG);
        }

    }

    private void initView() {
        boolean firstTime = PreferenceUtil.getBoolean(getApplicationContext(), PreferenceUtil.OPEN_APP_FIRST_TIME, true);
        if (firstTime) {
            if (PreferenceUtil.getString(getApplicationContext(), PreferenceUtil.SETTING_LANGUAGE, "").equals("")) {
                replaceFragment(new LanguageFragment(), LanguageFragment.TAG);
            } else if (!PreferenceUtil.getBoolean(getApplicationContext(), PreferenceUtil.IS_INTRO_OPENED, false)) {
                 replaceFragment(new OnboardFragment(), OnboardFragment.TAG);
            } else {
                replaceFragment(new HomeFragment(), HomeFragment.TAG);
            }
        } else{
             replaceFragment(new HomeFragment(), HomeFragment.TAG);
        }
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

    void initAds() {
        AppLovinSdk.getInstance(this).setMediationProvider("max");
        AppLovinSdk.initializeSdk(this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration) {
                createInterstitialAd(Const.KEY_ADS_GUIDE);
                createNativeAd(Const.KEY_ADS_LANGUAGE);
                //   Toast.makeText(getApplicationContext(), "done init", Toast.LENGTH_SHORT).show();
                Log.d("adsDebug", "onSdkInitialized: ");
            }
        });
    }

    protected void updateAdsRequest() {
        if (showInterstitial && interstitialAd != null && interstitialAd.isReady()) {
            setLoadingAdsView(false);
            interstitialAd.showAd();
            showInterstitial = false;
        }
        if (showNativeAd && nativeAdView != null && nativeAd != null) {
            nativeAdLoader.render(nativeAdView, nativeAd);
            if (nativeAdLoadedConsumer != null) {
                nativeAdLoadedConsumer.accept(nativeAdView);
            }
            showNativeAd = false;
        }
    }

    public void createNativeAd(String keyAds) {
        if (nativeAdLoader == null || nativeAdLoader.getAdUnitId() != keyAds) {
            nativeAdLoader = new MaxNativeAdLoader(keyAds, MainActivity.this);
            nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                @Override
                public void onNativeAdLoaded(@Nullable MaxNativeAdView maxNativeAdView, MaxAd maxAd) {
                    super.onNativeAdLoaded(maxNativeAdView, maxAd);
                    if (nativeAd != null) {
                        nativeAdLoader.destroy(maxAd);
                    }
                    nativeAd = maxAd;
                    updateAdsRequest();
                }

                @Override
                public void onNativeAdLoadFailed(String s, MaxError maxError) {
                    super.onNativeAdLoadFailed(s, maxError);
                    nativeAdLoader.loadAd();
                }

                @Override
                public void onNativeAdClicked(MaxAd maxAd) {
                    super.onNativeAdClicked(maxAd);
                }

                @Override
                public void onNativeAdExpired(MaxAd maxAd) {
                    super.onNativeAdExpired(maxAd);
                }
            });
            new Handler().postDelayed(() -> nativeAdLoader.loadAd(), 2000);
        }
    }

    public void createInterstitialAd(String keyAds) {
        if (interstitialAd == null || interstitialAd.getAdUnitId() != keyAds) {
            interstitialAd = new MaxInterstitialAd(keyAds, this);
            interstitialAd.setListener(new MaxAdListener() {
                @Override
                public void onAdLoaded(MaxAd maxAd) {
                    retryAttempt = 0;
                  //  Toast.makeText(MainActivity.this, "ad loaded", Toast.LENGTH_SHORT).show();
                    Log.d("adsDebug", "onAdLoaded: ");
                    updateAdsRequest();
                }

                @Override
                public void onAdDisplayed(MaxAd maxAd) {
                    Log.d("adsDebug", "onAdDisplayed: ");
                 //   Toast.makeText(MainActivity.this, "ad displayed", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdHidden(MaxAd maxAd) {
                    interstitialConsumer.accept(null);
                    interstitialAd.loadAd();
                }

                @Override
                public void onAdClicked(MaxAd maxAd) {
                    Log.d("adsDebug", "onAdClicked: ");
                }

                @Override
                public void onAdLoadFailed(String s, MaxError maxError) {
                    // Interstitial ad failed to load
                    // We recommend retrying with exponentially higher delays up to a maximum delay (in this case 64 seconds)
                    retryAttempt++;
                    long delayMillis = TimeUnit.SECONDS.toMillis(5);

                    new Handler().postDelayed(() -> interstitialAd.loadAd(), delayMillis);
                }

                @Override
                public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {
                    interstitialAd.loadAd();
                }
            });
        }
        // Load the first ad
        interstitialAd.loadAd();
    }

    public void createBannerAd(String keyAds,ViewGroup rootView){
        bannerAd = new MaxAdView(keyAds,MainActivity.this);
        // Stretch to the width of the screen for banners to be fully functional
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        // Banner height on phones and tablets is 50 and 90, respectively
        int heightPx = getResources().getDimensionPixelSize( R.dimen.banner_height );
        bannerAd.setLayoutParams( new FrameLayout.LayoutParams( width, heightPx ) );
        // Set background or background color for banners to be fully functional
        bannerAd.setBackgroundColor( Color.WHITE );
        rootView.addView( bannerAd );
        // Load the ad
        bannerAd.loadAd();
        bannerAd.setListener(new MaxAdViewAdListener() {
            @Override
            public void onAdExpanded(MaxAd maxAd) {

            }

            @Override
            public void onAdCollapsed(MaxAd maxAd) {

            }

            @Override
            public void onAdLoaded(MaxAd maxAd) {
                Log.d("adsDebug","failed");
                bannerAd.loadAd();
            }

            @Override
            public void onAdDisplayed(MaxAd maxAd) {

            }

            @Override
            public void onAdHidden(MaxAd maxAd) {

            }

            @Override
            public void onAdClicked(MaxAd maxAd) {

            }

            @Override
            public void onAdLoadFailed(String s, MaxError maxError) {
                Log.d("adsDebug","failed");
            }

            @Override
            public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {
                Log.d("adsDebug","failed");
            }
        });
    }


    public boolean showInterstitialAd(Consumer doneConsumer) {
        interstitialConsumer = doneConsumer;
        if (interstitialAd != null && interstitialAd.isReady()) {
            interstitialAd.showAd();
            return true;
        }
        setLoadingAdsView(true);
        showInterstitial = true;
        return false;
    }

    public void setNativeAdView(MaxNativeAdView view, Consumer loadedConsumer) {
        nativeAdLoadedConsumer = loadedConsumer;
        nativeAdView = view;
        showNativeAd = true;
    }

}