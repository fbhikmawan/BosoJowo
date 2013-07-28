package com.dephi.bosojowo;

import java.util.ArrayList;
import java.util.HashMap;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class ActivityCat extends SherlockActivity implements OnItemClickListener{
	private ArrayList<HashMap<String, String>> mResultDB;
	private DatabaseHelper mDB;
	
	private ListView mList;
	private AdapterList mAdapter;
	
	private String mSource;
	private int mSourceID;
	private int mSubCatIDtoAddNewEntry;
	private String mNextSource;
	
	public static final int REQ_CODE_REFRESH = 888;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sub_cat);
		
		// Inisiasi variabel member
		mDB = new DatabaseHelper(this);
		mList = (ListView)findViewById(R.id.listContent);
		
		// Ambil Intent Ektra dari pemanggilan sebelumnya
		Intent intent = getIntent();
		mSource = intent.getStringExtra("SOURCE");
		mSourceID = intent.getIntExtra("ID", 0);
		
		// Lakukan pengambilan data di database dan set untuk intent berikutnya
		if(mSource.equals("PEPAK_ID")){
			mResultDB = mDB.getCat(mSourceID);
			mNextSource = "CATEGORY_ID";
		} else if(mSource.equals("CATEGORY_ID")){
			mResultDB = mDB.getSubCat(mSourceID);
			mNextSource = "SUBCATEGORY_ID";
		} else if(mSource.equals("SUBCATEGORY_ID")){
			mResultDB = mDB.getPostsBySubCat(mSourceID);
			mNextSource = "POSTS_ID";
		} else if(mSource.equals("POSTS_ID")){
			mResultDB = mDB.getPostsByID(mSourceID);
		}
		
		// Set adapter ke listView
		if(mSource.equals("PEPAK_ID") || mSource.equals("CATEGORY_ID")){
			mAdapter=new AdapterList(this, mResultDB, AdapterList.POSITION.Category);
		} else if(mSource.equals("SUBCATEGORY_ID")){
			mAdapter=new AdapterList(this, mResultDB, AdapterList.POSITION.Post);
		} else if(mSource.equals("POSTS_ID")){
			mAdapter=new AdapterList(this, mResultDB, AdapterList.POSITION.Detail);
		}

        mList.setAdapter(mAdapter);		
        mList.setOnItemClickListener(this);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	new Utilities(this).createActionBarWholeApp(this, menu, mSource);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {   
    	// Deteksi sumber untuk ngambil subcatID untuk new entry
    	if(!mResultDB.isEmpty()){
    		if(mResultDB.get(0).containsKey("subcatId")){
    			mSubCatIDtoAddNewEntry = Integer.parseInt(mResultDB.get(0).get("subcatId"));
    		}
		}
    	
    	// Ngrespon actionbar
    	new Utilities(this).actionBarResponseWholeApp(this, item, mSubCatIDtoAddNewEntry);
    	return super.onOptionsItemSelected(item);
    }

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {		
		Intent intent = new Intent(this, ActivityCat.class);
    	intent.putExtra("SOURCE", mNextSource);
    	intent.putExtra("ID", mAdapter.getItemDBid(arg2));
    	startActivity(intent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case (REQ_CODE_REFRESH):
			// Merefresh list yang telah ada, spya new entry terlihat
			// dan memberikan notifikasi bhw entry telah disave
			mAdapter.refresh(mSubCatIDtoAddNewEntry);
			break;
		}
	}
}
