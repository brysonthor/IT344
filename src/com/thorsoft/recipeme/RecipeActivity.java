package com.thorsoft.recipeme;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;


public class RecipeActivity extends ListActivity
		        implements LoaderManager.LoaderCallbacks<Cursor> {

		    // This is the Adapter being used to display the list's data
		    SimpleCursorAdapter mAdapter;

		    // These are the Contacts rows that we will retrieve
		    static final String[] PROJECTION = new String[] {ContactsContract.Data._ID,
		            ContactsContract.Data.DISPLAY_NAME};

		    // This is the select criteria
		    static final String SELECTION = "((" + 
		            ContactsContract.Data.DISPLAY_NAME + " NOTNULL) AND (" +
		            ContactsContract.Data.DISPLAY_NAME + " != '' ))";

		    @TargetApi(11)
			@Override
		    protected void onCreate(Bundle savedInstanceState) {
		        super.onCreate(savedInstanceState);

		        // Create a progress bar to display while the list loads
		        ProgressBar progressBar = new ProgressBar(this);
		        //progressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER));
		        progressBar.setLayoutParams(new LayoutParams());
		        progressBar.setIndeterminate(true);
		        getListView().setEmptyView(progressBar);

		        // Must add the progress bar to the root of the layout
		        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
		        root.addView(progressBar);

		        // For the cursor adapter, specify which columns go into which views
		        String[] fromColumns = {ContactsContract.Data.DISPLAY_NAME};
		        int[] toViews = {android.R.id.text1}; // The TextView in simple_list_item_1
		        toast(fromColumns.toString());
		        // Create an empty adapter we will use to display the loaded data.
		        // We pass null for the cursor, then update it in onLoadFinished()
		        mAdapter = new SimpleCursorAdapter(this, 
		                android.R.layout.simple_list_item_checked, null,
		                fromColumns, toViews, 0);
		        setListAdapter(mAdapter);

		        // Prepare the loader.  Either re-connect with an existing one,
		        // or start a new one.
		        getLoaderManager().initLoader(0, null, this);
		    }

		    // Called when a new Loader needs to be created
		    @TargetApi(11)
			public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		        // Now create and return a CursorLoader that will take care of
		        // creating a Cursor for the data being displayed.
		        return new CursorLoader(this, ContactsContract.Data.CONTENT_URI,
		                PROJECTION, SELECTION, null, null);
		    }

		    // Called when a previously created loader has finished loading
		    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		        // Swap the new cursor in.  (The framework will take care of closing the
		        // old cursor once we return.)
		        mAdapter.swapCursor(data);
		    }

		    // Called when a previously created loader is reset, making the data unavailable
		    @TargetApi(11)
			public void onLoaderReset(Loader<Cursor> loader) {
		        // This is called when the last Cursor provided to onLoadFinished()
		        // above is about to be closed.  We need to make sure we are no
		        // longer using it.
		        mAdapter.swapCursor(null);
		    }

		    @Override 
		    public void onListItemClick(ListView l, View v, int position, long id) {
		        // Do something when a list item is clicked
		    	 CheckedTextView textView = (CheckedTextView)v;
		    	 textView.setChecked(!textView.isChecked());
		    }
		    
		    public void toast(String s ){
		    	Context context = getApplicationContext();
		    	CharSequence text = s;
		    	int duration = Toast.LENGTH_LONG;

		    	Toast toast = Toast.makeText(context, text, duration);
		    	toast.show();
		    }
		}

		
