package com.ht.fyforandroid.net.MultiThreadDownloader.bizs;

import com.ht.fyforandroid.net.MultiThreadDownloader.interfaces.IDListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 下载实体类
 * Download entity.
 *
 * @author AigeStudio 2015-05-16
 */
class DLInfo {
    int redirect;
    int totalBytes;
    int currentBytes;
    boolean hasListener;
    boolean isResume;
    String fileName;
    String dirPath;
    String baseUrl;
    String realUrl;
    String mimeType;
    String eTag;
    String disposition;
    String location;
    List<DLHeader> requestHeaders;
    final List<DLThreadInfo> threads;
    IDListener listener;
    File file;

    DLInfo() {
        threads = new ArrayList<>();
    }

    synchronized void addDLThread(DLThreadInfo info) {
        threads.add(info);
    }

    synchronized void removeDLThread(DLThreadInfo info) {
        threads.remove(info);
    }
}