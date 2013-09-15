package com.dephi.basajawa;

import java.util.ArrayList;
import java.util.HashMap;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.dephi.bosojowo.R;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * Acivity ini akan menampilkan list-list sesuai dengan kategori, subkategori
 * sampai dengan post yang telah dipilih user. Activity ini akan dipanggil 
 * berulang yang kemudian membentuk rentetan activity yg berurutan. <br> <br>
 * 
 * Pengategorian supaya pemanggilan berulang activity ini berjalan baik,
 * sebagai berikut: <br>
 * (1) PEPAK_ID >> berasal dari menu utama. <br>
 * (2) CATEGORY_ID & SUBCATEGORY_ID >> saat user memilih menu2 setelahnya. <br>
 * (3) POST_ID >> user memilih untuk melihat detail dari post tersebut.
 * 
 * @author Dephi
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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
	
	/**
     * Pengeksekusian selalu dilakukan disini terlebih dulu
     */
	@SuppressWarnings("deprecation")
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
		// dan menghandle pemasangan adapter yang nanti digunakan pada listview
		if(mSource.equals("PEPAK_ID")){
			mResultDB = mDB.getCat(mSourceID);
			mNextSource = "CATEGORY_ID";
			mAdapter=new AdapterList(this, mResultDB, AdapterList.POSITION.Category);
			mList.setOnItemClickListener(this);
		} else if(mSource.equals("CATEGORY_ID")){
			mResultDB = mDB.getSubCat(mSourceID);
			mNextSource = "SUBCATEGORY_ID";
			mAdapter=new AdapterList(this, mResultDB, AdapterList.POSITION.Category);
			mList.setOnItemClickListener(this);
		} else if(mSource.equals("SUBCATEGORY_ID")){
			mResultDB = mDB.getPostsBySubCat(mSourceID);
			mNextSource = "POSTS_ID";
			mAdapter=new AdapterList(this, mResultDB, AdapterList.POSITION.Post);
			mList.setOnItemClickListener(this);
		} else if(mSource.equals("POSTS_ID")){
			mResultDB = mDB.getPostsByID(mSourceID);
			mAdapter=new AdapterList(this, mResultDB, AdapterList.POSITION.Detail);
			mList.setOnItemClickListener(null);
		}

		// Menghilangkan dan memasangkan gambar background pada kondisi tertentu
		RelativeLayout mainView = (RelativeLayout)findViewById(R.id.layoutContent);
		if(mSource.equals("POSTS_ID") || mSource.equals("SUBCATEGORY_ID")){
			mainView.setBackgroundDrawable(null);
		} else {
			mainView.setBackgroundDrawable(getResources().getDrawable(R.drawable.contentbackground));
		}
		
		// Memasangkan adapter yang telah di-set tadi ke dalam list
        mList.setAdapter(mAdapter);  
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		// Pembuatan ActionBar ada di baris ini
    	new Utilities(this).createActionBarWholeApp(this, menu, mSource);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {   
    	// Deteksi asal pemanggilan lalu mengambil subcatID untuk 
    	// refresh list karena adany new entry
    	if(!mResultDB.isEmpty() && mResultDB.get(0).containsKey("subcatId")){
    		mSubCatIDtoAddNewEntry = Integer.parseInt(mResultDB.get(0).get("subcatId"));
		}
    	
    	// Penanganan interaksi dengan ActionBar ada di baris ini
    	new Utilities(this).actionBarResponseWholeApp(this, item, mSubCatIDtoAddNewEntry);
    	return super.onOptionsItemSelected(item);
    }
    
    /*
     * Memanggil kembali ActivityCat.java saat user melakukan interaksi di list,
     * namun format isi intent yang sama tetapi berbeda SOURCE-nya
     */
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
			// Merefresh list yang telah ada, supaya new entry terlihat
			// dan memberikan notifikasi bhw entry telah disave
			mAdapter.refresh(mSubCatIDtoAddNewEntry);
			break;
		}
	}
}
