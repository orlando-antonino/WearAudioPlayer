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
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.HashMap;

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

		final ImageButton playButton = (ImageButton) findViewById(R.id.btnPlay);
        playButton.setOnClickListener(new OnClickListener() {
			public void onClick(View src) {
				
				// Start the MusicService using the Intent

                musicServiceIntent.putExtra("command", "play");
				startService(musicServiceIntent);

			}
		});

        final ImageButton nextButton = (ImageButton) findViewById(R.id.btnNext);
        nextButton.setOnClickListener(new OnClickListener() {
            public void onClick(View src) {

                // Start the MusicService using the Intent

                musicServiceIntent.putExtra("command", "next");
                startService(musicServiceIntent);

            }
        });
        final ImageButton prevButton = (ImageButton) findViewById(R.id.btnPrevious);
        prevButton.setOnClickListener(new OnClickListener() {
            public void onClick(View src) {

                // Start the MusicService using the Intent

                musicServiceIntent.putExtra("command", "prev");
                startService(musicServiceIntent);

            }
        });


        musicServiceIntent.putExtra("command", "list");
        startService(musicServiceIntent);

        this.createNotify();
	}

    public void createNotify(){
        int notificationId = 001;

        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(getApplicationContext());
        notifBuilder.setSmallIcon(R.drawable.ic_launcher);
        notifBuilder.setContentTitle("WearMusicPlayer");
        notifBuilder    .setContentText("It is time to listen");
        notifBuilder    .addAction(android.R.drawable.ic_media_play, "Play/Pause", createIntent("play", notificationId));
        notifBuilder    .addAction(android.R.drawable.ic_media_previous, "Prev", createIntent("prev", notificationId));
        notifBuilder    .addAction(android.R.drawable.ic_media_next, "Next", createIntent("next", notificationId));


        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(getApplicationContext());

        notificationManager.notify(notificationId, notifBuilder.build());

    }

    public PendingIntent createIntent(String extra, int notifId){

        Intent intent = null;

        switch (extra) {
            case "play":
                intent =  new Intent("play")
                        .setClass(getBaseContext(), NotificationReceiver.class);
                break;

            case "stop":
                intent =  new Intent("stop")
                        .setClass(getBaseContext(), NotificationReceiver.class);
                break;
            case "next":
                intent =  new Intent("next")
                        .setClass(getBaseContext(), NotificationReceiver.class);
                break;
            case "prev":
                intent =  new Intent("prev")
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
