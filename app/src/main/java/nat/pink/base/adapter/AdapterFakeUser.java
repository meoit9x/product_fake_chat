package nat.pink.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import nat.pink.base.R;
import nat.pink.base.databinding.ItemFakeUserBinding;
import nat.pink.base.model.DaoContact;
import nat.pink.base.utils.ImageUtils;
import nat.pink.base.utils.Utils;

public class AdapterFakeUser extends RecyclerView.Adapter<AdapterFakeUser.ViewHolder> {

    private List<DaoContact> fakeUsers = new ArrayList<>();
    private Context context;
    private ItemClickListener itemClickListener;

    public interface ItemClickListener {
        void onItemClickListener(int position, List<DaoContact> user);
    }

    public AdapterFakeUser(Context context, ItemClickListener itemClickListener) {
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    public void setFakeUsers(List<DaoContact> fakeUsers) {
        this.fakeUsers = fakeUsers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFakeUserBinding binding = ItemFakeUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AdapterFakeUser.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemFakeUserBinding binding = holder.binding;
        binding.getRoot().setOnClickListener(v -> {
            itemClickListener.onItemClickListener(position, fakeUsers);
        });

        if (position == 0) {
            binding.avatar.setImageResource(R.drawable.add_fake_user2);
            binding.name.setText(context.getString(R.string.create_new));
            binding.onlineStatus.setVisibility(View.GONE);
            return;
        }

        DaoContact user = fakeUsers.get(position - 1);
        ImageUtils.loadImage(context, binding.avatar, user.getAvatar());
        binding.name.setText(user.getName());
        binding.mcvOnline.setCardBackgroundColor(context.getColor(user.getOnline() == 1 ? R.color.color_5AD439 : R.color.gray_D9));
    }

    @Override
    public int getItemCount() {
        return fakeUsers == null ? 1 : (fakeUsers.size() + 1);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemFakeUserBinding binding;

        public ViewHolder(@NonNull ItemFakeUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
