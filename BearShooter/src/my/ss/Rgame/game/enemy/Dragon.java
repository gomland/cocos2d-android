package my.ss.Rgame.game.enemy;

import java.util.Random;

import my.ss.Rgame.Global.Global;
import my.ss.Rgame.game.GameLayer;
import my.ss.Rgame.sound.Sound;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeTo;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCScaleTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.actions.interval.CCSpawn;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;

public class Dragon extends Enemy{	
	private CCRepeatForever moveAni;
	private CCRepeatForever flyAni;
	private CCAnimate stayAni;
	private CCAnimate baseAni;
	
	private CCAnimate fireAni;
	private CCAnimate fireStayAni;
	
	public Dragon(GameLayer gameLayer, float x, float y, int power, int life, int gold, int probability){
		super(gameLayer, "enemy/dragon/dragon_00.png", Enemy.E_DRAGON, x, y, power, life, gold, probability);
		
		createMoveAnimate();
		createFlyAnimate();
		createFireAnimate();		
		createBaseAnimate();
		
		this.runAction(this.flyAni);
	}

	@Override
	public void cleanup(){
		CCTextureCache.sharedTextureCache().removeTexture("enemy/dragon/dragon_00.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/dragon/dragon_01.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/dragon/dragon_02.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/dragon/dragon_03.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/dragon/dragon_04.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/dragon/dragon_05.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/dragon/dragon_06.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/dragon/dragon_07.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/dragon/dragon_08.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/dragon/dragon_09.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/breath.png");
		super.cleanup();
	}
	
	private void createMoveAnimate(){
		CCAnimation animation = CCAnimation.animation("fly");
		animation.addFrame("enemy/dragon/dragon_01.png");
		animation.addFrame("enemy/dragon/dragon_02.png");
		animation.addFrame("enemy/dragon/dragon_03.png");
		
		CCAnimate animate = CCAnimate.action(animation);
		animate.setDuration(0.8f);
		this.flyAni =  CCRepeatForever.action(animate);		
		
		CCAnimation animation2 = CCAnimation.animation("stay");
		animation2.addFrame("enemy/dragon/dragon_00.png");
		CCAnimate animate2 = CCAnimate.action(animation2);
		animate2.setDuration(15.0f);
		this.stayAni = animate2;
	}
	
	private void createFlyAnimate(){
		CCAnimation animation = CCAnimation.animation("move");
		animation.addFrame("enemy/dragon/dragon_04.png");
		animation.addFrame("enemy/dragon/dragon_05.png");
		
		CCAnimate animate = CCAnimate.action(animation);
		animate.setDuration(0.6f);
		this.moveAni =  CCRepeatForever.action(animate);		
	}
	
	private void createBaseAnimate(){
		CCAnimation animation = CCAnimation.animation("base");
		animation.addFrame("enemy/dragon/dragon_09.png");
		
		CCAnimate animate = CCAnimate.action(animation);
		animate.setDuration(0.8f);
		this.baseAni = animate;		
	}
	
	private void createFireAnimate(){
		CCAnimation animation = CCAnimation.animation("fire");
		animation.addFrame("enemy/dragon/dragon_06.png");
		animation.addFrame("enemy/dragon/dragon_07.png");
		
		CCAnimate animate = CCAnimate.action(animation);
		animate.setDuration(0.5f);
		this.fireAni = animate;
		
		CCAnimation animation2 = CCAnimation.animation("fireStay");
		animation2.addFrame("enemy/dragon/dragon_07.png");
		CCAnimate animate2 = CCAnimate.action(animation2);
		animate2.setDuration(15.0f);
		this.fireStayAni = animate2;
	}
	
	private CCAnimate createDieAnimate(){
		CCAnimation animation = CCAnimation.animation("die");
		animation.addFrame("enemy/dragon/dragon_08.png");
		
		CCAnimate animate = CCAnimate.action(animation);
		animate.setDuration(6.0f);
		return animate;
	}
	
	@Override
	protected void startAction(float x){
		int offset;
		
		if(x < 25){
			offset = 15;
			x = 30;
		}
		else if(x < 55)
			offset = 25;
		else if(x < 80)
			offset = 32;
		else if(x < 125)
			offset = 44;
		else if(x < 170)
			offset = 50;
		else if(x < 317)
			offset = 55;
		else if(x < 371)
			offset = 50;
		else if(x < 415)
			offset = 40;
		else if(x < 450)
			offset = 32;
		else{
			offset = 25;
			x = 450;
		}
		
		offset+=5;
		
		CGPoint point = CGPoint.ccp(x, Global.DEFAULT_HEIGHT - 170);
		this.setPosition(point.x, point.y);
		this.setScale(0.05f);
		
		CCFadeIn fade = CCFadeIn.action(10.0f);
		CCScaleTo scale = CCScaleTo.action(8.0f, 1.0f);
		CCMoveBy moveAction1 = CCMoveBy.action(10.0f, CGPoint.ccp(0, 150));
		CCMoveBy moveAction2 = CCMoveBy.action(0.8f, CGPoint.ccp(0, -210 - offset));
		CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "readyFinish");
		this.runAction(CCSequence.actions(CCSpawn.actions(fade, scale, moveAction1), moveAction2, actionMoveDone));
	}
	
	@Override
	public void readyFinish(Object sender){
		super.readyFinish(sender);
		this.runAction(this.stayAni);
	}
	
	@Override
	public void move() {
		this.stopAllActions();
		this.runAction(this.moveAni);
		
		int x = 0, y = -20;
		CGPoint enemyPoint = this.getPosition();
		Random ran = new Random();
		int MOVE_SIZE = this.MOVE_SIZE + 20;
		
		switch(ran.nextInt(15)%2){
		case 0:
			x=-MOVE_SIZE;
			if(enemyPoint.x + x <= MOVE_SIZE)
				x=MOVE_SIZE;
			break;
		case 1:
			x=MOVE_SIZE;
			if(enemyPoint.x + x >= Global.DEFAULT_WIDTH-MOVE_SIZE)
				x=-MOVE_SIZE;
			break;
		}
		
		CCMoveBy moveAction = CCMoveBy.action(3.5f, CGPoint.ccp(x, y));
		this.runAction(moveAction);		
	}
	
	public void attackPlayer() {
		this.stopAllActions();
		this.runAction(this.fireAni);
		
		Sound.engine.play(Sound.engine.S_BREATH);
		
		EnemyWepon breath = new EnemyWepon("enemy/attack/breath.png", EnemyWepon.TYPE_BREATH);
		CGPoint point = this.getPosition();
		CGSize breathSize = breath.getContentSize();
		
		float scaleY = (Global.DEFAULT_HEIGHT - (Global.DEFAULT_HEIGHT - point.y)) / breathSize.height + 0.09f;
		breath.setPosition(point.x, breathSize.height * scaleY * 0.01f);
		breath.setAnchorPoint(0.5f, 0.01f);
		breath.setOpacity(0);
		breath.setScaleX(0.05f);
		breath.setScaleY(scaleY);
		this.gameLayer.addChild(breath, GameLayer.WEPON_Z);
		
		CCFadeTo fade = CCFadeTo.action(2.5f, 100);
		CCCallFuncN actionFadeDone = CCCallFuncN.action(this, "addAttackList");
		CCFadeTo fade2 = CCFadeTo.action(0.5f, 255);
		CCFadeTo fade3 = CCFadeTo.action(1.0f, 0);
		CCCallFuncN actionFadeDone2 = CCCallFuncN.action(this, "attackFinish");
		breath.runAction(CCSequence.actions(fade, actionFadeDone, fade2, fade3, actionFadeDone2));
		
		CCDelayTime delay1 = CCDelayTime.action(0.5f);
		CCDelayTime delay = CCDelayTime.action(2.0f);
		CCCallFuncN actionDelayDone1 = CCCallFuncN.action(this, "motionSetting");
		CCCallFuncN actionDelayDone = CCCallFuncN.action(this, "delayFinish");
		breath.runAction(CCSequence.actions(delay1, actionDelayDone1, delay, actionDelayDone));
	}

	public void motionSetting(Object sender){
		if(this.life <= 0)
			return;
		this.stopAction(this.fireAni);
		this.runAction(this.fireStayAni);
	}
	
	public void delayFinish(Object sender){
		EnemyWepon breath = (EnemyWepon) sender;
		float scaleX = breath.getScaleX();
		
		if(scaleX <= 1.0f){
			breath.setScaleX(scaleX + 0.095f);
			CCDelayTime delay = CCDelayTime.action(0.05f);
			CCCallFuncN actionDelayDone = CCCallFuncN.action(this, "delayFinish");
			breath.runAction(CCSequence.actions(delay, actionDelayDone));
		}
	}
	
	public void addAttackList(Object sender){
		EnemyWepon breath = (EnemyWepon) sender;
		this.gameLayer.addAttackList(breath);
	}
	
	public void attackFinish(Object sender){
		CCSprite breath = (CCSprite) sender;
		this.gameLayer.removeAttackList(breath);
		breath.removeSelf();
		breath.cleanup();
	}
	
	@Override
	public void attackBase() {
		super.attackBase();
		this.stopAllActions();
		this.runAction(this.baseAni);
	}
	
	@Override
	public boolean die(int damege){
		boolean returnValue = super.die(damege);
		
		if(returnValue)
			this.runAction(createDieAnimate());
		
		return returnValue;
	}
}
