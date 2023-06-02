package nat.pink.base.ui.home;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;

import nat.pink.base.dao.DatabaseController;
import nat.pink.base.dialog.DialogCountdownTime;
import nat.pink.base.ui.call.CallFragment;
import nat.pink.base.ui.chat.FragmentChat;
import nat.pink.base.ui.create.CreateUserFragment;
import nat.pink.base.R;
import nat.pink.base.adapter.AdapterFakeUser;
import nat.pink.base.base.BaseFragment;
import nat.pink.base.databinding.HomeFragmentBinding;
import nat.pink.base.dialog.DialogSelectChat;
import nat.pink.base.ui.language.LanguageFragment;
import nat.pink.base.ui.manager.ManagerContactFragment;
import nat.pink.base.ui.notification.NotificationFragment;
import nat.pink.base.ui.setting.LanguageFragmentSetting;
import nat.pink.base.ui.video.VideoFragment;
import nat.pink.base.utils.Const;
import nat.pink.base.utils.PreferenceUtil;
import nat.pink.base.utils.Utils;

public class HomeFragment extends BaseFragment<HomeFragmentBinding, HomeViewModel> {
    private AdapterFakeUser adapterFakeUser;
    public static final String TAG = "HomeFragment";
    private DialogSelectChat dialog;
    private String type = "";
    private String typeCountDown = "";
    private long time = 0;
    private View navMenu;

    @Override
    protected HomeViewModel getViewModel() {
        return new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
    }

    @Override
    protected void initView() {
        super.initView();
        adapterFakeUser = new AdapterFakeUser(requireContext(), data -> {
            if (data == 0)
                addFragment(new CreateUserFragment(), CreateUserFragment.TAG);
        });
        LinearLayoutManager ln = new LinearLayoutManager(requireContext());
        ln.setOrientation(RecyclerView.HORIZONTAL);
        binding.rcvFakeUser.setLayoutManager(ln);
        binding.rcvFakeUser.setAdapter(adapterFakeUser);
        navMenu = binding.navView2.getHeaderView(0);

        dialog = new DialogSelectChat(requireContext(), R.style.MaterialDialogSheet, user -> {
            dialog.dismiss();
            if (user.getId() == -1) {
                addFragment(new CreateUserFragment(), CreateUserFragment.TAG);
            } else {
                switch (type) {
                    case Const.KEY_ADS_MESSAGE:
                        Intent intent = new Intent(requireActivity(), FragmentChat.class);
                        intent.putExtra(Const.KEY_DATA_CONTACT, user);
                        startActivity(intent);
                        break;
                    case Const.KEY_ADS_VIDEO_CALL:
                        addFragment(new VideoFragment(user, o -> {
                            updateCountdown(Const.KEY_ADS_VIDEO_CALL);
                        }), VideoFragment.TAG);
                        break;
                    case Const.KEY_ADS_VOICE_CALL:
                        addFragment(new CallFragment(user, o -> {
                            updateCountdown(Const.KEY_ADS_VOICE_CALL);
                        }), VideoFragment.TAG);
                        break;
                    case Const.KEY_ADS_NOTIFICATION:
                        addFragment(new NotificationFragment(user, o -> {
                            updateCountdown(Const.KEY_ADS_NOTIFICATION);
                        }), VideoFragment.TAG);
                        break;
                }
            }
        });
        binding.adsBannerView.setListener(new MaxAdViewAdListener() {
            @Override
            public void onAdExpanded(MaxAd maxAd) {

            }

            @Override
            public void onAdCollapsed(MaxAd maxAd) {

            }

            @Override
            public void onAdLoaded(MaxAd maxAd) {
                Log.d("adsDebug","test");
            }

            @Override
            public void onAdDisplayed(MaxAd maxAd) {
                Log.d("adsDebug","test");
            }

            @Override
            public void onAdHidden(MaxAd maxAd) {
                Log.d("adsDebug","test");
            }

            @Override
            public void onAdClicked(MaxAd maxAd) {

            }

            @Override
            public void onAdLoadFailed(String s, MaxError maxError) {
                new Handler().postDelayed(() -> binding.adsBannerView.loadAd(), 1000);
            }

            @Override
            public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {
                binding.adsBannerView.loadAd();
            }
        });
        binding.adsBannerView.loadAd();
        binding.adsBannerView.startAutoRefresh();
      // createBannerAd(Const.KEY_ADS_HOME,binding.frAdsHome);
    }

    @Override
    protected void initData() {
        super.initData();
        if (PreferenceUtil.getBoolean(requireContext(), PreferenceUtil.KEY_SETUP_DATA_DEFAULT, true)) {
            DatabaseController.getInstance(requireContext()).setupDataDefault();
        }
        getViewModel().getListContact(requireContext());
        getViewModel().contacts.observe(this, fakeUsers -> adapterFakeUser.setFakeUsers(fakeUsers));
        getViewModel().contactSuggest.observe(this, items -> dialog.setContactSuggest(items));
        getViewModel().contactNormal.observe(this, items -> dialog.setContactNormar(items));
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        binding.menu.setOnClickListener(v -> {
            binding.drawerLayout.openDrawer(GravityCompat.END);
        });
        binding.fakeMessage.setOnClickListener(v -> {
            showAction(Const.KEY_ADS_MESSAGE);
        });
        binding.fakeNoti.setOnClickListener(v -> {
            showAction(Const.KEY_ADS_NOTIFICATION);
        });
        binding.fakeVoice.setOnClickListener(v -> {
            showAction(Const.KEY_ADS_VOICE_CALL);
        });
        binding.fakeVideo.setOnClickListener(view -> {
            showAction(Const.KEY_ADS_VIDEO_CALL);
        });
        navMenu.findViewById(R.id.ll_home).setOnClickListener(v -> binding.drawerLayout.closeDrawers());
        navMenu.findViewById(R.id.ll_manager_coin).setOnClickListener(v -> {
            binding.drawerLayout.closeDrawers();
            addFragment(new ManagerContactFragment(),ManagerContactFragment.TAG);
        });
        navMenu.findViewById(R.id.ll_language).setOnClickListener(view -> {
            binding.drawerLayout.closeDrawers();
            addFragment(new LanguageFragmentSetting(), LanguageFragmentSetting.TAG);
        });
    }

    private void showMessage() {
        binding.frAdsHome.setVisibility(View.VISIBLE);
        dialog.setTypeAction(DialogSelectChat.TYPE_ACTION.ACTION_MESSAGE);
        dialog.show();
    }

    private void showVoiceCall() {
        binding.frAdsHome.setVisibility(View.VISIBLE);
        checkShowNoti(DialogSelectChat.TYPE_ACTION.ACTION_VOICE);
    }

    private void showVideoCall() {
        binding.frAdsHome.setVisibility(View.VISIBLE);
        checkShowNoti(DialogSelectChat.TYPE_ACTION.ACTION_VIDEO);
    }

    private void showNotification() {
        binding.frAdsHome.setVisibility(View.VISIBLE);
        checkShowNoti(DialogSelectChat.TYPE_ACTION.ACTION_NOTIFICATION);
    }


    private void showAction(String key) {
        type = key;
        switch (key) {
            case Const.KEY_ADS_VIDEO_CALL:
                showVideoCall();
                break;
            case Const.KEY_ADS_MESSAGE:
                showMessage();
                break;
            case Const.KEY_ADS_VOICE_CALL:
                showVoiceCall();
                break;
            case Const.KEY_ADS_NOTIFICATION:
                showNotification();
                break;
            default:
        }
    }

    private void updateCountdown(String key) {
        time = PreferenceUtil.getLong(requireContext(), PreferenceUtil.KEY_CURRENT_TIME);
        CountDownTimer countDownTimer = new CountDownTimer(time - System.currentTimeMillis(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                typeCountDown = key;
                String currentTime = Utils.formatDuration(millisUntilFinished / 1000, true);
                if (isAdded()) {
                    switch (key) {
                        case Const.KEY_ADS_VIDEO_CALL: {
                            int showNoti = PreferenceUtil.getInt(requireContext(), PreferenceUtil.KEY_CALLING_VIDEO);
                            int showPopup = PreferenceUtil.getInt(requireContext(), PreferenceUtil.KEY_COMMING_VIDEO);
                            if (time != 0 && (showNoti == 1 || showPopup == 1)) {
                                binding.txtVideoCall.setText(currentTime);
                                binding.txtVideoCall.setTextColor(requireContext().getColor(R.color.color_FE294D));
                                binding.txtVideoCall.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clock_circle_video_call, 0, 0, 0);
                            }
                            break;
                        }
                        case Const.KEY_ADS_VOICE_CALL: {
                            int showNoti = PreferenceUtil.getInt(requireContext(), PreferenceUtil.KEY_CALLING_VOICE);
                            int showPopup = PreferenceUtil.getInt(requireContext(), PreferenceUtil.KEY_COMMING_VOICE);
                            if (time != 0 && (showNoti == 1 || showPopup == 1)) {
                                binding.txtVoiceCall.setText(currentTime);
                                binding.txtVoiceCall.setTextColor(requireContext().getColor(R.color.color_FE294D));
                                binding.txtVoiceCall.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clock_circle_video_call, 0, 0, 0);
                            }
                            break;
                        }
                        case Const.KEY_ADS_NOTIFICATION: {
                            int showNoti = PreferenceUtil.getInt(requireContext(), PreferenceUtil.KEY_CURRENT_TIME_NOTI);
                            int showPopup = PreferenceUtil.getInt(requireContext(), PreferenceUtil.KEY_SHOW_POPUP);
                            if (time != 0 && (showNoti == 1 || showPopup == 1)) {
                                binding.txtNoti.setText(currentTime);
                                binding.txtNoti.setTextColor(requireContext().getColor(R.color.color_FE294D));
                                binding.txtNoti.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clock_circle_video_call, 0, 0, 0);
                            }
                            break;
                        }
                    }

                }
            }

            @Override
            public void onFinish() {
                typeCountDown = "";
                switch (key) {
                    case Const.KEY_ADS_VIDEO_CALL:
                        binding.txtVideoCall.setText(getString(R.string.two_hun_coin));
                        binding.txtVideoCall.setTextColor(requireContext().getColor(R.color.color_D770AD));
                        binding.txtVideoCall.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        break;
                    case Const.KEY_ADS_VOICE_CALL:
                        binding.txtVoiceCall.setText(getString(R.string.one_hun_coin));
                        binding.txtVoiceCall.setTextColor(requireContext().getColor(R.color.color_27839E));
                        binding.txtVoiceCall.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        break;
                    case Const.KEY_ADS_NOTIFICATION:
                        binding.txtNoti.setText(getString(R.string.three_hun_coin));
                        binding.txtNoti.setTextColor(requireContext().getColor(R.color.color_8DCC78));
                        binding.txtNoti.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        break;
                }
            }
        };
        countDownTimer.start();
    }

    private void checkShowNoti(DialogSelectChat.TYPE_ACTION typeAction) {
        time = PreferenceUtil.getLong(requireContext(), PreferenceUtil.KEY_CURRENT_TIME);
        long countdown = time - System.currentTimeMillis();
        if (countdown < 0) {
            PreferenceUtil.clearEdit(requireContext(), PreferenceUtil.KEY_CURRENT_TIME);
            dialog.setTypeAction(typeAction);
            dialog.show();
        } else {
            DialogCountdownTime dialogCountdownTime = new DialogCountdownTime(requireContext(), R.style.MaterialDialogSheet, o -> {
            });
            dialogCountdownTime.setTimeAndTitle(time, typeCountDown);
            dialogCountdownTime.show();
        }
    }

}
