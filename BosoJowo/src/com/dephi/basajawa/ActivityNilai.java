package com.dephi.basajawa;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.dephi.bosojowo.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityNilai extends SherlockActivity  {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nilai);
		
		// Sets the views
		TextView tvBenar = (TextView) findViewById(R.id.tvBenar);
		TextView tvNilai = (TextView) findViewById(R.id.tvNilai);
		
		// Sets from Intent
		Intent intent = getIntent();
		int jumlahBenar = intent.getExtras().getInt("nilai");
		int nilai = jumlahBenar*10;
		
		// Sets to Layout
		tvBenar.setText("" + jumlahBenar);
		tvNilai.setText("" + nilai);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		// Pembuatan ActionBar ada di baris ini
    	new Utilities(this).createActionBarWholeApp(this, menu, getString(R.string.title_activity_activity_nilai));
        return super.onCreateOptionsMenu(menu);
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(5);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}