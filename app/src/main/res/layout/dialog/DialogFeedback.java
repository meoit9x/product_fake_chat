package com.example.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

import com.example.base.databinding.DialogFeedbackBinding;

public class DialogFeedback extends Dialog {

    private Consumer<String> consumer;
    private DialogFeedbackBinding binding;

    public DialogFeedback(@NonNull Context context, int themeResId, Consumer<String> consumer) {
        super(context, themeResId);
        this.consumer = consumer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        binding = DialogFeedbackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.txtCancel.setOnClickListener(v -> dismiss());
        binding.txtSent.setOnClickListener(v -> {
//            if (binding.txtError.getVisibility() == View.VISIBLE)
//                return;
            consumer.accept(binding.edtRename.getText().toString());
            dismiss();
        });
        binding.edtRename.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                binding.txtError.setVisibility(s.toString().equals("") ? View.VISIBLE : View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}
