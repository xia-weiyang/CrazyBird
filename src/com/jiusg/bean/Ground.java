package com.jiusg.bean;

import com.jiusg.crazybird.BirdView;
import com.jiusg.crazybird.MainActivity;
import com.jiusg.crazybird.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Ground {

	public Bitmap bitmap;
	public int x0 = 0;
	public int y0 = 0;
	public int x1 = 0;
	public int y1 = 0;
	private Bird bird = null;
	private BirdView view = null;

	public Ground(Context mContext, BirdView view, Bird bird) {
		this.bird = bird;
		this.view = view;
		bitmap = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.ground);
		x0 = 0;
		y0 = MainActivity.height - bitmap.getHeight();
		x1 = bitmap.getWidth();
		y1 = y0;
	}

	public void count() {
		if ((bird.y + bird.bitmap.getHeight()) >= (MainActivity.height - (bitmap
				.getHeight() * BirdView.xP))) {
			view.states = view.GAMEOVER;
			System.out.println("***"+view.states);
			return;
		}
		x0 -= (int) BirdView.speedP;
		x1 -= (int) BirdView.speedP;
		if (x0 <= -bitmap.getWidth())
			x0 = x1 + bitmap.getWidth();
		if (x1 <= -bitmap.getWidth())
			x1 = x0 + bitmap.getWidth();
	}
}
