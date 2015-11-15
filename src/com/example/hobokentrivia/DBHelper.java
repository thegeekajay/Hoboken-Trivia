package com.example.hobokentrivia;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper{
	
	private static final int DATABASE_VERSION = 14;
	// Database Name
	private static final String DATABASE_NAME = "Hoboken_Trivia";
	// tasks table name
	private static final String TABLE_QUESTION = "Question";
	private static final String TABLE_TRACK_USER = "Track_User";
	// tasks Table Columns names
	private static final String KEY_ID = "ID";
	private static final String KEY_QUESTION = "QUESTION";
	private static final String KEY_ANSWER = "ANSWER"; //correct option
	private static final String KEY_OPTA= "OPTA"; //option a
	private static final String KEY_OPTB= "OPTB"; //option b
	private static final String KEY_OPTC= "OPTC"; //option c
	private static final String KEY_OPTD= "OPTD"; //option d
	private static final String KEY_USERID = "USERID";
	private static final String KEY_SCORE = "SCORE";
	private static final String KEY_COINS = "COINS";
	private static final String KEY_GAME_NUM = "GAME_NUMBER";
	private static final String KEY_TIME_IN = "TIME_IN";
	private static final String KEY_TIME_OUT = "TIME_OUT";
	private static final String KEY_TOTAL_TIME = "TOTAL_TIME";
	private SQLiteDatabase dbase;
	private Scanner file;
	private String Line;
	private int id;
	private String ans;
	private int ansids;
	private int correct; 
	private String ques;
	private Map<Integer, Question> QsSet=new HashMap<Integer, Question>();
	private Map<Integer, Answer[]> AnsSet=new HashMap<Integer, Answer[]>();
	private List<String> Index=new ArrayList<String>();
	private ArrayList<String> choices = new ArrayList<String>();
	private Context contxt;
	private BufferedReader reader;
	
	public DBHelper(Context context, BufferedReader reader) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		contxt = context;
		this.reader = reader;
		}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		dbase = db;
		String questions = "CREATE TABLE IF NOT EXISTS " + TABLE_QUESTION + "("
		+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_QUESTION
		+ " TEXT, " + KEY_ANSWER + " TEXT, " + KEY_OPTA +" TEXT, "
		+KEY_OPTB + " TEXT, " + KEY_OPTC + " TEXT, " + KEY_OPTD + " TEXT)";
		
		String user = "CREATE TABLE IF NOT EXISTS " + TABLE_TRACK_USER + "("
				+ KEY_USERID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_SCORE
				+ " INTEGER, " + KEY_COINS + " INTEGER, " + KEY_GAME_NUM + " INTEGER, " 
				+ KEY_TIME_IN +" DATETIME, "+KEY_TIME_OUT + " DATETIME, " + KEY_TOTAL_TIME + " TIME, " + 
				"UNIQUE (" + KEY_USERID + ", " + KEY_GAME_NUM + "))";
		dbase.execSQL(questions);
		dbase.execSQL(user);
		try {
			loadQuestions(reader);
			//startTracking();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void addNewGame(int id, int gameid){
		dbase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		  SimpleDateFormat dateFormat = new SimpleDateFormat(
	                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
	        Date date = new Date();
	        values.put(KEY_USERID, id);
	        values.put(KEY_GAME_NUM, gameid);
	        values.put(KEY_SCORE, 0);
	        values.put(KEY_COINS, 0);
	    	values.put(KEY_TIME_IN, dateFormat.format(date)); //time in is current time
	     
	    	dbase.insert(TABLE_TRACK_USER, null, values);
	}
	
	public int startTracking(){
		dbase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        //dateFormat.format(date);
		
		values.put(KEY_SCORE, 0);
		values.put(KEY_COINS, 0);
		values.put(KEY_GAME_NUM, 1);
		values.put(KEY_TIME_IN, dateFormat.format(date)); //time in is current time
		Log.d("values", values.toString());
		
		dbase.insert(TABLE_TRACK_USER, null, values);
		String selectQuery = "SELECT MAX(USERID) FROM " + TABLE_TRACK_USER;
		Cursor cursor = dbase.rawQuery(selectQuery, null);
	//	Log.d("cursor for id", cursor.getString(0));
		cursor.moveToFirst();
		int id = cursor.getInt(0);
		cursor.close();
		return id;	
		
	}
	
	public void updateTracking(TrackUser user){
		dbase = this.getWritableDatabase();
		Log.d("in update tracking id", String.valueOf(user.getId()));
		Log.d("updated coins", String.valueOf(user.getCoins()));
		ContentValues values = new ContentValues();
		values.put(KEY_GAME_NUM, user.getGameNum());
		values.put(KEY_SCORE, user.getScore());
		values.put(KEY_COINS, user.getCoins());
		values.put(KEY_TIME_IN, user.getTime_in().toString());
		values.put(KEY_TIME_OUT, user.getTime_out().toString());
		values.put(KEY_TOTAL_TIME, user.getTotal_time().toString());
		String whereClause = " USERID = " + user.getId() + " AND GAME_NUMBER = " + user.getGameNum();
		dbase.update(TABLE_TRACK_USER, values, whereClause, null);

	}
	
	public List<TrackUser> getTracking(int userid) throws ParseException {
		List<TrackUser> user = new ArrayList<TrackUser>();
		dbase = this.getReadableDatabase();
		
		  SimpleDateFormat dateFormat = new SimpleDateFormat(
	                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
	        Date date = new Date();
		Log.d("before select id", String.valueOf(userid));
		String selectQuery = "SELECT * FROM " + TABLE_TRACK_USER + " WHERE USERID = " + userid;
		
		Cursor cursor = dbase.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do
					{
						TrackUser usr = new TrackUser();
						usr.setId(userid);
						usr.setGameNum(cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_GAME_NUM)));
						usr.setCoins(cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_COINS)));
						usr.setScore(cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_SCORE)));
						Date time_in = dateFormat.parse(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TIME_IN)));
						usr.setTime_in(time_in);
					//	Date time_out = dateFormat.parse(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TIME_OUT)));
					//	usr.setTime_out(time_out);
					//	Date total_time = dateFormat.parse(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TOTAL_TIME)));
					//	Long total = total_time.getTime();
					//	usr.setTotal_time(total);
						user.add(usr);				

				}
			while (cursor.moveToNext());
		}
		cursor.close();
		// return list
		return user;
		
	}
	
	public void loadQuestions(BufferedReader reader) throws IOException{

		if(reader != null){
		Log.d("inside load questions method", "");
		while(reader.ready() && (Line = reader.readLine()) != null){
			Question q1=new Question();

			Answer[] anstemp = new Answer[4];
			//Log.d("line", Line);
			String info[] = Line.split("-");
			
			id=Integer.parseInt(info[0].trim()); 
			ques=info[1].trim();
			q1.setId(id);
			q1.setQuestion(ques);

			QsSet.put(id, q1);
			
			Line = reader.readLine();
			String[] cid=Line.split(":");
			correct=Integer.parseInt(cid[1].trim());
			
			for (int i=0;i<4;i++)
			{
				Answer ans1=new Answer();
				Line = reader.readLine();
				String info2[]=Line.split("-");
				ansids=Integer.parseInt(info2[0].trim());
				ans=info2[1].trim();
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
			addQuestion(q1, anstemp);
		}
	}
	}
	
	public void addQuestion(Question q, Answer[] answer){
		ContentValues values = new ContentValues();
		values.put(KEY_QUESTION, q.getQuestion());

		
		for(int i = 0; i < answer.length; i++){
			if(answer[i].isCorrect()){
				values.put(KEY_ANSWER, answer[i].getAnswer());
			}
		}

			values.put(KEY_OPTA, answer[0].getAnswer());
			values.put(KEY_OPTB, answer[1].getAnswer());
			values.put(KEY_OPTC, answer[2].getAnswer());
			values.put(KEY_OPTD, answer[3].getAnswer());

		// Inserting Row
		dbase.insert(TABLE_QUESTION, null, values);
	}
	
	
	public Map<Integer, Question> getQuestionSet() {
	//	List<Question> quesList = new ArrayList<Question>();
		// Select All Query
		dbase = this.getReadableDatabase();
		String selectQuery = "SELECT * FROM " + TABLE_QUESTION + " ORDER BY RANDOM() LIMIT 10";
		
		Cursor cursor = dbase.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				
				Question quest = new Question();
				int id = cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_ID));
				quest.setId(id);
				quest.setQuestion(cursor.getString(1));
			//	Log.d("question", quest.displayQuestion());
				QsSet.put(id, quest);
				
				String correct = "";
				correct = cursor.getString(2);
				
				//set answers
				Answer[] answer_set = new Answer[4];

				int cursor_pos = 3;
				for(int i = 0; i < 4; i++){
					Answer answer = new Answer();
					answer.setId(i);	
					answer.setQid(id);	
					
					if(cursor_pos == 3){
					//	Log.d("opt A", cursor.getString(3));
						answer.setAnswer(cursor.getString(3));
						if(cursor.getString(3).equals(correct)){
							answer.setCorrect(true);
						}
					}
					else if(cursor_pos == 4){
					//	Log.d("opt B", cursor.getString(4));
						answer.setAnswer(cursor.getString(4));
						if(cursor.getString(4).equals(correct)){
							answer.setCorrect(true);
						}
					}
					else if(cursor_pos == 5){
					//	Log.d("opt C", cursor.getString(5));
						answer.setAnswer(cursor.getString(5));
						if(cursor.getString(5).equals(correct)){
							answer.setCorrect(true);
						}
					}
					else if(cursor_pos == 6){
					//	Log.d("opt D", cursor.getString(6));
						answer.setAnswer(cursor.getString(6));
						if(cursor.getString(6).equals(correct)){
							answer.setCorrect(true);
						}
					}
					
					answer_set[i] = answer;
					cursor_pos++;
				}
				AnsSet.put(id, answer_set);
			} while (cursor.moveToNext());
		}
		cursor.close();
		// return quest list
		return QsSet;
	}
	
	public  Map<Integer, Answer[]> returnAnswerSet(){
		return this.AnsSet;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
		// Create tables again
		onCreate(db);
		
	}

}
