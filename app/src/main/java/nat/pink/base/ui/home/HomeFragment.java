package nat.pink.base.ui.home;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import nat.pink.base.adapter.AdapterFakeUser;
import nat.pink.base.adapter.AdapterLanguage;
import nat.pink.base.base.BaseFragment;
import nat.pink.base.databinding.HomeFragmentBinding;
import nat.pink.base.utils.PreferenceUtil;

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
        getViewModel().languages.observe(this, fakeUsers -> {
            adapterFakeUser.setFakeUsers(fakeUsers);
        });

    }

    @Override
    protected void initEvent() {
        super.initEvent();

    }
}
