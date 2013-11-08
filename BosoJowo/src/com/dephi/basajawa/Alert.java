package com.dephi.basajawa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Alert {
	/**
	 * Displays common alert dialog based on given parameter (with no response
	 * on OK clicked)
	 * 
	 * @param context
	 *            Context of activity which show the alert dialog
	 * @param title
	 *            Title of alert dialog
	 * @param message
	 *            Message of alert dialog
	 * @param status
	 *            true if success, false if failed
	 */
	public void showAlertDialog(final Context context, String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Parameter
		alertDialog.setTitle("Attention");
		alertDialog.setMessage(message);

		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});

		alertDialog.show();
	}

	

}
