package my.ss.Rgame.game.enemy;

import org.cocos2d.nodes.CCSprite;

public class EnemyWepon extends CCSprite{
	private int damege;
	private int type;
	
	public static final int TYPE_ARROW = 0;
	public static final int TYPE_STONE = 1;
	public static final int TYPE_AX = 2;
	public static final int TYPE_LIGHTNING = 3;
	public static final int TYPE_FIREBALL = 4;
	public static final int TYPE_BREATH = 5;
	
	public EnemyWepon(String fileName, int type){
		super(fileName);
		this.type = type;
		
		switch(type){
		case TYPE_ARROW :
			this.damege = 15;
			break;
		case TYPE_STONE :
			this.damege = 25;
			break;
		case TYPE_AX :
			this.damege = 35;
			break;
		case TYPE_LIGHTNING :
			this.damege = 50;
			break;
		case TYPE_FIREBALL :
			this.damege = 40;
			break;			
		case TYPE_BREATH :
			this.damege = 65;
			break;
		}
	}
	
	public int getType(){
		return this.type;
	}
	
	public int getDamege(){
		return this.damege;
	}
}
