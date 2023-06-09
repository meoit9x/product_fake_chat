package nat.pink.base.service;

import static nat.pink.base.utils.Utils.CHANNEL_ID;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;

import nat.pink.base.ui.MainActivity;
import nat.pink.base.R;
import nat.pink.base.model.ObjectCalling;
import nat.pink.base.ui.video.child.VideoCallActivity;
import nat.pink.base.utils.Const;
import nat.pink.base.utils.Utils;

public class CallingService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showIncomingCallPopup(intent);
        return START_STICKY;
    }

    private void showIncomingCallPopup(Intent intent) {

        Intent hangupIntent = new Intent(getApplicationContext(), HangupReceiver.class);
        PendingIntent hangupPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, hangupIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent incomingCallIntent = new Intent(getApplicationContext(), MainActivity.class);
        incomingCallIntent.setAction(intent.getAction());
        incomingCallIntent.putExtra(Const.ACTION_FORWARD_SCREEN, true);
        incomingCallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        incomingCallIntent.setType(intent.getType());
        incomingCallIntent.putExtra(Const.PUT_EXTRAL_OBJECT_CALL, intent.getSerializableExtra(Const.PUT_EXTRAL_OBJECT_CALL));
        PendingIntent incomingCallPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, incomingCallIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent answerIntent = new Intent(getApplicationContext(), VideoCallActivity.class);
        answerIntent.setAction(intent.getAction());
        answerIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        answerIntent.setType(intent.getType());
        answerIntent.putExtra(Const.PUT_EXTRAL_OBJECT_CALL, intent.getSerializableExtra(Const.PUT_EXTRAL_OBJECT_CALL));
        if (intent.getAction().equals(Const.ACTION_CALL_VOICE) || (intent.getAction().equals(Const.ACTION_COMMING_VOICE))) {
            answerIntent.putExtra("show_icon_video", true);
        }
        answerIntent.putExtra(Const.ACTION_FORWARD_SCREEN, true);
        PendingIntent answerPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, answerIntent, PendingIntent.FLAG_IMMUTABLE);

        //int view noti
        Gson gson = new Gson();
        ObjectCalling objectCalling = gson.fromJson(intent.getType(), ObjectCalling.class);

        RemoteViews customView = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            int nightModeFlags = getApplicationContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags) {
                case Configuration.UI_MODE_NIGHT_YES:
                    customView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.layout_call_noti);
                    customView.setTextColor(R.id.tvName, Color.WHITE);
                    customView.setTextColor(R.id.tvIncommingCall, Color.WHITE);
                    customView.setInt(R.id.container, "setBackgroundColor", Color.TRANSPARENT);
                    break;
                default:
                    customView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.layout_call_noti);
                    customView.setInt(R.id.container, "setBackgroundColor", Color.TRANSPARENT);
                    break;
            }

        } else {
            customView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.layout_call_noti_old);
        }


        customView.setTextViewText(R.id.tvName, objectCalling.getName());
        if (objectCalling.getPathImage().contains("R.drawable")) {
            customView.setImageViewResource(R.id.iv_avatar, Utils.convertStringToDrawable(this, objectCalling.getPathImage()));
        } else {
            customView.setImageViewUri(R.id.iv_avatar, Uri.parse(objectCalling.getPathImage()));
        }

        customView.setOnClickPendingIntent(R.id.btnAcceptCall, answerPendingIntent);
        customView.setOnClickPendingIntent(R.id.btnRejectCall, hangupPendingIntent);

        Utils.createNotificationChannel(getApplicationContext(), CHANNEL_ID);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_splash)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContent(customView)
                .setFullScreenIntent(incomingCallPendingIntent, true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVibrate(new long[]{0, 500, 1000})
                .setChannelId(CHANNEL_ID)
                //   .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ring_stone))
                .setAutoCancel(true);
        startForeground(1024, notificationBuilder.build());
    }


}
