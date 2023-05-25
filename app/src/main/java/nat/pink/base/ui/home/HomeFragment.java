package nat.pink.base.ui.home;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import nat.pink.base.ui.create.CreateUserFragment;
import nat.pink.base.R;
import nat.pink.base.adapter.AdapterFakeUser;
import nat.pink.base.base.BaseFragment;
import nat.pink.base.databinding.HomeFragmentBinding;
import nat.pink.base.dialog.DialogSelectChat;
import nat.pink.base.ui.notification.NotificationFragment;
import nat.pink.base.ui.video.VideoFragment;
import nat.pink.base.utils.Const;

public class HomeFragment extends BaseFragment<HomeFragmentBinding, HomeViewModel> {
    private AdapterFakeUser adapterFakeUser;
    public static final String TAG = "HomeFragment";
    private DialogSelectChat dialog;

    @Override
    protected HomeViewModel getViewModel() {
        return new ViewModelProvider(this).get(HomeViewModel.class);
    }

    @Override
    protected void initView() {
        super.initView();
        adapterFakeUser = new AdapterFakeUser(requireContext(), data -> {
            adapterFakeUser.notifyDataSetChanged();
        });
        LinearLayoutManager ln = new LinearLayoutManager(requireContext());
        ln.setOrientation(RecyclerView.HORIZONTAL);
        binding.rcvFakeUser.setLayoutManager(ln);
        binding.rcvFakeUser.setAdapter(adapterFakeUser);

        dialog = new DialogSelectChat(requireContext(), R.style.MaterialDialogSheet, user -> {
            if (user.getId() == -1) {
                addFragment(new CreateUserFragment(), CreateUserFragment.TAG);
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        getViewModel().initData(requireContext());
        getViewModel().users.observe(this, fakeUsers -> {
            adapterFakeUser.setFakeUsers(fakeUsers);
        });
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
        binding.fakeNoti.setOnClickListener(v->{
            addFragment(new NotificationFragment(),NotificationFragment.TAG);
        });
        binding.fakeVideo.setOnClickListener(view -> {
            addFragment(new VideoFragment(),VideoFragment.TAG);
        });

    }

    private void showMessage() {
        binding.frAdsHome.setVisibility(View.VISIBLE);
        dialog.show();
    }

    private void showAction(String key) {
//        if (key.equals(Const.KEY_ADS_VIDEO_CALL))
        //  showVideoCall();
        if (key.equals(Const.KEY_ADS_MESSAGE))
            showMessage();
//        if (key.equals(Const.KEY_ADS_NOTIFICATION))
        //   showNoti();
    }
}
