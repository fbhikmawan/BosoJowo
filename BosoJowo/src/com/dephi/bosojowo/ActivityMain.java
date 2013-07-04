
package com.dephi.bosojowo;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class ActivityMain extends SherlockActivity{
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setTheme(Utilities.THEME);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ArrayList<HashMap<String, String>> mainCat = new DatabaseHelper(this).getMain();
        
        RelativeLayout mainlayout = (RelativeLayout)findViewById(R.id.layoutMain);
        int childcount = mainlayout.getChildCount();
        for (int i=0; i < childcount; i++){
              TextView v = (TextView)mainlayout.getChildAt(i);
              final HashMap<String, String> current = mainCat.get(i);
              v.setText(current.get("name"));
              v.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					nextSubCat(current.get("_id"));					
				}
			});
        }
        
        new DatabaseHelper(this).searchAll(new String());
        
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
		new Utilities(this).createActionBarWholeApp(this, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {    	
    	new Utilities(this).actionBarResponseWholeApp(this, item);
    	return super.onOptionsItemSelected(item);
    }
}
