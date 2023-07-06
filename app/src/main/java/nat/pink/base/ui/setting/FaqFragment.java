package nat.pink.base.ui.setting;

import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import nat.pink.base.R;
import nat.pink.base.adapter.AdapterFaq;
import nat.pink.base.base.BaseFragment;
import nat.pink.base.databinding.FragmentFaqBinding;
import nat.pink.base.model.ObjectFaq;
import nat.pink.base.ui.home.HomeViewModel;

public class FaqFragment extends BaseFragment<FragmentFaqBinding, HomeViewModel> {

    public static final String TAG = "FaqFragment";
    private List<ObjectFaq> faqList = new ArrayList<>();
    private boolean morePoint = false;

    @Override
    protected HomeViewModel getViewModel() {
        return new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
    }

    public FaqFragment() {}

    public FaqFragment(boolean morePoint) {
        this.morePoint = morePoint;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData() {
        super.initData();
        binding.llTop.txtTitle.setText(getString(R.string.faq));

        faqList.add(new ObjectFaq(getString(R.string.faq_5), getString(R.string.faq_des_5)));
        faqList.add(new ObjectFaq(getString(R.string.faq_7), getString(R.string.faq_des_7)));
        faqList.add(new ObjectFaq(getString(R.string.faq_1), getString(R.string.faq_des_1)));
        faqList.add(new ObjectFaq(getString(R.string.faq_2), getString(R.string.faq_des_2)));
        faqList.add(new ObjectFaq(getString(R.string.faq_3), getString(R.string.faq_des_3)));
        faqList.add(new ObjectFaq(getString(R.string.faq_4), getString(R.string.faq_des_4)));
        faqList.add(new ObjectFaq(getString(R.string.faq_6), getString(R.string.faq_des_6)));

        binding.listView.setGroupIndicator(null);
        AdapterFaq adapterFaq = new AdapterFaq(requireActivity(), faqList);
        binding.listView.setAdapter(adapterFaq);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        binding.llTop.ivBack.setOnClickListener(v -> backStackFragment());
        binding.listView.expandGroup(morePoint ? 1 : 0);
        binding.listView.setOnGroupExpandListener(groupPosition -> {});

        binding.listView.setOnGroupCollapseListener(groupPosition -> {});

        binding.listView.setOnChildClickListener((expandableListView, v, groupPosition, childPosition, id) -> true);
    }
}
