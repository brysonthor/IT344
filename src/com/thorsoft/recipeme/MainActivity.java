package com.thorsoft.recipeme;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	public final static String USER_NAME = "com.thorsoft.recipeme.signInUser";
	public final static String PASS = "com.thorsoft.recipeme.signInPass";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    public void signIn(View view){
    	//Intent intent = new Intent(this, SignInActivity.class);
    	EditText editText = (EditText) findViewById(R.id.userName);
    	EditText editText2 = (EditText) findViewById(R.id.passwordField);
    	String user = editText.getText().toString();
    	String pass = editText2.getText().toString();
    	//intent.putExtra(USER_NAME, user);
    	//intent.putExtra(PASS, pass);
    	//startActivity(intent);
    	boolean auth = true;
     	
     	
     	
         
        if (auth == true){
        	toast("Authenticated");
         	Intent intent1 = new Intent(this, RecipeActivity.class);
         
         	startActivity(intent1);
         	//then go to the real app
        }
    
    }
    public String hashSha256(String toHash){
		
    	String Hashed = null;
    	MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	try {
			md.update(toHash.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Change this to "UTF-16" if needed
    	byte[] digest = md.digest();
    	return digest.toString();
    	
    }
    public void toast(String s ){
    	Context context = getApplicationContext();
    	CharSequence text = s;
    	int duration = Toast.LENGTH_SHORT;

    	Toast toast = Toast.makeText(context, text, duration);
    	toast.show();
    }
}
