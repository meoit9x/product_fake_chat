package nat.pink.base.base;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivityForFragment extends AppCompatActivity {

    private View viewContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewContainer = getLayoutResource();
        setContentView(viewContainer);
        initView();
        initData();
        initEvent();
        initAds();
    }

    protected View getViewPadding() {
        return null;
    }

    protected abstract View getLayoutResource();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initEvent();

    protected abstract void initAds();
}
