package com.wifin.kachingme.kaching_feature.karaoke;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.chat.muc_chat.MUCTest;
import com.wifin.kachingme.chat.single_chat.ChatTest;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.util.Constant;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class KaraokeYoutubeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, SeekBar.OnSeekBarChangeListener
{
    public static final String API_KEY = "AIzaSyBtP8tvaY1H8nAi1jhSbo5_7Cd7DUoIG9M";
    public static String VIDEO_ID = Constant.url;
    public static KaraokeYoutubeActivity mActivity;
    static int btn_clk = 0;
    public RefreshHandler handler = new RefreshHandler();
    ImageView pauseButton, shareButton, recoreButton, cancelBtn, resetBtn;
    int mScreenHeight = 0, mScreenWidth = 0;
    int mScreenHeightInDP = 0, mScreenWidthInDP = 0;
    MediaPlayer mediaPlayer;
    SharedPreferences sharedPrefs;
    int Count = 1;
    boolean mClickedPlay = false;
    boolean mClickedStop = false;
    TextView mDurationText;
    Timer timer;
    SeekBar mseekBar;
    Handler seekHandler = new Handler();
    boolean mclickedMediaPlayer = false, mKaraokeNoAudio = false, mVideoStartState = false;
    int count = 0;
    boolean Media_play = false;

    Bundle tabSelect;
    int positionTab = 0;

    Runnable run = new Runnable()
    {
        @Override
        public void run()
        {
            seekUpdation();
        }
    };

    private MediaRecorder myAudioRecorder;
    private String outputFile = null;
    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener()
    {
        @Override
        public void onAdStarted()
        {
            Constant.printMsg("Video State :: " + " Ad Started ");

            mVideoStartState = false;
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason arg0)
        {
            Constant.printMsg("Video State :: " + " On Error ");

            mVideoStartState = false;
        }

        @Override
        public void onLoaded(String arg0)
        {
            Constant.printMsg("Video State :: " + "On Loaded ");
        }

        @Override
        public void onLoading()
        {
            Constant.printMsg("Video State :: " + " On Loading ");
        }

        @Override
        public void onVideoEnded()
        {
            Constant.printMsg("Video State :: " + " On Video Ended ");
        }

        @Override
        public void onVideoStarted()
        {
            Constant.printMsg("Video State :: " + " Video Started ");

            mVideoStartState = true;
        }
    };

    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener()
    {
        @Override
        public void onBuffering(boolean arg0)
        {
            Constant.printMsg("Video State :: " + " Buffering Listener ");
        }

        @Override
        public void onPaused()
        {
            Constant.printMsg("Video State :: " + " Paused Listener ");
        }

        @Override
        public void onPlaying()
        {
            Constant.printMsg("Video State :: " + " Playing Listener ");
        }

        @Override
        public void onSeekTo(int arg0)
        {
            Constant.printMsg("Video State :: " + " Seek To Listener ");
        }

        @Override
        public void onStopped()
        {
            Constant.printMsg("Video State :: " + " Stopped Listener ");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.karaoke_youtube_activity);
        mActivity = this;

        if (Build.VERSION.SDK_INT >= 21)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.red_status_bar));
        }

        btn_clk = 0;
        VIDEO_ID = Constant.url;
        Constant.printMsg("Selected Song :: " + " " + VIDEO_ID + " " + Constant.url + " " + Constant.mSong);

        /** Initializing YouTube player view **/

        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
        youTubePlayerView.initialize(API_KEY, this);

        initVariable();
        screenArrangement();

        tabSelect = getIntent().getExtras();
        if (tabSelect != null)
        positionTab = tabSelect.getInt("tabselected");

        mediaPlayer = new MediaPlayer();
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean success = (new File(Constant.local_audio_dir)).mkdirs();

        if (!success)
        {
            // Log.w("directory not created", "directory not created");
        }

        outputFile = Constant.local_audio_dir + System.currentTimeMillis()+ ".amr";
        mseekBar.setFocusable(false);
        mseekBar.setProgress(0);
        mseekBar.setMax(100);

        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        myAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        myAudioRecorder.setOutputFile(outputFile);

        buttonClickAction();
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult)
    {

    }

    protected void seekUpdation()
    {
        mseekBar.setProgress(mediaPlayer.getCurrentPosition());
        seekHandler.postDelayed(run, 1000);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
    {
        // TODO Auto-generated method stub
        try
        {
            if (mediaPlayer.isPlaying() || mediaPlayer != null)
            {
                if (fromUser)
                    mediaPlayer.seekTo(progress);
            }
            else if (mediaPlayer == null)
            {
                seekBar.setProgress(0);
            }
        }
        catch (Exception e)
        {
            seekBar.setEnabled(false);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {

    }

    public void initVariable()
    {
        pauseButton = (ImageView) findViewById(R.id.stop_icon);
        shareButton = (ImageView) findViewById(R.id.share_icon);
        recoreButton = (ImageView) findViewById(R.id.record_icon);
        mseekBar = (SeekBar) findViewById(R.id.seekBar_krok);
        cancelBtn = (ImageView) findViewById(R.id.cancel_btn);
        resetBtn = (ImageView) findViewById(R.id.reset_btn);
        mDurationText = (TextView) findViewById(R.id.duration_text);
    }

    public void screenArrangement()
    {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenHeight = metrics.heightPixels;
        mScreenWidth = metrics.widthPixels;

        int density = (int) getResources().getDisplayMetrics().density;
        mScreenHeightInDP = metrics.heightPixels / density;
        mScreenWidthInDP = metrics.widthPixels / density;

        LinearLayout.LayoutParams mediaButtonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mediaButtonParams.width = mScreenWidth * 20 / 100;
        mediaButtonParams.height = mScreenWidth * 20 / 100;
        mediaButtonParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER;
        mediaButtonParams.rightMargin = mScreenWidth * 3 / 100;
        mediaButtonParams.topMargin = mScreenHeight * 10 / 100;
        mediaButtonParams.bottomMargin = mScreenHeight * 10 / 100;
        recoreButton.setLayoutParams(mediaButtonParams);
        recoreButton.setBackgroundResource(R.drawable.selector_for_record);

        LinearLayout.LayoutParams mediaButtonParams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mediaButtonParams1.width = mScreenWidth * 12 / 100;
        mediaButtonParams1.height = mScreenWidth * 12 / 100;
        mediaButtonParams1.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER;
        mediaButtonParams1.rightMargin = mScreenWidth * 3 / 100;
        mediaButtonParams1.topMargin = mScreenHeight * 10 / 100;
        mediaButtonParams1.bottomMargin = mScreenHeight * 10 / 100;
        pauseButton.setLayoutParams(mediaButtonParams1);
        shareButton.setLayoutParams(mediaButtonParams1);
        pauseButton.setBackgroundResource(R.drawable.icon_pause);
        shareButton.setBackgroundResource(R.drawable.icon_share);

        LinearLayout.LayoutParams mediaButtonParams2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mediaButtonParams2.width = mScreenWidth * 8 / 100;
        mediaButtonParams2.height = mScreenWidth * 8 / 100;
        mediaButtonParams2.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER;
        mediaButtonParams2.rightMargin = mScreenWidth * 3 / 100;
        mediaButtonParams2.topMargin = mScreenHeight * 7 / 100;
        mediaButtonParams2.bottomMargin = mScreenHeight * 7 / 100;
        cancelBtn.setLayoutParams(mediaButtonParams2);
        resetBtn.setLayoutParams(mediaButtonParams2);

        cancelBtn.setBackgroundResource(R.drawable.icon_cancel);
        resetBtn.setBackgroundResource(R.drawable.icon_refresh);
    }

    public void buttonClickAction()
    {
        cancelBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                stopMedia_Player();
                Intent i = new Intent(KaraokeYoutubeActivity.this, KaraokeListActivity.class).putExtra("tabposition", positionTab);
                startActivity(i);
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                stopMedia_Player();
                Intent i = new Intent(KaraokeYoutubeActivity.this, KaraokeYoutubeActivity.class);
                startActivity(i);
                finish();
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
        {
            @Override
            public void onPrepared(MediaPlayer mp)
            {
                // TODO Auto-generated method stub
                mp.start();
                mseekBar.setProgress(0);
                mseekBar.setMax(mp.getDuration());
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            public void onCompletion(MediaPlayer mp)
            {
                recoreButton.setBackgroundResource(R.drawable.play);
            }
        });

        recoreButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)

            throws IllegalArgumentException, SecurityException, IllegalStateException
            {
                Constant.printMsg("KKKKK btn click " + btn_clk);

                if (btn_clk == 0)
                {
                    btn_clk = 1;

                    startRecording();
                }
                else if (btn_clk == 1)
                {
                    playButtonClicked();
                }
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub

                if (!mKaraokeNoAudio)
                {
                    Toast.makeText(getApplicationContext(), "Recording Not Started", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    recoreButton.setBackgroundResource(R.drawable.play);
                    pauseButton.setBackgroundResource(R.drawable.stop2);

                    btn_clk = 1;

                    if (mClickedPlay == true)
                    {
                        mClickedPlay = false;
                        mClickedStop = true;

                        try
                        {
                            myAudioRecorder.stop();
                            mediaPlayer.stop();
                            onPause();
                            recoreButton.setEnabled(true);
                        }
                        catch (IllegalStateException e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        try
                        {
                            Constant.printMsg("Hi :::: +++++ else source");

                            mediaPlayer.setDataSource(outputFile);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }

                        Toast.makeText(getApplicationContext(), "Audio recorded Successfully", Toast.LENGTH_SHORT).show();
                    }
                    else if (Media_play)
                    {
                        Media_play = false;
                        mediaPlayer.stop();
                        mseekBar.setMax(1000);
                        count = 0;
                        recoreButton.setBackgroundResource(R.drawable.play);
                        recoreButton.setEnabled(true);
                    }
                }
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!mKaraokeNoAudio)
                {
                    Toast.makeText(getApplicationContext(), "Record Audio To Share", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (mediaPlayer != null)
                        mediaPlayer.pause();

                    if (mClickedStop)
                    {
                        Constant.bux = sharedPrefs.getLong("buxvalue", 0);

                        Long buxval = Constant.bux + Constant.karaokepoints;
                        Constant.bux = buxval;

                        Constant.totalkar = Count + Constant.totalkar;

                        SharedPreferences.Editor e = sharedPrefs.edit();
                        e.putLong("buxvalue", buxval);
                        e.apply();

                        Constant.karaoke = true;
                        Constant.file = outputFile;

                        if (Constant.mKroKFromSlider)
                        {
                            Intent in = new Intent(KaraokeYoutubeActivity.this, SliderTesting.class);
                            startActivity(in);
                            finish();
                        }
                        else if (Constant.mKrokFromGroup)
                        {
                            Constant.mKrokFromGroup = false;

                            if(MUCTest.mMUC_TestActivity!=null)
                                MUCTest.mMUC_TestActivity.finish();


                            Intent in = new Intent(KaraokeYoutubeActivity.this, MUCTest.class);
                            startActivity(in);
                            finish();
                        }
                        else
                        {
                            if(ChatTest.chatTestActivity!=null)
                                ChatTest.chatTestActivity.finish();

                            Intent in = new Intent(KaraokeYoutubeActivity.this, ChatTest.class);
                            startActivity(in);
                            finish();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "No Record is found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void startRecording()
    {
        // TODO Auto-generated method stub

        mClickedPlay = true;
        mClickedStop = false;

        recoreButton.setBackgroundResource(R.drawable.record2);
        pauseButton.setBackgroundResource(R.drawable.stop2);

        mKaraokeNoAudio = true;

        try
        {
            myAudioRecorder.prepare();
            myAudioRecorder.start();
        }
        catch (IllegalStateException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        recoreButton.setEnabled(false);

        Toast.makeText(getApplicationContext(), "Recording Started", Toast.LENGTH_SHORT).show();
    }

    public void playButtonClicked()
    {
        count++;
        Media_play = true;
        mclickedMediaPlayer = true;

        if (mClickedStop)
        {
            recoreButton.setBackgroundResource(R.drawable.record);
            pauseButton.setBackgroundResource(R.drawable.stop2);

            if (mediaPlayer.isPlaying())
            {
                mediaPlayer.pause();
                recoreButton.setBackgroundResource(R.drawable.play);
                Toast.makeText(getApplicationContext(), "Paused Audio", Toast.LENGTH_LONG).show();
            }
            else
            {
                if (count == 1)
                {
                    try
                    {
                        mediaPlayer.prepare();
                    }
                    catch (IOException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                mediaPlayer.start();
                recoreButton.setBackgroundResource(R.drawable.pause_audio);

                if (mediaPlayer != null)
                {
                    mediaPlayer.start();

                    seekHandler.postDelayed(new run(mediaPlayer, mseekBar), 100);
                    timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask()
                    {
                        @Override
                        public void run()
                        {
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    if (mediaPlayer != null && mediaPlayer.isPlaying())
                                    {
                                        handler.post(new Runnable()
                                        {
                                            @Override
                                            public void run()
                                            {
                                                Constant.printMsg("hsfsdhfios  " + mediaPlayer.getCurrentPosition());
                                                String seconds = String.valueOf((mediaPlayer.getCurrentPosition() % 60000) / 1000);
                                                String minutes = String.valueOf(mediaPlayer.getCurrentPosition() / 60000);

                                                System.out.println("hsfsdhfios121213  " + seconds + "   " + minutes);

                                                if (minutes.length() == 0)
                                                {
                                                    minutes = "00";

                                                    if (seconds.length() == 1)
                                                    {
                                                        mDurationText.setText(minutes + ":" + "0" + seconds);
                                                    }
                                                    if (seconds.length() == 2)
                                                    {
                                                        mDurationText.setText(minutes + ":" + seconds);
                                                    }
                                                }
                                                else
                                                {
                                                    if (seconds.length() == 1)
                                                    {
                                                        mDurationText.setText("0" + minutes + ":" + "0" + seconds);
                                                    }
                                                    if (seconds.length() == 2)
                                                    {
                                                        mDurationText.setText("0" + minutes + ":" + seconds);
                                                    }
                                                }
                                            }
                                        });
                                    }
                                    else
                                    {
                                        timer.cancel();

                                        timer.purge();
                                    }
                                }
                            });
                        }
                    }, 0, 1000);
                }
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No audio to play", Toast.LENGTH_SHORT).show();

            recoreButton.setBackgroundResource(R.drawable.selector_for_record);
        }
    }

    @Override
    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        stopMedia_Player();
        Intent in = new Intent(KaraokeYoutubeActivity.this, KaraokeListActivity.class).putExtra("tabposition", positionTab);
        startActivity(in);
        finish();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider arg0, YouTubePlayer player, boolean wasRestored)
    {
        // TODO Auto-generated method stub
        player.setPlayerStateChangeListener(playerStateChangeListener);
        player.setPlaybackEventListener(playbackEventListener);

        /** Start buffering **/
        if (!wasRestored)
        {
            player.cueVideo(Constant.url);
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onStop()
    {
        try
        {
            seekHandler.removeCallbacks(run);
            stopMedia_Player();
        }
        catch (Exception e)
        {

        }
        super.onStop();
    }

    public void stopMedia_Player()
    {
        try
        {
            seekHandler.removeCallbacks(run);

            if (mediaPlayer != null)
            {
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                    mediaPlayer = null;
                }
            }
        }
        catch (Exception e)
        {

        }
    }

    class run implements Runnable
    {
        MediaPlayer mp;
        SeekBar seek;

        public run(MediaPlayer m, SeekBar se)
        {
            mp = m;
            seek = se;
        }

        @Override
        public void run()
        {
            if (mediaPlayer != null)
            {
                if (mediaPlayer.isPlaying())
                {
                    seek.setProgress(mp.getCurrentPosition());
                }
            }
            seekHandler.postDelayed(this, 1);
        }
    }
}
