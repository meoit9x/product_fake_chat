package nat.pink.base.ui.setting;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import nat.pink.base.R;
import nat.pink.base.adapter.AdapterCoin;
import nat.pink.base.base.BaseFragment;
import nat.pink.base.databinding.FragmentListPointRankingBinding;
import nat.pink.base.databinding.FragmentPrivacyBinding;
import nat.pink.base.dialog.DialogCoinFact;
import nat.pink.base.dialog.DialogLoading;
import nat.pink.base.model.DataUserCoin;
import nat.pink.base.model.ResponseLeaderBoard;
import nat.pink.base.retrofit.RequestAPI;
import nat.pink.base.retrofit.RetrofitClient;
import nat.pink.base.ui.home.HomeViewModel;
import nat.pink.base.utils.Const;
import nat.pink.base.utils.ImageUtils;
import nat.pink.base.utils.PreferenceUtil;
import retrofit2.Retrofit;

public class CoinRankingFragment extends BaseFragment<FragmentListPointRankingBinding, HomeViewModel> {
    public static final String TAG = "CoinRankingFragment";
    private AdapterCoin adapterCoin;
    protected RequestAPI requestAPI;
    private DialogLoading dialogLoading;
    private int userId = 0;

    @NonNull
    @Override
    public HomeViewModel getViewModel() {
        return new ViewModelProvider(this).get(HomeViewModel.class);
    }

    @Override
    public void initView() {
        super.initView();
        Retrofit retrofit = RetrofitClient.getInstance(requireContext(), Const.URL_REQUEST);
        requestAPI = retrofit.create(RequestAPI.class);
        dialogLoading = new DialogLoading(requireContext(), R.style.MaterialDialogSheet, o -> dialogLoading.dismiss());
        binding.itemCoinRank.cardView.setBackgroundTintList(ColorStateList.valueOf(requireContext().getColor(R.color.color_AF7BE4)));
        binding.itemCoinRank.txtIdRank.setTextColor(requireContext().getColor(R.color.white));
        binding.itemCoinRank.txtNumber.setTextColor(requireContext().getColor(R.color.white));
        ;
        binding.itemCoinRank.txtTotalCoin.setTextColor(requireContext().getColor(R.color.color_FFD82B));
    }

    @Override
    public void initData() {
        super.initData();
        userId = PreferenceUtil.getInt(requireContext(), PreferenceUtil.KEY_USER_ID);
        adapterCoin = new AdapterCoin(requireContext(), (position, user) -> {

        });
        dialogLoading.show();
        getViewModel().getLeaderBoard(requestAPI, o -> {
            dialogLoading.dismiss();
            if (o instanceof ResponseLeaderBoard) {
                ResponseLeaderBoard responseLeaderBoard = (ResponseLeaderBoard) o;
                adapterCoin.setDataUserCoins(responseLeaderBoard.getData());
                setData(responseLeaderBoard);
            }
        });
        setUpRecyclerView();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        binding.ivBack.setOnClickListener(v -> {
            backStackFragment();
        });
        binding.imgAsk.setOnClickListener(view -> {
            new DialogCoinFact(requireContext()).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void setUpRecyclerView() {
        binding.viewMain.setHasFixedSize(true);
        binding.viewMain.setItemAnimator(new DefaultItemAnimator());
        binding.viewMain.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.viewMain.setAdapter(adapterCoin);
    }

    @SuppressLint("SetTextI18n")
    private void setData(ResponseLeaderBoard responseLeaderBoard) {
        if (responseLeaderBoard != null && responseLeaderBoard.getData().size() > 0) {
            binding.extIdOne.setText(requireContext().getString(R.string.id_user, responseLeaderBoard.getData().get(0).getId() + ""));
            binding.extTotalOne.setText(responseLeaderBoard.getData().get(0).getPoint() + "");
            binding.extTotalOne.setSelected(true);
            int[] colors = {requireContext().getColor(R.color.color_FFD82B), requireContext().getColor(R.color.color_FFE989), requireContext().getColor(R.color.color_DD8D00)};
            Shader textShader = new LinearGradient(
                    0f,
                    0f,
                    0f,
                    (float) (binding.extTotalOne.getTextSize() * 1.5),
                    colors,
                    null,
                    Shader.TileMode.CLAMP
            );
            Matrix matrix = new Matrix();
            textShader.setLocalMatrix(matrix);
            binding.extTotalOne.getPaint().setShader(textShader);
            if (responseLeaderBoard.getData().size() > 1) {
                binding.extIdTwo.setText(requireContext().getString(R.string.id_user, responseLeaderBoard.getData().get(1).getId() + ""));
                binding.extTotalTwo.setText(responseLeaderBoard.getData().get(1).getPoint() + "");
                binding.extTotalTwo.setSelected(true);
                if (responseLeaderBoard.getData().size() > 2) {
                    binding.extIdThree.setText(requireContext().getString(R.string.id_user, responseLeaderBoard.getData().get(2).getId() + ""));
                    binding.extTotalThree.setText(responseLeaderBoard.getData().get(2).getPoint() + "");
                    binding.extTotalThree.setSelected(true);
                } else {
                    binding.llTopThree.setVisibility(View.GONE);
                }
            } else {
                binding.llTopTwo.setVisibility(View.GONE);
            }
            for (int i = 0; i < responseLeaderBoard.getData().size(); i++) {
                DataUserCoin dataUserCoin = responseLeaderBoard.getData().get(i);
                if (dataUserCoin.getId() == userId) {
                    binding.cstNextButton.setVisibility(View.VISIBLE);
                    binding.itemCoinRank.txtNumber.setText(i + 1 + "");
                    ImageUtils.loadImageCoin(requireContext(), binding.itemCoinRank.ivAvatar, null);
                    binding.itemCoinRank.txtIdRank.setText(requireContext().getString(R.string.id_user, dataUserCoin.getId() + ""));
                    binding.itemCoinRank.txtTotalCoin.setText(dataUserCoin.getPoint() + "");
                    return;
                }
                binding.cstNextButton.setVisibility(View.GONE);
            }
        } else {
            binding.cstNextButton.setVisibility(View.GONE);
        }
    }

}
