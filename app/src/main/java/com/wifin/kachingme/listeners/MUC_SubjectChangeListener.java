package com.wifin.kachingme.listeners;

import java.util.Date;

import org.jivesoftware.smackx.bookmarks.BookmarkManager;
import org.jivesoftware.smackx.muc.SubjectUpdatedListener;
import org.jxmpp.jid.EntityFullJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;

import android.content.Context;
import android.content.Intent;

import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.pojo.ContactsGetSet;
import com.wifin.kachingme.pojo.MessageGetSet;
import com.wifin.kachingme.services.TempConnectionService;
import com.wifin.kachingme.util.Log;
import com.wifin.kachingme.util.Utils;

public class MUC_SubjectChangeListener implements SubjectUpdatedListener {

	DatabaseHelper dbAdapetr;
	Context context;
	String TAG = MUC_SubjectChangeListener.class.getSimpleName();

	public MUC_SubjectChangeListener(Context con) {
		context = con;
		dbAdapetr = KachingMeApplication.getDatabaseAdapter();
	}



	@Override
	public void subjectUpdated(String subject, EntityFullJid from) {
		{
			// TODO Auto-generated method stub

			Log.d("MUC_Subject Changed...", "Changed Subject::" + subject
					+ " Who Changed::" + from);

			String ar_sendr[] = from.toString().split("/");
			String remote_jid = from.toString().split("/")[0];

			BookmarkManager bm1;
			try {
				bm1 = BookmarkManager
						.getBookmarkManager(TempConnectionService.connection);

				bm1.addBookmarkedConference(subject.toString(), JidCreate.entityBareFrom(from.toString().split("/")[0]), true,
						Resourcepart.from(Utils.getBookmarkTime()), "");

			} catch (Exception e1) {
				// ACRA.getErrorReporter().handleException(e1);
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (ar_sendr.length > 1) {
				ContactsGetSet contact = dbAdapetr.getContact(remote_jid);

				String subjetc = contact.getDisplay_name();
				if (subjetc == null)
					subjetc = "";

				if (!subjetc.equals(subject)) {

					MessageGetSet msg = new MessageGetSet();
					msg.setData(subject);
					msg.setKey_from_me(0);
					msg.setKey_id("" + new Date().getTime());
					msg.setKey_remote_jid(from.toString().split("/")[0]);
					msg.setMedia_wa_type("10");
					msg.setNeeds_push(0);
					msg.setStatus(0);
					msg.setTimestamp(new Date().getTime());
					msg.setRemote_resource(from.toString().split("/")[1]);

					dbAdapetr.setInsertMessages(msg);

					dbAdapetr.setUpdateSubject(remote_jid, subject);
					int msg_id = dbAdapetr.getLastMsgid(remote_jid);
					if (dbAdapetr.isExistinChatList(remote_jid)) {
						dbAdapetr.setUpdateChat_lits(remote_jid, msg_id);
					} else {
						dbAdapetr.setInsertChat_list(remote_jid, msg_id);
					}

					Intent login_broadcast = new Intent("group_list");
					login_broadcast.putExtra("jid", "" + remote_jid);
					context.sendBroadcast(login_broadcast);

					Intent chat_broadcast = new Intent("chat");
					chat_broadcast.putExtra("jid", "" + remote_jid);
					context.sendBroadcast(chat_broadcast);
				}
			} else {
				long l = dbAdapetr.setUpdateSubject(from.toString(), subject);
				Log.d(TAG, "Subject update status::" + l + " jid::" + from
						+ " subject::" + subject);
			}
		}
	}
}
