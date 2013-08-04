
package com.dephi.bosojowo;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class ActivityMain extends SherlockActivity{
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setTheme(Utilities.THEME);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_splash);
        
		new RunTheMain(this).execute();
		
        //getSupportActionBar().setIcon(icon);
        //getSupportActionBar().setTitle(R.string.home);
        //getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void nextSubCat(String id) {
    	Intent intent = new Intent(this, ActivityCat.class);
    	intent.putExtra("SOURCE", "PEPAK_ID");
    	intent.putExtra("ID", Integer.parseInt(id));
    	startActivity(intent);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		new Utilities(this).createActionBarHome(this, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {    	
    	new Utilities(this).actionBarResponseWholeApp(this, item);
    	return super.onOptionsItemSelected(item);
    }
    
    private class RunTheMain extends AsyncTask<Void, Void, Void> {
    	private Activity activity;
    	
		public RunTheMain(Activity activity) {
			this.activity = activity;
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(3000); // wait to show logo
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			View prev = activity.findViewById(R.id.layoutSplash);
			AlphaAnimation anime0 = new AlphaAnimation(1, 0);
			anime0.setDuration(400);
			prev.startAnimation(anime0);
			
			setContentView(R.layout.activity_main);
		
			AlphaAnimation anime = new AlphaAnimation(0, 1);
			anime.setDuration(400);
			View v = findViewById(R.id.layoutMain);
			v.startAnimation(anime);
			
			
			setForEachItem();
		}
	}
    
	private void setForEachItem() {
		DatabaseHelper db = new DatabaseHelper(this);

		ArrayList<HashMap<String, String>> mainCat = db.getMain();

		RelativeLayout mainlayout = (RelativeLayout) findViewById(R.id.layoutMain);
		int childcount = mainlayout.getChildCount();
		int i = 0;
		while (i < 6) {
			ImageView iv = (ImageView) mainlayout.getChildAt(i);
			final HashMap<String, String> current = mainCat.get(i);
			// iv.setText(current.get("name"));
			iv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					nextSubCat(current.get("_id"));
				}
			});
			i++;
		}
	}
}
