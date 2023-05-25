package nat.pink.base.ui.incoming;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import java.io.Serializable;

import nat.pink.base.base.BaseFragment;
import nat.pink.base.databinding.FragmentCallScreenBinding;
import nat.pink.base.model.DaoContact;
import nat.pink.base.utils.Const;
import nat.pink.base.utils.ImageUtils;
import nat.pink.base.utils.Utils;

public class CallScreenFragment extends BaseFragment<FragmentCallScreenBinding,CallScreenViewModel> {
    public static final String TAG = "CallScreenFragment";
    DaoContact user;
    public CallScreenFragment(){

    }
    @Override
    protected CallScreenViewModel getViewModel() {
        return new ViewModelProvider(this).get(CallScreenViewModel.class);
    }

    @Override
    protected void initData() {
        super.initData();
        user  = new DaoContact(2, "Cristiano Ronaldo", 1, true,true,1, "harvard","new castle","", Uri.parse("android.resource://" + getContext().getPackageName() + "/drawable/ronaldo").toString());


        Bundle bundle = getArguments();
        if (bundle != null) {
            Serializable serializable =  bundle.getSerializable(Const.PUT_EXTRAL_OBJECT_CALL);
            if (serializable instanceof DaoContact) {
              //  objectCalling = (ObjectCalling) serializable;
            }
           // showIconVideo = getIntent().getBooleanExtra("show_icon_video", false);
        }
    }

    @Override
    protected void initView() {
        super.initView();
        Utils.showFullScreen(getActivity());


        binding.txtName.setText(user.getName());
        ImageUtils.loadImage(binding.ivCall,user.getAvatar());
        ImageUtils.loadImage(binding.ivContent,user.getAvatar());

    }

    @Override
    protected void initEvent() {
        super.initEvent();
        binding.ivRefuse.setOnClickListener(v -> {
            backStackFragment();
        });
        binding.ivAnswer.setOnClickListener(v -> {
            binding.ivReply.setVisibility(View.GONE);
            binding.txtAnswerContent.setVisibility(View.GONE);
            binding.txtRefuse.setVisibility(View.GONE);
            binding.ivRefuse.setVisibility(View.GONE);
            binding.txtAnswer.setVisibility(View.GONE);
            binding.ivAnswer.setVisibility(View.GONE);

            binding.footerButton.setVisibility(View.VISIBLE);
            binding.ivBack.setVisibility(View.VISIBLE);
            binding.swCamera.setVisibility(View.VISIBLE);
        });
    }
}