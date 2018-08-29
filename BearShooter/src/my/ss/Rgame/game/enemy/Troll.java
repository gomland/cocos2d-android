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

public class Troll extends Enemy{	
	private CCRepeatForever moveAni;
	private CCAnimate fireAni;
	
	public Troll(GameLayer gameLayer, float x, float y, int power, int life, int gold, int probability){
		super(gameLayer, "enemy/troll.png", Enemy.E_TROLL, x, y, power, life, gold, probability);
		
		createMoveAnimate();
		createFireAnimate();
		createFireDieAnimate();
		this.runAction(this.moveAni);
		
		this.schedule("lifeUp", 3.0f);
	}

	@Override
	public void cleanup(){
		CCTextureCache.sharedTextureCache().removeTexture("enemy/troll/troll_01.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/troll/troll_02.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/troll/troll_03.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/troll/troll_04.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/troll/troll_05.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/attack/ax/t_attack01.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/attack/ax/t_attack02.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/attack/ax/t_attack03.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/attack/ax/t_attack04.png");
		super.cleanup();
	}
	
	private void createMoveAnimate(){
		CCAnimation animation = CCAnimation.animation("move");
		animation.addFrame("enemy/troll/troll_01.png");
		animation.addFrame("enemy/troll/troll_02.png");
		
		CCAnimate animate = CCAnimate.action(animation);
		animate.setDuration(1.0f);
		this.moveAni =  CCRepeatForever.action(animate);		
	}
	
	private void createFireAnimate(){
		CCAnimation animation = CCAnimation.animation("fire");
		animation.addFrame("enemy/troll/troll_03.png");
		animation.addFrame("enemy/troll/troll_04.png");
		
		CCAnimate animate = CCAnimate.action(animation);
		animate.setDuration(0.5f);
		this.fireAni = animate;
	}
	
	private CCAnimate createFireDieAnimate(){
		CCAnimation animation = CCAnimation.animation("die");
		animation.addFrame("enemy/troll/troll_05.png");
		
		CCAnimate animate = CCAnimate.action(animation);
		animate.setDuration(6.0f);
		return animate;
	}
	
	public void lifeUp(float dt){
		if(this.maxLife > this.life){
			this.life++;
			this.updateLifeBar();
		}
	}
	
	@Override
	public void move() {
		this.stopAllActions();
		this.runAction(this.moveAni);
		super.move();
	}

	public void attackPlayer() {
		this.stopAllActions();
		this.runAction(this.fireAni);
		
		//Sound.engine.play(Sound.engine.S_SWING);
		
		Random ran = new Random();
		float x = ran.nextInt(ATTACK_X_RANGE);
		if(ran.nextBoolean())
			x *= -1;
			
		x = this.getPosition().x + x;
		
		EnemyWepon ax = new EnemyWepon("enemy/attack/ax/t_attack01.png", EnemyWepon.TYPE_AX);
		ax.setPosition(this.getPosition().x - 7, this.getPosition().y);
		ax.setAnchorPoint(0.5f, 1.0f);
		ax.setScale(0.1f);
		
		CCJumpTo move = CCJumpTo.action(3.5f, CGPoint.ccp(x, -100), 180, 1);
		CCEaseExponentialIn expAction = CCEaseExponentialIn.action(move);
		CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "attackFinish");
		ax.runAction(CCSequence.actions(expAction, actionMoveDone));
		
		CCScaleTo scale = CCScaleTo.action(3.3f, 1.7f);
		CCEaseExponentialIn expAction2 = CCEaseExponentialIn.action(scale);
		ax.runAction(expAction2);
		
		ax.runAction(createAxAnimate(0.2f));
		
		this.gameLayer.addChild(ax, GameLayer.WEPON_Z);
		this.gameLayer.addAttackList(ax);
	}
	
	private CCRepeatForever createAxAnimate(float speed){
		CCAnimation animation = CCAnimation.animation("ax");
		animation.addFrame("enemy/attack/ax/t_attack01.png");
		animation.addFrame("enemy/attack/ax/t_attack02.png");
		animation.addFrame("enemy/attack/ax/t_attack03.png");
		animation.addFrame("enemy/attack/ax/t_attack04.png");

		CCAnimate axAnimate = CCAnimate.action(animation);
		axAnimate.setDuration(speed);
			
		return CCRepeatForever.action(axAnimate);		
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
		this.runAction(this.fireAni);
	}
	
	@Override
	public boolean die(int damege){
		boolean returnValue = super.die(damege);
		
		if(returnValue)
			this.runAction(createFireDieAnimate());
		
		return returnValue;
	}
}
