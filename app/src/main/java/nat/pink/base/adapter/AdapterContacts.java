package nat.pink.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.function.Consumer;

import nat.pink.base.databinding.ItemChangeColorBinding;
import nat.pink.base.databinding.ItemManagerContactOneBinding;
import nat.pink.base.databinding.ItemManagerCreateNewBinding;
import nat.pink.base.databinding.ItemManagerNormalBinding;
import nat.pink.base.databinding.ItemManagerSuggestBinding;
import nat.pink.base.model.DaoContact;
import nat.pink.base.utils.ImageUtils;

public class AdapterContacts extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DaoContact> contactSuggests, contactNormals;
    private Context context;
    private Consumer consumerCreateNew;
    private Consumer<DaoContact> consumerEdit;
    private Consumer<DaoContact> consumerDelete;

    public AdapterContacts(Context context) {
        this.context = context;
    }

    public void setConsumerCreateNew(Consumer consumerCreateNew) {
        this.consumerCreateNew = consumerCreateNew;
    }

    public void setConsumerEdit(Consumer<DaoContact> consumerEdit) {
        this.consumerEdit = consumerEdit;
    }

    public void setConsumerDelete(Consumer<DaoContact> consumerDelete) {
        this.consumerDelete = consumerDelete;
    }

    public void setContactSuggests(List<DaoContact> contactSuggests) {
        this.contactSuggests = contactSuggests;
        notifyDataSetChanged();
    }

    public void setContactNormals(List<DaoContact> contactNormals) {
        this.contactNormals = contactNormals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                ItemManagerContactOneBinding binding = ItemManagerContactOneBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new AdapterContacts.ViewHolder(binding);
            case 2:
                ItemManagerSuggestBinding binding1 = ItemManagerSuggestBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new AdapterContacts.ViewHolderSuggest(binding1);
            case 3:
                ItemManagerCreateNewBinding binding2 = ItemManagerCreateNewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new AdapterContacts.ViewHolderCreateNew(binding2);
            case 4:
                ItemManagerNormalBinding binding3 = ItemManagerNormalBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new AdapterContacts.ViewHolderNormal(binding3);
        }
        ItemManagerContactOneBinding binding = ItemManagerContactOneBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AdapterContacts.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 1:
                break;
            case 2:
                ViewHolderSuggest viewHolderSuggest = (ViewHolderSuggest) holder;
                DaoContact daoContact = contactSuggests.get(position - 1);
                viewHolderSuggest.binding.txtName.setText(daoContact.getName());
                viewHolderSuggest.binding.ivCheckRank.setVisibility(daoContact.isVerified() ? View.VISIBLE : View.GONE);
                ImageUtils.loadImage(context, viewHolderSuggest.binding.ivAvatar, daoContact.getAvatar());
                viewHolderSuggest.binding.ivEdit.setOnClickListener(v -> consumerEdit.accept(daoContact));
                break;
            case 3:
                ViewHolderCreateNew viewHolderCreateNew = (ViewHolderCreateNew) holder;
                viewHolderCreateNew.binding.cvContent.setOnClickListener(view -> {
                    consumerCreateNew.accept(new Object());
                });
                break;
            case 4:
                ViewHolderNormal viewHolderNormal = (ViewHolderNormal) holder;
                DaoContact daoContactNormal = contactNormals.get(position - (contactSuggests.size() + 2));
                viewHolderNormal.binding.txtName.setText(daoContactNormal.getName());
                viewHolderNormal.binding.ivCheckRank.setVisibility(daoContactNormal.isVerified() ? View.VISIBLE : View.GONE);
                ImageUtils.loadImage(context, viewHolderNormal.binding.ivAvatar, daoContactNormal.getAvatar());
                viewHolderNormal.binding.ivEdit.setOnClickListener(v -> consumerEdit.accept(daoContactNormal));
                viewHolderNormal.binding.ivDelete.setOnClickListener(v -> consumerDelete.accept(daoContactNormal));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 1;
        if (position == 1 && contactSuggests.size() == 0)
            return 3;
        if (position > 0 && position < (contactSuggests.size() + 1))
            return 2;
        if (position == (contactSuggests.size() + 1))
            return 3;
        return 4;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        int suggest = contactSuggests == null ? 0 : contactSuggests.size();
        int normal = contactNormals != null ? contactNormals.size() : 0;
        return suggest + normal + 2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemManagerContactOneBinding binding;

        public ViewHolder(@NonNull ItemManagerContactOneBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class ViewHolderSuggest extends RecyclerView.ViewHolder {
        private final ItemManagerSuggestBinding binding;

        public ViewHolderSuggest(@NonNull ItemManagerSuggestBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class ViewHolderCreateNew extends RecyclerView.ViewHolder {
        private final ItemManagerCreateNewBinding binding;

        public ViewHolderCreateNew(@NonNull ItemManagerCreateNewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class ViewHolderNormal extends RecyclerView.ViewHolder {
        private final ItemManagerNormalBinding binding;

        public ViewHolderNormal(@NonNull ItemManagerNormalBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
