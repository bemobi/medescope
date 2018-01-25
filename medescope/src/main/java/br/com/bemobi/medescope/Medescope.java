package br.com.bemobi.medescope;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.util.Map;

import br.com.bemobi.medescope.callback.DownloadStatusCallback;
import br.com.bemobi.medescope.constant.DownloadConstants;
import br.com.bemobi.medescope.exception.DirectoryNotMountedException;
import br.com.bemobi.medescope.exception.PathNotFoundException;
import br.com.bemobi.medescope.log.Logger;
import br.com.bemobi.medescope.model.DownloadRequest;
import br.com.bemobi.medescope.service.impl.DownloadCommandService;

/**
 * Created by bkosawa on 26/06/15.
 */
public class Medescope {


    private final String TAG = Medescope.class.getSimpleName();

    public static final String ACTION_BROADCAST_IN_PROGRESS = "br.com.bemobi.medescope.ACTION_BROADCAST_IN_PROGRESS";
    public static final String ACTION_BROADCAST_PAUSED = "br.com.bemobi.medescope.ACTION_BROADCAST_PAUSED";
    public static final String ACTION_BROADCAST_NOT_ENQUEUED = "br.com.bemobi.medescope.ACTION_BROADCAST_NOT_ENQUEUED";
    public static final String ACTION_BROADCAST_CANCELLED = "br.com.bemobi.medescope.ACTION_BROADCAST_CANCELLED";
    public static final String ACTION_BROADCAST_FINISH_WITH_SUCCESS = "br.com.bemobi.medescope.ACTION_BROADCAST_FINISH_WITH_SUCCESS";
    public static final String ACTION_BROADCAST_FINISH_WITH_ERROR = "br.com.bemobi.medescope.ACTION_BROADCAST_FINISH_WITH_ERROR";

    private Context mContext;

    private static Medescope instance;

    private static StatusBroadcastReceiver mReceiver;

    private String applicationName;

    private Medescope(Context context) {
        this.mContext = context.getApplicationContext();
        Logger.setContext(context.getApplicationContext());
        this.applicationName = "";
    }

    public static Medescope getInstance(Context context) {
        if( instance == null) {
            instance = new Medescope(context);
            mReceiver = new StatusBroadcastReceiver();
        }
        return instance;
    }

    public void setApplicationName(String applicationName){
        this.applicationName = applicationName;
    }

    public void enqueue(String id, String uri, String fileName, String downloadName, String developerPayload) {
        DownloadRequest downloadRequest = new DownloadRequest();
        downloadRequest.setId(id);
        downloadRequest.setUri(uri);
        downloadRequest.setFileName(fileName);
        downloadRequest.setDownloadName(downloadName);
        downloadRequest.setDownloadDescription(this.applicationName);
        downloadRequest.setClientPayload(developerPayload);
        downloadRequest.setShouldDownloadOnlyInWifi(false);
        enqueue(downloadRequest);
    }

    public void enqueue(String id, String uri, String fileName, String downloadName, String developerPayload, boolean shouldDownloadOnlyInWifi) {
        DownloadRequest downloadRequest = new DownloadRequest();
        downloadRequest.setId(id);
        downloadRequest.setUri(uri);
        downloadRequest.setFileName(fileName);
        downloadRequest.setDownloadName(downloadName);
        downloadRequest.setDownloadDescription(this.applicationName);
        downloadRequest.setClientPayload(developerPayload);
        downloadRequest.setShouldDownloadOnlyInWifi(shouldDownloadOnlyInWifi);
        enqueue(downloadRequest);
    }

    public void enqueue(String id, String uri, String fileName, String downloadName, String developerPayload, boolean shouldDownloadOnlyInWifi, Map<String, String> customHeaders) {
        DownloadRequest downloadRequest = new DownloadRequest();
        downloadRequest.setId(id);
        downloadRequest.setUri(uri);
        downloadRequest.setFileName(fileName);
        downloadRequest.setDownloadName(downloadName);
        downloadRequest.setDownloadDescription(this.applicationName);
        downloadRequest.setClientPayload(developerPayload);
        if(customHeaders != null && !customHeaders.isEmpty()) {
            downloadRequest.setCustomHeaders(customHeaders);
        } else {
            Logger.error(TAG, "YOU, NASTY DEVELOPER, ARE PASSING A NULL OR EMPTY HEADER MAP");
        }
        downloadRequest.setShouldDownloadOnlyInWifi(shouldDownloadOnlyInWifi);
        enqueue(downloadRequest);
    }

    public void enqueue(DownloadRequest request) {
        if(request.isValid()){
            DownloadCommandService.actionEnqueue(mContext, request);
            return;
        }
        Logger.error(TAG, "This is not a valid Request!");
        Logger.error(TAG, "Please fill it up with the basic data");
    }

    public void cancel(String id){
        DownloadCommandService.actionCancel(mContext, id);
    }

    public void updateSubscriptionStatusId(Context context, String id){
        if(!mReceiver.isCallbackSet()){
            Logger.error(TAG, "YOU HAVE NOT SET A CALLBACK YET!!");
            Logger.error(TAG, "YOU SHOULD IMPLEMENT A BROADCAST RECEIVER BY YOURSELF");
        }
        DownloadCommandService.actionSubscribeStatusUpdate(context, id);
    }

    public void subscribeStatus(Activity activity, String id, DownloadStatusCallback callback){
        mReceiver.setCallback(callback);
        activity.registerReceiver(mReceiver, getStatusBroadcastFilter());
        DownloadCommandService.actionSubscribeStatusUpdate(mContext, id);
    }

    public void unsubscribeStatus(Activity activity){
        activity.unregisterReceiver(mReceiver);
        DownloadCommandService.actionUnsubscribeStatusUpdate(mContext);
    }

    public String getDownloadDirectoryToRead(String subPath) throws DirectoryNotMountedException, PathNotFoundException {
        if( !DownloadFileUtils.isExternalStorageReadable() ) {
            throw new DirectoryNotMountedException("Directory is not mounted to read!");
        }
        return getDownloadDirectory(subPath);
    }

    public String getDownloadDirectoryToWrite(String subPath) throws DirectoryNotMountedException, PathNotFoundException {
        if( !DownloadFileUtils.isExternalStorageWritable() ) {
            throw new DirectoryNotMountedException("Directory is not mounted to write!");
        }
        return getDownloadDirectory(subPath);
    }

    private String getDownloadDirectory(String subPath) throws PathNotFoundException {
        File file = mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if(file == null) {
            throw new PathNotFoundException("External Path DIRECTORY_DOWNLOADS was not found!!");
        }
        if(TextUtils.isEmpty(subPath)){
            return file.getAbsolutePath();
        }
        return file.getAbsolutePath() + (subPath.startsWith("/") ? subPath : "/" + subPath);
    }

    private IntentFilter getStatusBroadcastFilter(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(Medescope.ACTION_BROADCAST_NOT_ENQUEUED);
        filter.addAction(Medescope.ACTION_BROADCAST_PAUSED);
        filter.addAction(Medescope.ACTION_BROADCAST_IN_PROGRESS);
        filter.addAction(Medescope.ACTION_BROADCAST_CANCELLED);
        filter.addAction(Medescope.ACTION_BROADCAST_FINISH_WITH_ERROR);
        filter.addAction(Medescope.ACTION_BROADCAST_FINISH_WITH_SUCCESS);
        return filter;
    }


    private static class StatusBroadcastReceiver extends BroadcastReceiver {

        private final String TAG = StatusBroadcastReceiver.class.getName();

        private DownloadStatusCallback mCallback;

        public StatusBroadcastReceiver() {
        }

        public void setCallback(DownloadStatusCallback callback){
            this.mCallback = callback;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if(mCallback != null) {
                String downloadId = intent.getStringExtra(DownloadConstants.EXTRA_STRING_DOWNLOAD_ID);
                String action = intent.getAction();
                if (Medescope.ACTION_BROADCAST_IN_PROGRESS.equals(action)) {
                    int progress = intent.getIntExtra(DownloadConstants.EXTRA_INT_PROGRESS_PERCENTAGE, 0);
                    mCallback.onDownloadInProgress(downloadId, progress);
                } else if (Medescope.ACTION_BROADCAST_PAUSED.equals(action)) {
                    int reason = intent.getIntExtra(DownloadConstants.EXTRA_INT_REASON_KEY, 0);
                    mCallback.onDownloadPaused(downloadId, reason);
                } else if (Medescope.ACTION_BROADCAST_NOT_ENQUEUED.equals(action)) {
                    mCallback.onDownloadNotEnqueued(downloadId);
                } else if (Medescope.ACTION_BROADCAST_CANCELLED.equals(action)){
                    mCallback.onDownloadCancelled(downloadId);
                } else if (Medescope.ACTION_BROADCAST_FINISH_WITH_ERROR.equals(action)) {
                    int reason = intent.getIntExtra(DownloadConstants.EXTRA_INT_REASON_KEY, 0);
                    String data = intent.getStringExtra(DownloadConstants.EXTRA_STRING_JSON_DATA);
                    mCallback.onDownloadOnFinishedWithError(downloadId, reason, data);
                } else if (Medescope.ACTION_BROADCAST_FINISH_WITH_SUCCESS.equals(action)) {
                    String filePath = intent.getStringExtra(DownloadConstants.EXTRA_STRING_FILE_PATH);
                    String data = intent.getStringExtra(DownloadConstants.EXTRA_STRING_JSON_DATA);
                    mCallback.onDownloadOnFinishedWithSuccess(downloadId, filePath, data);
                }
            } else {
                Logger.error(TAG, "You forgot to set a callback!!");
            }
        }

        public boolean isCallbackSet() {
            return mCallback != null;
        }
    }
}
