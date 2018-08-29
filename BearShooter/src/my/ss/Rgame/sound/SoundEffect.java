package my.ss.Rgame.sound;

import java.io.IOException;
import java.util.HashMap;

import my.ss.Rgame.R;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

public class SoundEffect {
	public final int S_BUILD = 0;
	public final int S_SLING_SHOT = 1;   //너무 작아서 바꿔야될듯
	public final int S_BOW_GUN = 2;
	public final int S_GUN = 3;
	public final int S_LIGHTNING = 4;
	public final int S_DRAGON = 5;
	public final int S_ELEPHANT = 6;
	public final int S_PIG = 7;
	public final int S_GOLEM = 8;
	public final int S_TROLL = 9;
	public final int S_MAGE = 10;
	public final int S_FIREBALL = 11;
	public final int S_ATTACK_BASE = 12;
	public final int S_GUARD = 13;
	public final int S_GUARD_STEEL = 14;
	public final int S_BTN = 15;
	public final int S_BUY = 16;
	public final int S_NOT_ENOUGH = 17;
	public final int S_NEWS = 18;
	public final int S_BTN2 = 19;
	public final int S_BREATH = 20;
	public final int S_TOWER= 21;
	public final int S_BALEISTER = 22;
	public final int S_DRUM = 23;
	public final int S_ARROW = 24;
	public final int S_CRASH = 25;
	public final int S_SWING = 26;
	
	private final int S_MAX = 27;
	
	private SoundPool mSoundPool;
	private HashMap<Integer,Integer> mSoundPoolMap;
	private AudioManager  mAudioManager;
	private Context mContext;
	private MediaPlayer mp;
	
	private boolean isSetMedia;
	
	private boolean isSound;
	
	public SoundEffect(Context mContext){
		this.mContext = mContext;
		initSounds(mContext);
		loadSound();
	}
	
	public void initSounds(Context mContext) {
		this.mSoundPool = new SoundPool(14, AudioManager.STREAM_MUSIC, 0);
		this.mSoundPoolMap = new HashMap<Integer,Integer>();
		this.mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
		this.mp = new MediaPlayer();
		
	}
	
	public void addSound(int index, int SoundID)
	{
		this.mSoundPoolMap.put(index, this.mSoundPool.load(this.mContext, SoundID, 1));
	}
	
	private void playSound(int index)
	{
		float streamVolume = this.mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume / this.mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		this.mSoundPool.play((Integer) this.mSoundPoolMap.get(index), streamVolume, streamVolume, 1, 0, 1f);
	}
	
	public void playLoopedSound(int index)
	{
	    float streamVolume = this.mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	    streamVolume = streamVolume / this.mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	    this.mSoundPool.play((Integer) this.mSoundPoolMap.get(index), streamVolume, streamVolume, 1, -1, 1f);
	}

	private void loadSound(){
		addSound(S_BUILD, R.raw.build);
		addSound(S_SLING_SHOT, R.raw.sling_shot);
		addSound(S_BOW_GUN, R.raw.bowgun);
		addSound(S_GUN, R.raw.gun);
		addSound(S_LIGHTNING, R.raw.lightning);
		addSound(S_DRAGON, R.raw.dragon);
		addSound(S_ELEPHANT, R.raw.elephant);
		addSound(S_PIG, R.raw.pig);
		addSound(S_GOLEM, R.raw.golem);
		addSound(S_TROLL, R.raw.troll);
		addSound(S_MAGE, R.raw.mage);
		addSound(S_FIREBALL, R.raw.fireball);
		addSound(S_ATTACK_BASE, R.raw.base_attack);
		addSound(S_GUARD, R.raw.guard);
		addSound(S_GUARD_STEEL, R.raw.guard_steel);
		addSound(S_BTN, R.raw.btn);
		addSound(S_BUY, R.raw.coin);
		addSound(S_NOT_ENOUGH, R.raw.no_popup);
		addSound(S_NEWS, R.raw.news);
		addSound(S_BTN2, R.raw.btn2);
		addSound(S_BREATH, R.raw.breath);
		addSound(S_TOWER, R.raw.tower);
		addSound(S_BALEISTER, R.raw.baleister);
		addSound(S_DRUM, R.raw.drum);
		addSound(S_ARROW, R.raw.arrow);
		addSound(S_CRASH, R.raw.crash);
		addSound(S_SWING, R.raw.swing);
	}
	
	public void play(int type){
		if(!this.isSound)
			return;
		
		playSound(type);
	}
	
	public void playBtnPress(){
		if(!this.isSound)
			return;
		
		playSound(S_BTN);
	}
	
	public void playBtnPress2(){
		if(!this.isSound)
			return;
	
		playSound(S_BTN2);
	}
		
	
	public void playBGM(String fileName, boolean loop){
		if(!this.isSound)
			return;
		
		MediaPlayer mp = this.mp;
		
		if(fileName == null)
			return;
		
		try { 
			AssetFileDescriptor afd = this.mContext.getAssets().openFd("bgm/" + fileName);
			mp.reset();
			
			if(fileName.equals("game_bgm.mp3"))
				mp.setVolume(0.65f, 0.65f);
			else if(fileName.equals("main_bgm.mp3"))
				mp.setVolume(1f, 1f);
			
			mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			afd.close();
			mp.prepare();
			mp.setLooping(loop);
			mp.start();
			this.isSetMedia = true;
		}catch(IOException e){
			Log.d("SS", "BGM load error!");
		}

		this.mp = mp;
	}
	
	public void stopSound(){
		MediaPlayer mp = this.mp;
		
		if(mp != null){
			mp.reset();
			this.isSetMedia = false;
		}
		
		if(this.mSoundPool != null){
			for(int i=0; i<S_MAX; i++)
				this.mSoundPool.stop(i);
		}
	}
	
	public void pauseBGM(){
		MediaPlayer mp = this.mp;
		
		if(mp != null && this.isSetMedia && mp.isPlaying())
			mp.pause();		
	}
	
	public void resumeBGM(){
		MediaPlayer mp = this.mp;
		if(mp != null && this.isSetMedia && !mp.isPlaying())
			mp.start();
	}
	
	public void releaseMediaPlayer(){
		this.mp.release();
	}
	
	
	public void setSoundState(boolean isSound){
		this.isSound = isSound;
	}
}
