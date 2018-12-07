package com.example.mike.sdcardscanner.ui;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.mike.sdcardscanner.R;
import com.example.mike.sdcardscanner.SD.SDFilesScanner;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainContract.View{

    public static final String TAG = "__TAG__";
    private TextView tvMain;
    private MainPresenter mainPresenter;
    private TextView tvCurrentFile;
    private RecyclerView rvFiles;
    private FileRecyclerAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCurrentFile = findViewById( R.id.tvCurrentFile );
        rvFiles = findViewById( R.id.rvFiles );
        adapter = new FileRecyclerAdapter( new ArrayList<File>() );
        rvFiles.setLayoutManager( new LinearLayoutManager(this));
        rvFiles.setAdapter( adapter );

        mainPresenter = MainPresenter.getInstance();
        mainPresenter.onAttach(this);

        if ( checkSelfPermission( "android.permission.READ_EXTERNAL_STORAGE" ) != PackageManager.PERMISSION_GRANTED){
            requestPermissions( new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 1 );
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.onDetach();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void scanFiles(){
        if ( checkSelfPermission( "android.permission.READ_EXTERNAL_STORAGE" ) != PackageManager.PERMISSION_GRANTED){
            requestPermissions( new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 1 );
            return;
        }
        mainPresenter.scanFiles();
    }

    @Override
    public void onUpdate(File file) {
        Log.d(TAG, "onUpdate: "+file.getName());
        tvCurrentFile.setText( file.getName() );
    }

    @Override
    public void onStartScan() {
        tvCurrentFile.setText( "Scan started" );
    }

    @Override
    public void onFinishScan(ArrayList<File> files) {
        adapter.files = files;
        adapter.notifyDataSetChanged();
        tvCurrentFile.setText( "Scan finished, total of:"+files.size()+" files" );
    }

    @Override
    public void onStopScan() {
        tvCurrentFile.setText( "Scan Stopped" );
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void startScan(View view) {
        scanFiles();
    }

    public void stopScan(View view) {
        mainPresenter.stopScanFiles();
    }

}
