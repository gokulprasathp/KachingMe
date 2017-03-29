package com.wifin.kachingme.kaching_feature.karaoke;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import com.google.android.youtube.player.YouTubePlayerView;
import com.wifin.kachingme.chat.single_chat.ChatTest;
import com.wifin.kachingme.chat.muc_chat.MUCTest;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.util.CommonMethods;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Log;
import com.wifin.kaching.me.ui.R;

public class KaraokeYoutube extends YouTubeBaseActivity implements
		YouTubePlayer.OnInitializedListener, OnSeekBarChangeListener {
	public static final String API_KEY = "AIzaSyBtP8tvaY1H8nAi1jhSbo5_7Cd7DUoIG9M";
	FrameLayout bodyLayout;
	LinearLayout mMusicConrolLayout;
	TextView songNameText, kachingMeText;
	ImageView serializerImage;
	Button playButton, pauseButton, shareButton, recoreButton, cancelBtn,
			resetBtn;

	int mScreenHeight = 0, mScreenWidth = 0;
	int mScreenHeightInDP = 0, mScreenWidthInDP = 0;
	private MediaRecorder myAudioRecorder;
	// http://youtu.be/<VIDEO_ID>
	public static final String VIDEO_ID = com.wifin.kachingme.util.Constant.url;
	MediaPlayer mediaPlayer;
	private String outputFile = null;
	SharedPreferences sharedPrefs;
	int Count = 1;
	boolean mClickedPlay = false;
	boolean mClickedStop = false;
	TextView mDurationText;
	public RefreshHandler handler = new RefreshHandler();
	Timer timer;
	SeekBar mseekBar;
	Handler seekHandler = new Handler();
	boolean mclickedMediaPlayer = false;
	int count = 0;
	boolean Media_play = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/** attaching layout xml **/
		setContentView(R.layout.karoake_youtube);
		Constant.printMsg("vidioid" + VIDEO_ID + "        " + com.wifin.kachingme.util.Constant.url);
		/** Initializing YouTube player view **/
		YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
		youTubePlayerView.initialize(API_KEY, this);

		initVariable();
		screenArrangement();
		mediaPlayer = new MediaPlayer();
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		songNameText.setText(com.wifin.kachingme.util.Constant.mSong);
		mseekBar.setOnSeekBarChangeListener(this);

		boolean success = (new File(Constant.local_audio_dir)).mkdirs();
		if (!success) {
			Log.w("directory not created", "directory not created");
		}
		outputFile = Constant.local_audio_dir + System.currentTimeMillis()
				+ ".amr";
		mseekBar.setFocusable(false);
		mseekBar.setProgress(0);
		mseekBar.setMax(100);

		myAudioRecorder = new MediaRecorder();
		myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		myAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
		myAudioRecorder.setOutputFile(outputFile);
		recoreButton.setBackgroundResource(R.drawable.play);

		cancelBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(KaraokeYoutube.this, KaraokeTab.class);
				startActivity(i);
			}
		});
		resetBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(KaraokeYoutube.this, KaraokeYoutube.class);
				startActivity(i);
				finish();
			}
		});

		recoreButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
					// TODO Auto-generated method stub
					throws IllegalArgumentException, SecurityException,
					IllegalStateException {
				count++;
				Media_play = true;
				mclickedMediaPlayer = true;
				if (mClickedStop == true) {

					// MediaPlayer m = new MediaPlayer();
					//
					// try {
					// m.setDataSource(outputFile);
					// }
					//
					// catch (IOException e) {
					// e.printStackTrace();
					// }
					//
					// try {
					// m.prepare();
					// }
					//
					// catch (IOException e) {
					// e.printStackTrace();
					// }
					//
					// m.start();
					playButton.setBackgroundResource(R.drawable.record);
					pauseButton.setBackgroundResource(R.drawable.stop);
					if (mediaPlayer.isPlaying()) {

						mediaPlayer.pause();
						recoreButton.setBackgroundResource(R.drawable.play);
						Toast.makeText(getApplicationContext(), "Paused Audio",
								Toast.LENGTH_LONG).show();

					} else {
						if (count == 1) {
							try {
								mediaPlayer.prepare();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						mediaPlayer.start();
						recoreButton
								.setBackgroundResource(R.drawable.pause_audio);
						// Toast.makeText(getApplicationContext(),
						// "Playing Audio", Toast.LENGTH_LONG).show();

						// seekUpdation();
						if (mediaPlayer != null) {
							mediaPlayer.start();
							// mseekBar.setMax(mediaPlayer.getDuration());

							seekHandler.postDelayed(new run(mediaPlayer,
									mseekBar), 100);

							timer = new Timer();
							timer.scheduleAtFixedRate(new TimerTask() {
								@Override
								public void run() {
									runOnUiThread(new Runnable() {
										@Override
										public void run() {
											if (mediaPlayer != null
													&& mediaPlayer.isPlaying()) {
												handler.post(new Runnable() {
													@Override
													public void run() {
														Constant.printMsg("hsfsdhfios  "
																+ mediaPlayer
																		.getCurrentPosition());
														String seconds = String
																.valueOf((mediaPlayer
																		.getCurrentPosition() % 60000) / 1000);
														String minutes = String
																.valueOf(mediaPlayer
																		.getCurrentPosition() / 60000);

														System.out
																.println("hsfsdhfios121213  "
																		+ seconds
																		+ "   "
																		+ minutes);
														// mseekBar.setProgress((mediaPlayer
														// .getCurrentPosition()
														// % 60000) / 1000);

														if (minutes.length() == 0) {
															minutes = "00";

															if (seconds
																	.length() == 1) {
																// minutes =
																// "00";
																mDurationText
																		.setText(minutes
																				+ ":"
																				+ "0"
																				+ seconds);
															}
															if (seconds
																	.length() == 2) {
																// minutes =
																// "00";
																mDurationText
																		.setText(minutes
																				+ ":"
																				+ seconds);
															}
														} else {
															if (seconds
																	.length() == 1) {
																// minutes =
																// "00";
																mDurationText
																		.setText("0"
																				+ minutes
																				+ ":"
																				+ "0"
																				+ seconds);
															}
															if (seconds
																	.length() == 2) {
																// minutes =
																// "00";
																mDurationText
																		.setText("0"
																				+ minutes
																				+ ":"
																				+ seconds);
															}
														}
														// .getCurrentPosition());
													}
												});
											} else {
												timer.cancel();

												timer.purge();
											}
										}
									});
								}
							}, 0, 1000);
						}
					}

					// Toast.makeText(getApplicationContext(), "Playing audio",
					// Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "No audio to play",
							Toast.LENGTH_SHORT).show();
					recoreButton.setBackgroundResource(R.drawable.play);
				}
				// recoreButton.setBackgroundResource(R.drawable.play);
			}

		});
		mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				// Log.d("Player", "Player prepared....");
				mp.start();
				mseekBar.setProgress(0);
				mseekBar.setMax(mp.getDuration());
			}
		});
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

			public void onCompletion(MediaPlayer mp) {

				recoreButton.setBackgroundResource(R.drawable.play);

			}
		});
		recoreButton.setBackgroundResource(R.drawable.play);
		playButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mClickedPlay = true;
				mClickedStop = false;
				playButton.setBackgroundResource(R.drawable.stop_yellow);
				pauseButton.setBackgroundResource(R.drawable.stop);

				try {
					myAudioRecorder.prepare();
					myAudioRecorder.start();
				}

				catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				playButton.setEnabled(false);
				// pauseButton.setEnabled(true);

				Toast.makeText(getApplicationContext(), "Recording Started",
						Toast.LENGTH_SHORT).show();

			}
		});
		pauseButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				playButton.setBackgroundResource(R.drawable.record);
				pauseButton.setBackgroundResource(R.drawable.pause_yellow);
				playButton.setVisibility(View.GONE);
				if (mClickedPlay == true) {

					mClickedPlay = false;
					mClickedStop = true;

					try {

						myAudioRecorder.stop();
						mediaPlayer.stop();
						onPause();
						// mRecordingStop.setEnabled(false);
						playButton.setEnabled(false);
					}

					catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						Constant.printMsg("Hi :::: +++++ else source");

						mediaPlayer.setDataSource(outputFile);
					}

					catch (IOException e) {
						e.printStackTrace();
					}

					// try {
					// mediaPlayer.prepare();
					// }
					//
					// catch (IOException e) {
					// e.printStackTrace();
					// }

					Toast.makeText(getApplicationContext(),
							"Audio recorded Successfully", Toast.LENGTH_SHORT)
							.show();

				} else if (Media_play == true) {
					Media_play = false;
					mediaPlayer.stop();
					mseekBar.setMax(1000);
					count = 0;
					recoreButton.setBackgroundResource(R.drawable.play);
				}
			}

		});
		shareButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mClickedStop == true) {

					com.wifin.kachingme.util.Constant.bux = sharedPrefs.getLong("buxvalue", 0);

					Long buxval = com.wifin.kachingme.util.Constant.bux + com.wifin.kachingme.util.Constant.karaokepoints;
					com.wifin.kachingme.util.Constant.bux = buxval;

					com.wifin.kachingme.util.Constant.totalkar = Count + com.wifin.kachingme.util.Constant.totalkar;

					Editor e = sharedPrefs.edit();
					e.putLong("buxvalue", buxval);
					e.commit();

					com.wifin.kachingme.util.Constant.karaoke = true;
					com.wifin.kachingme.util.Constant.file = outputFile;
					if (com.wifin.kachingme.util.Constant.mKroKFromSlider == true) {
						Intent in = new Intent(KaraokeYoutube.this,
								SliderTesting.class);
						startActivity(in);
						finish();
					} else if (com.wifin.kachingme.util.Constant.mKrokFromGroup == true) {
						com.wifin.kachingme.util.Constant.mKrokFromGroup = false;
						Intent in = new Intent(KaraokeYoutube.this,
								MUCTest.class);
						startActivity(in);
						finish();
					}

					else {
						Intent in = new Intent(KaraokeYoutube.this, ChatTest.class);
						startActivity(in);
						finish();
					}

				} else {
					Toast.makeText(getApplicationContext(),
							"No Record is found", Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	protected void seekUpdation() {
		// TODO Auto-generated method stub
		mseekBar.setProgress(mediaPlayer.getCurrentPosition());
		seekHandler.postDelayed(run, 1000);
	}

	Runnable run = new Runnable() {
		@Override
		public void run() {
			seekUpdation();
		}
	};

	public void initVariable() {
		// TODO Auto-generated method stub

		mMusicConrolLayout = (LinearLayout) findViewById(R.id.media_control_layout);

		songNameText = (TextView) findViewById(R.id.song_name);
		kachingMeText = (TextView) findViewById(R.id.kaching_text);

		playButton = (Button) findViewById(R.id.play_icon);
		pauseButton = (Button) findViewById(R.id.stop_icon);
		shareButton = (Button) findViewById(R.id.share_icon);
		recoreButton = (Button) findViewById(R.id.record_icon);
		mDurationText = (TextView) findViewById(R.id.duration_text);
		mseekBar = (SeekBar) findViewById(R.id.seekBar_krok);
		cancelBtn = (Button) findViewById(R.id.cancel_btn);
		resetBtn = (Button) findViewById(R.id.reset_btn);

        try {
            Constant.typeFace(this,songNameText);
            Constant.typeFace(this,kachingMeText);
            Constant.typeFace(this,playButton);
            Constant.typeFace(this,pauseButton);
            Constant.typeFace(this,shareButton);
            Constant.typeFace(this,recoreButton);
            Constant.typeFace(this,mDurationText);
            Constant.typeFace(this,cancelBtn);
            Constant.typeFace(this,resetBtn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public void screenArrangement() {

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		mScreenHeight = metrics.heightPixels;
		mScreenWidth = metrics.widthPixels;

		int density = (int) getResources().getDisplayMetrics().density;
		mScreenHeightInDP = metrics.heightPixels / density;
		mScreenWidthInDP = metrics.widthPixels / density;

		FrameLayout.LayoutParams mediaParams = new FrameLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		mediaParams.topMargin = mScreenHeightInDP * 18 / 100;
		mediaParams.gravity = Gravity.CENTER;
		mMusicConrolLayout.setLayoutParams(mediaParams);

		LinearLayout.LayoutParams mediaButtonParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		mediaButtonParams.width = mScreenWidth * 12 / 100;
		mediaButtonParams.height = mScreenWidth * 12 / 100;
		mediaButtonParams.leftMargin = mScreenWidth * 3 / 100;
		mediaButtonParams.rightMargin = mScreenWidth * 3 / 100;
		playButton.setLayoutParams(mediaButtonParams);
		pauseButton.setLayoutParams(mediaButtonParams);
		shareButton.setLayoutParams(mediaButtonParams);
		recoreButton.setLayoutParams(mediaButtonParams);

		LinearLayout.LayoutParams mediaButtonParams1 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		mediaButtonParams1.width = mScreenWidth * 37 / 100;
		mediaButtonParams1.height = mScreenWidth * 7 / 100;
		mediaButtonParams1.leftMargin = mScreenWidth * 3 / 100;
		mediaButtonParams1.rightMargin = mScreenWidth * 3 / 100;
		cancelBtn.setLayoutParams(mediaButtonParams1);
		resetBtn.setLayoutParams(mediaButtonParams1);
	}

	private PlaybackEventListener playbackEventListener = new PlaybackEventListener() {

		@Override
		public void onBuffering(boolean arg0) {
		}

		@Override
		public void onPaused() {

			// Toast.makeText(getApplicationContext(), "Pause",
			// Toast.LENGTH_SHORT)
			// .show();

		}

		@Override
		public void onPlaying() {

			// Toast.makeText(getApplicationContext(), "Play",
			// Toast.LENGTH_SHORT)
			// .show();
		}

		@Override
		public void onSeekTo(int arg0) {
		}

		@Override
		public void onStopped() {

		}

	};

	private PlayerStateChangeListener playerStateChangeListener = new PlayerStateChangeListener() {

		@Override
		public void onAdStarted() {
		}

		@Override
		public void onError(ErrorReason arg0) {
		}

		@Override
		public void onLoaded(String arg0) {
		}

		@Override
		public void onLoading() {
		}

		@Override
		public void onVideoEnded() {
		}

		@Override
		public void onVideoStarted() {
		}
	};

	@Override
	public void onInitializationFailure(
			com.google.android.youtube.player.YouTubePlayer.Provider provider,
			YouTubeInitializationResult arg1) {
		// TODO Auto-generated method stub
		// Toast.makeText(this, "Failured to Initialize!", Toast.LENGTH_SHORT)
		// .show();
	}

	@Override
	public void onInitializationSuccess(
			com.google.android.youtube.player.YouTubePlayer.Provider arg0,
			YouTubePlayer player, boolean wasRestored) {
		// TODO Auto-generated method stub
		player.setPlayerStateChangeListener(playerStateChangeListener);
		player.setPlaybackEventListener(playbackEventListener);

		/** Start buffering **/
		if (!wasRestored) {
			player.cueVideo(com.wifin.kachingme.util.Constant.url);
		}
	}

	class run implements Runnable {

		MediaPlayer mp;
		SeekBar seek;

		public run(MediaPlayer m, SeekBar se) {
			mp = m;
			seek = se;
		}

		@Override
		public void run() {

			if (mediaPlayer.isPlaying()) {
				seek.setProgress(mp.getCurrentPosition());

				/*
				 * Log.d("Current Duration", "Current Postion::" +
				 * mp.getCurrentPosition() + " Seek Potition::" +
				 * seek.getProgress());
				 */
			}
			seekHandler.postDelayed(this, 1);
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent in = new Intent(KaraokeYoutube.this, KaraokeTab.class);
		startActivity(in);
		finish();
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		// Toast.makeText(getApplicationContext(), "Media is not running",
		// Toast.LENGTH_SHORT).show();
		try {
			if (mediaPlayer.isPlaying() || mediaPlayer != null) {
				if (fromUser)
					mediaPlayer.seekTo(progress);
			} else if (mediaPlayer == null) {
				// Toast.makeText(getApplicationContext(),
				// "Media is not running",
				// Toast.LENGTH_SHORT).show();
				seekBar.setProgress(0);
			}
		} catch (Exception e) {
			Log.e("seek bar", "" + e);
			seekBar.setEnabled(false);

		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		// Toast.makeText(getApplicationContext(), "Media is tracking",
		// Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		// Toast.makeText(getApplicationContext(), "Media is touch",
		// Toast.LENGTH_SHORT).show();
	}

}
