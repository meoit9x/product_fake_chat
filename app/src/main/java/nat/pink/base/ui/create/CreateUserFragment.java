package nat.pink.base.ui.create;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.bumptech.glide.Glide;

import nat.pink.base.R;
import nat.pink.base.base.BaseFragment;
import nat.pink.base.databinding.FragmentCreateUserBinding;
import nat.pink.base.ui.home.HomeViewModel;
import nat.pink.base.utils.Const;
import nat.pink.base.utils.Utils;

public class CreateUserFragment extends BaseFragment<FragmentCreateUserBinding, HomeViewModel> {
    public static final String TAG = "CreateUserFragment";

    @Override
    protected HomeViewModel getViewModel() {
        return new ViewModelProvider(this).get(HomeViewModel.class);
    }

    @Override
    protected void initView() {
        super.initView();
        binding.llTop.txtTitle.setText(getString(R.string.create_your_contact));
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        binding.swStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.lineView1.setVisibility(View.VISIBLE);
                binding.radios.setVisibility(View.VISIBLE);
            } else {
                binding.lineView1.setVisibility(View.GONE);
                binding.radios.setVisibility(View.GONE);
            }
        });

        binding.extChangeAva.setOnClickListener(v -> {
            Utils.hiddenKeyboard(requireActivity(), binding.edtName);
            Utils.openGallery(requireActivity(), false);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Utils.openGallery(requireActivity(), false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.ALBUM_REQUEST_CODE && data.getData() != null) {
            Glide.with(requireContext()).load(data.getDataString()).into(binding.ivChangeAva);
        }
    }
}