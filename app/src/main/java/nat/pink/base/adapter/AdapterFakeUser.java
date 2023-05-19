package nat.pink.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import nat.pink.base.databinding.ItemFakeUserBinding;
import nat.pink.base.model.ObjectUser;
import nat.pink.base.utils.ImageUtils;

public class AdapterFakeUser extends RecyclerView.Adapter<AdapterFakeUser.ViewHolder> {

    private ArrayList<ObjectUser> fakeUsers = new ArrayList<>();
    private Context context;
    private Consumer<ObjectUser> consumer;

    public AdapterFakeUser(Context context, Consumer<ObjectUser> consumer) {
        this.context = context;
        this.consumer = consumer;
    }

    public void setFakeUsers(ArrayList<ObjectUser> fakeUsers) {
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
        ObjectUser user = fakeUsers.get(position);
        ImageUtils.loadImage(binding.avatar, user.getAvatar());
     //   Glide.with(context).load(user.getAvatar()).into(binding.avatar);
        binding.name.setText(user.getName());
        if (user.getStatus()==0) {
            binding.onlineStatus.setVisibility(View.GONE);
        }

        //binding.cvContent.setOnClickListener(view -> consumer.accept(objectLanguage));
    }

    @Override
    public int getItemCount() {
        return fakeUsers == null ? 0 : fakeUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemFakeUserBinding binding;

        public ViewHolder(@NonNull ItemFakeUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
