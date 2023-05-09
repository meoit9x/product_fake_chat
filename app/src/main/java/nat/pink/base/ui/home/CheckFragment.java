package nat.pink.base.ui.home;

import androidx.lifecycle.ViewModelProvider;

import nat.pink.base.base.BaseFragment;
import nat.pink.base.databinding.HomeFragmentBinding;

public class CheckFragment extends BaseFragment<HomeFragmentBinding, HomeViewModel> {

    public static final String TAG = "CheckFragment";

    @Override
    protected HomeViewModel getViewModel() {
        return new ViewModelProvider(this).get(HomeViewModel.class);
    }
}
