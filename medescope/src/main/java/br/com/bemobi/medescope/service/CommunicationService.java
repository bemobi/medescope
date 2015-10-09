package br.com.bemobi.medescope.service;

/**
 * Created by bkosawa on 02/07/15.
 */
public interface CommunicationService {

    void sendFinishWithSuccessBroadcastData(String downloadId, String filePath, String data);

    void sendFinishWithErrorBroadcastData(String downloadId, int reason, String data);

    void sendDownloadStatusNotEnqueue(String downloadId);

    void sendDownloadStatusPaused(String downloadId, int reason);

    void sendDownloadStatusProgress(String downloadId, int progress);

    void showDownloadQueue();

    void sendCancelled(String downloadId);
}
