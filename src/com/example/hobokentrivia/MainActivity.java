package com.example.hobokentrivia;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		MediaPlayer mPlayer = MediaPlayer.create(MainActivity.this, R.raw.music);
//		mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//		mPlayer.setLooping(true);
//		mPlayer.start();
//	
		startService(new Intent(this, MusicService.class));
		
		Button new_button = (Button)findViewById(R.id.New_Game);
		
		new_button.setOnClickListener(new OnClickListener(){
			public void onClick(View v)
			{
				Intent i = new Intent (MainActivity.this,Choose_Difficulty.class);
			    startActivity(i);
			    finish();
			}
			});
		 
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
