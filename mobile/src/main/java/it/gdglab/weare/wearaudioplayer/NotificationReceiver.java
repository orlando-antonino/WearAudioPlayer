package it.gdglab.weare.wearaudioplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        // Intent used for starting the MusicService
        final Intent musicServiceIntent = new Intent(context,
                MusicService.class);
        musicServiceIntent.putExtra("command", action);
        context.startService(musicServiceIntent);

    }

}
