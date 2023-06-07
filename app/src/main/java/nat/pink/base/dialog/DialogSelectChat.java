package nat.pink.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
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

import java.util.ArrayList;
import java.util.List;

import nat.pink.base.R;
import nat.pink.base.adapter.AdapterFakeUserContact;
import nat.pink.base.databinding.DialogSelectChatBinding;
import nat.pink.base.model.DaoContact;


public class DialogSelectChat extends Dialog {
    public enum TYPE_ACTION {
        ACTION_MESSAGE, ACTION_VOICE, ACTION_VIDEO, ACTION_NOTIFICATION
    }

    private TYPE_ACTION typeAction = TYPE_ACTION.ACTION_MESSAGE;
    private Consumer consumer;
    private DialogSelectChatBinding binding;
    private AdapterFakeUserContact adapterSuggest, adapterContact;
    private List<DaoContact> contactSuggests, contactNormals;

    public DialogSelectChat(@NonNull Context context, int themeResId, Consumer<DaoContact> consumer) {
        super(context, themeResId);
        this.consumer = consumer;
    }

    public void setContactSuggest(List<DaoContact> fakeUsers) {
        contactSuggests = fakeUsers;

    }

    public void setContactNormar(List<DaoContact> fakeUsers) {
        contactNormals = fakeUsers;
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
        binding = DialogSelectChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initData();
        initEvent();
    }

    void initData() {
        adapterSuggest = new AdapterFakeUserContact(getContext(), contactSuggests, data -> {
            consumer.accept(data);
        });
        adapterSuggest.setTypeAction(getTypeAction(), contactSuggests);

        LinearLayoutManager ln = new LinearLayoutManager(getContext());
        ln.setOrientation(RecyclerView.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                ln.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.item_decor));
        binding.rcvSuggest.setLayoutManager(ln);
        binding.rcvSuggest.addItemDecoration(dividerItemDecoration);
        binding.rcvSuggest.setAdapter(adapterSuggest);

        contactNormals.add(0, new DaoContact(-1, getContext().getString(R.string.create_new_manager_suggest), 0, false, false, 0, "", "", "", "R.drawable.ic_create_new"));

        //contact adapter
        adapterContact = new AdapterFakeUserContact(getContext(), contactNormals, user -> {
            consumer.accept(user);
        });
        adapterContact.setTypeAction(getTypeAction(), contactNormals);

        LinearLayoutManager ln2 = new LinearLayoutManager(getContext());
        ln.setOrientation(RecyclerView.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.item_decor));
        binding.rcvContact.setLayoutManager(ln2);
        binding.rcvContact.addItemDecoration(dividerItemDecoration);
        binding.rcvContact.setAdapter(adapterContact);
    }

    void initEvent() {
        binding.ivExit.setOnClickListener(v -> DialogSelectChat.this.dismiss());
    }

    public TYPE_ACTION getTypeAction() {
        return typeAction;
    }

    public void setTypeAction(TYPE_ACTION typeAction) {
        this.typeAction = typeAction;
        if (adapterSuggest != null) {
            adapterSuggest.setTypeAction(typeAction, contactSuggests);
        }
        if(adapterContact != null) {
            adapterContact.setTypeAction(typeAction, contactNormals);
        }
    }

}
