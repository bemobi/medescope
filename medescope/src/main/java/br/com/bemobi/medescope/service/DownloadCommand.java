package br.com.bemobi.medescope.service;

import android.os.Bundle;

import br.com.bemobi.medescope.model.DownloadRequest;
import br.com.bemobi.medescope.model.DownloadInfo;

/**
 * Created by bkosawa on 30/06/15.
 */
public interface DownloadCommand {

    void startCommand();

    void executeCommand(String action, Bundle extras);

    void enqueue(DownloadRequest downloadRequest);

    void cancelAction(String downloadId);

    void finishAction(String downloadId, DownloadInfo downloadInfo);

    void clickNotificationAction(String[] downloadIds);

    void shutdownCommand();
}
