package my.ss.Rgame.game.building;

import java.util.ArrayList;
import java.util.Random;

import my.ss.Rgame.Global.Global;
import my.ss.Rgame.game.GameLayer;
import my.ss.Rgame.game.enemy.Enemy;
import my.ss.Rgame.sound.Sound;
import my.ss.Rgame.wepon.Wepon;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.ease.CCEaseExponentialIn;
import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCJumpTo;
import org.cocos2d.actions.interval.CCRotateBy;
import org.cocos2d.actions.interval.CCScaleBy;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;

public class GuardTower extends CCSprite{
	private final float delay = 3.0f;
	private GameLayer gameLayer;

	private final float range = 380;
	private int damege;
	
	private float respon;
	
	public GuardTower(GameLayer gameLayer, String name, float x, float y, int damege){
		super(name);
		this.gameLayer = gameLayer;
		this.damege = damege;
		
		this.runAction(createAnimate(damege));
		this.setPosition(x, y);
		this.setAnchorPoint(0.5f, 0);
		this.schedule("fire", 0.1f);
		
	}
	
	@Override
	public void cleanup(){
		super.cleanup();
		CCTextureCache.sharedTextureCache().removeTexture("building/gtower/gtower_00.png");
		CCTextureCache.sharedTextureCache().removeTexture("building/gtower/gtower_01.png");
		CCTextureCache.sharedTextureCache().removeTexture("building/gtower/gtower_02.png");
		CCTextureCache.sharedTextureCache().removeTexture("building/gtower/gtower_stone_00.png");
		CCTextureCache.sharedTextureCache().removeTexture("building/gtower/gtower_stone_01.png");
		CCTextureCache.sharedTextureCache().removeTexture("building/gtower/gtower_stone_02.png");
		CCTextureCache.sharedTextureCache().removeTexture("building/tower_arrow.PNG");
	}
	
	private CCRepeatForever createAnimate(int type){
		CCAnimation animation = CCAnimation.animation("tower");
		if(type == 1){
			animation.addFrame("building/gtower/gtower_00.png");
			animation.addFrame("building/gtower/gtower_01.png");
			animation.addFrame("building/gtower/gtower_02.png");
		}
		else{
			animation.addFrame("building/gtower/gtower_stone_00.png");
			animation.addFrame("building/gtower/gtower_stone_01.png");
			animation.addFrame("building/gtower/gtower_stone_02.png");
		}

		Random rand = new Random();
		float speed = ((float)rand.nextInt(5))/10f + 0.5f;
		
		CCAnimate axAnimate = CCAnimate.action(animation);
		axAnimate.setDuration(speed);
			
		return CCRepeatForever.action(axAnimate);		
	}
	
	public void fire(float dt){
		if(this.respon >= this.delay){
			this.respon = this.delay;
			if(this.gameLayer.getArrowCnt() > damege-1){
				towerFireArrow();
				this.respon = 0;
			}
		}
		else
			this.respon+=dt;
	}
	

	public void towerFireArrow(){
		ArrayList<Enemy> enemyList = this.gameLayer.getEnemyList();
		
		if(enemyList.size() <= 0)
			return;
		
		Random rand = new Random();
		int target = rand.nextInt(enemyList.size());
		
		CGPoint targetPoint = null;
		int i = target;
		do{
			CGPoint point = enemyList.get(i).getPosition();
			if(point.y < range){
				targetPoint = point;
				break;
			}
			
			if(++i == enemyList.size())
				i = 0;
		}while(i != target);
		
		if(targetPoint == null)
			return;

		towerFireAction(targetPoint);	
	}
	
	private void towerFireAction(CGPoint targetPoint){
		Sound.engine.play(Sound.engine.S_TOWER);
		
		String arrowName = "wepon/arrow/tower_arrow.png";
		if(this.damege == 2)
			arrowName = "wepon/arrow/tower_arrow_up.png";
		
		Wepon arrow = new Wepon(arrowName, Wepon.TYPE_ARROW);
		CGPoint towerPoint = this.getPosition();
		CGSize towerSize = this.getContentSize();
		
		arrow.damege = this.damege;
		arrow.setAnchorPoint(0.5f, 0f);
		arrow.setPosition(towerPoint.x, towerPoint.y + towerSize.width);
		this.gameLayer.addChild(arrow, GameLayer.WEPON_Z);
		this.gameLayer.updateArrowCnt(-1);
		
		float speed = this.gameLayer.arrowSpeed * ((Global.TAGRETZONE_UNDER + targetPoint.y)/400 + 1);
		
		setArrowRotate(arrow, targetPoint.x, targetPoint.y);
		
		CCRotateBy rotate;
		float arrowRot = arrow.getRotation();
		if(arrowRot < 0)
			rotate= CCRotateBy.action(speed, -(160 - Math.abs(arrowRot)));
		else
			rotate= CCRotateBy.action(speed, 160 - Math.abs(arrowRot));
		
		CCEaseExponentialIn expAction = CCEaseExponentialIn.action(rotate);
		arrow.runAction(expAction);
		
		CCScaleBy scale = CCScaleBy.action(speed, 0.7f);
		arrow.runAction(scale);
		
		CCJumpTo actionMove = CCJumpTo.action(speed, CGPoint.ccp(targetPoint.x, targetPoint.y + 5), (targetPoint.y - towerPoint.y) * 0.5f , 1);
		CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "fireFinish");
		arrow.runAction(CCSequence.actions(actionMove, actionMoveDone));
	}
	
	public void fireFinish(Object sender){
		this.gameLayer.fireFinish(sender);
	}
	
	public void setArrowRotate(Wepon arrow, float x, float y){
		CGPoint arrowP = CGPoint.ccp(arrow.getPosition().x, 60);
		CGPoint touchP = CGPoint.ccp(x, y);
		
		float dx = arrowP.x - touchP.x;
		float dy = arrowP.y - touchP.y;
		double radius = Math.toDegrees(Math.atan(dy/dx));
		
		if(radius < 0)
			arrow.setRotation((float)(90.0f - Math.abs(radius)) * -1);
		else
			arrow.setRotation((float)(90.0f - Math.abs(radius)));
	}
}
