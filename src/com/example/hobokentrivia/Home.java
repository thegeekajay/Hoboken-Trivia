package com.example.hobokentrivia;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Home extends Activity {

	  private RadioGroup radioCatGroup;
	  private RadioButton radioButton;
	  private Button btnDisplay;
	  
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.home);
	        radioCatGroup=(RadioGroup)findViewById(R.id.radioGroup);
	        
	        btnDisplay=(Button)findViewById(R.id.button1);
	        
	        btnDisplay.setOnClickListener(new View.OnClickListener() {
	           @Override
	           public void onClick(View v) {
	              int selectedId=radioCatGroup.getCheckedRadioButtonId();
	              radioButton=(RadioButton)findViewById(selectedId);
	          //    Toast.makeText(Home.this,radioButton.getText(),Toast.LENGTH_SHORT).show();
	              display(v);
	           }	    
	    });
	 }
	    
	    public void onHome(View view){
	    	//setContentView(R.layout.home);
	    }
	    
	    public void display(View view) {
	    //	System.out.println("in display question method");
	    //	 Toast.makeText(Home.this,radioButton.getText(),Toast.LENGTH_SHORT).show();
	    	Intent i = new Intent(Home.this, QuestionActivity.class);
	    	i.putExtra("radio_chosen", radioButton.getText().toString());
	    	startActivity(i);
	    }
	    
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.main, menu);
	        return true;
	    }

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // Handle action bar item clicks here. The action bar will
	        // automatically handle clicks on the Home/Up button, so long
	        // as you specify a parent activity in AndroidManifest.xml.
	        int id = item.getItemId();
	        if (id == R.id.action_settings) {
	            return true;
	        }
	        return super.onOptionsItemSelected(item);
	    }

}
