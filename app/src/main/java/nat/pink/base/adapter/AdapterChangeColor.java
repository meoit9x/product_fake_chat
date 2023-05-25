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
import java.util.Objects;

import nat.pink.base.R;
import nat.pink.base.databinding.ItemChangeColorBinding;
import nat.pink.base.databinding.ItemFakeUserBinding;
import nat.pink.base.model.DaoContact;
import nat.pink.base.utils.ImageUtils;
import nat.pink.base.utils.Utils;

public class AdapterChangeColor extends RecyclerView.Adapter<AdapterChangeColor.ViewHolder> {

    private List<Integer> colors;
    private Context context;
    private int colorDefault;

    public int getColorDefault() {
        return colorDefault;
    }

    public AdapterChangeColor(Context context, List<Integer> colors) {
        this.context = context;
        this.colors = colors;
        colorDefault = colors.get(0);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChangeColorBinding binding = ItemChangeColorBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AdapterChangeColor.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemChangeColorBinding binding = holder.binding;
        binding.ivContent.setColorFilter(colors.get(position));
        binding.ivChangeAva.setVisibility(colorDefault == colors.get(position) ? View.VISIBLE : View.INVISIBLE);
        binding.getRoot().setOnClickListener(v -> {
            colorDefault = colors.get(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return colors == null ? 0 : colors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemChangeColorBinding binding;

        public ViewHolder(@NonNull ItemChangeColorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
