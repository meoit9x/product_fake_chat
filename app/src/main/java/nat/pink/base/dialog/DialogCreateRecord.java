package nat.pink.base.dialog;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import nat.pink.base.R;
import nat.pink.base.base.BaseDialog;
import nat.pink.base.base.BuilderDialog;
import nat.pink.base.databinding.DialogCreateRecordBinding;
import nat.pink.base.utils.Config;

public class DialogCreateRecord extends BaseDialog<DialogCreateRecord.ExtendBuilder> {

    private DialogCreateRecordBinding recordBinding;

    public DialogCreateRecord(ExtendBuilder builder) {
        super(builder);
    }

    @Override
    protected void initControl() {
        recordBinding.edtSetTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (recordBinding.edtSetTime.getText().toString().matches("^0")) {
                    recordBinding.edtSetTime.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected TextView getTitle() {
        return recordBinding.tvTitle;
    }

    @Override
    protected TextView getNegativeButton() {
        return recordBinding.tvNegative;
    }

    @Override
    protected TextView getPositiveButton() {
        return recordBinding.tvPositive;
    }

    @Override
    protected void handleClickPositiveButton(HashMap<String, Object> datas) {
        if (validate()) {
            datas.put(Config.KEY_CONTENT, recordBinding.edtSetTime.getText().toString());
            datas.put(Config.KEY_TYPE_SEND, recordBinding.rdSend.isChecked());
            super.handleClickPositiveButton(datas);
        }
    }

    private boolean validate() {
        if (TextUtils.isEmpty(recordBinding.edtSetTime.getText().toString())) {
            Toast.makeText(getContext()
                    , getString(R.string.you_must_enter, getString(R.string.full_data))
                    , Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    protected View getLayoutResource() {
        recordBinding = DialogCreateRecordBinding.inflate(LayoutInflater.from(getContext()));
        return recordBinding.getRoot();
    }

    public static class ExtendBuilder extends BuilderDialog {
        @Override
        public BaseDialog build() {
            return new DialogCreateRecord(this);
        }
    }
}

