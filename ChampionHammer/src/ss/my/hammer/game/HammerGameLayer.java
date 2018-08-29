package ss.my.hammer.game;

import java.util.ArrayList;

import my.ss.sound.Sound;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCFadeTo;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCRotateTo;
import org.cocos2d.actions.interval.CCScaleTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.ccColor3B;

import ss.my.hammer.HammerActivity;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.MotionEvent;

public class HammerGameLayer extends CCLayer{
	private HammerActivity act;
	private RotateNumWidget rotateNumWidget;
	
	private CCLabel scoreLabel;
	private CCAnimate topAni;
	private CCSprite newScore;
	private CCSprite newAction;
	private CCRepeatForever topAction;
	private CCMenuItemImage resetBtn;
	
	private ArrayList<CCSprite> actionLabel;
	private final int E_TOUCH = 0;
	private final int E_READY = 1;
	private final int E_ACTION = 2;
	private final int E_WAIT = 3;
	
	private ArrayList<CCSprite> spriteList;
	private final int S_SCORELINE = 0;
	private final int S_ZOOM_BG = 1;
	private final int S_TARGET = 2;
	private final int S_TAP = 3;
	private final int S_HAMMER = 4;
	private final int S_HAMMER_ACTION = 5;
	private final int S_SPORT_LIGHT = 6;
	//private final int S_TOP = 7;
	
	private boolean isReady;
	private boolean isPunch;
	private boolean isCheck;
	
	private float acceleration;
	private float temp;
	
	private int sportCnt;
	
	private int cnt;
	private int cntMax;
	
	public static CCScene getScene(HammerActivity act){
		CCScene scene = CCScene.node();
		
		scene.setTag(HammerActivity.TAG_GAME);
		
		HammerGameLayer hammerGameLayer = new HammerGameLayer(act);
		scene.addChild(hammerGameLayer);
		
		act.setGameLayer(hammerGameLayer);
		
		return scene;
	}
	
	public HammerGameLayer(HammerActivity act){
		super();
		this.act = act;
		this.setIsTouchEnabled(true);
		this.spriteList = new ArrayList<CCSprite>();
		this.actionLabel = new ArrayList<CCSprite>();
		
		Sound.engine.playBGM("bg.mp3", true);
		init();
		updateScore(this.getScoreValue());
	}
	
	@Override
	public void cleanup(){
		super.cleanup();
		CCTextureCache.sharedTextureCache().removeTexture("back_btn_p.png");
		CCTextureCache.sharedTextureCache().removeTexture("back_btn.png");
		CCTextureCache.sharedTextureCache().removeTexture("reset_btn_p.png");
		CCTextureCache.sharedTextureCache().removeTexture("reset_btn.png");
		CCTextureCache.sharedTextureCache().removeTexture("background.png");
		CCTextureCache.sharedTextureCache().removeTexture("hammer_action.png");
		CCTextureCache.sharedTextureCache().removeTexture("hammer.png");
		CCTextureCache.sharedTextureCache().removeTexture("machine_bg.png");
		CCTextureCache.sharedTextureCache().removeTexture("score_line.png");
		CCTextureCache.sharedTextureCache().removeTexture("sport_light.png");
		CCTextureCache.sharedTextureCache().removeTexture("tap.png");
		CCTextureCache.sharedTextureCache().removeTexture("target.png");
		CCTextureCache.sharedTextureCache().removeTexture("top_00.png");
		CCTextureCache.sharedTextureCache().removeTexture("top_01.png");
		CCTextureCache.sharedTextureCache().removeTexture("zoom_machine.png");
		CCTextureCache.sharedTextureCache().removeTexture("effect/e_action.png");
		CCTextureCache.sharedTextureCache().removeTexture("effect/e_ready.png");
		CCTextureCache.sharedTextureCache().removeTexture("effect/e_touch.png");
		CCTextureCache.sharedTextureCache().removeTexture("effect/e_wait.png");
		CCTextureCache.sharedTextureCache().removeTexture("effect/new_action.png");
		CCTextureCache.sharedTextureCache().removeTexture("effect/record.png");
	}
	
	private void init(){
		RotateNumWidget rotateNumWidget = new RotateNumWidget(this);
		rotateNumWidget.setPosition(202, 705);
		
		this.addChild(rotateNumWidget, 2);
		this.rotateNumWidget = rotateNumWidget;
		
		createUI();
		
		this.schedule("checkDelay");
	}
	
	private void createUI(){
		CCSprite effectTouch = CCSprite.sprite("effect/e_touch.png");
		effectTouch.setAnchorPoint(0.5f, 0.5f);
		effectTouch.setPosition(380, 350);
		effectTouch.setScale(0.6f);
		effectTouch.runAction(getScaleAction(1.1f, 0.7f, 0.6f));
		this.addChild(effectTouch, 3);
		this.actionLabel.add(effectTouch);
		
		CCSprite effectReady = CCSprite.sprite("effect/e_ready.png");
		effectReady.setAnchorPoint(0.5f, 0.5f);
		effectReady.setPosition(350, 400);
		effectReady.setVisible(false);
		effectReady.runAction(getScaleAction(0.6f,  0.8f, 0.6f));
		this.addChild(effectReady, 3);
		this.actionLabel.add(effectReady);
		
		CCSprite effectAction = CCSprite.sprite("effect/e_action.png");
		effectAction.setAnchorPoint(0.5f, 0.5f);
		effectAction.setPosition(250, 200);
		effectAction.setVisible(false);
		effectAction.runAction(getScaleAction(0.3f, 0.8f, 0.5f));
		this.addChild(effectAction, 3);
		this.actionLabel.add(effectAction);
		
		CCSprite effectWait = CCSprite.sprite("effect/e_wait.png");
		effectWait.setAnchorPoint(0.5f, 0.5f);
		effectWait.setPosition(400, 600);
		effectWait.setVisible(false);
		effectWait.runAction(getScaleAction(0.7f,  0.7f, 0.5f));
		this.addChild(effectWait, 3);
		this.actionLabel.add(effectWait);
		
		CCSprite machineBg = CCSprite.sprite("machine_bg.png");
		machineBg.setAnchorPoint(0.5f, 0f);
		machineBg.setPosition(240, 222);
		this.addChild(machineBg, 0);
		
		CCSprite scoreLine = CCSprite.sprite("score_line.png");
		scoreLine.setAnchorPoint(0.5f, 0);
		scoreLine.setPosition(240, 229);
		scoreLine.setScaleY(0.001f);
		this.addChild(scoreLine, 0);
		this.spriteList.add(scoreLine);
		
		CCSprite bg = CCSprite.sprite("background.png");
		bg.setAnchorPoint(0, 0);
		bg.setPosition(0, 0);
		this.addChild(bg, 1);
		
		CCSprite newScore = CCSprite.sprite("effect/new_record.png");
		newScore.setAnchorPoint(0, 0);
		newScore.setPosition(43, 338);
		newScore.setVisible(false);
		this.addChild(newScore, 1);
		this.newScore = newScore;
		
		CCSprite newAction = CCSprite.sprite("effect/new_action.png");
		newAction.setAnchorPoint(1, 0);
		newAction.setPosition(85, 428);
		newAction.setVisible(false);
		this.addChild(newAction, 1);
		this.newAction = newAction;
		
		CCLabel scoreLabel = CCLabel.makeLabel("000", "", 80);
		scoreLabel.setAnchorPoint(0.5f, 0.5f);
		scoreLabel.setPosition(77, 575);
		scoreLabel.setRotation(18);
		scoreLabel.setColor(ccColor3B.ccRED);
		this.addChild(scoreLabel, 1);
		this.scoreLabel = scoreLabel;
		
		CCSprite zoomBg = CCSprite.sprite("zoom_machine.png");
		zoomBg.setAnchorPoint(0, 0);
		zoomBg.setPosition(0, 0);
		zoomBg.setVisible(false);
		this.addChild(zoomBg, 2);
		this.spriteList.add(zoomBg);
		
		CCSprite target = CCSprite.sprite("target.png");
		target.setAnchorPoint(0, 0);
		target.setVisible(false);
		this.addChild(target, 2);
		this.spriteList.add(target);
		
		CCSprite tap = CCSprite.sprite("tap.png");
		tap.setAnchorPoint(0, 0);
		tap.setVisible(false);
		this.addChild(tap, 2);
		this.spriteList.add(tap);
		
		CCSprite hammer = CCSprite.sprite("hammer.png");
		hammer.setAnchorPoint(0, 0);
		hammer.setVisible(false);
		this.addChild(hammer, 2);
		this.spriteList.add(hammer);
		
		CCSprite hammerAction = CCSprite.sprite("hammer_action.png");
		hammerAction.setAnchorPoint(0, 0);
		hammerAction.setVisible(false);
		hammerAction.setPosition(10, -360);
		this.addChild(hammerAction, 2);
		this.spriteList.add(hammerAction);
		
		CCSprite sportLight = CCSprite.sprite("sport_light.png");
		sportLight.setAnchorPoint(0, 0.5f);
		sportLight.setPosition(162, 236);
		this.addChild(sportLight, 1);
		this.spriteList.add(sportLight);
		sportLight.runAction(getSportLightAction(0.5f));
		
		createTopAnimate();
		
		CCSprite top = CCSprite.sprite("top_00.png");
		top.setAnchorPoint(0, 0);
		top.setPosition(180, 693);
		this.addChild(top, 1);
		this.spriteList.add(top);
		top.runAction(this.topAction);
		
		CCMenuItemImage backBtn = CCMenuItemImage.item("back_btn.png", "back_btn_p.png", this, "exit");		
		backBtn.setAnchorPoint(1, 1);
		backBtn.setPosition(470, 790);
		
		CCMenuItemImage resetBtn = CCMenuItemImage.item("reset_btn.png", "reset_btn_p.png", this, "reset");		
		resetBtn.setAnchorPoint(1, 1);
		resetBtn.setPosition(470, 700);
		this.resetBtn = resetBtn;
		
		CCMenu runMenu = CCMenu.menu();
		runMenu.setAnchorPoint(0, 0);
		runMenu.setPosition(0, 0);
		runMenu.addChild(backBtn);
		runMenu.addChild(resetBtn);
		this.addChild(runMenu, 5);
	}
	
	public void exit(Object sender){
		this.act.onBackPressed();
	}
	
	public void reset(Object sender){
		CCMenuItemImage resetBtn = (CCMenuItemImage) sender;
		
		if(resetBtn.getVisible()){
			SharedPreferences pf = this.act.getSharedPreferences("score", Activity.MODE_WORLD_WRITEABLE);
			SharedPreferences.Editor edit = pf.edit();
			edit.putInt("top_score", 0);
			edit.commit();
			
			this.updateScore(0);
		}
	}
	
	private void createTopAnimate(){
		CCAnimation animation = CCAnimation.animation("top");
		animation.addFrame("top_00.png");
		animation.addFrame("top_01.png");

		CCAnimate topAni = CCAnimate.action(animation);
		topAni.setDuration(1.0f);
		this.topAni = topAni;
		
		
		this.topAction = CCRepeatForever.action(topAni);		
	}
	
	public void setAcceleration(float acceleration){
		if(acceleration > this.acceleration)
			this.acceleration = acceleration;
	}

	public boolean ccTouchesEnded(MotionEvent event){
		if(!this.isReady)
			punchAction();
		
		return true;
	}
	
	private void punchAction(){
		CCDelayTime delay = CCDelayTime.action(1.0f);
		CCCallFuncN delayDone = CCCallFuncN.action(this, "ready");
		this.runAction(CCSequence.actions(delay, delayDone));
		
		Sound.engine.playBtnPress();
		
		this.resetBtn.setVisible(false);
		
		this.isReady = true;
		
		showInit();
	}
	
	public void ready(Object sender){
		this.temp = 0;
		this.acceleration = 0;
		this.isCheck = true;
		
		showReady();
	}
	
	public void checkDelay(float dt){
		if(this.isCheck){
			if(this.temp == 0){
				this.temp = this.acceleration;
				return;
			}
			
			//Log.d("SS", "acceleration = " + this.temp + ", " + this.acceleration);
			
			if(!this.isPunch){
				if(Math.abs(this.temp - this.acceleration) >= 7.5f){
					this.isPunch = true;
					createAction();
				}
				else{
					if(this.acceleration > 13.0f)
						this.acceleration = 0f;
					else
						this.temp = this.acceleration;
				}
			}
		}
	}
	
	private void createAction(){
		this.stopAllActions();
		
		CCDelayTime gameDelay = CCDelayTime.action(0.3f);
		CCCallFuncN gameDelayDone = CCCallFuncN.action(this, "punch");
		
		this.runAction(CCSequence.actions(gameDelay, gameDelayDone));
	}
	
	public void punch(Object sender){
		int score = 0;
		if(this.acceleration > 19f)
			score = 400 + (int)((this.acceleration - 19f) * 800f);
		else if(this.acceleration > 18f)
			score = 200 + (int)((this.acceleration - 18f) * 200f);
		else
			score = (int)(this.acceleration *  11.7f);
		
		if(score > 999)
			score = 999;
		else if(score <= 0)
			score = 0;

		this.rotateNumWidget.setNumber(score);
		this.isCheck = false;
		
		CCDelayTime gameDelay = CCDelayTime.action(1.0f);
		CCCallFuncN gameDelayDone = CCCallFuncN.action(this, "finishAction");
		
		this.runAction(CCSequence.actions(gameDelay, gameDelayDone));
		
		showPunch(score);
		Sound.engine.play(Sound.engine.S_PUNCH);
		this.act.playVibrate(350);
	}
	
	public void finishAction(Object sender){
		hideReady();
	}
	
	private void showInit(){
		ArrayList<CCSprite> spriteList = this.spriteList;
		
		spriteList.get(S_TARGET).setPosition(39, 260);
		spriteList.get(S_HAMMER).setPosition(10, 100);
		spriteList.get(S_HAMMER).setRotation(30);
		
		spriteList.get(S_ZOOM_BG).setVisible(true);
		spriteList.get(S_TARGET).setVisible(true);
		spriteList.get(S_HAMMER).setVisible(true);
		
		spriteList.get(S_SCORELINE).setScaleY(0.001f);
		
		CCMoveBy move = CCMoveBy.action(0.2f, CGPoint.ccp(0, 20));
		spriteList.get(S_TARGET).runAction(move);
		
		CCRotateTo rotate = CCRotateTo.action(0.6f, 0);
		spriteList.get(S_HAMMER).runAction(rotate);
		
		setEffectLabel(E_READY);
		Sound.engine.play(Sound.engine.S_READY);
	}
	
	private void showReady(){
		setEffectLabel(E_ACTION);
		CCMoveBy move = CCMoveBy.action(0.5f, CGPoint.ccp(0, 300));
		this.spriteList.get(S_HAMMER).runAction(move);
		Sound.engine.play(Sound.engine.S_HAMMER_READY);
	}
	
	private void hideReady(){
		ArrayList<CCSprite> spriteList = this.spriteList;
		spriteList.get(S_ZOOM_BG).setVisible(false);
		spriteList.get(S_TARGET).setVisible(false);
		spriteList.get(S_HAMMER).setVisible(false);
		spriteList.get(S_TAP).setVisible(false);
		spriteList.get(S_HAMMER_ACTION).setVisible(false);
		
		setEffectLabel(E_WAIT);
	}
	
	private void showPunch(int score){
		this.spriteList.get(S_HAMMER).stopAllActions();
		
		this.spriteList.get(S_HAMMER).setVisible(false);
		this.spriteList.get(S_HAMMER_ACTION).setVisible(true);
		
		CCMoveBy move = CCMoveBy.action(0.02f, CGPoint.ccp(0, -40));
		this.spriteList.get(S_TARGET).runAction(move);
		
		this.spriteList.get(S_TAP).setVisible(true);
		this.spriteList.get(S_TAP).setPosition(10, 210);
		
		this.cnt = 0;
		this.cntMax = (int)((float)score / 31f);
		
		this.spriteList.get(S_SCORELINE).runAction(getScoreDelayAction());
		
		this.topAni.setDuration(0.1f);
		this.spriteList.get(S_SPORT_LIGHT).stopAllActions();
		this.spriteList.get(S_SPORT_LIGHT).runAction(getSportLightAction(0.05f));
	}
	
	public void scoreAction(Object obj){
		CCSprite scoreLine = (CCSprite) obj;
		
		if(this.cnt <= this.cntMax){
			scoreLine.setScaleY((this.cnt + 1) * 0.031f);
			this.cnt ++;
			Sound.engine.play(Sound.engine.S_SCORE);
			
			this.topAni.setDuration(this.topAni.getDuration() + 0.05f);
			
			scoreLine.runAction(getScoreDelayAction());
		}
	}
	
	private CCSequence getScoreDelayAction(){
		CCDelayTime delay= CCDelayTime.action(6.5f / (float)this.cntMax);
		CCCallFuncN delayDone = CCCallFuncN.action(this, "scoreAction");
		
		return CCSequence.actions(delay, delayDone);
	}
	
	public void finishPunch(){
		if(this.isReady){
			this.isReady = false;
			this.isPunch = false;
			
			this.topAni.setDuration(1.0f);
			this.spriteList.get(S_SPORT_LIGHT).stopAllActions();
			this.spriteList.get(S_SPORT_LIGHT).runAction(getSportLightAction(0.5f));
			setEffectLabel(E_TOUCH);
			
			setScoreValue(this.rotateNumWidget.getScore());
			this.resetBtn.setVisible(true);
		}
	}
	
	private CCRepeatForever getSportLightAction(float dt){
		CCDelayTime delay= CCDelayTime.action(dt);
		CCCallFuncN delayDone = CCCallFuncN.action(this, "sportLightAction");
		
		return CCRepeatForever.action(CCSequence.actions(delay, delayDone));
	}
	
	public void sportLightAction(Object sender){
		CCSprite sportLight = (CCSprite) sender;
		CGPoint point = sportLight.getPosition();
		
		if(this.sportCnt == 13){
			sportLight.setPosition(point.x, 236);
			this.sportCnt = 0;
		}
		else{
			float offset = 31;
			switch(this.sportCnt){
			case 4:	offset = 32; break;
			case 5:	offset = 35; break;
			case 6:	offset = 36; break;
			case 7:	offset = 38; break;
			case 8: offset = 36; break;
			case 9: 
			case 10:
				offset = 35; break;
			case 11: offset = 33; break;
			
			}
			
			sportLight.setPosition(point.x, point.y + offset);
			
			this.sportCnt++;
		}
	}
	
	private void setScoreValue(int score) {
		 SharedPreferences pf = this.act.getSharedPreferences("score", Activity.MODE_WORLD_WRITEABLE);
		 
		 int oldScore = pf.getInt("top_score", 0);
		 
		 if(score >  oldScore){
			 SharedPreferences.Editor edit = pf.edit();
			 edit.putInt("top_score", score);
			 edit.commit();
			 updateScore(score);
			 actionNewScore();
		 }
		 else
			 Sound.engine.play(Sound.engine.S_NO_RECORD);
	}


	private int getScoreValue() {
		SharedPreferences pf = this.act.getSharedPreferences("score", Activity.MODE_WORLD_WRITEABLE);
		return pf.getInt("top_score", 0);
	}
	
	private void updateScore(int score){
		if(score == 0)
			this.scoreLabel.setString("000");
		else if(score < 100)
			this.scoreLabel.setString("0"+ score);
		else
			this.scoreLabel.setString(""+ score);
	}
	
	private void actionNewScore(){
		this.newScore.setVisible(true);
		this.newAction.setVisible(true);
		
		CCFadeTo fadeOut = CCFadeTo.action(0.15f, 0);
		CCFadeTo fadeIn = CCFadeTo.action(0.15f, 255);
		
		CCFadeTo fadeOut2 = CCFadeTo.action(0.3f, 0);
		CCFadeTo fadeIn2 = CCFadeTo.action(0.3f, 255);
		
		CCDelayTime delay = CCDelayTime.action(4.0f);
		CCCallFuncN done = CCCallFuncN.action(this, "newScoreAction");
		
		this.scoreLabel.runAction(CCRepeatForever.action(CCSequence.actions(fadeOut, fadeIn))); 
		this.newAction.runAction(CCRepeatForever.action(CCSequence.actions(fadeOut2, fadeIn2))); 
		this.newScore.runAction(CCSequence.actions(delay, done));
		
		Sound.engine.play(Sound.engine.S_NEW_RECORD);
	}
	
	public void newScoreAction(Object obj){
		this.newScore.stopAllActions();
		this.newAction.stopAllActions();
		this.scoreLabel.stopAllActions();
		this.scoreLabel.setOpacity(255);
		
		this.newScore.setVisible(false);
		this.newAction.setVisible(false);
	}
	
	private void setEffectLabel(int type){
		ArrayList<CCSprite> list = this.actionLabel;
		
		switch(type){
		case E_TOUCH :
			list.get(E_WAIT).setVisible(false);
			list.get(E_TOUCH).setVisible(true);
			break;
			
		case E_READY :
			list.get(E_TOUCH).setVisible(false);
			list.get(E_READY).setVisible(true);
			break;
			
		case E_ACTION : 
			list.get(E_READY).setVisible(false);
			list.get(E_ACTION).setVisible(true);
			break;
			
		case E_WAIT : 
			list.get(E_ACTION).setVisible(false);
			list.get(E_WAIT).setVisible(true);
			break;
		}
	}
	
	private CCRepeatForever getScaleAction(float speed, float up, float down){
		CCScaleTo scaleUp = CCScaleTo.action(speed, up);
		CCScaleTo scaleDown = CCScaleTo.action(speed/2f, down);
		
		return CCRepeatForever.action(CCSequence.actions(scaleUp, scaleDown)); 
	}
}
