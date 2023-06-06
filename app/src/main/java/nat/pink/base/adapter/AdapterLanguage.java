package nat.pink.base.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import nat.pink.base.R;
import nat.pink.base.databinding.ItemLanguageBinding;
import nat.pink.base.model.ObjectLanguage;
import nat.pink.base.utils.PreferenceUtil;

public class AdapterLanguage extends RecyclerView.Adapter<AdapterLanguage.ViewHolder> {

    private ArrayList<ObjectLanguage> objectLanguages = new ArrayList<>();
    private Context context;
    private Consumer<ObjectLanguage> consumer;

    public AdapterLanguage(Context context, Consumer<ObjectLanguage> consumer) {
        this.context = context;
        this.consumer = consumer;
    }

    public void setObjectLanguages(ArrayList<ObjectLanguage> objectLanguages) {
        this.objectLanguages = objectLanguages;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLanguageBinding binding = ItemLanguageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AdapterLanguage.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemLanguageBinding binding = holder.binding;
        ObjectLanguage objectLanguage = objectLanguages.get(position);
        Glide.with(context).load(objectLanguage.getFlags()).into(binding.ivFlag);
        binding.txtFlag.setText(objectLanguage.getLanguage());
        boolean selected = objectLanguage.getValue().contains(PreferenceUtil.getString(context, PreferenceUtil.KEY_CURRENT_LANGUAGE, " "));
        binding.rb.setChecked(selected);
        if (objectLanguage.getValue().equals("en")){
            binding.rb.setChecked(selected);
        }
        binding.cvContent.setOnClickListener(view -> consumer.accept(objectLanguage));
        binding.txtFlag.setTextColor(selected ? Color.WHITE : context.getColor(R.color.color_212121));
        binding.rootView.setBackgroundResource(selected ? R.drawable.bg_setting_language : R.drawable.bg_white);
    }

    @Override
    public int getItemCount() {
        return objectLanguages == null ? 0 : objectLanguages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemLanguageBinding binding;

        public ViewHolder(@NonNull ItemLanguageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
