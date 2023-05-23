package nat.pink.base.ui.notification;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import nat.pink.base.R;
import nat.pink.base.base.BaseFragment;
import nat.pink.base.custom.view.ExtButton;
import nat.pink.base.databinding.FragmentSetupNotificationBinding;
import nat.pink.base.ui.home.HomeViewModel;

public class NotificationFragment extends BaseFragment<FragmentSetupNotificationBinding, HomeViewModel> {

    public static final String TAG = "NotificationFragment";

    @Override
    protected HomeViewModel getViewModel() {
        return new ViewModelProvider(this).get(HomeViewModel.class);
    }

    private ExtButton btChatBubbles, btNavigationBar;

    @Override
    protected void initView() {
        super.initView();
        btChatBubbles = new ExtButton(requireContext());
        btNavigationBar = new ExtButton(requireContext());

        btChatBubbles.setStringText(getString(R.string.chat_bubbles));
        LinearLayoutCompat.LayoutParams lpChatBubbles =  new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT,1f);
        lpChatBubbles.setMarginEnd(15);
        btChatBubbles.setLayoutParams(lpChatBubbles);
        btChatBubbles.setSelected(true);
        binding.llButton.addView(btChatBubbles);

        LinearLayoutCompat.LayoutParams lpNavigationBar = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT,1f);
        lpNavigationBar.setMarginStart(15);
        btNavigationBar.setLayoutParams(lpNavigationBar);
        btNavigationBar.setStringText(getString(R.string.navigation_bar));
        btNavigationBar.setSelected(false);
        binding.llButton.addView(btNavigationBar);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        btChatBubbles.setOnClickListener(v -> setStateView(false));
        btNavigationBar.setOnClickListener(v -> setStateView(true));
    }

    private void setStateView(boolean isChatBubles){
        btNavigationBar.setSelected(isChatBubles);
        btChatBubbles.setSelected(!isChatBubles);
    }
}
