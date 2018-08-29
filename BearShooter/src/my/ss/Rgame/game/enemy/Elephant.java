package my.ss.Rgame.game.enemy;

import my.ss.Rgame.game.GameLayer;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGPoint;

public class Elephant extends Enemy{	
	private CCRepeatForever moveAni;
	private CCAnimate baseAtkAni;
	
	public Elephant(GameLayer gameLayer, float x, float y, int power, int life, int gold, int probability){
		super(gameLayer, "enemy/elephant/elephant_00.png", Enemy.E_ELEPHANT, x, y, power, life, gold, probability);
		createMoveAnimate();
		createBaseAttackAnimate();
		createDieAnimate();
	}

	@Override
	public void cleanup(){
		CCTextureCache.sharedTextureCache().removeTexture("enemy/elephant/elephant_00.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/elephant/elephant_01.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/elephant/elephant_02.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/elephant/elephant_03.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/elephant/elephant_04.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/elephant/elephant_05.png");
		super.cleanup();
	}
	
	private void createMoveAnimate(){
		CCAnimation animation = CCAnimation.animation("move");
		animation.addFrame("enemy/elephant/elephant_01.png");
		animation.addFrame("enemy/elephant/elephant_02.png");
		
		CCAnimate animate = CCAnimate.action(animation);
		animate.setDuration(1.5f);
		this.moveAni =  CCRepeatForever.action(animate);		
	}
	
	private void createBaseAttackAnimate(){
		CCAnimation animation = CCAnimation.animation("baseAttack");
		animation.addFrame("enemy/elephant/elephant_03.png");
		animation.addFrame("enemy/elephant/elephant_04.png");
		animation.addFrame("enemy/elephant/elephant_04.png");
		animation.addFrame("enemy/elephant/elephant_04.png");
		animation.addFrame("enemy/elephant/elephant_04.png");
		animation.addFrame("enemy/elephant/elephant_04.png");
		
		CCAnimate animate = CCAnimate.action(animation);
		animate.setDuration(1.0f);
		this.baseAtkAni = animate;
	}
	
	private CCAnimate createDieAnimate(){
		CCAnimation animation = CCAnimation.animation("die");
		animation.addFrame("enemy/elephant/elephant_05.png");
		
		CCAnimate animate = CCAnimate.action(animation);
		animate.setDuration(6.0f);
		return animate;
	}
	
	@Override
	public void move() {
		this.stopAllActions();
		this.runAction(this.moveAni);
		
		float x = this.getPosition().x;
		float xOffset = 0;
		
		if(x < 60)
			xOffset = 50;
		else if(x > 430)
			xOffset = -50;
		
		CCMoveBy moveAction = CCMoveBy.action(8.0f, CGPoint.ccp(xOffset, -MOVE_SIZE));
		this.runAction(moveAction);		
	}
	
	@Override
	public void attackPlayer() {
		move();
	}

	@Override
	public void attackBase(){
		this.stopAllActions();
		this.runAction(this.baseAtkAni);
		super.attackBase();
	}
	
	@Override
	public boolean die(int damege){
		boolean returnValue = super.die(damege);
		
		if(returnValue)
			this.runAction(createDieAnimate());
		
		return returnValue;
	}
}
