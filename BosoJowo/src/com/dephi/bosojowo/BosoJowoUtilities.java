package com.dephi.bosojowo;

import java.util.Locale;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;

public class BosoJowoUtilities {
	public static int THEME = R.style.Theme_Sherlock_Light;

	public static final int search = 10;
	public static final int help = 11;
	public static final int info = 12;

	public static final int first = 0;
	public static final int second = 1;
	public static final int third = 2;
	
	private EditText mEditsearch;
	private TextWatcher textWatcher = new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String text = editsearch.getText().toString()
						.toLowerCase(Locale.getDefault());
				adapter.filter(text);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

		};
	
	public BosoJowoUtilities() {
		
	}
	
	public void createActionBarBosoJowo(final Context context, Menu menu) {		
		// Sets for Search ActionBar 
		MenuItem menuItemSearch = menu.add(Menu.NONE, BosoJowoUtilities.search, BosoJowoUtilities.first,
				R.string.search)
				.setIcon(R.drawable.ic_action_search)
				.setActionView(R.layout.collapsible_edittext)
				.setShowAsActionFlags(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		
		// Sets Listener to TextChanged
		mEditsearch = (EditText) menuItemSearch.getActionView(); 
		mEditsearch.addTextChangedListener(watcher);
		
		// Sets Listener to Search ActionBar
		menuItemSearch.setOnActionExpandListener(new OnActionExpandListener() {
			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
				// Fokus pada EditText Search tersebut
				item.getActionView().requestFocus();

				// Memaksa keyboard muncul saat expanded
				InputMethodManager imm = (InputMethodManager) context
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
				return true;
			}

			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				// Reset EditText dan menghilangkan fokus
				EditText searchText = (EditText) item.getActionView();
				searchText.setText("");
				searchText.clearFocus();
				return true;
			}
		});
		
		
		// Sets for Info ActionBar
		int menuItemIdInfo = menu.add(Menu.NONE, BosoJowoUtilities.info, BosoJowoUtilities.third,
				R.string.info).setIcon(R.drawable.ic_action_info)
				.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
				.getItemId();
		
		/*
		 * // Sets for Help Button menu.add(Menu.NONE, BosoJowoUtilities.help,
		 * BosoJowoUtilities.second, R.string.help)
		 * .setIcon(R.drawable.ic_action_help)
		 * .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		 */

	}
}
