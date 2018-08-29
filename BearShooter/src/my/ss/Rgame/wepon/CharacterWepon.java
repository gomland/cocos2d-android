package my.ss.Rgame.wepon;

import java.util.ArrayList;

import my.ss.Rgame.Global.Global;
import my.ss.Rgame.game.GameLayer;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;

public class CharacterWepon extends CCLayer{
	private GameLayer gameLayer;
	private int type;
	private float interval;
	private ArrayList<CCSprite> weponList;
	private CCSprite range;
	
	private CCAnimate slingShotAni;
	private CCAnimate bowgunAni;
	private CCAnimate gunAni;
	
	public static final int TYPE_SLING_SHOT = 0;
	public static final int TYPE_BOW_GUN = 1;
	public static final int TYPE_GUN = 2;
	public static final int TYPE_SLING_SHOT_READY = 3;
	public static final int TYPE_BOW_GUN_READY = 4;
	public static final int TYPE_GUN_READY = 5;
	public static final int WEPON_MAX = 3;
	
	public static final int DAMEGE_SLING_SHOT = 1;
	public static final int DAMEGE_BOW_GUN = 3;
	public static final int DAMEGE_GUN = 8;
	
	public static final float RANGE_SLING_SHOT = 100;
	public static final float RANGE_BOW_GUN = 230;
	public static final float RANGE_GUN = 340;
	
	public CharacterWepon(GameLayer gameLayer, int defaultType){
		super();
		this.gameLayer = gameLayer;
		this.type = defaultType;
		this.weponList = new ArrayList<CCSprite>(); 
		init();
	}
	@Override
	public void cleanup(){
		CCTextureCache.sharedTextureCache().removeTexture("wepon/slingshot/slingshot_00.png");
		CCTextureCache.sharedTextureCache().removeTexture("wepon/slingshot/slingshot_01.png");
		CCTextureCache.sharedTextureCache().removeTexture("wepon/slingshot/slingshot_02.png");
		CCTextureCache.sharedTextureCache().removeTexture("wepon/slingshot/slingshot_03.png");
		CCTextureCache.sharedTextureCache().removeTexture("wepon/slingshot/slingshot_04.png");
		CCTextureCache.sharedTextureCache().removeTexture("wepon/slingshot/slingshot_05.png");
		CCTextureCache.sharedTextureCache().removeTexture("wepon/slingshot/slingshot_06.png");
		CCTextureCache.sharedTextureCache().removeTexture("wepon/bowgun/bowgun_00.png");
		CCTextureCache.sharedTextureCache().removeTexture("wepon/bowgun/bowgun_01.png");
		CCTextureCache.sharedTextureCache().removeTexture("wepon/bowgun/bowgun_02.png");
		CCTextureCache.sharedTextureCache().removeTexture("wepon/bowgun/bowgun_03.png");
		CCTextureCache.sharedTextureCache().removeTexture("wepon/gun/gun_00.png");
		CCTextureCache.sharedTextureCache().removeTexture("wepon/gun/gun_01.png");
		CCTextureCache.sharedTextureCache().removeTexture("wepon/gun/gun_02.png");
		CCTextureCache.sharedTextureCache().removeTexture("wepon/gun/gun_03.png");
		CCTextureCache.sharedTextureCache().removeTexture("wepon/gun/gun_04.png");
		CCTextureCache.sharedTextureCache().removeTexture("wepon/gun/gun_05.png");
		CCTextureCache.sharedTextureCache().removeTexture("wepon/gun.png");
		CCTextureCache.sharedTextureCache().removeTexture("wepon/wepon_range01g.png");
		CCTextureCache.sharedTextureCache().removeTexture("wepon/wepon_range02.png");
		super.cleanup();
	}
	
	
	private void init(){
		CCSprite range = CCSprite.sprite("wepon/wepon_range01.png");
		range.setAnchorPoint(0, 0);
		range.setPosition(0, 0);
		range.setVisible(false);
		range.runAction(createRange(0.1f));
		this.gameLayer.addChild(range, GameLayer.UI_Z);
		this.range = range;
		
		CCSprite slingShot = CCSprite.sprite("wepon/slingshot/slingshot_00.png");
		slingShot.setAnchorPoint(0.5f, 0.5f);
		slingShot.setPosition(35, -5);
		slingShot.setVisible(false);
		this.addChild(slingShot);
		this.weponList.add(slingShot);
		
		CCSprite bowGun = CCSprite.sprite("wepon/bowgun/bowgun_00.png");
		bowGun.setAnchorPoint(0.5f, 0.5f);
		bowGun.setPosition(35, 0);
		bowGun.setVisible(false);
		this.addChild(bowGun);
		this.weponList.add(bowGun);
		
		CCSprite gun = CCSprite.sprite("wepon/gun/gun_01.png");
		gun.setAnchorPoint(0.5f, 0.5f);
		gun.setPosition(37, -8);
		gun.setVisible(false);
		this.addChild(gun);
		this.weponList.add(gun);

		/* ready action */
		CCSprite slingShotReady = CCSprite.sprite("wepon/slingshot/slingshot_01.png");
		slingShotReady.setAnchorPoint(0.5f, 0.5f);
		slingShotReady.setPosition(35, -5);
		slingShotReady.setVisible(false);
		this.addChild(slingShotReady);
		this.weponList.add(slingShotReady);
		
		CCSprite bowGunReady = CCSprite.sprite("wepon/bowgun/bowgun_01.png");
		bowGunReady.setAnchorPoint(0.5f, 0.5f);
		bowGunReady.setPosition(35, 0);
		bowGunReady.setVisible(false);
		this.addChild(bowGunReady);
		this.weponList.add(bowGunReady);
		
		CCSprite gunReady = CCSprite.sprite("wepon/gun/gun_01.png");
		gunReady.setAnchorPoint(0.5f, 0.5f);
		gunReady.setPosition(37, -8);
		gunReady.setVisible(false);
		this.addChild(gunReady);
		this.weponList.add(gunReady);
		
		setWepon(this.type);
		createShotAnimate();
		createBowGunAnimate();
		createGunAnimate();
	}
	
	private void createShotAnimate(){
		CCAnimation animation = CCAnimation.animation("slingshot");
		animation.addFrame("wepon/slingshot/slingshot_01.png");
		animation.addFrame("wepon/slingshot/slingshot_02.png");
		animation.addFrame("wepon/slingshot/slingshot_03.png");
		animation.addFrame("wepon/slingshot/slingshot_04.png");
		animation.addFrame("wepon/slingshot/slingshot_05.png");
		animation.addFrame("wepon/slingshot/slingshot_06.png");

		CCAnimate slingShotAni = CCAnimate.action(animation);
		this.slingShotAni = slingShotAni;
	}
	
	private void createBowGunAnimate(){
		CCAnimation animation = CCAnimation.animation("bowgun");
		animation.addFrame("wepon/bowgun/bowgun_01.png");
		animation.addFrame("wepon/bowgun/bowgun_02.png");
		animation.addFrame("wepon/bowgun/bowgun_03.png");

		CCAnimate bowgunAni = CCAnimate.action(animation);
		this.bowgunAni = bowgunAni;
	}
	
	private void createGunAnimate(){
		CCAnimation animation = CCAnimation.animation("gun");
		animation.addFrame("wepon/gun/gun_01.png");
		animation.addFrame("wepon/gun/gun_02.png");
		animation.addFrame("wepon/gun/gun_03.png");
		animation.addFrame("wepon/gun/gun_04.png");
		animation.addFrame("wepon/gun/gun_05.png");

		CCAnimate gunAni = CCAnimate.action(animation);
		this.gunAni = gunAni;
	}
	
	public void shotReady(boolean visible){
		switch(this.type){
		case TYPE_SLING_SHOT :
			if(visible){
				this.weponList.get(TYPE_SLING_SHOT_READY).setVisible(true);
				this.weponList.get(TYPE_SLING_SHOT).setVisible(false);
			}
			else{
				this.weponList.get(TYPE_SLING_SHOT_READY).setVisible(false);
				this.weponList.get(TYPE_SLING_SHOT).setVisible(true);
			}
			
			break;
		case TYPE_BOW_GUN :
			if(visible){
				this.weponList.get(TYPE_BOW_GUN_READY).setVisible(true);
				this.weponList.get(TYPE_BOW_GUN).setVisible(false);
			}
			else{
				this.weponList.get(TYPE_BOW_GUN_READY).setVisible(false);
				this.weponList.get(TYPE_BOW_GUN).setVisible(true);
			}
			break;
		case TYPE_GUN :
			if(visible){
				this.weponList.get(TYPE_GUN_READY).setVisible(true);
				this.weponList.get(TYPE_GUN).setVisible(false);
			}
			else{
				this.weponList.get(TYPE_GUN_READY).setVisible(false);
				this.weponList.get(TYPE_GUN).setVisible(true);
			}
			break;
		}
		
	}
	
	public void shot(){
		switch(this.type){
		case TYPE_SLING_SHOT :
			this.weponList.get(TYPE_SLING_SHOT).stopAllActions();
			this.slingShotAni.setDuration(0.3f);
			this.weponList.get(TYPE_SLING_SHOT).runAction(this.slingShotAni);
			break;
		case TYPE_BOW_GUN :
			this.weponList.get(TYPE_BOW_GUN).stopAllActions();
			this.bowgunAni.setDuration(0.4f);
			this.weponList.get(TYPE_BOW_GUN).runAction(this.bowgunAni);
			break;
		case TYPE_GUN :
			this.weponList.get(TYPE_GUN).stopAllActions();
			this.gunAni.setDuration(0.2f);
			this.weponList.get(TYPE_GUN).runAction(this.gunAni);
			break;
		}
	}
	
	public void setWepon(int type){
		this.weponList.get(this.type).setVisible(false);
		
		switch(type){
		case TYPE_SLING_SHOT :
			this.weponList.get(TYPE_SLING_SHOT).setVisible(true);
			this.type = TYPE_SLING_SHOT;
			this.interval = 0.5f;
			break;
		case TYPE_BOW_GUN :
			this.weponList.get(TYPE_BOW_GUN).setVisible(true);
			this.type = TYPE_BOW_GUN;
			this.interval = 1.2f;
			break;
		case TYPE_GUN :
			this.weponList.get(TYPE_GUN).setVisible(true);
			this.type = TYPE_GUN;
			this.interval = 2.5f;
			break;
		}
	}
	
	public void setVisibleRange(boolean visible){
		if(visible){
			switch(this.type){
			case TYPE_SLING_SHOT :
				this.range.setPosition(0, RANGE_SLING_SHOT + Global.TAGRETZONE_UNDER);
				break;
			case TYPE_BOW_GUN :
				this.range.setPosition(0, RANGE_BOW_GUN + Global.TAGRETZONE_UNDER);
				break;
			case TYPE_GUN :
				this.range.setPosition(0, RANGE_GUN + Global.TAGRETZONE_UNDER);
				break;
			}
		}
		this.range.setVisible(visible);		
	}
	
	public int getType(){
		return this.type;
	}
	
	public float getAttackSpeed(){
		return this.interval;
	}
	
	public CCSprite getSelectWepon(){
		return this.weponList.get(this.type);
	}
	
	public CCSprite getReadyWepon(){
		return this.weponList.get(this.type + WEPON_MAX);
	}
	
	private CCRepeatForever createRange(float speed){
		CCAnimation animation = CCAnimation.animation("range");
		animation.addFrame("wepon/wepon_range01.png");
		animation.addFrame("wepon/wepon_range02.png");

		CCAnimate animate = CCAnimate.action(animation);
		animate.setDuration(speed);
			
		return CCRepeatForever.action(animate);		
	}
}
