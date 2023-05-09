package nat.pink.base.ui.onboard;

import androidx.lifecycle.ViewModelProvider;

import nat.pink.base.base.BaseFragment;
import nat.pink.base.databinding.FragmentOnboardBinding;

public class OnboardFragment extends BaseFragment<FragmentOnboardBinding, OnboardViewModel> {

    public static String TAG = "OnboardFragment";

    @Override
    protected OnboardViewModel getViewModel() {
        return new ViewModelProvider(this).get(OnboardViewModel.class);
    }
}
