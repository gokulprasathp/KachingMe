package com.wifin.kachingme.listeners;

import org.jivesoftware.smackx.muc.ParticipantStatusListener;
import org.jxmpp.jid.EntityFullJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.parts.Resourcepart;

import com.wifin.kachingme.util.Log;

public class MUC_ParticipantStatusListener implements ParticipantStatusListener {

	public MUC_ParticipantStatusListener() {
		Log.d("Participant Listener", "Participant Listener added......");

	}


	@Override
	public void joined(EntityFullJid participant) {

	}

	@Override
	public void left(EntityFullJid participant) {

	}

	@Override
	public void kicked(EntityFullJid participant, Jid actor, String reason) {

	}

	@Override
	public void voiceGranted(EntityFullJid participant) {

	}

	@Override
	public void voiceRevoked(EntityFullJid participant) {

	}

	@Override
	public void banned(EntityFullJid participant, Jid actor, String reason) {

	}

	@Override
	public void membershipGranted(EntityFullJid participant) {

	}

	@Override
	public void membershipRevoked(EntityFullJid participant) {

	}

	@Override
	public void moderatorGranted(EntityFullJid participant) {

	}

	@Override
	public void moderatorRevoked(EntityFullJid participant) {

	}

	@Override
	public void ownershipGranted(EntityFullJid participant) {

	}

	@Override
	public void ownershipRevoked(EntityFullJid participant) {

	}

	@Override
	public void adminGranted(EntityFullJid participant) {

	}

	@Override
	public void adminRevoked(EntityFullJid participant) {

	}

	@Override
	public void nicknameChanged(EntityFullJid participant, Resourcepart newNickname) {

	}
}
