package com.example.hobokentrivia;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {
	
	private ImageButton music;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startService(new Intent(this, MusicService.class));
		
		//set button to pause music
		 music = (ImageButton)findViewById(R.id.controlMusic);
		music.setTag("pause");
		music.setBackgroundResource(android.R.drawable.ic_lock_silent_mode);
		
		Button new_button = (Button)findViewById(R.id.New_Game);
		
		new_button.setOnClickListener(new OnClickListener(){
			public void onClick(View v)
			{
				Intent i = new Intent (MainActivity.this,Choose_Difficulty.class);
				i.putExtra("music", music.getTag().toString());
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

	//turn on/off music
	public void onSetMusic(View v){
		
		if(music.getTag() == "pause"){
			music.setBackgroundResource(android.R.drawable.ic_lock_silent_mode_off);
			stopService(new Intent(this, MusicService.class));
			music.setTag("resume");
		}
		else if(music.getTag() == "resume"){
			music.setBackgroundResource(android.R.drawable.ic_lock_silent_mode);
			startService(new Intent(this, MusicService.class));
			music.setTag("pause");
		}
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//stopService(new Intent(this, MusicService.class));	
	}	
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		 // stopService(new Intent(this, MusicService.class));	
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
