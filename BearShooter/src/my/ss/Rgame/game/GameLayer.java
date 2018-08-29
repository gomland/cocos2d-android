package my.ss.Rgame.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import my.ss.Rgame.MainActivity;
import my.ss.Rgame.SceneManager;
import my.ss.Rgame.Global.Global;
import my.ss.Rgame.game.building.Baleister;
import my.ss.Rgame.game.building.Factory;
import my.ss.Rgame.game.building.GuardTower;
import my.ss.Rgame.game.building.Wall;
import my.ss.Rgame.game.character.CharacterLayer;
import my.ss.Rgame.game.enemy.Dragon;
import my.ss.Rgame.game.enemy.Elephant;
import my.ss.Rgame.game.enemy.Enemy;
import my.ss.Rgame.game.enemy.EnemyWepon;
import my.ss.Rgame.game.enemy.Golem;
import my.ss.Rgame.game.enemy.Mage;
import my.ss.Rgame.game.enemy.Pig;
import my.ss.Rgame.game.enemy.Troll;
import my.ss.Rgame.game.item.Guard;
import my.ss.Rgame.game.itemShop.ItemShopLayer;
import my.ss.Rgame.game.timer.ArTimer;
import my.ss.Rgame.news.NewsLayer;
import my.ss.Rgame.sound.Sound;
import my.ss.Rgame.wepon.CharacterWepon;
import my.ss.Rgame.wepon.Wepon;

import org.cocos2d.actions.ease.CCEaseExponentialIn;
import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCJumpTo;
import org.cocos2d.actions.interval.CCScaleTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGPoint;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.MotionEvent;

public class GameLayer extends CCLayer{
	private float gameDelay = 4.5f;
	private MainActivity act;
	private UILayer uiLayer;
	private CharacterLayer chaLayer;
	private StageManager stageManager;
	private ArTimer timer;
	private NewsLayer popup;
	
	private Wall wall;
	private Factory factory;
	private ArrayList<GuardTower> gTowerList;
	private ArrayList<Baleister> baleisterList;
	private CharacterWepon chaWepon;
	
	private Guard guardL;
	private Guard guardM;
	private Guard guardR;
	
	private ArrayList<Wepon> arrows;
	private ArrayList<EnemyWepon> enemyAttackList;
	private ArrayList<Enemy> enemyList;
	private ArrayList<CCSprite> uiBottom;
	
	public static final int POS_LEFT = 80;
	public static final int POS_MID = 240;
	public static final int POS_RIGHT = 400;
	
	public final float arrowSpeed = 0.3f;
	private final float AIM_SIZE = 50;
	private final int Y_OFFSET = 13;
	
	private final int TAG_FACTORY = 123;
	
	public static final int DEFAULT_Z = 0;
	public static final int UI_Z = 1;
	public static final int ENEMY_Z = 2;
	public static final int WALL_Z = 3;
	public static final int BUILD_Z = 4;
	public static final int GUARD_Z = 5;	
	public static final int ARROW_Z = 6;
	public static final int SPRITE_Z = 7;
	public static final int WEPON_Z = 8;
		
	private final float weponPosY = 70;
	
	private boolean isChaMove;
	
	private float fireInterval;
	
	public GameLayer(MainActivity act, UILayer uiLayer){
		super();
		this.act = act;
		this.uiLayer =uiLayer;
		init();
		Sound.engine.stopSound();
		Sound.engine.play(Sound.engine.S_DRUM);
		Sound.engine.playBGM("game_bgm.mp3", true);
	}
	
	@Override
	public void onExit(){
		super.onExit();
		Global.WEPON_2_OPEN = false;
		Global.WEPON_3_OPEN = false;
		Global.SELECT_WEPON = 0;
	}
	
	@Override
	public void cleanup(){
		CCTextureCache.sharedTextureCache().removeTexture("wepon/arrow/arrow_0.png");
		CCTextureCache.sharedTextureCache().removeTexture("wepon/arrow/arrow_0.png");
		CCTextureCache.sharedTextureCache().removeTexture("wepon/arrow/arrow_1.png");
		CCTextureCache.sharedTextureCache().removeTexture("wepon/arrow/arrow_2.png");
		CCTextureCache.sharedTextureCache().removeTexture("wepon/arrow/arrow_3.png");
		CCTextureCache.sharedTextureCache().removeTexture("wepon/arrow/arrow_4.png");
		CCTextureCache.sharedTextureCache().removeTexture("wepon/arrow/arrow_5.png");
		CCTextureCache.sharedTextureCache().removeTexture("wepon/arrow/sling.png");
		CCTextureCache.sharedTextureCache().removeTexture("ui/bg.png");
		CCTextureCache.sharedTextureCache().removeTexture("ui/ui_bottom.png");
		CCTextureCache.sharedTextureCache().removeTexture("ui/ui_bottom_stone.png");
		CCTextureCache.sharedTextureCache().removeAllTextures();

		super.cleanup();
	}
	
	private void init(){
		this.setIsTouchEnabled(true);

		this.stageManager = new StageManager(this);
		this.arrows = new ArrayList<Wepon>();
		this.enemyList = new ArrayList<Enemy>();
		this.enemyAttackList = new ArrayList<EnemyWepon>();
		this.gTowerList = new ArrayList<GuardTower>();
		this.baleisterList = new ArrayList<Baleister>();
		this.uiBottom = new ArrayList<CCSprite>();
		
		createTimer();
		createBuilding();
		createBg();
		createDefaultArrow();
		createWall();
		
		this.uiLayer.setGoldCnt(0);
		
		this.chaLayer = new CharacterLayer(this);
		this.chaLayer.setTag(SceneManager.TAG_CCLAYER);
		this.addChild(this.chaLayer, SPRITE_Z);
		
		this.schedule("gameTimer", gameDelay);
		this.schedule("crashChecker", 1.0f / 60.0f);
		this.schedule("fireChecker", 0.05f);
		
	}
	
	private void createTimer(){
		ArTimer timer = new ArTimer();
		timer.setAnchorPoint(0, 1);
		timer.setPosition(Global.DEFAULT_WIDTH - 80, Global.DEFAULT_HEIGHT-95);
		timer.setTag(SceneManager.TAG_CCLAYER);
		this.addChild(timer, 100);
		this.timer = timer;
	}
	
	private void createBg(){
		CCSprite bg = CCSprite.sprite("ui/bg.png");
		bg.setAnchorPoint(0,0);
		bg.setPosition(0, 0);
		this.addChild(bg, DEFAULT_Z);
		
		CCSprite uiBottom = CCSprite.sprite("ui/ui_bottom.png");
		uiBottom.setAnchorPoint(0,0);
		uiBottom.setPosition(0, 0);
		this.addChild(uiBottom, SPRITE_Z);
		this.uiBottom.add(uiBottom);
		
		CCSprite uiBottomStone = CCSprite.sprite("ui/ui_bottom_stone.png");
		uiBottomStone.setAnchorPoint(0,0);
		uiBottomStone.setPosition(0, 0);
		uiBottomStone.setVisible(false);
		this.addChild(uiBottomStone, SPRITE_Z);
		this.uiBottom.add(uiBottomStone);
	}
	
	private void createWall(){
		Wall wall = new Wall(this);
		wall.setPosition(0, 174);
		wall.setTag(SceneManager.TAG_CCLAYER);
		this.addChild(wall, WALL_Z);
		this.wall = wall;
	}
	
	private void createBuilding(){
		this.factory = new Factory(this.uiLayer);
		this.factory.setTag(TAG_FACTORY);
		this.addChild(this.factory, BUILD_Z);
	}
	
	public int getFactorySpeed(){
		return this.factory.getSpeedLevel();
	}
	
	public void factorySpeedUp(){
		this.factory.speedUp();
	}

	public void wallUpgrade(){
		if(this.wall.getWallType() == Wall.TYPE_WOOD){
			changeWallType(Wall.TYPE_STONE);
			this.chaLayer.upgradeTop();
			
			ArrayList<GuardTower> gTowerList = this.gTowerList;
			
			for(int i=0; i<gTowerList.size(); i++){
				GuardTower oldTower = gTowerList.get(i);
				CGPoint oldPoint = oldTower.getPosition();
				GuardTower gaurdTower = new GuardTower(this, "building/gtower_stone.png", oldPoint.x, oldPoint.y, 2);
				
				this.removeChild(oldTower, true);
				gTowerList.remove(i);
				
				this.addChild(gaurdTower, BUILD_Z);
				gTowerList.add(i, gaurdTower);
			}
		}
	}
	
	public int getWallType(){
		return this.wall.getWallType();
	}
	
	
	public void resetGameTimer(){
		if(this.gameDelay > 1.0f){
			this.unschedule("gameTimer");
			this.gameDelay -= 0.6f;
			this.schedule("gameTimer", this.gameDelay);
		}
	}
	
	public void gameTimer(float dt){
		this.stageManager.action();
	}
	
	public void fireChecker(float dt){
		float weponInterval = this.chaWepon.getAttackSpeed();
		
		if(this.fireInterval >= weponInterval)
			this.fireInterval = weponInterval;
		else
			this.fireInterval += dt;
	}
	
	public void crashChecker(float dt){
		ArrayList<EnemyWepon> enemyAttackList = this.enemyAttackList;
		CharacterLayer chaLayer = this.chaLayer;
		CGPoint point;
		EnemyWepon attack;
		
		for(int i=0; i<enemyAttackList.size(); i++){
			attack = enemyAttackList.get(i);
			point = attack.getPosition();
				
			if(point.y < 200){
				boolean isGuard = false;
				
				if(this.guardL != null && this.guardL.crash(point, attack.getType())){
					this.guardL.crashAction(attack.getType());
					isGuard = true;
				}
				if(this.guardM != null && this.guardM.crash(point, attack.getType())){
					this.guardM.crashAction(attack.getType());
					isGuard = true;
				}
				if(this.guardR != null && this.guardR.crash(point, attack.getType())){
					this.guardR.crashAction(attack.getType());
					isGuard = true;
				}
				
				if(isGuard){
					enemyAttackList.remove(attack);
					attack.removeSelf();
				}
				
				if(!isGuard && chaLayer.crash(point, 40)){
					chaLayer.die(attack.getDamege());
				}
			}
		}
	}

	public void enemyCreate(int type, int probability){
		Random ran = new Random();
		int x = ran.nextInt((int)Global.DEFAULT_WIDTH);
		
		Enemy enemy = null;
		//enemy type 1 : power (1), life (3) 
		switch(type){
		case Enemy.E_PIG:
			enemy = new Pig(this, x, Global.DEFAULT_HEIGHT - 250, 2, 1, 1, probability);
			Sound.engine.play(Sound.engine.S_PIG);
			break;
		case Enemy.E_GOLEM:
			enemy = new Golem(this, x, Global.DEFAULT_HEIGHT - 250, 3, 2, 2, probability);
			Sound.engine.play(Sound.engine.S_GOLEM);
			break;
		case Enemy.E_TROLL:
			enemy = new Troll(this, x, Global.DEFAULT_HEIGHT - 250, 2, 3, 3, probability);
			Sound.engine.play(Sound.engine.S_TROLL);
			break;
		case Enemy.E_MAGE:
			enemy = new Mage(this, x, Global.DEFAULT_HEIGHT - 250, 2, 3, 3, probability);
			Sound.engine.play(Sound.engine.S_MAGE);
			break;
		case Enemy.E_ELEPHANT:
			enemy = new Elephant(this, x, Global.DEFAULT_HEIGHT - 250, 10, 20, 6, probability);
			Sound.engine.play(Sound.engine.S_ELEPHANT);
			break;		
		case Enemy.E_DRAGON:
			enemy = new Dragon(this, x, Global.DEFAULT_HEIGHT - 250, 6, 50, 15, probability);
			Sound.engine.play(Sound.engine.S_DRAGON);
			break;		
		}
		if(enemy != null)
			this.addChild(enemy, ENEMY_Z);
	}
	
	private void createDefaultArrow(){
		this.chaWepon = new CharacterWepon(this, CharacterWepon.TYPE_SLING_SHOT); 
		this.chaWepon.setPosition(Global.DEFAULT_WIDTH/2, weponPosY);
		this.chaWepon.setTag(SceneManager.TAG_CCLAYER);
		this.addChild(this.chaWepon, SPRITE_Z);
	}
	
	private Wepon createArrow(){
		Wepon arrow = new Wepon("wepon/arrow/arrow_6.png", Wepon.TYPE_ARROW);
		arrow.damege = CharacterWepon.DAMEGE_BOW_GUN;
		arrow.setAnchorPoint(0.5f, 1.0f);
		arrow.setPosition(this.chaWepon.getPosition().x + 30, weponPosY);
		this.arrows.add(arrow);
		this.addChild(arrow, ARROW_Z);
		
		Sound.engine.play(Sound.engine.S_BOW_GUN);
		
		return arrow;
	}
	
	private Wepon createSling(){
		Wepon arrow = new Wepon("wepon/arrow/sling.png", Wepon.TYPE_SLING);
		arrow.damege = CharacterWepon.DAMEGE_SLING_SHOT;
		arrow.setAnchorPoint(0.5f, 0.5f);
		arrow.setPosition(this.chaWepon.getPosition().x + 30, weponPosY);
		this.arrows.add(arrow);
		this.addChild(arrow, ARROW_Z);
		
		Sound.engine.play(Sound.engine.S_SLING_SHOT);
		
		return arrow;
	}
	
	private Wepon createSlug(){
		Wepon arrow = new Wepon("wepon/arrow/sling.png", Wepon.TYPE_GUN);
		arrow.damege = CharacterWepon.DAMEGE_GUN;
		arrow.setAnchorPoint(0.5f, 0.5f);
		arrow.setPosition(this.chaWepon.getPosition().x + 30, weponPosY);
		this.arrows.add(arrow);
		this.addChild(arrow, ARROW_Z);
		
		Sound.engine.play(Sound.engine.S_GUN);
		
		return arrow;
	}
	
	private CCAnimate createArrowAnimate(float speed){
		CCAnimation animation = CCAnimation.animation("arrow");
		animation.addFrame("wepon/arrow/arrow_0.png");
		animation.addFrame("wepon/arrow/arrow_1.png");
		animation.addFrame("wepon/arrow/arrow_2.png");
		animation.addFrame("wepon/arrow/arrow_3.png");
		animation.addFrame("wepon/arrow/arrow_4.png");
		animation.addFrame("wepon/arrow/arrow_5.png");
		animation.addFrame("wepon/arrow/arrow_6.png");
		
		CCAnimate arrowAnimate = CCAnimate.action(animation);
		arrowAnimate.setDuration(speed);
		
		return arrowAnimate;
	}
	
	public boolean ccTouchesBegan(MotionEvent event){
		CGPoint touchPoint = this.convertTouchToNodeSpace(event);
		
		this.chaLayer.actionStop(true);
		
		if(this.chaLayer.crash(touchPoint, 0)){
			this.isChaMove = true;
			if(touchPoint.x > 50 && touchPoint.x < 430){
				this.chaWepon.setPosition(touchPoint.x, this.weponPosY);
				this.chaLayer.setMove(touchPoint.x);
			}
		}
		else if(this.uiLayer.isTargetZone(touchPoint.y, this.chaWepon.getType())){
			setArrowRotate(this.chaWepon.getSelectWepon(), touchPoint.x, touchPoint.y);
			
			this.uiLayer.setAimEnable(touchPoint, true);
			this.chaWepon.setVisibleRange(true);
			
			this.chaWepon.shotReady(true);
			
			CGPoint point = getEnemy(touchPoint.x, touchPoint.y);
			if(point != null)
				this.uiLayer.setAim(point, touchPoint.x, touchPoint.y + Y_OFFSET);
		}
		
		return true;
	}
	
	public boolean ccTouchesMoved(MotionEvent event){
		CGPoint touchPoint = this.convertTouchToNodeSpace(event);
		
		if(this.isChaMove == true){
			if(touchPoint.x > 50 && touchPoint.x < 430){
				this.chaWepon.setPosition(touchPoint.x, this.weponPosY);
				this.chaLayer.setMove(touchPoint.x);
			}
		}
		else if(this.uiLayer.isTargetZone(touchPoint.y, this.chaWepon.getType())){
			setArrowRotate(this.chaWepon.getSelectWepon(), touchPoint.x, touchPoint.y);
			setArrowRotate(this.chaWepon.getReadyWepon(), touchPoint.x, touchPoint.y);
			
			this.chaLayer.setShotMotion(true);
			
			CGPoint point = getEnemy(touchPoint.x, touchPoint.y);
			if(point != null){
				this.uiLayer.setAim(point, touchPoint.x, touchPoint.y);
			}
			
			this.uiLayer.setAimEnable(touchPoint, true);
			this.chaWepon.setVisibleRange(true);
			this.chaWepon.shotReady(true);
		}
		else{
			this.chaLayer.setShotMotion(false);
			this.uiLayer.setAimEnable(null, false);
			this.chaWepon.setVisibleRange(false);
			this.chaWepon.shotReady(false);
		}
		
		return true;
	}

	public boolean ccTouchesEnded(MotionEvent event){
		CGPoint touchPoint = this.convertTouchToNodeSpace(event);
		
		if(this.isChaMove)
			this.isChaMove = false;
		else if(this.uiLayer.isTargetZone(touchPoint.y, this.chaWepon.getType()))
			fire(touchPoint.x, touchPoint.y);
		
		this.uiLayer.setAimEnable(null, false);
		this.chaWepon.setVisibleRange(false);
		this.chaLayer.setShotMotion(false);
		this.chaLayer.actionStop(false);
		this.chaWepon.shotReady(false);
		
		return true;
	}
	
	private CGPoint getEnemy(float x, float y){
		ArrayList<Enemy> enemyList = this.enemyList;
		float desX, desY, destance = 1000, buf;
		CGPoint returnPoint = null;
		
		for(int i=0; i<enemyList.size();i++){
			CGPoint point = enemyList.get(i).getPosition();
			if(point.x-AIM_SIZE <= x 
					&& point.x+AIM_SIZE >= x
					&&point.y-AIM_SIZE <= y 
					&& point.y+AIM_SIZE >= y){
				desX = x - point.x;
				desY = y - point.y;
				buf = (float)Math.sqrt(desX*desX + desY*desY);
				if(buf < destance){
					destance = buf;
					returnPoint = point;
				}
				
			}
		}
		
		return returnPoint;
	}
	
	public void setArrowRotate(CCSprite arrow, float x, float y){
		CGPoint arrowP = CGPoint.ccp(this.chaWepon.getPosition().x, weponPosY);
		CGPoint touchP = CGPoint.ccp(x, y);
		
		float dx = arrowP.x - touchP.x;
		float dy = arrowP.y - touchP.y;
		double radius = Math.toDegrees(Math.atan(dy/dx));
		
		if(radius < 0)
			arrow.setRotation(((float)(90.0f - Math.abs(radius)) * -1) /4.0f);
		else
			arrow.setRotation((float)(90.0f - Math.abs(radius))/4.0f);
	}
	
	private void fire(float x, float y){
		float interval = this.chaWepon.getAttackSpeed();
		
		if(this.fireInterval < interval)
			return ;
		
		this.fireInterval = 0;
		
		float speed = this.arrowSpeed * ((Global.TAGRETZONE_UNDER + y)/300 + 1);
		int weponType = this.chaWepon.getType();
		int arrowCnt = this.uiLayer.getCnt();
		
		if(weponType == CharacterWepon.TYPE_SLING_SHOT &&  arrowCnt == CharacterWepon.DAMEGE_SLING_SHOT-1)
			return;
		else if(weponType == CharacterWepon.TYPE_BOW_GUN && arrowCnt <= CharacterWepon.DAMEGE_BOW_GUN-1)
			return;
		else if(weponType == CharacterWepon.TYPE_GUN && arrowCnt <= CharacterWepon.DAMEGE_GUN-1)
			return;
		
		this.chaLayer.reloaded(interval);
		
		Wepon arrow = null;
		int subCnt = 0;
		
		switch(weponType){
		case CharacterWepon.TYPE_SLING_SHOT:
			arrow = createSling();
			subCnt = arrow.damege;
			arrow.setScale(1.2f);
			CCJumpTo move = CCJumpTo.action(speed * 0.9f, CGPoint.ccp(x, y), y - 170, 1);
			CCCallFuncN moveDone = CCCallFuncN.action(this, "fireFinish");
			arrow.runAction(CCSequence.actions(move, moveDone));
			
			CCScaleTo scale = CCScaleTo.action(speed * 0.8f, 0.2f);
			CCEaseExponentialIn expAction = CCEaseExponentialIn.action(scale);
			arrow.runAction(expAction);
			
			break;
		case CharacterWepon.TYPE_BOW_GUN:
			arrow = createArrow();
			subCnt = 2;
			arrow.runAction(createArrowAnimate(speed*1.2f));
			arrow.setRotation(this.chaWepon.getRotation()/2);
			CCJumpTo actionMove = CCJumpTo.action(speed, CGPoint.ccp(x, y + Y_OFFSET), y - 100, 1);
			arrow.runAction(actionMove);
			
			CCDelayTime actionDelay = CCDelayTime.action(speed*0.85f);
			CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "roatedFinish");
			CCDelayTime actionDelay2 = CCDelayTime.action(speed*0.15f);
			CCCallFuncN actionMoveDone2 = CCCallFuncN.action(this, "fireFinish");
			arrow.runAction(CCSequence.actions(actionDelay, actionMoveDone, actionDelay2, actionMoveDone2));
			
			break;
		case CharacterWepon.TYPE_GUN:
			arrow = createSlug();
			subCnt = 4;
			arrow.setScale(0.5f);
			CCJumpTo gunMove = CCJumpTo.action(0.3f, CGPoint.ccp(x, y), 50, 1);
			CCCallFuncN gunMoveDone = CCCallFuncN.action(this, "fireFinish");
			arrow.runAction(CCSequence.actions(gunMove, gunMoveDone));
			
			CCScaleTo gunScale = CCScaleTo.action(speed * 0.3f, 0.2f);
			CCEaseExponentialIn gunExpAction = CCEaseExponentialIn.action(gunScale);
			arrow.runAction(gunExpAction);
			break;
		}	
		
		if(arrow != null){
			updateArrowCnt(-subCnt);
			this.chaWepon.shot();
		}
	}
	
	public void roatedFinish(Object sender){
		Wepon arrow = (Wepon) sender;
		
		float rot = (arrow.getRotation() * -1)/3;
		arrow.setRotation(rot);
	}
	
	public void fireFinish(Object sender){
		Wepon arrow = (Wepon) sender;

		CCFadeOut fade = CCFadeOut.action(3.0f);
		CCCallFuncN done = CCCallFuncN.action(this, "fadeFinish");
		arrow.runAction(CCSequence.actions(fade, done));
		
		checkArrowCrash(arrow);
	}
	
	public void fadeFinish(Object sender){
		Wepon arrow = (Wepon) sender;
		this.arrows.remove(arrow);
		arrow.removeSelf();
		arrow.cleanup();
		arrow = null;
	}
	
	private void checkArrowCrash(Wepon arrow){
		ArrayList<Enemy> enemyList = this.enemyList;
		CGPoint point = arrow.getPosition();
		int offset;
		
		for(int i=0; i<enemyList.size(); i++){
			Enemy enemy = enemyList.get(i);
			
			if(arrow.getType() == Wepon.TYPE_BALEISTER)
				offset = 5;
			else if(arrow.getType() == Wepon.TYPE_SLING || arrow.getType() == Wepon.TYPE_GUN)
				offset = 0;
			else
				offset = Y_OFFSET;

			if(enemy.crash(point.x, point.y - offset)){
				Sound.engine.play(Sound.engine.S_CRASH);
				if(enemy.die(arrow.damege))	
					this.uiLayer.setGoldCnt(enemy.getGold());
			}
		}
	}
	
	public void addAttackList(EnemyWepon obj){
		this.enemyAttackList.add(obj);
	}
	
	public void removeAttackList(CCSprite obj){
		this.enemyAttackList.remove(obj);
	}
	
	public ArrayList<Enemy> getEnemyList(){
		return this.enemyList;
	}
	
	public void updateArrowCnt(int cnt){
		this.uiLayer.setCnt(cnt);
	}
	
	public int getArrowCnt(){
		return this.uiLayer.getCnt();
	}
	
	public Wall getWall(){
		return this.wall;
	}
	
	public void pauseAllAction(){
		this.setIsTouchEnabled(false);
		
		this.pauseSchedulerAndActions();
		List<CCNode> child = this.getChildren();
		
		this.timer.pauseTimer(true);
		
		for(int i=0; i<child.size(); i++){
			int tag = child.get(i).getTag();
			if(tag != SceneManager.TAG_CCLAYER){
				if(tag == TAG_FACTORY){
					List<CCNode> childOf = child.get(i).getChildren();
					for(int j=0; j<childOf.size(); j++)
						childOf.get(j).pauseSchedulerAndActions();
				}
				((CCSprite)child.get(i)).pauseSchedulerAndActions();
			}
		}
	}
	
	public void resumeAllAction(){
		this.setIsTouchEnabled(true);
		this.popup = null;
		this.resumeSchedulerAndActions();
		
		this.timer.pauseTimer(false);
		
		List<CCNode> child = this.getChildren();
		for(int i=0; i<child.size(); i++){
			int tag = child.get(i).getTag();
			if(tag != SceneManager.TAG_CCLAYER){
				if(tag == TAG_FACTORY){
					List<CCNode> childOf = child.get(i).getChildren();
					for(int j=0; j<childOf.size(); j++)
						childOf.get(j).resumeSchedulerAndActions();
				}
				((CCSprite)child.get(i)).resumeSchedulerAndActions();
			}
		}
	}
	
	public void build(int type, CGPoint pos){
		switch(type){
		case ItemShopLayer.ITEM_TYPE_GTOWER :
			GuardTower gaurdTower = null;
			if(this.wall.getWallType() == Wall.TYPE_STONE)
				gaurdTower = new GuardTower(this, "building/gtower_stone.png", pos.x - Global.BUILD_OFFSET, 202, 2);
			else
				gaurdTower = new GuardTower(this, "building/gtower.png", pos.x - Global.BUILD_OFFSET, 202, 1);
			
			this.addChild(gaurdTower, BUILD_Z);
			this.gTowerList.add(gaurdTower);
			gaurdTower.pauseSchedulerAndActions();
			Sound.engine.play(Sound.engine.S_BUILD);
			break;
		case ItemShopLayer.ITEM_TYPE_BALEISTER :
			Baleister baleister = new Baleister(this, pos.x - Global.BUILD_OFFSET, 100 + (pos.y%10*3));
			this.addChild(baleister, BUILD_Z);
			this.baleisterList.add(baleister);
			baleister.pauseSchedulerAndActions();
			Sound.engine.play(Sound.engine.S_BUILD);
			break;
		case ItemShopLayer.ITEM_TYPE_GUARD_WOOD:
		case ItemShopLayer.ITEM_TYPE_GUARD_STONE:
		case ItemShopLayer.ITEM_TYPE_GUARD_STEEL:
			Sound.engine.play(Sound.engine.S_BUILD);
			createGuard(type, pos.x);
			break;
		}
	}
	
	private void createGuard(int type, float x){
		String fileName;
		int guardType;
		
		if(type == ItemShopLayer.ITEM_TYPE_GUARD_WOOD){
			fileName = "guard/guard_wood.png";
			guardType = Guard.TYPE_WOOD;
		}
		else if(type == ItemShopLayer.ITEM_TYPE_GUARD_STEEL){
			fileName = "guard/guard_steel.png";
			guardType = Guard.TYPE_STEEL;
		}
		else{
			fileName = "guard/guard_stone.png";
			guardType = Guard.TYPE_STONE;
		}
		
		Guard guard = new Guard(this, fileName, guardType, x, 40);
		this.addChild(guard, GUARD_Z);
		
		if(x == Guard.X_POS_L)
			this.guardL = guard;
		else if(x == Guard.X_POS_M)
			this.guardM = guard;
		else
			this.guardR = guard;
	}

	public Guard guard_State(int pos, boolean cleanup){
		switch(pos){
		case 0:
			if(cleanup)
				this.guardL = null;
			
			return this.guardL;
		case 1:
			if(cleanup)
				this.guardM = null;
			
			return this.guardM;
		case 2:
			if(cleanup)
				this.guardR = null;
			
			return this.guardR;
		}
		
		return null;
	}
	
	public void characterItemChange(int type){
		this.chaWepon.setWepon(type);
	}
	
	public void changeWallType(int type){
		this.wall.setWallType(type);
		
		if(type == Wall.TYPE_WOOD){
			this.uiBottom.get(Wall.TYPE_STONE).setVisible(false);
			this.uiBottom.get(Wall.TYPE_WOOD).setVisible(true);
		}
		else{
			this.uiBottom.get(Wall.TYPE_STONE).setVisible(true);
			this.uiBottom.get(Wall.TYPE_WOOD).setVisible(false);
		}
	}
	
	public void gameOver(){
		if(this.popup != null){
			this.popup.close(false);
			this.popup = null;
		}
		
		this.popup = new NewsLayer(this, NewsLayer.TYPE_GAMEOVER);
	}
	
	public String getTime(){
		return this.timer.convertTime();
	}
	
	public void setGoldCnt(int gold){
		this.uiLayer.setGoldCnt(gold);
	}
	
	public void gameEnd(boolean recordSave){
		if(recordSave){
			SharedPreferences pf = this.act.getSharedPreferences("score", Activity.MODE_WORLD_WRITEABLE);
	
			int record = pf.getInt("record", 0);
			
			if(record < this.timer.getSec()){
				SharedPreferences.Editor edit = pf.edit();
				edit.putInt("record", this.timer.getSec());
				edit.commit();
			}
			
			this.act.updateMain();
		}
		Sound.engine.stopSound();
		Sound.engine.playBGM("main_bgm.ogg", true);
		CCDirector.sharedDirector().popScene();
	}
	
	public void endPopup(){
		if(this.uiLayer.getItemShop() != null){
			this.uiLayer.getItemShop().exit(null);
		}
		else if(this.popup == null)
			this.popup = new NewsLayer(this, NewsLayer.TYPE_END_POPUP);
		else{
			this.popup.close(false);
			this.popup = null;
		}
	}
	
	public void setPopup(NewsLayer layer){
		this.popup = layer;
	}
	
	public void resetTouch(){
		this.uiLayer.setAimEnable(null, false);
		this.chaWepon.setVisibleRange(false);
		this.chaLayer.setShotMotion(false);
		this.chaLayer.actionStop(false);
		this.chaWepon.shotReady(false);
		
		this.isChaMove = false;
	}
}
