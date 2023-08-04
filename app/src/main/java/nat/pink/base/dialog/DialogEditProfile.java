package nat.pink.base.dialog;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import nat.pink.base.R;
import nat.pink.base.databinding.DialogEditProfileBinding;
import nat.pink.base.utils.Config;

public class DialogEditProfile extends BaseDialog<DialogEditProfile.ExtendBuilder> {

    private DialogEditProfileBinding profileBinding;
    private ExtendBuilder extendBuilder;

    public DialogEditProfile(ExtendBuilder builder) {
        super(builder);
        this.extendBuilder = builder;
    }

    @Override
    protected void initView() {
        super.initView();
        if (!TextUtils.isEmpty(extendBuilder.nameGroup))
            profileBinding.edtFriendOn.setText(extendBuilder.nameGroup);
        if (!TextUtils.isEmpty(extendBuilder.liveIn))
            profileBinding.edtLiveIn.setText(extendBuilder.liveIn);
    }

    @Override
    protected void initControl() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected TextView getTitle() {
        return profileBinding.tvTitle;
    }

    @Override
    protected TextView getNegativeButton() {
        return profileBinding.tvNegative;
    }

    @Override
    protected TextView getPositiveButton() {
        return profileBinding.tvPositive;
    }

    @Override
    protected void handleClickPositiveButton(HashMap<String, Object> datas) {
        if (validate()) {
            datas.put(Config.KEY_NAME_GROUP, profileBinding.edtFriendOn.getText().toString());
            super.handleClickPositiveButton(datas);
        }
    }

    private boolean validate() {
        if (TextUtils.isEmpty(profileBinding.edtFriendOn.getText().toString())) {
            Toast.makeText(getContext()
                    , getString(R.string.you_must_enter, getString(R.string.full_data))
                    , Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    protected View getLayoutResource() {
        profileBinding = DialogEditProfileBinding.inflate(LayoutInflater.from(getContext()));
        return profileBinding.getRoot();
    }

    public static class ExtendBuilder extends BuilderDialog {

        private String nameGroup;
        private String liveIn;

        public ExtendBuilder setNameGroup(String nameGroup) {
            this.nameGroup = nameGroup;
            return this;
        }

        public ExtendBuilder setLiveIn(String liveIn) {
            this.liveIn = liveIn;
            return this;
        }

        @Override
        public BaseDialog build() {
            return new DialogEditProfile(this);
        }
    }
}
