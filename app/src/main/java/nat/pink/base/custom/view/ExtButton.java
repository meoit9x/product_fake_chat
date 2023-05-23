package nat.pink.base.custom.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import nat.pink.base.R;

public class ExtButton extends LinearLayoutCompat {

    private LayoutInflater mInflater;
    private View v;

    public ExtButton(Context context) {
        super(context);
        initView(context);
    }

    public ExtButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private boolean selected = false;
    private String stringText = "";

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
        ImageView view = v.findViewById(R.id.ic_left);
        ExtTextView text = v.findViewById(R.id.txt_content);
        LinearLayoutCompat layout = v.findViewById(R.id.ll_content);
        view.setImageResource(selected ? R.drawable.ic_rdb_check : R.drawable.ic_button_unselect);
        text.setText(stringText);
        text.setTextColor(getContext().getColor(selected ? R.color.white : R.color.color_7C76CE));
        layout.setBackgroundResource(selected ? R.drawable.bg_button_select : R.drawable.bg_button_unselect);
    }

    public void setStringText(String stringText) {
        this.stringText = stringText;
    }

    private void initView(Context context) {
        LayoutParams layoutParams = new LinearLayoutCompat.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f);
        setLayoutParams(layoutParams);
        mInflater = LayoutInflater.from(context);
        v = mInflater.inflate(R.layout.ext_button, this, true);

    }
}
