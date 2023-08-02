package nat.pink.base.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nat.pink.base.R;
import nat.pink.base.databinding.ItemFakeUserContactBinding;
import nat.pink.base.dialog.DialogSelectChat;
import nat.pink.base.model.DaoContact;
import nat.pink.base.model.DataUserCoin;
import nat.pink.base.utils.ImageUtils;


public class AdapterFakeUserGroup extends RecyclerView.Adapter<AdapterFakeUserGroup.ViewHolder> {

    private List<DaoContact> fakeUsers;
    private Context context;
    private Consumer<Integer> consumer;

    public AdapterFakeUserGroup(Context context, Consumer<Integer> consumer) {
        this.context = context;
        this.consumer = consumer;
    }

    public void setDataUser(List<DaoContact> fakeUsers) {
        this.fakeUsers = fakeUsers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFakeUserContactBinding binding = ItemFakeUserContactBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AdapterFakeUserGroup.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemFakeUserContactBinding binding = holder.binding;
        DaoContact user = fakeUsers.get(position);

        ImageUtils.loadImage(context, binding.avatar, user.getAvatar());
        binding.tvName.setText(user.getName());
        binding.ivVerify.setVisibility(View.GONE);
        binding.ivAction.setImageResource(R.drawable.ic_pen_new_square);
        binding.ivDelete.setVisibility(View.VISIBLE);
        binding.onlineStatus.setVisibility(View.GONE);

        binding.ivDelete.setOnClickListener(v -> consumer.accept(position));
    }

    @Override
    public int getItemCount() {
        return fakeUsers == null ? 0 : fakeUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemFakeUserContactBinding binding;

        public ViewHolder(@NonNull ItemFakeUserContactBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
