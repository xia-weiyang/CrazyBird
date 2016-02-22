package com.jiusg.bean;

import java.util.Random;

import com.jiusg.crazybird.BirdView;
import com.jiusg.crazybird.MainActivity;
import com.jiusg.crazybird.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Column {

	public Bitmap bitmap = null;

	/* 间距 */
	private int distance = 0;
	/* 到地面的高度 */
	public static int height = 0;
	/* 柱子中间空的占比 */
	public final float yp = 347f / 2987f;
	/* 一半柱子的高度，不算中间空的 */
	public int columnHeight0 = 0;
	/* 中间空的高度 */
	public int columnHeight1 = 0;

	/* 位置 */
	public int position1 = 0;
	public int position2 = 0;

	public int x1 = 0;
	public int y1 = 0;
	public int x2 = 0;
	public int y2 = 0;

	private BirdView view = null;
	private Bird bird = null;
	private Score score = null;

	private boolean isX1 = true;
	private boolean isX2 = true;
	private boolean isDown = false;

	private float g = 0;
	private float time = 0;
	private float v0 = 0;
	private float speed = 0;
	private Random random = null;

	public Column(Context context, BirdView view, Ground ground, Bird bird,
			Score score) {
		this.view = view;
		this.bird = bird;
		this.score = score;

		random = new Random();

		bitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.column);
		distance = (int) ((float) MainActivity.width / 1.8f);
		height = MainActivity.height
				- (int) (ground.bitmap.getHeight() * BirdView.xP);

		columnHeight0 = (int) ((bitmap.getHeight() * (1 - yp)) / 2);
		columnHeight1 = (int) (bitmap.getHeight() * yp);
		Log.i("test", "height=" + bitmap.getHeight() + "   0=" + columnHeight0
				+ "   1=" + columnHeight1);

		position1 = (int) (Math.random() * ((height - columnHeight1)));
		position2 = (int) (Math.random() * ((height - columnHeight1)));

		x1 = MainActivity.width;
		y1 = -(bitmap.getHeight() / 2) + position1 + (columnHeight1 / 2);
		x2 = MainActivity.width + bitmap.getWidth() + distance;
		y2 = -(bitmap.getHeight() / 2) + position2 + (columnHeight1 / 2);
		reSetMove();
	}

	public void count() {

		// 判断小鸟是否碰上柱子
		if ((bird.x + bird.bitmap.getWidth()) > x1
				&& bird.x < (x1 + bitmap.getWidth())) {
			if (bird.y < y1 + columnHeight0
					|| (bird.y + bird.bitmap.getHeight()) > (y1 + columnHeight0 + columnHeight1)) {
				view.states = view.GAMEOVER;
				return;
			}
		}
		if ((bird.x + bird.bitmap.getWidth()) > x2
				&& bird.x < (x2 + bitmap.getWidth())) {
			if (bird.y < y2 + columnHeight0
					|| (bird.y + bird.bitmap.getHeight()) > (y2 + columnHeight0 + columnHeight1)) {
				view.states = view.GAMEOVER;
				return;
			}
		}

		// 分数统计
		if (bird.x > (x1 + bitmap.getWidth()) && isX1) {
			isX1 = false;
			score.score++;
		}
		if (x1 >= MainActivity.width)
			isX1 = true;
		if (bird.x > (x2 + bitmap.getWidth()) && isX2) {
			isX2 = false;
			score.score++;
		}
		if (x1 >= MainActivity.width)
			isX2 = true;

		// 柱子横向移动
		x1 -= (int) BirdView.speedP;
		x2 -= (int) BirdView.speedP;
		if (x1 <= -bitmap.getWidth()) {
			position1 = (int) (Math.random() * ((height - columnHeight1)));
			x1 = x2 + bitmap.getWidth() + distance;
			y1 = -(bitmap.getHeight() / 2) + position1 + (columnHeight1 / 2);
			reSetMove();
		}
		if (x2 <= -bitmap.getWidth()) {
			position2 = (int) (Math.random() * ((height - columnHeight1)));
			x2 = x1 + bitmap.getWidth() + distance;
			y2 = -(bitmap.getHeight() / 2) + position2 + (columnHeight1 / 2);
			reSetMove();
		}

		// 柱子纵向移动
		if (x1 - bird.x < (distance - bitmap.getWidth())
				&& (x1 - bird.x) > -bitmap.getWidth()
				&& y1 < -(columnHeight0 - height)
				&& y1 > -(columnHeight0 + columnHeight1)) {
			if (isDown) {
				float v = speed;
				float s = v * time - g * time * time;
				y1 = (int) (y1 - s);
				speed = v - g * time;
			} else {
				float v = speed;
				float s = v * time - g * time * time;
				y1 = (int) (y1 + s);
				speed = v - g * time;
			}
		}
		if (x2 - bird.x < (distance - bitmap.getWidth())
				&& (x2 - bird.x) > -bitmap.getWidth()
				&& y2 < -(columnHeight0 - height)
				&& y2 > -(columnHeight0 + columnHeight1)) {
			if (isDown) {
				float v = speed;
				float s = v * time - g * time * time;
				y2 = (int) (y2 - s);
				speed = v - g * time;
			} else {
				float v = speed;
				float s = v * time - g * time * time;
				y2 = (int) (y2 + s);
				speed = v - g * time;
			}
		}

	}

	public void flyUp() {
		speed = v0;
	}

	/**
	 * 重置柱子移动属性
	 */
	private void reSetMove() {
		g = 4;
		time = 0.25f;
		v0 = 20;
		speed = v0;
		isDown = random.nextBoolean();
	}
}
