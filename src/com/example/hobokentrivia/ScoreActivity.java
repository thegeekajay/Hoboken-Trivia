package com.example.hobokentrivia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ScoreActivity extends Activity{
	
	private int coins;
	private  String level;
	private TextView tv1;
	private TextView tv2;

	   protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.end_screen);
	        //display coins won and expertise level
	    	Bundle extras = getIntent().getExtras(); 
	    	coins = extras.getInt("coins");
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
	    	
	   }
	   
	   
	   
	   public void onRestart(View view){ //replay
			startActivity(new Intent(ScoreActivity.this, Choose_Difficulty.class));  
	   }
	   
	   
	   public void onExit(View view){ //close app
            System.exit(1);
            finish();
	   }
	   
		public void onHome(View view){
			startActivity(new Intent(ScoreActivity.this, Choose_Difficulty.class));

		}
	   
	   
}
