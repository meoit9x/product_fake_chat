package nat.pink.base.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import nat.pink.base.R;

public class ImageUtils {

    public static void loadImageBitmap(ImageView imageView, Bitmap bitmap) {
        Glide.with(imageView).load(bitmap).placeholder(R.drawable.ic_user_default).into(imageView);

    }

    public static void loadImage(Context context, ImageView imageView, String path) {
        if (path == null) {
            imageView.setImageResource(R.drawable.ic_user_default);
            return;
        }
        if (path.contains("R.drawable")) {
            imageView.setImageResource(Utils.convertStringToDrawable(context, path));
        } else {
            loadImage(imageView, path);
        }
    }

    public static void loadImage(ImageView imageView, String path) {
        Glide.with(imageView)
                .load(path)
                .placeholder(R.drawable.ic_user_default)
                .into(imageView);
    }

    public static void loadImageDefault(ImageView imageView, String path) {
        Glide.with(imageView)
                .load(path)
                .placeholder(R.drawable.bg_no_image)
                .into(imageView);
    }
}
