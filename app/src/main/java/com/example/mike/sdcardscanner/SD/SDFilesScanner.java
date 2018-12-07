package com.example.mike.sdcardscanner.SD;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class SDFilesScanner {

    private static final String TAG = "__TAG__";
    private boolean stop = false;

    public ArrayList<File> getList(File f, final OnScan updater){
        File[] arrayFile = f.listFiles();
        if (arrayFile != null && !stop) {

            ArrayList<File> listFile = new ArrayList<>( );
            for (final File anArrayFile : arrayFile) {

                if ( stop ){
                    break;
                }
                if (anArrayFile.isDirectory()) {
                    listFile.addAll(getList( anArrayFile, updater));
                } else {

                    // Immitate slowness
                    try {
                        Thread.sleep(new Random().nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    listFile.add( anArrayFile );

                    Completable.fromAction(new Action() {
                        @Override
                        public void run() throws Exception {
                            updater.scan( anArrayFile );
                        }
                    }).subscribeOn(AndroidSchedulers.mainThread())
                            .subscribe();

                }
            }
            return listFile;
        }else{
            return new ArrayList<File>();
        }
    }

    public void stop(){
        this.stop = true;
    }

    public void getListCallback(final File f, final OnScan updater){
        stop = false;
        Single.fromCallable(new Callable<ArrayList<File>>() {
            @Override
            public ArrayList<File> call() throws Exception {
                return getList(f, updater);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ArrayList<File>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onSuccess(ArrayList<File> files) {
                        updater.onComplete(files);
                    }
                    @Override
                    public void onError(Throwable e) {
                        updater.onError(e.getMessage());
                    }
                });
    }

    public interface OnScan{
        void scan(File file);
        void onComplete(ArrayList<File> files);
        void onError(String e);
    }

}
