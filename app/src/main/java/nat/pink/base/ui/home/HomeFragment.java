package nat.pink.base.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import nat.pink.base.MainActivity;
import nat.pink.base.R;
import nat.pink.base.adapter.AdapterFakeUser;
import nat.pink.base.adapter.AdapterLanguage;
import nat.pink.base.base.BaseFragment;
import nat.pink.base.databinding.HomeFragmentBinding;
import nat.pink.base.dialog.DialogNetworkFail;
import nat.pink.base.dialog.DialogSelectChat;
import nat.pink.base.utils.Const;
import nat.pink.base.utils.InAppPurchase;
import nat.pink.base.utils.PreferenceUtil;
import nat.pink.base.utils.Utils;

public class HomeFragment extends BaseFragment<HomeFragmentBinding, HomeViewModel> {
    private AdapterFakeUser adapterFakeUser;
    public static final String TAG = "HomeFragment";

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
          showAds(Const.KEY_ADS_MESSAGE);
        });
    }
    private void showAds(String key) {
        if (Utils.isNetworkAvailable(requireActivity())) {
            if (isHideAds()||InAppPurchase.getInstance().isPurchaseAdsRemove()) {
                showAction(key);
                return;
            }
            Utils.showDialogRemoveAds(requireActivity(), o -> {
                binding.frAdsHome.setVisibility(View.GONE);
            }, o -> {
                setInterstitialAdUnitId(key);
                initInterstitialAd();
            });
        } else {
            new DialogNetworkFail(requireContext(), R.style.MaterialDialogSheet, false).show();
        }
    }

    private void showMessage() {
        binding.frAdsHome.setVisibility(View.VISIBLE);
      //  App.firebaseAnalytics.logEvent("Access_Message_Function", null);
      //  Toast.makeText(this.getContext(), "hello ", Toast.LENGTH_SHORT).show();
      //  ((MainActivity) getActivity()).addChildFragment(new FragmentListUser(), FragmentListUser.class.getSimpleName());

        DialogSelectChat dialog = new DialogSelectChat(requireContext(), R.style.MaterialDialogSheet, o -> {
            Toast.makeText(getContext(), "selected user", Toast.LENGTH_SHORT).show();
        });
//        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
//        InsetDrawable inset = new InsetDrawable(back, 100);
//        dialog.getWindow().setBackgroundDrawable(inset);
      // dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
