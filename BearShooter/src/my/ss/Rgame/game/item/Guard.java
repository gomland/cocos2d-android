package my.ss.Rgame.game.item;

import my.ss.Rgame.game.GameLayer;
import my.ss.Rgame.game.enemy.EnemyWepon;
import my.ss.Rgame.sound.Sound;

import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGPoint;

public class Guard extends CCSprite{
	public static final int TYPE_WOOD = 0;
	public static final int TYPE_STONE = 1;
	public static final int TYPE_STEEL = 2;
	
	public static final int X_POS_L = 95;
	public static final int X_POS_M = 240;
	public static final int X_POS_R = 385;
	
	private GameLayer gameLayer;
	
	private String fileName;
	private int life;
	private float defaultX;
	private float defaultY;
	private int pos;
	private int type;
	
	public Guard(GameLayer gameLayer, String fileName, int type, float x, float y){
		super(fileName);

		this.fileName = fileName;

		this.type = type;
		if(type == TYPE_WOOD)
			this.life = 4;
		else if(type == TYPE_STONE)
			this.life = 11;
		else if(type == TYPE_STEEL)
			this.life = 25;
		
		this.type = type;
		
		this.pos = (int)x;
		
		this.defaultX = x;
		this.defaultY = y;
		this.gameLayer = gameLayer;
		this.setPosition(x, y);
		this.setAnchorPoint(0.5f, 0);
	}
	
	@Override
	public void cleanup(){
		super.cleanup();
		CCTextureCache.sharedTextureCache().removeTexture(this.fileName);
	}
	

	public void crashAction(int attackType){
		this.stopAllActions();
		
		this.life--;
		
		if(this.life <= 0)
			die();
		else{
			CCMoveBy move1 = CCMoveBy.action(0.1f, CGPoint.ccp(0,3));
			CCMoveBy move2 = CCMoveBy.action(0.1f, CGPoint.ccp(2,-6));
			CCMoveBy move3 = CCMoveBy.action(0.1f, CGPoint.ccp(-4,6));
			CCMoveBy move4 = CCMoveBy.action(0.1f, CGPoint.ccp(4,-6));
			CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "actionFinish");
			this.runAction(CCSequence.actions(move1, move2, move3, move4, actionMoveDone));
			
			if(type != TYPE_STEEL)
				Sound.engine.play(Sound.engine.S_GUARD);
			else
				Sound.engine.play(Sound.engine.S_GUARD_STEEL);
		}
	}
	
	public void actionFinish(Object sender){
		this.setPosition(this.defaultX , this.defaultY);
	}
	
	private void die(){
		int pos = this.pos;
		
		if(pos == X_POS_L)
			pos = 0;
		else if(pos == X_POS_M)
			pos = 1;
		else
			pos = 2;
		
		this.gameLayer.guard_State(pos, true);
		this.cleanup();
		this.removeSelf();		
	}
	
	public boolean crash(CGPoint point, int attackType){
		float offset = 0;
		
		if(attackType == EnemyWepon.TYPE_BREATH)
			offset = 50;
			
		if(this.getBoundingBox().contains(point.x, point.y + offset)){
			if(attackType == EnemyWepon.TYPE_BREATH){
				this.die();
				return false;
			}
			
			return true;
		}
		
		return false;
	}
}
