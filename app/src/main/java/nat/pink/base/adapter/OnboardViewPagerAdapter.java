package nat.pink.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

import nat.pink.base.databinding.ItemScreenOnboardBinding;
import nat.pink.base.model.ScreenItem;

public class OnboardViewPagerAdapter extends PagerAdapter {
    Context mContext;
    List<ScreenItem> mListScreen;

    public OnboardViewPagerAdapter(Context mContext, List<ScreenItem> mListScreen){
        this.mContext = mContext;
        this.mListScreen = mListScreen;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container,int position){
       // LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ItemScreenOnboardBinding binding = ItemScreenOnboardBinding.inflate((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
     //   View layoutScreen = inflater.inflate(R.layout.layout_screen, null);

        ImageView imgSlide = binding.imgIntro;
        TextView title = binding.tvTitle;
        TextView description  = binding.tvDescription;
        ImageView imgBg = binding.imgBg;
        title.setText(mListScreen.get(position).getTitle());
        description.setText(mListScreen.get(position).getDescription());
        imgSlide.setImageResource(mListScreen.get(position).getScreenImg());
        imgBg.setImageResource(mListScreen.get(position).getBgImg());

        container.addView(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public int getCount(){
        return mListScreen.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o){
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object){
        container.removeView((View) object);
    }
}
