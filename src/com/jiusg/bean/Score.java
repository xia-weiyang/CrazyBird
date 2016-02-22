package com.jiusg.bean;

import android.graphics.Color;
import android.graphics.Paint;

import com.jiusg.crazybird.MainActivity;

public class Score {

	public int x = 0;
	public int y = 0;
	public int score = 0;
	public final String SCORE = "·ÖÊý: ";
	public Paint paint = null;

	public Score() {
		x = MainActivity.width / 20;
		y = (MainActivity.height / 30)+(int)(x*1.5);
		score = 0;
		paint = new Paint();
		paint.setTextSize((float)(x*1.5));
		paint.setColor(Color.WHITE);
		paint.setAlpha(180);
	}
}
