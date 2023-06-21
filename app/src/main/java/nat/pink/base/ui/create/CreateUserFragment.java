package nat.pink.base.ui.create;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;

import nat.pink.base.R;
import nat.pink.base.base.BaseFragment;
import nat.pink.base.databinding.FragmentCreateUserBinding;
import nat.pink.base.dialog.DialogChangeColor;
import nat.pink.base.model.DaoContact;
import nat.pink.base.ui.home.HomeFragment;
import nat.pink.base.ui.home.HomeViewModel;
import nat.pink.base.utils.Const;
import nat.pink.base.utils.ImageUtils;
import nat.pink.base.utils.Utils;

public class CreateUserFragment extends BaseFragment<FragmentCreateUserBinding, HomeViewModel> {
    public static final String TAG = "CreateUserFragment";

    @Override
    protected HomeViewModel getViewModel() {
        return new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
    }

    private DaoContact daoContact;
    private DialogChangeColor dialogChangeColor;
    private boolean isEdit = false;

    public CreateUserFragment(DaoContact daoContact) {
        this.daoContact = daoContact;
        isEdit = true;
    }

    public CreateUserFragment() {
    }

    @Override
    protected void initView() {
        super.initView();
        requireActivity().getWindow().setSoftInputMode(SOFT_INPUT_ADJUST_PAN);
        dialogChangeColor = new DialogChangeColor(requireContext(), v -> {
            daoContact.setColor(v);
            binding.ivContent.setColorFilter(v);
        });
        binding.ivContent.setColorFilter(requireContext().getColor(R.color.color_6366F1));
        setupUI(binding.getRoot());
    }

    @Override
    protected void initData() {
        super.initData();

        binding.llTop.txtTitle.setText(getString(daoContact == null ? R.string.create_your_contact : R.string.edit_your_contact));
        binding.txtDone.setText(daoContact == null ? R.string.create : R.string.edit);

        // init data edit
        if (daoContact != null) {
            ImageUtils.loadImage(requireContext(), binding.ivChangeAva, daoContact.getAvatar());
            binding.edtName.setText(daoContact.getName());
            binding.swStatus.setChecked(daoContact.getOnline() == 1);
            binding.radios.setVisibility(daoContact.getOnline() == 1 ? View.GONE : View.VISIBLE);
            binding.rb5Min.setChecked(daoContact.getOnline() == 2);
            binding.rb30Min.setChecked(daoContact.getOnline() == 3);
            binding.rb1Hour.setChecked(daoContact.getOnline() == 4);
            binding.rb1Day.setChecked(daoContact.getOnline() == 5);
            binding.swFriend.setChecked(daoContact.isIs_friend());
            binding.ivContent.setColorFilter(daoContact.getColor());
            binding.edtEdu.setText(daoContact.getEducation());
            binding.edtWork.setText(daoContact.getWork());
            binding.edtLive.setText(daoContact.getLive());

            return;
        }

        // data create
        daoContact = new DaoContact();
        daoContact.setOnline(1);
        daoContact.setVerified(true);
        daoContact.setIs_friend(true);
        createInterstitialAd(Const.KEY_ADMOB_INTERSTITIAL_TEST);
    }


    @Override
    protected void initEvent() {
        super.initEvent();
        binding.swStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                binding.lineView1.setVisibility(View.VISIBLE);
                binding.radios.setVisibility(View.VISIBLE);
                binding.rb5Min.setChecked(daoContact.getOnline() == 2 || daoContact.getOnline() == 1);
                binding.rb30Min.setChecked(daoContact.getOnline() == 3);
                binding.rb1Hour.setChecked(daoContact.getOnline() == 4);
                binding.rb1Day.setChecked(daoContact.getOnline() == 5);
                daoContact.setOnline(daoContact.getOnline() == 1 ? 2 : daoContact.getOnline());
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
                showInterstitialAd(o -> {
                    requireActivity().runOnUiThread(() -> {
                        if (isEdit)
                            getViewModel().updateContact(requireContext(), daoContact);
                        else
                            getViewModel().insertContact(requireContext(), daoContact);
                        backStackFragment();
                    });
                });
            }
        });
        binding.rb1Day.setOnClickListener(this::onRadioButtonClicked);
        binding.rb1Hour.setOnClickListener(this::onRadioButtonClicked);
        binding.rb5Min.setOnClickListener(this::onRadioButtonClicked);
        binding.rb30Min.setOnClickListener(this::onRadioButtonClicked);
        binding.frColor.setOnClickListener(v -> dialogChangeColor.show());
        binding.llTop.ivBack.setOnClickListener(v -> backStackFragment());

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
        if (requestCode == Const.ALBUM_REQUEST_CODE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
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

    @SuppressLint("ClickableViewAccessibility")
    public void setupUI(View view) {

        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                Utils.hideKeyboard(v);
                return false;
            });
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
}