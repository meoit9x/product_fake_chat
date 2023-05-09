package nat.pink.base;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import nat.pink.base.databinding.ActivityMainBinding;
import nat.pink.base.ui.home.HomeFragment;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<String> fragmentStates = new ArrayList<>();
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fragmentManager = getSupportFragmentManager();

        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        addFragment(new HomeFragment(), HomeFragment.TAG);
    }

    public void replaceFragment(Fragment fragment, String tag) {
        fragmentManager.beginTransaction()
                .replace(R.id.content, fragment, tag)
                .addToBackStack(tag)
                .commit();
        if (!fragmentStates.contains(tag))
            fragmentStates.add(tag);
    }

    public void addFragment(Fragment fragment, String tag) {
        fragmentManager.beginTransaction()
                .add(R.id.content, fragment, tag)
                .addToBackStack(tag)
                .commit();
        if (!fragmentStates.contains(tag))
            fragmentStates.add(tag);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (fragmentStates.size() > 1) {
            getSupportFragmentManager().popBackStack(fragmentStates.get(fragmentStates.size() - 1), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentStates.remove(fragmentStates.size() - 1);

            return;
        }
        finish();
    }
}