package nat.pink.base.ui.setting;

import androidx.lifecycle.ViewModelProvider;

import nat.pink.base.R;
import nat.pink.base.adapter.AdapterFaq;
import nat.pink.base.base.BaseFragment;
import nat.pink.base.databinding.FragmentFaqBinding;
import nat.pink.base.ui.home.HomeViewModel;

public class FaqFragment extends BaseFragment<FragmentFaqBinding, HomeViewModel> {

    public static final String TAG = "FaqFragment";

    @Override
    protected HomeViewModel getViewModel() {
        return new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData() {
        super.initData();
        binding.llTop.txtTitle.setText(getString(R.string.faq));
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        binding.llTop.ivBack.setOnClickListener(v -> backStackFragment());
        binding.listView.setGroupIndicator(null);
        AdapterFaq adapterFaq = new AdapterFaq(requireActivity());
        binding.listView.setAdapter(adapterFaq);
        binding.listView.setOnGroupExpandListener(groupPosition -> {});

        binding.listView.setOnGroupCollapseListener(groupPosition -> {});

        binding.listView.setOnChildClickListener((expandableListView, v, groupPosition, childPosition, id) -> true);
    }
}
