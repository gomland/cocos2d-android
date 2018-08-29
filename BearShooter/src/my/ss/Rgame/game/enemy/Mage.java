package my.ss.Rgame.game.enemy;

import java.util.Random;

import my.ss.Rgame.Global.Global;
import my.ss.Rgame.game.GameLayer;
import my.ss.Rgame.game.item.Guard;
import my.ss.Rgame.sound.Sound;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.ease.CCEaseExponentialIn;
import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCFadeTo;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCScaleTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGPoint;

public class Mage extends Enemy{
	private CCSprite lightBg;
	
	private CCRepeatForever moveAni;
	private CCRepeatForever fireAni;
	private CCRepeatForever lightAni;
	
	public Mage(GameLayer gameLayer, float x, float y, int power, int life, int gold, int probability){
		super(gameLayer, "enemy/mage/mage_00.png", Enemy.E_MAGE, x, y, power, life, gold, probability);
		createMoveAnimate();
		createFireAnimate();
		createLightAnimate();
		createDieAnimate();
	}

	@Override
	public void cleanup(){
		CCTextureCache.sharedTextureCache().removeTexture("enemy/mage/mage_00.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/mage/mage_01.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/mage/mage_02.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/mage/mage_03.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/mage/mage_04.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/mage/mage_05.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/mage/mage_06.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/mage/mage_07.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/mage/mage_08.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/mage/mage_09.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/mage/mage_10.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/mage/mage_11.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/mage/mage_12.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/mage/mage_13.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/mage/mage_14.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/attack/fireball.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/attack/light_bg.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/attack/light.png");
		super.cleanup();
	}

	private void createMoveAnimate(){
		CCAnimation animation = CCAnimation.animation("move");
		animation.addFrame("enemy/mage/mage_01.png");
		animation.addFrame("enemy/mage/mage_02.png");
		
		CCAnimate animate = CCAnimate.action(animation);
		animate.setDuration(1.0f);
		this.moveAni =  CCRepeatForever.action(animate);		
	}
	
	private void createFireAnimate(){
		CCAnimation animation = CCAnimation.animation("fire");
		animation.addFrame("enemy/mage/mage_03.png");
		animation.addFrame("enemy/mage/mage_04.png");
		animation.addFrame("enemy/mage/mage_05.png");
		
		CCAnimate animate = CCAnimate.action(animation);
		animate.setDuration(0.3f);
		this.fireAni = CCRepeatForever.action(animate);
	}
	
	private void createLightAnimate(){
		CCAnimation animation = CCAnimation.animation("light");
		animation.addFrame("enemy/mage/mage_06.png");
		animation.addFrame("enemy/mage/mage_07.png");
		animation.addFrame("enemy/mage/mage_08.png");
		animation.addFrame("enemy/mage/mage_09.png");
		
		CCAnimate animate = CCAnimate.action(animation);
		animate.setDuration(0.5f);
		this.lightAni = CCRepeatForever.action(animate);
	}
	
	private CCAnimate createDieAnimate(){
		CCAnimation animation = CCAnimation.animation("die");
		animation.addFrame("enemy/mage/mage_10.png");
		animation.addFrame("enemy/mage/mage_11.png");
		animation.addFrame("enemy/mage/mage_12.png");
		animation.addFrame("enemy/mage/mage_13.png");
		animation.addFrame("enemy/mage/mage_14.png");
		animation.addFrame("enemy/mage/mage_14.png");
		animation.addFrame("enemy/mage/mage_14.png");
		animation.addFrame("enemy/mage/mage_14.png");
		animation.addFrame("enemy/mage/mage_14.png");
		animation.addFrame("enemy/mage/mage_14.png");
		animation.addFrame("enemy/mage/mage_14.png");
		
		CCAnimate animate = CCAnimate.action(animation);
		animate.setDuration(3.0f);
		return animate;
	}
	
	@Override
	public void move() {
		this.stopAllActions();
		this.runAction(this.moveAni);
		super.move();
	}
	
	@Override
	public void attackPlayer() {
		int ran = new Random().nextInt(100);

		if(this.lightBg == null && ran >= 65)
			lightningBolt();
		else
			fireball();
	}
	
	private void lightningBolt(){
		this.stopAllActions();
		this.runAction(this.lightAni);
		
		Sound.engine.play(Sound.engine.S_LIGHTNING);
		
		Random ran = new Random();
		
		float x = 0;
		switch(ran.nextInt(3)){
		case 0 : 
			x = Guard.X_POS_L;
			break;
		case 1 :
			x = Guard.X_POS_M;
			break;
		case 2 : 
			x = Guard.X_POS_R;
			break;
		}
		
		CCSprite lightBg = CCSprite.sprite("enemy/attack/light_bg.png");
		lightBg.setPosition(x+10, Global.DEFAULT_HEIGHT - 80);
		lightBg.setAnchorPoint(0.5f, 0.5f);
		lightBg.setOpacity(0);
		this.lightBg = lightBg;
		this.gameLayer.addChild(lightBg, GameLayer.WEPON_Z);
		
		EnemyWepon light = new EnemyWepon("enemy/attack/light.png", EnemyWepon.TYPE_LIGHTNING);
		light.setPosition(x, 0);
		light.setAnchorPoint(0.5f, 0);
		light.setScale(0.05f);
		light.setOpacity(0);
		this.gameLayer.addChild(light, GameLayer.WEPON_Z);
		
		CCFadeTo fade = CCFadeTo.action(4.0f, 100);
		CCFadeTo fade1 = CCFadeTo.action(0.5f, 255);
		CCFadeTo fade2 = CCFadeTo.action(0.2f, 0);
		CCCallFuncN done = CCCallFuncN.action(this, "lightFinish");
		lightBg.runAction(CCSequence.actions(fade, fade1, fade2, done));
		
		CCDelayTime delay = CCDelayTime.action(4.0f);
		CCCallFuncN add = CCCallFuncN.action(this, "addAttackList");
		CCFadeTo lightFade1 = CCFadeTo.action(0.5f, 255);
		CCFadeTo lightFade2 = CCFadeTo.action(0.2f, 0);
		CCCallFuncN actionDone = CCCallFuncN.action(this, "attackFinish");
		light.runAction(CCSequence.actions(delay, add, lightFade1, lightFade2, actionDone));
		
		CCScaleTo scale = CCScaleTo.action(0.0f, 1.0f);
		light.runAction(CCSequence.actions(delay, scale));
	}
	
	public void addAttackList(Object sender){
		EnemyWepon light = (EnemyWepon) sender;
		this.gameLayer.addAttackList(light);
	}
	
	private void fireball(){
		this.stopAllActions();
		this.runAction(this.fireAni);
		
		//Sound.engine.play(Sound.engine.S_FIREBALL);
		
		Random ran = new Random();
		float x = ran.nextInt(ATTACK_X_RANGE);
		if(ran.nextBoolean())
			x *= -1;
			
		x = this.getPosition().x + x;
		
		EnemyWepon fire = new EnemyWepon("enemy/attack/fireball.png", EnemyWepon.TYPE_FIREBALL);
		fire.setPosition(this.getPosition().x - 10, this.getPosition().y + 6);
		fire.setAnchorPoint(0.5f, 0.5f);
		fire.setScale(0.05f);
		
		CCMoveTo move = CCMoveTo.action(3.5f, CGPoint.ccp(x, -110));
		CCEaseExponentialIn expAction = CCEaseExponentialIn.action(move);
		CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "attackFinish");
		fire.runAction(CCSequence.actions(expAction, actionMoveDone));
		
		CCScaleTo scale = CCScaleTo.action(3.3f, 1.8f);
		CCEaseExponentialIn expAction2 = CCEaseExponentialIn.action(scale);
		fire.runAction(expAction2);

		this.gameLayer.addChild(fire, GameLayer.WEPON_Z);
		this.gameLayer.addAttackList(fire);
	}

	public void attackFinish(Object sender){
		EnemyWepon wepon = (EnemyWepon)sender;
		this.gameLayer.removeAttackList(wepon);
		wepon.removeSelf();
		wepon.cleanup();
	}
	
	public void lightFinish(Object sender){
		this.lightBg.removeSelf();
		this.lightBg.cleanup();
		this.lightBg = null;
	}
	
	@Override
	public void attackBase() {
		super.attackBase();
		this.stopAllActions();
		this.runAction(this.fireAni);
	}
	
	@Override
	public boolean die(int damege){
		boolean returnValue = super.die(damege);
		
		if(returnValue)
			this.runAction(createDieAnimate());
		
		return returnValue;
	}
}
