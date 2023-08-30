package nat.pink.base.ui.incoming;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

import nat.pink.base.R;
import nat.pink.base.base.BaseFragment;
import nat.pink.base.databinding.FragmentCallScreenBinding;
import nat.pink.base.model.DaoContact;
import nat.pink.base.model.ObjectCalling;
import nat.pink.base.utils.Config;
import nat.pink.base.utils.Const;
import nat.pink.base.utils.ImageUtils;
import nat.pink.base.utils.Utils;

public class CallScreenFragment extends BaseFragment<FragmentCallScreenBinding, CallScreenViewModel> {
    public static final String TAG = "CallScreenFragment";
    ObjectCalling objectCalling;
    private Timer timer;
    private boolean showIconVideo;
    private String timeString = "";
    private long mElapsedTime;
    private Handler handlerTime = new Handler();
    private Runnable updateTime;
    private ScreenReceiver mReceiver;

    public static final int RESULT_PAUSE = 1001;
    public CallScreenFragment() {

    }

    @Override
    protected CallScreenViewModel getViewModel() {
        return new ViewModelProvider(this).get(CallScreenViewModel.class);
    }

    @Override
    protected void initData() {
        super.initData();
        Bundle bundle = getArguments();
        if (bundle != null) {
            Serializable serializable = bundle.getSerializable(Const.PUT_EXTRAL_OBJECT_CALL);
            if (serializable instanceof ObjectCalling) {
                objectCalling = (ObjectCalling) serializable;
            }
            binding.txtName.setText(objectCalling.getName());
            ImageUtils.loadImage(binding.ivCall, objectCalling.getPathImage());
            if (TextUtils.isEmpty(objectCalling.getPathBackground())) {
                binding.ivContent.setVisibility(View.VISIBLE);
                ImageUtils.loadImage(binding.ivContent, objectCalling.getPathImage());
            } else {
                binding.ivContent.setVisibility(View.GONE);
                binding.clContent.setBackgroundResource(Utils.convertStringToDrawable(requireContext(), objectCalling.getPathBackground()));
            }

            showIconVideo = getArguments().getBoolean("show_icon_video",false);
            mElapsedTime = 0;
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        }
    }

    @Override
    protected void initView() {
        super.initView();
        //user  = new DaoContact(2, "Cristiano Ronaldo", 1, true,true,1, "harvard","new castle","", Uri.parse("android.resource://" + getContext().getPackageName() + "/drawable/ronaldo").toString());

        Utils.showFullScreen(getActivity());

        updateTime = () -> {
            timeString = "" + getDurationString((int) mElapsedTime);
       //     binding.tvCount.setText(timeString.isEmpty() && showIconVideo ? getResources().getText(R.string.title_calling) : !showIconVideo ? getResources().getText(R.string.title_calling) : timeString);
            if (!timeString.isEmpty() && mElapsedTime > 1 ){
                binding.tvCount.setText(timeString);
            }else{
                binding.tvCount.setText(getResources().getText(R.string.title_calling));
            }
        };
        timer = new Timer();
        Handler handler = new Handler();
        Runnable update = () -> {
            if (!showIconVideo) {
                /*Intent intent = new Intent(this, VideoCallAnswerActivity.class);
                intent.putExtra(Const.PUT_EXTRAL_OBJECT_CALL, objectCalling);
                startActivityForResult(intent, Config.CHECK_TURN_OFF_VOICE_INCOMING);*/
            }
        };
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 500);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        binding.ivRefuse.setOnClickListener(v -> {
            backStackFragment();
        });
        binding.ivAnswer.setOnClickListener(v -> {
            binding.ivReply.setVisibility(View.GONE);
            binding.txtAnswerContent.setVisibility(View.GONE);
            binding.txtRefuse.setVisibility(View.GONE);
            binding.ivRefuse.setVisibility(View.GONE);
            binding.txtAnswer.setVisibility(View.GONE);
            binding.ivAnswer.setVisibility(View.GONE);
            binding.footerButton.setVisibility(View.VISIBLE);
            binding.ivBack.setVisibility(View.VISIBLE);
            binding.swCamera.setVisibility(View.VISIBLE);

            startCallTimer();
        });

        binding.ivBack.setOnClickListener(v -> {
            backStackFragment();
        });
        binding.itemButtomFooter.llCancelCall.setOnClickListener(v -> backStackFragment());

    }

    public class ScreenReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                Intent returnIntent = new Intent();
                getActivity().setResult(RESULT_PAUSE, returnIntent);
                backStackFragment();
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.CHECK_TURN_OFF_VOICE_INCOMING) {
            if (resultCode == this.RESULT_PAUSE) {
                getActivity().setResult(resultCode, data);
            }
            backStackFragment();
        }
    }
    private void startCallTimer() {
        if (timer == null) {
            timer = new Timer();
            mElapsedTime = 0;
            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    mElapsedTime += 1;
                    handlerTime.post(updateTime);
                }
            }, 0, 1000);
        }
    }

    public String getDurationString(int seconds) {

        if (seconds < 0 || seconds > 2000000)
            seconds = 0;
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        if (hours == 0)
            return twoDigitString(minutes) + " : " + twoDigitString(seconds);
        else
            return twoDigitString(hours) + " : " + twoDigitString(minutes) + " : " + twoDigitString(seconds);
    }

    public String twoDigitString(int number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mElapsedTime = 0;
        if (timer != null) {
            timer.cancel();
        }
        if (mReceiver != null) {
            getActivity().unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        timer = null;
        Utils.clearFlags(getActivity());
    }
}