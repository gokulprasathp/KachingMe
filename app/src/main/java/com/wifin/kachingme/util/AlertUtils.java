package com.wifin.kachingme.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

public class AlertUtils {

	private boolean result;

	public boolean yes_no_dialog(Context contex) {
		AlertDialog.Builder builder = new AlertDialog.Builder(contex);
		builder.setTitle("Erase hard drive")
				.setMessage("Are you sure?")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Yes button clicked, do something

								result = true;

							}
						})

				.setNegativeButton("No", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						result = false;
					}
				}) // Do nothing on no
				.show();

		return result;
	}

	public void Toast_call(Context contex, String message) {
		Toast.makeText(contex, message, Toast.LENGTH_SHORT).show();
	}

}
