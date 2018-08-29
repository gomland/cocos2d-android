package ss.my.hammer.main;

import my.ss.sound.Sound;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.interval.CCScaleTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGPoint;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.MotionEvent;

import ss.my.hammer.HammerActivity;

public class GuideLayer extends CCLayer{
	private HammerActivity act;
	
	private CCSprite soundOn;
	private CCSprite soundOff;
	
	private boolean isTouch;
	
	public static CCScene getScene(HammerActivity act){
		CCScene scene = CCScene.node();
		
		scene.setTag(HammerActivity.TAG_MAIN);
		
		GuideLayer guideLayer = new GuideLayer(act);
		scene.addChild(guideLayer);
	
		return scene;
	}
	
	public GuideLayer(HammerActivity act){
		super();
		this.act = act;
		init();
		setSoundState(getSoundSetting());
		this.setIsTouchEnabled(true);
	}
	
	@Override
	public void cleanup(){		
		super.cleanup();
		CCTextureCache.sharedTextureCache().removeTexture("intro/intro.png");
		CCTextureCache.sharedTextureCache().removeTexture("intro/intro_btn.png");
		CCTextureCache.sharedTextureCache().removeTexture("intro/intro_btn_p.png");
		CCTextureCache.sharedTextureCache().removeTexture("intro/sound_on.png");
		CCTextureCache.sharedTextureCache().removeTexture("intro/sound_off.png");
	}
	
	private void init(){
		CCSprite bg = CCSprite.sprite("intro/intro.png");
		bg.setAnchorPoint(0, 0);
		bg.setPosition(0, 0);
		this.addChild(bg);
		
		CCSprite soundOn = CCSprite.sprite("intro/sound_on.png");
		soundOn.setAnchorPoint(0, 0);
		soundOn.setPosition(0, 0);
		soundOn.setVisible(false);
		this.addChild(soundOn);
		this.soundOn = soundOn;
		
		CCSprite soundOff = CCSprite.sprite("intro/sound_off.png");
		soundOff.setAnchorPoint(0, 0);
		soundOff.setPosition(0, 0);
		soundOff.setVisible(false);
		this.addChild(soundOff);
		this.soundOff = soundOff;
		
		CCMenuItemImage run = CCMenuItemImage.item("intro/intro_btn.png", "intro/intro_btn_p.png", this, "runGame");		
		run.setAnchorPoint(0.5f, 0.5f);
		run.setPosition(150, 755);
		
		CCScaleTo scaleUp = CCScaleTo.action(0.6f, 1.3f);
		CCScaleTo scaleDown = CCScaleTo.action(0.3f, 1.0f);
		run.runAction(CCRepeatForever.action(CCSequence.actions(scaleUp, scaleDown))); 
		
		CCMenu runMenu = CCMenu.menu(run);
		runMenu.setAnchorPoint(0, 0);
		runMenu.setPosition(0, 0);
		this.addChild(runMenu);
		
		CCMenuItemImage backBtn = CCMenuItemImage.item("back_btn.png", "back_btn_p.png", this, "exit");		
		backBtn.setAnchorPoint(1, 1);
		backBtn.setPosition(470, 790);
		runMenu.addChild(backBtn);
	}
	
	public void exit(Object sender){
		this.act.onBackPressed();
	}
	
	public void runGame(Object obj){
		Sound.engine.playBtnPress();
		this.act.startGameScene();
	}
	
	
	public boolean ccTouchesBegan(MotionEvent event){
		CGPoint touchPoint = this.convertTouchToNodeSpace(event);
		
		if(this.soundOff.getBoundingBox().contains(touchPoint.x, touchPoint.y))
			this.isTouch = true;
			
		return true;
	}
	
	public boolean ccTouchesMoved(MotionEvent event){
		CGPoint touchPoint = this.convertTouchToNodeSpace(event);
		
		if(!this.soundOff.getBoundingBox().contains(touchPoint.x, touchPoint.y))
			this.isTouch = false;
		
		return true;
	}
	
	public boolean ccTouchesEnded(MotionEvent event){
		if(this.isTouch)
			changeSoundSetting();
		
		this.isTouch = false;
		
		return true;
	}
	
	private void changeSoundSetting() {
		SharedPreferences pf = this.act.getSharedPreferences("score",
				Activity.MODE_WORLD_WRITEABLE);

		boolean sound = getSoundSetting();
		
		SharedPreferences.Editor edit = pf.edit();
		edit.putBoolean("sound", !sound);
		edit.commit();
		
		setSoundState(!sound);
		
		if(!sound)
			Sound.engine.playBtnPress();
	}
	
	private boolean getSoundSetting(){
		SharedPreferences pf = this.act.getSharedPreferences("score",
				Activity.MODE_WORLD_WRITEABLE);
		
		return pf.getBoolean("sound", true);
	}
	
	private void setSoundState(boolean sound){
		Sound.engine.setSoundState(sound);
		
		if(sound){
			this.soundOn.setVisible(true);
			this.soundOff.setVisible(false);
		}
		else{
			this.soundOn.setVisible(false);
			this.soundOff.setVisible(true);
		}
	}
}
