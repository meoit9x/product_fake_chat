package nat.pink.base;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nat.pink.base.base.BaseFragment;
import nat.pink.base.databinding.FragmentCreateUserBinding;
import nat.pink.base.ui.home.HomeViewModel;

public class CreateUserFragment extends BaseFragment<FragmentCreateUserBinding, HomeViewModel> {
    public static final String TAG = "CreateUserFragment";

    @Override
    protected HomeViewModel getViewModel() {
        return new ViewModelProvider(this).get(HomeViewModel.class);
    }

}