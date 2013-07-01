
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
    	setTheme(BosoJowoUtilities.THEME);
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
    	BosoJowoUtilities.createActionBarBosoJowo(this, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {    	
    	switch (item.getItemId()) {
		case BosoJowoUtilities.search:
			Toast.makeText(this, "Got click: " + item.toString(), Toast.LENGTH_SHORT).show();
			break;
		case BosoJowoUtilities.help:
			Toast.makeText(this, "Got click: " + item.toString(), Toast.LENGTH_SHORT).show();
			break;
		case BosoJowoUtilities.info:
			Toast.makeText(this, "Got click: " + item.toString(), Toast.LENGTH_SHORT).show();
			break;
		case android.R.id.home:
			finish();
		default:
			break;
		}
    	return super.onOptionsItemSelected(item);
    }
}
