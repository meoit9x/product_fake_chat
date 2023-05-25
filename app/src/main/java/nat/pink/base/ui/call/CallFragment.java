package nat.pink.base.ui.call;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;

import nat.pink.base.R;
import nat.pink.base.base.BaseFragment;
import nat.pink.base.custom.view.ExtButton;
import nat.pink.base.databinding.FragmentSetupCallBinding;
import nat.pink.base.dialog.DialogChangeTime;
import nat.pink.base.model.DaoContact;
import nat.pink.base.utils.ImageUtils;
import nat.pink.base.utils.Utils;

public class CallFragment extends BaseFragment<FragmentSetupCallBinding, CallViewModel> {

    public static final String TAG = "CallFragment";

    private DaoContact user;
    public CallFragment(DaoContact user){
        this.user = user;
    }

    @Override
    protected CallViewModel getViewModel() {
        return new ViewModelProvider(this).get(CallViewModel.class);
    }

    private ExtButton btInComing, btOutComing;
    private DialogChangeTime dialogChangeTime;
    private DialogChangeTime.CHANGE_TYPE changeType = DialogChangeTime.CHANGE_TYPE.TEN_SSECONDS;

    @Override
    protected void initView() {
        super.initView();
        btInComing = new ExtButton(requireContext());
        btOutComing = new ExtButton(requireContext());

        ImageUtils.loadImage(binding.ivAvatarContact, user.getAvatar());
        binding.txtNameContact.setText(user.getName());
        if (user.isVerified()) {
            binding.ivCheckRank.setVisibility(View.VISIBLE);
        }

        binding.llTop.txtTitle.setText(getString(R.string.setup_fake_call));
        btInComing.setStringText(getString(R.string.in_coming_call));
        LinearLayoutCompat.LayoutParams lpChatBubbles = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, 1f);
        lpChatBubbles.setMarginEnd(15);
        btInComing.setLayoutParams(lpChatBubbles);
        btInComing.setSelected(true);
        binding.llButton.addView(btInComing);

        LinearLayoutCompat.LayoutParams lpNavigationBar = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, 1f);
        lpNavigationBar.setMarginStart(15);
        btOutComing.setLayoutParams(lpNavigationBar);
        btOutComing.setStringText(getString(R.string.out_coming_call));
        btOutComing.setSelected(false);
        binding.llButton.addView(btOutComing);

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
        btInComing.setOnClickListener(v -> setStateView(false));
        btOutComing.setOnClickListener(v -> setStateView(true));
        binding.llSelectTimer.setOnClickListener(view -> {
            if (!dialogChangeTime.isShowing())
                dialogChangeTime.show();
        });
        binding.txtDone.setOnClickListener(v -> {
            backStackFragment();
        });
        binding.llTop.ivBack.setOnClickListener(v -> backStackFragment());

    }

    private void setStateView(boolean isChatBubles) {
        btOutComing.setSelected(isChatBubles);
        btInComing.setSelected(!isChatBubles);
    }
}