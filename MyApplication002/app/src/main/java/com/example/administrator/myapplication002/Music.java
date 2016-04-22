package com.example.administrator.myapplication002;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.Service;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.view.menu.MenuView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.app.Activity;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer.OnCompletionListener;

import org.apache.http.protocol.ResponseDate;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.Format;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Music extends ActionBarActivity{
    int flag1=0;
    int flag2=0;
    int flag3=0;
    int flag4=0;
    int flag5=0;
    int flag6=1;
    private MediaPlayer mp=new MediaPlayer();
    ArrayList<String> history = new ArrayList<String>();
    ArrayList<String> musicurl = new ArrayList<String>();
    String[] musicpath;
    SimpleAdapter adapter;

    public static final String ACTION_MEDIA_SCANNER_SCAN_DIR = "android.intent.action.MEDIA_SCANNER_SCAN_DIR";
    public void scanDirAsync(Context ctx, String dir) {
        Intent scanIntent = new Intent(ACTION_MEDIA_SCANNER_SCAN_DIR);
        scanIntent.setData(Uri.fromFile(new File(dir)));
        ctx.sendBroadcast(scanIntent);
    }
    public ArrayList<String> scanAllAudioTitle(){
//生成动态数组，并且转载数据
        ArrayList<String> musictitle = new ArrayList<String>();
//查询媒体数据库
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
//遍历媒体数据库
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()) {
                //歌曲标题
                String tilte = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                //歌曲文件的大小 ：MediaStore.Audio.Media.SIZE
                Long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                if(size>1024*800){//大于800K
                    musictitle.add(tilte);
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        return musictitle;
    }

    public ArrayList<String> scanAllAudioUrl() {
//生成动态数组，并且转载数据
        ArrayList<String> musicurl = new ArrayList<String>();
//查询媒体数据库
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
//遍历媒体数据库
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()) {
                //歌曲文件的路径 ：MediaStore.Audio.Media.DATA
                String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                //歌曲文件的大小 ：MediaStore.Audio.Media.SIZE
                Long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                if(size>1024*800){//大于800K
                    musicurl.add(url);
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        return musicurl;
    }

    public ArrayList<String> scanAllAudioArtist(){
//生成动态数组，并且转载数据
        ArrayList<String> musicartist = new ArrayList<String>();
//查询媒体数据库
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
//遍历媒体数据库
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()) {
                //歌曲的歌手名： MediaStore.Audio.Media.ARTIST
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                //歌曲文件的大小 ：MediaStore.Audio.Media.SIZE
                Long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                if(size>1024*800){//大于800K
                    musicartist.add(artist);
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        return musicartist;
    }

    public ArrayList<String> scanAllAudioAlbum(){
//生成动态数组，并且转载数据
        ArrayList<String> musicalbum = new ArrayList<String>();
//查询媒体数据库
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
//遍历媒体数据库
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()) {
                //歌曲的专辑名：MediaStore.Audio.Media.ALBUM
                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                //歌曲文件的大小 ：MediaStore.Audio.Media.SIZE
                Long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                if(size>1024*800){//大于800K
                    musicalbum.add(album);
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        return musicalbum;
    }

    public ArrayList<String> scanAllAudioSize(){
//生成动态数组，并且转载数据
        ArrayList<String> musicsize = new ArrayList<String>();
//查询媒体数据库
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
//遍历媒体数据库
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()) {
                //歌曲文件的大小 ：MediaStore.Audio.Media.SIZE
                Long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                if(size>1024*800){//大于800K
                    musicsize.add(Long.toString(size));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        return musicsize;
    }

    public ArrayList<String> scanAllAudioSongid(){
//生成动态数组，并且转载数据
        ArrayList<String> musicid= new ArrayList<String>();
//查询媒体数据库
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
//遍历媒体数据库
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()) {
                //歌曲编号
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                Long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                if(size>1024*800){//大于800K
                    musicid.add(String.valueOf(id));
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        return musicid;
    }

    public ArrayList<String> scanAllAudioAlbumid(){
//生成动态数组，并且转载数据
        ArrayList<String> musicalbumid= new ArrayList<String>();
//查询媒体数据库
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
//遍历媒体数据库
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()) {
                Long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                String albumid = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                if(size>1024*800){//大于800K
                    musicalbumid.add(albumid);
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        return musicalbumid;
    }

    public void mediacreat(final String[] mpath,int num){
        if(mp!=null) {
            if (mp.isPlaying()) {
                mp.stop();
            }
        }
        mp.release();
        mp=null;

        Uri uri = Uri.parse(mpath[num]);
        mp=MediaPlayer.create(Music.this,uri);
        mp.setLooping(false);

        SeekBar mprogress=(SeekBar)findViewById(R.id.musicprogress);
        mprogress.setMax(mp.getDuration());

        TextView atime=(TextView)findViewById(R.id.atime);
        String mi;
        if((((mp.getDuration())/1000)/60)<10){
            mi = "0"+Integer.toString(((mp.getDuration())/1000)/60);
        }
        else{
            mi = Integer.toString(((mp.getDuration())/1000)/60);
        }
        String se;
        if((((mp.getDuration()) / 1000) - 60 * ((int) (((mp.getDuration()) / 1000) / 60)))<10){
            se="0"+Integer.toString(((mp.getDuration()) / 1000) - 60 * ((int) (((mp.getDuration()) / 1000) / 60)));
        }
        else{
            se=Integer.toString(((mp.getDuration()) / 1000) - 60 * ((int) (((mp.getDuration()) / 1000) / 60)));
        }
        atime.setText(mi+":"+se);

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                Button play = (Button) findViewById(R.id.play);
                flag1 = 0;
                play.setText("Play");
                play.setBackgroundResource(R.drawable.r_button_circle);
                flag2 = (++flag2) % (mpath.length);
                flag1 = 1;
                mediacreat(mpath, flag2);
                mediastart();
                play.setText("Stop");
                play.setBackgroundResource(R.drawable.g_button_circle);
            }

        });

        ArrayList<String> musictitle=scanAllAudioTitle();
        final String[] title = new String[musictitle.size()];
        musictitle.toArray(title);
        TextView Title=(TextView)findViewById(R.id.Title);
        Title.setGravity(Gravity.CENTER);
        Title.setText(title[num]);

        ArrayList<String> musicartist=scanAllAudioArtist();
        final String[] artist = new String[musicartist.size()];
        musicartist.toArray(artist);
        TextView Artist=(TextView)findViewById(R.id.Artist);
        Artist.setText(artist[num]);

        ArrayList<String> musicalbumid=scanAllAudioAlbumid();
        final String[] albumid = new String[musicalbumid.size()];
        musicalbumid.toArray(albumid);
        int aid=Integer.parseInt(albumid[num]);

        ArrayList<String> musicid=scanAllAudioSongid();
        final String[] id = new String[musicid.size()];
        musicid.toArray(id);

        String albumArt = getAlbumArt(aid);
        ImageView mImageView = (ImageView) findViewById(R.id.albumimage);
        Bitmap bm = null;
        if (albumArt == null) {
            mImageView.setImageDrawable(null);
            Drawable drawable = this.getResources().getDrawable(R.drawable.all);
            mImageView.setImageDrawable(drawable);
        } else {
            bm = BitmapFactory.decodeFile(albumArt);
            BitmapDrawable bmpDraw = new BitmapDrawable(bm);
            mImageView.setImageDrawable(bmpDraw);
        }
    }


    private String getAlbumArt(int album_id) {
        String mUriAlbums ="content://media/external/audio/albums";
        String[] projection = new String[] { "album_art" };
        Cursor cur = this.getContentResolver().query(
                Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)),
                projection, null, null, null);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        cur = null;
        return album_art;
    }

    public void mediastart(){
        mp.start();
        progresshandler.post(updateThread);
        timehandler.post(timeThread);
    }

    public void mediapause(){
        if(mp!=null) {
            if (mp.isPlaying()) {
                mp.pause();
            }
        }
    }

    Handler volumehandler = new Handler();
    final Runnable volumeThread=new Runnable() {
        @Override
        public void run() {
            final SeekBar volumer=(SeekBar)findViewById(R.id.Volume);
            AudioManager audiomanage = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
            int maxVolume = audiomanage.getStreamMaxVolume(AudioManager.STREAM_MUSIC);  //获取系统最大音量
            int currentVolume = audiomanage.getStreamVolume(AudioManager.STREAM_MUSIC);  //获取当前值
            volumer.setProgress(currentVolume);
            volumehandler.postDelayed(volumeThread, 100);
        }
    };

    Handler progresshandler = new Handler();
    final Runnable updateThread=new Runnable() {
        @Override
        public void run() {
            final SeekBar mprogress=(SeekBar)findViewById(R.id.musicprogress);
            mprogress.setProgress(mp.getCurrentPosition());
            progresshandler.postDelayed(updateThread, 100);
        }
    };

    Handler timehandler = new Handler();
    final Runnable timeThread=new Runnable() {
        @Override
        public void run() {
            TextView ntime = (TextView) findViewById(R.id.ntime);
            String mi1;
            if((((mp.getCurrentPosition())/1000)/60)<10){
                mi1 = "0"+Integer.toString(((mp.getCurrentPosition())/1000)/60);
            }
            else{
                mi1 = Integer.toString(((mp.getCurrentPosition())/1000)/60);
            }
            String se1;
            if((((mp.getCurrentPosition()) / 1000) - 60 * ((int) (((mp.getCurrentPosition()) / 1000) / 60)))<10){
                se1="0"+Integer.toString(((mp.getCurrentPosition()) / 1000) - 60 * ((int) (((mp.getCurrentPosition()) / 1000) / 60)));
            }
            else{
                se1=Integer.toString(((mp.getCurrentPosition()) / 1000) - 60 * ((int) (((mp.getCurrentPosition()) / 1000) / 60)));
            }
            ntime.setText(mi1+":"+se1);
            TextView atime = (TextView) findViewById(R.id.atime);
            int resttime=mp.getDuration()-mp.getCurrentPosition();
            String mi2;
            if((((resttime)/1000)/60)<10){
                mi2 = "0"+Integer.toString(((resttime)/1000)/60);
            }
            else{
                mi2 = Integer.toString(((resttime)/1000)/60);
            }
            String se2;
            if((((resttime) / 1000) - 60 * ((int) (((resttime) / 1000) / 60)))<10){
                se2="0"+Integer.toString(((resttime) / 1000) - 60 * ((int) (((resttime) / 1000) / 60)));
            }
            else{
                se2=Integer.toString(((resttime) / 1000) - 60 * ((int) (((resttime) / 1000) / 60)));
            }
            atime.setText(mi2+":"+se2);
            timehandler.postDelayed(timeThread, 100);
        }
    };

    private List<Map<String, Object>> getData(String[] mpath) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();

        ArrayList<String> musictitle=scanAllAudioTitle();
        final String[] title = new String[musictitle.size()];
        musictitle.toArray(title);

        ArrayList<String> musicartist=scanAllAudioArtist();
        final String[] artist = new String[musicartist.size()];
        musicartist.toArray(artist);

        ArrayList<String> musicsize=scanAllAudioSize();
        final String[] size = new String[musicartist.size()];
        musicsize.toArray(size);

        ArrayList<String> musicalbum=scanAllAudioAlbum();
        final String[] album = new String[musicartist.size()];
        musicalbum.toArray(album);
        for(int i=0;i<mpath.length;i++)
        {
            map = new HashMap<String, Object>();
            map.put("title", title[i]);

            int mass=Integer.parseInt(size[i]);
            mass=mass*10/1024/1024;
            int mass1=mass/10;
            int mass2=mass-mass/10;
            String Mass1=Integer.toString(mass1);
            String Mass2=Integer.toString(mass2);
            if(album[i].equals("sdcard"))
            {
                map.put("info", Mass1 + "." + Mass2 + "M" + "·" + artist[i]);
            }
            else{
                map.put("info", Mass1 + "." + Mass2 + "M" + "·" + artist[i]+"·《"+album[i]+"》");
            }
            list.add(map);
        }
        return list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        scanDirAsync(this, "/storage/sdcard/");

        musicurl=scanAllAudioUrl();
        musicpath= new String[musicurl.size()];
        musicurl.toArray(musicpath);
        final ListView musiclist = (ListView) findViewById(R.id.Musiclist);
        adapter = new SimpleAdapter(Music.this,getData(musicpath),R.layout.eachmusic,
                          new String[]{"title","info"},
                          new int[]{R.id.title,R.id.info});
        musiclist.setAdapter(adapter);
        mediacreat(musicpath,flag2);

        Button play = (Button) findViewById(R.id.play);
        play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Button text = (Button) v;
                if (flag1 == 0) {
                    text.setText("Stop");
                    text.setBackgroundResource(R.drawable.g_button_circle);
                    mediastart();
                }
                if (flag1 == 1) {
                    text.setText("Play");
                    text.setBackgroundResource(R.drawable.r_button_circle);
                    mediapause();
                    progresshandler.removeCallbacks(updateThread);
                    timehandler.removeCallbacks(timeThread);
                }
                if (flag1 == 1) flag1 = 0;
                else flag1 = 1;
            }
        });

        Button mod = (Button) findViewById(R.id.mod);
        mod.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Button text = (Button) v;
                if(flag3==0)
                {
                    text.setText("Ran.");
                    flag3=1;
                }
                else if(flag3==1)
                {
                    text.setText("Cyc.");
                    flag3=2;
                    flag5=0;
                    history.clear();
                }
                else if(flag3==2)
                {
                    text.setText("Ord.");
                    flag3=0;
                }
            }
        });

        Button forward = (Button) findViewById(R.id.forward);
        forward.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Button text = (Button) v;
                if(flag3==0)
                {
                    flag2 = (++flag2) % (musicpath.length);
                }
                if(flag3==1)
                {
                    int n;
                    flag4=flag2;
                    if(musicpath.length!=1) {
                        while (true) {
                            n = (int) (Math.random() * musicpath.length);
                            if(history.size()==(musicpath.length-1))
                            {
                                history.clear();
                            }
                            else
                            {
                                final String[] musichistory = new String[history.size()];
                                history.toArray(musichistory);
                                int is=0;
                                for(int m=0;m<history.size();m++)
                                {
                                    int h=Integer.parseInt(musichistory[m]);
                                    if(n==h||n==flag2) {is=1;}
                                }
                                if(is==0){break;}
                            }
                        }
                        flag5=1;
                        flag2=n;
                        history.add(Integer.toString(flag2));
                    }
                }
                if(flag3==2)
                {
                    ;
                }
                flag1 = 1;
                mediacreat(musicpath, flag2);
                mediastart();
                Button play = (Button) findViewById(R.id.play);
                play.setText("Stop");
                play.setBackgroundResource(R.drawable.g_button_circle);
            }
        });

        Button backward = (Button) findViewById(R.id.backward);
        backward.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Button text = (Button) v;
                if(flag3==0)
                {
                    flag2 = (flag2 + (musicpath.length) - 1) % (musicpath.length);
                }
                if(flag3==1)
                {
                    if(flag5==1&&flag2!=flag4)
                    {
                        flag2=flag4;
                    }
                    else{
                        int n;
                        flag4=flag2;
                        if(musicpath.length!=1) {
                            while (true) {
                                n = (int) (Math.random() * musicpath.length);
                                if(history.size()==(musicpath.length-1))
                                {
                                    history.clear();
                                }
                                else
                                {
                                    final String[] musichistory = new String[history.size()];
                                    history.toArray(musichistory);
                                    int is=0;
                                    for(int m=0;m<history.size();m++)
                                    {
                                        int h=Integer.parseInt(musichistory[m]);
                                        if(n==h||n==flag2) {is=1;}
                                    }
                                    if(is==0){break;}
                                }
                            }
                            flag5=0;
                            flag2=n;
                            history.add(Integer.toString(flag2));
                        }
                    }
                }
                if(flag3==2)
                {
                    ;
                }
                flag1 = 1;
                mediacreat(musicpath, flag2);
                mediastart();
                Button play = (Button) findViewById(R.id.play);
                play.setText("Stop");
                play.setBackgroundResource(R.drawable.g_button_circle);
            }
        });

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                Button play = (Button) findViewById(R.id.play);
                flag1 = 0;
                play.setText("Play");
                play.setBackgroundResource(R.drawable.r_button_circle);
                    if (flag3 == 0) {
                        flag2 = (++flag2) % (musicpath.length);
                    }
                    if (flag3 == 1) {
                        if(flag5==1&&flag2!=flag4)
                        {
                            flag2=flag4;
                        }
                        else{
                            int n;
                            flag4=flag2;
                            if(musicpath.length!=1) {
                                while (true) {
                                    n = (int) (Math.random() * musicpath.length);
                                    if(history.size()==(musicpath.length-1))
                                    {
                                        history.clear();
                                    }
                                    else
                                    {
                                        final String[] musichistory = new String[history.size()];
                                        history.toArray(musichistory);
                                        int is=0;
                                        for(int m=0;m<history.size();m++)
                                        {
                                            int h=Integer.parseInt(musichistory[m]);
                                            if(n==h||n==flag2) {is=1;}
                                        }
                                        if(is==0){break;}
                                    }
                                }
                                flag5=0;
                                flag2=n;
                                history.add(Integer.toString(flag2));
                            }
                        }
                    }
                    if (flag3 == 2) {
                        mp.stop();
                    }
                    mediacreat(musicpath, flag2);
                    mediastart();
                    play.setText("Stop");
                    play.setBackgroundResource(R.drawable.g_button_circle);
            }
        });

        final SeekBar musicprogress = (SeekBar) findViewById(R.id.musicprogress);
        musicprogress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                mediastart();
                flag1 = 1;
                Button play = (Button) findViewById(R.id.play);
                play.setText("Stop");
                play.setBackgroundResource(R.drawable.g_button_circle);
            }

            @Override

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override

            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (fromUser == true) {
                    mp.seekTo(progress);
                }
            }
        });

        final SeekBar volume = (SeekBar) findViewById(R.id.Volume);
        final AudioManager audiomanage = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        final int maxVolume = audiomanage.getStreamMaxVolume(AudioManager.STREAM_MUSIC);  //获取系统最大音量
        volume.setMax(maxVolume);   //拖动条最高值与系统最大声匹配
        int currentVolume = audiomanage.getStreamVolume(AudioManager.STREAM_MUSIC);  //获取当前值
        volume.setProgress(currentVolume);
        volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                seekBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override

            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (fromUser == true) {
                    audiomanage.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                    int currentVolume=audiomanage.getStreamVolume(AudioManager.STREAM_MUSIC);  //获取当前值
                    seekBar.setProgress(currentVolume);
                }
            }
        });

        Button musicvolume = (Button) findViewById(R.id.v);
        musicvolume.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final SeekBar volume = (SeekBar) findViewById(R.id.Volume);
                if(volume.getVisibility()==View.INVISIBLE) {
                    volume.setVisibility(View.VISIBLE);
                }
                else{
                    volume.setVisibility(View.INVISIBLE);
                }
            }
        });

        musiclist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
                if(flag2==arg2)
                {
                    flag1 = 1;
                    mediastart();
                    Button play = (Button) findViewById(R.id.play);
                    play.setText("Stop");
                    play.setBackgroundResource(R.drawable.g_button_circle);
                }
                else{
                    flag2=arg2;
                    mediacreat(musicpath, flag2);
                    mediastart();
                    flag1 = 1;
                    Button play = (Button) findViewById(R.id.play);
                    play.setText("Stop");
                    play.setBackgroundResource(R.drawable.g_button_circle);
                }
                history.clear();
                ImageView mImageView = (ImageView) findViewById(R.id.albumimage);
                mImageView.setVisibility(View.VISIBLE);
                musiclist.setVisibility(View.INVISIBLE);
                flag6=0;
            }
        });

        ImageView mImageView = (ImageView) findViewById(R.id.albumimage);
        mImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.INVISIBLE);
                musiclist.setVisibility(View.VISIBLE);
                flag6=1;
            }
        });

        TextView title=(TextView) findViewById(R.id.Title);
        title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView mImageView = (ImageView) findViewById(R.id.albumimage);
                mImageView.setVisibility(View.VISIBLE);
                musiclist.setVisibility(View.INVISIBLE);
                flag6=0;
            }
        });

        TextView artist = (TextView) findViewById(R.id.Artist);
        artist.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new  AlertDialog.Builder(Music.this)
                        .setTitle("Copyright" )
                        .setMessage("  This app is producted by UESTC.\n  Designed by\n  Zhang Mingjie & Zhang Junpeng.\n                             Copyright © 2015" )
                        .setPositiveButton("Sure" ,  null )
                        .show();
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            // 创建退出对话框
            AlertDialog isExit = new AlertDialog.Builder(this).create();
            // 设置对话框标题
            isExit.setTitle("System Tips");
            // 设置对话框消息
            isExit.setMessage("Sure you want to exit?");
            // 添加选择按钮并注册监听
            isExit.setButton("Close", listener);
            isExit.setButton2("Exit", listener);
            // 显示对话框
            isExit.show();

        }
        return false;
    }

    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case AlertDialog.BUTTON_POSITIVE:// "Close"按钮转到后台
                    moveTaskToBack(false);
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "Exit"第二个按钮退出程序
                    timehandler.removeCallbacks(timeThread);
                    progresshandler.removeCallbacks(updateThread);
                    volumehandler.removeCallbacks(volumeThread);
                    mp.release();
                    mp=null;
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_music, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

}
