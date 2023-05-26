package nat.pink.base.ui.notification;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.lifecycle.ViewModelProvider;

import nat.pink.base.R;
import nat.pink.base.base.BaseFragment;
import nat.pink.base.custom.view.ExtButton;
import nat.pink.base.databinding.FragmentSetupNotificationBinding;
import nat.pink.base.dialog.DialogChangeTime;
import nat.pink.base.ui.home.HomeViewModel;
import nat.pink.base.utils.Utils;

public class NotificationFragment extends BaseFragment<FragmentSetupNotificationBinding, HomeViewModel> {

    public static final String TAG = "NotificationFragment";

    @Override
    protected HomeViewModel getViewModel() {
        return new ViewModelProvider(this).get(HomeViewModel.class);
    }

    private ExtButton btChatBubbles, btNavigationBar;
    private DialogChangeTime dialogChangeTime;
    private DialogChangeTime.CHANGE_TYPE changeType = DialogChangeTime.CHANGE_TYPE.TEN_SECONDS;

    @Override
    protected void initView() {
        super.initView();
        btChatBubbles = new ExtButton(requireContext());
        btNavigationBar = new ExtButton(requireContext());



        binding.llTop.txtTitle.setText(getString(R.string.setup_fake_notiffication));
        btChatBubbles.setStringText(getString(R.string.chat_bubbles));
        LinearLayoutCompat.LayoutParams lpChatBubbles = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, 1f);
        lpChatBubbles.setMarginEnd(15);
        btChatBubbles.setLayoutParams(lpChatBubbles);
        btChatBubbles.setSelected(true);
        binding.llButton.addView(btChatBubbles);

        LinearLayoutCompat.LayoutParams lpNavigationBar = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, 1f);
        lpNavigationBar.setMarginStart(15);
        btNavigationBar.setLayoutParams(lpNavigationBar);
        btNavigationBar.setStringText(getString(R.string.navigation_bar));
        btNavigationBar.setSelected(false);
        binding.llButton.addView(btNavigationBar);

        dialogChangeTime = new DialogChangeTime(requireContext(), v -> {
            if (dialogChangeTime.isShowing())
                dialogChangeTime.dismiss();
            changeType = v;
            binding.txtTimer.setText(Utils.getStringTimeDelay(requireContext(), v));
        });

    }

    @Override
    protected void initData() {
        super.initData();
        binding.txtTimer.setText(getString(R.string.ten_seconds_later));
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        btChatBubbles.setOnClickListener(v -> setStateView(false));
        btNavigationBar.setOnClickListener(v -> setStateView(true));
        binding.llSelectTimer.setOnClickListener(view -> {
            if (!dialogChangeTime.isShowing())
                dialogChangeTime.show();
        });
        binding.txtDone.setOnClickListener(v -> {
            backStackFragment();
        });
        binding.llTop.ivBack.setOnClickListener(v -> backStackFragment());
        binding.txtDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.txtNumberText.setText(charSequence.length() + "/100");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setStateView(boolean isChatBubles) {
        btNavigationBar.setSelected(isChatBubles);
        btChatBubbles.setSelected(!isChatBubles);
    }
}
