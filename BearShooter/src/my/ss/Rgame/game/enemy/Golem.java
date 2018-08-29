package my.ss.Rgame.game.enemy;

import java.util.Random;

import my.ss.Rgame.Global.Global;
import my.ss.Rgame.game.GameLayer;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.ease.CCEaseExponentialIn;
import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCRotateBy;
import org.cocos2d.actions.interval.CCScaleTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGPoint;

public class Golem extends Enemy{
	private CCRepeatForever moveAni;
	private CCAnimate fireAni;
	
	public Golem(GameLayer gameLayer, float x, float y, int power, int life, int gold, int probability){
		super(gameLayer, "enemy/golem/golem_00.png", Enemy.E_GOLEM, x, y, power, life, gold, probability);
		createMoveAnimate();
		createFireAnimate();
		createFireDieAnimate();
		this.runAction(this.moveAni);
	}

	@Override
	public void cleanup(){
		CCTextureCache.sharedTextureCache().removeTexture("enemy/golem/golem_00.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/golem/golem_01.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/golem/golem_02.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/golem/golem_03.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/golem/golem_04.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/golem/golem_05.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/attack/stone.png");
		super.cleanup();
	}

	private void createMoveAnimate(){
		CCAnimation animation = CCAnimation.animation("move");
		animation.addFrame("enemy/golem/golem_01.png");
		animation.addFrame("enemy/golem/golem_02.png");
		
		CCAnimate animate = CCAnimate.action(animation);
		animate.setDuration(1.0f);
		this.moveAni =  CCRepeatForever.action(animate);		
	}
	
	private void createFireAnimate(){
		CCAnimation animation = CCAnimation.animation("fire");
		animation.addFrame("enemy/golem/golem_03.png");
		animation.addFrame("enemy/golem/golem_04.png");
		
		CCAnimate animate = CCAnimate.action(animation);
		animate.setDuration(0.5f);
		this.fireAni = animate;
	}
	
	private CCAnimate createFireDieAnimate(){
		CCAnimation animation = CCAnimation.animation("die");
		animation.addFrame("enemy/golem/golem_05.png");
		
		CCAnimate animate = CCAnimate.action(animation);
		animate.setDuration(6.0f);
		return animate;
	}
	
	@Override
	public void move() {
		this.stopAllActions();
		this.runAction(this.moveAni);
		
		int x = 0, y = -MOVE_SIZE;
		CGPoint enemyPoint = this.getPosition();
		Random ran = new Random();
		
		switch(ran.nextInt(15)%3){
		case 0:
			x=-MOVE_SIZE;
			if(enemyPoint.x + x <= MOVE_SIZE)
				x=MOVE_SIZE;
			break;
		case 1:
			x=0;
			break;
		case 2:
			x=MOVE_SIZE;
			if(enemyPoint.x + x >= Global.DEFAULT_WIDTH-MOVE_SIZE)
				x=-MOVE_SIZE;
			break;
		}
		
		CCMoveBy moveAction = CCMoveBy.action(7.0f, CGPoint.ccp(x, y));
		this.runAction(moveAction);		
	}
	
	@Override
	public void attackPlayer() {
		this.stopAllActions();
		this.runAction(this.fireAni);
		
		//Sound.engine.play(Sound.engine.S_SWING);
		
		Random ran = new Random();
		float x = ran.nextInt(ATTACK_X_RANGE);
		if(ran.nextBoolean())
			x *= -1;
			
		x = this.getPosition().x + x;
		
		EnemyWepon stone = new EnemyWepon("enemy/attack/stone.png", EnemyWepon.TYPE_STONE);
		stone.setPosition(this.getPosition().x, this.getPosition().y + 10);
		stone.setAnchorPoint(0.5f, 0.5f);
		stone.setScale(0.1f);
		
		CCMoveTo move = CCMoveTo.action(2.6f, CGPoint.ccp(x, -100));
		CCEaseExponentialIn expAction = CCEaseExponentialIn.action(move);
		CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "attackFinish");
		stone.runAction(CCSequence.actions(expAction, actionMoveDone));
		
		CCScaleTo scale = CCScaleTo.action(2.4f, 1.8f);
		CCEaseExponentialIn expAction2 = CCEaseExponentialIn.action(scale);
		stone.runAction(expAction2);
		
		CCRotateBy rotate = CCRotateBy.action(3.0f, 270);
		stone.runAction(rotate);
		
		this.gameLayer.addChild(stone, GameLayer.WEPON_Z);
		this.gameLayer.addAttackList(stone);
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
