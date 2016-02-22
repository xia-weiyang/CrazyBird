package com.jiusg.bean;

import java.util.Timer;
import java.util.TimerTask;

import com.jiusg.crazybird.MainActivity;
import com.jiusg.crazybird.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

public class Bird {

	public Bitmap bitmap = null;
	public int x = 0;
	public int y = 0;
	public float degree = 0f;
	private float g = 0;
	private float time = 0;
	private float v0 = 0;
	private float speed = 0;
	private int state = 1;
	private Context context = null;

	public Bird(Context context) {
		this.context = context;
		state = 1;
		g = 4;
		time = 0.25f;
		v0 = 20;
		speed = v0;
		degree = 0f;
		x = MainActivity.width / 3;
		y = MainActivity.height / 3;
		bitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.bird0);

		final BirdHandler handler = new BirdHandler();

		TimerTask task = new TimerTask() {

			@Override
			public void run() {

				Message msg = handler.obtainMessage();
				handler.sendMessage(msg);

			}
		};
		Timer timer = new Timer(true);
		timer.schedule(task, 0, 1000 / 4);
	}

	public void count() {
		float v = speed;
		float s = v * time - g * time * time;
		y = (int) (y - s);
		speed = v - g * time;
		if (degree < 50)
			degree++;

	}

	public void flyUp() {
		speed = v0;
		degree = -30;
	}

	@SuppressLint("HandlerLeak")
	class BirdHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			state++;
			switch (state % 8) {
			case 0:
				bitmap = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.bird0);
				break;
			case 1:
				bitmap = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.bird1);
				break;
			case 2:
				bitmap = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.bird2);
				break;
			case 3:
				bitmap = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.bird3);
				break;
			case 4:
				bitmap = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.bird4);
				break;
			case 5:
				bitmap = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.bird5);
				break;
			case 6:
				bitmap = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.bird6);
				break;
			case 7:
				bitmap = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.bird7);
				break;
			default:
				bitmap = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.bird0);
				break;
			}
		}

	}
}
