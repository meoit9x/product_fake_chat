package nat.pink.base.utils;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static nat.pink.base.utils.Const.MY_PERMISSIONS_REQUEST_STORAGE;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.google.gson.Gson;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import nat.pink.base.R;
import nat.pink.base.dialog.DialogChangeTime;
import nat.pink.base.service.AlarmReceiver;

public class Utils {
    public  static String CHANNEL_ID = "xab_002_ntr";

    public static int getHeightScreen(Activity context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int getWithScreen(Activity context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static String getStringFromIndex(Context context, int checked) {
        if (checked == Const.CHECK_5_M)
            return context.getString(R.string.online_5_minutes_ago);
        if (checked == Const.CHECK_30_M)
            return context.getString(R.string.online_30_minutes_ago);
        if (checked == Const.CHECK_1_H)
            return context.getString(R.string.online_1_hour_ago);
        if (checked == Const.CHECK_1_D)
            return context.getString(R.string.online_1_day_ago);
        return context.getString(R.string.online);
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void hiddenKeyboard(Activity activity, View view) {
        if (activity == null)
            return;
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboard(View view) {
        if (view == null) {
            return;
        }
        try {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (!imm.isActive()) {
                return;
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getStringTimeDelay(Context context, DialogChangeTime.CHANGE_TYPE changeType) {

        if (changeType == DialogChangeTime.CHANGE_TYPE.NOW)
            return context.getString(R.string.get_a_call_now);
        if (changeType == DialogChangeTime.CHANGE_TYPE.FIFTEEN_SECONDS)
            return context.getString(R.string.fifteen_seconds_later);
        if (changeType == DialogChangeTime.CHANGE_TYPE.FIVE_SECONDS)
            return context.getString(R.string.five_seconds_later);
        if (changeType == DialogChangeTime.CHANGE_TYPE.TEN_SECONDS)
            return context.getString(R.string.ten_seconds_later);
        if (changeType == DialogChangeTime.CHANGE_TYPE.TWENTY_SECONDS)
            return context.getString(R.string.twenty_seconds_later);
        return context.getString(R.string.get_a_call_now);
    }

    public static int getIntTimeDelay(Context context, DialogChangeTime.CHANGE_TYPE changeType) {

        if (changeType == DialogChangeTime.CHANGE_TYPE.NOW)
            return 1;
        if (changeType == DialogChangeTime.CHANGE_TYPE.FIFTEEN_SECONDS)
            return 7;
        if (changeType == DialogChangeTime.CHANGE_TYPE.FIVE_SECONDS)
            return 2;
        if (changeType == DialogChangeTime.CHANGE_TYPE.TEN_SECONDS)
            return 3;
        if (changeType == DialogChangeTime.CHANGE_TYPE.TWENTY_SECONDS)
            return 8;
        return 0;
    }

    public static long getTimeFromKey(Context context, int item) {
        switch (item) {
            case Const.KEY_TIME_NOW:
                return 2000;
            case Const.KEY_TIME_5_S:
                return 5000;
            case Const.KEY_TIME_10_S:
                return 10000;
            case Const.KEY_TIME_15_S:
                return 15000;
            case Const.KEY_TIME_20_S:
                return 20000;
            case Const.KEY_TIME_30_S:
                return 30000;
            case Const.KEY_TIME_5_M:
                return 300000;
            case Const.KEY_TIME_1_M:
                return 60000;
        }
        return 0;
    }


    public static String getRealPathFromURI(Activity activity, Uri contentURI) {
        String result = "";

        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


    public static void showFullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        hideSystemUI(activity);
    }

    public static String getPath() {
        String folderPath = "";
        if (Build.VERSION.SDK_INT >= 28) {
            folderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/FakeMessenger";
        } else {
            folderPath = Environment.getExternalStorageDirectory().getPath() + "/FakeMessenger";
        }
        File folder = new File(folderPath);
        if (!folder.exists()) {
            File wallPaper = new File(folderPath);
            wallPaper.mkdir();
        }
        return folderPath;
    }

    public static void hideSystemUI(Activity activity) {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public static void startAlarmService(Activity activity, long time, String action, Serializable objectIncoming) {
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(activity, AlarmReceiver.class);
        intent.putExtra(Const.PUT_EXTRAL_OBJECT_CALL, objectIncoming);
        Gson gson = new Gson();
        String json = gson.toJson(objectIncoming);
        intent.setAction(action);
        intent.setType(json);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, 0, intent, PendingIntent.FLAG_IMMUTABLE | FLAG_UPDATE_CURRENT);

        long l = System.currentTimeMillis() + time;
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, l, pendingIntent);

    }

    public static void clearFlags(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            activity.setShowWhenLocked(false);
            activity.setTurnScreenOn(false);
        }
        activity.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

    }

    public static void overLockScreen(Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            activity.setShowWhenLocked(true);
            activity.setTurnScreenOn(true);
            KeyguardManager keyguardManager =
                    (KeyguardManager) activity.getSystemService(Context.KEYGUARD_SERVICE);
            keyguardManager.requestDismissKeyguard(activity, null);
        }
        activity.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    public static void askPermissionStorage(Activity context, Callable<Void> callable) throws Exception {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_STORAGE);
        } else {
            callable.call();
        }
    }

    public static void requestGetGallery(Activity activity) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        activity.startActivityForResult(galleryIntent, Const.PICK_FROM_GALLERY);
    }

    public static String convertTimeStampToTimeString(long time) {
        if (time < 0) {
            return "";
        }
        String result = "";
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);

        Calendar currentTime = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");
        String yymmdd = dateFormat.format(cal.getTime());
        String currentYYmmdd = dateFormat.format(currentTime.getTime());

        if (!currentYYmmdd.equals(yymmdd)) {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM");
            result = df.format(cal.getTime());
        } else {
            SimpleDateFormat df = new SimpleDateFormat("hh:mm");
            result = df.format(cal.getTime());
        }

        return result;
    }

    public static String formatDuration(long duration, boolean isLong) {
        int h = (int) (duration / 3600);
        int m = (int) (duration / 60 % 60);
        int s = (int) (duration % 60);
        if (h == 0) {
            if (isLong) {
                return String.format(Locale.US, "%02d:%02d", m, s);
            } else {
                return String.format(Locale.US, "%d:%02d", m, s);
            }
        } else {
            return String.format(Locale.US, "%d:%02d:%02d", h, m, s);
        }
    }

    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showDialogRemoveAds(Activity activity, Consumer consumerBuy, Consumer showAds) {
//        new DialogRemoveAds(activity, R.style.MaterialDialogSheet, aBoolean -> {
//            if (aBoolean) {
//                ((MainActivity) activity).addChildFragment(new InAppFragment(o -> consumerBuy.accept(new Object())), InAppFragment.class.getSimpleName());
//            } else {
//                showAds.accept(new Object());
//            }
//        }).show();
    }

    public static void openGallery(Activity activity, boolean isVideo) {
        if (!checkPermission(activity.getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE)) {
            activity.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Const.ALBUM_REQUEST_CODE);
            return;
        }
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (isVideo)
            i.setType("video/*");
        else
            i.setType("image/*");
        activity.startActivityForResult(i, isVideo ? Const.ALBUM_REQUEST_ONLY_VIDEO : Const.ALBUM_REQUEST_CODE);
    }

    public static Bitmap getBitmapFromVideo(Context context, Uri uri) {
        String picturePath = UriUtils.getPathFromUri(context, uri);
        if (picturePath == null)
            return null;
        return ThumbnailUtils.createVideoThumbnail(picturePath, MediaStore.Video.Thumbnails.MICRO_KIND);
    }
    public static int convertStringToDrawable(Context context, String uri){
        String s = uri.replace("R.drawable.", "");
        return context.getResources().getIdentifier(s, "drawable", context.getPackageName());
    }


    public static boolean checkPermission(Context context,String permission) {
        if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static void createNotificationChannel(Context context, String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                    .build();

            String name = "Incoming Calls";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            //channel.setSound(Uri.parse("android.resource://" +context.getPackageName() + "/" + R.raw.ring_stone), audioAttributes);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
