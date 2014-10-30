package it.gdglab.weare.wearaudioplayer;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MusicServiceClient extends Activity {



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        takeActionOnNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        takeActionOnNewIntent(getIntent());

    }

    public void takeActionOnNewIntent(Intent intent){
        if (getIntent().hasExtra("command")){
            String action = getIntent().getAction();
            String commandValue = getIntent().getStringExtra("command");
            final Intent musicServiceIntent = new Intent(getApplicationContext(),
                    MusicService.class);
            musicServiceIntent.putExtra("command", commandValue);
            startService(musicServiceIntent);
        }
    }
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);


		// Intent used for starting the MusicService
		final Intent musicServiceIntent = new Intent(getApplicationContext(),
				MusicService.class);

        //musicServiceIntent.putExtra("NOT",notif);

		final Button startButton = (Button) findViewById(R.id.start_button);
		startButton.setOnClickListener(new OnClickListener() {
			public void onClick(View src) {
				
				// Start the MusicService using the Intent

                musicServiceIntent.putExtra("command", "play");
				startService(musicServiceIntent);

			}
		});

		final Button stopButton = (Button) findViewById(R.id.stop_button);
		stopButton.setOnClickListener(new OnClickListener() {
			public void onClick(View src) {

				// Stop the MusicService using the Intent
                musicServiceIntent.putExtra("command", "stop");
				stopService(musicServiceIntent);

			}
		});

        this.createNotify();
	}

    public void createNotify(){
        int notificationId = 001;

        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(getApplicationContext());
        notifBuilder.setSmallIcon(R.drawable.ic_launcher);
        notifBuilder.setContentTitle("WearMusicPlayer");
        notifBuilder    .setContentText("It is time to listen");
        notifBuilder    .addAction(R.drawable.ic_start, "Start", createIntent("start", notificationId));
        notifBuilder    .addAction(R.drawable.ic_stop, "Stop", createIntent("stop", notificationId));

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(getApplicationContext());

        notificationManager.notify(notificationId, notifBuilder.build());

    }

    public PendingIntent createIntent(String extra, int notifId){

        Intent intent = null;

        switch (extra) {
            case "start":
                intent =  new Intent("play")
                        .setClass(getBaseContext(), NotificationReceiver.class);
                break;

            case "stop":
                intent =  new Intent("stop")
                        .setClass(getBaseContext(), NotificationReceiver.class);
                break;



            default:
                break;

        }
        PendingIntent pending =
                PendingIntent.getBroadcast(getBaseContext(), notifId, intent, 0);

        return pending;


    }


}
