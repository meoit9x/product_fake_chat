package nat.pink.base.custom.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import nat.pink.base.databinding.ItemFakeUserBinding;
import nat.pink.base.databinding.ItemFakeUserContactBinding;


public class ItemFakeUserContact extends LinearLayout {
    protected ItemFakeUserContactBinding binding;

    public ItemFakeUserContact(Context context) {
        super(context);
        initViews();
    }

    public ItemFakeUserContact(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public ItemFakeUserContact(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    public void initViews() {
        binding = ItemFakeUserContactBinding.inflate(LayoutInflater.from(getContext()), this, true);
    }

    public void setAva(Drawable icon) {
        binding.avatar.setImageDrawable(icon);
    }

    public void setText(String text) {
        binding.tvName.setText(text);
    }

    public static ItemFakeUserContact create(Context context, String text, @DrawableRes int icon) {
        return create(context, text, context.getResources().getDrawable(icon));
    }

    public static ItemFakeUserContact create(Context context, String text, Drawable icon) {
        ItemFakeUserContact view = new ItemFakeUserContact(context);
        view.setText(text);
        view.setAva(icon);
        return view;
    }

    public void onClick(OnClickListener listener) {
        binding.getRoot().setOnClickListener(listener);
    }

}
