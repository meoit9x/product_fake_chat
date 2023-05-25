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

public class DialogChangeColor extends Dialog {

    public DialogChangeColor(@NonNull Context context, Consumer<Integer> consumer) {
        super(context);
        this.consumer = consumer;
        this.context = context;
    }

    private DialogChangeColorBinding binding;
    private Consumer<Integer> consumer;
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

        binding = DialogChangeColorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.txtClose.setOnClickListener(view -> dismiss());
        binding.txtDone.setOnClickListener(view -> {
            consumer.accept(adapterChangeColor.getColorDefault());
            dismiss();
        });
        binding.rcvColor.setLayoutManager(new GridLayoutManager(context, 6));

        adapterChangeColor = new AdapterChangeColor(context, initDataDefault());
        binding.rcvColor.setAdapter(adapterChangeColor);
    }

    private List<Integer> initDataDefault() {
        List<Integer> integers = new ArrayList<>();
        integers.add(context.getColor(R.color.color_6366F1));
        integers.add(context.getColor(R.color.color_3B82F6));
        integers.add(context.getColor(R.color.color_F97316));
        integers.add(context.getColor(R.color.color_FACC15));
        integers.add(context.getColor(R.color.color_4ADE80));
        integers.add(context.getColor(R.color.color_2DD4BF));
        integers.add(context.getColor(R.color.color_EF4444));
        integers.add(context.getColor(R.color.color_EC4899));
        integers.add(context.getColor(R.color.color_D946EF));
        integers.add(context.getColor(R.color.color_8B5CF6));
        integers.add(context.getColor(R.color.color_10B981));
        integers.add(context.getColor(R.color.color_84CC16));
        return integers;
    }
}