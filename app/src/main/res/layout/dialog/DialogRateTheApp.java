package com.example.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

import com.example.base.databinding.DialogRateTheAppBinding;

public class DialogRateTheApp extends Dialog {
    private Consumer consumer;
    private DialogRateTheAppBinding binding;

    public DialogRateTheApp(@NonNull Context context, int themeResId, Consumer consumer) {
        super(context, themeResId);
        this.consumer = consumer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        binding = DialogRateTheAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.extCancel.setOnClickListener(v -> dismiss());
        binding.rtContent.setOnRatingChangeListener((ratingBar, rating, fromUser) -> {
            consumer.accept(new Object());
            dismiss();
        });
    }
}
