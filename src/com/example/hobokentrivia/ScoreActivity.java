package com.example.hobokentrivia;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ScoreActivity extends Activity{
	
	private int coins;
	private  String level;
	private TextView tv1;
	private TextView tv2;
	private int ID;
	DBHelper db;
	private List<TrackUser> user_info;
	private int score;
	private TrackUser user;
	private int game_id;

	   protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.end_screen);
	        db=new DBHelper(this, null);
	        //display coins won and expertise level
	    	Bundle extras = getIntent().getExtras(); 
	    	coins = extras.getInt("coins");
	    	score = extras.getInt("score");
	    	ID = extras.getInt("id");
	    	if(coins >= 0 && coins < 5 ){
	    		level = "Novice";
	    	}
	    	else if(coins >= 5 && coins < 9){
	    		level = "Intermediate";
	    	}
	    	else if(coins >= 9){
	    		level = "Expert";
	    	}
	    	
	    	tv1=(TextView)findViewById(R.id.textView1);
			tv2=(TextView)findViewById(R.id.textView2);
			tv1.setText("Coins Won: " + coins);
			tv2.setText("Your Hoboken expertise: " + level);
	    	updateUser(ID);
	    	Log.d("id in score activity", String.valueOf(ID));
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
	     //  String time_out = dateFormat.format(date);
		  //make updates
		  //get most recent row for this user
	        if(user_info != null && user_info.size() > 0){
	        	Log.d("udpating user info", "");
		  user = user_info.get(user_info.size() - 1);
		  user.setCoins(coins);
		  user.setScore(score);
		  user.setTime_out(date);
		  game_id = user.getGameNum();
		  Long t =  user.getTime_out().getTime() - user.getTime_in().getTime(); //milliseconds
		Log.d("total time", t.toString());
		  user.setTotal_time(t); //get difference in seconds
		  
		 db.updateTracking(user);
	        }
	   }
   	   
	   
	   public void onRestart(View view){ //replay
		   Intent i = new Intent(ScoreActivity.this, Choose_Difficulty.class);
		   i.putExtra("id", ID);
		   i.putExtra("game_id", game_id);
		   startActivity(i);  
	   }
	   
	   
	   public void onExit(View view){ //close app
            System.exit(1);
            finish();
	   }
	   
		public void onHome(View view){
			startActivity(new Intent(ScoreActivity.this, Choose_Difficulty.class));

		}
	   
	   
}
