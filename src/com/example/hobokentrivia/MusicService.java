package com.example.hobokentrivia;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;

public class MusicService extends Service {
	private MediaPlayer player;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void onCreate() {
	    // TODO Auto-generated method stub
	    super.onCreate();
	       player = MediaPlayer.create(this, R.raw.music);
	       player.setAudioStreamType(AudioManager.STREAM_MUSIC);
	       player.setLooping(true); // Set looping
	       player.start();
	}
	
	
	public MediaPlayer getPlayer(){
		return player;
	}

	public void onPause(){
		player.stop();
		player.release();
	}
	
	public void onStop(){
		player.stop();
		player.release();
	}
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		player.stop();
		player.release();
		super.onDestroy();
	}





}
