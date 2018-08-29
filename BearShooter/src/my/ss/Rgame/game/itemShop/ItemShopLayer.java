package my.ss.Rgame.game.itemShop;

import java.util.ArrayList;

import my.ss.Rgame.CustomCCScene;
import my.ss.Rgame.Global.Global;
import my.ss.Rgame.game.GameLayer;
import my.ss.Rgame.game.UILayer;
import my.ss.Rgame.game.building.Wall;
import my.ss.Rgame.game.item.Guard;
import my.ss.Rgame.sound.Sound;

import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCFadeTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGPoint;

import android.view.MotionEvent;

public class ItemShopLayer extends CCLayer{
	private ArrayList<Item> items;
	private ArrayList<CCSprite> moveItems;
	private ArrayList<CCSprite> zones;
	private ArrayList<CCSprite> titles;
	private CCSprite popup;
	private GameLayer gameLayer;
	private UILayer uiLayer;
	
	private int selected;
	
	public static final int ITEM_TYPE_GTOWER = 0;
	public static final int ITEM_TYPE_BALEISTER = 1;
	public static final int ITEM_TYPE_GUARD_WOOD = 2;
	public static final int ITEM_TYPE_GUARD_STONE = 3;
	public static final int ITEM_TYPE_GUARD_STEEL = 4;
	public static final int ITEM_TYPE_WEPON_SLINGSHOT = 5;
	public static final int ITEM_TYPE_WEPON_BOWGUN = 6;
	public static final int ITEM_TYPE_WEPON_GUN = 7;
	public static final int ITEM_TYPE_MAKE_SPEED_UP = 8;
	public static final int ITEM_TYPE_WALL_UPGRADE = 9;
	
	private final int ZONE_TYPE_GTOWER_L = 0;
	private final int ZONE_TYPE_GTOWER_R = 1;
	private final int ZONE_TYPE_BALEISTER = 2;
	private final int ZONE_TYPE_GUARD_L = 3;
	private final int ZONE_TYPE_GUARD_M = 4;
	private final int ZONE_TYPE_GUARD_R = 5;
	
	private boolean isOneTouchItem;
	
	public ItemShopLayer(){
		super();
		
		this.items = new ArrayList<Item>();
		this.moveItems = new ArrayList<CCSprite>();
		this.zones = new ArrayList<CCSprite>();
		this.titles = new ArrayList<CCSprite>();
		
		getLayer();
		init();		
	}
	
	@Override
	public void cleanup(){		
		super.cleanup();
		CCTextureCache.sharedTextureCache().removeTexture("item_shop/black_bg.png");
		CCTextureCache.sharedTextureCache().removeTexture("guard/guard_wood.png");
		CCTextureCache.sharedTextureCache().removeTexture("guard/guard_stone.png");
		CCTextureCache.sharedTextureCache().removeTexture("guard/guard_steel.png");
		CCTextureCache.sharedTextureCache().removeTexture("building/gtower.png");
		CCTextureCache.sharedTextureCache().removeTexture("building/gtower_stone.png");
		CCTextureCache.sharedTextureCache().removeTexture("building/baleister.png");
		CCTextureCache.sharedTextureCache().removeTexture("item_shop/build_1.png");
		CCTextureCache.sharedTextureCache().removeTexture("item_shop/build_1_1.png");
		CCTextureCache.sharedTextureCache().removeTexture("item_shop/build_2.png");
		CCTextureCache.sharedTextureCache().removeTexture("item_shop/guard_wood.png");
		CCTextureCache.sharedTextureCache().removeTexture("item_shop/guard_stone.png");
		CCTextureCache.sharedTextureCache().removeTexture("item_shop/guard_steel.png");
		CCTextureCache.sharedTextureCache().removeTexture("item_shop/title_building.png");
		CCTextureCache.sharedTextureCache().removeTexture("item_shop/title_wepon.png");
		CCTextureCache.sharedTextureCache().removeTexture("item_shop/title_shield.png");
		CCTextureCache.sharedTextureCache().removeTexture("item_shop/title_upgrade.png");
		CCTextureCache.sharedTextureCache().removeTexture("item_shop/wepon_1.png");
		CCTextureCache.sharedTextureCache().removeTexture("item_shop/wepon_2.png");
		CCTextureCache.sharedTextureCache().removeTexture("item_shop/wepon_3.png");
		CCTextureCache.sharedTextureCache().removeTexture("item_shop/wepon_2_tag.png");
		CCTextureCache.sharedTextureCache().removeTexture("item_shop/wepon_3_tag.png");
		CCTextureCache.sharedTextureCache().removeTexture("ui/exit_btn.png");
		CCTextureCache.sharedTextureCache().removeTexture("ui/exit_btn_p.png");
		CCTextureCache.sharedTextureCache().removeTexture("item_shop/gtower_set_zone.png");
		CCTextureCache.sharedTextureCache().removeTexture("item_shop/bal_set_zone.png");
		CCTextureCache.sharedTextureCache().removeTexture("item_shop/guard_set_zone.png");
		CCTextureCache.sharedTextureCache().removeTexture("item_shop/popup_msg1.png");
		CCTextureCache.sharedTextureCache().removeTexture("item_shop/wall_up.png");
		CCTextureCache.sharedTextureCache().removeTexture("item_shop/speed_up.png");
	}
	
	private void init(){
		this.setIsTouchEnabled(true);
		
		CCSprite bg = CCSprite.sprite("item_shop/black_bg.png");
		bg.setAnchorPoint(0,0);
		bg.setPosition(0,0);
		bg.setOpacity(180);
		this.addChild(bg);
		
		drawZone();
		drawItem();
		drawTitle();
		createOther();
		createMoveItem();
	}
	
	private void getLayer(){
		CustomCCScene fgScene = (CustomCCScene)CCDirector.sharedDirector().getRunningScene();
		
		GameLayer layer = (GameLayer)fgScene.getChildren().get(0);
		layer.pauseAllAction();
		this.gameLayer = layer;
		
		UILayer uiLayer = (UILayer)fgScene.getChildren().get(1);
		this.uiLayer = uiLayer;
		
	}
	
	private void drawZone(){
		CCSprite zone_GuardTower_left = CCSprite.sprite("item_shop/gtower_set_zone.png");
		zone_GuardTower_left.setAnchorPoint(0, 0);
		zone_GuardTower_left.setPosition(65, 215);
		zone_GuardTower_left.setVisible(false);
		this.addChild(zone_GuardTower_left);
		this.zones.add(zone_GuardTower_left);
		
		CCSprite zone_GuardTower_right = CCSprite.sprite("item_shop/gtower_set_zone.png");
		zone_GuardTower_right.setAnchorPoint(1, 0);
		zone_GuardTower_right.setPosition(Global.DEFAULT_WIDTH - 65, 215);
		zone_GuardTower_right.setVisible(false);
		this.addChild(zone_GuardTower_right);
		this.zones.add(zone_GuardTower_right);
		
		CCSprite zone_Baleister = CCSprite.sprite("item_shop/bal_set_zone.png");
		zone_Baleister.setAnchorPoint(1, 0);
		zone_Baleister.setPosition(Global.DEFAULT_WIDTH, 100);
		zone_Baleister.setVisible(false);
		this.addChild(zone_Baleister);
		this.zones.add(zone_Baleister);
		
		CCSprite zone_Guard1 = CCSprite.sprite("item_shop/guard_set_zone.png");
		zone_Guard1.setAnchorPoint(0.5f, 0.5f);
		zone_Guard1.setPosition(97, 80);
		zone_Guard1.setVisible(false);
		this.addChild(zone_Guard1);
		this.zones.add(zone_Guard1);
		
		CCSprite zone_Guard2 = CCSprite.sprite("item_shop/guard_set_zone.png");
		zone_Guard2.setAnchorPoint(0.5f, 0.5f);
		zone_Guard2.setPosition(240, 80);
		zone_Guard2.setVisible(false);
		this.addChild(zone_Guard2);
		this.zones.add(zone_Guard2);
		
		CCSprite zone_Guard3 = CCSprite.sprite("item_shop/guard_set_zone.png");
		zone_Guard3.setAnchorPoint(0.5f, 0.5f);
		zone_Guard3.setPosition(388, 80);
		zone_Guard3.setVisible(false);
		this.addChild(zone_Guard3);
		this.zones.add(zone_Guard3);
	}
	
	private void drawTitle(){
		CCSprite titleWepon = CCSprite.sprite("item_shop/title_wepon.png");
		titleWepon.setAnchorPoint(0, 1);
		titleWepon.setPosition(15, Global.DEFAULT_HEIGHT-14);
		this.addChild(titleWepon);
		this.titles.add(titleWepon);
		
		CCSprite titleShield = CCSprite.sprite("item_shop/title_shield.png");
		titleShield.setAnchorPoint(0, 1);
		titleShield.setPosition(15, Global.DEFAULT_HEIGHT-165);
		this.addChild(titleShield);
		this.titles.add(titleShield);
		
		CCSprite titleBuilding = CCSprite.sprite("item_shop/title_building.png");
		titleBuilding.setAnchorPoint(0, 1);
		titleBuilding.setPosition(15, Global.DEFAULT_HEIGHT-362);
		this.addChild(titleBuilding);
		this.titles.add(titleBuilding);
		
		CCSprite titleUpgrade = CCSprite.sprite("item_shop/title_upgrade.png");
		titleUpgrade.setAnchorPoint(0, 1);
		titleUpgrade.setPosition(15, Global.DEFAULT_HEIGHT-530);
		this.addChild(titleUpgrade);
		this.titles.add(titleUpgrade);
	}

	private void createMoveItem(){
		String gtowerName = "building/gtower.png";
		if(this.gameLayer.getWallType() == Wall.TYPE_STONE)
			gtowerName = "building/gtower_stone.png";
		
		CCSprite item_GuardTower = CCSprite.sprite(gtowerName);
		item_GuardTower.setAnchorPoint(0.5f, 0.5f);
		item_GuardTower.setPosition(0, 0);
		item_GuardTower.setVisible(false);
		this.addChild(item_GuardTower);
		this.moveItems.add(item_GuardTower);
		
		CCSprite item_Baleister = CCSprite.sprite("building/baleister.png");
		item_Baleister.setAnchorPoint(0.5f, 0.5f);
		item_Baleister.setPosition(0, 0);
		item_Baleister.setVisible(false);
		this.addChild(item_Baleister);
		this.moveItems.add(item_Baleister);
		
		CCSprite item_GuardWood = CCSprite.sprite("guard/guard_wood.png");
		item_GuardWood.setAnchorPoint(0.5f, 0.5f);
		item_GuardWood.setPosition(0, 0);
		item_GuardWood.setVisible(false);
		this.addChild(item_GuardWood);
		this.moveItems.add(item_GuardWood);
		
		CCSprite item_GuardCopper = CCSprite.sprite("guard/guard_stone.png");
		item_GuardCopper.setAnchorPoint(0.5f, 0.5f);
		item_GuardCopper.setPosition(0, 0);
		item_GuardCopper.setVisible(false);
		this.addChild(item_GuardCopper);
		this.moveItems.add(item_GuardCopper);
		
		CCSprite item_GuardSteel = CCSprite.sprite("guard/guard_steel.png");
		item_GuardSteel.setAnchorPoint(0.5f, 0.5f);
		item_GuardSteel.setPosition(0, 0);
		item_GuardSteel.setVisible(false);
		this.addChild(item_GuardSteel);
		this.moveItems.add(item_GuardSteel);
	}
	
	private void drawItem(){
		String gtowerName = "item_shop/build_1.png";
		if(this.gameLayer.getWallType() == Wall.TYPE_STONE)
			gtowerName = "item_shop/build_1_1.png";
		
		Item item_GuardTower = new Item(0, gtowerName, 30, true, false);
		item_GuardTower.setAnchorPoint(0, 1);
		item_GuardTower.setPosition(35, Global.DEFAULT_HEIGHT-405);
		this.addChild(item_GuardTower);
		this.items.add(item_GuardTower);
		
		Item item_Baleister = new Item(0,"item_shop/build_2.png", 50, true, false);
		item_Baleister.setAnchorPoint(0, 1);
		item_Baleister.setPosition(170, Global.DEFAULT_HEIGHT-405);
		this.addChild(item_Baleister);
		this.items.add(item_Baleister);
		
		Item item_GuardWood = new Item(0,"item_shop/guard_wood.png", 5, true, false);
		item_GuardWood.setAnchorPoint(0, 1);
		item_GuardWood.setPosition(15, Global.DEFAULT_HEIGHT-205);
		this.addChild(item_GuardWood);
		this.items.add(item_GuardWood);
		
		Item item_GuardCopper = new Item(0,"item_shop/guard_stone.png", 10, true, false);
		item_GuardCopper.setAnchorPoint(0, 1);
		item_GuardCopper.setPosition(175, Global.DEFAULT_HEIGHT-205);
		this.addChild(item_GuardCopper);
		this.items.add(item_GuardCopper);
		
		Item item_GuardSteel = new Item(0,"item_shop/guard_steel.png", 20, true, false);
		item_GuardSteel.setAnchorPoint(0, 1);
		item_GuardSteel.setPosition(330, Global.DEFAULT_HEIGHT-205);
		this.addChild(item_GuardSteel);
		this.items.add(item_GuardSteel);
		
		Item item_SlingShot = new Item(ITEM_TYPE_WEPON_SLINGSHOT, "item_shop/wepon_1.png", 0, true, true);
		item_SlingShot.setAnchorPoint(0, 1);
		item_SlingShot.setPosition(20, Global.DEFAULT_HEIGHT - 47);
		item_SlingShot.visibleSelect(true);
		this.addChild(item_SlingShot);
		this.items.add(item_SlingShot);
		
		Item item_bowGun = new Item(ITEM_TYPE_WEPON_BOWGUN, "item_shop/wepon_2.png", 90, Global.WEPON_2_OPEN, true);
		item_bowGun.setAnchorPoint(0, 1);
		item_bowGun.setPosition(145, Global.DEFAULT_HEIGHT - 47);
		this.addChild(item_bowGun);
		this.items.add(item_bowGun);
		
		Item item_gun = new Item(ITEM_TYPE_WEPON_GUN, "item_shop/wepon_3.png", 180, Global.WEPON_3_OPEN, true);
		item_gun.setAnchorPoint(0, 1);
		item_gun.setPosition(270, Global.DEFAULT_HEIGHT - 47);
		this.addChild(item_gun);
		this.items.add(item_gun);
		
		weponItemSelected(Global.SELECT_WEPON);
		
		Item makeSpeed = new Item(ITEM_TYPE_MAKE_SPEED_UP, "item_shop/speed_up.png", 30, true, false);
		makeSpeed.setAnchorPoint(0, 1);
		makeSpeed.setPosition(16, Global.DEFAULT_HEIGHT - 577);
		makeSpeed.setSpeedLevel(this.gameLayer.getFactorySpeed());
		this.addChild(makeSpeed);
		this.items.add(makeSpeed);
		
		Item wallUpgrade = new Item(0, "item_shop/wall_up.png", 100, true, false);
		wallUpgrade.setAnchorPoint(0, 1);
		wallUpgrade.setPosition(140, Global.DEFAULT_HEIGHT - 575);
		this.addChild(wallUpgrade);
		this.items.add(wallUpgrade);
		if(this.gameLayer.getWallType() == Wall.TYPE_STONE)
			wallUpgrade.setOpacity(120);
	}
	
	private void createOther(){
		CCMenuItemImage exit = CCMenuItemImage.item("ui/exit_btn.png", "ui/exit_btn_p.png", this, "exit");		
		exit.setAnchorPoint(1.0f, 1.0f);
		CCMenu shopMenu = CCMenu.menu(exit);
		shopMenu.setAnchorPoint(0, 0);
		shopMenu.setPosition(Global.DEFAULT_WIDTH, Global.DEFAULT_HEIGHT);
		this.addChild(shopMenu);
		
		CCSprite popup = CCSprite.sprite("item_shop/popup_msg1.png");
		popup.setAnchorPoint(0.5f, 0);
		popup.setPosition(240, 80);
		popup.setScale(1.3f);
		popup.setVisible(false);
		this.addChild(popup);
		this.popup = popup;
	}
	
	private void visibleZone(int type, boolean visible){
		ArrayList<CCSprite> zones = this.zones;
		
		switch(type){
		case ITEM_TYPE_GTOWER:
			zones.get(ZONE_TYPE_GTOWER_L).setVisible(visible);
			zones.get(ZONE_TYPE_GTOWER_R).setVisible(visible);
			break;
		case ITEM_TYPE_BALEISTER:
			zones.get(ZONE_TYPE_BALEISTER).setVisible(visible);
			break;
		case ITEM_TYPE_GUARD_STONE:
		case ITEM_TYPE_GUARD_WOOD:
		case ITEM_TYPE_GUARD_STEEL:
			if(this.gameLayer.guard_State(0, false) == null)
				zones.get(ZONE_TYPE_GUARD_L).setVisible(visible);
			else
				zones.get(ZONE_TYPE_GUARD_L).setVisible(false);
			
			if(this.gameLayer.guard_State(1, false) == null)
				zones.get(ZONE_TYPE_GUARD_M).setVisible(visible);
			else
				zones.get(ZONE_TYPE_GUARD_M).setVisible(false);
			
			if(this.gameLayer.guard_State(2, false) == null)
				zones.get(ZONE_TYPE_GUARD_R).setVisible(visible);
			else
				zones.get(ZONE_TYPE_GUARD_R).setVisible(false);
			break;
		}
	}
	
	private int checkZone(int type, CGPoint touch){
		ArrayList<CCSprite> zones = this.zones;
		int start = 0, end = 0;
		
		switch(type){
		case ITEM_TYPE_GTOWER:
			start = ZONE_TYPE_GTOWER_L;
			end = ZONE_TYPE_GTOWER_R;
			break;
		case ITEM_TYPE_BALEISTER:
			start = end = ZONE_TYPE_BALEISTER;
			break;
		case ITEM_TYPE_GUARD_STONE:
		case ITEM_TYPE_GUARD_WOOD:
		case ITEM_TYPE_GUARD_STEEL:
			start =ZONE_TYPE_GUARD_L;
			end = ZONE_TYPE_GUARD_R;
			break;
		}
		
		for(int i=start; i<=end; i++){
			CCSprite zone = zones.get(i);
			if(zone.getBoundingBox().contains(touch.x, touch.y) && zone.getVisible())
				return i;
		}
		
		return -1;
	}
	
	private int checkSelected(CGPoint touch){
		ArrayList<Item> items = this.items;
		
		for(int i=0; i<items.size(); i++){
			if(items.get(i).getBoundingBox().contains(touch.x, touch.y)){
				return i;
			}
		}
		
		return -1;
	}
	
	public boolean ccTouchesBegan(MotionEvent event){
		CGPoint touchPoint = this.convertTouchToNodeSpace(event);
		int selected;
		
		selected = checkSelected(touchPoint);
		if(selected != -1){
			if(selected >= ITEM_TYPE_WEPON_SLINGSHOT){
				this.isOneTouchItem = true;
			}
			else{
				CCSprite item = this.moveItems.get(selected);
				item.setPosition(touchPoint.x - Global.BUILD_OFFSET, touchPoint.y + Global.BUILD_OFFSET);
				item.setVisible(true);
				
				visibleZone(selected, true);
				setAllVisible(false);
			}
			this.selected = selected;
		}
		else
			this.selected = -1;
		
		return true;
	}
	
	public boolean ccTouchesMoved(MotionEvent event){
		CGPoint touchPoint = this.convertTouchToNodeSpace(event);
		int selected = this.selected;
		
		if(selected != -1 && !this.isOneTouchItem){
			CCSprite item = this.moveItems.get(selected);
			item.setPosition(touchPoint.x - Global.BUILD_OFFSET, touchPoint.y + Global.BUILD_OFFSET);
		}
		
		return true;
	}

	public boolean ccTouchesEnded(MotionEvent event){
		CGPoint touchPoint = this.convertTouchToNodeSpace(event);
		int selected = this.selected;
		
		if(this.isOneTouchItem){
			int selectEnded = checkSelected(touchPoint);
			if(selectEnded != -1 && selected == selectEnded){
				Item item = this.items.get(selected);
				if(!item.getOpenState()){
					int gold = this.uiLayer.getGoldCnt();
					if(gold >= item.cost){
						this.uiLayer.setGoldCnt(-item.cost);
						item.open();
						
						Sound.engine.play(Sound.engine.S_BUY);
						
						if(selected == ITEM_TYPE_WEPON_BOWGUN)
							Global.WEPON_2_OPEN = true;
						else if(selected == ITEM_TYPE_WEPON_GUN)
							Global.WEPON_3_OPEN = true;
					}
					else
						notEnoughMoney();
				}
				else{
					if(selected >= ITEM_TYPE_WEPON_SLINGSHOT && selected <= ITEM_TYPE_WEPON_GUN){
						this.gameLayer.characterItemChange(selected - ITEM_TYPE_WEPON_SLINGSHOT);
						weponItemSelected(selected);
						Global.SELECT_WEPON = selected;
						Sound.engine.playBtnPress();
					}
					else if(selected == ITEM_TYPE_MAKE_SPEED_UP){
						if(item.getSpeedLevel() == 5)
							return true;
						
						int gold = this.uiLayer.getGoldCnt();
						if(gold >= item.cost){
							this.uiLayer.setGoldCnt(-item.cost);
							item.setSpeedLevel(1);
							this.gameLayer.factorySpeedUp();
							Sound.engine.play(Sound.engine.S_BUY);
						}
						else
							notEnoughMoney();						
					}
					else if(selected == ITEM_TYPE_WALL_UPGRADE){
						if(this.gameLayer.getWallType() == Wall.TYPE_STONE)
							return true;
						
						int gold = this.uiLayer.getGoldCnt();
						if(gold >= item.cost){
							this.uiLayer.setGoldCnt(-item.cost);
							item.setOpacity(120);
							this.gameLayer.wallUpgrade();
							Sound.engine.play(Sound.engine.S_BUY);
						}
						else
							notEnoughMoney();	
					}
				}
			}
			this.isOneTouchItem = false;
		}
		else if(selected != -1){
			CCSprite moveItem = this.moveItems.get(selected);
			moveItem.setPosition(0, 0);
			moveItem.setVisible(false);
			
			Item item = this.items.get(selected);
			int check = checkZone(selected, touchPoint);
			if(check != -1){
				if(selected == ITEM_TYPE_GUARD_WOOD
						|| selected == ITEM_TYPE_GUARD_STONE
						|| selected == ITEM_TYPE_GUARD_STEEL){
					switch(check){
					case ZONE_TYPE_GUARD_L :
						touchPoint.x = Guard.X_POS_L;
						break;
					case ZONE_TYPE_GUARD_M :
						touchPoint.x = Guard.X_POS_M;
						break;
					case ZONE_TYPE_GUARD_R :
						touchPoint.x = Guard.X_POS_R;
						break;
					}
				}
					
				
				int gold = this.uiLayer.getGoldCnt();
				if(gold >= item.cost){
					this.uiLayer.setGoldCnt(-item.cost);
					this.gameLayer.build(selected, touchPoint);
				}
				else
					notEnoughMoney();
			}
			
			setAllVisible(true);
			visibleZone(selected, false);
		}
		
		return true;
	}
	
	public void exit(Object sender){
		Sound.engine.playBtnPress();
		this.setIsTouchEnabled(false);
		this.gameLayer.resumeAllAction();
		this.uiLayer.resumeBtn();
		this.removeSelf();
	}
	
	private void notEnoughMoney(){
		CCSprite popup = this.popup;
		popup.setOpacity(0);
		popup.setVisible(true);
		
		CCFadeTo fade1 = CCFadeTo.action(0.5f, 255);
		CCDelayTime delay = CCDelayTime.action(1.0f);
		CCFadeTo fade2 = CCFadeTo.action(0.5f, 0);
		CCCallFuncN func = CCCallFuncN.action(this, "popupFinish");
		
		popup.stopAllActions();
		popup.runAction(CCSequence.actions(fade1, delay, fade2, func));
		
		Sound.engine.play(Sound.engine.S_NOT_ENOUGH);
	}
	
	public void popupFinish(Object sender){
		CCSprite popup = (CCSprite) sender;
		popup.setVisible(false);
	}
	
	private void weponItemSelected(int id){
		switch(id){
		case ITEM_TYPE_WEPON_SLINGSHOT:
			this.items.get(ITEM_TYPE_WEPON_SLINGSHOT).visibleSelect(true);
			this.items.get(ITEM_TYPE_WEPON_BOWGUN).visibleSelect(false);
			this.items.get(ITEM_TYPE_WEPON_GUN).visibleSelect(false);
			break;
		case ITEM_TYPE_WEPON_BOWGUN:
			this.items.get(ITEM_TYPE_WEPON_SLINGSHOT).visibleSelect(false);
			this.items.get(ITEM_TYPE_WEPON_BOWGUN).visibleSelect(true);
			this.items.get(ITEM_TYPE_WEPON_GUN).visibleSelect(false);
			break;
		case ITEM_TYPE_WEPON_GUN:
			this.items.get(ITEM_TYPE_WEPON_SLINGSHOT).visibleSelect(false);
			this.items.get(ITEM_TYPE_WEPON_BOWGUN).visibleSelect(false);
			this.items.get(ITEM_TYPE_WEPON_GUN).visibleSelect(true);
			break;
		}
	}
	
	private void setAllVisible(boolean visible){
		ArrayList<Item> items = this.items;
		ArrayList<CCSprite> titles = this.titles;
		
		for(int i=0; i<items.size(); i++)
			items.get(i).setVisible(visible);
		
		for(int i=0; i<titles.size(); i++)
			titles.get(i).setVisible(visible);
		
	}
	
}
