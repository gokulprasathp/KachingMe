package com.wifin.kachingme.listeners;

import org.jivesoftware.smackx.muc.ParticipantStatusListener;

import com.wifin.kachingme.util.Log;

public class MUC_ParticipantStatusListener implements ParticipantStatusListener {

	public MUC_ParticipantStatusListener() {
		Log.d("Participant Listener", "Participant Listener added......");

	}

	@Override
	public void adminGranted(String arg0) {
		// TODO Auto-generated method stub
		Log.d("Participant Listener", "Admin Granted.." + arg0);
	}

	@Override
	public void adminRevoked(String arg0) {
		// TODO Auto-generated method stub
		Log.d("Participant Listener", "Admin Revoked.." + arg0);
	}

	@Override
	public void banned(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		Log.d("Participant Listener", "Banned.." + arg0);
	}

	@Override
	public void joined(String arg0) {
		// TODO Auto-generated method stub
		Log.d("Participant Listener", "Joined.." + arg0);
	}

	@Override
	public void kicked(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		Log.d("Participant Listener", "Kicked .." + arg0);
	}

	@Override
	public void left(String arg0) {
		// TODO Auto-generated method stub
		Log.d("Participant Listener", "Left.." + arg0);
	}

	@Override
	public void membershipGranted(String arg0) {
		// TODO Auto-generated method stub
		Log.d("Participant Listener", "Membership Granted.." + arg0);
	}

	@Override
	public void membershipRevoked(String arg0) {
		// TODO Auto-generated method stub
		Log.d("Participant Listener", "Membership Revoked.." + arg0);
	}

	@Override
	public void moderatorGranted(String arg0) {
		// TODO Auto-generated method stub
		Log.d("Participant Listener", "Moderator Granted.." + arg0);
	}

	@Override
	public void moderatorRevoked(String arg0) {
		// TODO Auto-generated method stub
		Log.d("Participant Listener", "Moderator Revoked.." + arg0);
	}

	@Override
	public void nicknameChanged(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ownershipGranted(String arg0) {
		// TODO Auto-generated method stub
		Log.d("Participant Listener", "Ownweship Granted.." + arg0);

	}

	@Override
	public void ownershipRevoked(String arg0) {
		// TODO Auto-generated method stub
		Log.d("Participant Listener", "Ownweship Revoked.." + arg0);
	}

	@Override
	public void voiceGranted(String arg0) {
		// TODO Auto-generated method stub
		Log.d("Participant Listener", "Voice Granted.." + arg0);

	}

	@Override
	public void voiceRevoked(String arg0) {
		// TODO Auto-generated method stub
		Log.d("Participant Listener", "Voice Revoked.." + arg0);

	}

}
