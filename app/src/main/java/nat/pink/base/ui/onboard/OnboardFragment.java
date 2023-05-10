package nat.pink.base.ui.onboard;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import nat.pink.base.R;
import nat.pink.base.adapter.OnboardViewPagerAdapter;
import nat.pink.base.base.BaseFragment;
import nat.pink.base.databinding.FragmentOnboardBinding;
import nat.pink.base.model.ScreenItem;
import nat.pink.base.ui.home.HomeFragment;

public class OnboardFragment extends BaseFragment<FragmentOnboardBinding, OnboardViewModel> {
    private ViewPager screenPager;
    private OnboardViewPagerAdapter onboardViewPagerAdapter;

    public static final String TAG = "OnboardFragment";

    @Override
    protected OnboardViewModel getViewModel() {
        return new ViewModelProvider(this).get(OnboardViewModel.class);
    }

    @Override
    protected void initView() {
        super.initView();
        if (restorePreData()){
            replaceFragment(new HomeFragment(), HomeFragment.TAG);
        }

        //Data
        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Realistic Fake Conversations app", "Create fake chat, fake call, fake video call and fake notification completely like messenger.", R.drawable.ob_1,R.drawable.ob_1_bg));
        mList.add(new ScreenItem("Control the both sides of the conversation", "Easily set the time to send and receive messages.", R.drawable.ob_2,R.drawable.ob_2_bg));
        mList.add(new ScreenItem("Prank your friend or your partner", "Create celebrities to text with them like the real thing. Use it to troll your friends.", R.drawable.ob_3,R.drawable.ob_3_bg));
        //Setup viewPager
        screenPager = binding.screenViewpager;
        onboardViewPagerAdapter = new OnboardViewPagerAdapter(getContext(), mList);
        screenPager.setAdapter(onboardViewPagerAdapter);

        //Setup tab indicator
        binding.tabIndicator.setupWithViewPager(screenPager);

        //Button Next
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screenPager.setCurrentItem(screenPager.getCurrentItem()+1, true);
            }
        });

        binding.tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition()==mList.size()-1){
                    loadLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

       //Button Get Started
            binding.btnGetStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new HomeFragment(), HomeFragment.TAG);
                savePrefsData();
                //finish();
            }
        });

        binding.skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new HomeFragment(), HomeFragment.TAG);
                savePrefsData();
            }
        });
    }

    private boolean restorePreData(){
        SharedPreferences preferences = getContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        Boolean isIntroActivityOpenedBefore = preferences.getBoolean("isIntroOpened", false);
        return isIntroActivityOpenedBefore;
    }

    private void savePrefsData(){
        SharedPreferences preferences = getContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isIntroOpened", true);
        editor.apply();
    }

    private void loadLastScreen(){
       binding.btnNext.setVisibility(View.INVISIBLE);
       binding.btnGetStart.setVisibility(View.VISIBLE);
    }
}
