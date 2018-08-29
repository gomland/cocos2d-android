package my.ss.Rgame.wepon;

import org.cocos2d.nodes.CCSprite;

public class Wepon extends CCSprite{
	public int damege;
	private int type;
	
	public static final int TYPE_SLING = 0;
	public static final int TYPE_ARROW = 1;
	public static final int TYPE_GUN = 2;
	public static final int TYPE_GTOWER = 5;
	public static final int TYPE_BALEISTER = 6;
	
	public Wepon(String fileName, int type){
		super(fileName);
		this.type = type;
	}
	
	public int getType(){
		return this.type;
	}
}
