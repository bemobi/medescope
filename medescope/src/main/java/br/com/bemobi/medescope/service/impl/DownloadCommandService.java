package br.com.bemobi.medescope.service.impl;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import br.com.bemobi.medescope.constant.DownloadConstants;
import br.com.bemobi.medescope.constant.DownloadInfoReasonConstants;
import br.com.bemobi.medescope.constant.Extras;
import br.com.bemobi.medescope.log.IntentLogger;
import br.com.bemobi.medescope.log.Logger;
import br.com.bemobi.medescope.model.DownloadRequest;
import br.com.bemobi.medescope.model.DownloadInfo;
import br.com.bemobi.medescope.repository.DownloadDataRepository;
import br.com.bemobi.medescope.repository.impl.MapDownloadDataRepository;
import br.com.bemobi.medescope.service.CommunicationService;
import br.com.bemobi.medescope.service.DownloadCommand;
import br.com.bemobi.medescope.service.DownloadService;


import static br.com.bemobi.medescope.constant.DownloadConstants.LOG_FEATURE_DOWNLOAD;
import static br.com.bemobi.medescope.constant.DownloadConstants.LOG_FEATURE_SERVICE_LIFECYCLE;

/**
 * Created by bkosawa on 26/06/15.
 */
public class DownloadCommandService extends Service implements DownloadCommand {

    private static final String TAG = DownloadCommandService.class.getSimpleName();

    private static final String ACTION_ENQUEUE = "br.com.bemobi.medescope.ACTION_ENQUEUE";
    private static final String ACTION_CANCEL = "br.com.bemobi.medescope.ACTION_CANCEL";
    private static final String ACTION_FINISH = "br.com.bemobi.medescope.ACTION_FINISH";
    private static final String ACTION_NOTIFICATION_CLICK = "br.com.bemobi.medescope.ACTION_NOTIFICATION_CLICK";
    private static final String ACTION_REGISTER_FOR_STATUS = "br.com.bemobi.medescope.ACTION_REGISTER_FOR_STATUS";
    private static final String ACTION_UNREGISTER_FOR_STATUS = "br.com.bemobi.medescope.ACTION_UNREGISTER_FOR_STATUS";

    private DownloadService downloadService;

    private DownloadDataRepository downloadDataRepository;

    private CommunicationService communicationService;

    private Looper mServiceLooper;

    private ServiceHandler mServiceHandler;

    private String downloadIdRegisteredToSendProgress = "";

    private boolean isStartedSendProgress = false;

    public static void actionEnqueue(Context context, DownloadRequest downloadRequest) {
        Intent serviceIntent = new Intent(context, DownloadCommandService.class);
        serviceIntent.setAction(ACTION_ENQUEUE);
        serviceIntent.putExtra(Extras.EXTRA_DOWNLOAD, downloadRequest);
        context.startService(serviceIntent);
    }

    public static void actionSubscribeStatusUpdate(Context context, String id) {
        Intent serviceIntent = new Intent(context, DownloadCommandService.class);
        serviceIntent.setAction(ACTION_REGISTER_FOR_STATUS);
        serviceIntent.putExtra(DownloadConstants.EXTRA_STRING_DOWNLOAD_ID, id);
        context.startService(serviceIntent);
    }

    public static void actionUnsubscribeStatusUpdate(Context context) {
        Intent serviceIntent = new Intent(context, DownloadCommandService.class);
        serviceIntent.setAction(ACTION_UNREGISTER_FOR_STATUS);
        context.startService(serviceIntent);
    }

    public static void actionCancel(Context context, String id) {
        Intent serviceIntent = new Intent(context, DownloadCommandService.class);
        serviceIntent.setAction(ACTION_CANCEL);
        serviceIntent.putExtra(DownloadConstants.EXTRA_STRING_DOWNLOAD_ID, id);
        context.startService(serviceIntent);
    }

    public static void actionFinishDownload(Context context, String downloadId, DownloadInfo downloadInfo) {
        Intent serviceIntent = new Intent(context, DownloadCommandService.class);
        serviceIntent.setAction(ACTION_FINISH);
        serviceIntent.putExtra(DownloadConstants.EXTRA_STRING_DOWNLOAD_ID, downloadId);
        serviceIntent.putExtra(DownloadConstants.EXTRA_DOWNLOAD_INFO, downloadInfo);
        context.startService(serviceIntent);
    }

    public static void actionNotificationClicked(Context context, String[] downloadIds) {
        Intent serviceIntent = new Intent(context, DownloadCommandService.class);
        serviceIntent.setAction(ACTION_NOTIFICATION_CLICK);
        serviceIntent.putExtra(DownloadConstants.EXTRA_ARRAY_STRING_DOWNLOAD_IDS, downloadIds);
        context.startService(serviceIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Logger.debug(TAG, LOG_FEATURE_SERVICE_LIFECYCLE, "onBind()");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.debug(TAG, LOG_FEATURE_SERVICE_LIFECYCLE, "onCreate()");
        initThreadHandler();
        startCommand();
    }

    private void initThreadHandler() {
        HandlerThread thread = new HandlerThread("PROGRESS_SENDER", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public void startCommand() {
        Logger.debug(TAG, LOG_FEATURE_SERVICE_LIFECYCLE, "startCommand()");
        this.downloadDataRepository = MapDownloadDataRepository.getInstance(getApplicationContext());
        this.downloadService = DMDownloadService.getInstance(getApplicationContext());
        this.communicationService = BroadcastCommunicationService.getInstance(getApplicationContext());
        this.isStartedSendProgress = false;
        this.downloadIdRegisteredToSendProgress = downloadDataRepository.recoverSubscribedId();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.debug(TAG, LOG_FEATURE_SERVICE_LIFECYCLE, "onStartCommand()");
        if (intent != null) {
            executeCommand(intent.getAction(), intent.getExtras());
        } else {
            startCommand();
            if( !TextUtils.isEmpty(this.downloadIdRegisteredToSendProgress )) {
                startProgressSender();
            }
        }
        return START_STICKY;
    }

    @Override
    public void executeCommand(String action, Bundle extras) {
        Logger.debug(TAG, LOG_FEATURE_SERVICE_LIFECYCLE, String.format(">>>>>>>>>>>>>>>>>>>>>>>>>> ACTION RECEIVED: %s", action));
        new IntentLogger(TAG, LOG_FEATURE_DOWNLOAD).logBundle(extras);

        if (ACTION_ENQUEUE.equals(action)) {
            //TODO transformar os parametros em um objeto Parcelable
            DownloadRequest downloadRequest = (DownloadRequest) extras.getSerializable(Extras.EXTRA_DOWNLOAD);
            this.enqueue(downloadRequest);
        } else if (ACTION_CANCEL.equals(action)) {
            cancelAction(getIdStringFromBundle(extras));
        } else if (ACTION_FINISH.equals(action)) {
            finishAction(getDownloadIdFromBundle(extras), getDownloadInfoFromBundle(extras));
        } else if (ACTION_NOTIFICATION_CLICK.equals(action)) {
            clickNotificationAction(getDownloadIdsFromBundle(extras));
        } else if (ACTION_REGISTER_FOR_STATUS.equals(action)) {
            registerForStatus(getIdStringFromBundle(extras));
        } else if (ACTION_UNREGISTER_FOR_STATUS.equals(action)) {
            unregisterForStatus();
        }

        if (downloadDataRepository.isEmptyDownloadData() && !isStartedSendProgress) {
            stopSelf();
            shutdownCommand();
        }
    }

    @Override
    public void onDestroy() {
        Logger.debug(TAG, LOG_FEATURE_SERVICE_LIFECYCLE, "onDestroy()");
        shutdownCommand();
        super.onDestroy();
    }

    @Override
    public void shutdownCommand() {
        downloadService.shutdown();
    }

    @Override
    public void enqueue(DownloadRequest downloadRequest) {
        if (downloadRequest == null || !downloadRequest.isValid()) {
            Logger.error(TAG, DownloadConstants.LOG_FEATURE_DOWNLOAD, String.format("Invalid download object param: %s", downloadRequest.toString()));
            return;
        }

        if (downloadDataRepository.containsDownloadDataKey(downloadRequest.getId())) {
            DownloadInfo downloadInfo = downloadService.getDownloadInfo(downloadRequest.getId());

            if (downloadInfo == null) {
                this.enqueue(downloadService, downloadRequest);
                return;
            }

            if (downloadInfo.hasFinished()) {
                downloadService.cleanupId(downloadRequest.getId());
                this.enqueue(downloadService, downloadRequest);
            } else {
                Logger.error(TAG, DownloadConstants.LOG_FEATURE_DOWNLOAD, String.format("This download is in execution with status: %s", downloadInfo.getStatus()));
                Logger.error(TAG, DownloadConstants.LOG_FEATURE_DOWNLOAD, "Do you would like to subscribe to receive status update? Medescope.subscribeStatus([downloadId])");
            }

        } else {
            this.enqueue(downloadService, downloadRequest);
        }
    }

    private void enqueue(DownloadService downloadService, DownloadRequest downloadRequest){
        downloadService.cleanupId(downloadRequest.getId());
        downloadDataRepository.removeDownloadData(downloadRequest.getId());
        downloadDataRepository.putDownloadData(downloadRequest.getId(), downloadRequest.getClientPayload());

        if(!hasPermission()){
            communicationService
                    .sendFinishWithErrorBroadcastData(
                            downloadRequest.getId(),
                            DownloadInfoReasonConstants.ERROR_PERMISSION_NOT_GRANTED,
                            downloadDataRepository.getDownloadData(downloadRequest.getId()));
            return;
        }

        boolean enqueued = downloadService.enqueue(
                        downloadRequest.getId(),
                        downloadRequest.getUri(),
                        downloadRequest.getFileName(),
                        downloadRequest.getDownloadName(),
                        downloadRequest.getDownloadDescription(),
                        downloadRequest.getClientPayload(),
                        downloadRequest.shouldDownloadOnlyInWifi(),
                        downloadRequest.getCustomHeaders()
        );
        if (shouldStartSendProgressOnEnqueue(downloadRequest.getId())) {
            startProgressSender();
        }

        if (!enqueued) {
            communicationService
                .sendFinishWithErrorBroadcastData(
                        downloadRequest.getId(),
                        DownloadInfoReasonConstants.ERROR_GENERIC,
                        downloadDataRepository.getDownloadData(downloadRequest.getId()));
        }
    }

    private boolean hasPermission(){
        int permission = ActivityCompat
                .checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permission== PackageManager.PERMISSION_GRANTED){
            return true;
        }

        return false;
    }

    private  boolean shouldStartSendProgressOnEnqueue(String enqueueDownloadId){
        return enqueueDownloadId.equals(downloadIdRegisteredToSendProgress) && !isStartedSendProgress;
    }

    @Override
    public void cancelAction(String downloadId) {
        if (!TextUtils.isEmpty(downloadId) && downloadDataRepository.containsDownloadDataKey(downloadId)) {
            downloadService.cancel(downloadId);
        }
    }

    @Override
    public void finishAction(String downloadId, DownloadInfo downloadInfo) {
        Logger.debug(TAG, DownloadConstants.LOG_FEATURE_DOWNLOAD, "Finish Action");
        String downloadData = downloadDataRepository.getDownloadData(downloadId);

        downloadDataRepository.removeDownloadData(downloadId);
        downloadService.cleanupId(downloadId);

        if (downloadInfo != null) {
            if (downloadInfo.hasFinishedWithSuccess()) {
                communicationService.sendFinishWithSuccessBroadcastData(downloadId, downloadInfo.getFilename(), downloadData);
            } else if (downloadInfo.hasFinishedWithError()) {
                communicationService.sendFinishWithErrorBroadcastData(downloadId, downloadInfo.getReason(), downloadData);
            }
        } else {
            Logger.debug(TAG, DownloadConstants.LOG_FEATURE_DOWNLOAD, "Cancelled");
            communicationService.sendCancelled(downloadId);
        }
    }

    @Override
    public void clickNotificationAction(String[] downloadIds) {
        communicationService.showDownloadQueue();
    }

    private void registerForStatus(String downloadId) {
        downloadIdRegisteredToSendProgress = downloadId;
        downloadDataRepository.persistSubscribedId(downloadId);
        Logger.debug(TAG, DownloadConstants.LOG_FEATURE_SERVICE_LIFECYCLE, "isStartedSendProgress:" + isStartedSendProgress);

        if (!isStartedSendProgress) {
            Logger.debug(TAG, DownloadConstants.LOG_FEATURE_SERVICE_LIFECYCLE, "Starting Sender Progress!");
            startProgressSender();
        }
    }

    private void unregisterForStatus() {
        downloadIdRegisteredToSendProgress = "";
        downloadDataRepository.removeSubscribedId();
    }

    //TODO this method must be atomic(?)
    public void startProgressSender() {
        Logger.debug(TAG, LOG_FEATURE_SERVICE_LIFECYCLE, "startProgressSender()");

        isStartedSendProgress = true;
        Message msg = mServiceHandler.obtainMessage();
        mServiceHandler.sendMessage(msg);
    }

    private final class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper) {
            super(looper);
            Logger.debug(TAG, LOG_FEATURE_SERVICE_LIFECYCLE, "ServiceHandler()");
        }

        @Override
        public void handleMessage(Message msg) {
            Logger.debug(TAG, LOG_FEATURE_SERVICE_LIFECYCLE, "ServiceHandler.handleMessage()");
            DownloadInfo lastSentDownloadInfo = null;

            if (!downloadDataRepository.containsDownloadDataKey(downloadIdRegisteredToSendProgress)) {
                communicationService.sendDownloadStatusNotEnqueue(downloadIdRegisteredToSendProgress);
                isStartedSendProgress = false;
            }

            DownloadInfo downloadInfo = downloadService.getDownloadInfo(downloadIdRegisteredToSendProgress);
            if (downloadInfo == null) {
                communicationService.sendDownloadStatusNotEnqueue(downloadIdRegisteredToSendProgress);
                isStartedSendProgress = false;
            }

            while (!TextUtils.isEmpty(downloadIdRegisteredToSendProgress) && isStartedSendProgress) {
                try {

                    if (downloadInfo == null
                            || !downloadDataRepository.containsDownloadDataKey(downloadIdRegisteredToSendProgress)) {
                        isStartedSendProgress = false;
                        break;
                    }

                    if (downloadInfo.equals(lastSentDownloadInfo)) {
                        Logger.debug(TAG, DownloadConstants.LOG_FEATURE_DOWNLOAD_SEND_PROGRESS, "I have already sent this info");
                    } else if (downloadInfo.hasFinished()) {
                        // TODO em alguns casos o download está com 100% e não está sendo enviado o evento de FINISHED. Tratar esse caso posteriormente
                        communicationService.sendDownloadStatusProgress(downloadIdRegisteredToSendProgress, downloadInfo.getProgress());
                        isStartedSendProgress = false;
                        break;
                    }else if (downloadInfo.isPaused()) {
                        communicationService.sendDownloadStatusPaused(downloadIdRegisteredToSendProgress, downloadInfo.getReason());
                    } else if (downloadInfo.isInProgress()) {
                        communicationService.sendDownloadStatusProgress(downloadIdRegisteredToSendProgress, downloadInfo.getProgress());
                    } else {
                        Logger.debug(TAG, DownloadConstants.LOG_FEATURE_DOWNLOAD_SEND_PROGRESS, "BIZARRE STATUS!! PANIC ALERT!!!");
                    }

                    lastSentDownloadInfo = downloadInfo;

                    Thread.sleep(300);

                    downloadInfo = downloadService.getDownloadInfo(downloadIdRegisteredToSendProgress);

                } catch (InterruptedException exception) {
                    Logger.error(TAG, DownloadConstants.LOG_FEATURE_DOWNLOAD_SEND_PROGRESS, "ERROR ON THREAD STATUS SENDER!!!!");
                    isStartedSendProgress = false;
                }
            }

            isStartedSendProgress = false;

            Logger.debug(TAG, DownloadConstants.LOG_FEATURE_DOWNLOAD_SEND_PROGRESS, "Finishing Status Sender!!!!!");
        }
    }

    private String getIdStringFromBundle(Bundle extras) {
        return extras.getString(DownloadConstants.EXTRA_STRING_DOWNLOAD_ID);
    }

    private String getDownloadIdFromBundle(Bundle extras) {
        return extras.getString(DownloadConstants.EXTRA_STRING_DOWNLOAD_ID);
    }

    private String[] getDownloadIdsFromBundle(Bundle extras) {
        return extras.getStringArray(DownloadConstants.EXTRA_ARRAY_STRING_DOWNLOAD_IDS);
    }

    private DownloadInfo getDownloadInfoFromBundle(Bundle extras) {
        return extras.getParcelable(DownloadConstants.EXTRA_DOWNLOAD_INFO);
    }
}
