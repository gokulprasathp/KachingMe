package com.wifin.kachingme.kaching_feature.karaoke;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.chat.single_chat.ChatTest;
import com.wifin.kachingme.chat_home.MainActivity;
import com.wifin.kachingme.util.Constant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SmackThatActivity extends MainActivity {

    protected static final Runnable runnable = null;
    private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
    private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
    private String file_exts[] = {AUDIO_RECORDER_FILE_EXT_MP4,
            AUDIO_RECORDER_FILE_EXT_3GP};
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    public static SmackThatActivity mActivity;
    public Context context = null;
    public TextView mKaroakeText = null;
    public RefreshHandler handler = new RefreshHandler();
    // public RefreshHandler handler1 = null;
    public long start_time, current_time, elapsed_time = 0L;
    public int duration = 10;
    public int end = 0;
    // public Button mMusic = null;
    MediaPlayer mediaPlayer;
    List<String> mDurationList = new ArrayList<String>();
    List<String> mSpeedList = new ArrayList<String>();
    List<String> mLyricsList = new ArrayList<String>();
    SharedPreferences sharedPrefs;
    double speed = 0.0;
    int value = 0;
    String mKaraokeText;
    int thread_delay = 100;
    int thred_count = 0;
    int total_listSize = 0;
    int slow = 0;
    int fast = 1;
    int medium = 2;
    int doubleFast = 3;
    int thriceFast = 4;
    int count = 0;
    // String u =
    // "http://a84.phobos.apple.com/us/r1000/145/Music6/v4/13/98/e9/1398e9ab-a2f2-a8e4-d7fb-4bca40a68c87/mzaf_6583155097318094075.plus.aac.p.m4a";
    // String ur = "http://picosong.com/bmSu";
    // String a =
    // "http://bossmp3.in/upload_file/1/13/852/Give%20Me%20Freedom,%20Give%20Me%20Fire%20-%20(BossMp3.In).mp3";
    boolean music = false;
    // for recording
    Button mRecordingPlay, mRecordingStop, mRecording, mRecordingSend,
            mKaroakePlay, mKaroakePause;
    ImageView mKaroakeImg;
    LinearLayout mKaroakeLayout, mRecordingLayout;
    boolean mPause = false;
    int Count = 1;
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;
    private int currentFormat = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        mActivity = this;

        // setContentView(R.layout.activity_karaoke);
        ViewGroup vg = (ViewGroup) findViewById(R.id.Ka_datalayout);
        View.inflate(this, R.layout.activity_karaoke, vg);
        logo.setVisibility(ImageView.GONE);
        back.setVisibility(ImageView.VISIBLE);
        cart.setVisibility(ImageView.INVISIBLE);
        initialize();
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mKaroakeImg.setBackgroundResource(R.drawable.smackthat);
        screenArrange();
        mediaPlayer = new MediaPlayer();
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();

                }

                Intent intent = new Intent(SmackThatActivity.this,
                        KaraokeMainActivity.class);
                startActivity(intent);
                finish();

            }
        });
        // for regording

        mRecordingStop.setEnabled(false);
        mRecordingPlay.setEnabled(false);
        // SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        // Constant.printMsg("sdf:::::::::" + sdf.toString());
        // String date = sdf.toString();

        // File sdcard = Environment.getExternalStorageDirectory();
        // File storagePath = new File(sdcard.getAbsolutePath() + "/karoake");
        // File outputFile= new File(storagePath + "/ recording.3gp");s
        outputFile = Environment.getExternalStorageDirectory()
                .getAbsolutePath()
                + "/smackthatrecord"
                + System.currentTimeMillis() + ".mp3";
        // if(outputFile.exists()){
        // outfile.delete();
        // }
        Constant.printMsg("dgfdh::::::::::::" + outputFile);
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);

        mRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Constant.printMsg("dgfdhrwer::::::::::::" + outputFile);

                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                    Constant.printMsg("dgfdhrfhgdhgfwer::::::::::::"
                            + outputFile);

                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                mRecording.setEnabled(false);
                mRecordingStop.setEnabled(true);

                Toast.makeText(getApplicationContext(), "Recording started",
                        Toast.LENGTH_LONG).show();
            }
        });

        mRecordingStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();

                mediaPlayer.stop();
                handler.sleep(1500);
                onPause();
                // myAudioRecorder.stop();
                // myAudioRecorder.release();
                // myAudioRecorder = null;

                mRecordingStop.setEnabled(false);
                mRecordingPlay.setEnabled(true);

                Toast.makeText(getApplicationContext(),
                        "Audio recorded successfully", Toast.LENGTH_LONG)
                        .show();
            }
        });
        mRecordingPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws IllegalArgumentException,
                    SecurityException, IllegalStateException {
                mRecordingPlay.setEnabled(false);
                MediaPlayer m = new MediaPlayer();
                if (m.isPlaying()) {
                    m.stop();
                    m.release();
                }

                try {
                    m.setDataSource(outputFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    m.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                m.start();
                Toast.makeText(getApplicationContext(), "Playing audio",
                        Toast.LENGTH_LONG).show();
            }
        });

        mRecordingSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Constant.printMsg("path:::>>>>>" + outputFile);

                Constant.bux = sharedPrefs.getLong("buxvalue", 0);

                Long buxval = Constant.bux + Constant.karaokepoints;
                Constant.bux = buxval;

                Constant.totalkar = Count + Constant.totalkar;

                Editor e = sharedPrefs.edit();
                e.putLong("buxvalue", buxval);
                e.commit();

                Constant.karaoke = true;
                Constant.file = outputFile;
                Intent in = new Intent(SmackThatActivity.this, ChatTest.class);
                startActivity(in);
                finish();
            }
        });

        // mLyricsList
        // .add("                                                                                             ");
        // mDurationList.add("6000");
        // mSpeedList.add(String.valueOf(slow));
        //
        // mLyricsList
        // .add("                                                                                             ");
        // mDurationList.add("6000");
        // mSpeedList.add(String.valueOf(slow));
        //
        // mLyricsList
        // .add("                                                                               ");
        // mDurationList.add("6000");
        // mSpeedList.add(String.valueOf(slow));

        // mLyricsList
        // .add("                                                                                             ");
        // mDurationList.add("5000");
        // mSpeedList.add(String.valueOf(slow));
        //
        // mLyricsList
        // .add("                                                                                             ");
        // mDurationList.add("6000");
        // mSpeedList.add(String.valueOf(slow));
        //
        // mLyricsList
        // .add("                                                                                             ");
        // mLyricsList
        // .add("                                                                                             ");
        // mDurationList.add("5000");
        // mSpeedList.add(String.valueOf(slow));
        //
        // mLyricsList
        // .add("                                                                                             ");
        // mDurationList.add("6000");
        // mSpeedList.add(String.valueOf(slow));
        //
        // mLyricsList
        // .add("                                                                                             ");
        // mDurationList.add("6000");
        // mSpeedList.add(String.valueOf(slow));
        //
        // mLyricsList
        // .add("                                                                                             ");
        // mDurationList.add("5000");
        // mDurationList.add("5000");
        // mSpeedList.add(String.valueOf(slow));

        mLyricsList.add(" Shady   ");
        mDurationList.add("3000");
        mSpeedList.add(String.valueOf(slow));

        mLyricsList.add("      Konvict        ");
        mDurationList.add("3000");
        mSpeedList.add(String.valueOf(slow));

        mLyricsList.add("    Upfront    ");
        mDurationList.add("3000");
        mSpeedList.add(String.valueOf(slow));

        mLyricsList.add("    Akon   ");
        mDurationList.add("3000");
        mSpeedList.add(String.valueOf(medium));

        mLyricsList.add("   Slim Shady   ");
        mDurationList.add("3000");
        mSpeedList.add(String.valueOf(medium));

        mLyricsList.add(" I  see  the  one  ");
        mDurationList.add("3000");
        mSpeedList.add(String.valueOf(medium));

        mLyricsList.add("    cause she be that lady   ");
        mDurationList.add("3000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("    hey    ");
        mDurationList.add("3000");
        mSpeedList.add(String.valueOf(slow));

        mLyricsList.add("I feel you creeping up I can see it from my shadow");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("  Wanna  jump  up  in my  Lamborghini  Gallardo   ");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList
                .add("     Maybe go to my place and just kick it like Ta Bo     ");
        mDurationList.add("3000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList
                .add("  And  possibly  bend you  over, look back  and watch me  ");
        mDurationList.add("3000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("Smack that, all on the floor");
        mDurationList.add("3000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("Smack that, give me some more");
        mDurationList.add("1500");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("Smack that, 'til you get sore");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("Smack that, oh ooh");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(medium));

        mLyricsList.add("Smack that, all on the floor");
        mDurationList.add("3000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("Smack that, give me some more");
        mDurationList.add("1500");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("Smack that, 'til you get sore");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("Smack that, oh ooh");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(medium));

        mLyricsList.add("Upfront style ready to attack now");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(thriceFast));

        mLyricsList.add("Pull in the parking lot slow with the lac down");
        mDurationList.add("1000");
        mSpeedList.add(String.valueOf(thriceFast));

        mLyricsList.add("Konvicts got the whole thing packed now");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(thriceFast));

        mLyricsList.add("Step in the club the wardobe intact now");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(thriceFast));

        mLyricsList.add("I feel it don and crack now");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(doubleFast));

        mLyricsList.add(" Ooh I see it, don't let back now   ");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(thriceFast));

        mLyricsList.add("  I'ma call her then I put the mack down  ");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(thriceFast));

        mLyricsList.add("Money? No problem, pocket full of that now");
        mDurationList.add("1000");
        mSpeedList.add(String.valueOf(thriceFast));

        mLyricsList.add("I feel you creeping up I can see it from my shadow");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("  Wanna  jump  up  in my  Lamborghini  Gallardo   ");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList
                .add("     Maybe go to my place and just kick it like Ta Bo     ");
        mDurationList.add("3000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList
                .add("  And  possibly  bend you  over, look back  and watch me  ");
        mDurationList.add("3000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("Smack that, all on the floor");
        mDurationList.add("3000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("Smack that, give me some more");
        mDurationList.add("1500");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("Smack that, 'til you get sore");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("Smack that, oh ooh");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(medium));

        mLyricsList.add("Smack that, all on the floor");
        mDurationList.add("3000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("Smack that, give me some more");
        mDurationList.add("1500");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("Smack that, 'til you get sore");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("Smack that, oh ooh");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(medium));

        mLyricsList.add("Oh, looks like another club banger");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(thriceFast));

        mLyricsList.add("They better hang on when they throw this thing on");
        mDurationList.add("3000");
        mSpeedList.add(String.valueOf(thriceFast));

        mLyricsList.add("Get a lil drink on");
        mDurationList.add("3000");
        mSpeedList.add(String.valueOf(thriceFast));

        mLyricsList.add(" They gonna flip for this Akon ");
        mDurationList.add("3000");
        mSpeedList.add(String.valueOf(thriceFast));

        mLyricsList.add("You can bank on it");
        mDurationList.add("1500");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add(" Pedicure, manicure, kitty-cat claws ");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(thriceFast));

        mLyricsList.add(" The way she climbs up and down them poles ");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(thriceFast));

        mLyricsList.add("Looking like one of them putty-cat dolls");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(thriceFast));

        mLyricsList.add(" Trying to hold my back through my drawers ");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(thriceFast));

        mLyricsList.add("Steps upstage, didn't think I saw");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(thriceFast));

        mLyricsList.add("Creeps up behind me, and shes like, You're");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("I'm like, I know, let's cut to the chase");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(thriceFast));

        mLyricsList.add("No time to waste, back to my place");
        mDurationList.add("1000");
        mSpeedList.add(String.valueOf(thriceFast));

        mLyricsList.add("No time to waste, back to my place");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(thriceFast));

        mLyricsList.add("Or more like a palace, shall I say");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add(" And plus I got pal if your gal is game ");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(thriceFast));

        mLyricsList
                .add("In fact he's the one singing the song that's playingAkon!");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(thriceFast));

        mLyricsList
                .add("                                                                                             ");
        mDurationList.add("6000");
        mSpeedList.add(String.valueOf(slow));

        // mLyricsList
        // .add("                                                                                             ");
        // mDurationList.add("6000");
        // mSpeedList.add(String.valueOf(slow));

        mLyricsList.add("                                       ");
        mDurationList.add("5000");
        mSpeedList.add(String.valueOf(slow));

        // mLyricsList
        // .add("                                                                                             ");
        // mDurationList.add("5000");
        // mSpeedList.add(String.valueOf(slow));

        mLyricsList.add("I feel you creeping up I can see it from my shadow");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("  Wanna  jump  up  in my  Lamborghini  Gallardo   ");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList
                .add("     Maybe go to my place and just kick it like Ta Bo     ");
        mDurationList.add("3000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList
                .add("  And  possibly  bend you  over, look back  and watch me  ");
        mDurationList.add("3000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("  Smack that, all on the floor  ");
        mDurationList.add("3000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("  Smack that, give me some more  ");
        mDurationList.add("1500");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("  Smack that, 'til you get sore  ");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("Smack that, oh ooh  ");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(medium));

        mLyricsList.add("  Smack that, all on the floor  ");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("  Smack that, give me some more  ");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("  Smack that, 'til you get sore  ");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("Smack that, oh ooh  ");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(medium));

        mLyricsList.add("Eminem's rollin', D an' em rollin");
        mDurationList.add("3000");
        mSpeedList.add(String.valueOf(thriceFast));

        mLyricsList.add("Boo and ol' Marvelous an' them rollin");
        mDurationList.add("3000");
        mSpeedList.add(String.valueOf(thriceFast));

        mLyricsList.add("Women just holdin'big booty rollin");
        mDurationList.add("1000");
        mSpeedList.add(String.valueOf(thriceFast));

        mLyricsList.add("Soon I be all in 'em an' throwin D      ");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(doubleFast));

        mLyricsList.add("Hittin' no less than three      ");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("Block wheel style, like whee      ");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("Girl I can tell you want me 'cause lately      ");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("I feel you creeping up I can see it from my shadow");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("  Wanna  jump  up  in my  Lamborghini  Gallardo   ");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList
                .add("     Maybe go to my place and just kick it like Ta Bo     ");
        mDurationList.add("3000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList
                .add("  And  possibly  bend you  over, look back  and watch me  ");
        mDurationList.add("3000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("Smack that, all on the floor");
        mDurationList.add("3000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("Smack that, give me some more");
        mDurationList.add("1500");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("Smack that, 'til you get sore");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("Smack that, oh ooh");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(medium));

        mLyricsList.add("Smack that, all on the floor");
        mDurationList.add("3000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("Smack that, give me some more");
        mDurationList.add("1500");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("Smack that, 'til you get sore");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(fast));

        mLyricsList.add("Smack that, oh ooh");
        mDurationList.add("2000");
        mSpeedList.add(String.valueOf(medium));

        total_listSize = mLyricsList.size();

        context = getApplicationContext();

        handler = new RefreshHandler();

        mKaroakePlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // // TODO Auto-generated method stub

                mKaroakePlay.setEnabled(false);
                mKaroakePause.setEnabled(true);
                if (mPause == true) {

                    mediaPlayer.start();

                } else {
                    handler.resume(100);
                    music = true;
                    String fileName = "smackthat.mp3";
                    String completePath = Environment
                            .getExternalStorageDirectory() + "/" + fileName;
                    Constant.printMsg("mp3:::::::completePath:::"
                            + completePath);
                    File file = new File(completePath);
                    Uri mp3 = Uri.fromFile(file);

                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    try {
                        mediaPlayer.setDataSource(SmackThatActivity.this, mp3);
                        mediaPlayer.prepare();
                        mediaPlayer.start();

                    } catch (IllegalArgumentException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (SecurityException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
                callKaroake();

            }
        });
        mKaroakePause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // handler.removeCallbacks(thread_delay);
                mKaroakePlay.setEnabled(true);
                mKaroakePause.setEnabled(false);

                mediaPlayer.pause();

                handler.sleep(1500);
                onPause();
                mPause = true;
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void callKaroake() {
        // TODO Auto-generated method stub
        start_time = Long.valueOf(System.currentTimeMillis());
        current_time = start_time;

        Constant.printMsg("Called:::::::::::::");
        handler.post(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(getApplicationContext(), "value" + value,
                        Toast.LENGTH_SHORT);
                if (thred_count >= duration) {
                    thred_count = 0;
                    start_time += duration;

                }
                if (value < total_listSize) {
                    speed = Double.valueOf(String.valueOf(mSpeedList.get(value)
                            .toString()));
                    duration = Integer.valueOf(String.valueOf(mDurationList
                            .get(value).toString()));
                    mKaraokeText = mLyricsList.get(value).toString();
                    count = mKaraokeText.length();

                    mKaroakeText.setText(mKaraokeText);
                    changeTextColor();
                    end++;
                }

                handler.postDelayed(this, thread_delay);

                thred_count += thread_delay;
            }

        });

    }

    private void screenArrange() {
        // TODO Auto-generated method stub
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        LinearLayout.LayoutParams img_params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        img_params.width = width;
        img_params.height = height * 25 / 100;
        // img_params.topMargin = height * 2 / 100;
        img_params.gravity = Gravity.CENTER;
        mKaroakeImg.setLayoutParams(img_params);

        LinearLayout.LayoutParams text_params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        text_params.width = width * 72 / 100;
        text_params.height = height * 45 / 100;
        text_params.topMargin = height * 2 / 100;
        text_params.leftMargin = width * 14 / 100;
        text_params.rightMargin = width * 14 / 100;
        text_params.gravity = Gravity.CENTER;
        mKaroakeText.setLayoutParams(text_params);

        LinearLayout.LayoutParams karoakelayout_params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        karoakelayout_params.width = width * 20 / 100;
        karoakelayout_params.height = height * 7 / 100;
        karoakelayout_params.topMargin = height * 2 / 100;
        karoakelayout_params.leftMargin = width * 29 / 100;
        karoakelayout_params.gravity = Gravity.CENTER;
        mKaroakePlay.setLayoutParams(karoakelayout_params);

        LinearLayout.LayoutParams karoakebutton_params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        karoakebutton_params.width = width * 20 / 100;
        karoakebutton_params.height = height * 7 / 100;
        karoakebutton_params.topMargin = height * 2 / 100;
        karoakebutton_params.gravity = Gravity.CENTER;
        karoakebutton_params.leftMargin = width * 5 / 100;
        mKaroakePause.setLayoutParams(karoakebutton_params);

        LinearLayout.LayoutParams recordingbutton_params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        recordingbutton_params.width = (int) (width * 18.75 / 100);
        recordingbutton_params.height = height * 7 / 100;
        recordingbutton_params.topMargin = height * 2 / 100;
        recordingbutton_params.leftMargin = (int) (width * 5.0 / 100);

        recordingbutton_params.gravity = Gravity.CENTER;
        mRecording.setLayoutParams(recordingbutton_params);

        LinearLayout.LayoutParams recordingstop_params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        recordingstop_params.width = (int) (width * 18.75 / 100);
        recordingstop_params.height = height * 7 / 100;
        recordingstop_params.topMargin = height * 2 / 100;
        recordingstop_params.leftMargin = (int) (width * 5.0 / 100);
        recordingstop_params.gravity = Gravity.CENTER;
        mRecordingStop.setLayoutParams(recordingstop_params);

        LinearLayout.LayoutParams recordingplay_params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        recordingplay_params.width = (int) (width * 18.75 / 100);
        recordingplay_params.height = height * 7 / 100;
        recordingplay_params.topMargin = height * 2 / 100;
        recordingplay_params.leftMargin = (int) (width * 5.0 / 100);
        recordingplay_params.gravity = Gravity.CENTER;
        mRecordingPlay.setLayoutParams(recordingplay_params);

        LinearLayout.LayoutParams recordingsend_params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        recordingsend_params.width = (int) (width * 18.75 / 100);
        recordingsend_params.height = height * 7 / 100;
        recordingsend_params.topMargin = height * 2 / 100;
        recordingsend_params.leftMargin = (int) (width * 5.0 / 100);
        recordingsend_params.gravity = Gravity.CENTER;
        mRecordingSend.setLayoutParams(recordingsend_params);

        if (width >= 600) {
            mKaroakeText.setTextSize(43);

        } else if (width < 600 && width >= 480) {
            mKaroakeText.setTextSize(38);

        } else if (width < 480 && width >= 320) {
            mKaroakeText.setTextSize(33);

        } else if (width < 320) {
            mKaroakeText.setTextSize(28);

        }

    }

    private void initialize() {
        // TODO Auto-generated method stub
        mKaroakeLayout = (LinearLayout) findViewById(R.id.karoake_layout);
        mRecordingLayout = (LinearLayout) findViewById(R.id.recording_layout);
        mRecordingPlay = (Button) findViewById(R.id.play_recording);
        mRecordingStop = (Button) findViewById(R.id.stop_recording);
        mRecordingSend = (Button) findViewById(R.id.send_recording);
        mRecording = (Button) findViewById(R.id.recording);
        mKaroakeImg = (ImageView) findViewById(R.id.song_img);
        mKaroakeText = (TextView) findViewById(R.id.karaoke_text);
        mKaroakePlay = (Button) findViewById(R.id.play_karoake);
        mKaroakePause = (Button) findViewById(R.id.pause_karoake);

        try {
            Constant.typeFace(this, mRecordingPlay);
            Constant.typeFace(this, mRecordingStop);
            Constant.typeFace(this, mRecordingSend);
            Constant.typeFace(this, mRecording);
            Constant.typeFace(this, mKaroakeText);
            Constant.typeFace(this, mKaroakePlay);
            Constant.typeFace(this, mKaroakePause);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void changeTextColor() {
        // TODO Auto-generated method stub
        current_time = Long.valueOf(System.currentTimeMillis());

        elapsed_time = Long.valueOf(current_time) - Long.valueOf(start_time);

        if (end >= count) {
            // Toast.makeText(context, "Done", Toast.LENGTH_LONG).show();
            end = 0;
            value++;

        } else {
            if (speed == medium) {
                end = end + 1 / 2;
            }
            if (speed == fast) {
                end = end + 1;
            }
            if (speed == slow) {
                end = end + 0;
            }
            if (speed == doubleFast) {
                end = end + 3 / 2;
            }

            if (speed == thriceFast) {
                end = end + 2;
            }
            Spannable WordtoSpan = new SpannableString(mKaroakeText.getText());
            WordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), 0, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            mKaroakeText.setText(WordtoSpan);

        }

    }

    private void stopRecording() {
        try {
            myAudioRecorder.stop();
        } catch (RuntimeException stopException) {
            // handle cleanup here
        }

    }

    private String getFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);
        if (!file.exists()) {
            file.mkdirs();
        }
        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + file_exts[currentFormat]);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();

        }
        Intent intent = new Intent(SmackThatActivity.this,
                KaraokeMainActivity.class);
        startActivity(intent);
        finish();
    }

}
