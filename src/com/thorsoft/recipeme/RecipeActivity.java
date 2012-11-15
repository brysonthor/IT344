package com.thorsoft.recipeme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class RecipeActivity extends ListActivity 
			implements LoaderManager.LoaderCallbacks<Cursor> {
			public final static String URL = "com.thorsoft.recipeme.url";
			public final static String INGREDIENTS = "com.thorsoft.recipeme.ingredients";
			private Context list;
			public String[] ing;
			public HashMap<Integer,String> checked;
		    // This is the Adapter being used to display the list's data
		    SimpleCursorAdapter mAdapter;
		    // url to make re10.4.53.19910.4.53.199quest
		    private static String url = "http://192.168.100.180:8080/";//ingredients/";
		    private static String subUrl = "ingredients/";
		    // JSON Node names
		    private static final String TAG_INGRE = "ingredients";
		   

		    @SuppressLint("UseSparseArrays")
			@TargetApi(11)
			@Override
		    protected void onCreate(Bundle savedInstanceState) {
		        super.onCreate(savedInstanceState);
		        checked = new HashMap<Integer,String>();
		        //http://www.vogella.com/articles/AndroidListView/article.html
		        //http://www.androidhive.info/2012/01/android-json-parsing-tutorial/
		        list = (Context)this;
		        loadIngredients();
		    }
		    private class JSONParser extends AsyncTask<String,Void,JSONObject> {
		    	 
		        InputStream is = null;
		        JSONObject jObj = null;
		        String json = "";
		     
		        // constructor
		        public JSONParser() {
		     
		        }
		     
		        public JSONObject getJSONFromUrl(String url) {
		     
		            // Making HTTP request
		            try {
		                // defaultHttpClient
		                DefaultHttpClient httpClient = new DefaultHttpClient();
		                HttpGet httpGet = new HttpGet(url);
		     
		                HttpResponse httpResponse = httpClient.execute(httpGet);
		                HttpEntity httpEntity = httpResponse.getEntity();
		                is = httpEntity.getContent();           
		     
		            } catch (UnsupportedEncodingException e) {
		                e.printStackTrace();
		            } catch (ClientProtocolException e) {
		                e.printStackTrace();
		            } catch (IOException e) {
		                e.printStackTrace();
		            }
		     
		            try {
		                BufferedReader reader = new BufferedReader(new InputStreamReader(
		                        is, "iso-8859-1"), 8);
		                StringBuilder sb = new StringBuilder();
		                String line = null;
		                while ((line = reader.readLine()) != null) {
		                    sb.append(line + "\n");
		                }
		                is.close();
		                json = sb.toString();
		            } catch (Exception e) {
		                Log.e("Buffer Error", "Error converting result " + e.toString());
		            }
		            
		            // try parse the string to a JSON object
		            try {
		                jObj = new JSONObject(json);
		            } catch (JSONException e) {
		                Log.e("JSON Parser", "Error parsing data " + e.toString()+json);
		            }
		     
		            // return JSON String
		            return jObj;
		     
		        }
		        protected void onPostExecute(JSONObject result) {
					JSONArray ingredients = null;
					//toast("Got Here");
			    	if(result != null){
				    	try {
				    	    ingredients = result.getJSONArray(TAG_INGRE);
				    	    ing = new String[ingredients.length()];
				    	    // looping through All Contacts
				    	    for(int i = 0; i < ingredients.length(); i++){
				    	        String ingred = ingredients.getString(i);		    	        
				                ing[i]=ingred; 
				    	    }
				    	} catch (JSONException e) {
				    	    Log.e("JSON Problem","");
				    	}
			    	}
			    	if (ing != null){
			    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(list,
			                android.R.layout.simple_list_item_checked, ing);
			        setListAdapter(adapter); 
			    	}
			    	else{
			    		toast("Server Not Reachable");
			    	}
				}
		    	@Override
		    	protected JSONObject doInBackground(String... url) {
		    		return getJSONFromUrl(url[0]);
		    		
		    	}
		    }
		    public void loadIngredients(){
		    	
		    	JSONParser jParser = new JSONParser();
			    jParser.execute(new String[] {url+subUrl});
		    	
		    }
		   
		    public void onListItemClick(ListView l, View v, int position, long id) {
		        String sendable = "null";
		    	// Do something when a list item is clicked
		    	
		        if (position == 0){
		        	if(checked.isEmpty()){
		        		toast("No Ingredients Choosen");
		        	}
		        	else{
		        		Iterator<String> i = checked.values().iterator();
		        		while(i.hasNext()){
		        			sendable += ","+i.next();
		        		}
		        		Intent intent = new Intent(this, RecipeMainActivity.class);
				    	intent.putExtra(URL, url);
				     	intent.putExtra(INGREDIENTS, sendable);
				     	startActivity(intent);
		        	}
		    	}
		        
		        
		        CheckedTextView textView = (CheckedTextView)v;
		    	textView.setChecked(!textView.isChecked());
		    	if(checked.containsKey((Integer)position)){
		    		checked.remove(((Integer)position));
		    	}
		    	else{
		    		checked.put(((Integer)position), ing[position]);
		    	}
		    	//toast(((Integer)position).toString());
		    	//Intent intent = new Intent(this, RecipeMainActivity.class);
		    	//intent.putExtra(URL, url);
		     	//intent.putExtra(INGREDIENTS, ingrList);
		     	//startActivity(intent);
		  
		    }
		    
		    public void toast(String s ){
		    	Context context = getApplicationContext();
		    	CharSequence text = s;
		    	int duration = Toast.LENGTH_SHORT;

		    	Toast toast = Toast.makeText(context, text, duration);
		    	toast.show();
		    }

			public Loader<Cursor> onCreateLoader(int id, Bundle args) {
				// TODO Auto-generated method stub
				return null;
			}

			public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
				// TODO Auto-generated method stub
				
			}

			public void onLoaderReset(Loader<Cursor> arg0) {
				// TODO Auto-generated method stub
				
			}
			

			  
		}

		
