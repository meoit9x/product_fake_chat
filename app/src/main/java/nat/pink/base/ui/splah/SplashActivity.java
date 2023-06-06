package nat.pink.base.ui.splah;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

import nat.pink.base.MainActivity;
import nat.pink.base.R;
import nat.pink.base.databinding.ActivityMainBinding;
import nat.pink.base.databinding.ActivitySplashBinding;
import nat.pink.base.ui.home.HomeFragment;
import nat.pink.base.ui.language.LanguageFragment;
import nat.pink.base.ui.onboard.OnboardFragment;
import nat.pink.base.utils.PreferenceUtil;

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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initAct();
    }
    protected void initAct() {

//        Runnable update = new Runnable() {
//            int progress = 0;
//
//            public void run() {
//                progress += 1;
//                Log.d("debug", "run: progress =");
//                binding.progress.setProgress(progress);
//                if (progress == 99) {
//                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                    i.setAction("android.intent.action.MAIN");
//                    startActivity(i);
//                    finish();
//                }
//            }
//        };
//
//        timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(update);
//            }
//        }, 5, 25);

       runnable = new Runnable() {
            @Override
            public void run() {
                progress += 1;
                Log.d("debug", "run: progress =" + progress);
                binding.progress.setProgress(progress);
                if (progress == 99) {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.setAction("android.intent.action.MAIN");
                    startActivity(i);
                    finish();
                }
                handler.postDelayed(this, 10);
            }
        };
        handler.postDelayed(runnable, 100);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}