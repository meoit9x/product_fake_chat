package nat.pink.base.ui.setting;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import nat.pink.base.R;
import nat.pink.base.base.BaseFragment;
import nat.pink.base.databinding.FragmentPrivacyBinding;
import nat.pink.base.ui.home.HomeViewModel;

public class PrivacyFragment extends BaseFragment<FragmentPrivacyBinding, HomeViewModel> {
    public static final String TAG = "PrivacyFragment";

    @NonNull
    @Override
    public HomeViewModel getViewModel() {
        return new ViewModelProvider(this).get(HomeViewModel.class);
    }

    @Override
    public void initView() {
        super.initView();
        binding.llTop.txtTitle.setText(getString(R.string.privacy_policy));
        binding.llConstraint.setOnClickListener(v -> {
        });
        binding.llTop.ivBack.setOnClickListener(v -> {
            backStackFragment();
        });
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}

