package com.example.hobokentrivia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hobokentrivia.DBHelper;


	public class QuestionActivity extends Activity {

		protected static CharSequence result_text;
		private Button B1;
		private Button B2;
		private Button B3;
		private Button B4;
		private Button score;
		private Button pauseGame;
		private Button resumeGame;
		private Button btnDisplay;
		private TextView tv;
		private TextView textView1; 
		Question Q=new Question();
		Answer[] As=new Answer[4];
		int ncorrect, nwrong;
		private int questionInRound=0; 
		private ImageView imgView1;
		private boolean timer;
	    private TextView mTextField;
		private int[] index;
		private boolean skipped;
		private CountDownTimer time;
		private CountDownTimer time2;
		private String statues=null; 
		private int ID;
		private int score_total;
		Map<Integer, Question> QsSet = new HashMap<Integer, Question>();
		Map<Integer, Answer[]> AnsSet=new HashMap<Integer, Answer[]>();
		List<Integer> Index=new ArrayList<Integer>();
		DBHelper db;
		private BufferedReader reader;
		private String message;
		private int game_id;
		private Object music_state;
		private ImageButton music_btn;
	    long tRemaining; 
	    boolean IsResumed=false; 

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.question_screen);

			 try {
				 InputStream is = getResources().getAssets().open("Questions.txt");
				 if(is != null){
				 reader = new BufferedReader(new InputStreamReader(is));
				 }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}						
			
			db=new DBHelper(this, reader); //call to db
           imgView1=(ImageView)findViewById(R.id.imageView1);
			btnDisplay=(Button)findViewById(R.id.button1);
			textView1=(TextView)findViewById(R.id.textView1);
		    imgView1=(ImageView)findViewById(R.id.imageView1);
			tv=(TextView)findViewById(R.id.textView2);
			B1=(Button)findViewById(R.id.button2);
			B2=(Button)findViewById(R.id.button3);
			B3=(Button)findViewById(R.id.button4);
			B4=(Button)findViewById(R.id.button5);
			score=(Button)findViewById(R.id.button6);
			pauseGame=(Button)findViewById(R.id.button7);
			resumeGame=(Button)findViewById(R.id.button8);
			resumeGame.setVisibility(View.GONE);
			mTextField=(TextView)findViewById(R.id.timer);
			score.setVisibility(View.GONE);
			btnDisplay.setVisibility(View.INVISIBLE);
			
			ncorrect=0;
			nwrong=0;
			
			Bundle extras = getIntent().getExtras();
			 if (extras != null) {
	             message= extras.getString("difficulty");
	             //Log.d("difficulty chosen", message);
	             if(extras.containsKey("id")){
	            	 ID = extras.getInt("id");
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
			 	 
			 
				 ID = db.startTracking(); //start tracking user 
			
			switch(message){
			case "No Time Limit":
				timer = false;
				mTextField.setVisibility(TextView.INVISIBLE);
				break;
			case "Time Limit":
				timer = true;
				break;
			}

			// Displaying First Round question
			
					firstQuestion();
		}
		
		private void firstQuestion(){ //method just for first question
			QsSet = db.getQuestionSet(); //get random set of 10 questions
			index = new int[QsSet.size()];
			AnsSet = db.returnAnswerSet(); //get answer set for the questions
			int count = 0;
			//assign index to question set
			for (Entry<Integer, Question> entry : QsSet.entrySet()) {
				index[count] = entry.getKey();
				count++;
			}
						
			db.close();
			displayQS();
		}


		private void displayQS() {
			int Qnum=questionInRound+1;	
			statues= "displayQS";
			textView1.setText("Question #"+ Qnum); //+" out of 10");
			enableAnswerButtons();
			resetColor();
			
			if(timer){
				long timerSeconds=21000;
				displayTimer(timerSeconds);

			}
					
			//get next question from question hash map
			Q = QsSet.get(index[Qnum - 1]);
			
			//get answer set for this question and set buttons
			As=AnsSet.get(Q.getId());
			tv.setText(Q.getQuestion());
			B1.setId(As[0].getId());
			B1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0); //clear check mark and 'X' icons
			B1.setText(As[0].getAnswer());
			B2.setId(As[1].getId());
			B2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			B2.setText(As[1].getAnswer());
			B3.setId(As[2].getId());
			B3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			B3.setText(As[2].getAnswer());
			B4.setId(As[3].getId());
			B4.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			B4.setText(As[3].getAnswer());
			questionInRound++;
			
		}

		private void displayTimer(long timerSeconds) {
			
			 time = new CountDownTimer(timerSeconds, 1000) { //20 second count down


					public void onTick(long millisUntilFinished) {
				         mTextField.setText(" Seconds: " + millisUntilFinished / 1000);
				       //  mTextField.setCompoundDrawablesWithIntrinsicBounds(R.drawable.clock, 0, 0, 0);
				         tRemaining=millisUntilFinished; 
				     }

				     public void onFinish() { //time's up
				    	 //show correct answer
				    	 int id = 0;
				    	 for(int k = 0; k < 4; k++){
								if(As[k].isCorrect()){
									 id = As[k].getId();
								}
							}
							Button B = (Button) findViewById(id);
							B.setBackgroundColor(Color.GREEN);
							B.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.checkbox_on_background, 0, 0, 0);
				    	 
				    	//only enable sound if user did not stop music
				    	 if(music_btn.getTag() == "pause"){
						 		MediaPlayer mPlayer = MediaPlayer.create(getBaseContext(), R.raw.time_up_v2);
								mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
								mPlayer.setVolume(100, 100);
								mPlayer.start(); 
				    	 }
				    	
				         nwrong++;
				         if(questionInRound < 10){
				        	 //need a delay of 1 second before next question is brought up to show correct answer
				        	 
				        	 new CountDownTimer(1000, 1000) { //delay 1 second

				        		   public void onTick(long millisUntilFinished) {
				        		   }

				        		   public void onFinish() {
				        		       nextQuestion();
				        		       btnDisplay.setVisibility(View.VISIBLE);
				        		   }

				        		}.start();				             
				         }
				         else{
				        	 score.setVisibility(View.VISIBLE);
				         }
				     }
				  }.start();
				  IsResumed=false;
		}


		private void disableAnswerButtons() {

			B1.setEnabled(false);
			B2.setEnabled(false);
			B3.setEnabled(false);
			B4.setEnabled(false);

		}
		private void enableAnswerButtons(){
			B1.setEnabled(true);
			B2.setEnabled(true);
			B3.setEnabled(true);
			B4.setEnabled(true);
		}
		private void resetColor(){
			
			B1.setBackgroundColor(Color.DKGRAY);
			B2.setBackgroundColor(Color.DKGRAY);
			B3.setBackgroundColor(Color.DKGRAY);
			B4.setBackgroundColor(Color.DKGRAY);

		}

		public void onHome(View view){ //home icon was pressed
			Intent i = new Intent(this, Choose_Difficulty.class);
			i.putExtra("music", music_btn.getTag().toString());
			startActivity(i);

		}
		
		public void result(View view) {
			statues="result";
			disableAnswerButtons();
			int rcolor = Color.RED;
			int gcolor=Color.GREEN;
			
			if(timer){
				time.cancel(); //stop timer
			}

			int selectedId=view.getId();
			if(selectedId != View.NO_ID){
				skipped = false;
			for (int i=0;i<4;i++){
				if(As[i].getId()==selectedId){
					if (As[i].isCorrect()==true){
						
				    	//only enable sound if user did not stop music
				    	 if(music_btn.getTag() == "pause"){						
						//play correct sound effect
						MediaPlayer mPlayer = MediaPlayer.create(getBaseContext(), R.raw.correct_answer_v2);
						mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
						mPlayer.start();
				    	 }
						//draw check mark on this button
						Button b = (Button) findViewById(selectedId);
						b.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.checkbox_on_background, 0, 0, 0);
						
						ncorrect++;

						view.setBackgroundColor(gcolor);
						result_text="Correct!";

					}
					else 	{ //wrong answer
						
				    	//only enable sound if user did not stop music
				    	 if(music_btn.getTag() == "pause"){
						//play wrong answer sound effect
						MediaPlayer mPlayer = MediaPlayer.create(getBaseContext(), R.raw.wrong_answer);
						mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
						mPlayer.start();
				    	 }
						nwrong++;

						view.setBackgroundColor(rcolor);
						result_text = "Sorry, that is incorrect";
						
						//draw 'X' on wrong answer
						Button btn = (Button) findViewById(selectedId);
						btn.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_delete, 0, 0, 0);
						//need to display correct answer
						int id = 0;
						for(int k = 0; k < 4; k++){
							if(As[k].isCorrect()){
								 id = As[k].getId();
							}
						}
						Button B = (Button) findViewById(id);
						B.setBackgroundColor(gcolor);

						B.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.checkbox_on_background, 0, 0, 0);

					}
				}
			}
		}
			//answer was not selected
				else{
				time.cancel();
				skipped = true;
				result_text = "Skipped";
				}
				
		//	Toast.makeText(QuestionActivity.this,QuestionActivity.result_text,Toast.LENGTH_SHORT).show();

			if(questionInRound == 10 && skipped == false){
				  score.setVisibility(View.VISIBLE);
			}
			else 
				btnDisplay.setVisibility(View.VISIBLE);
		
			
		}

	
			public void nextQuestion(){
		statues="nextQuestion";
		btnDisplay.setVisibility(View.INVISIBLE);
			displayQS();

		}
		
		public void OnClickNextQuestion(View v)
		{
		if(!skipped && questionInRound < 10)
			nextQuestion();
		btnDisplay.setVisibility(View.INVISIBLE);
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
	    


		public void score(View view)
		{ 

			//calculate score
			score_total = ncorrect;
			
			Intent i = new Intent(QuestionActivity.this, ScoreActivity.class);
			//pass score,user id, and music on/off to score activity class
	    	i.putExtra("score", score_total);
	    	i.putExtra("id", ID);
	    	i.putExtra("music", music_btn.getTag().toString());
	    	startActivity(i);
	    	finish();

		} 
		
		@Override
		public void onPause() {
		    super.onPause();
		  
		    
		   
		}
		
		@Override
		public void onResume() {
		    super.onResume();
		    
		    
		}

public void pause(View v)
{
	imgView1.setVisibility(View.INVISIBLE);
	B1.setVisibility(View.GONE);
	B2.setVisibility(View.GONE);
	B3.setVisibility(View.GONE);
	B4.setVisibility(View.GONE);
	tv.setVisibility(View.GONE);
	mTextField.setVisibility(View.GONE);
	textView1.setVisibility(View.GONE);
	pauseGame.setVisibility(View.GONE);
	btnDisplay.setVisibility(View.GONE);
	resumeGame.setVisibility(View.VISIBLE);
	IsResumed=false;
	if (timer )
		time.cancel();
		
	onPause();
	
	
}
public void resume(View v)
{
	B1.setVisibility(View.VISIBLE);
	B2.setVisibility(View.VISIBLE);
	B3.setVisibility(View.VISIBLE);
	B4.setVisibility(View.VISIBLE);
	tv.setVisibility(View.VISIBLE);
	imgView1.setVisibility(View.VISIBLE);
	textView1.setVisibility(View.VISIBLE);
	pauseGame.setVisibility(View.VISIBLE);
	resumeGame.setVisibility(View.GONE);
	btnDisplay.setVisibility(View.VISIBLE);

	if (timer && statues !="result")
	{ 
		mTextField.setVisibility(View.VISIBLE);
		displayTimer(tRemaining);
		/*time2=new  CountDownTimer(tRemaining, 1000){
			public void onTick(long millisUntilFinished) {
				
		         mTextField.setText("Seconds remaining: " + millisUntilFinished / 1000);
		         tRemaining=millisUntilFinished; 
		     }
			public void onFinish() {
		    	 
		 		MediaPlayer mPlayer = MediaPlayer.create(getBaseContext(), R.raw.time_up);
				mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mPlayer.start();
		    	
		         mTextField.setText("Time's Up!");
		         nwrong++;
		         if(questionInRound < 10){
		             nextQuestion(); 
		             btnDisplay.setVisibility(View.VISIBLE);//***
		         }
		         else{
		        	 score.setVisibility(View.VISIBLE);
		         }
		     }
		  }.start();
		*/
		
			
		

	}
	if(statues=="result")
	{mTextField.setVisibility(View.VISIBLE);
		displayQS();
	}
		
	}
	}
	

