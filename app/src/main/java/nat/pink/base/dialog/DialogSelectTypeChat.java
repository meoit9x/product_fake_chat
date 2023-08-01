package nat.pink.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nat.pink.base.R;
import nat.pink.base.adapter.AdapterFakeUserContact;
import nat.pink.base.databinding.DialogSelectChatBinding;
import nat.pink.base.databinding.DialogSelectTypeChatBinding;
import nat.pink.base.model.DaoContact;


public class DialogSelectTypeChat extends Dialog {

    private Consumer<String> consumer;
    private DialogSelectTypeChatBinding binding;

    public DialogSelectTypeChat(@NonNull Context context, int themeResId, Consumer<String> consumer) {
        super(context, themeResId);
        this.consumer = consumer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 100);
        this.getWindow().setBackgroundDrawable(inset);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        binding = DialogSelectTypeChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initData();
        initEvent();
    }

    void initData() {
        binding.itemChat.avatar.setImageResource(R.drawable.ic_profile_photo);
        binding.itemChat.tvName.setText(getContext().getString(R.string.create_chat));
        binding.itemChat.ivAction.setImageResource(R.drawable.ic_next);
        binding.itemChat.ivVerify.setVisibility(View.GONE);

        binding.itemGroup.avatar.setImageResource(R.drawable.ic_profile_group_photo);
        binding.itemGroup.tvName.setText(getContext().getString(R.string.create_group_chat));
        binding.itemGroup.ivAction.setImageResource(R.drawable.ic_next);
        binding.itemGroup.ivVerify.setVisibility(View.GONE);
    }

    void initEvent() {
        binding.ivExit.setOnClickListener(v -> DialogSelectTypeChat.this.dismiss());
        binding.itemChat.getRoot().setOnClickListener(view -> {
            consumer.accept(getContext().getString(R.string.create_chat));
        });
        binding.itemGroup.getRoot().setOnClickListener(view -> {
            consumer.accept(getContext().getString(R.string.create_group_chat));
        });
    }


}
