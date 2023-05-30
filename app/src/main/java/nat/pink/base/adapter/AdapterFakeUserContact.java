package nat.pink.base.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import nat.pink.base.R;
import nat.pink.base.databinding.ItemFakeUserContactBinding;
import nat.pink.base.model.DaoContact;
import nat.pink.base.utils.ImageUtils;
import nat.pink.base.utils.Utils;


public class AdapterFakeUserContact extends RecyclerView.Adapter<AdapterFakeUserContact.ViewHolder> {

    private List<DaoContact> fakeUsers;
    private Context context;
    private Consumer<DaoContact> consumer;

    public AdapterFakeUserContact(Context context, List<DaoContact> fakeUsers, Consumer<DaoContact> consumer) {
        this.context = context;
        this.consumer = consumer;
        this.fakeUsers = fakeUsers;
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
        DaoContact user = fakeUsers.get(position);

        if (user.getAvatar()!=null&& user.getAvatar().contains("R.drawable")) {
            binding.avatar.setImageResource(Utils.convertStringToDrawable(context, user.getAvatar()));
        } else
            ImageUtils.loadImage(binding.avatar, user.getAvatar());

        binding.tvName.setText(user.getName());
        binding.ivVerify.setVisibility(user.isVerified() ? View.VISIBLE : View.INVISIBLE);

        //case add new user
        if (user.getId() == -1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.ivVerify.setVisibility(View.GONE);
                binding.tvName.setTextColor(context.getColor(R.color.color_7C76CE));
                binding.tvName.setTextAppearance(Typeface.NORMAL);
                binding.ivAction.setVisibility(View.GONE);
            }
        }
        binding.mcvOnline.setCardBackgroundColor(context.getColor(user.getOnline() == 1 ? R.color.color_5AD439 : R.color.gray_D9));
        binding.mainAction.setOnClickListener(v -> consumer.accept(user));
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
