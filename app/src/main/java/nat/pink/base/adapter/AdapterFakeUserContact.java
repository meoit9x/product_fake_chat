package nat.pink.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import nat.pink.base.databinding.ItemFakeUserBinding;
import nat.pink.base.databinding.ItemFakeUserContactBinding;
import nat.pink.base.model.ObjectUser;
import nat.pink.base.utils.ImageUtils;


public class AdapterFakeUserContact extends RecyclerView.Adapter<AdapterFakeUserContact.ViewHolder> {

    private ArrayList<ObjectUser> fakeUsers = new ArrayList<>();
    private Context context;
    private Consumer<ObjectUser> consumer;

    public AdapterFakeUserContact(Context context, Consumer<ObjectUser> consumer) {
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
        ItemFakeUserContactBinding binding = ItemFakeUserContactBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AdapterFakeUserContact.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemFakeUserContactBinding binding = holder.binding;
        ObjectUser user = fakeUsers.get(position);
        ImageUtils.loadImage(binding.avatar, user.getAvatar());
        //   Glide.with(context).load(user.getAvatar()).into(binding.avatar);
        binding.tvName.setText(user.getName());
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
        private final ItemFakeUserContactBinding binding;

        public ViewHolder(@NonNull ItemFakeUserContactBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
