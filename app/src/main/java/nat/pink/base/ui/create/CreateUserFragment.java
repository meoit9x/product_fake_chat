package nat.pink.base.ui.create;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import nat.pink.base.R;
import nat.pink.base.base.BaseFragment;
import nat.pink.base.databinding.FragmentCreateUserBinding;
import nat.pink.base.ui.home.HomeViewModel;

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
            if (isChecked){
                binding.lineView1.setVisibility(View.VISIBLE);
                binding.radios.setVisibility(View.VISIBLE);
            }else{
                binding.lineView1.setVisibility(View.GONE);
                binding.radios.setVisibility(View.GONE);
            }
        });

    }
}