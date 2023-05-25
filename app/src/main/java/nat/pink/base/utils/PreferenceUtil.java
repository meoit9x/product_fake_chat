package nat.pink.base.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {
    public static final String SETTING_ENGLISH = "SETTING_ENGLISH";
    public static final String SETTING_PREMIUM_MONTHLY = "SETTING_PREMIUM_MONTHLY";
    private static final String MyPREFERENCES = "MyPreferences";
    public static final String OPEN_APP_FIRST_TIME = "open_app_first_time";
    public static final String GO_BACK_COUNT = "back_from_detail_view_count";
    public static final String DISPLAY_ADS_AFTER_ONBOARD = "display_ads_after_onboard";
    public static final String KEY_CURRENT_LANGUAGE = "key_current_language";
    public static final String KEY_CURRENT_TIME = "key_current_time";
    public static final String KEY_CURRENT_TIME_NOTI = "key_current_time_noti";
    public static final String KEY_SHOW_POPUP = "key_show_popup";
    public static final String KEY_COMMING_VIDEO = "key_comming_video";
    public static final String KEY_CALLING_VIDEO = "key_calling_video";
    public static final String KEY_COMMING_VOICE = "key_comming_voice";
    public static final String KEY_CALLING_VOICE = "key_calling_voice";
    public static final String KEY_SETUP_DATA_DEFAULT = "KEY_SETUP_DATA_DEFAULT";

    public static void saveBoolean(Context context, String str, boolean z) {
        SharedPreferences.Editor edit = context.getSharedPreferences(MyPREFERENCES, 0).edit();
        edit.putBoolean(str, z);
        edit.apply();
    }

    public static boolean getBoolean(Context context, String str, boolean z) {
        return context.getSharedPreferences(MyPREFERENCES, 0).getBoolean(str, z);
    }

    public static void saveString(Context context, String str, String str2) {
        SharedPreferences.Editor edit = context.getSharedPreferences(MyPREFERENCES, 0).edit();
        edit.putString(str, str2);
        edit.apply();
    }

    public static void saveLong(Context context, String str, Long l2) {
        SharedPreferences.Editor edit = context.getSharedPreferences(MyPREFERENCES, 0).edit();
        edit.putLong(str, l2);
        edit.apply();
    }

    public static void saveKey(Context context, String key) {
        saveInt(context, key, 1);
    }

    public static void clearEdit(Context context, String key) {
        SharedPreferences.Editor edit = context.getSharedPreferences(MyPREFERENCES, 0).edit();
        edit.remove(key).commit();
    }

    public static long getLong(Context context, String key) {
        return context.getSharedPreferences(MyPREFERENCES, 0).getLong(key, 0);
    }

    public static String getString(Context context, String str, String str2) {
        return context.getSharedPreferences(MyPREFERENCES, 0).getString(str, str2);
    }

    public static void saveInt(Context context, String key, Integer value) {
        SharedPreferences.Editor edit = context.getSharedPreferences(MyPREFERENCES, 0).edit();
        edit.putInt(key, value);
        edit.apply();
    }

    public static Integer getInt(Context context, String key) {
        int value = context.getSharedPreferences(MyPREFERENCES, 0).getInt(key, 0);
        return value;
    }
}
