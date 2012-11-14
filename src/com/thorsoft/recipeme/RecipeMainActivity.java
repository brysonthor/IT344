package com.thorsoft.recipeme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

public class RecipeMainActivity extends ListActivity {
	private Context list;
	private static final String TAG_RECIPES = "recipes";
    // This is the Adapter being used to display the list's data
    SimpleCursorAdapter mAdapter;
	private static String url = null;
	protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
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
		    	String[] ing = null;
		    	toast("Got Here");
		    	try {
		    	    ingredients = result.getJSONArray(TAG_RECIPES);
		    	    ing = new String[ingredients.length()];
		    	    // looping through All Contacts
		    	    for(int i = 0; i < ingredients.length(); i++){
		    	        String ingred = ingredients.getString(i);		    	        
		                ing[i]=ingred; 
		    	    }
		    	} catch (JSONException e) {
		    	    Log.e("JSON Problem","");
		    	}
		    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(list,
		                android.R.layout.simple_list_item_checked, ing);
		        setListAdapter(adapter); 
			}
	    	@Override
	    	protected JSONObject doInBackground(String... url) {
	    		return getJSONFromUrl(url[0]);
	    		
	    	}
	    }
	    public void loadIngredients(){
	    	
	    	JSONParser jParser = new JSONParser();
		    jParser.execute(new String[] {url});
	    	
	    }
	   
	    public void onListItemClick(ListView l, View v, int position, long id) {
	        
	    	// Do something when a list item is clicked
	    	 CheckedTextView textView = (CheckedTextView)v;
	    	 textView.setChecked(!textView.isChecked());
	    	 if (position == 0){
	    		 ListView list = (ListView) findViewById(android.R.id.list);
	    		 SparseBooleanArray checked = list.getCheckedItemPositions();
	    		 for (int i = 0; i < checked.size(); i++) {
	    				Log.i("ListViewTest", "Position"+i+": " + checked.get(i));
	    			}
	    	 }
	    }
	    
	    public void toast(String s ){
	    	Context context = getApplicationContext();
	    	CharSequence text = s;
	    	int duration = Toast.LENGTH_LONG;

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

	
