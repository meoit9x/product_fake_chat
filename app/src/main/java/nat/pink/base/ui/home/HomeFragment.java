package nat.pink.base.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.applovin.mediation.MaxAd;
//import com.applovin.mediation.MaxAdViewAdListener;
//import com.applovin.mediation.MaxError;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;
import java.util.Locale;

import nat.pink.base.base.App;
import nat.pink.base.databinding.HomeFragmentBinding;
import nat.pink.base.dialog.DialogEnoughPoints;
import nat.pink.base.model.ResponseDevice;
import nat.pink.base.model.ResponseFeedback;
import nat.pink.base.ui.MainActivity;
import nat.pink.base.dao.DatabaseController;
import nat.pink.base.dialog.DialogCountdownTime;
import nat.pink.base.dialog.DialogFeedback;
import nat.pink.base.dialog.DialogForceUpdate;
import nat.pink.base.dialog.DialogLoading;
import nat.pink.base.dialog.DialogNetworkFail;
import nat.pink.base.retrofit.RequestAPI;
import nat.pink.base.retrofit.RetrofitClient;
import nat.pink.base.ui.call.CallFragment;
import nat.pink.base.ui.chat.FragmentChat;
import nat.pink.base.ui.create.CreateUserFragment;
import nat.pink.base.R;
import nat.pink.base.adapter.AdapterFakeUser;
import nat.pink.base.base.BaseFragment;
import nat.pink.base.dialog.DialogSelectChat;
import nat.pink.base.ui.manager.ManagerContactFragment;
import nat.pink.base.ui.notification.NotificationFragment;
import nat.pink.base.ui.setting.CoinRankingFragment;
import nat.pink.base.ui.setting.FaqFragment;
import nat.pink.base.ui.setting.LanguageFragmentSetting;
import nat.pink.base.ui.setting.PrivacyFragment;
import nat.pink.base.ui.video.VideoFragment;
import nat.pink.base.utils.Const;
import nat.pink.base.utils.PreferenceUtil;
import nat.pink.base.utils.Utils;
import retrofit2.Retrofit;

public class HomeFragment extends BaseFragment<HomeFragmentBinding, HomeViewModel> {
    private AdapterFakeUser adapterFakeUser;
    public static final String TAG = "HomeFragment";
    private DialogSelectChat dialog;
    private String type = "";
    private String typeCountDown = "";
    private long time = 0;
    private long timePresent = 0;
    private long timePresentReward = 0;
    private int totalCoin = 0;
    private View navMenu;
    private DialogCountdownTime dialogCountdownTime;
    private DialogLoading dialogLoading;
    private DialogForceUpdate dialogForceUpdate;
    protected RequestAPI requestAPI;
    private AdView mAdView;
    private Handler handler;
    private Runnable runnable;
    private boolean isDaily = false;
    private static final int DELAY_MILLIS = 5000;

    @Override
    protected HomeViewModel getViewModel() {
        return new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
    }

    @Override
    protected void initView() {
        super.initView();
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        adapterFakeUser = new AdapterFakeUser(requireContext(), (position, user) -> {
            if (position == 0) {
                addFragment(new CreateUserFragment(), CreateUserFragment.TAG);
            } else {
                Intent intent = new Intent(requireActivity(), FragmentChat.class);
                intent.putExtra(Const.KEY_DATA_CONTACT, user.get(position - 1));
                startActivity(intent);
            }
        });
        LinearLayoutManager ln = new LinearLayoutManager(requireContext());
        ln.setOrientation(RecyclerView.HORIZONTAL);
        binding.rcvFakeUser.setLayoutManager(ln);
        binding.rcvFakeUser.setAdapter(adapterFakeUser);
        navMenu = binding.navView2.getHeaderView(0);

        timePresent = PreferenceUtil.getLong(requireContext(), PreferenceUtil.KEY_PRESENT_EVERYDAY);
        timePresentReward = PreferenceUtil.getLong(requireContext(), PreferenceUtil.KEY_PRESENT);

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
                        if (checkPointEnough(200)) {
                            getViewModel().updatePoint(requestAPI, Utils.deviceId(requireContext()), 2, 200);
                            addFragment(new VideoFragment(user, o -> {
                                updateCountdown(Const.KEY_ADS_VIDEO_CALL);
                            }), VideoFragment.TAG);
                        }
                        break;
                    case Const.KEY_ADS_VOICE_CALL:
                        if (checkPointEnough(100)) {
                            getViewModel().updatePoint(requestAPI, Utils.deviceId(requireContext()), 2, 100);
                            addFragment(new CallFragment(user, o -> {
                                updateCountdown(Const.KEY_ADS_VOICE_CALL);
                            }), VideoFragment.TAG);
                        }
                        break;
                    case Const.KEY_ADS_NOTIFICATION:
                        if (checkPointEnough(300)) {
                            getViewModel().updatePoint(requestAPI, Utils.deviceId(requireContext()), 2, 300);
                            addFragment(new NotificationFragment(user, o -> {
                                updateCountdown(Const.KEY_ADS_NOTIFICATION);
                            }), VideoFragment.TAG);
                        }
                        break;
                }
            }
        });
        dialogForceUpdate = new DialogForceUpdate(requireContext(), R.style.MaterialDialogSheet, v -> {
            final String appPackageName = requireActivity().getPackageName();
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        });
        dialogCountdownTime = new DialogCountdownTime(requireContext(), R.style.MaterialDialogSheet, o -> {
            totalCoin = Integer.parseInt(PreferenceUtil.getString(requireContext(), PreferenceUtil.KEY_TOTAL_COIN, "0"));
            binding.coinCount.setText(String.valueOf(totalCoin));
        });
        dialogLoading = new DialogLoading(requireContext(), R.style.MaterialDialogSheet, o -> dialogLoading.dismiss());
        mAdView = binding.adView;
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
//        binding.adsBannerView.setListener(new MaxAdViewAdListener() {
//            @Override
//            public void onAdExpanded(MaxAd maxAd) {
//
//            }
//
//            @Override
//            public void onAdCollapsed(MaxAd maxAd) {
//
//            }
//
//            @Override
//            public void onAdLoaded(MaxAd maxAd) {
//                Log.d("adsDebug", "test");
//            }
//
//            @Override
//            public void onAdDisplayed(MaxAd maxAd) {
//                Log.d("adsDebug", "test");
//            }
//
//            @Override
//            public void onAdHidden(MaxAd maxAd) {
//                Log.d("adsDebug", "test");
//            }
//
//            @Override
//            public void onAdClicked(MaxAd maxAd) {
//                App.getInstance().getFirebaseAnalytics().logEvent("ClickHomeNative", null);
//            }
//
//            @Override
//            public void onAdLoadFailed(String s, MaxError maxError) {
//                new Handler().postDelayed(() -> binding.adsBannerView.loadAd(), 1000);
//            }
//
//            @Override
//            public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {
//                binding.adsBannerView.loadAd();
//            }
//        });
//        binding.adsBannerView.loadAd();
//        binding.adsBannerView.startAutoRefresh();
        checkShowPresent();
        // createBannerAd(Const.KEY_ADS_HOME,binding.frAdsHome);
    }

    private boolean checkPointEnough(int point) {
        if (totalCoin - point < 0) {
            new DialogEnoughPoints(requireContext(), o -> {
                addFragment(new FaqFragment(true), FaqFragment.TAG);
            }).show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        Retrofit retrofit = RetrofitClient.getInstance(requireContext(), Const.URL_REQUEST);
        requestAPI = retrofit.create(RequestAPI.class);
        if (PreferenceUtil.getBoolean(requireContext(), PreferenceUtil.KEY_SETUP_DATA_DEFAULT, true)) {
            DatabaseController.getInstance(requireContext()).setupDataDefault();
        }
        getViewModel().getListContact(requireContext());
        getViewModel().contacts.observe(this, fakeUsers -> adapterFakeUser.setFakeUsers(fakeUsers));
        getViewModel().contactSuggest.observe(this, items -> dialog.setContactSuggest(items));
        getViewModel().contactNormal.observe(this, items -> dialog.setContactNormar(items));

        if (requireActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) requireActivity();
            if (!dialogLoading.isShowing())
                dialogLoading.show();
            getViewModel().foreUpdate(App.getInstance().getFirebaseDatabase(), requireContext());
            getViewModel().forceUpdate.observe(requireActivity(), v -> {
                activity.runOnUiThread(() -> {
                    dialogLoading.dismiss();
                    if (v) {
                        if (!dialogForceUpdate.isShowing())
                            dialogForceUpdate.show();
                    }
                });
            });
        }
        getViewModel().getPoint(requestAPI, Utils.deviceId(requireContext()));
        getViewModel().totalCoin.observe(this, totalCoin -> {
            if (totalCoin.getStatus() == 1) {
                PreferenceUtil.saveString(requireContext(), PreferenceUtil.KEY_TOTAL_COIN, String.valueOf(totalCoin.getPoints()));
                checkShowPresent();
            }
        });
        getViewModel().updateCoin.observe(this, responseUpdatePoint -> {
            if (responseUpdatePoint.getStatus() == 1) {
                PreferenceUtil.saveString(requireContext(), PreferenceUtil.KEY_TOTAL_COIN, String.valueOf(responseUpdatePoint.getPoints()));
                checkShowPresent();
            }
        });

    }

    @Override
    protected void initEvent() {
        super.initEvent();
        binding.menu.setOnClickListener(v -> {
            binding.drawerLayout.openDrawer(GravityCompat.END);
        });
        binding.present.setOnClickListener(v -> {
            createInterstitialAd(Const.KEY_ADMOB_POINT, o -> {
                if (showInterstitialAd(o1 -> {
                    requireActivity().runOnUiThread(() -> {
                        PreferenceUtil.saveLong(requireContext(), PreferenceUtil.KEY_PRESENT, System.currentTimeMillis());
                        dialogCountdownTime.setTimeAndTitle(0L, Const.KEY_ADS_PRESENT);
                        dialogCountdownTime.show();
                        getViewModel().updatePoint(requestAPI, Utils.deviceId(requireContext()), 1, 200);
                    });
                })) ;
            });
            binding.present.setVisibility(View.GONE);
        });
        handler = new Handler();
        runnable = () -> {
            if (timePresent == 0 || isNewDateGreaterThanGiven(timePresent)) {
                PreferenceUtil.saveLong(requireContext(), PreferenceUtil.KEY_PRESENT_EVERYDAY, System.currentTimeMillis());
                dialogCountdownTime.setTimeAndTitle(0L, Const.KEY_ADS_PRESENT_EVERYDAY);
                dialogCountdownTime.show();
                getViewModel().updatePoint(requestAPI, Utils.deviceId(requireContext()), 1, 300);
                isDaily = true;
            }
        };
        handler.postDelayed(runnable, DELAY_MILLIS);
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
            addFragment(new ManagerContactFragment(), ManagerContactFragment.TAG);
        });
        navMenu.findViewById(R.id.ll_guide).setOnClickListener(view -> {
            binding.drawerLayout.closeDrawers();
            addFragment(new CoinRankingFragment(), CoinRankingFragment.TAG);
        });
        navMenu.findViewById(R.id.ll_language).setOnClickListener(view -> {
            binding.drawerLayout.closeDrawers();
            addFragment(new LanguageFragmentSetting(), LanguageFragmentSetting.TAG);
        });
        navMenu.findViewById(R.id.ll_rate_app).setOnClickListener(view -> {
            binding.drawerLayout.closeDrawers();
            addFragment(new FaqFragment(), FaqFragment.TAG);
        });
        navMenu.findViewById(R.id.ll_privacy).setOnClickListener(view -> {
            binding.drawerLayout.closeDrawers();
            addFragment(new PrivacyFragment(), PrivacyFragment.TAG);
        });
        navMenu.findViewById(R.id.ll_contact_us).setOnClickListener(view -> {
            binding.drawerLayout.closeDrawers();
            new DialogFeedback(requireContext(), R.style.TransparentDialog, o -> {
                dialogLoading.show();
                PackageManager manager = requireContext().getPackageManager();
                PackageInfo info;
                try {
                    info = manager.getPackageInfo(
                            requireContext().getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    throw new RuntimeException(e);
                }
                getViewModel().feedback(requestAPI, requireActivity().getPackageName(), info.versionName, o, o1 -> {
                    dialogLoading.dismiss();
                    if (o1 instanceof ResponseFeedback) {
                        dialogCountdownTime = new DialogCountdownTime(requireContext(), R.style.MaterialDialogSheet, o2 -> {
                        });
                        dialogCountdownTime.setTimeAndTitle(0L, Const.KEY_ADS_DONE);
                        dialogCountdownTime.show();
                    } else {
                        new DialogNetworkFail(requireContext(), R.style.MaterialDialogSheet, true).show();
                    }
                });
            }).show();
        });
    }

    private void checkShowPresent() {
        timePresentReward = PreferenceUtil.getLong(requireContext(), PreferenceUtil.KEY_PRESENT);
        if (areTimesThreeMinutesApart(System.currentTimeMillis(), timePresentReward)) {
            binding.present.setVisibility(View.VISIBLE);
            binding.timeCountDown.setVisibility(View.GONE);
        } else {
            if (!isDaily) {
                countDownPresent(Const.TOTAL_TIME_MS - Math.abs(System.currentTimeMillis() - timePresentReward));
                binding.present.setVisibility(View.GONE);
                binding.timeCountDown.setVisibility(View.VISIBLE);
            }
        }
        totalCoin = Integer.parseInt(PreferenceUtil.getString(requireContext(), PreferenceUtil.KEY_TOTAL_COIN, "0"));
        binding.coinCount.setText(String.valueOf(totalCoin));
        isDaily = false;
    }

    private void showMessage() {
        dialog.setTypeAction(DialogSelectChat.TYPE_ACTION.ACTION_MESSAGE);
        dialog.show();
    }

    private void showVoiceCall() {
        checkShowNoti(DialogSelectChat.TYPE_ACTION.ACTION_VOICE);
    }

    private void showVideoCall() {
        checkShowNoti(DialogSelectChat.TYPE_ACTION.ACTION_VIDEO);
    }

    private void showNotification() {
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
            dialogCountdownTime = new DialogCountdownTime(requireContext(), R.style.MaterialDialogSheet, o -> {
            });
            dialogCountdownTime.setTimeAndTitle(time, typeCountDown);
            dialogCountdownTime.show();
        }
    }

    public boolean isNewDateGreaterThanGiven(long givenTimeInMillis) {
        Calendar givenTime = Calendar.getInstance();
        givenTime.setTimeInMillis(givenTimeInMillis);

        Calendar currentTime = Calendar.getInstance();

        if (givenTime.get(Calendar.YEAR) != currentTime.get(Calendar.YEAR)
                || givenTime.get(Calendar.MONTH) != currentTime.get(Calendar.MONTH)
                || givenTime.get(Calendar.DAY_OF_MONTH) != currentTime.get(Calendar.DAY_OF_MONTH)) {
            return currentTime.getTimeInMillis() > givenTime.getTimeInMillis();
        }
        return false;
    }

    public boolean areTimesThreeMinutesApart(long time1, long time2) {
        long timeDifference = Math.abs(time1 - time2);
        return timeDifference >= Const.TOTAL_TIME_MS;
    }

    private void countDownPresent(long time) {
        CountDownTimer countDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / (60 * 1000);
                long seconds = (millisUntilFinished % (60 * 1000)) / 1000;
                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                binding.timeCountDown.setText(timeLeftFormatted);
            }

            @Override
            public void onFinish() {
                checkShowPresent();
            }
        };
        countDownTimer.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
