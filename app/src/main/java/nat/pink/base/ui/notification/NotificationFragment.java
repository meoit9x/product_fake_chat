package nat.pink.base.ui.notification;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.function.Consumer;

import nat.pink.base.databinding.FragmentSetupNotificationBinding;
import nat.pink.base.ui.MainActivity;
import nat.pink.base.R;
import nat.pink.base.base.BaseFragment;
import nat.pink.base.custom.view.ExtButton;
import nat.pink.base.dialog.DialogChangeTime;
import nat.pink.base.dialog.DialogNetworkFail;
import nat.pink.base.dialog.DialogShowError;
import nat.pink.base.model.DaoContact;
import nat.pink.base.model.ObjectCalling;
import nat.pink.base.service.ChatHeadService;
import nat.pink.base.ui.home.HomeViewModel;
import nat.pink.base.utils.Const;
import nat.pink.base.utils.ImageUtils;
import nat.pink.base.utils.PreferenceUtil;
import nat.pink.base.utils.Utils;

public class NotificationFragment extends BaseFragment<FragmentSetupNotificationBinding, HomeViewModel> {

    public static final String TAG = "NotificationFragment";

    @Override
    protected HomeViewModel getViewModel() {
        return new ViewModelProvider(this).get(HomeViewModel.class);
    }

    private DaoContact user;
    private Consumer<Object> consumer;
    private ExtButton btChatBubbles, btNavigationBar;
    private DialogChangeTime dialogChangeTime;
    private DialogChangeTime.CHANGE_TYPE changeType = DialogChangeTime.CHANGE_TYPE.TEN_SECONDS;
    private ObjectCalling objectTop = new ObjectCalling();
    private ObjectCalling objectPopup = new ObjectCalling();
    private String edtNameFinal = "";
    private final int MAX_CHARACTERS = 100;

    public NotificationFragment() {

    }

    public NotificationFragment(DaoContact objectUser, Consumer<Object> consumer) {
        this.user = objectUser;
        this.consumer = consumer;
    }

    @SuppressLint("ClickableViewAccessibility")
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
            if (btNavigationBar.isSelected())
                objectTop.setTimer(changeType);
            else
                objectPopup.setTimer(changeType);
            binding.txtTimer.setText(Utils.getStringTimeDelay(requireContext(), v));
        });

        binding.txtDescription.setOnTouchListener((v, event) -> {
            if (binding.txtDescription.hasFocus()) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_SCROLL) {
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    return true;
                }
            }
            return false;
        });
        binding.txtNumberText.setText(String.format("%1$s/%2$s)", 0, MAX_CHARACTERS));

    }

    @Override
    protected void initData() {
        super.initData();
        binding.txtTimer.setText(getString(R.string.ten_seconds_later));
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        String ava = user.getAvatar();
        String name = user.getName();

        btChatBubbles.setOnClickListener(v -> setStateView(false));
        btNavigationBar.setOnClickListener(v -> setStateView(true));

        ImageUtils.loadImage(requireContext(), binding.ivAvatarContact, user.getAvatar());
        binding.txtNameContact.setText(user.getName());
        if (user.isVerified()) {
            binding.ivCheckRank.setVisibility(View.VISIBLE);
        }
        binding.llSelectTimer.setOnClickListener(view -> {
            if (!dialogChangeTime.isShowing())
                dialogChangeTime.show();
        });
        binding.txtDone.setOnClickListener(v -> {
            if (checkErrorPath()) {
                new DialogShowError(requireContext(), R.style.MaterialDialogSheet, o -> {
                }).show();
                return;
            }

            if (Utils.isNetworkAvailable(requireActivity())) {
                showActionDone();
            } else {
                new DialogNetworkFail(requireContext(), R.style.MaterialDialogSheet, false).show();
            }
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
                if (btNavigationBar.isSelected())
                    objectTop.setMessage(charSequence.toString());
                else
                    objectPopup.setMessage(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                edtNameFinal = editable.toString();
            }
        });
        binding.swSound.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (btNavigationBar.isSelected()) {
                objectTop.setSound(isChecked);
            } else {
                objectPopup.setSound(isChecked);
            }
        });
        objectPopup.setName(name);
        objectPopup.setPathImage(ava);
        objectTop.setName(name);
        objectTop.setPathImage(ava);
    }

    private void setStateView(boolean isChatBubles) {
        btNavigationBar.setSelected(isChatBubles);
        btChatBubbles.setSelected(!isChatBubles);

        binding.txtDescription.setText(isChatBubles ? objectTop.getMessage() : objectPopup.getMessage());
        binding.txtDescription.setSelection(isChatBubles ? objectTop.getMessage().length() : objectPopup.getMessage().length());
        binding.txtNumberText.setText((isChatBubles ? objectTop.getMessage().length() : objectPopup.getMessage().length()) + "/100");
        binding.txtTimer.setText(Utils.getStringTimeDelay(requireContext(), isChatBubles ? objectTop.getTimer() : objectPopup.getTimer()));
        binding.swSound.setChecked(isChatBubles ? objectTop.isSound() : objectPopup.isSound());
    }

    private void showActionDone() {
        if (btNavigationBar.isSelected()) {
            if (objectTop.getTimer() == DialogChangeTime.CHANGE_TYPE.NOW) {
                if (NotificationManagerCompat.from(requireContext()).areNotificationsEnabled())
                    showNotification();
            } else {
                PreferenceUtil.saveLong(requireContext(), PreferenceUtil.KEY_CURRENT_TIME, System.currentTimeMillis() + Utils.getTimeFromKey(requireContext(), Utils.getIntTimeDelay(getContext(), changeType)));
                PreferenceUtil.saveKey(requireContext(), PreferenceUtil.KEY_CURRENT_TIME_NOTI);
                Utils.startAlarmService(requireActivity(), Utils.getTimeFromKey(requireContext(), Utils.getIntTimeDelay(getContext(), changeType)), Const.ACTION_SHOW_NOTI, objectTop);
                backStackFragment();
            }
        } else {
            if (objectPopup.getTimer() == DialogChangeTime.CHANGE_TYPE.NOW) {
                checkPermission();
                backStackFragment();
            } else {
                checkPermission();
                PreferenceUtil.saveLong(requireContext(), PreferenceUtil.KEY_CURRENT_TIME, System.currentTimeMillis() + Utils.getTimeFromKey(requireContext(), Utils.getIntTimeDelay(getContext(), changeType)));
                PreferenceUtil.saveKey(requireContext(), PreferenceUtil.KEY_SHOW_POPUP);
                Utils.startAlarmService(requireActivity(), Utils.getTimeFromKey(requireContext(), Utils.getIntTimeDelay(getContext(), changeType)), Const.ACTION_SHOW_POP_UP, objectPopup);
                backStackFragment();
            }
        }
        consumer.accept(new Object());
    }

    public void checkPermission() {
        if (!Settings.canDrawOverlays(getContext())) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getContext().getPackageName()));
            startActivityForResult(intent, Const.ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
        } else {
            if (objectPopup.getTimer() != DialogChangeTime.CHANGE_TYPE.NOW) {
                return;
            }
            showPopup();
        }
    }

    private void showPopup() {
        Gson gson = new Gson();
        String json = gson.toJson(objectPopup);
        Intent intent = new Intent(getContext(), ChatHeadService.class);
        intent.setAction(Const.ACTION_SHOW_POP_UP);
        intent.setType(json);
        requireActivity().startService(intent);
    }

    private boolean checkErrorPath() {
        if (btChatBubbles.isSelected()) {
            return objectPopup.getMessage().trim().isEmpty() && edtNameFinal.isEmpty();
        } else {
            return objectTop.getMessage().trim().isEmpty() && edtNameFinal.isEmpty();
        }
    }

    private void showNotification() {
        Uri imageUri = objectTop.getPathImage() == null || objectTop.getPathImage().equals("") ? null : Uri.parse(objectTop.getPathImage());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            builder.setSmallIcon(R.drawable.ic_messenger);
            builder.setColor(getResources().getColor(R.color.color_057BF7));
        } else {
            builder.setSmallIcon(R.drawable.ic_messenger);
        }

        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setAction(Const.ACTION_CREAT_NOTIFICATION);
        PendingIntent pendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        long yourmilliseconds = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
        Date resultdate = new Date(yourmilliseconds);

        RemoteViews contentView = new RemoteViews(requireActivity().getPackageName(), R.layout.custom_notification);
        if (imageUri != null && imageUri.toString().contains("R.drawable")) {
            contentView.setImageViewResource(R.id.ava, Utils.convertStringToDrawable(requireContext(), user.getAvatar()));
        } else {
            contentView.setImageViewUri(R.id.ava, imageUri);
        }
        contentView.setTextViewText(R.id.ext_name, user.getName());
        contentView.setTextViewText(R.id.ext_title, objectTop.getMessage());
        contentView.setTextViewText(R.id.time, sdf.format(resultdate));

        builder.setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            builder.setCustomBigContentView(contentView);
        } else {
            builder.setCustomContentView(contentView);
        }
        builder.setContentTitle(getResources().getText(R.string.message));
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);

        NotificationManager mNotificationManager =
                (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            mNotificationManager.createNotificationChannel(new NotificationChannel("my_call_app", "Call App", NotificationManager.IMPORTANCE_DEFAULT));
            builder.setChannelId("my_call_app");
        }

        // Will display the notification in the notification bar
        mNotificationManager.notify(1, builder.build());
        backStackFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            showPopup();
        }
    }
}
