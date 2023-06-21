package nat.pink.base.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import nat.pink.base.utils.MyContextWrapper;
import nat.pink.base.utils.PreferenceUtil;

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
    }

    protected View getViewPadding() {
        return null;
    }

    protected abstract View getLayoutResource();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initEvent();

    @Override
    protected void attachBaseContext(Context newBase) {
        String english = PreferenceUtil.getString(newBase, PreferenceUtil.KEY_CURRENT_LANGUAGE, "");
        if(!TextUtils.isEmpty(english)) {
            super.attachBaseContext(MyContextWrapper.wrap(newBase, english));
        } else {
            super.attachBaseContext(newBase);
        }
    }
}
