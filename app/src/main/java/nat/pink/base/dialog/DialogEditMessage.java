package nat.pink.base.dialog;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import nat.pink.base.R;
import nat.pink.base.base.BaseDialog;
import nat.pink.base.base.BuilderDialog;
import nat.pink.base.databinding.DialogEditMessagerBinding;
import nat.pink.base.utils.Config;

public class DialogEditMessage extends BaseDialog<DialogEditMessage.ExtendBuilder> {

    private DialogEditMessagerBinding binding;

    private ExtendBuilder extendBuilder;

    public DialogEditMessage(DialogEditMessage.ExtendBuilder builder) {
        super(builder);
        this.extendBuilder = builder;
    }

    @Override
    protected void initControl() {
        if (!TextUtils.isEmpty(extendBuilder.messageContent)) {
            binding.edtData.setText(extendBuilder.messageContent);
            binding.edtData.requestFocus(extendBuilder.messageContent.length());
        }
    }

    @Override
    protected TextView getTitle() {
        return binding.tvTitle;
    }

    @Override
    protected TextView getNegativeButton() {
        return binding.tvNegative;
    }

    @Override
    protected TextView getPositiveButton() {
        return binding.tvPositive;
    }

    @Override
    protected void handleClickPositiveButton(HashMap<String, Object> datas) {
        if (validate()) {
            datas.put(Config.KEY_CONTENT, binding.edtData.getText().toString());
            super.handleClickPositiveButton(datas);
        }
    }

    private boolean validate() {
        if (TextUtils.isEmpty(binding.edtData.getText().toString())) {
            Toast.makeText(getContext()
                    , getString(R.string.you_must_enter, getString(R.string.full_data))
                    , Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    protected View getLayoutResource() {
        binding = DialogEditMessagerBinding.inflate(LayoutInflater.from(getContext()));
        return binding.getRoot();
    }

    public static class ExtendBuilder extends BuilderDialog {

        private String messageContent;

        public ExtendBuilder setMessageContent(String messageContent) {
            this.messageContent = messageContent;
            return this;
        }

        @Override
        public BaseDialog build() {
            return new DialogEditMessage(this);
        }
    }
}


