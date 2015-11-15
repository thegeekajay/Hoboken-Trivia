package com.example.hobokentrivia;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

	import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hobokentrivia.DBHelper;


	public class QuestionActivity extends Activity {

		protected static CharSequence result_text;
		private Button B1;
		private Button B2;
		private Button B3;
		private Button B4;
		private Button score;
		private TextView tv;
		private TextView textView1; 
		Question Q=new Question();
		Answer[] As=new Answer[4];
		int ncorrect, nwrong;
		private int questionInRound=0; 
		private boolean timer;
	    private TextView mTextField;
		private int[] index;
		private boolean skipped;
		private CountDownTimer time;
		private int ID;
		private int coins;
		private int score_total;
		Map<Integer, Question> QsSet = new HashMap<Integer, Question>();
		Map<Integer, Answer[]> AnsSet=new HashMap<Integer, Answer[]>();
		List<Integer> Index=new ArrayList<Integer>();
		DBHelper db;
		private BufferedReader reader;
		private String message;
		private int game_id;

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
			
			db=new DBHelper(this, reader);
	
		//	btnDisplay=(Button)findViewById(R.id.button1);
			textView1=(TextView)findViewById(R.id.textView1);
			tv=(TextView)findViewById(R.id.textView2);
			B1=(Button)findViewById(R.id.button2);
			B2=(Button)findViewById(R.id.button3);
			B3=(Button)findViewById(R.id.button4);
			B4=(Button)findViewById(R.id.button5);
			score=(Button)findViewById(R.id.button6);
			mTextField=(TextView)findViewById(R.id.timer);
			score.setVisibility(View.GONE);
			ncorrect=0;
			nwrong=0;
			
			Bundle extras = getIntent().getExtras();
			 if (extras != null) {
	             message= extras.getString("radio_chosen");
	             if(extras.containsKey("id")){
	            	 ID = extras.getInt("id");
	             }
	             if(extras.containsKey("game_id")){
	            	 game_id = extras.getInt("game_id");
	             }
	        }
	//		 if(ID == 0){
				 ID = db.startTracking(); //start tracking user 
	//		 }
	//		 else{
	//			 db.addNewGame(ID, game_id + 1);
	//		 }
			 
			 Log.d("userid", String.valueOf(ID));
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
		
		private void firstQuestion(){
			QsSet = db.getQuestionSet();
			index = new int[QsSet.size()];
			AnsSet = db.returnAnswerSet();
			Log.d("question set", QsSet.values().toString());
			int count = 0;
			for (Entry<Integer, Question> entry : QsSet.entrySet()) {
				index[count] = entry.getKey();
				count++;
			}
						
			db.close();
			displayQS();
		}


		private void displayQS() {
			int Qnum=questionInRound+1;
			textView1.setText("Question #"+ Qnum); //+" out of 10");
			enableAnswerButtons();
			resetColor();
			
			if(timer){
				 time = new CountDownTimer(11000, 1000) { //10 second count down


					public void onTick(long millisUntilFinished) {
				         mTextField.setText("Seconds remaining: " + millisUntilFinished / 1000);
				     }

				     public void onFinish() {
				         mTextField.setText("Time's Up!");
				         nwrong++;
				         if(questionInRound < 10){
				             nextQuestion(); 
				         }
				         else{
				        	 score.setVisibility(View.VISIBLE);
				         }
				     }
				  }.start();
			}
			
			//get next question from question hash map
			Q = QsSet.get(index[Qnum - 1]);
			
			As=AnsSet.get(Q.getId());
			Log.d("first answer", As[0].getAnswer());
			tv.setText(Q.getQuestion());
			B1.setId(As[0].getId());
			B1.setText(As[0].getAnswer());
			B2.setId(As[1].getId());
			B2.setText(As[1].getAnswer());
			B3.setId(As[2].getId());
			B3.setText(As[2].getAnswer());
			B4.setId(As[3].getId());
			B4.setText(As[3].getAnswer());
			questionInRound++;

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

		public void onHome(View view){
			startActivity(new Intent(QuestionActivity.this, Choose_Difficulty.class));

		}
		
		public void result(View view) {
			disableAnswerButtons();
			int rcolor = Color.RED;
			int gcolor=Color.GREEN;
			
			if(timer){
				time.cancel();
			}

			int selectedId=view.getId();
			Log.d("view id", String.valueOf(selectedId));
			if(selectedId != View.NO_ID){
				skipped = false;
			for (int i=0;i<4;i++){
				if(As[i].getId()==selectedId){
					if (As[i].isCorrect()==true){
						ncorrect++;

						view.setBackgroundColor(gcolor);
						result_text="Correct!";

					}
					else 	{
						nwrong++;

						view.setBackgroundColor(rcolor);
						result_text = "Sorry, that is incorrect";
						//need to display correct answer
						int id = 0;
						for(int k = 0; k < 4; k++){
							if(As[k].isCorrect()){
								 id = As[k].getId();
							}
						}
						Button B = (Button) findViewById(id);
						B.setBackgroundColor(gcolor);
						

					}
				}
			}
		}
			//answer was not selected
				else{
				time.cancel();
				Log.d("no view", "in no view part");
				//nwrong++;
				skipped = true;
				result_text = "Skipped";
				}
				
			Toast.makeText(QuestionActivity.this,QuestionActivity.result_text,Toast.LENGTH_SHORT).show();
			if(questionInRound == 10 && skipped == false){
				  score.setVisibility(View.VISIBLE);
			}
		
			
		}

		public void nextQuestion(){
		

			displayQS();
		
		}
		
		public void OnClickNextQuestion(View v)
		{
		if(!skipped)
			nextQuestion();
		}



		public void score(View view)
		{ 

			//calculate score
			coins = ncorrect - nwrong;
			score_total = ncorrect;
			if(coins < 0){
				coins = 0;
			}
			
			Intent i = new Intent(QuestionActivity.this, ScoreActivity.class);
			//pass coins and user id
			Log.d("coins", String.valueOf(coins));
	    	i.putExtra("coins", coins);
	    	i.putExtra("score", score_total);
	    	i.putExtra("id", ID);
	    	startActivity(i);
	    	finish();

		}
	}

