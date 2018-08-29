package my.ss.Rgame.game;

import my.ss.Rgame.CustomCCScene;
import my.ss.Rgame.Global.Global;
import my.ss.Rgame.game.itemShop.ItemShopLayer;
import my.ss.Rgame.sound.Sound;
import my.ss.Rgame.wepon.CharacterWepon;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.ccColor3B;

public class UILayer extends CCLayer{
	private CCSprite pointer;
	private CCSprite aim;
	private CCLabel arrowCnt;
	private CCLabel goldCnt;
	private CCMenu shopMenu;
	private ItemShopLayer itemShop;
	private int haveArrow = Global.DEFAULT_ARROW_CNT;
	private int haveGold = 0;
	
	public UILayer(){
		init();
	}
	
	@Override
	public void cleanup(){
		CCTextureCache.sharedTextureCache().removeTexture("ui/bottom.png");
		CCTextureCache.sharedTextureCache().removeTexture("ui/aim.png");
		CCTextureCache.sharedTextureCache().removeTexture("ui/pointer01.png");
		CCTextureCache.sharedTextureCache().removeTexture("ui/pointer02.png");
		CCTextureCache.sharedTextureCache().removeTexture("ui/pointer03.png");
		CCTextureCache.sharedTextureCache().removeTexture("ui/shop_btn.png");
		CCTextureCache.sharedTextureCache().removeTexture("ui/shop_btn_p.png");
		CCTextureCache.sharedTextureCache().removeTexture("ui/menu_btn.png");
		CCTextureCache.sharedTextureCache().removeTexture("ui/menu_btn_p.png");
		CCTextureCache.sharedTextureCache().removeTexture("ui/score.png");
		CCTextureCache.sharedTextureCache().removeTexture("ui/timer.png");
		super.cleanup();
	}
	
	private void init(){
		this.setIsTouchEnabled(true);
		createUI();
		createShopBtn();
		createAIM();
		createScore();
	}
	
	private void createUI(){
		CCSprite score = CCSprite.sprite("ui/score.png");
		score.setAnchorPoint(0, 1.0f);
		score.setPosition(5, Global.DEFAULT_HEIGHT - 40);
		this.addChild(score, 0);
		
		CCSprite timer = CCSprite.sprite("ui/timer.png");
		timer.setAnchorPoint(0.5f, 0.5f);
		timer.setPosition(Global.DEFAULT_WIDTH - 105, Global.DEFAULT_HEIGHT - 101);
		this.addChild(timer, 0);
	}
	
	private void createScore(){
		CCLabel arrowCnt = CCLabel.makeLabel("x "+Global.DEFAULT_ARROW_CNT, "fonts/MARKER.TTF", 30);
		arrowCnt.setPosition(60, Global.DEFAULT_HEIGHT - 53);
		arrowCnt.setAnchorPoint(0, 1);
		this.addChild(arrowCnt);
		this.arrowCnt = arrowCnt;
		
		CCLabel goldCnt = CCLabel.makeLabel("0 g", "fonts/MARKER.TTF", 25);
		goldCnt.setPosition(62, Global.DEFAULT_HEIGHT - 106);
		goldCnt.setAnchorPoint(0, 1);
		goldCnt.setColor(ccColor3B.ccYELLOW);
		this.addChild(goldCnt);
		this.goldCnt = goldCnt;
	}	
	
	private void createShopBtn(){
		CCMenuItemImage callShop = CCMenuItemImage.item("ui/shop_btn.png", "ui/shop_btn_p.png", this, "callShop");
		callShop.setAnchorPoint(1.0f, 1.0f);
		CCMenuItemImage callMenu = CCMenuItemImage.item("ui/menu_btn.png", "ui/menu_btn_p.png", this, "callMenu");
		callMenu.setAnchorPoint(1.0f, 1.0f);
		callMenu.setPosition(-90, 0);
		callMenu.setRotation(10);
		CCMenu shopMenu = CCMenu.menu();
		shopMenu.addChild(callShop);
		shopMenu.addChild(callMenu);
		shopMenu.setAnchorPoint(0, 0);
		shopMenu.setPosition(Global.DEFAULT_WIDTH, Global.DEFAULT_HEIGHT);
		this.addChild(shopMenu);
		this.shopMenu = shopMenu;
	}
	
	private void createAIM(){
		CCSprite aim = CCSprite.sprite("ui/aim.png");
		aim.setAnchorPoint(0.5f, 0.5f);
		aim.setPosition(50, Global.DEFAULT_HEIGHT - 185);
		//aim.setOpacity(150);
		aim.setVisible(false);
		this.addChild(aim);
		this.aim = aim;
		
		CCSprite pointer = CCSprite.sprite("ui/pointer01.png");
		pointer.setAnchorPoint(0.5f, 0.5f);
		pointer.setPosition(-10, 0);
		pointer.setVisible(false);
		pointer.runAction(createAnimate(0.5f));
		this.addChild(pointer);
		this.pointer = pointer;
	}
	
	public boolean isTargetZone(float y, int weponType){
		float height = Global.TAGRETZONE_UNDER;
		
		switch(weponType){
		case CharacterWepon.TYPE_SLING_SHOT:
			height += CharacterWepon.RANGE_SLING_SHOT;
			break;
		case CharacterWepon.TYPE_BOW_GUN:
			height += CharacterWepon.RANGE_BOW_GUN;
			break;
		case CharacterWepon.TYPE_GUN:
			height += CharacterWepon.RANGE_GUN;
			break;
		}
		
		if(y >= Global.TAGRETZONE_UNDER && y <= height)
			return true;
		
		return false;
	}
	
	public void setAim(CGPoint point, float x, float y){
		CCSprite pointer = this.pointer;
		CGPoint aimPoint = this.aim.getPosition();
		pointer.setPosition(aimPoint.x + (x - point.x)*2, aimPoint.y + (y - point.y)*2);
	}
	
	public void setAimEnable(CGPoint touchPoint, boolean set){
		if(this.aim.getVisible() != set){
			if(touchPoint != null)
				this.aim.setPosition(touchPoint.x, touchPoint.y + 150);
			this.aim.setVisible(set);
			this.pointer.setVisible(set);
			this.pointer.setPosition(-10, 0);
		}
	}
	
	public void setCnt(int cnt){
		this.haveArrow += cnt;
		this.arrowCnt.setString("x " + this.haveArrow);
	}
	
	public void setGoldCnt(int cnt){
		this.haveGold += cnt;
		this.goldCnt.setString(this.haveGold + " g");
	}
	
	public int getCnt(){
		return this.haveArrow;
	}
	
	public int getGoldCnt(){
		return this.haveGold;
	}
	
	public void callShop(Object sender){
		if(!this.isTouchEnabled())
			return ;
		
		Sound.engine.playBtnPress();
		CustomCCScene fgScene = (CustomCCScene)CCDirector.sharedDirector().getRunningScene();
		ItemShopLayer itemShop = new ItemShopLayer();
		fgScene.addChild(itemShop);
		this.itemShop = itemShop;
		this.shopMenu.setIsTouchEnabled(false);
	}
	
	public void callMenu(Object sender){
		CustomCCScene fgScene = (CustomCCScene)CCDirector.sharedDirector().getRunningScene();
		GameLayer layer = (GameLayer)fgScene.getChildren().get(0);
		layer.endPopup();
	}
	
	public void resumeBtn(){
		this.itemShop = null;
		this.shopMenu.setIsTouchEnabled(true);
	}
	
	public ItemShopLayer getItemShop(){
		return this.itemShop;
	}
	
	private CCRepeatForever createAnimate(float speed){
		CCAnimation animation = CCAnimation.animation("aim_pointer");
		animation.addFrame("ui/pointer01.png");
		animation.addFrame("ui/pointer02.png");
		animation.addFrame("ui/pointer03.png");

		CCAnimate animate = CCAnimate.action(animation);
		animate.setDuration(speed);
			
		return CCRepeatForever.action(animate);		
	}
}
