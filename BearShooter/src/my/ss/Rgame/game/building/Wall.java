package my.ss.Rgame.game.building;

import java.util.ArrayList;

import my.ss.Rgame.Global.Global;
import my.ss.Rgame.game.GameLayer;

import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGPoint;

public class Wall extends CCLayer{
	private ArrayList<CCSprite> wallList;
	private ArrayList<CCSprite> wallAttackList;
	private CCSprite wallLife;
	
	private GameLayer gameLayer;
	
	private int life = Global.WALL_LIFE;
	private int lifeMax = Global.WALL_LIFE;
	
	public static final int TYPE_WOOD = 0;
	public static final int TYPE_STONE = 1;
	
	private int defensive = 1;
	
	private int type;
	
	public Wall(GameLayer gameLayer){
		super();
		this.gameLayer = gameLayer;
		this.wallList = new ArrayList<CCSprite>();
		this.wallAttackList = new ArrayList<CCSprite>();
		init();
	}
	
	@Override
	public void cleanup(){
		CCTextureCache.sharedTextureCache().removeTexture("ui/wall.png");
		CCTextureCache.sharedTextureCache().removeTexture("ui/wall_attack.png");
		CCTextureCache.sharedTextureCache().removeTexture("ui/wall_stone.png");
		CCTextureCache.sharedTextureCache().removeTexture("ui/wall_attack_stone.png");
		CCTextureCache.sharedTextureCache().removeTexture("ui/wall_life.png");
		CCTextureCache.sharedTextureCache().removeTexture("ui/wall_life_bar.png");
		super.cleanup();
	}
	
	private void init(){
		CCSprite wall = CCSprite.sprite("ui/wall.png");
		wall.setAnchorPoint(0, 0);
		wall.setPosition(0, 0);
		this.addChild(wall);
		this.wallList.add(wall);
		
		CCSprite wallStone = CCSprite.sprite("ui/wall_stone.png");
		wallStone.setAnchorPoint(0, 0);
		wallStone.setPosition(0, 0);
		wallStone.setVisible(false);
		this.addChild(wallStone);
		this.wallList.add(wallStone);
		
		CCSprite wallAttack = CCSprite.sprite("ui/wall_attack.png");
		wallAttack.setAnchorPoint(0, 0);
		wallAttack.setPosition(0, 0);
		wallAttack.setVisible(false);
		this.addChild(wallAttack);
		this.wallAttackList.add(wallAttack);
		
		CCSprite wallAttackStone = CCSprite.sprite("ui/wall_attack_stone.png");
		wallAttackStone.setAnchorPoint(0, 0);
		wallAttackStone.setPosition(0, 0);
		wallAttackStone.setVisible(false);
		this.addChild(wallAttackStone);
		this.wallAttackList.add(wallAttackStone);
		
		CCSprite wallLife = CCSprite.sprite("ui/wall_life.png");
		wallLife.setAnchorPoint(0, 0);
		wallLife.setPosition(Global.DEFAULT_WIDTH - 21, 79);
		this.addChild(wallLife);
		this.wallLife = wallLife;
		
		CCSprite wallLifeBar = CCSprite.sprite("ui/wall_life_bar.png");
		wallLifeBar.setAnchorPoint(0, 0);
		wallLifeBar.setPosition(Global.DEFAULT_WIDTH - 35, 70);
		this.addChild(wallLifeBar);
	}
	
	public void underAttack(int damege){
		damege /= this.defensive;
		this.life -= damege;
		if(this.life < 0){
			this.life = 0;
			gameOver();
		}
		else
			attackAction();		
		
		updateLifeBar(this.life);
	}
	
	private void attackAction(){
		this.wallList.get(type).setVisible(false);
		this.wallAttackList.get(type).setVisible(true);
		this.wallAttackList.get(type).stopAllActions();
		this.wallAttackList.get(type).setPosition(0, 0);
		
		CCMoveBy move1 = CCMoveBy.action(0.1f, CGPoint.ccp(0,1));
		CCMoveBy move2 = CCMoveBy.action(0.1f, CGPoint.ccp(0,-2));
		CCMoveBy move3 = CCMoveBy.action(0.1f, CGPoint.ccp(0,5));
		CCMoveBy move4 = CCMoveBy.action(0.1f, CGPoint.ccp(0,-7));
		CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "attackFinish");
		this.wallAttackList.get(type).runAction(CCSequence.actions(move1, move2, move3, move4, actionMoveDone));
	}
	
	public void attackFinish(Object sender){
		CCSprite wallAttack = (CCSprite) sender;
		
		this.wallList.get(type).setVisible(true);
		wallAttack.setVisible(false);
		wallAttack.setPosition(0, 0);
	}
	
	private void gameOver(){
		this.gameLayer.gameOver();
	}
	
	private void updateLifeBar(float life){
		this.wallLife.setScaleY(life/this.lifeMax);
	}
	
	public void setWallType(int type){
		this.type = type;
		
		if(type == TYPE_WOOD){
			this.defensive = 1;
			this.wallList.get(Wall.TYPE_WOOD).setVisible(true);
			this.wallAttackList.get(Wall.TYPE_WOOD).setVisible(false);
			this.wallList.get(Wall.TYPE_STONE).setVisible(false);
			this.wallAttackList.get(Wall.TYPE_STONE).setVisible(false);
		}
		else{
			this.defensive = 2;
			this.wallList.get(Wall.TYPE_STONE).setVisible(true);
			this.wallAttackList.get(Wall.TYPE_STONE).setVisible(false);
			this.wallList.get(Wall.TYPE_WOOD).setVisible(false);
			this.wallAttackList.get(Wall.TYPE_WOOD).setVisible(false);
		}
	}
	
	public int getWallType(){
		return this.type;
	}
	
}
