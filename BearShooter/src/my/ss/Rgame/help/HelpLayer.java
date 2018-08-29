package my.ss.Rgame.help;

import my.ss.Rgame.Global.Global;
import my.ss.Rgame.sound.Sound;

import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemImage;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGPoint;

import android.view.MotionEvent;

public class HelpLayer extends CCLayer{
	private CCLayer touchLayer;
	
	private CCSprite arrowLeft;
	private CCSprite arrowRight;
	
	private int focus;
	private int itemMax;
	private final int PAGE_WIDTH = 480;
	private CGPoint oldPoint;
	private CGPoint bufPoint;
	
	public HelpLayer(){
		super();

		init();
	}
	
	@Override
	public void cleanup(){
		super.cleanup();
		CCTextureCache.sharedTextureCache().removeTexture("help/help_bg.png");
		CCTextureCache.sharedTextureCache().removeTexture("help/help_00.png");
		CCTextureCache.sharedTextureCache().removeTexture("help/help_01.png");
		CCTextureCache.sharedTextureCache().removeTexture("help/help_02.png");
		CCTextureCache.sharedTextureCache().removeTexture("help/help_03.png");
		CCTextureCache.sharedTextureCache().removeTexture("help/help_04.png");
		CCTextureCache.sharedTextureCache().removeTexture("help/arrow_left.png");
		CCTextureCache.sharedTextureCache().removeTexture("help/arrow_right.png");
		CCTextureCache.sharedTextureCache().removeTexture("ui/exit_btn_p.png");
		CCTextureCache.sharedTextureCache().removeTexture("ui/exit_btn.png");
	}
	
	private void init(){
		this.setIsTouchEnabled(true);
		
		CCSprite bg = CCSprite.sprite("help/help_bg.png");
		bg.setAnchorPoint(0,0);
		bg.setPosition(0,0);
		bg.setOpacity(70);
		this.addChild(bg);
		
		CCLayer touchLayer = CCLayer.node();
		touchLayer.setAnchorPoint(0,0);
		touchLayer.setPosition(0, 0);
		this.touchLayer = touchLayer;
		this.addChild(touchLayer);
		
		CCSprite page1 = CCSprite.sprite("help/help_00.png");
		page1.setAnchorPoint(0.5f, 0);
		page1.setPosition(Global.DEFAULT_WIDTH/2, 0);
		touchLayer.addChild(page1);
		this.itemMax++;
		
		CCSprite page2 = CCSprite.sprite("help/help_01.png");
		page2.setAnchorPoint(0.5f, 0);
		page2.setPosition(Global.DEFAULT_WIDTH/2 + this.itemMax * PAGE_WIDTH, 0);
		touchLayer.addChild(page2);
		this.itemMax++;
		
		CCSprite page3 = CCSprite.sprite("help/help_02.png");
		page3.setAnchorPoint(0.5f, 0);
		page3.setPosition(Global.DEFAULT_WIDTH/2 + this.itemMax * PAGE_WIDTH, 0);
		touchLayer.addChild(page3);
		this.itemMax++;
		
		CCSprite page4 = CCSprite.sprite("help/help_03.png");
		page4.setAnchorPoint(0.5f, 0);
		page4.setPosition(Global.DEFAULT_WIDTH/2 + this.itemMax * PAGE_WIDTH, 0);
		touchLayer.addChild(page4);
		this.itemMax++;
		
		CCSprite page5 = CCSprite.sprite("help/help_04.png");
		page5.setAnchorPoint(0.5f, 0);
		page5.setPosition(Global.DEFAULT_WIDTH/2 + this.itemMax * PAGE_WIDTH, 0);
		touchLayer.addChild(page5);
		this.itemMax++;
		
		CCMenuItemImage exit = CCMenuItemImage.item("ui/exit_btn.png", "ui/exit_btn_p.png", this, "exit");		
		exit.setAnchorPoint(1.0f, 1.0f);
		CCMenu shopMenu = CCMenu.menu(exit);
		shopMenu.setAnchorPoint(0, 0);
		shopMenu.setPosition(Global.DEFAULT_WIDTH, Global.DEFAULT_HEIGHT - 10);
		this.addChild(shopMenu);
		
		CCSprite arrowLeft = CCSprite.sprite("help/arrow_left.png");
		arrowLeft.setAnchorPoint(0, 0.5f);
		arrowLeft.setPosition(10, Global.DEFAULT_HEIGHT/2 - 50);
		arrowLeft.setScale(0.7f);
		arrowLeft.setVisible(false);
		this.addChild(arrowLeft);
		this.arrowLeft = arrowLeft;
		
		CCSprite arrowRight = CCSprite.sprite("help/arrow_right.png");
		arrowRight.setAnchorPoint(1, 0.5f);
		arrowRight.setScale(0.7f);
		arrowRight.setPosition(Global.DEFAULT_WIDTH - 10, Global.DEFAULT_HEIGHT/2 - 50);
		this.addChild(arrowRight);
		this.arrowRight = arrowRight;
		
	}
	
	
	
	public boolean ccTouchesBegan(MotionEvent event){
		CGPoint touchPoint = this.convertTouchToNodeSpace(event);
		this.oldPoint = touchPoint;
		this.bufPoint = touchPoint;
		this.touchLayer.stopAllActions();
		
		return true;
	}
	
	public boolean ccTouchesMoved(MotionEvent event){
		CGPoint touchPoint = this.convertTouchToNodeSpace(event);
		CGPoint thisPoint = this.touchLayer.getPosition();
		
		this.touchLayer.setPosition(thisPoint.x + (touchPoint.x - this.bufPoint.x), 0);
		this.bufPoint = touchPoint;

		return true;
	}
	
	public boolean ccTouchesEnded(MotionEvent event){
		CGPoint touchPoint = this.convertTouchToNodeSpace(event);
		float offset = touchPoint.x - this.oldPoint.x;
		
		if(offset < -150)
			actionLeft();
		else if(offset > 150)
			actionRight();
		else
			actionMid();

		if(this.focus <= 0)
			this.arrowLeft.setVisible(false);
		else if(this.focus+1 >= itemMax)
			this.arrowRight.setVisible(false);
		else{
			this.arrowLeft.setVisible(true);
			this.arrowRight.setVisible(true);
		}
			
		return true;
	}
	
	private void actionRight(){
		if(this.focus - 1 < 0)
			actionMid();
		else{
			Sound.engine.play(Sound.engine.S_NEWS);
			this.focus --;
			CCMoveTo move = CCMoveTo.action(0.1f, CGPoint.ccp(-(this.focus * PAGE_WIDTH), 0));
			this.touchLayer.runAction(move);
		}
	}
	
	private void actionLeft(){
		if(this.focus + 1 >= this.itemMax)
			actionMid();
		else{
			Sound.engine.play(Sound.engine.S_NEWS);
			this.focus ++;
			CCMoveTo move = CCMoveTo.action(0.1f, CGPoint.ccp(-(this.focus * PAGE_WIDTH), 0));
			this.touchLayer.runAction(move);
		}
	}
	
	private void actionMid(){
		CCMoveTo move = CCMoveTo.action(0.1f, CGPoint.ccp(-(this.focus * PAGE_WIDTH), 0));
		this.touchLayer.runAction(move);
	}
	
	public void exit(Object sender){
		Sound.engine.playBtnPress();
		CCDirector.sharedDirector().popScene();
	}
}
