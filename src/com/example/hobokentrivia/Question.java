package com.example.hobokentrivia;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Question extends Activity {
	
	 protected static CharSequence result_text;
	private RadioGroup answerGroup;
	  private RadioButton radioButton;
	  private Button btnDisplay;
	
    protected void onCreate(Bundle savedInstanceState) {
    	 String message = "";
    	 String question = "";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_screen);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
             message= extras.getString("radio_chosen");
        }
        switch(message){
        
        case "No Time Limit":
         question = "Hoboken is the birthplace of what sport?";   	
        	break;
        
        }
        
        TextView tv=(TextView)findViewById(R.id.textView2);
        tv.setText(question);
        	
    }
    
    public void onHome(View view){
    	startActivity(new Intent(Question.this, Home.class));
    //	onCreate(new Bundle());
    //	setContentView(R.layout.home);
    }
    
    public void result(View view) {
    	 answerGroup=(RadioGroup)findViewById(R.id.radioGroup);
	        
	        btnDisplay=(Button)findViewById(R.id.button1);

	              int selectedId=answerGroup.getCheckedRadioButtonId();
	              radioButton=(RadioButton)findViewById(selectedId);
	              if(!radioButton.getText().toString().equals("Baseball")){
	            	  int color = Color.RED;
	            	 // radioButton.setTextColor(color);
	            	  radioButton.setBackgroundColor(color);
	            	  result_text = "Sorry, that is incorrect";
	            	  //RadioButton correctAnswer=(RadioButton)findViewById(id);
	            	  
	              }
	              else{
	            	  int color = Color.GREEN;
	            	  radioButton.setBackgroundColor(color); 
	            	  result_text="Correct!";
	              }
	              Toast.makeText(Question.this,Question.result_text,Toast.LENGTH_SHORT).show();
	           }	    
 }
