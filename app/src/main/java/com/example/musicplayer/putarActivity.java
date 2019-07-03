package com.example.musicplayer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import ir.mirrajabi.viewfilter.core.ViewFilter;
import ir.mirrajabi.viewfilter.renderers.BlurRenderer;

public class putarActivity extends AppCompatActivity {


    Button btnNext,btnPrev,btnPause;
    TextView judulLagu;
    SeekBar waktulagu;

    static MediaPlayer mediaplayerku;
    int position;
    String sname;

    ArrayList<File> laguku;
    Thread updateseekbar;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_putar);

        ViewFilter.getInstance(this)
                //Use blur effect or implement your custom IRenderer
                .setRenderer(new BlurRenderer(16))
                .applyFilterOnView(findViewById(R.id.bgputar),
                        findViewById(R.id.rootview));

        btnNext=(Button)findViewById(R.id.next);
        btnPrev=(Button)findViewById(R.id.prev);
        btnPause=(Button)findViewById(R.id.pause);
        judulLagu=(TextView)findViewById(R.id.judulLagu);
        waktulagu=(SeekBar)findViewById(R.id.seekBar);

        getSupportActionBar().setTitle("Memainkan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        updateseekbar= new Thread(){
            @Override
            public void run() {
                int totalDurasi = mediaplayerku.getDuration();
                int currentPosisi = 0;

                while (currentPosisi<totalDurasi){
                    try {
                        sleep(500);
                        currentPosisi= mediaplayerku.getCurrentPosition();
                        waktulagu.setProgress(currentPosisi);
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        if (mediaplayerku!=null){
            mediaplayerku.stop();
            mediaplayerku.release();
        }
        Intent i= getIntent();
        Bundle bundle = i.getExtras();

        laguku = (ArrayList) bundle.getParcelableArrayList("lagu");
        sname = laguku.get(position).getName().toString();
        final String namaLagu = i.getStringExtra("namalagu");

        judulLagu.setText(namaLagu);
        judulLagu.setSelected(true);

        position= bundle.getInt("pos",0);
        Uri u= Uri.parse(laguku.get(position).toString());

        mediaplayerku = MediaPlayer.create(getApplicationContext(),u);
        mediaplayerku.start();
        waktulagu.setMax(mediaplayerku.getDuration());

        updateseekbar.start();

        waktulagu.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        waktulagu.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.SRC_IN);



        waktulagu.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaplayerku.seekTo(seekBar.getProgress());

            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waktulagu.setMax(mediaplayerku.getDuration());

                if (mediaplayerku.isPlaying()){
                    btnPause.setBackgroundResource(R.drawable.play);
                    mediaplayerku.pause();
                }
                else {
                    btnPause.setBackgroundResource(R.drawable.pause);
                    mediaplayerku.start();
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaplayerku.stop();
                mediaplayerku.release();
                position=((position+1)%laguku.size());

                Uri u = Uri.parse(laguku.get(position).toString());

                mediaplayerku= MediaPlayer.create(getApplicationContext(),u);
                sname=laguku.get(position).getName().toString();
                judulLagu.setText(sname);

                mediaplayerku.start();
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaplayerku.stop();
                mediaplayerku.release();

                position =((position-1)<0)?(laguku.size()-1):(position-1);
                Uri u = Uri.parse(laguku.get(position).toString());
                mediaplayerku= MediaPlayer.create(getApplicationContext(),u);
                sname=laguku.get(position).getName().toString();
                judulLagu.setText(sname);

                mediaplayerku.start();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);

    }
}
