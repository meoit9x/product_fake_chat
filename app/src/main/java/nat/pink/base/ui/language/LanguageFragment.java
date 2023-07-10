package nat.pink.base.ui.language;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.util.Consumer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

//import com.applovin.mediation.nativeAds.MaxNativeAdView;
//import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder;

import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import nat.pink.base.R;
import nat.pink.base.adapter.AdapterLanguage;
import nat.pink.base.base.BaseFragment;
import nat.pink.base.databinding.FragmentLanguageBinding;
import nat.pink.base.model.ObjectLanguage;
import nat.pink.base.model.ResponseDevice;
import nat.pink.base.model.ResponseUpdatePoint;
import nat.pink.base.retrofit.RequestAPI;
import nat.pink.base.retrofit.RetrofitClient;
import nat.pink.base.ui.onboard.OnboardFragment;
import nat.pink.base.ui.splah.SplashActivity;
import nat.pink.base.utils.Const;
import nat.pink.base.utils.PreferenceUtil;
import nat.pink.base.utils.Utils;
import retrofit2.Retrofit;

public class LanguageFragment extends BaseFragment<FragmentLanguageBinding, LanguageViewModel> {

    public static final String TAG = "LanguageFragment";
    private AdapterLanguage adapterLanguage;
    boolean isChanged = false;
    protected RequestAPI requestAPI;

    @Override
    protected LanguageViewModel getViewModel() {
        return new ViewModelProvider(this).get(LanguageViewModel.class);
    }


    @Override
    protected void initView() {
        super.initView();
        adapterLanguage = new AdapterLanguage(requireContext(), data -> {
            isChanged = true;
            PreferenceUtil.saveString(requireContext(), PreferenceUtil.KEY_CURRENT_LANGUAGE, data.getValue());
            adapterLanguage.notifyDataSetChanged();
        });
        binding.rcvEnglish.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rcvEnglish.setAdapter(adapterLanguage);
        binding.txtSave.setOnClickListener(v -> {
            if (isChanged) {
                Intent intent = new Intent(getContext(), SplashActivity.class);
                intent.setAction("ACTION_CHANGE_LANGUAGE");
                startActivity(intent);
                requireActivity().finish();
            } else {
                replaceFragment(new OnboardFragment(), OnboardFragment.TAG);
            }

        });
        createNativeAdView();
    }

    private void createNativeAdView() {

//        AdLoader.Builder builder = new AdLoader.Builder(getContext(), "ca-app-pub-3940256099942544/2247696110")
//                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
//                    @Override
//                    public void onNativeAdLoaded(NativeAd nativeAd) {
//                        // Assumes you have a placeholder FrameLayout in your View layout
//                        // (with id fl_adplaceholder) where the ad is to be placed.
//                        FrameLayout frameLayout = binding.nativeAdsLanguageHome;
//                        // Assumes that your ad layout is in a file call native_ad_layout.xml
//                        // in the res/layout folder
//                        NativeAdView adView = (NativeAdView) getLayoutInflater()
//                                .inflate(R.layout.native_custom_mob_ads_big, null);
//                        // This method sets the text, images and the native ad, etc into the ad
//                        // view.
//                        populateNativeAdView(nativeAd, adView);
//                        frameLayout.removeAllViews();
//                        frameLayout.addView(adView);
//                    }
//                });
//        builder.build();
        createNativeAd(Const.KEY_ADMOB_NATIVE_TEST);
        setNativeAdConsumer(o -> {
            if (o instanceof String) {
                return;
            }
            NativeAdView adView = (NativeAdView) getLayoutInflater().inflate(R.layout.native_custom_mob_ads_big, null);
            populateNativeAdView((NativeAd) o, adView);
            FrameLayout frameLayout = binding.nativeAdsLanguageHome;
            frameLayout.removeAllViews();
            frameLayout.addView(adView);
        });
    }

    void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        TextView headLine = adView.findViewById(R.id.txt_title);
        TextView advertiser = adView.findViewById(R.id.txt_advertiser);
        TextView body = adView.findViewById(R.id.txt_body);
        ImageView icon = adView.findViewById(R.id.icon_image_view);
        MediaView media = adView.findViewById(R.id.media_view_container);
        Button action = adView.findViewById(R.id.cta_button);

        headLine.setText(nativeAd.getHeadline());
        adView.setHeadlineView(headLine);
        advertiser.setText(nativeAd.getAdvertiser());
        adView.setAdvertiserView(advertiser);
        body.setText(nativeAd.getBody());
        adView.setBodyView(body);
        if(nativeAd.getIcon() != null)
        icon.setImageDrawable(nativeAd.getIcon().getDrawable());
        action.setText(nativeAd.getStore());
        adView.setCallToActionView(action);
        media.setMediaContent(nativeAd.getMediaContent());
        adView.setMediaView(media);
        adView.setNativeAd(nativeAd);
    }

//    private MaxNativeAdView createNativeAdView() {
//        MaxNativeAdViewBinder binder = new MaxNativeAdViewBinder.Builder(R.layout.native_custom_ads_big).setTitleTextViewId(R.id.txt_title).setBodyTextViewId(R.id.txt_body).setAdvertiserTextViewId(R.id.txt_advertiser).setIconImageViewId(R.id.icon_image_view).setMediaContentViewGroupId(R.id.media_view_container).setOptionsContentViewGroupId(R.id.ad_options_view).setCallToActionButtonId(R.id.cta_button).build();
//        return new MaxNativeAdView(binder, getContext());
//    }

    @Override
    protected void initData() {
        super.initData();
        Retrofit retrofit = RetrofitClient.getInstance(requireContext(), Const.URL_REQUEST);
        requestAPI = retrofit.create(RequestAPI.class);
        getViewModel().initData(requireContext());
        getViewModel().languages.observe(this, objectLanguages -> {
            adapterLanguage.setObjectLanguages(objectLanguages);
        });
        //default english
        PreferenceUtil.saveString(requireContext(), PreferenceUtil.KEY_CURRENT_LANGUAGE, "en");
        getViewModel().getPoint(requestAPI, Utils.deviceId(requireContext()), o -> {
            if (o instanceof ResponseDevice) {
                ResponseDevice responseDevice = (ResponseDevice) o;
                PreferenceUtil.saveInt(requireContext(), PreferenceUtil.KEY_USER_ID, responseDevice.getUserId());
                if(responseDevice.getFirstTime() && responseDevice.getPoints() == 0) {
                    getViewModel().updatePoint(requestAPI, Utils.deviceId(requireContext()), 1, 200, o1 -> {
                        if(o1 instanceof ResponseUpdatePoint) {
                            ResponseUpdatePoint responseUpdatePoint = (ResponseUpdatePoint) o1;
                            PreferenceUtil.saveString(requireContext(), PreferenceUtil.KEY_TOTAL_COIN, String.valueOf(responseUpdatePoint.getPoints()));
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void initEvent() {
        super.initEvent();
    }
}
