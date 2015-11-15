package com.example.hobokentrivia;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public class TrackUser {
	
	private int id;
	private int game_num;
	private int coins;
	private int score;
	private Date time_in;
	private Date time_out;
	private Long total_time;

	//****************** Getters and setters*****************
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGameNum() {
		return this.game_num;
	}
	public void setGameNum(int i) {
		this.game_num = i;
	}
	public int getCoins() {
		return coins;
	}
	public void setCoins(int coins) {
		this.coins = coins;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public Date getTime_in() {
		return time_in;
	}
	public void setTime_in(Date time_in2) {
		this.time_in = time_in2;
	}
	public Date getTime_out() {
		return time_out;
	}
	public void setTime_out(Date time_out2) {
		this.time_out = time_out2;
	}
	public Long getTotal_time() {
		return total_time;
	}
	public void setTotal_time(Long t) {
		this.total_time = t;
	}
	

}
