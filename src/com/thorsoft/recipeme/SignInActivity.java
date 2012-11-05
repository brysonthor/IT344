package com.thorsoft.recipeme;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SignInActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        //intent is passed to Activity every time, contains text message.
        Intent intent = getIntent();
        String user = intent.getStringExtra(MainActivity.USER_NAME);
        String pass = intent.getStringExtra(MainActivity.PASS);
        TextView signInText = (TextView) findViewById(R.id.signInAs);
        
        signInText.setText("Authenticating "+user+" ...");
        //signInText.setText(pass);
       
        //implement auth-system
        boolean auth = true;
    	
    	
    	
        
        if (auth == true){
        	Intent intent1 = new Intent(this, RecipeActivity.class);
        
        	startActivity(intent1);
        	//then go to the real app
        }
        else{
        	Intent intent2 = new Intent(this, MainActivity.class);
        	startActivity(intent2);
        }
    }
    
}
