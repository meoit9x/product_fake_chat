package nat.pink.base.dialog;

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
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import nat.pink.base.R;
import nat.pink.base.adapter.AdapterChangeColor;
import nat.pink.base.databinding.DialogChangeColorBinding;
import nat.pink.base.databinding.DialogEnoughPointBinding;

public class DialogEnoughPoints extends Dialog {

    public DialogEnoughPoints(@NonNull Context context, Consumer<Object> consumer) {
        super(context);
        this.consumer = consumer;
        this.context = context;
    }

    private DialogEnoughPointBinding binding;
    private Consumer<Object> consumer;
    private Context context;
    private AdapterChangeColor adapterChangeColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        binding = DialogEnoughPointBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.txtClose.setOnClickListener(view -> dismiss());
        binding.txtDone.setOnClickListener(view -> {
            consumer.accept(new Object());
            dismiss();
        });
    }
}
