package com.jiusg.bean;

import java.util.Timer;
import java.util.TimerTask;

import com.jiusg.crazybird.BirdView;
import com.jiusg.crazybird.MainActivity;
import com.jiusg.crazybird.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

public class Ghoast {

	public int x = 0;
	public int y = 0;
	public Bitmap bitmap = null;

	private Bird bird = null;
	private GhoastHandler handler = null;
	private int state = 0;
	private Context context = null;
	private BirdView view = null;

	public Ghoast(Context context, Bird bird, BirdView view) {
		this.bird = bird;
		this.context = context;
		this.view = view;
		handler = new GhoastHandler();
		bitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.ghost1);
		
		reSet();

		TimerTask task = new TimerTask() {

			@Override
			public void run() {

				Message msg = handler.obtainMessage();
				handler.sendMessage(msg);

			}
		};
		Timer timer = new Timer(true);
		timer.schedule(task, 0, 1000 / 2);
	}

	public void count() {

		// ÅÐ¶ÏÓëÐ¡ÄñÅö×²
		if ((bird.x + bird.bitmap.getWidth()) > x
				&& bird.x < (x + bitmap.getWidth())) {
			if (bird.y < y + bitmap.getHeight()
					&& (bird.y + bird.bitmap.getHeight()) > y) {
				view.states = view.GAMEOVER;
				return;
			}
		}

		x -= (int) BirdView.speedP * 2;
		if (x < -MainActivity.width) {
			reSet();
		}

	}

	/**
	 * ÖØÖÃÓÄÁéÊôÐÔ
	 */
	public void reSet() {

		x = MainActivity.width + (int) (MainActivity.width * Math.random());
		y = (int) (Column.height * Math.random());
	}

	@SuppressLint("HandlerLeak")
	class GhoastHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			state++;
			switch (state % 2) {
			case 0:
				bitmap = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.ghost1);
				break;
			case 1:
				bitmap = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.ghost2);
				break;
			default:
				break;
			}
		}

	}
}
