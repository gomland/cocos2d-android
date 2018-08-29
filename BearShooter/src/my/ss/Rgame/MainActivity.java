package my.ss.Rgame;

import my.ss.Rgame.Global.Global;
import my.ss.Rgame.game.GameLayer;
import my.ss.Rgame.main.MainLayer;
import my.ss.Rgame.sound.Sound;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
	private LinearLayout layout;
	private CCGLSurfaceView glSurfaceView;
	private CustomCCScene mainScene;
	private int winWidth;
	private int winHeight;

	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    	this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    	this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	
    	this.layout = new LinearLayout(this);
    	this.glSurfaceView = new CCGLSurfaceView(this);
    	
    	LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    	this.layout.setLayoutParams(params);
    	this.layout.setBackgroundColor(Color.BLACK);
    	this.layout.addView(this.glSurfaceView);
    	this.setContentView(this.layout);
    	
    	Display display = ((WindowManager)this.getSystemService(Activity.WINDOW_SERVICE)).getDefaultDisplay();
    	this.winWidth = display.getWidth();
    	this.winHeight = display.getHeight();
    	this.reSizeView(this.glSurfaceView);
    	
    	new Sound(this);
    	
    	mainMenuStart();
    }
	
	
	@Override //block calling onStop() & onDestroy() when screen off. 
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
	}

	
	@Override
	public void onDestroy(){
		Sound.engine.stopSound();
		super.onDestroy();
	}
	
    @Override
    public void onStart() { 
    	super.onStart();  
    	CCDirector.sharedDirector().attachInView(this.glSurfaceView);   
    	CCDirector.sharedDirector().setDisplayFPS(false);     
    	CCDirector.sharedDirector().setAnimationInterval(1.0f / 60); 
    }
    
    @Override
    public void onPause() { 
    	super.onPause();    
    	Sound.engine.pauseBGM();
    	CCDirector.sharedDirector().pause();
    }  
    
    @Override
    public void onResume() {  
    	super.onResume();   
    	Sound.engine.resumeBGM();	
    	CCDirector.sharedDirector().resume();
    
    }  
    
    @Override
    public void onStop() {  
    	super.onStop();  
    	//CCDirector.sharedDirector().end(); 
    } 
    
    @Override
	public void onBackPressed() {
    	CustomCCScene scene = (CustomCCScene)CCDirector.sharedDirector().getRunningScene();
    	if(scene != null && scene.getSceneType() == CustomCCScene.SCENE_GAME){
    		GameLayer layer = (GameLayer)scene.getChildren().get(0);
    		layer.endPopup();
    	}
    	else if(scene != null && scene.getSceneType() == CustomCCScene.SCENE_HELP){
    		CCDirector.sharedDirector().popScene();
    	}
    	else
    		this.finish();
    }

    public void mainMenuStart()
    {
    	CustomCCScene scene = SceneManager.getMainScene(this);
    	setScale(scene);
    	this.mainScene = scene;
    	CCDirector.sharedDirector().runWithScene(scene);
    }	 
    
    public void gameStart()
    {
    	CustomCCScene scene = SceneManager.getGameScene(this);
    	setScale(scene);
    	CCDirector.sharedDirector().pushScene(scene);
    }	

    public void helpStart()
    {
    	CustomCCScene scene = SceneManager.getHelpScene();
    	setScale(scene);
    	CCDirector.sharedDirector().pushScene(scene);
    }	
    
    private void reSizeView(CCGLSurfaceView view){
		float scaleWidth = ((float)this.winWidth)/Global.DEFAULT_WIDTH;
    	float scaleHeight = ((float)this.winHeight)/Global.DEFAULT_HEIGHT;
    	
    	LayoutParams params = view.getLayoutParams();
    	
    	if(scaleWidth > scaleHeight){
    		int padding = (int)((this.winWidth - ((float)Global.DEFAULT_WIDTH) * scaleHeight)/2);
    		this.layout.setPadding(padding, 0, padding, 0);
    		params.width = (int)((float)Global.DEFAULT_WIDTH * scaleHeight);
    		Global.SCREEN_SCALE = scaleHeight;
    	}
    	else{
    		int padding = (int)((this.winHeight - ((float)Global.DEFAULT_HEIGHT) * scaleWidth)/2);
    		this.layout.setPadding(0, padding, 0, padding);
    		params.height = (int)((float)Global.DEFAULT_HEIGHT * scaleWidth);
    		Global.SCREEN_SCALE = scaleWidth;
    	}
    	
    	view.setLayoutParams(params);
	}
	
    private void setScale(CCScene scene){
    	scene.setAnchorPoint(0,0);
    	scene.setScaleX(this.winWidth / (float)Global.DEFAULT_WIDTH);
    	scene.setScaleY(this.winHeight / (float)Global.DEFAULT_HEIGHT);
    }
    
    public void updateMain(){
    	MainLayer layer = (MainLayer)this.mainScene.getChildren().get(0);
    	layer.updateRecord();
    }
}