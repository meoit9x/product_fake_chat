package nat.pink.base.custom.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import nat.pink.base.R;

public class ExtButton extends LinearLayout {

    public ExtButton(Context context) {
        super(context);
        initView(context);
    }

    private boolean selected = false;
    private String stringText = "";

    private void initView(Context context) {
        View v = inflate(context, R.layout.ext_button, this);
        addView(v);
        ImageView view = v.findViewById(R.id.ic_left);
        ExtTextView text = v.findViewById(R.id.txt_content);
        LinearLayout layout = v.findViewById(R.id.ll_content);
        view.setImageResource(selected ? R.drawable.ic_rdb_check : R.drawable.ic_button_unselect);
        text.setText(stringText);
        layout.setBackgroundResource(selected ? R.drawable.bg_button_select : R.drawable.bg_button_unselect);
    }


}
