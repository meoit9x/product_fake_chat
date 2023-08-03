package nat.pink.base.ui.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import nat.pink.base.R;
import nat.pink.base.adapter.SelectMemberAdapter;
import nat.pink.base.databinding.DialogSelectMemberBinding;
import nat.pink.base.model.DaoContact;

public class DialogSelectMember extends BaseDialog<DialogSelectMember.ExtendBuilder> {

    private DialogSelectMemberBinding selectMemberBinding;
    private SelectMemberAdapter selectMemberAdapter;
    private ExtendBuilder extendBuilder;

    public DialogSelectMember(ExtendBuilder builder) {
        super(builder);
        this.extendBuilder = builder;
    }

    @Override
    protected void initControl() {

    }

    @Override
    protected void initView() {
        super.initView();
        selectMemberAdapter = new SelectMemberAdapter(getContext(), extendBuilder.lstMember);
        selectMemberAdapter.setOnItemClickListener(position -> {
            if (extendBuilder.itemSelectListener != null)
                extendBuilder.itemSelectListener.onItemSelectListner(extendBuilder.lstMember.get(position));
            dismiss();
        });
        selectMemberBinding.rcvMember.setAdapter(selectMemberAdapter);
        selectMemberBinding.tvTitle.setText(getString(R.string.select_contact));
    }

    @Override
    protected TextView getTitle() {
        return selectMemberBinding.tvTitle;
    }

    @Override
    protected View getLayoutResource() {
        selectMemberBinding = DialogSelectMemberBinding.inflate(LayoutInflater.from(getContext()));
        return selectMemberBinding.getRoot();
    }

    public static class ExtendBuilder extends BuilderDialog {

        private List<DaoContact> lstMember = new ArrayList<>();
        private ItemSelectListener itemSelectListener;

        public interface ItemSelectListener {
            void onItemSelectListner(DaoContact userMessageModel);
        }

        public ExtendBuilder setItemSelectListener(ItemSelectListener itemSelectListener) {
            this.itemSelectListener = itemSelectListener;
            return this;
        }

        public ExtendBuilder setLstMember(List<DaoContact> lstMember) {
            this.lstMember.clear();
            this.lstMember.addAll(lstMember);
            return this;
        }

        @Override
        public BaseDialog build() {
            return new DialogSelectMember(this);
        }
    }
}
