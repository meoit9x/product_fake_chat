package nat.pink.base;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
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
import nat.pink.base.ui.notification.NotificationFragment;
import nat.pink.base.ui.splah.SplashFragment;
import nat.pink.base.ui.video.child.OutCommingActivity;
import nat.pink.base.ui.video.child.VideoCallActivity;
import nat.pink.base.utils.Const;
import nat.pink.base.utils.PreferenceUtil;

import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity  {

    private ActivityMainBinding binding;
    private ArrayList<String> fragmentStates = new ArrayList<>();
    private FragmentManager fragmentManager;
    private MaxInterstitialAd interstitialAd;
    private int retryAttempt;
    private boolean showInterstitial = false;
    private Consumer interstitialConsumer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fragmentManager = getSupportFragmentManager();

        //initView();
        initAds();
        initData();
        Intent intent = getIntent();

        boolean isFromNoti = intent.getBooleanExtra(Const.ACTION_FORWARD_SCREEN,false);
        if (isFromNoti){
            Intent serviceIntent = new Intent(this, CallingService.class);
            this.stopService(serviceIntent);
        }

        if (intent!=null && intent.getAction().equals("android.intent.action.MAIN")){
            initView();
        }
        if (intent != null && intent.getAction().equals(Const.ACTION_COMMING_VIDEO)) {
            Gson gson = new Gson();
            Intent mIntent = new Intent(this, VideoCallActivity.class);
            mIntent.putExtra(
                    Const.PUT_EXTRAL_OBJECT_CALL,
                    gson.fromJson(intent.getType(), ObjectCalling.class)
            );
            startActivity(mIntent);
            finish();
        } else if (intent.getAction().equals(Const.ACTION_CALL_VIDEO)) {
            Gson gson = new Gson();
            Intent mIntent = new Intent(this, OutCommingActivity.class);
            mIntent.putExtra(
                    Const.PUT_EXTRAL_OBJECT_CALL,
                    gson.fromJson(intent.getType(), ObjectCalling.class)
            );
            startActivity(mIntent);
            finish();
        } else if (intent.getAction().equals(Const.ACTION_CALL_VOICE)) {
            Gson gson = new Gson();
            Intent mIntent = new Intent(this, OutCommingActivity.class);
            mIntent.putExtra(
                    Const.PUT_EXTRAL_OBJECT_CALL,
                    gson.fromJson(intent.getType(), ObjectCalling.class)
            );
            mIntent.putExtra("show_icon_video", true);
            startActivity(mIntent);
            finish();
        } else if (intent.getAction().equals(Const.ACTION_COMMING_VOICE)) {
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
        if (intent.getAction().equals(Const.ACTION_CREAT_NOTIFICATION)) {
            addFragment(new HomeFragment(), HomeFragment.TAG);
        }

    }

    private void initView() {
        replaceFragment(new SplashFragment(), SplashFragment.TAG);
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

    void initAds(){
        AppLovinSdk.getInstance( this ).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk( this, configuration -> {
         //   Toast.makeText(getApplicationContext(), "done init", Toast.LENGTH_SHORT).show();
            Log.d("adsDebug", "onSdkInitialized: ");
        });
    }

    protected void updateAdsRequest(){
        if (showInterstitial && interstitialAd !=null && interstitialAd.isReady()){
            setLoadingAdsView(false);
            interstitialAd.showAd();
            showInterstitial = false;
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

    public boolean showInterstitialAd(Consumer doneConsumer){
        interstitialConsumer = doneConsumer;
        if (interstitialAd !=null && interstitialAd.isReady())
        {
            interstitialAd.showAd();
            return true;
        }
        setLoadingAdsView(true);
        showInterstitial = true;
        createInterstitialAd(Const.KEY_ADS_GUIDE);
        return false;
    }




}