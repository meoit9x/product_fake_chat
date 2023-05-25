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
import android.widget.RadioButton;

import com.bumptech.glide.Glide;

import nat.pink.base.R;
import nat.pink.base.base.BaseFragment;
import nat.pink.base.databinding.FragmentCreateUserBinding;
import nat.pink.base.dialog.DialogChangeColor;
import nat.pink.base.model.DaoContact;
import nat.pink.base.ui.home.HomeViewModel;
import nat.pink.base.utils.Const;
import nat.pink.base.utils.Utils;

public class CreateUserFragment extends BaseFragment<FragmentCreateUserBinding, HomeViewModel> {
    public static final String TAG = "CreateUserFragment";

    @Override
    protected HomeViewModel getViewModel() {
        return new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
    }

    private DaoContact daoContact;
    private DialogChangeColor dialogChangeColor;

    @Override
    protected void initView() {
        super.initView();
        binding.llTop.txtTitle.setText(getString(R.string.create_your_contact));
        dialogChangeColor = new DialogChangeColor(requireContext(), v -> {
            daoContact.setColor(v);
            binding.ivContent.setColorFilter(v);
        });
        binding.ivContent.setColorFilter(requireContext().getColor(R.color.color_6366F1));
    }

    @Override
    protected void initData() {
        super.initData();
        daoContact = new DaoContact();
        daoContact.setOnline(1);
        daoContact.setVerified(true);
        daoContact.setIs_friend(true);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        binding.swStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                binding.lineView1.setVisibility(View.VISIBLE);
                binding.radios.setVisibility(View.VISIBLE);
            } else {
                binding.lineView1.setVisibility(View.GONE);
                binding.radios.setVisibility(View.GONE);
                daoContact.setOnline(1);
            }
        });

        binding.extChangeAva.setOnClickListener(v -> {
            Utils.hiddenKeyboard(requireActivity(), binding.edtName);
            Utils.openGallery(requireActivity(), false);
        });

        binding.rlDone.setOnClickListener(v -> {
            if (setupDone()) {
                getViewModel().insertContact(requireContext(), daoContact);
                backStackFragment();
            }
        });
        binding.rb1Day.setOnClickListener(this::onRadioButtonClicked);
        binding.rb1Hour.setOnClickListener(this::onRadioButtonClicked);
        binding.rb5Min.setOnClickListener(this::onRadioButtonClicked);
        binding.rb30Min.setOnClickListener(this::onRadioButtonClicked);
        binding.frColor.setOnClickListener(v -> dialogChangeColor.show());

    }

    private boolean setupDone() {
        if ("".equals(binding.edtName.getText().toString())) {
            binding.edtError.setVisibility(View.VISIBLE);
            return false;
        }
        daoContact.setName(binding.edtName.getText().toString());
        daoContact.setVerified(binding.swVerify.isChecked());
        daoContact.setIs_friend(binding.swFriend.isChecked());
        daoContact.setEducation(binding.edtEdu.getText().toString());
        daoContact.setWork(binding.edtWork.getText().toString());
        daoContact.setLive(binding.edtLive.getText().toString());
        return true;
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
            daoContact.setAvatar(data.getDataString());
        }
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.rb_5_min:
                if (checked)
                    daoContact.setOnline(2);
                break;
            case R.id.rb_30_min:
                if (checked)
                    daoContact.setOnline(3);
                break;
            case R.id.rb_1_hour:
                if (checked)
                    daoContact.setOnline(4);
                break;
            case R.id.rb_1_day:
                if (checked)
                    daoContact.setOnline(5);
                break;
        }
    }
}