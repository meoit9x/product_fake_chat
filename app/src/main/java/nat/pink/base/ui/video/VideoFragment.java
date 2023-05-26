package nat.pink.base.ui.video;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import java.util.function.Consumer;

import nat.pink.base.R;
import nat.pink.base.base.BaseFragment;
import nat.pink.base.custom.view.ExtButton;
import nat.pink.base.databinding.FragmentSetupVideoCallBinding;
import nat.pink.base.dialog.DialogChangeTime;
import nat.pink.base.dialog.DialogErrorLink;
import nat.pink.base.dialog.DialogNetworkFail;
import nat.pink.base.dialog.DialogShowError;
import nat.pink.base.model.DaoContact;
import nat.pink.base.model.ObjectCalling;
import nat.pink.base.ui.home.HomeViewModel;
import nat.pink.base.ui.video.child.OutCommingActivity;
import nat.pink.base.ui.video.child.VideoCallActivity;
import nat.pink.base.utils.Const;
import nat.pink.base.utils.ImageUtils;
import nat.pink.base.utils.PreferenceUtil;
import nat.pink.base.utils.UriUtils;
import nat.pink.base.utils.Utils;

public class VideoFragment extends BaseFragment<FragmentSetupVideoCallBinding, HomeViewModel> {

    public static final String TAG = "VideoFragment";

    @Override
    protected HomeViewModel getViewModel() {
        return new ViewModelProvider(this).get(HomeViewModel.class);
    }
    private Consumer<Object> consumer;
    private ExtButton btChatBubbles, btNavigationBar;
    private DialogChangeTime dialogChangeTime;
    private ObjectCalling objectIncoming = new ObjectCalling();
    private ObjectCalling objectCalling = new ObjectCalling();
    private DaoContact user;
    private DialogChangeTime.CHANGE_TYPE changeType = DialogChangeTime.CHANGE_TYPE.TEN_SECONDS;

    public VideoFragment() {
    }

    public VideoFragment(DaoContact objectUser, Consumer<Object> consumer) {
        this.user = objectUser;
        this.consumer = consumer;
    }

    @Override
    protected void initView() {
        super.initView();
        btChatBubbles = new ExtButton(requireContext());
        btNavigationBar = new ExtButton(requireContext());

        if (user.getAvatar().contains("R.drawable")) {
            binding.ivAvatarContact.setImageResource(Utils.convertStringToDrawable(requireContext(), user.getAvatar()));
        } else
            ImageUtils.loadImage(binding.ivAvatarContact, user.getAvatar());
        binding.txtNameContact.setText(user.getName());
        if (user.isVerified()) {
            binding.ivCheckRank.setVisibility(View.VISIBLE);
        }

        binding.llTop.txtTitle.setText(getString(R.string.setup_fake_video_call));
        LinearLayoutCompat.LayoutParams lpChatBubbles = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, 1f);
        lpChatBubbles.setMarginEnd(15);
        btChatBubbles.setLayoutParams(lpChatBubbles);
        btChatBubbles.setStringText(getString(R.string.incoming_call));
        btChatBubbles.setSelected(true);
        binding.llButton.addView(btChatBubbles);

        LinearLayoutCompat.LayoutParams lpNavigationBar = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, 1f);
        lpNavigationBar.setMarginStart(15);
        btNavigationBar.setLayoutParams(lpNavigationBar);
        btNavigationBar.setStringText(getString(R.string.call_away));
        btNavigationBar.setSelected(false);
        binding.llButton.addView(btNavigationBar);

        dialogChangeTime = new DialogChangeTime(requireContext(), v -> {
            if (dialogChangeTime.isShowing())
                dialogChangeTime.dismiss();
            changeType = v;
            if (btNavigationBar.isSelected())
                objectCalling.setTimer(changeType);
            else
                objectIncoming.setTimer(changeType);
            binding.txtTimer.setText(Utils.getStringTimeDelay(requireContext(), changeType));

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
            if (!dialogChangeTime.isShowing()) {
                if (btNavigationBar.isSelected()) {
                    dialogChangeTime.setData(getString(R.string.pick_up_time), getString(R.string.des_pick_up_time), getString(R.string.pick_up_now));
                } else {
                    dialogChangeTime.setData(getString(R.string.time_delay), getString(R.string.dialog_description_time_delay), getString(R.string.get_a_call_now));
                }
                dialogChangeTime.show();
            }
        });

        binding.llTop.ivBack.setOnClickListener(v -> backStackFragment());
        binding.frIncomingCall.setOnClickListener(v -> {
            Utils.openGallery(requireActivity(), true);
        });
        binding.txtDone.setOnClickListener(v -> {
            if (checkErrorPath()) {
                new DialogShowError(requireContext(), R.style.MaterialDialogSheet, o -> {
                }).show();
                return;
            }
            if (Utils.isNetworkAvailable(requireActivity())) {
                actionSave();
            } else {
                new DialogNetworkFail(requireContext(), R.style.MaterialDialogSheet, false).show();
            }
        });
    }

    private void setStateView(boolean isChatBubles) {
        btNavigationBar.setSelected(isChatBubles);
        btChatBubbles.setSelected(!isChatBubles);

        binding.extTitleTime.setText(isChatBubles ? getString(R.string.pick_up_time) : getString(R.string.time_delay));

        binding.txtTimer.setText(Utils.getStringTimeDelay(requireContext(), isChatBubles ? objectCalling.getTimer() : objectIncoming.getTimer()));

        Bitmap bitmap = null;
        if (btNavigationBar.isSelected() && objectCalling.getPathVideo() != null)
            bitmap = Utils.getBitmapFromVideo(requireContext(), Uri.parse(objectCalling.getPathVideo()));
        else if (btChatBubbles.isSelected() && objectIncoming.getPathVideo() != null) {
            bitmap = Utils.getBitmapFromVideo(requireContext(), Uri.parse(objectIncoming.getPathVideo()));
        }
        if (bitmap == null) {
            Glide.with(requireContext()).load(R.drawable.bg_upload_video).into(binding.imgVideo);
            updateButtonChangeVideo(false);
            return;
        }
        Glide.with(requireContext()).load(bitmap).into(binding.imgVideo);
        updateButtonChangeVideo(true);
    }

    private boolean checkErrorPath() {
        if (btNavigationBar.isSelected()) {
            return objectCalling.getPathVideo() == null;
        } else {
            return objectIncoming.getPathVideo() == null;
        }
    }

    private void updateButtonChangeVideo(boolean isChange) {
        binding.extChangeVideo.setText(isChange ? getString(R.string.change_video) : getString(R.string.upload_video));
        binding.extChangeVideo.setCompoundDrawablesWithIntrinsicBounds(isChange ? R.drawable.ic_refresh_circle : R.drawable.ic_upload_file, 0, 0, 0);
        binding.extChangeVideo.setBackgroundResource(isChange ? R.drawable.bg_change_video : R.color.transparent);
    }

    private void actionSave() {
        if (btNavigationBar.isSelected()) {
            if (objectCalling.getTimer() == DialogChangeTime.CHANGE_TYPE.NOW) {
                Intent intent = new Intent(getActivity(), OutCommingActivity.class);
                intent.putExtra(Const.PUT_EXTRAL_OBJECT_CALL, objectCalling);
                startActivityForResult(intent, Const.CHECK_TURN_OFF_VOICE);
            } else {
                PreferenceUtil.saveLong(requireContext(), PreferenceUtil.KEY_CURRENT_TIME, System.currentTimeMillis() + Utils.getTimeFromKey(requireContext(), Utils.getIntTimeDelay(getContext(), changeType)));
                PreferenceUtil.saveKey(requireContext(), PreferenceUtil.KEY_CALLING_VIDEO);
                Utils.startAlarmService(requireActivity(), Utils.getTimeFromKey(requireContext(), Utils.getIntTimeDelay(getContext(), changeType)), Const.ACTION_CALL_VIDEO, objectCalling);
                backStackFragment();
            }
        } else {
            if (objectIncoming.getTimer() == DialogChangeTime.CHANGE_TYPE.NOW) {
                Intent intent = new Intent(getActivity(), VideoCallActivity.class);
                intent.putExtra(Const.PUT_EXTRAL_OBJECT_CALL, objectIncoming);
                startActivityForResult(intent, Const.CHECK_TURN_OFF_VOICE);
            } else {
                PreferenceUtil.saveLong(requireContext(), PreferenceUtil.KEY_CURRENT_TIME, System.currentTimeMillis() + Utils.getTimeFromKey(requireContext(), Utils.getIntTimeDelay(getContext(), changeType)));
                PreferenceUtil.saveKey(requireContext(), PreferenceUtil.KEY_COMMING_VIDEO);
                Utils.startAlarmService(requireActivity(), Utils.getTimeFromKey(requireContext(), Utils.getIntTimeDelay(getContext(), changeType)), Const.ACTION_COMMING_VIDEO, objectIncoming);
                backStackFragment();
            }
        }
        consumer.accept(new Object());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.ALBUM_REQUEST_ONLY_VIDEO && data != null && data.getData() != null) {
            if (btNavigationBar.isSelected())
                objectCalling.setPathVideo(data.getData().toString());
            else {
                objectIncoming.setPathVideo(data.getData().toString());
            }
            if (UriUtils.isNewGooglePhotosUri(data.getData())) {
                new DialogErrorLink(requireContext(), R.style.MaterialDialogSheet, o -> {
                }).show();
                return;
            }
            Bitmap bitmap = Utils.getBitmapFromVideo(getContext(), data.getData());
            if (bitmap == null)
                return;
            Glide.with(requireContext()).load(bitmap).into(binding.imgVideo);
            updateButtonChangeVideo(true);
        }
    }
}
