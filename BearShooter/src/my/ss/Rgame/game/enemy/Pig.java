package my.ss.Rgame.game.enemy;

import java.util.Random;

import my.ss.Rgame.game.GameLayer;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.ease.CCEaseExponentialIn;
import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCJumpTo;
import org.cocos2d.actions.interval.CCScaleTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGPoint;

public class Pig extends Enemy{
	private CCRepeatForever moveAni;
	private CCAnimate fireAni;
	private CCAnimate baseAtkAni;
	
	public Pig(GameLayer gameLayer, float x, float y, int power, int life, int gold, int probability){
		super(gameLayer, "enemy/pig/pig_00.png", Enemy.E_PIG, x, y, power, life, gold, probability);
		createMoveAnimate();
		createFireAnimate();
		createFireDieAnimate();
		createBaseAtkAnimate();
		this.runAction(this.moveAni);
	}

	@Override
	public void cleanup(){
		CCTextureCache.sharedTextureCache().removeTexture("enemy/pig/pig_00.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/pig/pig_01.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/pig/pig_02.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/pig/pig_03.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/pig/pig_04.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/pig/pig_05.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/attack/p_arrow.png");
		super.cleanup();
	}

	@Override
	public void move() {
		this.stopAllActions();
		this.runAction(this.moveAni);
		super.move();
	}
	
	private void createMoveAnimate(){
		CCAnimation animation = CCAnimation.animation("move");
		animation.addFrame("enemy/pig/pig_01.png");
		animation.addFrame("enemy/pig/pig_02.png");
		
		CCAnimate animate = CCAnimate.action(animation);
		animate.setDuration(0.7f);
		this.moveAni =  CCRepeatForever.action(animate);		
	}
	
	private void createFireAnimate(){
		CCAnimation animation = CCAnimation.animation("fire");
		animation.addFrame("enemy/pig/pig_03.png");
		
		CCAnimate animate = CCAnimate.action(animation);
		animate.setDuration(5.0f);
		this.fireAni = animate;
	}
	
	private void createBaseAtkAnimate(){
		CCAnimation animation = CCAnimation.animation("base");
		animation.addFrame("enemy/pig/pig_05.png");
		
		CCAnimate animate = CCAnimate.action(animation);
		animate.setDuration(0.8f);
		this.baseAtkAni = animate;
	}
	
	private CCAnimate createFireDieAnimate(){
		CCAnimation animation = CCAnimation.animation("die");
		animation.addFrame("enemy/pig/pig_04.png");
		
		CCAnimate animate = CCAnimate.action(animation);
		animate.setDuration(6.0f);
		return animate;
	}
	
	public void attackPlayer() {
		this.stopAllActions();
		this.runAction(this.fireAni);
		
		//Sound.engine.play(Sound.engine.S_ARROW);
		
		Random ran = new Random();
		float x = ran.nextInt(ATTACK_X_RANGE);
		if(ran.nextBoolean())
			x *= -1;
			
		x = this.getPosition().x + x;
		
		EnemyWepon arrow = new EnemyWepon("enemy/attack/p_arrow.png", EnemyWepon.TYPE_ARROW);
		arrow.setPosition(this.getPosition().x - 5, this.getPosition().y);
		arrow.setAnchorPoint(0.5f, 1.0f);
		arrow.setScale(0.05f);
		
		CCJumpTo move = CCJumpTo.action(2.8f, CGPoint.ccp(x, -100), 240, 1);
		CCEaseExponentialIn expAction = CCEaseExponentialIn.action(move);
		CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "attackFinish");
		arrow.runAction(CCSequence.actions(expAction, actionMoveDone));
		
		CCScaleTo scale = CCScaleTo.action(2.5f, 1.3f);
		CCEaseExponentialIn expAction2 = CCEaseExponentialIn.action(scale);
		arrow.runAction(expAction2);
		
		this.gameLayer.addChild(arrow, GameLayer.WEPON_Z);
		this.gameLayer.addAttackList(arrow);
	}
	
	public void attackFinish(Object sender){
		EnemyWepon wepon = (EnemyWepon)sender;
		this.gameLayer.removeAttackList(wepon);
		wepon.removeSelf();
		wepon.cleanup();
	}
	
	@Override
	public void attackBase() {
		super.attackBase();
		this.stopAllActions();
		this.runAction(this.baseAtkAni);
	}
	
	@Override
	public boolean die(int damege){
		boolean returnValue = super.die(damege);
		
		if(returnValue)
			this.runAction(createFireDieAnimate());
		
		return returnValue;
	}
}
