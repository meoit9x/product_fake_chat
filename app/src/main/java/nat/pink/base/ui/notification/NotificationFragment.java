package nat.pink.base.ui.notification;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import nat.pink.base.base.BaseFragment;
import nat.pink.base.databinding.FragmentSetupNotificationBinding;
import nat.pink.base.ui.home.HomeViewModel;

public class NotificationFragment extends BaseFragment<FragmentSetupNotificationBinding, HomeViewModel> {

    public static final String TAG = "NotificationFragment";

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
    }
}
