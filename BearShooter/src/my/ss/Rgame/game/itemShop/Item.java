package my.ss.Rgame.game.itemShop;

import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.ccColor3B;

public class Item extends CCSprite{
	public int cost;
	private boolean open;
	
	private CCSprite get;
	private CCSprite select;
	private CCLabel speedLabel;
	
	private int speedLevel;
	
	public Item(int type, String fileName, int cost, boolean open, boolean isSelect){
		super(fileName);
		this.cost = cost;
		this.open = open;
		
		if(!open){
			String str = "item_shop/wepon_2_tag.png";
			if(type == ItemShopLayer.ITEM_TYPE_WEPON_GUN)
				str = "item_shop/wepon_3_tag.png";
				
			CCSprite get = CCSprite.sprite(str);
			get.setAnchorPoint(0.5f, 0.5f);
			get.setPosition(80, 30);
			this.addChild(get);
			this.get = get;
			
			this.setOpacity(100);
		}
		
		if(isSelect){
			CCSprite select = CCSprite.sprite("item_shop/select.png");
			select.setAnchorPoint(0.5f, 0.5f);
			select.setPosition(50, 50);
			select.setVisible(false);
			this.addChild(select);
			this.select = select;
		}
		
		if(type == ItemShopLayer.ITEM_TYPE_MAKE_SPEED_UP){
			CCLabel speedLabel = CCLabel.makeLabel("lv 0", "fonts/MARKER.TTF", 12);
			speedLabel.setAnchorPoint(0.5f, 0.5f);
			speedLabel.setPosition(72, 21);
			speedLabel.setRotation(-3);
			speedLabel.setColor(ccColor3B.ccRED);
			this.addChild(speedLabel);
			this.speedLabel = speedLabel;
		}
	}
	
	@Override
	public void cleanup(){
		CCTextureCache.sharedTextureCache().removeTexture("item_shop/wepon_2_tag.png");
		CCTextureCache.sharedTextureCache().removeTexture("item_shop/wepon_3_tag.png");
		CCTextureCache.sharedTextureCache().removeTexture("item_shop/select.png");
		super.cleanup();
	}
	
	public void open(){
		if(this.get != null){
			this.removeChild(this.get, true);
			this.get = null;
		}
		this.setOpacity(255);
		this.open = true;
	}
	
	public boolean getOpenState(){
		return open;
	}
	
	public void visibleSelect(boolean isSelect){
		if(this.select != null){
			this.select.setVisible(isSelect);
		}
	}

	public void setSpeedLevel(int level){
		this.speedLevel += level;
		
		if(this.speedLevel >= 5){
			this.speedLevel = 5;
			this.setOpacity(120);
			this.speedLabel.setString("Max");
		}
		else
			this.speedLabel.setString("lv " + this.speedLevel);
	}
	
	public int getSpeedLevel(){
		return this.speedLevel;
	}
}
