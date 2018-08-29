package ss.my.hammer.game;

import java.util.ArrayList;

import org.cocos2d.layers.CCLayer;

public class RotateNumWidget extends CCLayer{
	private ArrayList<NumbersLayer> numbersList;
	private HammerGameLayer gameLayer;
	
	private int number;
	
	public static int TAG_LEFT = 124;
	public static int TAG_MID = 125;
	public static int TAG_RIGHT = 126;
	
	public RotateNumWidget(HammerGameLayer gameLayer){
		super();
		this.gameLayer = gameLayer;
		init();
	}
	
	private void init(){
		this.numbersList = new ArrayList<NumbersLayer>();
		this.setAnchorPoint(0, 0);
		this.setScale(0.2f);
		
		NumbersLayer left = new NumbersLayer(this, 0);
		left.setTag(TAG_LEFT);
		left.setAnchorPoint(0, 0);
		left.setPosition(0, 0);
		this.addChild(left);
		
		this.numbersList.add(left);
		NumbersLayer mid = new NumbersLayer(this, 0);
		mid.setTag(TAG_MID);
		mid.setAnchorPoint(0, 0);
		mid.setPosition(140, 0);
		this.addChild(mid);
		this.numbersList.add(mid);
		
		NumbersLayer right = new NumbersLayer(this, 0);
		right.setTag(TAG_RIGHT);
		right.setAnchorPoint(0, 0);
		right.setPosition(280, 0);
		this.addChild(right);
		this.numbersList.add(right);
	}
	
	public void setNumber(int number){
		int right = number % 10;
		int mid = (number % 100) / 10;
		int left = number / 100;
		
		this.number = number;
		
		this.numbersList.get(0).rotateAction(left, 0);
		this.numbersList.get(1).rotateAction(mid, 8);
		this.numbersList.get(2).rotateAction(right, 16);
	}
	
	public void finishPunch(){
		this.gameLayer.finishPunch();
	}
	
	public int getScore(){
		return this.number;
	}
}
