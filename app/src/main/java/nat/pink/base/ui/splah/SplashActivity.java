package nat.pink.base.ui.splah;

import static nat.pink.base.utils.Const.BROADCAST_NETWORK;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import android.Manifest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.ump.ConsentDebugSettings;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.FormError;
import com.google.android.ump.UserMessagingPlatform;

import java.util.Arrays;
import java.util.Timer;

import nat.pink.base.base.App;
import nat.pink.base.base.BaseActivityForFragment;
import nat.pink.base.databinding.ActivitySplashBinding;
import nat.pink.base.ui.MainActivity;
import nat.pink.base.ui.home.HomeViewModel;
import nat.pink.base.utils.Const;
import nat.pink.base.utils.NetworkUtil;
import nat.pink.base.utils.Utils;

public class SplashActivity extends BaseActivityForFragment {
    ActivitySplashBinding binding;
    Handler handler = new Handler();
    Runnable runnable;
    private int progress = 0;

    private ConsentInformation consentInformation;
    private ConsentForm consentForm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        askNotificationPermission();
        if (Utils.isNetworkAvailable(this))
            initAct();
        else {
            isNetWorkNotAvaiable();
        }
    }

    @Override
    protected void stateNetWork(boolean isAvaiable) {
        if (isAvaiable) {
            initAct();
        } else {
            isNetWorkNotAvaiable();
        }
    }

    private void isNetWorkNotAvaiable() {
        Log.e("natruou", "1");
    }

    @Override
    protected View getLayoutResource() {
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    protected void initAct() {
        if (getIntent() != null && getIntent().getAction() != null && getIntent().getAction().equals("ACTION_CHANGE_LANGUAGE")) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.setAction("android.intent.action.MAIN");
            startActivity(i);
            finish();
        } else {
            showAds();
        }
    }

    private void showAds(){
        MobileAds.initialize(SplashActivity.this, initializationStatus -> {
            if (getIntent() != null && getIntent().getAction() != null && getIntent().getAction().equals("ACTION_CHANGE_LANGUAGE_SETTING")) {
                newIntentMain();
            } else {
                createInterstitialAd(Const.KEY_ADMOB_POINT, o -> {
                    showInterstitialAd(o1 -> {
                        newIntentMain();
                    });
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    private void newIntentMain() {
        SplashActivity.this.runOnUiThread(() -> {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.setAction("android.intent.action.MAIN");
            startActivity(i);
            finish();
        });
    }

    // [START ask_post_notifications]
    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
    // [END ask_post_notifications]

    public void initAds() {
        ConsentDebugSettings debugSettings = new ConsentDebugSettings.Builder(this)
                .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                .addTestDeviceHashedId("4EF6073B35F860FD3BFFAA0F8181AB49")
                .build();

        ConsentRequestParameters params = new ConsentRequestParameters
                .Builder()
                .setConsentDebugSettings(debugSettings)
                .build();

        consentInformation = UserMessagingPlatform.getConsentInformation(this);
        consentInformation.requestConsentInfoUpdate(this,
                params,
                () -> {
                    if (consentInformation.isConsentFormAvailable()) {
                        loadForm();
                    }
                },
                formError -> {
                    showAds();
                });
    }

    public void loadForm() {
        // Loads a consent form. Must be called on the main thread.
        UserMessagingPlatform.loadConsentForm(
                this,
                consentForm -> {
                    SplashActivity.this.consentForm = consentForm;
                    if (consentInformation.getConsentStatus() == ConsentInformation.ConsentStatus.REQUIRED) {
                        consentForm.show(
                                SplashActivity.this,
                                formError -> {
                                    int status = consentInformation.getConsentStatus();
                                    Log.e("natruou",""+ status);
//                                    if (consentInformation.getConsentStatus() == ConsentInformation.ConsentStatus.OBTAINED) {
//                                        showAds();
//                                        return;
//                                    }
//
//                                    // Handle dismissal by reloading form.
//                                    loadForm();
                                });
                    }
                },
                formError -> {
                    showAds();
                }
        );
    }
}