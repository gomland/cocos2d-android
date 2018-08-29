package ss.my.hammer.game;

import java.util.ArrayList;

import my.ss.sound.Sound;

import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;

public class NumbersLayer extends CCLayer{
	private RotateNumWidget widget;
	private ArrayList<CCSprite> numList;
	private int focus;
	private int select;
	
	private int cnt;
	private int delay;
	private CCDelayTime delayTime;
	private CCCallFuncN delayDone;
	
	public NumbersLayer(RotateNumWidget widget, int displayNumber){
		super();
		
		this.widget = widget;
		this.numList = new ArrayList<CCSprite>();
		this.setAnchorPoint(0, 0);
		
		createNumber();
		setDisplayNum(displayNumber);
	}
	
	@Override
	public void cleanup(){
		super.cleanup();
		for(int i=0; i<10; i++)
			CCTextureCache.sharedTextureCache().removeTexture("number/num_" + i +".png");
	}
	
	private void createNumber(){
		for(int i=0; i<10; i++)
			getNumberSprite(i);
	}
	
	private void getNumberSprite(int i){
		CCSprite num = CCSprite.sprite("number/num_" + i + ".png");
		num.setAnchorPoint(0.5f, 0.5f);
		num.setPosition(70, 125);
		num.setVisible(false);
		this.addChild(num);
		this.numList.add(num);
	}
	
	public void setDisplayNum(int number){
		this.numList.get(this.focus).setVisible(false);
		this.numList.get(number).setVisible(true);
		this.focus = number;
	}
	
	public void setNextNum(){
		this.numList.get(this.focus++).setVisible(false);
		
		if(this.focus > 9)
			this.focus = 0;
		
		this.numList.get(this.focus).setVisible(true);
	}
	
	public void rotateAction(int select, int delay){
		this.stopAllActions();
		this.select = select;
		this.cnt = 0;
		this.delay = delay;
		
		CCDelayTime delayTime = CCDelayTime.action(0.005f);
		CCCallFuncN delayDone = CCCallFuncN.action(this, "delayFunc");
		
		this.delayTime = delayTime;
		this.delayDone = delayDone;
		this.runAction(CCSequence.actions(delayTime, delayDone));
		
	}
	
	public void delayFunc(Object sender){
		this.cnt++;
		
		if(this.cnt > 20 && this.getTag() == RotateNumWidget.TAG_RIGHT)
			Sound.engine.play(Sound.engine.S_SCORE_ROTATE);
		
		float value = 1000f;
		
		if(this.cnt + this.delay > 70)
			value = 600f;
		else if(this.cnt + this.delay> 75)
			value = 300f;
		else if(this.cnt + this.delay> 80)
			value = 50f;
		else if(this.cnt + this.delay> 100)
			value = 1f;
		
		this.delayTime.setDuration(((float)this.cnt) / value);
		
		setNextNum();
		
		if(this.cnt > (80 + this.delay) && this.focus == this.select){
			this.stopAllActions();
			if(this.getTag() == RotateNumWidget.TAG_RIGHT)
				this.widget.finishPunch();
		}
		else{
			this.runAction(CCSequence.actions(this.delayTime, this.delayDone));
		}
	}
}
