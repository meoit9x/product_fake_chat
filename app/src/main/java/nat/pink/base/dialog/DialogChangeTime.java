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

import nat.pink.base.databinding.DialogChangeTimeBinding;

public class DialogChangeTime extends Dialog {

    public DialogChangeTime(@NonNull Context context, Consumer<CHANGE_TYPE> consumer) {
        super(context);
        this.consumer = consumer;
    }

    public enum CHANGE_TYPE {
        NOW, FIVE_SECONDS, TEN_SSECONDS, FIFTEEN_SECONDS, TWENTY_SECONDS
    }

    private Consumer<CHANGE_TYPE> consumer;

    private DialogChangeTimeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        binding = DialogChangeTimeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivExit.setOnClickListener(view -> dismiss());
        binding.llTenSeconds.setOnClickListener(view -> consumer.accept(CHANGE_TYPE.TEN_SSECONDS));
        binding.txtFifteenSeconds.setOnClickListener(view -> consumer.accept(CHANGE_TYPE.FIFTEEN_SECONDS));
        binding.txtFiveSeconds.setOnClickListener(view -> consumer.accept(CHANGE_TYPE.FIVE_SECONDS));
        binding.txtGetNow.setOnClickListener(view -> consumer.accept(CHANGE_TYPE.NOW));
        binding.txtTwentySeconds.setOnClickListener(view -> consumer.accept(CHANGE_TYPE.TWENTY_SECONDS));
    }
}
