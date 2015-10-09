package br.com.bemobi.medescope.callback;

/**
 * Created by bkosawa on 08/07/15.
 */
public interface DownloadStatusCallback {
    void onDownloadNotEnqueued(String downloadId);

    void onDownloadPaused(String downloadId, int reason);

    void onDownloadInProgress(String downloadId, int progress);

    void onDownloadOnFinishedWithError(String downloadId, int reason, String data);

    void onDownloadOnFinishedWithSuccess(String downloadId, String filePath, String data);

    void onDownloadCancelled(String downloadId);
}
