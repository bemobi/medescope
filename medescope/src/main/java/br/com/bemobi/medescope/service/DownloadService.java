package br.com.bemobi.medescope.service;

import android.content.Context;

import java.util.Map;

import br.com.bemobi.medescope.model.DownloadInfo;

/**
 * Created by bkosawa on 30/06/15.
 */
public interface DownloadService {

    void shutdown();

    boolean enqueue(String downloadId, String uri, String fileName, String title, String description, String data, boolean shouldDownloadOnlyInWifi, Map<String, String> customHeaders);

    boolean isDownloadManagerUiActivated();

    boolean isDownloadManagerUiDeactivated();

    boolean isDownloadManagerActivated();

    boolean isDownloadManagerDeactivated(Context context);

    boolean cancel(String downloadId);

    void cleanupId(String downloadId);

    void notificationClicked(String[] downloadIds);

    DownloadInfo getDownloadInfo(String downloadId);
}
