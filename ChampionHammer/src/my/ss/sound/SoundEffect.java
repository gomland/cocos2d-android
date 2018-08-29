package my.ss.sound;

import java.io.IOException;
import java.util.HashMap;

import ss.my.pah.R;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

public class SoundEffect {
	public final int S_PUNCH = 0;
	public final int S_READY = 1;
	public final int S_SCORE = 2;
	public final int S_NEW_RECORD = 3;
	public final int S_SCORE_ROTATE = 4;
	public final int S_TOUCH = 5;
	public final int S_NO_RECORD = 6;
	public final int S_HAMMER_READY = 7;
	
	private final int S_MAX = S_HAMMER_READY + 1;
	
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
	
	private void playSoundVolumeDown(int index)
	{
		float streamVolume = this.mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume / this.mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 4;
		this.mSoundPool.play((Integer) this.mSoundPoolMap.get(index), streamVolume, streamVolume, 1, 0, 1f);
	}
	
	public void playLoopedSound(int index)
	{
	    float streamVolume = this.mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	    streamVolume = streamVolume / this.mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	    this.mSoundPool.play((Integer) this.mSoundPoolMap.get(index), streamVolume, streamVolume, 1, -1, 1f);
	}

	private void loadSound(){
		addSound(S_PUNCH, R.raw.punch);
		addSound(S_READY, R.raw.ready);
		addSound(S_SCORE, R.raw.score);
		addSound(S_NEW_RECORD, R.raw.new_record);
		addSound(S_SCORE_ROTATE, R.raw.score_rotate);
		addSound(S_TOUCH, R.raw.touch);
		addSound(S_NO_RECORD, R.raw.no_record);
		addSound(S_HAMMER_READY, R.raw.hammer_ready);
	}
	
	public void play(int type){
		if(!this.isSound)
			return;
		
		if(type == S_SCORE)
			playSoundVolumeDown(type);
		else
			playSound(type);
	}
	
	public void playBtnPress(){
		if(!this.isSound)
			return;
		
		playSound(S_TOUCH);
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
			mp.setVolume(0.3f, 0.3f);
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
