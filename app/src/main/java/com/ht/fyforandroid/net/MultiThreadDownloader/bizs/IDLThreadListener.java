package com.ht.fyforandroid.net.MultiThreadDownloader.bizs;

interface IDLThreadListener {
    void onProgress(int progress);

    void onStop(DLThreadInfo threadInfo);

    void onFinish(DLThreadInfo threadInfo);
}