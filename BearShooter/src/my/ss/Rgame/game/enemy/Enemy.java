package my.ss.Rgame.game.enemy;

import java.util.ArrayList;
import java.util.Random;

import my.ss.Rgame.Global.Global;
import my.ss.Rgame.game.GameLayer;
import my.ss.Rgame.sound.Sound;

import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCScaleTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;

public class Enemy extends CCSprite implements EnermyInterface{
	private int ACTION_PAT1_MOVE = 75;
	
	private final int ACTION_PAT2_ATTACK_BASE = 50;
	private final int ACTION_MAX = 100;
	
	private final float thinkDelay = 3.0f;
	protected final int MOVE_SIZE = 50;
	
	private int type;
	private boolean isReday;
	
	protected GameLayer gameLayer;
	private CCSprite lifeBar;
	private CCSprite thinkAttack;
	private CCSprite targetPoint;
	
	private int power;
	protected float life;	
	protected float maxLife;
	private int gold;
	
	protected int ATTACK_X_RANGE = 80;
	
	public static final int E_PIG = 0;
	public static final int E_GOLEM = 1;
	public static final int E_TROLL = 2;
	public static final int E_MAGE = 3;
	public static final int E_ELEPHANT = 4;
	public static final int E_DRAGON = 5;
	
	public Enemy(GameLayer gameLayer, String path, int type, float x, float y, int power, int life, int gold,int probability){
		super(path);
		this.type = type;
		this.gameLayer = gameLayer;
		this.setAnchorPoint(0.5f, 0.5f);
		this.setPosition(x, y);
		this.setScale(0.3f);
		this.setOpacity(0);
		this.power = power;
		this.life = life;
		this.maxLife = life;
		this.gold = gold;
		this.ACTION_PAT1_MOVE = probability;
		createLifeBar();
		createThinkAttack();
		startAction(x);
		createTargetPoint();
	}

	@Override
	public void cleanup(){
		CCTextureCache.sharedTextureCache().removeTexture("enemy/target_point_small.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/target_point.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/target_point_large.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/target_point_dragon.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/attack/e_lifebare_lifebar.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/think_attack.png");
		super.cleanup();
	}
	
	private void createThinkAttack(){
		int offset = 0;
		
		if(this.type == E_GOLEM)
			offset = -10;
		else if(this.type == E_ELEPHANT)
			offset = -15;
		
		CCSprite thinkAttack = CCSprite.sprite("enemy/think_attack.png");
		CGSize size = this.getContentSize();
		thinkAttack.setAnchorPoint(0.5f, 0);
		thinkAttack.setPosition(size.width/2, size.height + 10 + offset);
		thinkAttack.setVisible(false);
		this.addChild(thinkAttack, 2);
		this.thinkAttack = thinkAttack;
	}
	
	private void createTargetPoint(){
		String fileName = "enemy/target_point_small.png";
		float offset = 0;
		
		if(this.type == E_DRAGON){
			fileName = "enemy/target_point_dragon.png";
			offset = 5;
		}
		else if(this.type == E_ELEPHANT){
			fileName = "enemy/target_point_large.png";
			offset = 5;
		}
		else
			fileName = "enemy/target_point.png";
		
		CCSprite targetPoint = CCSprite.sprite(fileName);
		CGSize size = this.getContentSize();
		targetPoint.setAnchorPoint(0.5f, 0f);
		targetPoint.setPosition(size.width/ 2, offset);
		targetPoint.setOpacity(0);
		this.addChild(targetPoint);
		this.targetPoint = targetPoint; 
	}
	
	private void createLifeBar(){
		int offset = 0;
		
		if(this.type == E_GOLEM)
			offset = -10;
		else if(this.type == E_ELEPHANT)
			offset = -15;
		
		CCSprite lifeBar = CCSprite.sprite("enemy/e_lifebar.png");
		CGSize size = this.getContentSize();
		lifeBar.setAnchorPoint(0,0);
		lifeBar.setPosition((size.width - lifeBar.getContentSize().width) / 2, size.height + 5 + offset);
		this.addChild(lifeBar);
		this.lifeBar = lifeBar;
	}
	
	protected void startAction(float x){
		int offset;
		
		if(x < 25)
			offset = 15;
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
		else
			offset = 25;
		
		offset+=5;
		
		CGPoint point = this.getPosition();
		this.setPosition(point.x, point.y + offset);
		
		CCFadeIn fade = CCFadeIn.action(2.5f);
		CCMoveBy moveAction = CCMoveBy.action(5.5f, CGPoint.ccp(0, -10));
		CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "readyFinish");
		this.runAction(CCSequence.actions(fade, moveAction, actionMoveDone));
		
		CCScaleTo scale = CCScaleTo.action(8.0f, 1.0f);
		this.runAction(scale);
	}
	
	public void readyFinish(Object sender){
		ArrayList<Enemy> enemyList = this.gameLayer.getEnemyList();
		enemyList.add(this);
		this.isReday = true;		
		this.stopAllActions();
		this.schedule("think", thinkDelay);	
	}
	
	public boolean getIsReady(){
		return this.isReday;
	}
	
	public void think(float ft) {
		this.stopAllActions();
		Random ran = new Random();
		this.thinkAttack.setVisible(false);
		
		if(this.getPosition().y > 268){ //접근
			int action = ran.nextInt(ACTION_MAX);
			
			if(action < ACTION_PAT1_MOVE)
				move();
			else{
				if(this.type != E_ELEPHANT)
					this.thinkAttack.setVisible(true);
				attackPlayer();
			}
		}
		else{ //벽에 근접상태
			int action = ran.nextInt(ACTION_MAX);
			
			if(action < ACTION_PAT2_ATTACK_BASE)
				attackBase();
		}
	}

	public void move() {
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
		
		CCMoveBy moveAction = CCMoveBy.action(6.0f, CGPoint.ccp(x, y));
		this.runAction(moveAction);		
	}

	public void attackBase() {
		Sound.engine.play(Sound.engine.S_ATTACK_BASE);
		this.gameLayer.getWall().underAttack(this.power);
	}
	
	public boolean die(int damege) {
		this.thinkAttack.setVisible(false);
		
		this.life-=damege;
		if(this.life < 0)
			this.life = 0;
		
		updateLifeBar();
		
		if(this.life <= 0){
			this.stopAllActions();
			this.unscheduleAllSelectors();
			this.gameLayer.getEnemyList().remove(this);
			CCDelayTime delay = CCDelayTime.action(1.0f);
			CCFadeOut fade = CCFadeOut.action(2.0f);
			CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "dieFinish");
			this.runAction(CCSequence.actions(delay, fade, actionMoveDone));
			return true;
		}
		
		return false;
	}
	
	public void dieFinish(Object sender){
		this.stopAllActions();
		this.removeSelf();
		this.cleanup();		
	}
	
	protected void updateLifeBar(){
		this.lifeBar.setScaleX(this.life/this.maxLife);
	}
	
	public void attackPlayer() {
	
	}
	
	public int getGold(){
		return this.gold;
	}
	
	public int getType(){
		return this.type;
	}
	
	public boolean crash(float x, float y){
		CGPoint pos = this.getPosition();
		CGSize size = this.getContentSize();
		
		if(this.targetPoint.getBoundingBox().contains(x - (pos.x - size.width/2), y - (pos.y - size.height/2)))
			return true;
		
		return false;
	}
}
