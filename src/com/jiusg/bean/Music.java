package com.jiusg.bean;

import java.util.Timer;
import java.util.TimerTask;

import com.jiusg.crazybird.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;

public class Music {

	private MediaPlayer player = null;
	private MusicHandler handler = null;
	private Timer musictimer = null;
	private Context context = null;

	public Music(Context context) {
		this.context = context;
		handler = new MusicHandler();
	}

	public void startMusic() {
		TimerTask musictask = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg = handler.obtainMessage();
				handler.sendMessage(msg);
			}

		};
		musictimer = new Timer(true);
		musictimer.schedule(musictask, 0, 36000);
	}

	public void stopMusic() {
		if (player != null)
			player.stop();
		if (musictimer != null)
			musictimer.cancel();
		musictimer = null;
	}

	@SuppressLint("HandlerLeak")
	class MusicHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			player = MediaPlayer.create(context, R.raw.bird);
			player.start();

		}

	}

}
