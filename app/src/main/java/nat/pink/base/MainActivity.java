package nat.pink.base;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import nat.pink.base.databinding.ActivityMainBinding;
import nat.pink.base.ui.home.HomeFragment;
import nat.pink.base.ui.splah.SplashFragment;

import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<String> fragmentStates = new ArrayList<>();
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fragmentManager = getSupportFragmentManager();

        //disable darkmode
        // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //use full taskbar
/*        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);*/

        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        replaceFragment(new SplashFragment(), SplashFragment.TAG);
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
        if (fragmentStates.size() == 0 || fragmentStates.get(fragmentStates.size() - 1).equals(HomeFragment.TAG) || fragmentStates.get(fragmentStates.size() - 1).equals(SplashFragment.TAG)) {
            finish();
            return;
        }
        getSupportFragmentManager().popBackStack(fragmentStates.get(fragmentStates.size() - 1), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentStates.remove(fragmentStates.size() - 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment item : getSupportFragmentManager().getFragments())
            item.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (Fragment item : getSupportFragmentManager().getFragments())
            item.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    public void setLoadingAdsView(Boolean visible) {
        Log.d(TAG, "LoadingAdsView: " + visible);
        binding.loadingAdsLayout.loadingAdsLayout.bringToFront();
        binding.loadingAdsLayout.loadingAdsLayout.setVisibility(View.VISIBLE == binding.loadingAdsLayout.loadingAdsLayout.getVisibility() ? View.GONE : View.VISIBLE);
        binding.frContent.setVisibility(View.VISIBLE == binding.frContent.getVisibility() ? View.GONE : View.VISIBLE);
    }
}