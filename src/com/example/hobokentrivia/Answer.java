
package com.example.hobokentrivia;

public class Answer {
private int Qid;
private int id;
private String answer;
private boolean isCorrect; 
//************************Getters and Setters********************
public int getQid() {
	return Qid;
}

public void setQid(int qid) {
	Qid = qid;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getAnswer() {
	return answer;
}
public void setAnswer(String answer) {
	this.answer = answer;
}
public boolean isCorrect() {
	return isCorrect;
}
public void setCorrect(boolean isCorrect) {
	this.isCorrect = isCorrect;
}

}
