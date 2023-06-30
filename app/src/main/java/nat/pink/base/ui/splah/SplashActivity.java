package nat.pink.base.ui.splah;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Arrays;
import java.util.Timer;

import nat.pink.base.base.App;
import nat.pink.base.databinding.ActivitySplashBinding;
import nat.pink.base.ui.MainActivity;
import nat.pink.base.ui.home.HomeViewModel;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;
    private Timer timer;
    Handler handler = new Handler();
    Runnable runnable;
    private int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initAct();
    }

    protected void initAct() {

        if (getIntent() != null && getIntent().getAction() != null && getIntent().getAction().equals("ACTION_CHANGE_LANGUAGE")) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.setAction("android.intent.action.MAIN");
            startActivity(i);
            finish();
        } else {
            runnable = new Runnable() {
                @Override
                public void run() {
                    progress += 1;
                    binding.progress.setProgress(progress);
                    if (progress == 99) {
                        MobileAds.initialize(SplashActivity.this, initializationStatus -> {
                            Log.d("adsDebug", "init Complete");
                            new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("19F4C875114642E78629F2650F04AFD2"));
                            HomeViewModel.getAdsType(App.getInstance().getFirebaseDatabase(), v -> {
                                App.getInstance().setTypeAds(v);
                                SplashActivity.this.runOnUiThread(() -> {
                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    i.setAction("android.intent.action.MAIN");
                                    startActivity(i);
                                    finish();
                                });

                            });
                        });
                    }
                    handler.postDelayed(this, 10);
                }
            };
            handler.postDelayed(runnable, 100);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}