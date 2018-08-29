package my.ss.Rgame.main;

import my.ss.Rgame.MainActivity;
import my.ss.Rgame.Global.Global;
import my.ss.Rgame.sound.Sound;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.ccColor3B;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.MotionEvent;

public class MainLayer extends CCLayer{
	private MainActivity act;
	private CCLabel recordLabel;
	private CCSprite soundOn;
	private CCSprite soundOff;
	private boolean isSoundBtn;
	
	public MainLayer(MainActivity act){
		super();
		this.act = act;
		init();
		
		Sound.engine.setSoundState(getSoundPref());
		Sound.engine.playBGM("main_bgm.ogg", true);
	}
	
	@Override
	public void onExit(){
		super.onExit();
	}
	
	@Override
	public void cleanup(){
		CCTextureCache.sharedTextureCache().removeTexture("main/ani_00.png");
		CCTextureCache.sharedTextureCache().removeTexture("main/ani_01.png");
		CCTextureCache.sharedTextureCache().removeTexture("main/ani_02.png");
		CCTextureCache.sharedTextureCache().removeTexture("main/menu_back.png");
		CCTextureCache.sharedTextureCache().removeTexture("main/main.png");
		CCTextureCache.sharedTextureCache().removeTexture("main/menu_start.png");
		CCTextureCache.sharedTextureCache().removeTexture("main/menu_help.png");
		CCTextureCache.sharedTextureCache().removeTexture("main/menu_exit.png");
		CCTextureCache.sharedTextureCache().removeTexture("main/menu_start_p.png");
		CCTextureCache.sharedTextureCache().removeTexture("main/menu_help_p.png");
		CCTextureCache.sharedTextureCache().removeTexture("main/menu_exit_p.png");
		CCTextureCache.sharedTextureCache().removeTexture("main/sound_on.png");
		CCTextureCache.sharedTextureCache().removeTexture("main/sound_off.png");
		CCTextureCache.sharedTextureCache().removeTexture("main/menu_exit_p.png");
		CCTextureCache.sharedTextureCache().removeTexture("main/record.png");
		CCTextureCache.sharedTextureCache().removeAllTextures();
		super.cleanup();
	}
	
	private void init(){
		this.setIsTouchEnabled(true);
		
		CCSprite bg = CCSprite.sprite("main/main.png");
		bg.setAnchorPoint(0, 0);
		bg.setPosition(0, 0);
		this.addChild(bg);
		
		CCSprite sand = CCSprite.sprite("main/ani_00.png");
		sand.setAnchorPoint(0, 0);
		sand.setPosition(0, -20);
		sand.runAction(this.createSandAnimate());
		this.addChild(sand);
		
		CCSprite record = CCSprite.sprite("main/record.png");
		record.setAnchorPoint(1, 0);
		record.setPosition(Global.DEFAULT_WIDTH, 350);
		this.addChild(record);
		
		CCSprite startBtn = CCSprite.sprite("main/menu_back.png");
		startBtn.setAnchorPoint(0.5f, 0);
		startBtn.setPosition(90, 0);
		this.addChild(startBtn);
		
		CCLabel recordLabel = CCLabel.makeLabel("--:--:--", "", 45);
		recordLabel.setAnchorPoint(1, 0);
		recordLabel.setPosition(Global.DEFAULT_WIDTH - 20, 365);
		recordLabel.setColor(ccColor3B.ccYELLOW);
		this.addChild(recordLabel, 3);
		this.recordLabel = recordLabel;
		
		CCSprite dragon = CCSprite.sprite("main/dragon.png");
		dragon.setAnchorPoint(0.5f, 0.5f);
		dragon.setPosition(145, 376);
		dragon.setScale(0.7f);
		dragon.runAction(createMovedAnimate());
		this.addChild(dragon);
		
		CCSprite dragon2 = CCSprite.sprite("main/dragon.png");
		dragon2.setAnchorPoint(0.5f, 0.5f);
		dragon2.setPosition(160, 436);
		dragon2.setScale(0.4f);
		dragon2.runAction(createMovedAnimate());
		this.addChild(dragon2);
		
		CCSprite dragon3 = CCSprite.sprite("main/dragon.png");
		dragon3.setAnchorPoint(0.5f, 0.5f);
		dragon3.setPosition(60, 416);
		dragon3.setScale(0.23f);
		dragon3.runAction(createMovedAnimate());
		this.addChild(dragon3);
		
		CCSprite soundOn = CCSprite.sprite("main/sound_on.png");
		soundOn.setAnchorPoint(0, 0);
		soundOn.setPosition(350, 0);
		soundOn.setVisible(false);
		this.addChild(soundOn);
		this.soundOn = soundOn;
		
		CCSprite soundOff = CCSprite.sprite("main/sound_off.png");
		soundOff.setAnchorPoint(0, 0);
		soundOff.setPosition(350, 0);
		soundOff.setVisible(false);
		this.addChild(soundOff);
		this.soundOff = soundOff;
		
		if(getSoundPref())
			soundOn.setVisible(true);
		else
			soundOff.setVisible(true);
			
		CCMenu menu = CCMenu.menu();
		CCMenuItemImage start = CCMenuItemImage.item("main/menu_start.png", "main/menu_start_p.png", this, "menuStart");		
		CCMenuItemImage help = CCMenuItemImage.item("main/menu_help.png", "main/menu_help_p.png", this, "menuHelp");		
		CCMenuItemImage exit = CCMenuItemImage.item("main/menu_exit.png", "main/menu_exit_p.png", this, "menuExit");		
		
		start.setAnchorPoint(0, 0);
		help.setAnchorPoint(0, 0);
		exit.setAnchorPoint(0, 0);
		
		start.setPosition(0, 190);
		help.setPosition(20, 125);
		exit.setPosition(10, 50);
		
		menu.addChild(start);
		menu.addChild(help);
		menu.addChild(exit);
		
		menu.setAnchorPoint(0, 0);
		menu.setPosition(10, 0);
		this.addChild(menu);
		
		
		updateRecord();
	}
	
	public void updateRecord(){	
    	SharedPreferences pf = this.act.getSharedPreferences("score", Activity.MODE_WORLD_WRITEABLE);
		int record = pf.getInt("record", 0);
		
		if(record == 0)
			this.recordLabel.setString("--:--:--");
		else
			this.recordLabel.setString(convertTime(record));
	}
	
	public String convertTime(int second){
		int sec = second % 60;
		int min = ((second - sec) / 60 % 60);
		int hour = (second - sec) / 60 / 60;
		String strSec;
		String strMin;
		String strHour;
		
		if(sec > 9)
			strSec = String.valueOf(sec);
		else
			strSec = "0"+String.valueOf(sec);
		
		if(min > 9)
			strMin = String.valueOf(min);
		else
			strMin = "0"+String.valueOf(min);
		
		if(hour > 9)
			strHour = String.valueOf(hour);
		else
			strHour = "0"+String.valueOf(hour);
		
		return strHour + ":" + strMin + ":" +  strSec; 
	}
	
	public void menuStart(Object sender){
		Sound.engine.playBtnPress2();
		this.act.gameStart();
	}
	
	public void menuHelp(Object sender){
		Sound.engine.playBtnPress2();
		this.act.helpStart();
	}
	
	public void menuExit(Object sender){
		Sound.engine.playBtnPress2();
		this.act.finish();
	}
	
	private CCRepeatForever createSandAnimate(){
		CCAnimation animation = CCAnimation.animation("sand");
		animation.addFrame("main/ani_00.png");
		animation.addFrame("main/ani_01.png");
		animation.addFrame("main/ani_02.png");

		CCAnimate axAnimate = CCAnimate.action(animation);
		axAnimate.setDuration(0.7f);
			
		return CCRepeatForever.action(axAnimate);		
	}
	
	private CCRepeatForever createMovedAnimate(){
		CCMoveBy move1 = CCMoveBy.action(2.0f, CGPoint.ccp(0, -5));
		CCMoveBy move2 = CCMoveBy.action(5.0f, CGPoint.ccp(0, 10));
		CCMoveBy move3 = CCMoveBy.action(1.5f, CGPoint.ccp(0, -5));
		
		return CCRepeatForever.action(CCSequence.actions(move1, move2, move3));		
	}
	
	public boolean ccTouchesBegan(MotionEvent event){
		CGPoint touchPoint = this.convertTouchToNodeSpace(event);
		
		if(this.soundOff.getBoundingBox().contains(touchPoint.x, touchPoint.y))
			this.isSoundBtn = true;
		
		return true;
	}

	public boolean ccTouchesEnded(MotionEvent event){
		CGPoint touchPoint = this.convertTouchToNodeSpace(event);
		
		if(this.isSoundBtn && this.soundOff.getBoundingBox().contains(touchPoint.x, touchPoint.y)){
			setSoundPref();
		}
		
		this.isSoundBtn = false;
		
		return true;
	}
	
	private void setSoundPref(){
		SharedPreferences pf = this.act.getSharedPreferences("score", Activity.MODE_WORLD_WRITEABLE);
		
		boolean state = pf.getBoolean("sound", true);
		
		SharedPreferences.Editor edit = pf.edit();
		edit.putBoolean("sound", !state);
		edit.commit();
		
		Sound.engine.setSoundState(!state);
		
		if(state){
			Sound.engine.stopSound();
			this.soundOff.setVisible(true);
			this.soundOn.setVisible(false);
		}
		else{
			Sound.engine.playBGM("main_bgm.ogg", true);
			Sound.engine.playBtnPress2();
			this.soundOff.setVisible(false);
			this.soundOn.setVisible(true);
		}
	}
	
	private boolean getSoundPref(){
		SharedPreferences pf = this.act.getSharedPreferences("score", Activity.MODE_WORLD_WRITEABLE);
		
		return pf.getBoolean("sound", true);
	}
}
