package nat.pink.base.ui.home;

import androidx.lifecycle.ViewModelProvider;

import nat.pink.base.base.BaseFragment;
import nat.pink.base.databinding.HomeFragmentBinding;

public class HomeFragment extends BaseFragment<HomeFragmentBinding, HomeViewModel> {

    public static final String TAG = "HomeFragment";

    @Override
    protected HomeViewModel getViewModel() {
        return new ViewModelProvider(this).get(HomeViewModel.class);
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData() {
        super.initData();
        getViewModel().loadData();
        getViewModel().loadData.observe(getViewLifecycleOwner(), s -> {
            binding.check.setText("123");
        });
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        binding.check.setOnClickListener(view -> {
            addFragment(new CheckFragment(), CheckFragment.TAG);
        });
    }
}
