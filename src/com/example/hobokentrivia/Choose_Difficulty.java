package com.example.hobokentrivia;
	
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

	public class Choose_Difficulty extends Activity {
		
		private RadioGroup radioCatGroup;
		private RadioButton radioButton;
		private Button btnDisplay;
		private int id = 0;
		private int game_id = 0;
		private Object music_state;
		private ImageButton music_btn;
		private Button btn;
		  
		    @Override
		    protected void onCreate(Bundle savedInstanceState) {
		        super.onCreate(savedInstanceState);
		        setContentView(R.layout.choose_difficulty);		  
		        Bundle extras = getIntent().getExtras();
		        if(extras != null){
		        	if(extras.containsKey("id")){
		        		id = extras.getInt("id");
		        	}
		        	if(extras.containsKey("game_id")){
		        		game_id = extras.getInt("game_id");
		        	}
		        	if(extras.containsKey("music"))
		        		music_state = extras.get("music");
		        }
		        //set music button as it was from previous screen
		        music_btn = (ImageButton)findViewById(R.id.controlMusic);
		        if(music_state.equals("pause")){
					music_btn.setBackgroundResource(R.drawable.mute);
					music_btn.setTag("pause");
		        }
		        else if(music_state.equals("resume")){
					music_btn.setBackgroundResource(R.drawable.musicc);
					music_btn.setTag("resume");
		        }
		        
		 }
		    
		    public void onButtonClick(View v){
		    	   int selectedId = v.getId();
	        	   btn = (Button)findViewById(selectedId);
	        	   display(v);
		    }
		    
			//turn on/off music
			public void onSetMusic(View v){
				
				if(music_btn.getTag() == "pause"){
					music_btn.setBackgroundResource(R.drawable.musicc);
					stopService(new Intent(this, MusicService.class));
					music_btn.setTag("resume");
				}
				else if(music_btn.getTag() == "resume"){
					music_btn.setBackgroundResource(R.drawable.mute);
					startService(new Intent(this, MusicService.class));
					music_btn.setTag("pause");
				}
			}
		    
		    public void onHome(View view){
		    //	setContentView(R.layout.choose_difficulty);
		    }
		    
		    public void display(View view) {
		    	Intent i = new Intent(Choose_Difficulty.this, QuestionActivity.class);
		    	i.putExtra("difficulty", btn.getText());
		    	if(id > 0 && game_id > 0 ){
		    		i.putExtra("id", id);
		    		i.putExtra("game_id", game_id);
		    	}
		    	i.putExtra("music", music_btn.getTag().toString());
		    	startActivity(i);
		    	finish();
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
		    public void exit(View v){
		    	
		    	AlertDialog.Builder exitAlert = new AlertDialog.Builder(Choose_Difficulty.this);
		    	exitAlert.setMessage("Are you sure you want to exit?");
		    	
		    	
		    	exitAlert.setPositiveButton("Yes", new OnClickListener() {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		                 finish();                  
		            }
		        }); 

		    	exitAlert.setNegativeButton("No", new OnClickListener() {
		    		
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	dialog.cancel(); 
		            	
		            }
		        });
		    	
		    	
		    	exitAlert.create();
		    	exitAlert.show();
		    	
		    	

		    	
		    	
		    }
		    
		    

	}


