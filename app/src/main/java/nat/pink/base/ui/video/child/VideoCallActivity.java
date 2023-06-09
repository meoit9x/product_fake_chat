package nat.pink.base.ui.video.child;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

import java.io.Serializable;

import nat.pink.base.R;
import nat.pink.base.databinding.ActivityScreenIncomingBinding;
import nat.pink.base.model.ObjectCalling;
import nat.pink.base.service.CallingService;
import nat.pink.base.utils.Const;
import nat.pink.base.utils.ImageUtils;
import nat.pink.base.utils.Utils;

public class VideoCallActivity extends AppCompatActivity {

    private ObjectCalling objectCalling;
    private ActivityScreenIncomingBinding binding;
    private boolean showVideo;
    private ScreenReceiver mReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Utils.overLockScreen(this);
        super.onCreate(savedInstanceState);

        Utils.showFullScreen(this);

        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);

        if (getIntent() != null) {
            Serializable serializable = getIntent().getSerializableExtra(Const.PUT_EXTRAL_OBJECT_CALL);
            if (serializable instanceof ObjectCalling) {
                objectCalling = (ObjectCalling) serializable;
            }
            showVideo = getIntent().getBooleanExtra("show_icon_video", false);
        }

        binding = ActivityScreenIncomingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
        iniData();
        initEvent();
    }

    private void initView() {
        if (showVideo) {
            binding.ivAnswer.setImageResource(R.drawable.ic_call);
        }
        if (getIntent().getBooleanExtra(Const.ACTION_FORWARD_SCREEN, false)) {
            Intent serviceIntent = new Intent(this, CallingService.class);
            this.stopService(serviceIntent);
            if (Utils.checkPermission(this, Manifest.permission.CAMERA)) {
                showView();
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, Const.REQUEST_CODE_CAMERA);
            }
        }
    }

    private void iniData() {
        if (objectCalling != null) {
/*            Glide.with(this)
                    .load(objectCalling.getPathImage() != null ? Uri.parse(objectCalling.getPathImage()) : null)
                    .placeholder(R.drawable.ic_user_default)
                    .into(binding.ivCall);
            Glide.with(this).load(objectCalling.getPathImage() != null ? Uri.parse(objectCalling.getPathImage()) : null)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.ic_user_default)
                    .fitCenter().into(binding.ivContent);*/
            if (objectCalling.getPathImage() != null) {
                ImageUtils.loadImage(this, binding.ivCall, objectCalling.getPathImage());
                ImageUtils.loadImage(this, binding.ivContent, objectCalling.getPathImage());
            }

            binding.txtName.setText(objectCalling.getName());
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        Utils.clearFlags(this);
    }

    public void initEvent() {
        binding.clContent.setOnClickListener(v -> {
        });
        binding.ivRefuse.setOnClickListener(v -> finish());
        binding.ivAnswer.setOnClickListener(v -> {
            if (Utils.checkPermission(this, Manifest.permission.CAMERA)) {
                showView();
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, Const.REQUEST_CODE_CAMERA);
            }

        });
    }

    private void showView() {
        if (showVideo) {
            Intent intent = new Intent(this, OutCommingActivity.class);
            intent.putExtra(Const.PUT_EXTRAL_OBJECT_CALL, objectCalling);
            intent.putExtra("show_icon_video", showVideo);
            startActivityForResult(intent, Const.CHECK_TURN_OFF_VOICE_INCOMING);
        } else {
            Intent intent = new Intent(this, VideoCallAnswerActivity.class);
            intent.putExtra(Const.PUT_EXTRAL_OBJECT_CALL, objectCalling);
            startActivityForResult(intent, Const.CHECK_TURN_OFF_VOICE_INCOMING);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Const.REQUEST_CODE_CAMERA && Utils.checkPermission(this, Manifest.permission.CAMERA)) {
            showView();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.CHECK_TURN_OFF_VOICE_INCOMING) {
            if (resultCode == OutCommingActivity.RESULT_PAUSE) {
                setResult(resultCode, data);
            }
            finish();
        }
    }

    public class ScreenReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                Intent returnIntent = new Intent();
                VideoCallActivity.this.setResult(OutCommingActivity.RESULT_PAUSE, returnIntent);
                finish();
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

            }
        }

    }
}
