package ss.my.hammer;

import my.ss.sound.Sound;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.opengl.CCGLSurfaceView;

import ss.my.hammer.game.HammerGameLayer;
import ss.my.hammer.main.GuideLayer;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Display;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class HammerActivity extends Activity implements SensorEventListener{
	private LinearLayout layout;
	private CCGLSurfaceView glSurfaceView;
	
	private HammerGameLayer hammerGameLayer;
	
	public static Display display;
	
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	
	public static final int TAG_MAIN = 1256;
	public static final int TAG_GAME = 1259;

	private Vibrator vibe;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		android.os.Process
				.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_DISPLAY);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		this.layout = new LinearLayout(this);
		this.glSurfaceView = new CCGLSurfaceView(this);

		LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		this.layout.setLayoutParams(params);
		this.layout.setBackgroundColor(Color.BLACK);
		this.layout.addView(this.glSurfaceView);
		this.setContentView(this.layout);

		display = ((WindowManager) this
				.getSystemService(Activity.WINDOW_SERVICE)).getDefaultDisplay();
		this.reSizeView(this.glSurfaceView);
		
		new Sound(this);
		
		this.vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		
		startMainScene();
		createSensor();
	}
	
	private void createSensor(){
		this.mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        this.mAccelerometer = this.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onBackPressed() {
		CCScene fgScene = CCDirector.sharedDirector().getRunningScene();
		
		Sound.engine.stopSound();
		Sound.engine.playBtnPress();
		
		if(fgScene.getTag() == TAG_GAME)
			CCDirector.sharedDirector().popScene();
		else
			super.onBackPressed();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
		Sound.engine.stopSound();
		
		CCTextureCache.sharedTextureCache().removeAllTextures();
		CCSpriteFrameCache.purgeSharedSpriteFrameCache();
		CCTextureCache.purgeSharedTextureCache();
		CCDirector.sharedDirector().purgeCachedData();
		CCDirector.sharedDirector().getSendCleanupToScene();

	}

	@Override
	public void onStart() {
		super.onStart();
		CCDirector.sharedDirector().attachInView(this.glSurfaceView);
		CCDirector.sharedDirector().setDisplayFPS(false);
		CCDirector.sharedDirector().setAnimationInterval(1.0f / 60.0f);
	}

	@Override
	public void onPause() {
		super.onPause();
		CCDirector.sharedDirector().pause();
		Sound.engine.pauseBGM();
		this.mSensorManager.unregisterListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		Sound.engine.resumeBGM();
		CCDirector.sharedDirector().resume();
		this.mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_ACCELEROMETER);		
	}

	@Override
	public void onStop() {
		super.onStop();

	}

	public void startMainScene() {
		CCScene scene = GuideLayer.getScene(this);
		setScale(scene);
		CCDirector.sharedDirector().runWithScene(scene);
	}
	
	public void startGameScene() {
		CCScene scene = HammerGameLayer.getScene(this);
		setScale(scene);
		CCDirector.sharedDirector().pushScene(scene);
	}

	private void reSizeView(CCGLSurfaceView view) {
		int winWidth = display.getWidth();
		int winHeight = display.getHeight();

		float scaleWidth = ((float) winWidth) / 480f;
		float scaleHeight = ((float) winHeight) / 800f;

		LayoutParams params = view.getLayoutParams();

		if (scaleWidth > scaleHeight) {
			int padding = (int) ((winWidth - ((float) 480f) * scaleHeight) / 2);
			this.layout.setPadding(padding, 0, padding, 0);
			params.width = (int) ((float) 480f * scaleHeight);
		} else {
			int padding = (int) ((winHeight - ((float) 800f) * scaleWidth) / 2);
			this.layout.setPadding(0, padding, 0, padding);
			params.height = (int) ((float) 800f * scaleWidth);
		}

		view.setLayoutParams(params);
	}

	private void setScale(CCScene scene) {
		scene.setAnchorPoint(0, 0);
		scene.setScaleX(display.getWidth() / 480f);
		scene.setScaleY(display.getHeight() / 800f);
	}
	
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;
		
		if(this.hammerGameLayer != null){
			float x = Math.abs(event.values[0]);
			float y = Math.abs(event.values[1]);
			float z = Math.abs(event.values[2]);
			
			if(x > z && x > y)
				this.hammerGameLayer.setAcceleration(x);
			else if(y > z && y > x)
				this.hammerGameLayer.setAcceleration(y);
			else
				this.hammerGameLayer.setAcceleration(z);
		}
	}

	public void setGameLayer(HammerGameLayer hammerGameLayer){
		this.hammerGameLayer = hammerGameLayer;
	}
	
	public void playVibrate(int milliseconds){
		this.vibe.vibrate(milliseconds);
	}
}
