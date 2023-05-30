package nat.pink.base.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class HangupReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, CallingService.class);
        context.stopService(serviceIntent);
    }
}