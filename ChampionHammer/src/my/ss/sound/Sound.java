package my.ss.sound;

import android.app.Activity;

public class Sound {
	public static SoundEffect engine;
	
	public Sound(Activity act){
		engine = new SoundEffect(act);
	}
}
