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
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
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
import nat.pink.base.dialog.DialogCountdownTime;
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
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class MainActivity extends BaseActivityForFragment {

    private ActivityMainBinding binding;
    private ArrayList<String> fragmentStates = new ArrayList<>();
    private FragmentManager fragmentManager;
    private boolean isConnect = false;
    private DialogCountdownTime dialogDisconnected;

    @Override
    protected void stateNetWork(boolean isAvaiable) {
       setConnect(isAvaiable);
        if (isAvaiable && dialogDisconnected.isShowing())
            dialogDisconnected.dismiss();
        else
            dialogDisconnected.show();
    }

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
        createInterstitialAd(Const.KEY_ADMOB_GUILDE);
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
        } else if (intent.getAction() != null && intent.getAction().equals(Const.ACTION_COMMING_VIDEO)) {
            Gson gson = new Gson();
            Intent mIntent = new Intent(this, VideoCallActivity.class);
            mIntent.putExtra(Const.PUT_EXTRAL_OBJECT_CALL, gson.fromJson(intent.getType(), ObjectCalling.class));
            startActivity(mIntent);
        } else if (intent.getAction() != null && intent.getAction().equals(Const.ACTION_CALL_VIDEO)) {
            Gson gson = new Gson();
            Intent mIntent = new Intent(this, OutCommingActivity.class);
            mIntent.putExtra(Const.PUT_EXTRAL_OBJECT_CALL, gson.fromJson(intent.getType(), ObjectCalling.class));
            startActivity(mIntent);
        } else if (intent.getAction() != null && intent.getAction().equals(Const.ACTION_CALL_VOICE)) {
            Gson gson = new Gson();
            Intent mIntent = new Intent(this, OutCommingActivity.class);
            mIntent.putExtra(Const.PUT_EXTRAL_OBJECT_CALL, gson.fromJson(intent.getType(), ObjectCalling.class));
            mIntent.putExtra("show_icon_video", true);
            startActivity(mIntent);
        } else if (intent.getAction() != null && intent.getAction().equals(Const.ACTION_COMMING_VOICE)) {
            Gson gson = new Gson();
            Intent mIntent = new Intent(this, VideoCallActivity.class);
            mIntent.putExtra(Const.PUT_EXTRAL_OBJECT_CALL, gson.fromJson(intent.getType(), ObjectCalling.class));
            mIntent.putExtra("show_icon_video", true);
            startActivity(mIntent);
        }
    }

    protected void initView() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        fragmentManager = getSupportFragmentManager();
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
        dialogDisconnected = new DialogCountdownTime(this, R.style.MaterialDialogSheet, o -> {
        });
        dialogDisconnected.setTimeAndTitle(0L, Const.KEY_DISCONNECTED);
    }

    public void replaceFragment(Fragment fragment, String tag) {
        fragmentManager.beginTransaction().replace(R.id.content, fragment, tag).addToBackStack(tag).commit();
        if (!fragmentStates.contains(tag)) fragmentStates.add(tag);
    }

    public void addFragment(Fragment fragment, String tag) {
        fragmentManager.beginTransaction().add(R.id.content, fragment, tag).addToBackStack(tag).commit();
        if (!fragmentStates.contains(tag)) fragmentStates.add(tag);
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

    public boolean isConnect() {
        return isConnect;
    }

    public void setConnect(boolean connect) {
        isConnect = connect;
    }
}