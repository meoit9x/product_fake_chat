package nat.pink.base.ui.language;

import android.graphics.Color;

import androidx.core.util.Consumer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder;

import nat.pink.base.R;
import nat.pink.base.adapter.AdapterLanguage;
import nat.pink.base.base.BaseFragment;
import nat.pink.base.databinding.FragmentLanguageBinding;
import nat.pink.base.ui.onboard.OnboardFragment;
import nat.pink.base.utils.Const;
import nat.pink.base.utils.PreferenceUtil;

public class LanguageFragment extends BaseFragment<FragmentLanguageBinding, LanguageViewModel> {

    public static final String TAG = "LanguageFragment";
    private AdapterLanguage adapterLanguage;

    @Override
    protected LanguageViewModel getViewModel() {
        return new ViewModelProvider(this).get(LanguageViewModel.class);
    }



    @Override
    protected void initView() {
        super.initView();
        adapterLanguage = new AdapterLanguage(requireContext(), data -> {
            PreferenceUtil.saveString(requireContext(), PreferenceUtil.SETTING_ENGLISH, data.getValue());
            adapterLanguage.notifyDataSetChanged();
        });
        binding.rcvEnglish.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rcvEnglish.setAdapter(adapterLanguage);
        binding.txtSave.setOnClickListener(v -> {
            replaceFragment(new OnboardFragment(), OnboardFragment.TAG);
        });
        setNativeAdView(createNativeAdView(), o -> {
            binding.nativeAdsLanguageHome.removeAllViews();
            binding.nativeAdsLanguageHome.addView((MaxNativeAdView)o);
            binding.nativeAdsLanguageHome.setBackgroundColor(Color.WHITE);
        });
    }

    private MaxNativeAdView createNativeAdView() {
        MaxNativeAdViewBinder binder = new MaxNativeAdViewBinder.Builder(R.layout.native_custom_ads_big)
                .setTitleTextViewId(R.id.txt_title)
                .setBodyTextViewId(R.id.txt_body)
                .setAdvertiserTextViewId(R.id.txt_advertiser)
                .setIconImageViewId(R.id.icon_image_view)
                .setMediaContentViewGroupId(R.id.media_view_container)
                .setOptionsContentViewGroupId(R.id.ad_options_view)
                .setCallToActionButtonId(R.id.cta_button)
                .build();
        return new MaxNativeAdView(binder, getContext());
    }

    @Override
    protected void initData() {
        super.initData();
        getViewModel().initData(requireContext());
        getViewModel().languages.observe(this, objectLanguages -> {
            adapterLanguage.setObjectLanguages(objectLanguages);
        });
        //createNativeAd(Const.KEY_ADS_LANGUAGE);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
    }
}
