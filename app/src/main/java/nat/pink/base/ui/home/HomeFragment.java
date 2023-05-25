package nat.pink.base.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Timer;

import nat.pink.base.dao.DatabaseController;
import nat.pink.base.model.DaoContact;
import nat.pink.base.ui.call.CallFragment;
import nat.pink.base.ui.chat.FragmentChat;
import nat.pink.base.ui.create.CreateUserFragment;
import nat.pink.base.R;
import nat.pink.base.adapter.AdapterFakeUser;
import nat.pink.base.base.BaseFragment;
import nat.pink.base.databinding.HomeFragmentBinding;
import nat.pink.base.dialog.DialogSelectChat;
import nat.pink.base.ui.notification.NotificationFragment;
import nat.pink.base.utils.Config;
import nat.pink.base.utils.Const;
import nat.pink.base.utils.PreferenceUtil;

public class HomeFragment extends BaseFragment<HomeFragmentBinding, HomeViewModel> {
    private AdapterFakeUser adapterFakeUser;
    public static final String TAG = "HomeFragment";
    private DialogSelectChat dialog;
    private Timer timer;
    private String timeString = "";
    private Handler handlerTime = new Handler();
    private Runnable updateTime;
    private ScreenReceiver mReceiver;
    public static final int RESULT_PAUSE = 1001;

    @Override
    protected HomeViewModel getViewModel() {
        return new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
    }

    @Override
    protected void initView() {
        super.initView();
        adapterFakeUser = new AdapterFakeUser(requireContext(), data -> {
            if (data == 0) addFragment(new CreateUserFragment(), CreateUserFragment.TAG);
        });
        LinearLayoutManager ln = new LinearLayoutManager(requireContext());
        ln.setOrientation(RecyclerView.HORIZONTAL);
        binding.rcvFakeUser.setLayoutManager(ln);
        binding.rcvFakeUser.setAdapter(adapterFakeUser);

        dialog = new DialogSelectChat(requireContext(), R.style.MaterialDialogSheet, user -> {
            dialog.dismiss();
            if (user.getId() == -1) {
                addFragment(new CreateUserFragment(), CreateUserFragment.TAG);
            } else {
                addFragment(new FragmentChat(user), FragmentChat.TAG);
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
            addFragment(new CallFragment(new DaoContact(2, "Cristiano Ronaldo", 1, true,true,1, "harvard","new castle","",Uri.parse("android.resource://" + getContext().getPackageName() + "/drawable/ronaldo").toString())), CallFragment.TAG);
        });

    }

    private void showMessage() {
        binding.frAdsHome.setVisibility(View.VISIBLE);
        dialog.show();
    }

    private void showAction(String key) {
//        if (key.equals(Const.KEY_ADS_VIDEO_CALL))
        //  showVideoCall();
        if (key.equals(Const.KEY_ADS_MESSAGE)) showMessage();
//        if (key.equals(Const.KEY_ADS_NOTIFICATION))
        //   showNoti();
    }

    public class ScreenReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                Intent returnIntent = new Intent();
                getActivity().setResult(RESULT_PAUSE, returnIntent);
                backStackFragment();
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.CHECK_TURN_OFF_VOICE_INCOMING) {
            if (resultCode == this.RESULT_PAUSE) {
                getActivity().setResult(resultCode, data);
            }
            this.backStackFragment();
        }
    }
}
