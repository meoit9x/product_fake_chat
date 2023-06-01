package nat.pink.base.ui.manager;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import nat.pink.base.R;
import nat.pink.base.adapter.AdapterContacts;
import nat.pink.base.base.BaseFragment;
import nat.pink.base.dao.DatabaseController;
import nat.pink.base.databinding.FragmentManagerContactBinding;
import nat.pink.base.ui.create.CreateUserFragment;
import nat.pink.base.ui.home.HomeViewModel;

public class ManagerContactFragment extends BaseFragment<FragmentManagerContactBinding, HomeViewModel> {

    public static final String TAG = "ManagerContactFragment";

    private AdapterContacts adapterContacts;

    @Override
    protected HomeViewModel getViewModel() {
        return new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
    }

    @Override
    protected void initView() {
        super.initView();
        binding.rcvContent.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapterContacts = new AdapterContacts(requireContext());
        binding.rcvContent.setAdapter(adapterContacts);
    }

    @Override
    protected void initData() {
        super.initData();
        binding.llTop.txtTitle.setText(getString(R.string.manage_conversation));
        getViewModel().getListContact(requireContext());
        getViewModel().contactSuggest.observe(requireActivity(), v -> {
            adapterContacts.setContactSuggests(v);
        });
        getViewModel().contactNormal.observe(requireActivity(), v -> {
            adapterContacts.setContactNormals(v);
        });
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        binding.llTop.ivBack.setOnClickListener(v -> backStackFragment());
        adapterContacts.setConsumerCreateNew(v -> addFragment(new CreateUserFragment(), CreateUserFragment.TAG));
        adapterContacts.setConsumerEdit(v -> addFragment(new CreateUserFragment(), CreateUserFragment.TAG));
        adapterContacts.setConsumerDelete(v -> {
            getViewModel().deleteContact(requireContext(), v);
        });
    }
}
