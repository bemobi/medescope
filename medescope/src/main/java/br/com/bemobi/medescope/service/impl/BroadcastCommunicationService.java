package br.com.bemobi.medescope.service.impl;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import br.com.bemobi.medescope.Medescope;
import br.com.bemobi.medescope.constant.DownloadConstants;
import br.com.bemobi.medescope.log.Logger;
import br.com.bemobi.medescope.service.CommunicationService;
import br.com.bemobi.medescope.service.DownloadService;

/**
 * Created by bkosawa on 02/07/15.
 */
public class BroadcastCommunicationService implements CommunicationService {

    private static final String TAG = BroadcastCommunicationService.class.getSimpleName();

    private static CommunicationService instance;

    private final Context mContext;

    private BroadcastCommunicationService(Context context){
        this.mContext = context;
    }

    public static CommunicationService getInstance(Context context){
        if(instance == null){
            instance = new BroadcastCommunicationService(context);
        }
        return instance;
    }

    @Override
    public void sendFinishWithSuccessBroadcastData(String downloadId, String filePath, String data) {
        Logger.debug(TAG, DownloadConstants.LOG_FEATURE_DOWNLOAD, "sendFinishWithSuccessBroadcastData with data: " + data);
        Intent finishIntent = new Intent();
        finishIntent.setAction(Medescope.ACTION_BROADCAST_FINISH_WITH_SUCCESS);
        finishIntent.setPackage(mContext.getPackageName());
        finishIntent.putExtra(DownloadConstants.EXTRA_STRING_DOWNLOAD_ID, downloadId);
        finishIntent.putExtra(DownloadConstants.EXTRA_STRING_FILE_PATH, filePath);
        finishIntent.putExtra(DownloadConstants.EXTRA_STRING_JSON_DATA, data);
        mContext.sendBroadcast(finishIntent);
    }

    @Override
    public void sendFinishWithErrorBroadcastData(String downloadId, int reason, String data) {
        Logger.debug(TAG, DownloadConstants.LOG_FEATURE_DOWNLOAD, "sendFinishWithErrorBroadcastData with data: " + data + " and reason: " + reason);
        Intent finishIntent = new Intent();
        finishIntent.setAction(Medescope.ACTION_BROADCAST_FINISH_WITH_ERROR);
        finishIntent.setPackage(mContext.getPackageName());
        finishIntent.putExtra(DownloadConstants.EXTRA_STRING_DOWNLOAD_ID, downloadId);
        finishIntent.putExtra(DownloadConstants.EXTRA_INT_ERROR_REASON, reason);
        finishIntent.putExtra(DownloadConstants.EXTRA_STRING_JSON_DATA, data);
        mContext.sendBroadcast(finishIntent);
    }

    @Override
    public void sendDownloadStatusNotEnqueue(String downloadId) {
        Logger.debug(TAG, DownloadConstants.LOG_FEATURE_DOWNLOAD, "sendDownloadStatusNotEnqueue");
        Intent progressIntent = new Intent();
        progressIntent.setAction(Medescope.ACTION_BROADCAST_NOT_ENQUEUED);
        progressIntent.setPackage(mContext.getPackageName());
        progressIntent.putExtra(DownloadConstants.EXTRA_STRING_DOWNLOAD_ID, downloadId);
        mContext.sendBroadcast(progressIntent);
    }

    @Override
    public void sendDownloadStatusPaused(String downloadId, int reason) {
        Logger.debug(TAG, DownloadConstants.LOG_FEATURE_DOWNLOAD, "sendDownloadStatusPaused");
        Intent progressIntent = new Intent();
        progressIntent.setAction(Medescope.ACTION_BROADCAST_PAUSED);
        progressIntent.setPackage(mContext.getPackageName());
        progressIntent.putExtra(DownloadConstants.EXTRA_STRING_DOWNLOAD_ID, downloadId);
        progressIntent.putExtra(DownloadConstants.EXTRA_INT_REASON_KEY, reason);
        mContext.sendBroadcast(progressIntent);
    }

    @Override
    public void sendDownloadStatusProgress(String downloadId, int progress) {
        Logger.debug(TAG, DownloadConstants.LOG_FEATURE_DOWNLOAD, "sendDownloadStatusProgress");
        Intent progressIntent = new Intent();
        progressIntent.setAction(Medescope.ACTION_BROADCAST_IN_PROGRESS);
        progressIntent.setPackage(mContext.getPackageName());
        progressIntent.putExtra(DownloadConstants.EXTRA_STRING_DOWNLOAD_ID, downloadId);
        progressIntent.putExtra(DownloadConstants.EXTRA_INT_PROGRESS_PERCENTAGE, progress);
        mContext.sendBroadcast(progressIntent);
    }

    @Override
    public void sendCancelled(String downloadId) {
        Logger.debug(TAG, DownloadConstants.LOG_FEATURE_DOWNLOAD, "sendCancelled");
        Intent progressIntent = new Intent();
        progressIntent.setAction(Medescope.ACTION_BROADCAST_CANCELLED);
        progressIntent.setPackage(mContext.getPackageName());
        progressIntent.putExtra(DownloadConstants.EXTRA_STRING_DOWNLOAD_ID, downloadId);
        mContext.sendBroadcast(progressIntent);
    }

    @Override
    public void showDownloadQueue() {
        DownloadService downloadService = DMDownloadService.getInstance(mContext);

        if (downloadService.isDownloadManagerUiActivated() && downloadService.isDownloadManagerActivated()) {
            Intent i = new Intent();
            i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);
        } else {
            Log.w(TAG, "DownloadManager app disabled before trying to open the Download List screen. Abort!");
        }
    }
}
