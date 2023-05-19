package com.example.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

import com.example.base.databinding.DialogRemoveAdsBinding;

public class DialogRemoveAds extends Dialog {

    private Consumer<Boolean> consumer;
    private DialogRemoveAdsBinding binding;

    public DialogRemoveAds(@NonNull Context context, int themeResId, Consumer<Boolean> consumer) {
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

        binding = DialogRemoveAdsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.txtRemoveAds.setOnClickListener(v -> {
            consumer.accept(true);
            dismiss();
        });
        binding.ivClose.setOnClickListener(v -> dismiss());

        binding.llSeeAds.setOnClickListener(v -> {
            consumer.accept(false);
            dismiss();
        });
    }
}
