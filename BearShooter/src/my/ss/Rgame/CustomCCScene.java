package my.ss.Rgame;

import org.cocos2d.layers.CCScene;

public class CustomCCScene extends CCScene{
	public static final int SCENE_MAIN = 0;
	public static final int SCENE_GAME = 1;
	public static final int SCENE_HELP = 2;
	private int sceneType;
	
	public CustomCCScene(){
		super();
	}

	public void setSceneType(int type){
		this.sceneType = type;
	}
	
	public int getSceneType(){
		return this.sceneType;
	}
}
