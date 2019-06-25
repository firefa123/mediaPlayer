package com.example.musicplayer;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    ListView listUntukLagu;
    String[] item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listUntukLagu = (ListView) findViewById(R.id.listlagu);
        runPermission();
    }

    public void runPermission(){
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                    token.continuePermissionRequest();
                    }
                }).check();
    }

    public ArrayList <File> carilagu (File file) {
        ArrayList<File> arrayList = new ArrayList<>();

        File[] files = file.listFiles();
        for (File satufile: files){

            if (satufile.isDirectory() && !satufile.isHidden()){
                arrayList.addAll(carilagu(satufile));
            }
            else {
                if (satufile.getName().endsWith(".mp3") ||
                satufile.getName().endsWith(".wav")){
                    arrayList.add(satufile);
                }
            }
        }
        return arrayList;
    }

    void tampil(){
        final ArrayList<File> laguku = carilagu(Environment.getExternalStorageDirectory());

        item = new String[laguku.size()];
        for (int i=0;i<laguku.size();i++){

            item[i]= laguku.get(i).getName().toString().replace(".mp3","").replace(".wav","");

        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,item);
        listUntukLagu.setAdapter(adapter);

        listUntukLagu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String namaLagu =  listUntukLagu.getItemAtPosition(i).toString();

                startActivity(new Intent(getApplicationContext(),putarActivity.class).putExtra("lagu",laguku).putExtra("namalagu",namaLagu).putExtra("pos",i));

            }
        });
    }
}
