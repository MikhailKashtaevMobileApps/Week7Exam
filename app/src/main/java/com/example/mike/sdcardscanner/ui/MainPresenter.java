package com.example.mike.sdcardscanner.ui;

import android.os.Environment;
import android.util.Log;

import com.example.mike.sdcardscanner.SD.SDFilesScanner;

import java.io.File;
import java.util.ArrayList;

public class MainPresenter implements MainContract.Presenter {

    private static final String TAG = "__TAG__";
    private MainContract.View view;
    private Boolean scanning = false;
    private static MainPresenter instance;

    SDFilesScanner scanner;

    public MainPresenter(SDFilesScanner scanner) {
        this.scanner = scanner;
    }

    public static MainPresenter getInstance(){
        if ( instance == null ){
            instance = new MainPresenter( new SDFilesScanner() );
            return instance;
        }else{
            return instance;
        }
    }

    @Override
    public void scanFiles() {
        if ( !scanning ){
            scanning = true;
            view.onStartScan();
            scanner.getListCallback( Environment.getExternalStorageDirectory(), new SDFilesScanner.OnScan() {
                @Override
                public void scan(File file) {
                    if ( getView() != null ){
                        Log.d(TAG, "scan: "+getView().toString());
                        getView().onUpdate(file);
                    }
                }

                @Override
                public void onComplete(ArrayList<File> files) {
                    scanning = false;
                    if ( getView() != null ){
                        getView().onFinishScan( files );
                    }
                }

                @Override
                public void onError(String e) {
                    scanning = false;
                }

            });
        }

    }

    @Override
    public void stopScanFiles() {
        scanner.stop();
        scanning = false;
        view.onStopScan();
    }

    @Override
    public void onAttach(MainContract.View view) {
        this.view = view;
    }

    @Override
    public void onDetach() {
        this.view = null;
    }

    public MainContract.View getView() {
        return view;
    }

    public void setView(MainContract.View view) {
        this.view = view;
    }
}
