package com.jiusg.crazybird;

import java.util.Timer;
import java.util.TimerTask;

import com.jiusg.bean.Bird;
import com.jiusg.bean.Column;
import com.jiusg.bean.Ghoast;
import com.jiusg.bean.Ground;
import com.jiusg.bean.Music;
import com.jiusg.bean.Score;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class BirdView extends SurfaceView implements SurfaceHolder.Callback {

	private Context mContext;
	private Timer timer = null;
	private Timer timerCount = null;
	private BirdHandler handler = null;
	private BirdCountHandler countHandler = null;
	private Paint paint = null;
	private Canvas canvas = null;
	private Matrix matrix = null;
	private SurfaceHolder mSurfaceHolder = null;

	private Bitmap bg_start = null;
	private Bitmap bg_stop = null;
	private Ground ground = null;
	private Bird bird = null;
	private Bitmap bg_tip = null;
	private Column column = null;
	private Score score = null;
	private Ghoast ghoast = null;
	private Music music = null;

	public int states = 1;
	public final int START = 1;
	public final int TIP = 4;
	public final int RUNNING = 2;
	public final int GAMEOVER = 3;

	/* ground系数 地面与整个图片高的比例系数 */
	public static final float xP = 230f / 340f;
	/* speed系数 控制速度 */
	public static final float speedP = (float) (MainActivity.width) / 300f;

	private SharedPreferences tip = null;

	/* 暂停 */
	public static boolean PAUSE = false;

	private final String TAG = "BirdView";

	public BirdView(Context context) {
		super(context);
		this.mContext = context;
		handler = new BirdHandler();
		countHandler = new BirdCountHandler();
		paint = new Paint();
		matrix = new Matrix();
		music = new Music(context);
		mSurfaceHolder = getHolder();
		mSurfaceHolder.addCallback(this);

		this.setBackgroundResource(R.drawable.bg);
		this.setZOrderOnTop(true);
		getHolder().setFormat(PixelFormat.TRANSLUCENT);

		tip = context.getSharedPreferences("tip", 0);

		init();
	}

	@SuppressLint("WrongCall")
	public void Draw() {
		canvas = mSurfaceHolder.lockCanvas();
		if (mSurfaceHolder == null || canvas == null) {

			return;
		}
		canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR); // 清屏效果
		switch (states) {
		case START:
			onDrawStart(canvas);
			break;
		case RUNNING:
			onDrawRunning(canvas);
			break;
		case GAMEOVER:
			onDrawGameover(canvas);
			break;
		case TIP:
			onDrawTip(canvas);
			break;
		default:
			break;
		}
		onDraw(canvas);
		mSurfaceHolder.unlockCanvasAndPost(canvas);
	}

	private void onDrawRunning(Canvas canvas) {

		canvas.drawBitmap(column.bitmap, column.x1, column.y1, paint);
		canvas.drawBitmap(column.bitmap, column.x2, column.y2, paint);
		canvas.drawBitmap(ground.bitmap, ground.x0, ground.y0, paint);
		canvas.drawBitmap(ground.bitmap, ground.x1, ground.y1, paint);
		canvas.drawBitmap(ghoast.bitmap, ghoast.x, ghoast.y, paint);
		canvas.drawText("" + score.score, score.x, score.y, score.paint);
		matrix.reset();
		matrix.setTranslate(bird.x, bird.y);
		matrix.preRotate(bird.degree, bird.bitmap.getWidth() / 2,
				bird.bitmap.getHeight() / 2);
		canvas.drawBitmap(bird.bitmap, matrix, paint);
	}

	private void onDrawStart(Canvas canvas) {

		canvas.drawBitmap(bg_start, 0, 0, paint);
	}

	private void onDrawGameover(Canvas canvas) {

		gameover();
		onDrawRunning(canvas);
		canvas.drawBitmap(bg_stop, 0, 0, paint);
	}

	private void onDrawTip(Canvas canvas) {
		canvas.drawBitmap(bg_tip, 0, 0, paint);
	}

	private void gameover() {
		Log.i(TAG, "gameover");
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (timerCount != null) {
			timerCount.cancel();
			timerCount = null;
		}
		if (music != null) {
			music.stopMusic();
		}
	}

	private void startTip() {
		states = TIP;
		Draw();
	}

	private void startGame() {

		states = RUNNING;

		if (music != null)
			music.startMusic();

		TimerTask task = new TimerTask() {
			public void run() {

				Message msg = handler.obtainMessage();
				handler.sendMessage(msg);
			}
		};
		if (timer == null)
			timer = new Timer(true);

		timer.schedule(task, 0, 1000 / 60);

		TimerTask task1 = new TimerTask() {
			public void run() {

				Message msg = countHandler.obtainMessage();
				countHandler.sendMessage(msg);
			}
		};
		if (timerCount == null)
			timerCount = new Timer(true);
		timerCount.schedule(task1, 0, 1000 / 50);
	}

	private void init() {

		bg_start = BitmapFactory.decodeResource(getResources(),
				R.drawable.start);
		bg_start = Bitmap.createScaledBitmap(bg_start, MainActivity.width,
				MainActivity.height, false);

		bg_stop = BitmapFactory.decodeResource(getResources(),
				R.drawable.gameover);
		bg_stop = Bitmap.createScaledBitmap(bg_stop, MainActivity.width,
				MainActivity.height, false);

		bg_tip = BitmapFactory.decodeResource(getResources(), R.drawable.tip);
		bg_tip = Bitmap.createScaledBitmap(bg_tip, MainActivity.width,
				MainActivity.height, false);

		score = new Score();
		bird = new Bird(mContext);
		ground = new Ground(mContext, this, bird);
		column = new Column(mContext, this, ground, bird, score);
		ghoast = new Ghoast(mContext, bird, this);

	}

	private void count() {

		bird.count();
		ground.count();
		column.count();
		ghoast.count();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		Draw();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

	@SuppressLint("HandlerLeak")
	class BirdHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			if (!PAUSE)
				Draw();
			this.removeCallbacksAndMessages(null);
		}

	}

	@SuppressLint("HandlerLeak")
	class BirdCountHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			if (!PAUSE)
				count();
			this.removeCallbacksAndMessages(null);
		}

	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (states) {
		case START:
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (tip.getBoolean("tip", false))
					startGame();
				else
					startTip();
				break;
			default:
				break;
			}
			break;
		case RUNNING:
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				if (event.getX() > MainActivity.width / 2) {
					Log.i(TAG, "right");
					column.flyUp();
				} else {
					Log.i(TAG, "left");
					bird.flyUp();
				}
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				if (event.getX(1) > MainActivity.width / 2) {
					Log.i(TAG, "right+2");
					column.flyUp();
				} else {
					Log.i(TAG, "left+2");
					bird.flyUp();
				}
				break;
			default:
				break;
			}
			break;
		case GAMEOVER:
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				init();
				startGame();
				break;
			default:
				break;
			}
			break;
		case TIP:
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (event.getX() > (0.2875f * MainActivity.width)
						&& event.getX() < (0.70625f * MainActivity.width))
					if (event.getY() > (0.775f * MainActivity.height)
							&& event.getY() < (0.875f * MainActivity.height)) {
						startGame();
						tip.edit().putBoolean("tip", true).apply();
					}
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}

		return true;
	}

}
