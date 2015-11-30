package com.example.hobokentrivia;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ScoreActivity extends Activity{
	
	private  String level;
	private TextView tv1;
	private TextView tv2;
	private int ID;
	DBHelper db;
	private List<TrackUser> user_info;
	private int score;
	private TrackUser user;
	private int game_id;
	Drawable background;
	private ImageButton music_btn;
	private Object music_state;

	   protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.end_screen);
	        db=new DBHelper(this, null);
	        
	        //get score and ID
	        
	        Bundle extras = getIntent().getExtras(); 
	    	score = extras.getInt("score");
	    	ID = extras.getInt("id");
	    	music_state = extras.get("music");
	    	
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
	    	
	    	RelativeLayout layout =(RelativeLayout)findViewById(R.id.background);
	    	
	    	//determine level based on score, set background appropriately
	    	if(score >= 0 && score < 5 ){
	    		level = "novice";
	    		layout.setBackgroundResource(R.drawable.novice);
	    	}
	    	else if(score >= 5 && score < 9){
	    		level = "intermediate";
	    		layout.setBackgroundResource(R.drawable.intermediate);
	    	}
	    	else if(score >= 9){
	    		level = "expert";
	    		layout.setBackgroundResource(R.drawable.expert);
	    	}
	    	
			tv2=(TextView)findViewById(R.id.textView2);
			tv2.setText("  " + score);	
									
	    	updateUser(ID);
	   }
	   
	   public void updateUser(int id){
		  try {
			user_info = db.getTracking(id);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  SimpleDateFormat dateFormat = new SimpleDateFormat(
	                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
	        Date date = new Date();
	
		  //get most recent row for this user
	        if(user_info != null && user_info.size() > 0){
		  user = user_info.get(user_info.size() - 1);
		  user.setScore(score);
		  user.setTime_out(date);
		  game_id = user.getGameNum();
		  Long t =  user.getTime_out().getTime() - user.getTime_in().getTime(); //milliseconds
		  user.setTotal_time(t); //get difference in second		  
		 db.updateTracking(user);
	      }
	   }
   	   
	   
	   public void onRestart(View view){ //replay
		   finish();
		   Intent i = new Intent(ScoreActivity.this, Choose_Difficulty.class);
		   i.putExtra("id", ID);
		   i.putExtra("game_id", game_id);
		   i.putExtra("music", music_btn.getTag().toString());
		   startActivity(i);  
	   }
	   
	   
	   public void onExit(View view){ //close app
		   stopService(new Intent(this, MusicService.class));
            System.exit(1);
            finish();
	   }
	   
		public void onHome(View view){ //go back to home screen
			Intent i = new Intent(this, Choose_Difficulty.class);
			i.putExtra("music", music_btn.getTag().toString());
			startActivity(i);

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
		public void exit(View v){
			
			AlertDialog.Builder exitAlert = new AlertDialog.Builder(ScoreActivity.this);
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
