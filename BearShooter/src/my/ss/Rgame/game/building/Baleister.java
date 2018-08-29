package my.ss.Rgame.game.building;

import java.util.ArrayList;
import java.util.Random;

import my.ss.Rgame.Global.Global;
import my.ss.Rgame.game.GameLayer;
import my.ss.Rgame.game.enemy.Enemy;
import my.ss.Rgame.sound.Sound;
import my.ss.Rgame.wepon.Wepon;

import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCJumpTo;
import org.cocos2d.actions.interval.CCRotateBy;
import org.cocos2d.actions.interval.CCScaleTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;

public class Baleister extends CCSprite{
	private final float delay = 7.0f;
	private GameLayer gameLayer;	
	
	private final float rangeMin = 330;
	private final float rangeMax = 520;
	private final int DAMEGE = 6;
	
	private CCAnimate animate;
	
	private float respon;
	
	public Baleister(GameLayer gameLayer, float x, float y){
		super("building/baleister/baleister_00.png");
		
		this.gameLayer = gameLayer;
		createShotAnimate();
		
		this.setPosition(x, y);
		this.setAnchorPoint(0.5f, 0);
		this.schedule("fire", delay);
	}

	@Override
	public void cleanup(){
		super.cleanup();
		CCTextureCache.sharedTextureCache().removeTexture("building/baleister/baleister_00.png");
		CCTextureCache.sharedTextureCache().removeTexture("building/baleister/baleister_01.png");
		CCTextureCache.sharedTextureCache().removeTexture("building/baleister/baleister_02.png");
		CCTextureCache.sharedTextureCache().removeTexture("building/baleister/baleister_03.png");
		CCTextureCache.sharedTextureCache().removeTexture("building/baleister/baleister_04.png");
		CCTextureCache.sharedTextureCache().removeTexture("building/baleister/baleister_05.png");
		CCTextureCache.sharedTextureCache().removeTexture("building/b_stone.png");
	}
	
	public void fire(float dt){
		if(this.respon >= this.delay){
			this.respon = this.delay;
			if(this.gameLayer.getArrowCnt() > 2){
				baleisterFire();
				this.respon = 0;
			}
		}
		else
			this.respon+=dt;
	}
	
	private void baleisterFire(){
		ArrayList<Enemy> enemyList = this.gameLayer.getEnemyList();
		
		if(enemyList.size() <= 0)
			return;
		
		Random rand = new Random();
		int target = rand.nextInt(enemyList.size());
		
		CGPoint targetPoint = null;
		int i = target;
		do{
			CGPoint point = enemyList.get(i).getPosition();
			if(point.y < rangeMax && point.y > rangeMin){
				targetPoint = point;
				break;
			}
			
			if(++i == enemyList.size())
				i = 0;
		}while(i != target);
		
		if(targetPoint == null)
			return;
		
		baleisterFireAction(targetPoint);		
	}
	
	private void baleisterFireAction(CGPoint targetPoint){
		this.gameLayer.updateArrowCnt(-4);
		
		Sound.engine.play(Sound.engine.S_BALEISTER);
		
		Wepon stone = new Wepon("building/b_stone.png", Wepon.TYPE_BALEISTER);
		CGPoint towerPoint = this.getPosition();
		CGSize towerSize = this.getContentSize();
		
		stone.damege = DAMEGE;
		stone.setAnchorPoint(0.5f, 0.5f);
		stone.setPosition(towerPoint.x, towerPoint.y + towerSize.width);
		this.gameLayer.addChild(stone, GameLayer.WEPON_Z);
		
		float speed = (this.gameLayer.arrowSpeed + 0.3f) * ((Global.TAGRETZONE_UNDER + targetPoint.y)/400 + 1);
		
		CCRotateBy rotate = CCRotateBy.action(speed, 360);
		stone.runAction(rotate);
		
		CCScaleTo scale1 = CCScaleTo.action(speed * 0.6f, 1.7f);
		CCScaleTo scale2 = CCScaleTo.action(speed * 0.4f, 0.7f);
		stone.runAction(CCSequence.actions(scale1, scale2));
		
		CCJumpTo actionMove = CCJumpTo.action(speed, CGPoint.ccp(targetPoint.x, targetPoint.y - 5), 300, 1);
		CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "fireFinish");
		stone.runAction(CCSequence.actions(actionMove, actionMoveDone));
		
		this.animate.setDuration(0.9f);
		this.runAction(this.animate);
	}
	
	public void fireFinish(Object sender){
		this.gameLayer.fireFinish(sender);
	}
	
	private void createShotAnimate(){
		CCAnimation animation = CCAnimation.animation("ax");
		animation.addFrame("building/baleister/baleister_00.png");
		animation.addFrame("building/baleister/baleister_01.png");
		animation.addFrame("building/baleister/baleister_02.png");
		animation.addFrame("building/baleister/baleister_03.png");
		animation.addFrame("building/baleister/baleister_04.png");
		animation.addFrame("building/baleister/baleister_05.png");
		animation.addFrame("building/baleister/baleister_05.png");
		animation.addFrame("building/baleister/baleister_05.png");
		animation.addFrame("building/baleister/baleister_05.png");

		CCAnimate animate = CCAnimate.action(animation);
		this.animate = animate;
	}
}
