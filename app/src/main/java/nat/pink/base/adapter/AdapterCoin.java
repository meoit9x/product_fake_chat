package nat.pink.base.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import nat.pink.base.R;
import nat.pink.base.databinding.ItemCoinRankBinding;
import nat.pink.base.databinding.ItemFakeUserBinding;
import nat.pink.base.model.DaoContact;
import nat.pink.base.model.DataUserCoin;
import nat.pink.base.model.ResponseLeaderBoard;
import nat.pink.base.utils.ImageUtils;

public class AdapterCoin extends RecyclerView.Adapter<AdapterCoin.ViewHolder> {

    private List<DataUserCoin> dataUserCoins = new ArrayList<>();
    private Context context;
    private AdapterCoin.ItemClickListener itemClickListener;

    public interface ItemClickListener {
        void onItemClickListener(int position, List<DataUserCoin> user);
    }

    public AdapterCoin(Context context, AdapterCoin.ItemClickListener itemClickListener) {
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    public void setDataUserCoins(List<DataUserCoin> dataUserCoins) {
        this.dataUserCoins = dataUserCoins;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdapterCoin.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCoinRankBinding binding = ItemCoinRankBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AdapterCoin.ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdapterCoin.ViewHolder holder, int position) {
        ItemCoinRankBinding binding = holder.binding;
        binding.getRoot().setOnClickListener(v -> {
            itemClickListener.onItemClickListener(position, dataUserCoins);
        });

        DataUserCoin user = dataUserCoins.get(position);
        binding.txtNumber.setText(position + 1 + "");
        ImageUtils.loadImageCoin(context, binding.ivAvatar, null);
        binding.txtIdRank.setText(context.getString(R.string.id_user, user.getId() + ""));
        binding.txtTotalCoin.setText(user.getPoint() + "");
        int[] colors = {context.getColor(R.color.color_FFD82B), context.getColor(R.color.color_FFD82B), context.getColor(R.color.color_DD8D00)};
        Shader textShader = new LinearGradient(
                0f,
                0f,
                0f,
                (float) (binding.txtTotalCoin.getTextSize() * 1.5),
                colors,
                null,
                Shader.TileMode.CLAMP
        );
        Matrix matrix = new Matrix();
        textShader.setLocalMatrix(matrix);
        binding.txtTotalCoin.getPaint().setShader(textShader);
    }

    @Override
    public int getItemCount() {
        return dataUserCoins == null ? 0 : dataUserCoins.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemCoinRankBinding binding;

        public ViewHolder(@NonNull ItemCoinRankBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
