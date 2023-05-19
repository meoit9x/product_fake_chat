package com.example.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

import com.example.base.R;
import com.example.base.databinding.DialogCountdownTimeBinding;

public class DialogCountdownTime extends Dialog {
    private Consumer consumer;
    private DialogCountdownTimeBinding binding;
    private Paint paint;
    private boolean notification = false;
    private boolean another = false;
    private boolean delete = false;
    private String time;

    public DialogCountdownTime(@NonNull Context context, int themeResId, Consumer consumer) {
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

        paint = binding.extOk.getPaint();
        float width = paint.measureText(binding.extOk.getText().toString());

        Shader textShader = new LinearGradient(0f, 0f, width, binding.extOk.getTextSize(), new int[]{
                Color.parseColor("#E35D93"),
                Color.parseColor("#AF39EF"),
                Color.parseColor("#3877FB")
        }, null, Shader.TileMode.REPEAT);
        if (!delete) {
            paint.setShader(textShader);
        }
        binding.tvTime.setText(time);
        if (notification) {
            binding.tvMsg.setText(getContext().getText(R.string.noti_come_later));
        } else if (another) {
            binding.tvMsg.setText(getContext().getText(R.string.another_task));
            binding.tvTime.setVisibility(View.GONE);
            binding.extEdit.setVisibility(View.GONE);
        } else if (delete) {
            binding.tvTitle.setText(getContext().getText(R.string.delete));
            binding.tvMsg.setText(getContext().getText(R.string.delete_this_conversation));
            binding.tvTime.setVisibility(View.GONE);
            binding.extEdit.setText(getContext().getText(R.string.cancel));
            binding.extOk.setText(getContext().getText(R.string.delete));
            binding.extOk.setTextColor(getContext().getColor(R.color.color_FF3B30));
        } else {
            binding.tvMsg.setText(getContext().getText(R.string.call_come_later));
            binding.tvMsg.setVisibility(View.VISIBLE);
            binding.extEdit.setVisibility(View.VISIBLE);
        }

        binding.extEdit.setOnClickListener(v -> {
            dismiss();
        });


        binding.extOk.setOnClickListener(v -> {
            consumer.accept(new Object());
            dismiss();
        });
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setNotification(boolean notification, boolean another) {
        this.notification = notification;
        this.another = another;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }
}
