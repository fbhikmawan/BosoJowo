package com.dephi.basajawa;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.dephi.basajawa.handler.DatabaseMainHandler;
import com.dephi.bosojowo.R;

/**
 * Acivity ini merupakan yang utama, pemanggilan pertama pada applikasi
 * (dapat dilihat di Androidmanifest.xml).
 * <br>
 * Menerapkan Sherlock ActionBar (extend SherlockActivity) sehingga dapat
 * menampilkan menu di deretan atas yang sesuai dengan kriteria umum 
 * aplikasi di OS Android.
 * <br>
 * Permanggilan/pembuatan ActionBar, dilakukan didalam barisan perintah: 
 * 		onCreateOptionsMenu(Menu menu).
 * Sedangkan respon dari ActionBar ditangani melalui barisan perintah:
 * 		onOptionsItemSelected(MenuItem item).
 * 
 * @author Dephi
 */
public class ActivityMain extends SherlockActivity{
	
    
    /**
     * Pengeksekusian selalu dilakukan disini terlebih dulu
     */
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	// Meng-set tema untuk SherockActivity
    	setTheme(Utilities.THEME);
        
    	super.onCreate(savedInstanceState);
        
        // Menentukan view/layout untuk aktif di activity ini
        setContentView(R.layout.screen_splash);
        
        // Sembunyikan ActionBar terlebih dulu untuk 
        // menampilkan SplashScreen sejenak
        getSupportActionBar().hide();
        
        // Menampilkan SplashScreen sejenak, kemudian 
        // memunculkan animasi transisi saat berpindah 
        // ke activity berikutnya
		new RunTheMain(this).execute();

    }
    
    /*
     * Memanggil halaman sleanjutnya, yaitu ActivityCat.java,
     * dengan isi (intent) berupa:
     * (1) SOURCE, dan
     * (2) ID
     * Keduanya penting untuk menentukan asal drmana activity 
     * tersebut terpanggil.
     */
    private void nextSubCat(String id) {
    	Intent intent = new Intent(this, ActivityCat.class);
    	intent.putExtra("SOURCE", "PEPAK_ID");
    	intent.putExtra("ID", Integer.parseInt(id));
    	startActivity(intent);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		// Pembuatan ActionBar ada di baris ini
		new Utilities(this).createActionBarHome(this, menu);
		return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {    	
    	// Penanganan interaksi dengan ActionBar ada di baris ini
    	new Utilities(this).actionBarResponseWholeApp(this, item);
    	return super.onOptionsItemSelected(item);
    }
    
    /**
     * Class ini adalah ekstensi (extend) dari Class AsyncTask, yang
     * memiliki 2 elemen utama: <br>
     * (1) doInBackground, elemen ini yang dijalankan pertama.<br>
     * (2) onPostExecute, lalu setelah elemen pertama selesai dijalankan,
     * maka akan melakukan elemen ini.<br><br>
     * 
     * Class ini pada dasarnya adalah untuk menahan SplashScreen 
     * beberapa saat lalu memunculkan animasi perpindahan 
     * ke halaman selanjutnya
     * 
     * @author 
     * 		Dephi
     *
     */
    private class RunTheMain extends AsyncTask<Void, Void, Void> {
    	private Activity activity;
    	
		public RunTheMain(Activity activity) {
			this.activity = activity;
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				// Lama waktu tunggu (dalam milidetik)
				Thread.sleep(3000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// Animasi perpindahan ada di baris-baris ini
			View prev = activity.findViewById(R.id.layoutSplash);
			AlphaAnimation anime0 = new AlphaAnimation(1, 0);
			anime0.setDuration(400);
			prev.startAnimation(anime0);
			setContentView(R.layout.activity_main);
			AlphaAnimation anime = new AlphaAnimation(0, 1);
			anime.setDuration(400);
			View v = findViewById(R.id.layoutMain);
			v.startAnimation(anime);
			getSupportActionBar().show();
			
			setForEachItem();
		}
	}
    
    /**
     * Untuk menampilkan icon dan tulisan serta action untuk 
     * interaksi pada tampilan menu utama
     */
	private void setForEachItem() {
		DatabaseMainHandler db = new DatabaseMainHandler(this);
		ArrayList<HashMap<String, String>> mainCat = db.getMain();
		RelativeLayout mainlayout = (RelativeLayout) findViewById(R.id.layoutMain);
		int i = 0;
		while (i < 6) {
			ImageView iv = (ImageView) mainlayout.getChildAt(i);
			final HashMap<String, String> current = mainCat.get(i);
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
