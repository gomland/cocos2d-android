package my.ss.Rgame.news;

import java.util.ArrayList;

import my.ss.Rgame.CustomCCScene;
import my.ss.Rgame.Global.Global;
import my.ss.Rgame.game.GameLayer;
import my.ss.Rgame.game.UILayer;
import my.ss.Rgame.sound.Sound;

import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.ccColor3B;

import android.view.MotionEvent;

public class NewsLayer extends CCLayer{
	private GameLayer gameLayer;
	private UILayer uiLayer;
	private ArrayList<CCSprite> enemyList;
	private int type;
	private CCLabel exit;
	
	private CCSprite yesBtn;
	private CCSprite noBtn;
	
	private boolean isExit;
	
	public static final int TYPE_END_POPUP = -1;
	public static final int TYPE_GAMEOVER = 0;
	public static final int TYPE_STAGE = 1; 
	
	private boolean isTouch; 
	
	public NewsLayer(GameLayer gameLayer, int type){
		super();

		this.gameLayer = gameLayer;
		
		gameLayer.pauseAllAction();
		
		this.type = type;
		this.enemyList = new ArrayList<CCSprite>();
		init(type);
		
		CustomCCScene fgScene = (CustomCCScene)CCDirector.sharedDirector().getRunningScene();
		fgScene.addChild(this);
		
		UILayer uiLayer = (UILayer)fgScene.getChildren().get(1);
		this.uiLayer = uiLayer;
		uiLayer.setIsTouchEnabled(false);
	}
	
	@Override
	public void cleanup(){
		super.cleanup();
		CCTextureCache.sharedTextureCache().removeTexture("item_shop/black_bg.png");
		CCTextureCache.sharedTextureCache().removeTexture("news/news.png");
		CCTextureCache.sharedTextureCache().removeTexture("news/gold.png");
		CCTextureCache.sharedTextureCache().removeTexture("news/news_gameover.png");
		CCTextureCache.sharedTextureCache().removeTexture("news/news_bear.png");
		CCTextureCache.sharedTextureCache().removeTexture("news/news_dojang.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/pig.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/golem.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/troll.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/mage.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/elephant.png");
		CCTextureCache.sharedTextureCache().removeTexture("enemy/dragon.png");
		CCTextureCache.sharedTextureCache().removeTexture("popup/popup.png");
		CCTextureCache.sharedTextureCache().removeTexture("popup/popup_yes.png");
		CCTextureCache.sharedTextureCache().removeTexture("popup/popup_no.png");
		CCTextureCache.sharedTextureCache().removeTexture("news/broken_bg.png");		
		
	}
	
	private void init(int type){
		this.setIsTouchEnabled(true);
		this.setPosition(0, 0);
		this.setAnchorPoint(0, 0);
		
		createEnemyList();
		
		if(type == TYPE_END_POPUP){
			Sound.engine.play(Sound.engine.S_NOT_ENOUGH);
			
			CCSprite bg = CCSprite.sprite("item_shop/black_bg.png");
			bg.setAnchorPoint(0,0);
			bg.setPosition(0,0);
			bg.setOpacity(150);
			this.addChild(bg);
			
			CCSprite popupBg = CCSprite.sprite("popup/popup.png");
			popupBg.setAnchorPoint(0.5f, 0.5f);
			popupBg.setPosition(Global.DEFAULT_WIDTH/2, Global.DEFAULT_HEIGHT/2);
			this.addChild(popupBg, 2);
			
			CCSprite yesBtn = CCSprite.sprite("popup/popup_yes.png");
			yesBtn.setAnchorPoint(0.5f, 0.5f);
			yesBtn.setScale(1.3f);
			yesBtn.setPosition(Global.DEFAULT_WIDTH/2 - 47, Global.DEFAULT_HEIGHT/2 - 16);
			this.addChild(yesBtn, 2);
			this.yesBtn = yesBtn;
			
			CCSprite noBtn = CCSprite.sprite("popup/popup_no.png");
			noBtn.setAnchorPoint(0.5f, 0.5f);
			noBtn.setScale(1.3f);
			noBtn.setPosition(Global.DEFAULT_WIDTH/2 + 47, Global.DEFAULT_HEIGHT/2 - 15);
			this.addChild(noBtn, 2);
			this.noBtn = noBtn;
		}
		else if(type == TYPE_GAMEOVER){
			Sound.engine.stopSound();
			Sound.engine.playBGM("game_over.ogg", false);
			
			CCDelayTime delay = CCDelayTime.action(2.0f);
			CCCallFuncN done = CCCallFuncN.action(this, "delayDone");
			this.runAction(CCSequence.actions(delay, done));
			
			CCSprite bg = CCSprite.sprite("news/broken_bg.png");
			bg.setAnchorPoint(0,0);
			bg.setPosition(0,0);
			bg.setOpacity(200);
			this.addChild(bg);
			
			CCSprite gameover = CCSprite.sprite("news/news_gameover.png");
			gameover.setAnchorPoint(0.5f, 0.5f);
			gameover.setPosition(Global.DEFAULT_WIDTH/2, 450);
			this.addChild(gameover, 2);
			
			CCSprite dojang1 = CCSprite.sprite("news/news_dojang.png");
			dojang1.setAnchorPoint(0, 0.5f);
			dojang1.setPosition(100, 320);
			this.addChild(dojang1, 2);
			
			CCSprite dojang2 = CCSprite.sprite("news/news_dojang.png");
			dojang2.setAnchorPoint(1, 0.5f);
			dojang2.setPosition(380, 320);
			this.addChild(dojang2, 2);
			
			CCLabel time = CCLabel.makeLabel(this.gameLayer.getTime(), "fonts/MARKER.TTF", 40);
			time.setAnchorPoint(0.5f,0.5f);
			time.setPosition(Global.DEFAULT_WIDTH/2, 320);
			this.addChild(time, 2);
			
			CCMenuItemImage exit = CCMenuItemImage.item("ui/menu_btn.png", "ui/menu_btn_p.png", this, "exit");		
			exit.setAnchorPoint(1, 0);
			CCMenu shopMenu = CCMenu.menu(exit);
			shopMenu.setAnchorPoint(1, 0);
			shopMenu.setPosition(Global.DEFAULT_WIDTH - 10, 10);
			this.addChild(shopMenu);
		}
		else if(type >= TYPE_STAGE){
			Sound.engine.play(Sound.engine.S_NEWS);
			this.gameLayer.resetTouch();
			
			CCSprite bg = CCSprite.sprite("item_shop/black_bg.png");
			bg.setAnchorPoint(0,0);
			bg.setPosition(0,0);
			bg.setOpacity(150);
			this.addChild(bg);
			
			CCSprite paper = CCSprite.sprite("news/paper.png");
			paper.setAnchorPoint(0.5f, 0.5f);
			paper.setPosition(Global.DEFAULT_WIDTH/2, Global.DEFAULT_HEIGHT/2);
			this.addChild(paper, 1);
			
			CCSprite bear = CCSprite.sprite("news/news_bear.png");
			bear.setAnchorPoint(1, 0);
			bear.setScale(1.5f);
			bear.setPosition(Global.DEFAULT_WIDTH, 0);
			this.addChild(bear, 2);
			
			CCLabel title = CCLabel.makeLabel("Stage " + type, "", 30);
			title.setAnchorPoint(0.5f, 1);
			title.setColor(ccColor3B.ccBLACK);
			title.setPosition(Global.DEFAULT_WIDTH/2, Global.DEFAULT_HEIGHT/2 + 105);
			this.addChild(title, 2);
			
			CCLabel enemy = CCLabel.makeLabel("적 출현", "fonts/HMFMPYUN.TTF", 22);
			enemy.setAnchorPoint(0, 1);
			enemy.setColor(ccColor3B.ccBLACK);
			enemy.setPosition(Global.DEFAULT_WIDTH/2 - 115, Global.DEFAULT_HEIGHT/2 + 60);
			this.addChild(enemy, 2);
			
			CCLabel support = CCLabel.makeLabel("지원금", "fonts/HMFMPYUN.TTF", 22);
			support.setAnchorPoint(0, 1);
			support.setColor(ccColor3B.ccBLACK);
			support.setPosition(Global.DEFAULT_WIDTH/2 - 115, Global.DEFAULT_HEIGHT/2 - 22);
			this.addChild(support, 2);
			
			CCSprite gold = CCSprite.sprite("news/gold.png");
			gold.setAnchorPoint(0.5f, 0.5f);
			gold.setPosition(Global.DEFAULT_WIDTH/2 - 95, Global.DEFAULT_HEIGHT/2 - 70);
			this.addChild(gold, 2);
			
			CCLabel goldCost = CCLabel.makeLabel((type*5) + " g", "fonts/MARKER.TTF", 20);
			goldCost.setAnchorPoint(0, 0.5f);
			goldCost.setColor(ccColor3B.ccYELLOW);
			goldCost.setPosition(Global.DEFAULT_WIDTH/2 - 70, Global.DEFAULT_HEIGHT/2 - 70);
			this.addChild(goldCost, 2);
			
			CCLabel exit = CCLabel.makeLabel("Close", "", 25);
			exit.setAnchorPoint(0, 0.5f);
			exit.setColor(ccColor3B.ccBLACK);
			exit.setPosition(Global.DEFAULT_WIDTH/2 + 60, Global.DEFAULT_HEIGHT/2 - 100);
			this.addChild(exit, 2);
			this.exit = exit;
			
			this.gameLayer.setGoldCnt(type*5);
			
			if(type == 6){
				this.enemyList.get(5).setVisible(true);
				this.enemyList.get(5).setPosition(Global.DEFAULT_WIDTH/2 - 100, Global.DEFAULT_HEIGHT/2 + 10);
			}
			else{
				int max = type;
				
				if(max > 6)
					max = 6;
				
				for(int i=0; i<max; i++)
					this.enemyList.get(i).setVisible(true);
			}
		}
	}
	
	private void createEnemyList(){
		CCSprite enemy1 = CCSprite.sprite("enemy/pig.png");
		enemy1.setAnchorPoint(0.5f, 0.5f);
		enemy1.setPosition(Global.DEFAULT_WIDTH/2 - 100, Global.DEFAULT_HEIGHT/2 + 10);
		enemy1.setVisible(false);
		enemy1.setScale(1.2f);
		this.addChild(enemy1, 3);
		this.enemyList.add(enemy1);
		
		CCSprite enemy2 = CCSprite.sprite("enemy/golem.png");
		enemy2.setAnchorPoint(0.5f, 0.5f);
		enemy2.setPosition(Global.DEFAULT_WIDTH/2 - 60, Global.DEFAULT_HEIGHT/2 + 12);
		enemy2.setVisible(false);
		enemy2.setScale(1.2f);
		this.addChild(enemy2, 3);
		this.enemyList.add(enemy2);
		
		CCSprite enemy22 = CCSprite.sprite("enemy/troll.png");
		enemy22.setAnchorPoint(0.5f, 0.5f);
		enemy22.setPosition(Global.DEFAULT_WIDTH/2 - 20, Global.DEFAULT_HEIGHT/2 + 10);
		enemy22.setVisible(false);
		enemy22.setScale(1.2f);
		this.addChild(enemy22, 3);
		this.enemyList.add(enemy22);

		
		CCSprite enemy3 = CCSprite.sprite("enemy/mage.png");
		enemy3.setAnchorPoint(0.5f, 0.5f);
		enemy3.setPosition(Global.DEFAULT_WIDTH/2 + 20, Global.DEFAULT_HEIGHT/2 + 10);
		enemy3.setVisible(false);
		enemy3.setScale(1.2f);
		this.addChild(enemy3, 3);
		this.enemyList.add(enemy3);
		
		CCSprite enemy4 = CCSprite.sprite("enemy/elephant.png");
		enemy4.setAnchorPoint(0.5f, 0.5f);
		enemy4.setPosition(Global.DEFAULT_WIDTH/2 + 60, Global.DEFAULT_HEIGHT/2 + 10);
		enemy4.setVisible(false);
		enemy4.setScale(0.8f);
		this.addChild(enemy4, 3);
		this.enemyList.add(enemy4);
		
		CCSprite enemy5 = CCSprite.sprite("enemy/dragon.png");
		enemy5.setAnchorPoint(0.5f, 0.5f);
		enemy5.setPosition(Global.DEFAULT_WIDTH/2 + 100, Global.DEFAULT_HEIGHT/2 + 10);
		enemy5.setVisible(false);
		enemy5.setScale(0.6f);
		this.addChild(enemy5, 3);
		this.enemyList.add(enemy5);
	}
	
	public boolean ccTouchesBegan(MotionEvent event){
		this.isTouch = true;
		return true;
	}
	
	public boolean ccTouchesEnded(MotionEvent event){
		CGPoint touchPoint = this.convertTouchToNodeSpace(event);
		
		if(this.isTouch){
			if(this.type == TYPE_STAGE){
				if(this.exit.getBoundingBox().contains(touchPoint.x, touchPoint.y))
					close(false);
			}
			else if(this.type == TYPE_END_POPUP){
				if(this.yesBtn.getBoundingBox().contains(touchPoint.x, touchPoint.y))
					close(true);
				else if(this.noBtn.getBoundingBox().contains(touchPoint.x, touchPoint.y))
					close(false);
			}
			else if(this.type == TYPE_GAMEOVER){
				
			}
			else
				close(false);

		}		
		this.isTouch = false;
			
		return true;
	}
	
	public void close(boolean end){
		this.uiLayer.setIsTouchEnabled(true);
		this.removeSelf();
		this.cleanup();
		
		Sound.engine.playBtnPress();
		
		if(type == TYPE_GAMEOVER)
			this.gameLayer.gameEnd(true);
		else if(end)
			this.gameLayer.gameEnd(false);
		else
			this.gameLayer.resumeAllAction();
	}
	
	public void delayDone(Object sender){
		this.isExit = true;
	}
	
	public void exit(Object sender){
		if(this.isExit)
			close(false);
	}
}
