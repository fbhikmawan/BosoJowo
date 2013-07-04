package com.dephi.bosojowo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;

public class Utilities {
	public static int THEME = R.style.Theme_Sherlock_Light;

	public static final int search = 10;
	public static final int help = 11;
	public static final int info = 12;

	public static final int first = 0;
	public static final int second = 1;
	public static final int third = 2;

	private Context mContext;
	private Activity mActivity;
	private EditText mEditsearch;
	private TextWatcher mTextWatcher;

	protected static final String TAG = "Utilities";

	public Utilities(Activity activity) {
		this.mActivity = activity;
	}

	public Utilities(final AdapterList adapter) {
		
	}

	/**
	 * Membuat ActionBar untuk halaman Launcher
	 * 
	 * @param context
	 *            Context yang aktif
	 * @param menu
	 *            Menu dari Activity yang aktif
	 */
	public void createActionBarWholeApp(final Context context, Menu menu) {
		createActionBarBosoJowo(context, null, menu, false);
	}

	/**
	 * Membuat ActionBar untuk halaman Search
	 * 
	 * @param context
	 *            Context yang aktif
	 * @param menu
	 *            Menu dari Activity yang aktif
	 */
	public void createActionBarSearch(final Context context, AdapterList adapter, Menu menu) {
		createActionBarBosoJowo(context, adapter, menu, true);
	}

	/**
	 * Fungsi Dasar untuk membuat ActionBar
	 * 
	 * @param context
	 *            Context yang aktif
	 * @param menu
	 *            Menu dari Activity yang aktif
	 * @param isSearching
	 *            Boolean apakah untuk launcher atau tidak
	 */
	private void createActionBarBosoJowo(final Context context, final AdapterList adapter , Menu menu,
			boolean isSearching) {
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

					DatabaseHelper dbHandler = new DatabaseHelper(context);
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

		// Sets for Info ActionBar
		int menuItemIdInfo = menu
				.add(Menu.NONE, Utilities.info,
						Utilities.third, R.string.info)
				.setIcon(R.drawable.ic_action_info)
				.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
				.getItemId();

		/*
		 * // Sets for Help Button menu.add(Menu.NONE, BosoJowoUtilities.help,
		 * BosoJowoUtilities.second, R.string.help)
		 * .setIcon(R.drawable.ic_action_help)
		 * .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		 */
	}

	public void actionBarResponseWholeApp(Activity activity, MenuItem item) {
		actionBarResponse(activity, item, false);
	}

	public void actionBarResponseSearch(Activity activity, MenuItem item) {
		actionBarResponse(activity, item, true);
	}

	private void actionBarResponse(Activity activity, MenuItem item,
			boolean isSearching) {
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
			Toast.makeText(activity, "Got click: " + item.toString(),
					Toast.LENGTH_SHORT).show();
			break;
		case android.R.id.home:
			activity.finish();
		default:
			break;
		}
	}

	/**
	 * Fungsi untuk mendukung AutoSearch, yaitu menduplikasi
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
}