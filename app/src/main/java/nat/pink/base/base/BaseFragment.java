package nat.pink.base.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.concurrent.TimeUnit;

import nat.pink.base.MainActivity;
import nat.pink.base.model.DaoContact;

public abstract class BaseFragment<VB extends ViewBinding, VM extends BaseViewModel> extends Fragment {

    protected abstract VM getViewModel();
    private boolean hideAds = true;
    protected VB binding;
    public VB getBinding() {
        return binding;
    }

    public void setBinding(VB binding) {
        this.binding = binding;
    }

    public boolean isHideAds() {
        return hideAds;
    }

    public void setHideAds(boolean hideAds) {
        this.hideAds = hideAds;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
        initObserver();
        initEvent();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Class<VB> clazz = (Class<VB>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        try {
            Method inflateMethod = clazz.getMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
            binding = (VB) inflateMethod.invoke(null, inflater, container, false);
            return binding.getRoot();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void initEvent() {
    }

    protected void initObserver() {
    }

    protected void initData() {
    }

    protected void initView() {
    }

    protected void backStackFragment() {
        if (requireActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) requireActivity();
            activity.onBackPressed();
        }
    }

    protected void replaceFragment(Fragment fragment, String tag) {
        if (requireActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) requireActivity();
            activity.replaceFragment(fragment, tag);
        }
    }

    protected void addFragment(Fragment fragment, String tag) {
        if (requireActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) requireActivity();
            activity.addFragment(fragment, tag);
        }
    }
    protected boolean showInterstitialAd(Consumer consumer) {
        if (requireActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) requireActivity();
            return activity.showInterstitialAd(consumer);
        }
        return false;
    }
    protected void createInterstitialAd(String key) {
        if (requireActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) requireActivity();
            activity.createInterstitialAd(key);
        }
    }






    //AppLovin Ads Integrate!!!
    protected void initInterstitialAd() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setLoadingAdsView(true);
        }
    }
}
