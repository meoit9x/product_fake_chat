package nat.pink.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

import nat.pink.base.R;
import nat.pink.base.databinding.DialogCountdownTimeBinding;
import nat.pink.base.utils.Const;
import nat.pink.base.utils.PreferenceUtil;
import nat.pink.base.utils.Utils;

public class DialogCountdownTime extends Dialog {
    private DialogCountdownTimeBinding binding;
    private long time = 0;
    private String type = "";
    private Consumer<Object> consumer;
    private CountDownTimer countDownTimer;

    public DialogCountdownTime(@NonNull Context context, int themeResId, Consumer<Object> consumer) {
        super(context, themeResId);
        this.consumer = consumer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        binding = DialogCountdownTimeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (time > 0) {
            countDownTimer = new CountDownTimer(time - System.currentTimeMillis(), 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    String currentTime = Utils.formatDuration(millisUntilFinished / 1000, true);
                    binding.tvTime.setText(currentTime);
                }

                @Override
                public void onFinish() {
                    dismiss();
                }
            };
            countDownTimer.start();
        }

        String tvMsg = "";
        switch (type) {
            case Const.KEY_ADS_VIDEO_CALL:
                tvMsg = getContext().getString(R.string.ads_video_call);
                break;
            case Const.KEY_ADS_VOICE_CALL:
                tvMsg = getContext().getString(R.string.ads_voice_call);
                break;
            case Const.KEY_ADS_NOTIFICATION:
                tvMsg = getContext().getString(R.string.ads_notification);
                break;
            case Const.KEY_ADS_DONE:
                binding.tvTime.setText(getContext().getString(R.string.thank_you));
                binding.tvTitle.setImageResource(R.drawable.ic_tick);
                tvMsg = getContext().getString(R.string.ads_done);
                break;
            case Const.KEY_ADS_PRESENT_EVERYDAY:
                binding.tvTime.setText(getContext().getString(R.string.three_extra_coin));
                binding.tvTime.setTextColor(getContext().getColor(R.color.color_FE9923));
                binding.tvTitle.setImageResource(R.drawable.ic_coin);
                tvMsg = getContext().getString(R.string.daily_login);
                binding.txtDone.setText(getContext().getString(R.string.claim));
                break;
            case Const.KEY_ADS_PRESENT:
                binding.tvTime.setText(getContext().getString(R.string.two_extra_coin));
                binding.tvTime.setTextColor(getContext().getColor(R.color.color_FE9923));
                binding.tvTitle.setImageResource(R.drawable.ic_coin);
                tvMsg = getContext().getString(R.string.received_a_reward);
                binding.txtDone.setText(getContext().getString(R.string.claim));
                break;
            case Const.KEY_DISCONNECTED:
                binding.tvTime.setText(getContext().getString(R.string.disconnected));
                binding.tvTitle.setImageResource(R.drawable.ic_no_wifi);
                tvMsg = getContext().getString(R.string.disconnected_des);
                binding.txtDone.setText(getContext().getString(R.string.ok));
                break;
            default:
                break;

        }
        binding.tvMsg.setText(tvMsg);

        binding.txtDone.setOnClickListener(v -> {
            consumer.accept(new Object());
            dismiss();
        });
    }

    public void setTimeAndTitle(long time, String key) {
        this.time = time;
        this.type = key;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (countDownTimer != null)
            countDownTimer.cancel();
    }
}
