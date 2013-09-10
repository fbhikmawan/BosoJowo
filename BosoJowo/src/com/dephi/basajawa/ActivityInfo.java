package com.dephi.basajawa;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.dephi.bosojowo.R;

import android.content.res.Resources;
import android.os.Bundle;

/**
 * Acivity ini untuk menampilkan info ttg aplikasi.
 * Tidak ada hal penting di sini, kecuali hanya sekedar info saja
 * 
 * @author Dephi
 */
public class ActivityInfo extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Resources res = getResources();
		String title = res.getString(R.string.title_info);
		
		new Utilities(this).createActionBarEmpty(this, menu, title);
		return super.onCreateOptionsMenu(menu);
	}
}
