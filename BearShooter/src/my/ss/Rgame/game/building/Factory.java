package my.ss.Rgame.game.building;

import java.util.Random;

import my.ss.Rgame.game.UILayer;

import org.cocos2d.actions.CCProgressTimer;
import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCProgressTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.ccColor3B;

public class Factory extends CCSprite{
	private float interval = 2.9f;
	private UILayer uiLayer;
	private CCProgressTo progress;
	private CCLabel level;
	
	private int speedLevel;
	
	public Factory(UILayer uiLayer){
		super("building/factory.png");
		this.uiLayer = uiLayer;
		this.setAnchorPoint(0.5f, 0);
		this.setPosition(50, 100);
		startProess();
		createLevel();
		this.runAction(createAnimate());
	}
	
	@Override
	public void cleanup(){
		super.cleanup();
		CCTextureCache.sharedTextureCache().removeTexture("building/factory.png");
		CCTextureCache.sharedTextureCache().removeTexture("building/factory/factory_00.png");
		CCTextureCache.sharedTextureCache().removeTexture("building/factory/factory_01.png");
		CCTextureCache.sharedTextureCache().removeTexture("building/factory/factory_02.png");
		CCTextureCache.sharedTextureCache().removeTexture("building/loading.png");
	}
	
	private CCRepeatForever createAnimate(){
		CCAnimation animation = CCAnimation.animation("factory");
		
		animation.addFrame("building/factory/factory_00.png");
		animation.addFrame("building/factory/factory_01.png");
		animation.addFrame("building/factory/factory_02.png");
		
		CCAnimate axAnimate = CCAnimate.action(animation);
		axAnimate.setDuration(0.5f);
			
		return CCRepeatForever.action(axAnimate);		
	}
	
	private void createLevel(){
		CCLabel level = CCLabel.makeLabel("0", "fonts/MARKER.TTF", 12);
		level.setAnchorPoint(0.5f, 0.5f);
		level.setPosition(13, 62);
		level.setColor(ccColor3B.ccBLACK);
		this.addChild(level);
		this.level = level;
	}
	
	private void startProess(){
		CCProgressTimer timer = CCProgressTimer.progress("building/loading.png");
		timer.setPosition(40, 83);		
		timer.runAction(createMakeAction());
		this.addChild(timer);
	}
	
	private CCSequence createMakeAction(){
		CCProgressTo progress = CCProgressTo.action(this.interval, 100);
		this.progress = progress;
		CCCallFuncN done = CCCallFuncN.action(this, "making");
		return CCSequence.actions(progress, done);
	}
	
	public void making(Object sender){
		CCProgressTimer timer = (CCProgressTimer)sender;
		this.uiLayer.setCnt(+1);
		timer.runAction(createMakeAction());
	}
		
	public void speedUp(){
		this.speedLevel++;
		this.level.setString("" + this.speedLevel);
		this.interval -= 0.5f;
		this.progress.setDuration(this.interval);
	}
	
	public int getSpeedLevel(){
		return this.speedLevel;
	}
}
