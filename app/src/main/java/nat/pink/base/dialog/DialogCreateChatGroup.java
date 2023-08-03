package nat.pink.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nat.pink.base.R;
import nat.pink.base.adapter.AdapterFakeUserContact;
import nat.pink.base.adapter.AdapterFakeUserGroup;
import nat.pink.base.databinding.DialogCreateChatGroupBinding;
import nat.pink.base.databinding.DialogSelectChatBinding;
import nat.pink.base.model.DaoContact;
import nat.pink.base.utils.Config;


public class DialogCreateChatGroup extends BaseDialog<DialogCreateChatGroup.ExtendBuilder> {
    private DialogCreateChatGroupBinding binding;
    private AdapterFakeUserGroup adapterFakeUserGroup;
    private ExtendBuilder extendBuilder;
    private String urlAvatar;
    private boolean isStatus = true;
    private List<DaoContact> contactList;

    public DialogCreateChatGroup(ExtendBuilder builder) {
        super(builder);
        this.extendBuilder = builder;
    }

    @Override
    protected void initControl() {
        binding.ivExit.setOnClickListener(v -> dismiss());
        binding.swStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setStatus(isChecked);
        });
    }

    @Override
    protected void initData() {
        contactList = new ArrayList<>(extendBuilder.contacts);
        adapterFakeUserGroup = new AdapterFakeUserGroup(getContext(), o -> {
            contactList.remove((int) o);
            adapterFakeUserGroup.setDataUser(contactList);
        });
        adapterFakeUserGroup.setDataUser(extendBuilder.contacts);

        LinearLayoutManager ln = new LinearLayoutManager(getContext());
        ln.setOrientation(RecyclerView.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                ln.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.item_decor));
        binding.rcvSuggest.setLayoutManager(ln);
        binding.rcvSuggest.addItemDecoration(dividerItemDecoration);
        binding.rcvSuggest.setAdapter(adapterFakeUserGroup);
    }

    @Override
    protected View getLayoutResource() {
        binding = DialogCreateChatGroupBinding.inflate(LayoutInflater.from(getContext()));
        return binding.getRoot();
    }

    @Override
    protected TextView getTitle() {
        return binding.selectContact;
    }

    @Override
    protected TextView getNegativeButton() {
        return binding.txtClose;
    }

    @Override
    protected TextView getPositiveButton() {
        return binding.txtDone;
    }

    @Override
    protected void handleClickPositiveButton(HashMap<String, Object> datas) {
        if (validate()) {
            datas.put(Config.KEY_TITLE, binding.edtNameGroup.getText().toString());
            datas.put(Config.KEY_AVATAR, urlAvatar);
            datas.put(Config.KEY_GROUP, extendBuilder.isCreateGroup);
            datas.put(Config.KEY_STATUS_ON, isStatus);
            datas.put(Config.KEY_LIST_USER, contactList);
            super.handleClickPositiveButton(datas);
        }
    }

    private boolean validate() {
        if (TextUtils.isEmpty(binding.edtNameGroup.getText().toString())) {
            Toast.makeText(getContext()
                    , getString(R.string.you_must_enter, getString(R.string.convesation_name))
                    , Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void setImageConversation(String url) {
        this.urlAvatar = url;
//        Glide.with(this)
//                .load(urlAvatar)
//                .skipMemoryCache(true)
//                .placeholder(R.drawable.ic_user_default)
//                .into(binding.imAvatar);
    }

    public static class ExtendBuilder extends BuilderDialog {

        private SelectImageListener selectImageListener;
        private boolean isCreateGroup;

        public ExtendBuilder setCreateGroup(boolean createGroup) {
            isCreateGroup = createGroup;
            return this;
        }

        public interface SelectImageListener {
            void onSelectImage(DialogCreateChatGroup dialog);
        }

        public ExtendBuilder onSelectImageListener(SelectImageListener selectImageListener) {
            this.selectImageListener = selectImageListener;
            return this;
        }

        @Override
        public BaseDialog build() {
            return new DialogCreateChatGroup(this);
        }
    }

    public boolean isStatus() {
        return isStatus;
    }

    public void setStatus(boolean status) {
        isStatus = status;
    }

}
