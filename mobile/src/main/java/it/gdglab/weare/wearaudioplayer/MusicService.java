package it.gdglab.weare.wearaudioplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class MusicService extends Service {

	@SuppressWarnings("unused")
	private final String TAG = "MusicService";

	private static final int NOTIFICATION_ID = 1;
	private MediaPlayer mPlayer;
	private int mStartID;

	@Override
	public void onCreate() {
		super.onCreate();

		// Set up the Media Player
		mPlayer = MediaPlayer.create(this, R.raw.badnews);

		if (null != mPlayer) {

			mPlayer.setLooping(false);

			// Stop Service when music has finished playing
			mPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {

					// stop Service if it was started with this ID
					// Otherwise let other start commands proceed
					stopSelf(mStartID);

				}
			});
		}
	}

	
	@Override
	public int onStartCommand(Intent intent, int flags, int startid) {

		if (null != mPlayer) {

			// ID for this start command
			mStartID = startid;

            if (intent.hasExtra("command")){
                String command = intent.getStringExtra("command");
                if (command.equalsIgnoreCase("play")){
                    if (mPlayer.isPlaying()) {


                        mPlayer.pause();

                    } else {

                        // Start playing song
                        mPlayer.start();

                    }
                }
                else if (command.equalsIgnoreCase("stop")){
                    if (mPlayer.isPlaying()) {

                        // Rewind to beginning of song
                        mPlayer.stop();

                    }
                }
            }


		}

		// Don't automatically restart this Service if it is killed
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {

		if (null != mPlayer) {

			mPlayer.stop();
			mPlayer.release();

		}
	}

	// Can't bind to this Service
	@Override
	public IBinder onBind(Intent intent) {

		return null;

	}

}
