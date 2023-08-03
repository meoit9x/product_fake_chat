package nat.pink.base.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nat.pink.base.R;
import nat.pink.base.base.BaseRecyclerAdapter;
import nat.pink.base.databinding.ItemSelectMemberBinding;
import nat.pink.base.model.DaoContact;
import nat.pink.base.utils.ImageUtils;

public class SelectMemberAdapter extends BaseRecyclerAdapter<DaoContact, SelectMemberAdapter.ViewHolder> {
    public SelectMemberAdapter(Context context, List<DaoContact> list) {
        super(context, list);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(list.get(position));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_select_member, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemSelectMemberBinding selectMemberBinding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            selectMemberBinding = ItemSelectMemberBinding.bind(itemView);
        }

        public void bindData(DaoContact userMessageModel) {
            if (userMessageModel == null)
                return;
            ImageUtils.loadImage(mContext, selectMemberBinding.imAvatar, userMessageModel.getAvatar());
            if (!TextUtils.isEmpty(userMessageModel.getName()))
                selectMemberBinding.tvName.setText(userMessageModel.getName());

            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(getAdapterPosition());
            });
        }
    }
}
