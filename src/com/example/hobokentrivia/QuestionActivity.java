package com.example.hobokentrivia;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionActivity extends Activity {

	protected static CharSequence result_text;
	private RadioGroup answerGroup;
	private RadioButton radioButton;
	private Button B1;
	private Button B2;
	private Button B3;
	private Button B4;
	private Button btnDisplay;
	private Button score;
	private TextView tv;
	private TextView textView1; 
	Question Q=new Question();
	Answer[] As=new Answer[4];
	int ncorrect, nwrong;
	private LinearLayout answerLayout; 
	private int questionInRound=0; 
	Map<Integer, Question> QsSet=new HashMap<Integer, Question>();
	Map<Integer, Answer[]> AnsSet=new HashMap<Integer, Answer[]>();
	List<Integer> Index=new ArrayList<Integer>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_screen);
		Bundle extras = getIntent().getExtras();
		btnDisplay=(Button)findViewById(R.id.button1);
		textView1=(TextView)findViewById(R.id.textView1);
		tv=(TextView)findViewById(R.id.textView2);
		B1=(Button)findViewById(R.id.button2);
		B2=(Button)findViewById(R.id.button3);
		B3=(Button)findViewById(R.id.button4);
		B4=(Button)findViewById(R.id.button5);
		score=(Button)findViewById(R.id.button6);
		//score.setVisibility(View.GONE);
		//btnDisplay.setVisibility(View.INVISIBLE);
		//Answer answer[];
		ncorrect=0;
		nwrong=0;

		//---------------------------------------------------------------------------


		//Generate List to use it for indexing
		for (int j=1;j<=10;j++)
			Index.add(j);
		// Shuffling the list
		Collections.shuffle(Index);

		//This code to read question an pick 10 and display them
		// Reading from the file until we get the DB

		DataInputStream dis=null;
		try {
			dis = new DataInputStream(new BufferedInputStream(getAssets().open("Questions.txt")));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String Line, ques,ans;
		int id;
		int ansids;
		int correct; 
		// Reading QS from file we can remove it later..Assuming that there is a particular format 
		try {
			while (dis.available()!=0){
				Question q1=new Question();

				Answer[] anstemp=new Answer[4];
				Line=dis.readLine();
				String info[]=Line.split("-");
				id=Integer.parseInt(info[0]); 
				ques=info[1];
				q1.setId(id);
				q1.setQuestion(ques);

				QsSet.put(id, q1);

				Line=dis.readLine();
				String[] cid=Line.split(":");
				correct=Integer.parseInt(cid[1]);


				for (int i=0;i<4;i++)
				{
					Answer ans1=new Answer();

					Line=dis.readLine();
					String info2[]=Line.split("-");
					ansids=Integer.parseInt(info2[0]);
					ans=info2[1];
					ans1.setQid(id);
					ans1.setId(ansids);
					if(ansids==correct)
						ans1.setCorrect(true);
					else
						ans1.setCorrect(false);
					ans1.setAnswer(ans);
					anstemp[i]=ans1;
				}
				AnsSet.put(id, anstemp);
			}
		} catch (NumberFormatException | IOException e) {

			e.printStackTrace();
		}	
		//*************************************************************************************************

		// Displaying First Round question
		
				nextQuestion();
			
//		
//			
		
			

	}


	private void displayQS() {
		int Qnum=questionInRound+1;
		textView1.setText("Question "+Qnum+" Out of 10");
		enableAnswerButtons();
		resetColor();
		Q=QsSet.get(Index.get(questionInRound));
		As=AnsSet.get(Q.getId());
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
		//btnDisplay.setVisibility(View.INVISIBLE);
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
		startActivity(new Intent(QuestionActivity.this, Home.class));

	}
	public void result(View view) {
		disableAnswerButtons();
		
		int rcolor = Color.RED;
		int gcolor=Color.GREEN;

		int selectedId=view.getId();
		for (int i=0;i<4;i++)
			if(As[i].getId()==selectedId)

			{
				if (As[i].isCorrect()==true)
				{
					ncorrect++;

					view.setBackgroundColor(gcolor);
					result_text="Correct!";

				}
				else 
				{
					nwrong++;

					view.setBackgroundColor(rcolor);
					result_text = "Sorry, that is incorrect";

				}
			}
		Toast.makeText(QuestionActivity.this,QuestionActivity.result_text,Toast.LENGTH_SHORT).show();
      if (questionInRound<10)
	       btnDisplay.setVisibility(View.VISIBLE);
        else
        {
        	score.setVisibility(View.VISIBLE);
        	
        }

		

	}

	public void nextQuestion(){
	

		displayQS();
		btnDisplay.setVisibility(View.INVISIBLE);
		score.setVisibility(View.GONE);
		
       // btnDisplay.setVisibility(View.GONE);
	
	}
	
	public void OnClickNextQuestion(View v)
	{
		nextQuestion();
	}


	// Or go to score screen 



	public void score( View v)
	{ 
		// we should add score screen here. 
		Intent i = new Intent(getBaseContext(), ScoreActivity.class);
		startActivity(i);
		finish();
	
		tv.setText(ncorrect);
		B1.setVisibility(View.GONE);
		B2.setVisibility(View.GONE);
		B3.setVisibility(View.GONE);
		B4.setVisibility(View.GONE);
	}
}
