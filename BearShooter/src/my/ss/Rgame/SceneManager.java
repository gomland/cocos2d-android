package my.ss.Rgame;

import my.ss.Rgame.game.GameLayer;
import my.ss.Rgame.game.UILayer;
import my.ss.Rgame.help.HelpLayer;
import my.ss.Rgame.main.MainLayer;


public class SceneManager {
	public static final int TAG_CCLAYER = 99;
	public static final int TAG_CCLAYER_TIMER = 1012;
	
	public static CustomCCScene getMainScene(MainActivity act){
		CustomCCScene scene = new CustomCCScene();
		MainLayer mainLayer = new MainLayer(act);
		
		scene.addChild(mainLayer);
		scene.setSceneType(CustomCCScene.SCENE_MAIN);
		return scene;
	}
	
	public static CustomCCScene getHelpScene(){
		CustomCCScene scene = new CustomCCScene();
		HelpLayer helpLayer = new HelpLayer();
		
		scene.addChild(helpLayer);
		scene.setSceneType(CustomCCScene.SCENE_HELP);
		return scene;
	}
	
	public static CustomCCScene getGameScene(MainActivity act){
		CustomCCScene scene = new CustomCCScene();
		UILayer uiLayer = new UILayer();
		GameLayer gameLayer = new GameLayer(act, uiLayer);
		
		uiLayer.setTag(TAG_CCLAYER);
		gameLayer.setTag(TAG_CCLAYER);
		
		scene.addChild(gameLayer);
		scene.addChild(uiLayer);
		
		scene.setSceneType(CustomCCScene.SCENE_GAME);
		
		return scene;
	}
}
