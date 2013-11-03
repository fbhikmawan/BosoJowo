package com.dephi.basajawa;

import java.util.ArrayList;
import java.util.HashMap;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.dephi.basajawa.handler.DatabaseMainHandler;
import com.dephi.bosojowo.R;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Acivity ini digunakan untuk pencarian entry database sesuai dengan masukan 
 * keyword masukan dari user. Pencarian dilakkan untuk entry POST_ID saja, tidak
 * pada category ataupun subcategory <br> <br>
 * 
 * @author Dephi
 */
public class ActivitySearch extends SherlockActivity implements OnItemClickListener{
	private ArrayList<HashMap<String, String>> resultDB;
	private ListView mList;
	private AdapterList mAdapter;
	private DatabaseMainHandler mDB;
	
	/**
     * Pengeksekusian selalu dilakukan disini terlebih dulu
     */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		// Inisiasi elemen widget pada layout
		mList = (ListView)findViewById(R.id.listSearch);
		mDB = new DatabaseMainHandler(this);
		String keyword = null;
		resultDB = mDB.getSearchResult(keyword);

		mAdapter=new AdapterList(this, resultDB, AdapterList.POSITION.Category);
		mList.setAdapter(mAdapter);
		mList.setOnItemClickListener(this);
	}
	
	/*
     * Memanggil ActivityCat.java saat user melakukan interaksi di list hasil
     * pencarian, sehingga user akan masuk ke ActivityCat seperti biasa.
     */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {		
		Intent intent = new Intent(this, ActivityCat.class);
    	intent.putExtra("SOURCE", "POSTS_ID");
    	intent.putExtra("ID", mAdapter.getItemDBid(arg2));
    	startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Pembuatan ActionBar ada di baris ini
		new Utilities(this).createActionBarSearch(this, mAdapter, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Penanganan interaksi dengan ActionBar ada di baris ini
		new Utilities(this).actionBarResponseSearch(this, item);
		return super.onOptionsItemSelected(item);
	}

}