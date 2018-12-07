package com.example.mike.sdcardscanner.ui;

import com.example.mike.sdcardscanner.base.BasePresenter;
import com.example.mike.sdcardscanner.base.BaseView;

import java.io.File;
import java.util.ArrayList;

public class MainContract {

    interface View extends BaseView{
        void onUpdate(File file);
        void onStartScan();
        void onFinishScan(ArrayList<File> files);
        void onStopScan();
    }
    interface Presenter extends BasePresenter{
        void scanFiles();
        void stopScanFiles();
        void onAttach(View view);
        void onDetach();
    }

}
