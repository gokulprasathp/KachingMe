package com.wifin.kachingme.util;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.wifin.kachingme.util.Log;

public class Self_Desruct_Group {

	Context context;

	public Self_Desruct_Group(Context c) {
		context = c;
	}

	public void setDestruct(String message_id, long time, String jid) {
		Log.d("Self_destruction_Message", "set self destruction message id::"
                + message_id + " time::" + time + " jid::" + jid);
		long milsec = time * 1000;
		Calendar calendar = Calendar.getInstance();
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent11 = new Intent("sec");
		intent11.putExtra("ID", 1);
		intent11.putExtra("message_id", message_id);
		intent11.putExtra("jid", jid);
		PendingIntent pi = PendingIntent.getBroadcast(context,
				Integer.parseInt(message_id), intent11, 0);
		// am.setRepeating(AlarmManager.RTC_WAKEUP,(calendar.getTimeInMillis()+milsec),
		// AlarmManager.INTERVAL_DAY , pi);

		am.set(AlarmManager.RTC_WAKEUP, (calendar.getTimeInMillis() + milsec),
				pi);
	}

}
