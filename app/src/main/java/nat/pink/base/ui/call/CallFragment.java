package nat.pink.base.ui.call;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import java.util.function.Consumer;

import nat.pink.base.R;
import nat.pink.base.base.BaseFragment;
import nat.pink.base.custom.view.ExtButton;
import nat.pink.base.databinding.FragmentSetupCallBinding;
import nat.pink.base.dialog.DialogChangeTime;
import nat.pink.base.model.DaoContact;
import nat.pink.base.model.ObjectCalling;
import nat.pink.base.ui.incoming.CallScreenFragment;
import nat.pink.base.ui.video.child.OutCommingActivity;
import nat.pink.base.ui.video.child.VideoCallActivity;
import nat.pink.base.utils.Config;
import nat.pink.base.utils.Const;
import nat.pink.base.utils.ImageUtils;
import nat.pink.base.utils.PreferenceUtil;
import nat.pink.base.utils.Utils;

public class CallFragment extends BaseFragment<FragmentSetupCallBinding, CallViewModel> {

    public static final String TAG = "CallFragment";

    private DaoContact user;
    private Consumer<Object> consumer;

    public CallFragment() {

    }

    public CallFragment(DaoContact objectUser, Consumer<Object> consumer) {
        this.user = objectUser;
        this.consumer = consumer;
    }

    @Override
    protected CallViewModel getViewModel() {
        return new ViewModelProvider(this).get(CallViewModel.class);
    }

    private ExtButton btInComing, btOutComing;
    private ObjectCalling objectIncoming = new ObjectCalling();
    private ObjectCalling objectCalling = new ObjectCalling();
    private DialogChangeTime dialogChangeTime;
    private DialogChangeTime.CHANGE_TYPE changeType = DialogChangeTime.CHANGE_TYPE.TEN_SECONDS;

    @Override
    protected void initView() {
        super.initView();
        // user = new DaoContact(2, "Cristiano Ronaldo", 1, true, true, 1, "harvard", "new castle", "", Uri.parse("android.resource://" + getContext().getPackageName() + "/drawable/ronaldo").toString());
        btInComing = new ExtButton(requireContext());
        btOutComing = new ExtButton(requireContext());

        if (user.getAvatar().contains("R.drawable")) {
            binding.ivAvatarContact.setImageResource(Utils.convertStringToDrawable(getContext(), user.getAvatar()));
        } else {
            ImageUtils.loadImage(binding.ivAvatarContact, user.getAvatar());
        }
        //ImageUtils.loadImage(binding.ivAvatarContact, user.getAvatar());
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
            if (dialogChangeTime.isShowing()) dialogChangeTime.dismiss();
            changeType = v;
            binding.txtTimer.setText(Utils.getStringTimeDelay(requireContext(), v));
        });

        binding.txtTimer.setText(getString(R.string.ten_seconds_later));
    }

    @Override
    protected void initData() {
        super.initData();
        objectIncoming.setCalling(false);
        objectCalling.setCalling(true);
        objectCalling.setName(user.getName());
        objectCalling.setPathImage(user.getAvatar());
        objectIncoming.setName(user.getName());
        objectIncoming.setPathImage(user.getAvatar());

    }

    @Override
    protected void initEvent() {
        super.initEvent();
        btInComing.setOnClickListener(v -> setStateView(false));
        btOutComing.setOnClickListener(v -> setStateView(true));
        binding.llSelectTimer.setOnClickListener(view -> {
            if (!dialogChangeTime.isShowing()) dialogChangeTime.show();
        });
        binding.txtDone.setOnClickListener(v -> {
            objectIncoming.setTimer(changeType);
            objectCalling.setTimer(changeType);
           /* CallScreenFragment callScreenFragment = new CallScreenFragment();

            ObjectCalling objectCalling = new ObjectCalling();
            objectCalling.setName(user.getName());
            objectCalling.setPathImage(user.getAvatar());
            objectCalling.setTimer(changeType);//todo
            Bundle bundle = new Bundle();
            //is call video
            bundle.putBoolean("show_icon_video",false);
            bundle.putSerializable(Const.PUT_EXTRAL_OBJECT_CALL, objectCalling);
            callScreenFragment.setArguments(bundle);

            if (objectCalling.getTimer() == DialogChangeTime.CHANGE_TYPE.NOW){
                replaceFragment(callScreenFragment, CallScreenFragment.TAG);
            }else{
                PreferenceUtil.saveLong(requireContext(), PreferenceUtil.KEY_CURRENT_TIME, System.currentTimeMillis() + Utils.getTimeFromKey(requireContext(), Utils.getIntTimeDelay(getContext(),changeType)));
                PreferenceUtil.saveKey(requireContext(), PreferenceUtil.KEY_COMMING_VOICE);
                Utils.startAlarmService(requireActivity(), Utils.getTimeFromKey(requireContext(), Utils.getIntTimeDelay(getContext(),changeType)), Const.ACTION_COMMING_VOICE, objectCalling);
                backStackFragment();
            }*/

            if (btOutComing.isSelected()) {
                if (objectCalling.getTimer() == DialogChangeTime.CHANGE_TYPE.NOW) {
                    Intent intent = new Intent(requireContext(), OutCommingActivity.class);
                    intent.putExtra(Const.PUT_EXTRAL_OBJECT_CALL, objectCalling);
                    intent.putExtra("show_icon_video", true);
                    startActivityForResult(intent, Config.CHECK_TURN_OFF_VOICE);
                } else {
                    PreferenceUtil.saveLong(requireContext(), PreferenceUtil.KEY_CURRENT_TIME, System.currentTimeMillis() + Utils.getTimeFromKey(requireContext(), Utils.getIntTimeDelay(getContext(), changeType)));
                    PreferenceUtil.saveKey(requireContext(), PreferenceUtil.KEY_CALLING_VOICE);
                    Utils.startAlarmService(requireActivity(), Utils.getTimeFromKey(requireContext(), Utils.getIntTimeDelay(getContext(), changeType)), Const.ACTION_CALL_VOICE, objectCalling);
                    backStackFragment();
                }
            } else {
                if (objectIncoming.getTimer() == DialogChangeTime.CHANGE_TYPE.NOW) {
                    Intent intent = new Intent(requireContext(), VideoCallActivity.class);
                    intent.putExtra(Const.PUT_EXTRAL_OBJECT_CALL, objectIncoming);
                    intent.putExtra("show_icon_video", true);
                    startActivityForResult(intent, Config.CHECK_TURN_OFF_VOICE);
                } else {
                    PreferenceUtil.saveLong(requireContext(), PreferenceUtil.KEY_CURRENT_TIME, System.currentTimeMillis() + Utils.getTimeFromKey(requireContext(), Utils.getIntTimeDelay(getContext(), changeType)));
                    PreferenceUtil.saveKey(requireContext(), PreferenceUtil.KEY_COMMING_VOICE);
                    Utils.startAlarmService(requireActivity(), Utils.getTimeFromKey(requireContext(), Utils.getIntTimeDelay(getContext(), changeType)), Const.ACTION_COMMING_VOICE, objectIncoming);
                    backStackFragment();
                }
            }
            consumer.accept(new Object());
        });
        binding.llTop.ivBack.setOnClickListener(v -> backStackFragment());

    }


    private void setStateView(boolean isChatBubles) {
        btOutComing.setSelected(isChatBubles);
        btInComing.setSelected(!isChatBubles);
    }


}