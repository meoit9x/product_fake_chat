package nat.pink.base.ui.splah;

import android.os.Handler;

import androidx.lifecycle.ViewModelProvider;

import java.util.Timer;
import java.util.TimerTask;

import nat.pink.base.base.BaseFragment;
import nat.pink.base.databinding.FragmentSplashBinding;
import nat.pink.base.ui.home.HomeFragment;
import nat.pink.base.ui.language.LanguageFragment;
import nat.pink.base.ui.onboard.OnboardFragment;
import nat.pink.base.utils.PreferenceUtil;

public class SplashFragment extends BaseFragment<FragmentSplashBinding, SplashViewModel> {

    public static final String TAG = "SplashFragment";

    @Override
    protected SplashViewModel getViewModel() {
        return new ViewModelProvider(this).get(SplashViewModel.class);
    }

    private Timer timer;

    @Override
    protected void initEvent() {
        super.initEvent();
        initAct();
    }

    protected void initAct() {
        Handler handler = new Handler();
        Runnable update = new Runnable() {
            int progress = 0;

            public void run() {
                progress += 1;
                binding.progress.setProgress(progress);
                if (progress == 99) {
                    boolean firstTime = PreferenceUtil.getBoolean(requireContext(), PreferenceUtil.OPEN_APP_FIRST_TIME, true);
                    if (firstTime)
                        replaceFragment(new LanguageFragment(), LanguageFragment.TAG);
                    else
                        replaceFragment(new HomeFragment(), HomeFragment.TAG);
                }
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 5, 25);
    }
}
