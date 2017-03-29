package com.wifin.kachingme.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.wifin.kaching.me.ui.R;

public class AlertManager {

	public void showAlertDialog(Context context, String message, Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle("Kaching.Me");

		// Setting Dialog Message
		alertDialog.setMessage(message);

		if (status != null)
			// Setting alert dialog icon
			// alertDialog.setIcon((status) ? R.drawable.success :
			// R.drawable.fail);

			// Setting OK Button
			alertDialog.setButton(
					context.getResources().getString(R.string.Ok),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

						}
					});

		// Showing Alert Message
		alertDialog.show();
	}

	public static void getToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

}
