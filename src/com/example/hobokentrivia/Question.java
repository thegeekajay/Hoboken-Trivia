package com.example.hobokentrivia;

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

public class Question {
	
	private int id;
	private String question;

	//****************** Getters and setters*****************
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	
	public String displayQuestion(){
		String s = "";
		s = s + "id = " + this.id + " question = " + this.question;
		return s;
	}
	
	
	
//	public String getOPTA() {
//		return OPTA;
//	}
//	public String getOPTB() {
//		return OPTB;
//	}
//	public String getOPTC() {
//		return OPTC;
//	}
//	public String getANSWER() {
//		return ANSWER;
//	}
//	public void setOPTA(String oPTA) {
//		OPTA = oPTA;
//	}
//	public void setOPTB(String oPTB) {
//		OPTB = oPTB;
//	}
//	public void setOPTC(String oPTC) {
//		OPTC = oPTC;
//	}
//	public void setANSWER(String aNSWER) {
//		ANSWER = aNSWER;
//	}
	
 }
