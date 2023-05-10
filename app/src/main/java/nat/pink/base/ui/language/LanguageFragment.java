package nat.pink.base.ui.language;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import nat.pink.base.adapter.AdapterLanguage;
import nat.pink.base.base.BaseFragment;
import nat.pink.base.databinding.FragmentLanguageBinding;
import nat.pink.base.utils.PreferenceUtil;

public class LanguageFragment extends BaseFragment<FragmentLanguageBinding, LanguageViewModel> {

    public static final String TAG = "LanguageFragment";
    private AdapterLanguage adapterLanguage;

    @Override
    protected LanguageViewModel getViewModel() {
        return new ViewModelProvider(this).get(LanguageViewModel.class);
    }

    @Override
    protected void initView() {
        super.initView();
        adapterLanguage = new AdapterLanguage(requireContext(), data -> {
            PreferenceUtil.saveString(requireContext(), PreferenceUtil.SETTING_ENGLISH, data.getValue());
            adapterLanguage.notifyDataSetChanged();
        });
        binding.rcvEnglish.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rcvEnglish.setAdapter(adapterLanguage);
    }

    @Override
    protected void initData() {
        super.initData();
        getViewModel().initData(requireContext());
        getViewModel().languages.observe(this, objectLanguages -> {
            adapterLanguage.setObjectLanguages(objectLanguages);
        });
    }

    @Override
    protected void initEvent() {
        super.initEvent();
    }
}
