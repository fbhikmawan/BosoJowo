package com.dephi.basajawa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;
import com.dephi.basajawa.handler.DatabaseMainHandler;
import com.dephi.bosojowo.R;

/**
 * Berisi fungsi2 pendukung untuk aplikasi ini, antara lain: <br>
 * 1) Pembuatan actionbar, <br>
 * 2) Pembuatan elemen-elemen yang terdapat dalam actionbar, <br>
 * 3) Pembuatan action/respon dari interaksi user pada masing2 elemen, <br>
 * 4) dan Penanganan decode gambar untuk penyimpanan di sdcard atau <br>
 * penyajian gambar di layar.
 * 
 * @author Dephi
 */
public class Utilities {
	public static int THEME = R.style.Theme_Sherlock_Light;
	
	// ID elemen ActionBar
	public static final int search = 10;
	public static final int help = 11;
	public static final int info = 12;
	public static final int add = 13;
	
	// Posisi ActionBar
	public static final int first = 0;
	public static final int second = 1;
	public static final int third = 2;

	private Activity mActivity;
	private EditText mEditsearch;
	
	// macam elemen-elemen yang nanti diisi di actionbar
	public static enum ACTBAR{
		SEARCH,
		INFO,
		HELP,
		ADD,
	}
	
	// macam sumber data
	public static enum SOURCE{
		PEPAK_ID,
		CATEGORY_ID,
		SUBCATEGORY_ID,
		POSTS_ID,
	}
	
	// Konstruktor
	public Utilities(Activity activity) {
		this.mActivity = activity;
	}
	
	/**
	 * Membuat ActionBar untuk halaman kosong
	 * 
	 */
	public void createActionBarEmpty(final SherlockActivity activity, Menu menu, String title) {
		doCreateActionBarBosoJowo(activity, null, menu, null, title, false);
	}
	
	/**
	 * Membuat ActionBar untuk halaman Home
	 * 
	 */
	public void createActionBarHome(final SherlockActivity activity, Menu menu) {
		Resources res = activity.getResources();
		String titleHome = res.getString(R.string.title_home);
		
		doCreateActionBarBosoJowo(activity, null, menu, new ACTBAR[] 
				{ACTBAR.SEARCH, ACTBAR.INFO }, titleHome,false);
	}
	
	/**
	 * Membuat ActionBar untuk halaman lain
	 * 
	 */
	public void createActionBarWholeApp(SherlockActivity activity, Menu menu, String source) {
		String title = null;
		Resources res = activity.getResources();
		activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		activity.getSupportActionBar().setHomeButtonEnabled(true);

		if(source.equals("PEPAK_ID")){
			title = res.getString(R.string.title_category);
		} else if(source.equals("CATEGORY_ID")){
			title = res.getString(R.string.title_subcategory);
		} else if(source.equals("SUBCATEGORY_ID")){
			title = res.getString(R.string.title_isi);
		} else if(source.equals("POSTS_ID")){
			title = res.getString(R.string.title_detail);
		} else {
			title = source;
		}
		
		// Memunculkan ActionBar Add pada saat halaman di posisi akhir
		if (source.equals("SUBCATEGORY_ID")) {
			doCreateActionBarBosoJowo(activity, null, menu,
					new ACTBAR[] { ACTBAR.INFO, ACTBAR.ADD }, title, false);
		} else {
			doCreateActionBarBosoJowo(activity, null, menu,
					new ACTBAR[] { ACTBAR.INFO }, title, false);
		}
	}

	/**
	 * Membuat ActionBar untuk halaman Search
	 * 
	 */
	public void createActionBarSearch(SherlockActivity activity, AdapterList adapter, Menu menu) {
		Resources res = activity.getResources();
		String title = res.getString(R.string.title_search);
		doCreateActionBarBosoJowo(activity, adapter, menu, new ACTBAR[] 
				{ACTBAR.SEARCH}, title,true);
	}

	/**
	 * Fungsi Dasar untuk membuat ActionBar
	 * 
	 * @param activity
	 *            Context yang aktif
	 * @param menu
	 *            Menu dari Activity yang aktif
	 * @param isSearching
	 *            Boolean apakah untuk launcher atau tidak
	 */
	private void doCreateActionBarBosoJowo(final SherlockActivity activity,
			final AdapterList adapter, Menu menu, ACTBAR[] whichShown, String title, boolean isSearching) {
		activity.getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>" + title + "</font>"));
		// Sets the actionbar background
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            BitmapDrawable bg = (BitmapDrawable) activity.getResources().getDrawable(R.drawable.bg_actionbar);
            activity.getSupportActionBar().setBackgroundDrawable(bg);
        }
        
		if(whichShown!=null){
			int whichShownSize = whichShown.length;
			for(int i = 0; i < whichShownSize; i++){
				switch (whichShown[i]) {
				case SEARCH:
					doCreateElementSearch(activity, adapter, menu, isSearching);
					break;
				case INFO:
					doCreateElementInfo(menu);
					break;
				case HELP:
					doCreateElemetHelp(menu);
					break;
				case ADD:
					doCreateElemetAdd(menu);
					break;
				default:
					break;
				}
			}
		}
	}
	
	// Pembuatan elemen pencarian
	private void doCreateElementSearch(final Context context,
			final AdapterList adapter, Menu menu, boolean isSearching) {
		MenuItem menuItemSearch;
		if (isSearching) {
			// Sets for Search ActionBar
			menuItemSearch = menu
					.add(Menu.NONE, Utilities.search,
							Utilities.first, R.string.search)
					.setIcon(R.drawable.ic_action_search)
					.setActionView(R.layout.collapsible_edittext)
					.setShowAsActionFlags(
							MenuItem.SHOW_AS_ACTION_IF_ROOM
									| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

			// Expands Actionbar ini
			menuItemSearch.expandActionView();

			// Sets Listener to Search ActionBar
			menuItemSearch.setOnActionExpandListener(new OnActionExpandListener() {
				@Override
				public boolean onMenuItemActionExpand(MenuItem item) {			
					return true;
				}
				
				@Override
				public boolean onMenuItemActionCollapse(MenuItem item) { //
					mActivity.finish();
					return true;
				}
			});
			
			// Sets parameters ke EditText
			mEditsearch = (EditText) menuItemSearch.getActionView();
			mEditsearch.setImeOptions(EditorInfo.IME_ACTION_GO);
			mEditsearch.setSingleLine(true);
			mEditsearch.setOnEditorActionListener(new OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView v, int actionId,
						KeyEvent event) {
					String keyword = mEditsearch.getText().toString();

					DatabaseMainHandler dbHandler = new DatabaseMainHandler(context);
					ArrayList<HashMap<String, String>> resultDB = dbHandler.getSearchResult(keyword);
					
					adapter.filter(resultDB);
					return true;
				}
			});
			
		} else {
			// Sets for Search ActionBar
			menuItemSearch = menu
					.add(Menu.NONE, Utilities.search,
							Utilities.first, R.string.search)
					.setIcon(R.drawable.ic_action_search)
					.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}
	}
	
	// Pembuatan elemen info
	private void doCreateElementInfo(Menu menu){
		// Sets for Info ActionBar
		int menuItemIdInfo = menu
				.add(Menu.NONE, Utilities.info, Utilities.third, R.string.info)
				.setIcon(R.drawable.ic_action_info)
				.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
				.getItemId();
	}
	
	// Pembuatan elemen bantuan
	private void doCreateElemetHelp(Menu menu){
		 // Sets for Help ActionBar 
		int menuItemIdHelp = menu
				.add(Menu.NONE, Utilities.help, Utilities.second, R.string.help)
		 .setIcon(R.drawable.ic_action_help)
		 .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
		 .getItemId();
	}
	
	// Pembuatan elemen penambahan entry baru
	private void doCreateElemetAdd(Menu menu) {
		// Sets for Add ActionBar 
		int menuItemIdAdd = menu
				.add(Menu.NONE, Utilities.add, Utilities.first, R.string.add)
		 .setIcon(R.drawable.ic_action_add)
		 .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
		 .getItemId();
	}
	
	// Membuat respon untuk interaksi user dengan actionbar tanpa id
	public void actionBarResponseWholeApp(Activity activity, MenuItem item) {
		doActionBarResponse(activity, item, false, 0);
	}
	
	// Membuat respon untuk interaksi user dengan actionbar dengan id
	public void actionBarResponseWholeApp(Activity activity, MenuItem item, int id) {
		doActionBarResponse(activity, item, false, id);
	}
	
	// Membuat respon untuk interaksi user dengan actionbar pencarian
	public void actionBarResponseSearch(Activity activity, MenuItem item) {
		doActionBarResponse(activity, item, true, 0);
	}
	
	/**
	 * 
	 * 
	 * @param activity
	 *            Context yang aktif
	 * @param menu
	 *            Menu dari Activity yang aktif
	 * @param isSearching
	 *            Boolean apakah untuk launcher atau tidak
	 */
	
	/**
	 * Fungsi Dasar untuk membuat respon ActionBar
	 * 
	 * @param activity Activity yang aktif saat ini
	 * @param item MenuItem yang dipilih
	 * @param isSearching Apakah respon ini untuk pencarian
	 * @param id ID yang dipilih
	 */
	private void doActionBarResponse(Activity activity, MenuItem item,
			boolean isSearching, int id) {
		Intent intent = null;
		switch (item.getItemId()) {
		case Utilities.search:
			if (isSearching) {
				Toast.makeText(activity, "Got click: " + item.toString(),
						Toast.LENGTH_SHORT).show();
				
			} else {
				Intent i = new Intent(mActivity, ActivitySearch.class);
				i.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
						| Intent.FLAG_ACTIVITY_NO_HISTORY);
				mActivity.startActivity(i);
			}
			break;
		case Utilities.help:
			Toast.makeText(activity, "Got click: " + item.toString(),
					Toast.LENGTH_SHORT).show();
			break;
		case Utilities.info:
			intent = new Intent(activity, ActivityInfo.class);
	    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    	activity.startActivity(intent);
			break;
		case Utilities.add:
			intent = new Intent(mActivity, ActivityNewPost.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
			intent.putExtra("ID", id);
			mActivity.startActivityForResult(intent, ActivityCat.REQ_CODE_REFRESH);
			break;
		case android.R.id.home:
			intent = new Intent(activity, ActivityMain.class);
	    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    	activity.startActivity(intent);
		default:
			break;
		}
	}

	/**
	 * Fungsi untuk mendukung AutoSearch, yaitu menduplikasi arraylist
	 * 
	 * @param from
	 *            ArrayList sumber
	 * @param target
	 */
	public static void copyToNewList(ArrayList<HashMap<String, String>> from,
			ArrayList<HashMap<String, String>> target) {
		for (HashMap<String, String> map : from) {
			HashMap<String, String> tmp = new HashMap<String, String>();
			HashMap<String, String> toCopy = new HashMap<String, String>();
			for (Entry<String, String> mapEntry : map.entrySet()) {
				tmp.put(mapEntry.getKey(), mapEntry.getValue());
			}
			tmp.keySet().removeAll(toCopy.keySet());
			toCopy.putAll(tmp);

			target.add(toCopy);
		}
	}
	
	/**
	 * Penanganan gambar saat user ampai pada item detail.
	 * 
	 * @param vi View yang aktif saat ini.
	 * @param current Data yang dipilih oleh user
	 * @param position Posisi data yang dipilih
	 */
	public static void arrangeItems(View vi, HashMap<String, String> current, AdapterList.POSITION position) {
		// Inisiasi elemen widget pada layout
		ImageView img = (ImageView) vi.findViewById(R.id.picture);
		TextView postOne = (TextView) vi.findViewById(R.id.postOne);
		TextView postTwo = (TextView) vi.findViewById(R.id.postTwo);
		
		// Ambil data dari Data yang dipilih oleh user kedalam tampilan
		postOne.setText(current.get("postOne"));
		postTwo.setText(current.get("postTwo"));
		String picture = current.get("picture");
		
		// Dibawah ini adalah penanganan gambar yang ditampilkan ke bagian detail,
		// Jika memiliki text "sdcard", maka dapat berarti ini adalah entry baru 
		// maka gambar diambil dari sdcard, jika tidak maka diambil dari resources. 
		if (picture.contains("sdcard"))
			switch (position) {
			case Post:
				img.setImageBitmap(Utilities.decodeBitmap(picture, 50));
				break;
			case Detail:
				img.setImageBitmap(Utilities.decodeBitmap(picture, 320));
				break;
			default:
				break;
			}
		else if (picture.equals("dangu"))
			img.setImageResource(R.drawable.dangu);
		else if (picture.equals("ontong"))
			img.setImageResource(R.drawable.ontong);
		else if (picture.equals("hanacaraka"))
			img.setImageResource(R.drawable.hanan);
		else if (picture.equals("abimanyu"))
			img.setImageResource(R.drawable.abimanyu);
		else if (picture.equals("anoman"))
			img.setImageResource(R.drawable.anoman);
		else if (picture.equals("aswotomo"))
			img.setImageResource(R.drawable.aswotomo);
		else if (picture.equals("dursasana"))
			img.setImageResource(R.drawable.dursasana);
		else if (picture.equals("gatotkaca"))
			img.setImageResource(R.drawable.gatotkaca);
		else if (picture.equals("irawan"))
			img.setImageResource(R.drawable.irawan);
		else if (picture.equals("janaka"))
			img.setImageResource(R.drawable.janaka);
		else if (picture.equals("lesmana"))
			img.setImageResource(R.drawable.lesmana);
		else if (picture.equals("nakula"))
			img.setImageResource(R.drawable.nakula);
		else if (picture.equals("ontorejo"))
			img.setImageResource(R.drawable.antareja);
		else if (picture.equals("ontoseno"))
			img.setImageResource(R.drawable.antasena);
		else if (picture.equals("kartamarma"))
			img.setImageResource(R.drawable.kartamarma);
		else if (picture.equals("sadewa"))
			img.setImageResource(R.drawable.sadewa);
		else if (picture.equals("setyaka"))
			img.setImageResource(R.drawable.setyaka);
		else if (picture.equals("setyaki"))
			img.setImageResource(R.drawable.setyaki);
		else if (picture.equals("samba"))
			img.setImageResource(R.drawable.samba);
		else if (picture.equals("sengkuni"))
			img.setImageResource(R.drawable.sengkuni);
		else if (picture.equals("udawa"))
			img.setImageResource(R.drawable.udawa);
		else if (picture.equals("werkudara"))
			img.setImageResource(R.drawable.werkudara);
		else if (picture.equals("asem"))
			img.setImageResource(R.drawable.asem);
		else if (picture.equals("jambu"))
			img.setImageResource(R.drawable.jambu);
		else 
			img.setImageResource(R.drawable.default_pic);
	}
	
	/**
	 * User memanggil fungsi ini untuk mendecode gambar.
	 * 
	 * @param urlImagePath String path dari gambar
	 * @param ukuranGambarYgDiinginkan Ukuran gambar yang diinginkan
	 * @return Bitmap hasil decode gambar
	 */
	public static Bitmap decodeBitmap(String urlImagePath, int ukuranGambarYgDiinginkan){
		return doDecodeBitmap(urlImagePath, ukuranGambarYgDiinginkan);
	}
	
	/**
	 * Proses pendecodean gambar
	 * 
	 * @param urlImagePath String path dari gambar
	 * @param ukuranGambarYgDiinginkan Ukuran gambar yang diinginkan
	 * @return Bitmap hasil decode gambar
	 */
    private static Bitmap doDecodeBitmap(String urlImagePath, int ukuranGambarYgDiinginkan){
        try {
        	File f = new File(urlImagePath);
        	
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inInputShareable = true;
            o.inPurgeable = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);
 
            //Find the correct scale value. It should be the power of 2.
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<ukuranGambarYgDiinginkan || height_tmp/2<ukuranGambarYgDiinginkan)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }
 
            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        }
        return null;
    }        
}
