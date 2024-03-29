package nat.pink.base.ui.video.child;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.firebase.database.core.utilities.Utilities;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import nat.pink.base.R;
import nat.pink.base.custom.view.camera.CameraHelper;
import nat.pink.base.custom.view.camera.CameraListener;
import nat.pink.base.custom.view.camera.RoundTextureView;
import nat.pink.base.custom.view.camera.glsurface.GLUtil;
import nat.pink.base.custom.view.camera.glsurface.RoundCameraGLSurfaceView;
import nat.pink.base.databinding.ActivityVideoCallAnswerBinding;
import nat.pink.base.model.ObjectCalling;
import nat.pink.base.utils.Const;
import nat.pink.base.utils.Toolbox;
import nat.pink.base.utils.Utils;

public class VideoCallAnswerActivity extends AppCompatActivity implements ViewTreeObserver.OnGlobalLayoutListener, CameraListener {

    private ActivityVideoCallAnswerBinding binding;
    private ObjectCalling objectCalling;
    private CameraHelper cameraHelper;
    private RoundTextureView textureView;
    private static final int CAMERA_ID = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private Camera.Size previewSize;
    private int squarePreviewSize;
    private RoundCameraGLSurfaceView roundCameraGLSurfaceView;
    private ScreenReceiver mReceiver;
    private int rvSpinnerHeight = 0;
    private boolean checkClickHideView = false;
    private Handler handler;
    private Runnable runnable;
    private boolean isCheckClickHideView = false;
    private WindowInsetsControllerCompat windowInsetsController;
    private boolean showVideo = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Utils.overLockScreen(this);
        super.onCreate(savedInstanceState);

//        Utils.showFullScreen(this);

        windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        // Configure the behavior of the hidden system bars.
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE
        );

        windowInsetsController.show(WindowInsetsCompat.Type.statusBars());
        windowInsetsController.show(WindowInsetsCompat.Type.navigationBars());
        getWindow().setStatusBarColor(getColor(R.color.color_white_33));

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

        binding = ActivityVideoCallAnswerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        initData();
        initEvent();
        setHandler();
    }

    private void initView() {
        textureView = binding.texturePreview;
        roundCameraGLSurfaceView = binding.cameraGlSurfaceView;
        roundCameraGLSurfaceView.setFragmentShaderCode(GLUtil.FRAG_SHADER_SCULPTURE);
        roundCameraGLSurfaceView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        Toolbox.setMargins(binding.itemButtomFooter.getRoot(), Toolbox.dp(50), Toolbox.dp(150), Toolbox.dp(50), Toolbox.dp(150));
        binding.itemButtomFooter.getRoot().setVisibility(showVideo ? View.VISIBLE : View.GONE);
    }

    private void initEvent() {
        binding.layoutTopCall.imBack.setOnClickListener(v -> finish());
        binding.itemButtomFooter.llCancelCall.setOnClickListener(v -> finish());
        binding.view.setOnClickListener(v -> {
            setAnimated(binding.iconFitler.getVisibility() != View.VISIBLE);
        });
        binding.itemButtomFooter.llAudio.setOnClickListener(view -> {
            isCheckClickHideView = !isCheckClickHideView;
            cameraHelper.switchCamera(this, isCheckClickHideView);
        });
    }

    private void initData() {
        Uri uri = Uri.parse(objectCalling.getPathVideo());
        if (uri == null)
            return;
        binding.videoView.setVideoURI(uri);
        binding.videoView.start();
        binding.layoutTopCall.tvName.setText(objectCalling.getName());
    }

    void initCamera() {
        cameraHelper = new CameraHelper.Builder()
                .cameraListener(this)
                .specificCameraId(CAMERA_ID)
                .previewOn(textureView)
                .previewViewSize(new Point(textureView.getLayoutParams().width, textureView.getLayoutParams().height))
                .rotation(getWindowManager().getDefaultDisplay().getRotation())
                .build();
        cameraHelper.start();
    }

    @Override
    public void onGlobalLayout() {
        roundCameraGLSurfaceView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        int withScreen = Utils.getWithScreen(this);

        FrameLayout.LayoutParams textureViewLayoutParams = (FrameLayout.LayoutParams) textureView.getLayoutParams();

        textureViewLayoutParams.width = (withScreen / 360) * 118;
        textureViewLayoutParams.height = (withScreen / 360) * 190;

        textureView.setLayoutParams(textureViewLayoutParams);

        initCamera();
    }

    @Override
    protected void onPause() {
        if (cameraHelper != null) {
            cameraHelper.stop();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cameraHelper != null) {
            cameraHelper.start();
        }
    }


    @Override
    public void onCameraOpened(Camera camera, final int cameraId, final int displayOrientation, boolean isMirror) {
        previewSize = camera.getParameters().getPreviewSize();
        squarePreviewSize = Math.min(previewSize.width, previewSize.height);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                {
                    ViewGroup.LayoutParams layoutParams = textureView.getLayoutParams();
                    //横屏
                    if (displayOrientation % 180 == 0) {
                        layoutParams.height = layoutParams.width * previewSize.height / previewSize.width;
                    }
                    //竖屏
                    else {
                        layoutParams.height = layoutParams.width * previewSize.width / previewSize.height;
                    }
                    textureView.setLayoutParams(layoutParams);
                }
                roundCameraGLSurfaceView.init(cameraId == Camera.CameraInfo.CAMERA_FACING_FRONT, displayOrientation, squarePreviewSize, squarePreviewSize);
                radiusCamera();

            }
        });
    }

    @Override
    public void onPreview(final byte[] nv21, Camera camera) {
    }

    @Override
    public void onCameraClosed() {
    }

    @Override
    public void onCameraError(Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onCameraConfigurationChanged(int cameraID, int displayOrientation) {

    }

    @Override
    protected void onDestroy() {
        if (cameraHelper != null) {
            cameraHelper.release();
        }
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        Utils.clearFlags(this);
        super.onDestroy();
    }

    private void radiusCamera() {
        int progress = 15;
        int max = 100;
        textureView.setRadius(progress * Math.min(textureView.getWidth(), textureView.getHeight()) / 2 / max);
        textureView.turnRound();
    }

    public class ScreenReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                Intent returnIntent = new Intent();
                VideoCallAnswerActivity.this.setResult(OutCommingActivity.RESULT_PAUSE, returnIntent);
                finish();
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {

            int hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);

            List<String> permissions = new ArrayList<String>();

            if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CAMERA);

            }
            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[0]), 111);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 111: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        System.out.println("Permissions --> " + "Permission Granted: " + permissions[i]);


                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        System.out.println("Permissions --> " + "Permission Denied: " + permissions[i]);

                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacksAndMessages(null);
    }

    private void setHandler() {
        handler = new Handler();
        runnable = () -> setAnimated(false);
        handler.postDelayed(runnable, 3000L);
    }

    private void setAnimated(boolean hasChange) {
        if(showVideo) {
            binding.layoutTopCall.getRoot().animate().alpha(hasChange ? 1.0f : 0.0f).scaleX(hasChange ? 1.0f : 0.0f).scaleY(hasChange ? 1.0f : 0.0f).setDuration(180).start();
            binding.layoutTopCall.getRoot().setVisibility(hasChange ? View.VISIBLE : View.GONE);
            binding.itemButtomFooter.getRoot().animate().alpha(hasChange ? 1.0f : 0.0f).scaleX(1.0f).scaleY(1.0f).setDuration(180).translationY(hasChange ? 1.0f : binding.itemButtomFooter.ctsBottomNavigation.getMeasuredHeight()).start();
            binding.itemButtomFooter.getRoot().setVisibility(hasChange ? View.VISIBLE : View.GONE);
        } else {
            binding.itemButtomFooter.getRoot().setVisibility(View.GONE);
        }
        binding.iconFitler.animate().alpha(hasChange ? 1.0f : 0.0f).scaleX(hasChange ? 1.0f : 0.0f).scaleY(hasChange ? 1.0f : 0.0f).setDuration(180).start();
        binding.texturePreview.animate().alpha(1.0f).scaleX(hasChange ? 1.0f : 1.2f).scaleY(hasChange ? 1.0f : 1.2f).setDuration(180).translationY(hasChange ? 0 : -30).start();
        binding.iconFitler.setVisibility(hasChange ? View.VISIBLE : View.GONE);
        binding.frSurface.setVisibility(hasChange ? View.VISIBLE : View.GONE);
        if (hasChange) {
            setHandler();
            windowInsetsController.show(WindowInsetsCompat.Type.statusBars());
            windowInsetsController.show(WindowInsetsCompat.Type.navigationBars());
        } else {
            if (handler != null)
                handler.removeCallbacksAndMessages(null);
            windowInsetsController.hide(WindowInsetsCompat.Type.statusBars());
            windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars());
        }
    }

}
