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
import android.widget.MediaController;
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

@SuppressWarnings("deprecation")
public class SongList extends MainActivity implements MediaPlayer.OnCompletionListener
{
    public MediaPlayer mediaPlayer;
    ListView lv;
    Button okay, cancel, mSongsText, mKaraokeText, start, pause, stop;
    LinearLayout mBtnLayout, mTextLayout, mSeekLayout, mBtnPlayLayout;
    SeekBar seek_bar;
    TextView mDurationText;

    boolean isClickableButton = false, isSongPlaying = false;
    List<String> songList;
    List<String> showsongList;

    int global_position = -1;
    boolean isKaraokeAudio = true, isSongAudio = true;
    int width, height;

    String strURL, mClickedBtn;
    Timer timer;
    RefreshHandler handler = new RefreshHandler();
    Handler seekHandler = new Handler();

    Runnable run = new Runnable()
    {
        @Override
        public void run()
        {
            seekUpdation();
        }
    };

    private SeekBarUtilities utils;

    private Runnable mUpdateTimeTask = new Runnable()
    {
        public void run()
        {
            long totalDuration = mediaPlayer.getDuration();
            long currentDuration = mediaPlayer.getCurrentPosition();
            int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));

            seek_bar.setProgress(progress);
            seekHandler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch_activity_main);

        mClickedBtn = "Karaoke";
        Constant.song_list.clear();
        mediaPlayer = new MediaPlayer();
        utils = new SeekBarUtilities();

        init();

        //seek_bar.setOnSeekBarChangeListener(this);
        seek_bar.setFocusable(false);
        seek_bar.setProgress(0);
        seek_bar.setMax(100);

        songList = new ArrayList<>();
        showsongList = new ArrayList<>();

        initMusicList();
        screenArrange();

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (Constant.isModeDebug)
                    Constant.printMsg("Song List Aaaaa back pressed");

                Constant.printMsg("Song List cancel clicked::");

                if (mediaPlayer.isPlaying())
                {
                    Constant.pause_mode = false;
                    mediaPlayer.stop();
                    onPause();
                }

                if (Constant.mFromGroupAudio)
                {
                    Constant.mFromGroupAudio = false;
                    Constant.isMUC_Paused = true;

                    Intent in = new Intent(SongList.this, MUCTest.class);
                    startActivity(in);
                }
                else if (Constant.mFromBrodAudio)
                {
                    Constant.mFromBrodAudio = false;
                    Intent in = new Intent(SongList.this, BroadCastTest.class);
                    startActivity(in);
                }
                else
                {
                    Intent in = new Intent(SongList.this, ChatTest.class);
                    startActivity(in);
                }
            }
        });

        mKaraokeText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mKaraokeText.setSelected(true);
                mSongsText.setSelected(false);
                mKaraokeText.setTextColor(getResources().getColor(R.color.white));
                mSongsText.setTextColor(getResources().getColor(R.color.accent_500));

                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                    seek_bar.setProgress(0);
                }

                if (isKaraokeAudio)
                {
                    isKaraokeAudio = false;
                    isSongAudio = true;
                    mClickedBtn = "Karaoke";
                    initMusicList();
                }
            }
        });

        mSongsText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mSongsText.setSelected(true);
                mKaraokeText.setSelected(false);
                mSongsText.setTextColor(getResources().getColor(R.color.white));
                mKaraokeText.setTextColor(getResources().getColor(R.color.accent_500));

                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                    seek_bar.setProgress(0);
                }

                if (isSongAudio)
                {
                    isKaraokeAudio = true;
                    isSongAudio = false;
                    mClickedBtn = "Songs";
                    initMusicList();
                }
            }
        });

        start.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isSongPlaying)
                {
                    Constant.printMsg("Song List Playing Song..");

                    Toast.makeText(getApplicationContext(), "Song Already Playing", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (!isClickableButton)
                    {
                        Constant.printMsg("Song List Not Enabled..");

                        Toast.makeText(getApplicationContext(), "Choose One Song To Play", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Intent intent = new Intent("com.android.music.musicservicecommand");
                        intent.putExtra("command", "pause");
                        sendBroadcast(intent);
                        Constant.printMsg("Song List start   " + Constant.songName);

                        if (Constant.pause_mode)
                        {
                            Constant.pause_mode = false;
                            mediaPlayer.start();
                        }
                        else
                        {
                            if (Constant.songName != null)
                            {
                                mediaPlayer = new MediaPlayer();

                                try
                                {
                                    Constant.printMsg("Song List Hi :::: +++++ else source" + Constant.songName);
                                    mediaPlayer.setDataSource(Constant.songName);
                                }
                                catch (IOException e)
                                {
                                    e.printStackTrace();
                                }

                                try
                                {
                                    Constant.printMsg("Song List Hi :::: +++++ else source" + Constant.songName);
                                    mediaPlayer.prepare();
                                }
                                catch (IOException e)
                                {
                                    e.printStackTrace();
                                }
                                mediaPlayer.start();
                                isSongPlaying = true;
                                isClickableButton = false;
                            }
                        }
                        if (mediaPlayer != null)
                        {
                            mediaPlayer.start();
                            updateProgressBar();

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
                                                        Constant.printMsg("Song List " + mediaPlayer.getCurrentPosition());
                                                        String seconds = String.valueOf((mediaPlayer.getCurrentPosition() % 60000) / 1000);
                                                        String minutes = String.valueOf(mediaPlayer.getCurrentPosition() / 60000);
                                                        System.out.println("Song List " + seconds + "   " + minutes);

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
            }
        });

        pause.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub

                Constant.pause_mode = true;
                isClickableButton = true;
                isSongPlaying = false;

                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                    Constant.printMsg("Song List Pause Clicked ::: ");
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub

                isSongPlaying = false;
                isClickableButton = true;
                Constant.pause_mode = false;
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                    seek_bar.setProgress(0);
                }
                //updateProgressBar();
                Constant.printMsg("Song List Media Player Stop Button Clicked ::: ");
            }
        });

        mediaPlayer.setOnPreparedListener(new OnPreparedListener()
        {
            @Override
            public void onPrepared(MediaPlayer mp)
            {
                mp.start();
                seek_bar.setProgress(0);
                seek_bar.setMax(mp.getDuration());
                Constant.printMsg("Song List Media Player Prepared ::: ");
            }
        });

        mediaPlayer.setOnCompletionListener(new OnCompletionListener()
        {
            public void onCompletion(MediaPlayer mp)
            {
                Constant.printMsg("Song List Media Player Completed ::: ");
                Toast.makeText(getApplicationContext(), "Media Player Finished", Toast.LENGTH_SHORT).show();
                mp.stop();
                isSongPlaying = false;
                seek_bar.setProgress(0);
            }
        });
    }

    private void screenArrange()
    {
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

        LinearLayout.LayoutParams layoutdatat = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutdatat.height = (int) (height * 6 / 100);
        layoutdatat.width = width * 35 / 100;
        layoutdatat.gravity = Gravity.CENTER;
        layoutdatat.leftMargin = width * 2 / 100;
        okay.setLayoutParams(layoutdatat);
        cancel.setLayoutParams(layoutdatat);

        LinearLayout.LayoutParams layout_seek = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layout_seek.gravity = Gravity.CENTER;
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
        layoutdatat11.height = height;
        layoutdatat11.width = width;
        layoutdatat11.gravity = Gravity.CENTER;
        layoutdatat11.weight = 2;
        mTextLayout.setLayoutParams(layoutdatat11);

        LinearLayout.LayoutParams layout_seek1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layout_seek1.width = (width * 90 / 100);
        layout_seek1.gravity = Gravity.CENTER;
        mSeekLayout.setLayoutParams(layout_seek1);

        LinearLayout.LayoutParams layoutdatat12 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutdatat12.width = LinearLayout.LayoutParams.MATCH_PARENT;
        layoutdatat12.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        layoutdatat12.gravity = Gravity.CENTER;
        layoutdatat12.leftMargin = width * 2 / 100;
        layoutdatat12.rightMargin = width * 2 / 100;
        layoutdatat12.topMargin = width *2 / 100;
        layoutdatat12.bottomMargin = width * 2 / 100;
        layoutdatat12.weight = 1;
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

    private void init()
    {
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

        mKaraokeText.setSelected(true);
        mSongsText.setSelected(false);

        mKaraokeText.setTextColor(getResources().getColor(R.color.white));
        mSongsText.setTextColor(getResources().getColor(R.color.accent_500));
    }

    private void initMusicList()
    {
        Constant.printMsg("Song List Clicked Btn Detail ::: " + mClickedBtn);

        String selection = MediaStore.Audio.Media.IS_MUSIC + " !=0";

        String[] projection = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST_ID, MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION};

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null);

        songList = new ArrayList<>();
        showsongList = new ArrayList<>();

        showsongList.clear();
        songList.clear();

        Constant.printMsg("Song List Cursor 01 ::: " + cursor.getCount());

        /*while (cursor.moveToNext())
        {
            songList.add(cursor.getString(4));
            showsongList.add(cursor.getString(3));
            Constant.printMsg("Song List Cursor Songs 01 ::: " + cursor.getString(4) + "  " + cursor.getString(3));
        }*/

        Constant.printMsg("Song List Cursor 02 ::: " + songList.size() + "  " + showsongList.size());

        Constant.printMsg("Song List Cursor 03 ::: " + new File(Constant.local_audio_dir).list().length);

        if (mClickedBtn.equalsIgnoreCase("Karaoke"))
        {
            showsongList = new ArrayList();
            songList = new ArrayList();

            showsongList.clear();
            songList.clear();

            String[] files1 = new File(Constant.local_audio_dir).list();
            Constant.song_list.clear();

            for(String songName : files1)
            {
                showsongList.add(Constant.local_audio_dir + songName);
                songList.add(Constant.local_audio_dir + songName);
                Constant.printMsg("Song List Files ::: " + Constant.local_audio_dir + songName);
            }

            ArrayAdapter<String> a1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, files1);
            lv.setAdapter(a1);
            Constant.printMsg("Song List Total Karaoke Count ::: " + lv.getAdapter().getCount() + " " + Constant.song_list.size() + " Song List Size :: " + showsongList.size());
        }

        if (mClickedBtn.equalsIgnoreCase("Songs"))
        {
            while (cursor.moveToNext())
            {
                songList.add(cursor.getString(4));
                showsongList.add(cursor.getString(3));
                Constant.printMsg("Song List Cursor Songs 02 ::: " + cursor.getString(4) + "  " + cursor.getString(3));
            }
            Constant.song_list.clear();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, songList);
            lv.setAdapter(arrayAdapter);
            Constant.printMsg("Song List Total Songs Count ::: " + lv.getAdapter().getCount() + " " + Constant.song_list.size() + " Song List Size :: " + showsongList.size());
        }

        lv.setOnItemClickListener(new OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                try
                {
                    Constant.pause_mode = false;
                    Constant.printMsg("Song List Position 01 ::: " + showsongList.get(position).toString() + " --- " + position);
                    global_position = position;
                    Constant.songName = showsongList.get(position).toString();
                    Constant.printMsg("Song List Selected Song ::: " + Constant.songName + " " + global_position);

                    if (Constant.song_list.size() > 0)
                    {
                        if (Constant.song_list.contains(Constant.songName))
                        {
                            for (int i = 0; i < Constant.song_list.size(); i++)
                            {
                                if (Constant.song_list.get(i).equalsIgnoreCase(Constant.songName))
                                {
                                    System.out.println("Song List Inside List");

                                    Constant.song_list.remove(i);

                                    if (Constant.song_list.size() > 0)
                                    {
                                        Constant.songName = Constant.song_list.get(Constant.song_list.size() - 1).toString();

                                        Constant.printMsg("Song List Added Song Count 00 ::: " + Constant.song_list.size());
                                    }
                                }
                            }
                        }
                        else
                        {
                            Constant.song_list.add(Constant.songName);

                            Constant.printMsg("Song List Added Song Count 01 ::: " + Constant.song_list.size());
                        }
                    }
                    else
                    {
                        Constant.song_list.add(Constant.songName);

                        Constant.printMsg("Song List Added Song Count 02 ::: " + Constant.song_list.size());
                    }

                    Constant.printMsg("Song List ::: " + Constant.song_list.size() + "  " + position);

                    if(Constant.song_list.size() < 2)
                    {
                        isClickableButton = true;
                        isSongPlaying = false;
                    }
                    else
                    {
                        isClickableButton = false;
                        isSongPlaying = false;

                        if (mediaPlayer.isPlaying())
                        {
                            mediaPlayer.stop();
                            seek_bar.setProgress(0);
                        }
                    }

                    if (mediaPlayer.isPlaying())
                    {
                        isClickableButton = false;
                        isSongPlaying = false;
                        mediaPlayer.stop();
                        seek_bar.setProgress(0);
                        onPause();
                    }
                }
                catch (Exception e)
                {
                    Constant.printMsg(e.toString());
                }
            }
        });

        okay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                    seek_bar.setProgress(0);
                    Constant.printMsg("Song List Hi :::: +++++ if if");
                }

                Constant.printMsg("Song List Global Position ::: " + global_position);

                try
                {
                    if (global_position >= 0)
                    {
                        Constant.printMsg("Song List Clicked ::: " + global_position
                                + " value is :: "
                                + songList.get(global_position).toString()
                                + " showsong:::: "
                                + showsongList.get(global_position).toString());

                        Constant.songPath = "";
                        strURL = showsongList.get(global_position).toString();
                        Constant.printMsg("Song List ::: strURL " + strURL);

                        if (Constant.mFromGroupAudio)
                        {
                            Constant.mFromGroupAudio = false;
                            Constant.isMUC_Paused = true;

                            if(MUCTest.mMUC_TestActivity!=null)
                                MUCTest.mMUC_TestActivity.finish();


                            Intent in = new Intent(SongList.this, MUCTest.class);
                            startActivity(in);
                            finish();
                        }
                        else if (Constant.mFromBrodAudio)
                        {
                            Constant.mFromBrodAudio = false;
                            Intent in = new Intent(SongList.this, BroadCastTest.class);
                            startActivity(in);
                            finish();
                        }
                        else
                        {

                            if(ChatTest.chatTestActivity!=null)
                                ChatTest.chatTestActivity.finish();

                            Intent in = new Intent(SongList.this, ChatTest.class);
                            startActivity(in);
                            finish();
                        }

                        Constant.songlist = true;
                        Constant.songPath = strURL;
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Please select a song", Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e)
                {
                    Constant.printMsg("Song List Exception ::: " + e.toString());
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                Constant.printMsg("Song List cancel clicked::");

                if (mediaPlayer.isPlaying())
                {
                    Constant.pause_mode = false;
                    mediaPlayer.stop();
                    seek_bar.setProgress(0);
                    onPause();
                }

                if (Constant.mFromGroupAudio)
                {
                    Constant.mFromGroupAudio = false;
                    Constant.isMUC_Paused = true;
                    Intent in = new Intent(SongList.this, MUCTest.class);
                    startActivity(in);
                }
                else if (Constant.mFromBrodAudio)
                {
                    Constant.mFromBrodAudio = false;
                    Intent in = new Intent(SongList.this, BroadCastTest.class);
                    startActivity(in);
                }
                else
                {
                    Intent in = new Intent(SongList.this, ChatTest.class);
                    startActivity(in);
                }
                finish();
            }
        });
    }

    private void playMedia()
    {
        // TODO Auto-generated method stub
        try
        {
            mediaPlayer.setDataSource(Constant.songName);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            mediaPlayer.prepare();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }

    @Override
    public void onBackPressed()
    {
        Constant.printMsg("cancel clicked::");

        if (mediaPlayer.isPlaying())
        {
            Constant.pause_mode = false;
            mediaPlayer.stop();
            seek_bar.setProgress(0);
            onPause();
        }

        if (Constant.mFromGroupAudio)
        {
            Constant.mFromGroupAudio = false;
            Constant.isMUC_Paused = true;
            Intent in = new Intent(SongList.this, MUCTest.class);
            startActivity(in);
            finish();
        }
        else if (Constant.mFromBrodAudio)
        {
            Constant.mFromBrodAudio = false;
            Intent in = new Intent(SongList.this, BroadCastTest.class);
            startActivity(in);
            finish();
        }
        else
        {
            Intent in = new Intent(SongList.this, ChatTest.class);
            startActivity(in);
            finish();
        }
    }

    public void updateProgressBar()
    {
        seekHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /*@Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch)
    {

    }

    *//**
     * When user starts moving the progress handler
     *//*
    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {
        seekHandler.removeCallbacks(mUpdateTimeTask);
    }

    *//**
     * When user stops moving the progress hanlder
     *//*
    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {
        Constant.pause_mode = true;
        seekHandler.removeCallbacks(mUpdateTimeTask);

        int totalDuration = mediaPlayer.getDuration();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mediaPlayer.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }*/

    protected void seekUpdation()
    {
        // TODO Auto-generated method stub
        seek_bar.setProgress(mediaPlayer.getCurrentPosition());
        seekHandler.postDelayed(run, 1000);
    }

    @Override
    public void onCompletion(MediaPlayer mp)
    {
        Constant.printMsg("Song List Media Player Completed ::: ");
        Toast.makeText(getApplicationContext(), "Media Player Finished", Toast.LENGTH_SHORT).show();
        mp.stop();
        isSongPlaying = false;
        seek_bar.setProgress(0);
    }

    /*class run implements Runnable
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
            if (mediaPlayer.isPlaying())
            {
                seek.setProgress(mp.getCurrentPosition());
            }
            seekHandler.postDelayed(this, 1);
        }
    }*/
}
