package my.ss.Rgame.game.character;

import java.util.ArrayList;
import java.util.Random;

import my.ss.Rgame.Global.Global;
import my.ss.Rgame.game.GameLayer;

import org.cocos2d.actions.CCProgressTimer;
import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCProgressTo;
import org.cocos2d.actions.interval.CCRotateBy;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGPoint;

public class CharacterLayer extends CCLayer{
	private ArrayList<CCSprite> charater;
	private CCSprite chaLife;
	private CCSprite topBg;
	
	private GameLayer gameLayer;
	private CCRepeatForever defaultAction;
	private CCSequence headRotateAction;
	
	private CCProgressTimer reloadTimer;
	
	private float life = 100;
	private final float lifeMax = 100;
	private boolean isDamege;
	
	private final int MOTION_BLOCK = 0;
	private final int MOTION_HEAD = 1;
	private final int MOTION_HEAD_FIRE = 2;
	private final int MOTION_NONE = 3;
	private final int MOTION_FIRE = 4;
	
	public CharacterLayer(GameLayer gameLayer){
		super();
		this.gameLayer = gameLayer;
		this.charater = new ArrayList<CCSprite>();
		init();
		this.schedule("lifeFix", 1.0f);
	}

	@Override
	public void cleanup(){
		super.cleanup();
		CCTextureCache.sharedTextureCache().removeTexture("character/cha_block.png");
		CCTextureCache.sharedTextureCache().removeTexture("character/cha_head_00.png");
		CCTextureCache.sharedTextureCache().removeTexture("character/cha_head_01.png");
		CCTextureCache.sharedTextureCache().removeTexture("character/cha_body_00.png");
		CCTextureCache.sharedTextureCache().removeTexture("character/cha_body_01.png");
		CCTextureCache.sharedTextureCache().removeTexture("character/cha_life.png");
		CCTextureCache.sharedTextureCache().removeTexture("character/cha_life_bar.png");
		CCTextureCache.sharedTextureCache().removeTexture("ui/ui_top.png");
		CCTextureCache.sharedTextureCache().removeTexture("ui/ui_top_stone.png");
		CCTextureCache.sharedTextureCache().removeTexture("character/reloaded.png");		
	}
	
	public void lifeFix(float dt){
		if(this.life < lifeMax){
			this.life += 0.25f;
			this.updateLifeBar();
		}
	}
	
	private void init(){
		CCSprite topBg = CCSprite.sprite("ui/ui_top.png");
		topBg.setAnchorPoint(0,1.0f);
		topBg.setPosition(0, Global.DEFAULT_HEIGHT);
		this.addChild(topBg, 0);
		this.topBg = topBg;
		
		
		CCSprite block = CCSprite.sprite("character/cha_block.png");
		block.setAnchorPoint(0.5f, 0);
		block.setPosition(Global.DEFAULT_WIDTH/2, 0);
		this.addChild(block, 2);
		this.charater.add(block);

		CCSprite chaHead = CCSprite.sprite("character/cha_head_00.png");
		chaHead.setAnchorPoint(0.5f, 0.5f);
		chaHead.setPosition(52, 65);
		block.addChild(chaHead, 0);
		this.charater.add(chaHead);
		
		CCSprite chaHead2 = CCSprite.sprite("character/cha_head_01.png");
		chaHead2.setAnchorPoint(0, 0);
		chaHead2.setPosition(0, 0);
		chaHead2.setVisible(false);
		block.addChild(chaHead2, 0);
		this.charater.add(chaHead2);
		
		CCSprite chaBody1 = CCSprite.sprite("character/cha_body_00.png");
		chaBody1.setAnchorPoint(0, 0);
		chaBody1.setPosition(0, 0);
		block.addChild(chaBody1, -1);
		this.charater.add(chaBody1);
		
		CCSprite chaBody2 = CCSprite.sprite("character/cha_body_01.png");
		chaBody2.setAnchorPoint(0, 0);
		chaBody2.setPosition(0, 0);
		chaBody2.setVisible(false);
		block.addChild(chaBody2, -1);
		this.charater.add(chaBody2);
		
		CCSprite chaLife = CCSprite.sprite("character/cha_life.png");
		chaLife.setAnchorPoint(0, 1);
		chaLife.setPosition(45, Global.DEFAULT_HEIGHT - 13);
		this.addChild(chaLife, 1);
		this.chaLife = chaLife;
		
		CCSprite chaLifeBar = CCSprite.sprite("character/cha_life_bar.png");
		chaLifeBar.setAnchorPoint(0, 1);
		chaLifeBar.setPosition(5, Global.DEFAULT_HEIGHT - 5);
		this.addChild(chaLifeBar, 2);
		
		reloadTimer();
		createRandomAction();
		createDefaultAction();
		this.charater.get(MOTION_BLOCK).runAction(this.defaultAction);
	}
	
	public void setShotMotion(boolean set){
		if(set){
			this.charater.get(MOTION_HEAD).setVisible(false);
			this.charater.get(MOTION_HEAD_FIRE).setVisible(true);
			this.charater.get(MOTION_NONE).setVisible(false);
			this.charater.get(MOTION_FIRE).setVisible(true);
		}
		else{
			this.charater.get(MOTION_HEAD).setVisible(true);
			this.charater.get(MOTION_HEAD_FIRE).setVisible(false);
			this.charater.get(MOTION_NONE).setVisible(true);
			this.charater.get(MOTION_FIRE).setVisible(false);
		}
	}
	
	public void createDefaultAction(){
		CCMoveBy moveTop = CCMoveBy.action(1.5f, CGPoint.ccp(0, -3));
		CCMoveBy moveBack = CCMoveBy.action(1.2f, CGPoint.ccp(0, 3));
		CCDelayTime delay = CCDelayTime.action(0.5f);
		CCSequence action = CCSequence.actions(moveTop, moveBack, delay);
		this.defaultAction = CCRepeatForever.action(action);
	}
	
	public void createRandomAction(){
		CCRotateBy rotateLeft = CCRotateBy.action(0.2f, -10);
		CCRotateBy rotateRight = CCRotateBy.action(0.4f, 20);
		CCRotateBy rotateMid = CCRotateBy.action(0.2f, -10);
		CCSequence action = CCSequence.actions(rotateLeft, rotateRight, rotateMid);
		this.headRotateAction = action;
	}
	
	public void actionStop(boolean isStop){
		if(isStop){
			CGPoint defalutPos = this.charater.get(MOTION_BLOCK).getPosition();
			this.charater.get(MOTION_BLOCK).setPosition(defalutPos.x, 0);
			this.charater.get(MOTION_BLOCK).stopAllActions();
			
			this.charater.get(MOTION_HEAD).stopAllActions();
			this.charater.get(MOTION_HEAD).setRotation(0);
		}
		else{
			if(new Random().nextInt(5) < 2)
				this.charater.get(MOTION_HEAD).runAction(this.headRotateAction);
			this.charater.get(MOTION_BLOCK).runAction(this.defaultAction);
		}
	}
	
	public void setMove(float pos){
		this.charater.get(MOTION_BLOCK).setPosition(pos, 0);
	}
	
	public boolean crash(CGPoint point, float offset){
		if(this.charater.get(MOTION_BLOCK).getBoundingBox().contains(point.x, point.y + offset))
			return true;
		
		return false;
	}
	
	public void die(int damege){
		if(this.isDamege)
			return;
		
		this.life -= (float)damege;
		if(this.life < 0)
			this.life = 0;
		updateLifeBar();
		
		if(this.life <= 0)
			gameOver();
		else{
			setOpacity(100);
			this.isDamege = true;
			
			CCDelayTime actionDelay = CCDelayTime.action(1.5f);
			CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "opacityFinish");
			this.runAction(CCSequence.actions(actionDelay, actionMoveDone));
		}
	}
	
	public void opacityFinish(Object sender){
		setOpacity(255);
		this.isDamege = false;
	}
	
	public void setOpacity(int opacity){
		ArrayList<CCSprite> list = this.charater;
		
		for(int i=0; i<list.size(); i++)
			list.get(i).setOpacity(opacity);
	}
	
	private void gameOver(){
		this.gameLayer.gameOver();
	}
	
	private void updateLifeBar(){
		this.chaLife.setScaleX(this.life/lifeMax);
		
	}
	
	public void upgradeTop(){
		CCSprite topBg = CCSprite.sprite("ui/ui_top_stone.png");
		topBg.setAnchorPoint(0,1.0f);
		topBg.setPosition(0, Global.DEFAULT_HEIGHT);
		this.addChild(topBg, 0);
		this.removeChild(this.topBg, true);
		this.topBg = topBg;
		
	}
	
	private void reloadTimer(){
		CCProgressTimer timer = CCProgressTimer.progress("character/reloaded.png");
		timer.setPosition(50, 140);		
		timer.runAction(createMakeAction(0.01f));
		this.charater.get(MOTION_BLOCK).addChild(timer);
		this.reloadTimer = timer;
	}
	
	private CCProgressTo createMakeAction(float dt){
		CCProgressTo progress = CCProgressTo.action(dt, 100);
		return progress;
	}
	
	public void reloaded(float dt){
		this.reloadTimer.runAction(createMakeAction(dt));
	}	
}
