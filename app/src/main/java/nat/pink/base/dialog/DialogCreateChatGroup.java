package nat.pink.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.view.Gravity;
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
import nat.pink.base.adapter.AdapterFakeUserGroup;
import nat.pink.base.databinding.DialogCreateChatGroupBinding;
import nat.pink.base.databinding.DialogSelectChatBinding;
import nat.pink.base.model.DaoContact;


public class DialogCreateChatGroup extends Dialog {
    private Consumer<List<DaoContact>> consumer;
    private DialogCreateChatGroupBinding binding;
    private AdapterFakeUserGroup adapterFakeUserGroup;
    private List<DaoContact> contacts;

    public DialogCreateChatGroup(@NonNull Context context, int themeResId, Consumer<List<DaoContact>> consumer) {
        super(context, themeResId);
        this.consumer = consumer;
    }

    public void setContacts(List<DaoContact> fakeUsers) {
        contacts = fakeUsers;
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
        binding = DialogCreateChatGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initData();
        initEvent();
    }

    void initData() {
        adapterFakeUserGroup = new AdapterFakeUserGroup(getContext(), o -> {

        });
        adapterFakeUserGroup.setDataUser(contacts);

        LinearLayoutManager ln = new LinearLayoutManager(getContext());
        ln.setOrientation(RecyclerView.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                ln.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.item_decor));
        binding.rcvSuggest.setLayoutManager(ln);
        binding.rcvSuggest.addItemDecoration(dividerItemDecoration);
        binding.rcvSuggest.setAdapter(adapterFakeUserGroup);
    }

    void initEvent() {
        binding.ivExit.setOnClickListener(v -> dismiss());
        binding.txtClose.setOnClickListener(v -> dismiss());
        binding.txtDone.setOnClickListener(v -> {
            consumer.accept(contacts);
            dismiss();
        });
    }

}
