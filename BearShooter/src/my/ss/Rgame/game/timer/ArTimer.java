package my.ss.Rgame.game.timer;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCLabel;

public class ArTimer extends CCLayer{
	private float bufTime;
	private int sec;
	private CCLabel time;
	
	public ArTimer(){
		this.setContentSize(200, 50);
		
		CCLabel time = CCLabel.makeLabel("00:00:00", "fonts/MARKER.TTF", 20);
		time.setAnchorPoint(0,1);
		time.setPosition(0,0);
		this.time = time;
		this.addChild(time);
		
		this.schedule("timer");
	}
	
	public void timer(float dt){
		this.bufTime+=dt;
		if(this.bufTime >= 1.0f){
			sec++;
			updateTime();
			this.bufTime-=1.0f;
		}
	}
	
	private void updateTime(){
		this.time.setString(convertTime());
	}
	
	public String convertTime(){
		int sec = this.sec % 60;
		int min = ((this.sec - sec) / 60 % 60);
		int hour = (this.sec - sec) / 60 / 60;
		String strSec;
		String strMin;
		String strHour;
		
		if(sec > 9)
			strSec = String.valueOf(sec);
		else
			strSec = "0"+String.valueOf(sec);
		
		if(min > 9)
			strMin = String.valueOf(min);
		else
			strMin = "0"+String.valueOf(min);
		
		if(hour > 9)
			strHour = String.valueOf(hour);
		else
			strHour = "0"+String.valueOf(hour);
		
		return strHour + ":" + strMin + ":" +  strSec; 
	}
	
	public void pauseTimer(boolean pause){
		if(pause)
			this.pauseSchedulerAndActions();
		else
			this.resumeSchedulerAndActions();
	}
	
	public int getSec(){
		return this.sec;
		
	}
}
