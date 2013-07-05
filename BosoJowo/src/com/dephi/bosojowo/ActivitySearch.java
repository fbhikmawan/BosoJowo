package com.dephi.bosojowo;

import java.util.ArrayList;
import java.util.HashMap;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;

import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ActivitySearch extends SherlockActivity {
	private ArrayList<HashMap<String, String>> resultDB;
	private ListView mList;
	private EditText searchText;
	private SQLiteDatabase db = null;
	private Cursor cursor;
	private AdapterList mAdapter;
	private DatabaseHelper mDB;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		
		mList = (ListView)findViewById(R.id.listSearch);
		
		mDB = new DatabaseHelper(this);
		String keyword = null;
		resultDB = mDB.getSearchResult(keyword);

		mAdapter=new AdapterList(this, resultDB, AdapterList.AIM.Category);
		mList.setAdapter(mAdapter);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new Utilities(this).createActionBarSearch(this, mAdapter, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		new Utilities(this).actionBarResponseSearch(this, item);
		return super.onOptionsItemSelected(item);
	}

}