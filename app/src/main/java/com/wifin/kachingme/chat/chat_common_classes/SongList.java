package com.wifin.kachingme.chat.chat_common_classes;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.chat.broadcast_chat.BroadCastTest;
import com.wifin.kachingme.chat.muc_chat.MUCTest;
import com.wifin.kachingme.chat.single_chat.ChatTest;
import com.wifin.kachingme.chat_home.MainActivity;
import com.wifin.kachingme.handler.RefreshHandler;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.SeekBarUtilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SongList extends MainActivity implements OnSeekBarChangeListener {
    public MediaPlayer mediaPlayer;
    List<String> songList = new ArrayList<String>();
    ListView lv;
    Button okay, cancel;
    List<String> showsongList = new ArrayList<String>();
    int global_position = -1;
    boolean isKaraokeAudio = true;
    int width, height;
    LinearLayout songlay, mBtnLayout, mTextLayout, mSeekLayout, mBtnPlayLayout;
    Button mSongsText, mKaraokeText;
    String strURL;
    String mClickedBtn;
    int pos = -1;
    TextView mDurationText;
    Timer timer;
    RefreshHandler handler = new RefreshHandler();
    Button start, pause, stop;
    // boolean pause_mode = false;
    SeekBar seek_bar;
    Handler seekHandler = new Handler();
    Runnable run = new Runnable() {
        @Override
        public void run() {
            seekUpdation();
        }
    };

    // SongList_Adapter mAdapter;
    private SeekBarUtilities utils;
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mediaPlayer.getDuration();
            long currentDuration = mediaPlayer.getCurrentPosition();

            // Updating progress bar
            int progress = (int) (utils.getProgressPercentage(currentDuration,
                    totalDuration));
            // Log.d("Progress", ""+progress);
            seek_bar.setProgress(progress);

            // Running this thread after 100 milliseconds
            seekHandler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//		ViewGroup vg = (ViewGroup) findViewById(R.id.Ka_datalayout);
//		View.inflate(this, R.layout.ch_activity_main, vg);

        setContentView(R.layout.ch_activity_main);
        /*logo.setVisibility(ImageView.INVISIBLE);
        back.setVisibility(ImageView.VISIBLE);
		cart.setVisibility(ImageView.INVISIBLE);
		sideMenufoot.setVisibility(LinearLayout.GONE);
		head.setTextColor(Color.parseColor("#FFFFFF"));
		Ka_newlogo.setVisibility(ImageView.INVISIBLE);
		back.setBackgroundResource(R.drawable.arrow);
		headlay.setBackgroundColor(Color.parseColor("#FE0000"));
		head.setText("Select Song");*/
        mClickedBtn = "Karaoke";
        Constant.song_list.clear();
        mediaPlayer = new MediaPlayer();
        utils = new SeekBarUtilities();

        init();

        seek_bar.setOnSeekBarChangeListener(this);

        seek_bar.setFocusable(false);
        seek_bar.setProgress(0);
        seek_bar.setMax(100);

        songList = new ArrayList<String>();
        initMusicList();
        screenArrange();



        mSongsText.setVisibility(View.GONE);
    /*	mSongsText.setBackgroundResource(R.drawable.whitecolr);
		mSongsText.setTextColor(Color.parseColor("#ffffff"));

		mKaraokeText.setBackgroundResource(R.drawable.selectorforclick);
		mKaraokeText.setTextColor(Color.parseColor("#ffffff"));*/
        // lv.setOnItemClickListener(new CheckBoxClick());
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (Constant.isModeDebug)
                    Constant.printMsg("Aaaaa back pressed");

                Constant.printMsg("cancel clicked::");
                if (mediaPlayer.isPlaying()) {
                    Constant.pause_mode = false;
                    mediaPlayer.stop();
                    onPause();
                }
                if (Constant.mFromGroupAudio == true) {
                    Constant.mFromGroupAudio = false;
                    Constant.isMUC_Paused = true;

                    Intent in = new Intent(SongList.this, MUCTest.class);
                    startActivity(in);
                } else if (Constant.mFromBrodAudio == true) {
                    Constant.mFromBrodAudio = false;

                    Intent in = new Intent(SongList.this, BroadCastTest.class);
                    startActivity(in);
                } else {
                    Intent in = new Intent(SongList.this, ChatTest.class);
                    startActivity(in);

                }
            }
        });
        mKaraokeText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isKaraokeAudio) {

                    isKaraokeAudio = false;
                    mClickedBtn = "Songs";
                    mKaraokeText.setText("Songs");
                    initMusicList();
                } else {
                    isKaraokeAudio = true;
                    mClickedBtn = "Karaoke";
                    mKaraokeText.setText("Karaoke");
                    initMusicList();
                }


            }
        });
        mSongsText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


            }
        });

        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent = new Intent("com.android.music.musicservicecommand");
                intent.putExtra("command", "pause");
                sendBroadcast(intent);

                Constant.printMsg("start   " + Constant.songName);
                if (Constant.pause_mode) {
                    Constant.pause_mode = false;
                    mediaPlayer.start();
                } else {
                    if (Constant.songName != null) {

                        mediaPlayer = new MediaPlayer();
                        try {
                            Constant.printMsg("Hi :::: +++++ else source"
                                    + Constant.songName);

                            mediaPlayer.setDataSource(Constant.songName);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        try {
                            Constant.printMsg("Hi :::: +++++ else source"
                                    + Constant.songName);

                            mediaPlayer.prepare();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mediaPlayer.start();
                    }
                }
                if (mediaPlayer != null) {

                    mediaPlayer.start();
                    // mseekBar.setMax(mediaPlayer.getDuration());

                    updateProgressBar();

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
                                                String seconds = String.valueOf((mediaPlayer
                                                        .getCurrentPosition() % 60000) / 1000);
                                                String minutes = String.valueOf(mediaPlayer
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

                                                    if (seconds.length() == 1) {
                                                        // minutes =
                                                        // "00";
                                                        mDurationText
                                                                .setText(minutes
                                                                        + ":"
                                                                        + "0"
                                                                        + seconds);
                                                    }
                                                    if (seconds.length() == 2) {
                                                        // minutes =
                                                        // "00";
                                                        mDurationText
                                                                .setText(minutes
                                                                        + ":"
                                                                        + seconds);
                                                    }
                                                } else {
                                                    if (seconds.length() == 1) {
                                                        // minutes =
                                                        // "00";
                                                        mDurationText
                                                                .setText("0"
                                                                        + minutes
                                                                        + ":"
                                                                        + "0"
                                                                        + seconds);
                                                    }
                                                    if (seconds.length() == 2) {
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
        });
        pause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Constant.pause_mode = true;
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    Constant.printMsg("Hi :::: +++++ if if");

                }
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Constant.pause_mode = false;
                mediaPlayer.stop();
                updateProgressBar();
                // onPause();
            }
        });

        mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                // TODO Auto-generated method stub
                // Log.d("Player", "Player prepared....");
                mp.start();
                seek_bar.setProgress(0);
                seek_bar.setMax(mp.getDuration());
            }
        });
        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

            public void onCompletion(MediaPlayer mp) {

                // recoreButton.setBackgroundResource(R.drawable.play);

            }
        });

    }

    private void screenArrange() {// TODO Auto-generated method stub
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        Constant.screenWidth = width;
        Constant.screenHeight = height;

        LinearLayout.LayoutParams vilay = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        vilay.height = height * 53 / 100;
        vilay.width = width;
        vilay.gravity = Gravity.CENTER;
        lv.setLayoutParams(vilay);
        // vilay.setMargins(width * 3 / 100, height * 7 / 100, width * 3 / 100,
        // height * 5 / 100);

        LinearLayout.LayoutParams layoutdatat = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutdatat.height = (int) (height * 6 / 100);
        layoutdatat.width = width * 35 / 100;
        layoutdatat.gravity = Gravity.CENTER;
        layoutdatat.leftMargin = width * 2 / 100;
        // datalay.setLayoutParams(layoutdatat);
        okay.setLayoutParams(layoutdatat);
        cancel.setLayoutParams(layoutdatat);

        LinearLayout.LayoutParams layout_seek = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
//        layout_seek.width = width * 90/100;
        layout_seek.gravity = Gravity.CENTER;
//        layout_seek.leftMargin = width * 2 / 100;
//        layout_seek.rightMargin = width * 2 / 100;
        seek_bar.setLayoutParams(layout_seek);

        LinearLayout.LayoutParams layoutdatat2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutdatat2.height = (int) (height * 17 / 100);
        mBtnLayout.setLayoutParams(layoutdatat2);


        LinearLayout.LayoutParams layoutdatat1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutdatat1.height = (int) (height * 81 / 100);
        layoutdatat1.gravity = Gravity.CENTER;
        layoutdatat1.topMargin = width * 2 / 100;
        datalay.setLayoutParams(layoutdatat1);

        LinearLayout.LayoutParams layoutdatat11 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutdatat11.height = (int) (height * 12 / 100);
        layoutdatat11.width = width;
        // datalay.setLayoutParams(layoutdatat);
        mTextLayout.setLayoutParams(layoutdatat11);

        LinearLayout.LayoutParams layout_seek1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
//        layout_seek1.height = (int) (height * 9/ 100);
        layout_seek1.width = (width * 90 / 100);
        layout_seek1.gravity = Gravity.CENTER;
        // datalay.setLayoutParams(layoutdatat);
        mSeekLayout.setLayoutParams(layout_seek1);

        LinearLayout.LayoutParams layoutdatat12 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutdatat12.height = (int) (height * 6 / 100);
//		layoutdatat12.width = width;
        layoutdatat12.gravity = Gravity.CENTER;
        layoutdatat12.leftMargin = width * 8 / 100;
        layoutdatat12.rightMargin = width * 8 / 100;
        layoutdatat12.topMargin = width * 1
                / 100;
        layoutdatat12.bottomMargin = width * 1 / 100;
        // datalay.setLayoutParams(layoutdatat);
        mSongsText.setLayoutParams(layoutdatat12);
        mKaraokeText.setLayoutParams(layoutdatat12);

        LinearLayout.LayoutParams durationParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        durationParams.height = (int) (height * 6 / 100);
        durationParams.width = width * 10 / 100;
        durationParams.gravity = Gravity.CENTER;
        durationParams.leftMargin = width * 3 / 100;
        mDurationText.setLayoutParams(durationParams);
        start.setLayoutParams(durationParams);
        pause.setLayoutParams(durationParams);
        stop.setLayoutParams(durationParams);

    }

    private void init() {
        // TODO Auto-generated method stub
        lv = (ListView) findViewById(R.id.list_check);
        okay = (Button) findViewById(R.id.song_ok);
        cancel = (Button) findViewById(R.id.song_cancel);
        mBtnLayout = (LinearLayout) findViewById(R.id.btn_layout);
        mBtnPlayLayout = (LinearLayout) findViewById(R.id.play_layout);
        mSeekLayout = (LinearLayout) findViewById(R.id.seek_layout);
        mTextLayout = (LinearLayout) findViewById(R.id.text_layout);
        mSongsText = (Button) findViewById(R.id.all_songs_text);
        mKaraokeText = (Button) findViewById(R.id.karaoke_songs_text);
        mDurationText = (TextView) findViewById(R.id.duration_text);
        start = (Button) findViewById(R.id.start_btn);
        pause = (Button) findViewById(R.id.pause_btn);
        stop = (Button) findViewById(R.id.stop_btn);
        seek_bar = (SeekBar) findViewById(R.id.seek);

    }

    private void initMusicList() {
        String selection = MediaStore.Audio.Media.IS_MUSIC + " !=0";
        String[] projection = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST_ID, MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION};

        Cursor cursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
                selection, null, null);
        songList = new ArrayList<String>();
        showsongList = new ArrayList<String>();
        showsongList.clear();
        songList.clear();

        Constant.printMsg("SSSSSS cursor " + cursor.getCount());

        while (cursor.moveToNext()) {
            songList.add(
                    // cursor.getString(0) + "||" + cursor.getString(1)
                    // + "||" + cursor.getString(2) + "||" + cursor.getString(3)+"||"+
                    cursor.getString(4));
            // + "||" + cursor.getString(5));
            // + "||" + cursor.getString(6));

            // showsongList.add("c1::" + cursor.getString(0) + "c2"
            // + cursor.getString(1) + "c3" + cursor.getString(2) + "c4"
            // + cursor.getString(3) + "c5" + cursor.getString(4) + "c6"
            // + cursor.getString(5));
            showsongList.add(cursor.getString(3));

        }

        Constant.printMsg("SSSSSS cursor " + songList.size() + showsongList.size());

     /*   String[] files = new File(Constant.local_audio_dir).list();
        ArrayAdapter<String> a = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, files);
        lv.setAdapter(a);*/

        Constant.printMsg("SSSSSS cursor " + new File(Constant.local_audio_dir).list().length);


        if (mClickedBtn.equalsIgnoreCase("Karaoke")) {
			/*mSongsText.setBackgroundResource(R.drawable.selectorforclick);
			mSongsText.setTextColor(Color.parseColor("#ffffff"));

			mKaraokeText.setBackgroundResource(R.drawable.whitecolr);
			mKaraokeText.setTextColor(Color.parseColor("#ffffff"));*/
            String[] files1 = new File(Constant.local_audio_dir).list();
            ArrayAdapter<String> a1 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_single_choice, files1);
            lv.setAdapter(a1);
            // songList.clear();
            // songList = new ArrayList<String>(Arrays.asList(files));
            // mAdapter = new SongList_Adapter(SongList.this, songList);
            //
            // lv.setAdapter(mAdapter);
        }
        if (mClickedBtn.equalsIgnoreCase("Songs")) {

		/*	mSongsText.setBackgroundResource(R.drawable.whitecolr);
			mSongsText.setTextColor(Color.parseColor("#ffffff"));

			mKaraokeText.setBackgroundResource(R.drawable.selectorforclick);
			mKaraokeText.setTextColor(Color.parseColor("#ffffff"));*/
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_single_choice, songList);
            lv.setAdapter(arrayAdapter);

            // mAdapter = new SongList_Adapter(SongList.this, songList);
            //
            // lv.setAdapter(mAdapter);
        }

        // final String MEDIA_PATH1 = new String(
        // Environment.getExternalStorageDirectory()
        // + Constants.local_audio_dir);
        // Constant.printMsg("songlist size is::" + songList.size()
        // + "showsongList" + showsongList.size() + "   " + files
        // + "     " + MEDIA_PATH1);

        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // Toast.makeText(
                // getBaseContext(),
                // songList.get(position).toString() + " value is::"
                // + position, Toast.LENGTH_SHORT).show();
                try {

                    Constant.pause_mode = false;
                    // mediaPlayer.stop();
                    // onPause();
                    Constant.printMsg("showsongList value is::"
                            + showsongList.get(position).toString() + "---" + position);
                    global_position = position;
                    Constant.songName = showsongList.get(position).toString();
                    Constant.printMsg("onitem click   " + Constant.songName);
                    if (Constant.song_list.size() > 0) {
                        if (Constant.song_list.contains(Constant.songName)) {
                            for (int i = 0; i < Constant.song_list.size(); i++) {

                                if (Constant.song_list.get(i).equalsIgnoreCase(
                                        Constant.songName)) {
                                    System.out
                                            .println("called inside the song condition");
                                    Constant.song_list.remove(i);

                                    if (Constant.song_list.size() > 0)
                                        Constant.songName = Constant.song_list.get(
                                                Constant.song_list.size() - 1)
                                                .toString();
                                }
                            }
                        } else {
                            Constant.song_list.add(Constant.songName);

                        }

                    } else {
                        Constant.song_list.add(Constant.songName);

                    }

                    Constant.printMsg("Hi :::: +++++ " + Constant.song_list.size() + "  " + position);

                    if(Constant.song_list.size()<2)
                    {
                        mBtnPlayLayout.setVisibility(View.VISIBLE);
                        mSeekLayout.setVisibility(View.VISIBLE);
                    }else
                    {
                        mBtnPlayLayout.setVisibility(View.GONE);
                        mSeekLayout.setVisibility(View.GONE);
                    }

                    if (mediaPlayer.isPlaying()) {

                        mediaPlayer.stop();
                        onPause();
                    }
                } catch (Exception e) {

                }

            }

        });
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    Constant.printMsg("Hi :::: +++++ if if");

                }

                if (global_position >= 0) {

                    Constant.printMsg("okay clicked::" + global_position
                            + " value is::"
                            + songList.get(global_position).toString()
                            + "showsong::::"
                            + showsongList.get(global_position).toString());
                    Constant.songPath = "";
                    strURL = showsongList.get(global_position).toString();
                    Constant.printMsg("okay clicked::strURL" + strURL);

                    if (Constant.mFromGroupAudio == true) {
                        Constant.mFromGroupAudio = false;
                        Constant.isMUC_Paused = true;

                        Intent in = new Intent(SongList.this, MUCTest.class);
                        startActivity(in);
                        finish();
                    } else if (Constant.mFromBrodAudio == true) {
                        Constant.mFromBrodAudio = false;

                        Intent in = new Intent(SongList.this, BroadCastTest.class);
                        startActivity(in);
                        finish();
                    } else {
                        Intent in = new Intent(SongList.this, ChatTest.class);
                        startActivity(in);
                        finish();
                    }

                    Constant.songlist = true;
                    Constant.songPath = strURL;
                } else {
                    Toast.makeText(getApplicationContext(), "Please select a song", Toast.LENGTH_LONG).show();
                }
                // Chat ch = new Chat();
                // ch.SEND_FILE(strURL);

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Constant.printMsg("cancel clicked::");
                if (mediaPlayer.isPlaying()) {
                    Constant.pause_mode = false;
                    mediaPlayer.stop();
                    onPause();
                }
                if (Constant.mFromGroupAudio == true) {
                    Constant.mFromGroupAudio = false;
                    Constant.isMUC_Paused = true;

                    Intent in = new Intent(SongList.this, MUCTest.class);
                    startActivity(in);
                } else if (Constant.mFromBrodAudio == true) {
                    Constant.mFromBrodAudio = false;

                    Intent in = new Intent(SongList.this, BroadCastTest.class);
                    startActivity(in);
                } else {
                    Intent in = new Intent(SongList.this, ChatTest.class);
                    startActivity(in);

                }

                finish();
            }
        });

    }

    private void playMedia() {
        // TODO Auto-generated method stub
        try {
            mediaPlayer.setDataSource(Constant.songName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.start();
    }

    @Override
    public void onBackPressed() {

        Constant.printMsg("cancel clicked::");
        if (mediaPlayer.isPlaying()) {
            Constant.pause_mode = false;
            mediaPlayer.stop();
            onPause();
        }
        if (Constant.mFromGroupAudio == true) {
            Constant.mFromGroupAudio = false;
            Constant.isMUC_Paused = true;

            Intent in = new Intent(SongList.this, MUCTest.class);
            startActivity(in);
        } else {
            Intent in = new Intent(SongList.this, ChatTest.class);
            startActivity(in);

        }

    }

    public void updateProgressBar() {
        seekHandler.postDelayed(mUpdateTimeTask, 100);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromTouch) {

    }

    /**
     * When user starts moving the progress handler
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar
        seekHandler.removeCallbacks(mUpdateTimeTask);

    }

    // @Override
    // public void onProgressChanged(SeekBar seekBar, int progress,
    // boolean fromUser) {
    // // TODO Auto-generated method stub
    // try {
    // if (mediaPlayer.isPlaying() || mediaPlayer != null) {
    // if (fromUser)
    // mediaPlayer.seekTo(progress);
    // } else if (mediaPlayer == null) {
    // // Toast.makeText(getApplicationContext(),
    // // "Media is not running",
    // // Toast.LENGTH_SHORT).show();
    // seekBar.setProgress(0);
    // }
    // } catch (Exception e) {
    // Log.e("seek bar", "" + e);
    // seekBar.setEnabled(false);
    //
    // }
    // }
    //
    // @Override
    // public void onStartTrackingTouch(SeekBar seekBar) {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // @Override
    // public void onStopTrackingTouch(SeekBar seekBar) {
    // // TODO Auto-generated method stub
    //
    // }

    /**
     * When user stops moving the progress hanlder
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        Constant.pause_mode = true;
        seekHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mediaPlayer.getDuration();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(),
                totalDuration);

        // forward or backward to certain seconds
        mediaPlayer.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }

    protected void seekUpdation() {
        // TODO Auto-generated method stub
        seek_bar.setProgress(mediaPlayer.getCurrentPosition());
        seekHandler.postDelayed(run, 1000);
    }

    //
    // private void initMusicList() {
    // String extStore = System.getenv("EXTERNAL_STORAGE");
    // File f_prim = new File(extStore);
    // Constant.printMsg("ext:::f_prim" + extStore);
    // // To get the external SD card you can use
    //
    // String secStore = System.getenv("SECONDARY_STORAGE");
    // File f_secs = new File(secStore);
    //
    // Constant.printMsg("sdcard secStore:::" + secStore);
    //
    // // String fileN ="/storage/emulated/1/";
    // File f = new File(String.valueOf(f_secs));
    // // File f = new File(fileN);
    //
    // File[] files = f.listFiles();
    //
    // for (int i = 0; i < files.length; i++) {
    // File file = files[i];
    // /* It's assumed that all file in the path are in supported type */
    // String filePath = file.getPath();
    // if (filePath.endsWith(".mp3")) // Condition to check .txt file
    // // extension
    // {
    // Constant.printMsg("The file paths are ::::" + filePath);
    // displayPath(filePath);
    // songList.add(filePath);
    // Constant.printMsg("song list size::" + songList.size());
    // }
    // }
    // }
    //
    // public void displayPath(String mFileName) {
    // File dir = Environment.getExternalStorageDirectory();
    // // File yourFile = new File(dir,
    // // "path/to/the/file/inside/the/sdcard.ext");
    //
    // // Get the text file
    // File file = new File(mFileName);
    // // i have kept text.mp3 in the sd-card
    //
    // if (file.exists()) // check if file exist
    // {
    // // Read text from file
    // StringBuilder text = new StringBuilder();
    //
    // try {
    // BufferedReader br = new BufferedReader(new FileReader(file));
    // String line;
    //
    // while ((line = br.readLine()) != null) {
    // text.append(line);
    // text.append('n');
    // }
    // } catch (IOException e) {
    // // You'll need to add proper error handling here
    // }
    // // Set the text
    // Constant.printMsg("The file name is ::::" + mFileName + "/n "
    // + text);
    //
    // // String toAdds = "vino.tkp@gmail.com";
    // // String fromAdds = "vino.tkp@gmail.com";
    // // String sub = "Capture the flag 2016";
    // // String msg = text.toString();
    // // try {
    // // sendEmail(toAdds, fromAdds, sub, msg, null);
    // // } catch (Exception e) {
    // // e.printStackTrace();
    // // }
    // } else {
    // Constant.printMsg("Sorry file doesn't exist!!");
    // }
    // }
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

}
