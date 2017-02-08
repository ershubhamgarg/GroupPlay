package com.example.shubhamgarg.groupplay;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener {

    static MediaPlayer mediaplayer;
    //**********************declarations***********************************************************
    TextView showname, currentTime, totalTime, panelbackground;
    ImageView albumart = null;
    Button broadcastbtn, recievebtn;
    ImageButton play, previous, next, volumebtn, listbutton, settingsbtn;
    String filename, filepath;
    SeekBar seekbar, volumeseekbar;
    ArrayList<File> songslist;
    int currentSongIndex;
    File f;
    AudioManager audiomanager;
    MediaMetadataRetriever mmdr;
    MyVolumeFrag volumefrag;
    PreviousFragment previousfragment;
    int x = 0, y = 0;
    SharedPreferences sharedpreferences;
    WifiManager wifimanager;
    boolean flag = true;
    private String ssid = null;
    private String key = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

//*****************************mapping from layout file*********************************************
        showname = (TextView) findViewById(R.id.showname);
        broadcastbtn = (Button) findViewById(R.id.broadcastbtn);
        recievebtn = (Button) findViewById(R.id.recievebtn);
        listbutton = (ImageButton) findViewById(R.id.listbutton);
        play = (ImageButton) findViewById(R.id.play);
        settingsbtn = (ImageButton) findViewById(R.id.settingsbtn);
        previous = (ImageButton) findViewById(R.id.previous);
        next = (ImageButton) findViewById(R.id.next);
        albumart = (ImageView) findViewById(R.id.albumart);
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        currentTime = (TextView) findViewById(R.id.currenttime);
        totalTime = (TextView) findViewById(R.id.totaltime);
        volumebtn = (ImageButton) findViewById(R.id.volumebtn);
        volumeseekbar = (SeekBar) findViewById(R.id.volumeseekbar);

        panelbackground = (TextView) findViewById(R.id.panelbackground);
        volumefrag = new MyVolumeFrag();
        previousfragment = new PreviousFragment();
        audiomanager = (AudioManager) getSystemService(AUDIO_SERVICE);
        sharedpreferences = getSharedPreferences("MY_SHARED_PREFERENCE", MODE_PRIVATE);
        mmdr = new MediaMetadataRetriever();
        wifimanager = (WifiManager) getSystemService(WIFI_SERVICE);

        Intent in = getIntent();
        ssid = in.getStringExtra("username");
        key = in.getStringExtra("password");

//*********************************************getting the index of song from list and setting the filename and filepath********************

        currentSongIndex = in.getIntExtra("songIndex", -1);
        File lastPlayedFile = null;
        try {
            lastPlayedFile = new File(sharedpreferences.getString("lastsongpath", null));

        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
        }


        songslist = MySongLoader.getInstance().getFilesAll();
        if (currentSongIndex >= 0 && currentSongIndex < songslist.size()) {
            f = songslist.get(currentSongIndex);
            if (f != null) {

                filename = f.getName();
                filepath = f.getAbsolutePath();
            }

            System.out.println("size of list is " + songslist.size() + "#######################" + "and Index of song is " + currentSongIndex);
        } else if (lastPlayedFile != null) {
            filepath = lastPlayedFile.getAbsolutePath();
            currentSongIndex = songslist.indexOf(new File(filepath));
            filename = lastPlayedFile.getName();
        }
        if (filepath != null) {
            mediaplayer = MediaPlayer.create(this, Uri.parse(filepath));
            setAlbumArt(filepath);


        }
        showname.setText(filename);
        showname.setSelected(true);


//******************************on changing the progress of seekbar**************************************************

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                long currenttime = mediaplayer.getCurrentPosition();
                currentTime.setText(getTimeString(currenttime));
                long totaltime = mediaplayer.getDuration();
                totalTime.setText(getTimeString(totaltime));
                if (fromUser) {
                    mediaplayer.seekTo(progress);
                    seekBar.setProgress(progress);

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


//*********************************************on clicking the list button**************************************
        listbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listbutton.setBackgroundColor(Color.red(3));
                Intent i = new Intent(MainActivity.this, MyListActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            }
        });

//*********************************************reistering buttons with onClickListener********************
        broadcastbtn.setOnClickListener(this);
        recievebtn.setOnClickListener(this);
        previous.setOnClickListener(this);
        next.setOnClickListener(this);


//************************************************on clicking play button*************************

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filepath != null && mediaplayer != null) {
                    if (mediaplayer.isPlaying()) {
                        play.setImageResource(R.drawable.play2);
                        mediaplayer.pause();

                    } else {
                        play.setImageResource(R.drawable.pause2);
                        mediaplayer.start();
                        new MySeekBarUpdate().start();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Filepath null/ No media selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

//****************************************on clicking previous button*********************************************

        previous.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    previous.setImageResource(R.drawable.backward2fill);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    previous.setImageResource(R.drawable.backward2);
                }
                return false;
            }
        });

//****************************************on clicking next button*********************************************

        next.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    next.setImageResource(R.drawable.forward2fill);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    next.setImageResource(R.drawable.forward2);
                }
                return false;
            }
        });


//****************************************on touching list button button*********************************************

        listbutton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    listbutton.setBackgroundColor(Color.GRAY);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    listbutton.setBackgroundColor(Color.TRANSPARENT);
                }
                return false;
            }
        });
        volumebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Volume button pressed", Toast.LENGTH_SHORT).show();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                if (flag == true) {
                    ft.replace(R.id.container_frame, volumefrag);
                    ft.addToBackStack(null);
                    ft.commit();
                    flag = false;
                } else {
                    ft.replace(R.id.container_frame, previousfragment);
                    ft.addToBackStack(null);
                    ft.commit();
                    flag = true;
                }

            }
        });

        settingsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ip = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(ip);
            }
        });
    }


    //*******************************************method to play the track***********************************************
    public void playTrack(String path) {
        if (path != null) {
            mediaplayer = MediaPlayer.create(this, Uri.parse(path));

            mediaplayer.start();
            play.setImageResource(R.drawable.pause2);

            new MySeekBarUpdate().start();
            setAlbumArt(path);

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("lastsongpath", path);
            editor.commit();
        }
    }

    //********************************on clicking broadcast,receive,next and previous buttons*******************
    public void onClick(View v) {

        if (v == broadcastbtn) {
            if (ssid != null && key != null) {
                Toast.makeText(MainActivity.this, "Hotspot Created", Toast.LENGTH_SHORT).show();
                broadcastbtn.setBackgroundColor(Color.BLACK);
                broadcastbtn.setTextColor(Color.WHITE);
                broadcastbtn.setText("Broadcasting..");
                recievebtn.setBackgroundColor(Color.WHITE);
                recievebtn.setTextColor(Color.BLACK);
                recievebtn.setText("Recieve");

                wifimanager.setWifiEnabled(false);
                System.out.println("Starting...");
                ApManager.isApOn(this); // check Ap state :boolean
                ApManager.configApState(this); // change Ap state :boolean
                setHotspotName(this);
                System.out.println("Started...");


            } else {
                Toast.makeText(MainActivity.this, "Setup hotspot username and password in settings", Toast.LENGTH_SHORT).show();
            }


        } else if (v == recievebtn) {
            Toast.makeText(MainActivity.this, "Wifi turned On", Toast.LENGTH_SHORT).show();
            recievebtn.setBackgroundColor(Color.BLACK);
            recievebtn.setTextColor(Color.WHITE);
            recievebtn.setText("Receiving..");
            broadcastbtn.setBackgroundColor(Color.WHITE);
            broadcastbtn.setTextColor(Color.BLACK);
            broadcastbtn.setText("Broadcast");
            enableWifi();
            ApManager.isApOn(this); // check Ap state :boolean
            ApManager.configApState(this); // change Ap state :boolean

            System.out.println("Connecting...");


        } else if (v == next) {
            System.out.println("Current song index is " + currentSongIndex + "************************************************************ Total tracks are " + songslist.size());
            System.out.println(filepath + "************************************************************");
            if (currentSongIndex < songslist.size() - 1) {
                currentSongIndex++;
                switchSong(currentSongIndex);

            } else if (currentSongIndex == songslist.size() - 1) {
                currentSongIndex = 0;
                switchSong(currentSongIndex);

            }
            showname.setText(filename);
            mediaplayer.reset();
            playTrack(filepath);
            play.setImageResource(R.drawable.pause2);
        } else if (v == previous) {
            System.out.println("Current song index is " + currentSongIndex + "************************************************************ Total tracks are " + songslist.size());
            System.out.println(filepath + "************************************************************");
            if (currentSongIndex > 0) {
                currentSongIndex--;
            } else if (currentSongIndex == 0) {
                currentSongIndex = songslist.size() - 1;
            }
            switchSong(currentSongIndex);


            showname.setText(filename);
            mediaplayer.reset();
            playTrack(filepath);
            play.setImageResource(R.drawable.pause2);
        }

    }

    private void switchSong(int index) {
        f = songslist.get(index);
        if (f != null) {
            filename = f.getName();
            filepath = f.getAbsolutePath();
        } else {
            Toast.makeText(MainActivity.this, toString().valueOf(currentSongIndex), Toast.LENGTH_SHORT).show();
        }
    }

    //***********************************************method to get the time duration of track in required format**********************
    private String getTimeString(long millis) {
        StringBuffer buf = new StringBuffer();
        // long hours = millis/(1000*60*60);
        long minutes = (millis % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = ((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000;
        buf
       /* .append(String.format("%02d",hours))
        .append(":")*/
                .append(String.format("%02d", minutes))
                .append(":")
                .append(String.format("%02d", seconds));

        return buf.toString();
    }

    //**********************************************method for animation*************************************************************
    private void startAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        albumart.clearAnimation();
        albumart.startAnimation(anim);
        panelbackground.clearAnimation();
        panelbackground.startAnimation(anim);

    }

    public boolean setHotspotName(Context context) {
        try {
            wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
            Method getConfigMethod = wifimanager.getClass().getMethod("getWifiApConfiguration");
            WifiConfiguration wifiConfig = (WifiConfiguration) getConfigMethod.invoke(wifimanager);

            wifiConfig.SSID = ssid;
            wifiConfig.preSharedKey = key;

            Method setConfigMethod = wifimanager.getClass().getMethod("setWifiApConfiguration", WifiConfiguration.class);
            setConfigMethod.invoke(wifimanager, wifiConfig);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //***************************************method for hotspot***************************************************************************

    private void enableWifi() {
//        String networkSSID = "DON";
//        String networkPass = "3194@moga##";


        WifiConfiguration wifiConfig = new WifiConfiguration();
//        wifiConfig.SSID = String.format("\"{0}\"", networkSSID);
        wifiConfig.SSID = "\"" + ssid + "\"";
//        wifiConfig.preSharedKey = String.format("\"{0}\"", networkPass);
        wifiConfig.preSharedKey = "\"" + key + "\"";

        System.out.println(wifiConfig.SSID);
        System.out.println(wifiConfig.preSharedKey);
        wifimanager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifimanager.setWifiEnabled(true);
        // Use ID
        int netId = wifimanager.addNetwork(wifiConfig);
        wifimanager.disconnect();
        wifimanager.enableNetwork(netId, true);
        wifimanager.reconnect();
    }
    //************************************************method for wifi****************************************************************

    private void initControls() {
        try {
            volumeseekbar.setMax(audiomanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeseekbar.setProgress(audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC));
            volumeseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                    Toast.makeText(MainActivity.this, progress, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAlbumArt(String path) {
        //**********************************************setting the albumart**************************************
        mmdr.setDataSource(path);
        byte[] data = mmdr.getEmbeddedPicture();
        if (data != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            albumart.setImageBitmap(bitmap);
            startAnimations();
//*********************************************setting the background of media panel according to albumart**********************
            int pixel = bitmap.getPixel(x, y);
            int redValue = Color.red(pixel);
            int greenValue = Color.green(pixel);
            int blueValue = Color.blue(pixel);
            panelbackground.setBackgroundColor(Color.rgb(redValue, greenValue, blueValue));
            albumart.setAdjustViewBounds(true);

        } else {
            albumart.setImageResource(R.drawable.front1);
            albumart.setAdjustViewBounds(true);
        }
    }

    //*******************************************custom Thread for seekbar****************************************
    class MySeekBarUpdate extends Thread {
        public void run() {
            seekbar.setMax(mediaplayer.getDuration());
            while (mediaplayer != null && mediaplayer.isPlaying()) {
                seekbar.setProgress(mediaplayer.getCurrentPosition());
            }
        }
    }
}


