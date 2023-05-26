package nat.pink.base.ui.home;

import android.os.CountDownTimer;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import nat.pink.base.MainActivity;
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
import nat.pink.base.ui.notification.NotificationFragment;
import nat.pink.base.ui.video.VideoFragment;
import nat.pink.base.utils.Const;
import nat.pink.base.utils.PreferenceUtil;
import nat.pink.base.utils.Utils;

public class HomeFragment extends BaseFragment<HomeFragmentBinding, HomeViewModel> {
    private AdapterFakeUser adapterFakeUser;
    public static final String TAG = "HomeFragment";
    private DialogSelectChat dialog;
    private String type = "";
    private long time = 0;

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
        binding.txtVideoCall.setTextColor(requireContext().getColor(R.color.color_FE294D));

        dialog = new DialogSelectChat(requireContext(), R.style.MaterialDialogSheet, user -> {
            dialog.dismiss();
            if (user.getId() == -1) {
                addFragment(new CreateUserFragment(), CreateUserFragment.TAG);
            } else {
                if (type.equals(Const.KEY_ADS_MESSAGE)) {
                    addFragment(new FragmentChat(user), FragmentChat.TAG);
                } else if (type.equals(Const.KEY_ADS_VIDEO_CALL)) {
                    addFragment(new VideoFragment(user, o -> {
                        countDownVideo();
                    }), VideoFragment.TAG);
                }
            }
        });
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
        countDownVideo();
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
            addFragment(new NotificationFragment(), NotificationFragment.TAG);
        });
        binding.fakeVoice.setOnClickListener(v -> {
            addFragment(new CallFragment(), CallFragment.TAG);
        });
        binding.fakeVideo.setOnClickListener(view -> {
            showAction(Const.KEY_ADS_VIDEO_CALL);
        });

    }

    private void showMessage() {
        binding.frAdsHome.setVisibility(View.VISIBLE);
        dialog.show();
    }

    private void showVideoCall() {
        binding.frAdsHome.setVisibility(View.VISIBLE);
        checkShowNoti(PreferenceUtil.KEY_CALLING_VIDEO, PreferenceUtil.KEY_COMMING_VIDEO);
    }

    private void showAction(String key) {
        type = key;
        if (key.equals(Const.KEY_ADS_VIDEO_CALL)) {
            showVideoCall();
        } else if (key.equals(Const.KEY_ADS_MESSAGE)) {
            showMessage();
        }
//        if (key.equals(Const.KEY_ADS_NOTIFICATION))
        //   showNoti();
    }

    private void countDownVideo() {
        time = PreferenceUtil.getLong(requireContext(), PreferenceUtil.KEY_CURRENT_TIME);
        CountDownTimer countDownTimer = new CountDownTimer(time - System.currentTimeMillis(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String currentTime = Utils.formatDuration(millisUntilFinished / 1000, true);
                if (isAdded()) {
                    int showNoti = PreferenceUtil.getInt(requireContext(), PreferenceUtil.KEY_CALLING_VIDEO);
                    int showPopup = PreferenceUtil.getInt(requireContext(), PreferenceUtil.KEY_COMMING_VIDEO);
                    if (time != 0 && (showNoti == 1 || showPopup == 1)) {
                        binding.txtVideoCall.setText(currentTime);
                        binding.txtVideoCall.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clock_circle_video_call, 0, 0, 0);
                    }
                }
            }

            @Override
            public void onFinish() {
                binding.txtVideoCall.setText("-200 coin");
                binding.txtVideoCall.setTextColor(requireContext().getColor(R.color.color_FE294D));
                binding.txtVideoCall.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
        };
        countDownTimer.start();
    }

    private void checkShowNoti(String key1, String key2) {
        time = PreferenceUtil.getLong(requireContext(), PreferenceUtil.KEY_CURRENT_TIME);
        int showNoti = PreferenceUtil.getInt(requireContext(), key1);
        int showPopup = PreferenceUtil.getInt(requireContext(), key2);
        long countdown = time - System.currentTimeMillis();
        if (time != 0 && (showNoti == 1 || showPopup == 1)) {
            if (countdown < 0) {
                PreferenceUtil.clearEdit(requireContext(), PreferenceUtil.KEY_CURRENT_TIME);
                dialog.show();
            } else {
                DialogCountdownTime dialogCountdownTime = new DialogCountdownTime(requireContext(), R.style.MaterialDialogSheet, o -> {
                });
                dialogCountdownTime.setTimeAndTitle(time, type);
                dialogCountdownTime.show();
            }
        } else {
            if (time == 0) {
                dialog.show();
            } else {
                if (countdown < 0) {
                    PreferenceUtil.clearEdit(requireContext(), PreferenceUtil.KEY_CURRENT_TIME);
                    dialog.show();
                } else {
                    DialogCountdownTime dialogCountdownTime = new DialogCountdownTime(requireContext(), R.style.MaterialDialogSheet, o -> {
                    });
                    dialogCountdownTime.show();
                }
            }
        }
    }
}
