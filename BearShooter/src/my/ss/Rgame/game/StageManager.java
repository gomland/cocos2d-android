package my.ss.Rgame.game;

import java.util.Random;

import my.ss.Rgame.news.NewsLayer;


public class StageManager {
	private final int[] stageInterval = {20, 20, 20, 15, 15, 1, 45, 45, 50, 50, 99999};
	private final int[] STAGE_1_ENEMY = {100, 0, 0, 0, 0, 0};
	private final int[] STAGE_2_ENEMY = {30, 70, 0, 0 ,0 ,0};
	private final int[] STAGE_3_ENEMY = {25, 25, 50, 0 ,0 ,0};
	private final int[] STAGE_4_ENEMY = {15, 15, 15, 55 ,0 ,0};
	private final int[] STAGE_5_ENEMY = {15, 15, 20, 25 ,25 ,0};
	private final int[] STAGE_6_ENEMY = {0, 0, 0, 0 ,0 , 100};
	private final int[] STAGE_7_ENEMY = {20, 20, 22, 21 , 14 , 3};
	private final int[] STAGE_8_ENEMY = {15, 15, 20, 25 , 19 , 6};
	private final int[] STAGE_9_ENEMY = {10, 15, 15, 25 , 25 , 10};
	private final int[] STAGE_10_ENEMY = {10, 10, 15, 30 , 25 , 20};
	private final int[] STAGE_11_ENEMY = {10, 10, 10, 20 , 30 , 30};
	
	private final int STAGE_MAX = 11;
			
	private final int MAX = 100;
	
	private int oldstage = -1;
	private int stage = 0;
	private int cnt;
	private int delayTime = -1;
	
	private GameLayer gameLayer;
	
	private boolean isCreate; 
	
	private final int DELAY_TIME = 100;

	public StageManager(GameLayer gameLayer){
		this.gameLayer = gameLayer;
	}
	
	public void action(){
		if(delayTime >= 0){
			delayTime--;
			int size = this.gameLayer.getEnemyList().size();
			
			if(this.isCreate && size <= 0)
				delayTime = 0;
			
			if(delayTime == 0){
				delayTime = -1;
				this.stage++;
				this.isCreate = false;
			}
			else{
				if(size > 0)
					this.isCreate = true;
				return;
			}
		}
		
		if(this.stage != this.oldstage){
			this.oldstage = this.stage;
			this.gameLayer.setPopup(new NewsLayer(this.gameLayer, this.stage+1));			
		}
		else{
			int type = getEnemyType();
			int probability = 75;
			
			if(this.stage > 8)
				probability = 60;
			else if(this.stage > 7)
				probability = 65;
			else if(this.stage > 6)
				probability = 70;
			
			this.gameLayer.enemyCreate(type , probability);
			
			if(this.stage < STAGE_MAX - 1){
				if(cnt == stageInterval[this.stage] - 1){
					if(this.stage > 6)
						this.gameLayer.resetGameTimer();
					
					delayTime = DELAY_TIME;
					
					cnt = 0;
				}
				else
					cnt++;
			}
		}
	}
	
	private int getEnemyType(){
		int temp[];
		
		switch(this.stage){
		case 0:
			temp = STAGE_1_ENEMY;
			break;
		case 1:
			temp = STAGE_2_ENEMY;
			break;
		case 2:
			temp = STAGE_3_ENEMY;
			break;
		case 3:
			temp = STAGE_4_ENEMY;
			break;
		case 4:
			temp = STAGE_5_ENEMY;
			break;
		case 5:
			temp = STAGE_6_ENEMY;
			break;
		case 6:
			temp = STAGE_7_ENEMY;
			break;
		case 7:
			temp = STAGE_8_ENEMY;
			break;
		case 8:
			temp = STAGE_9_ENEMY;
			break;
		case 9:
			temp = STAGE_10_ENEMY;
			break;
		case 10:
			temp = STAGE_11_ENEMY;
			break;
		default :
			temp = STAGE_10_ENEMY;			
		}
		
		return getEnemyId(temp);
	}
	
	private int getEnemyId(int[] interval){
		Random rand = new Random();
		int value = rand.nextInt(MAX) + 1;
		int temp = 0;
		
		for(int i=0; i<interval.length; i++){
			temp += interval[i];
			if(value <= temp)
				return i;
		}
		
		return 0;
	}
	
}
