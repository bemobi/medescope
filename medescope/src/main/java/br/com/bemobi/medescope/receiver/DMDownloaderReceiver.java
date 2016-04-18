package br.com.bemobi.medescope.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import br.com.bemobi.medescope.service.DownloadService;
import br.com.bemobi.medescope.service.impl.DMDownloadService;
import br.com.bemobi.medescope.service.impl.DMIntentService;

import static br.com.bemobi.medescope.constant.DownloadConstants.LOG_FEATURE_DOWNLOAD;

/**
 * Created by bkosawa on 26/06/15.
 */
public class DMDownloaderReceiver extends BroadcastReceiver {
    private static final String TAG = DMDownloaderReceiver.class.getSimpleName();

    public DMDownloaderReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        new BroadcastReceiverLogger(TAG, LOG_FEATURE_DOWNLOAD).onReceive(context, intent);

        String action = intent.getAction();

        if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
            Log.d(TAG, "DM DOWNLOAD COMPLETED");
            Long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            DMIntentService.actionFinish(context, downloadId);
        }
        else if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(action)) {
            Log.d(TAG, "DM NOTIFICATION CLICKED");

            long[] downloadIds = null;

            if (intent.hasExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS)){
                downloadIds = intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
            }

            DownloadService downloadService = DMDownloadService.getInstance(context);

            if (downloadService.isDownloadManagerUiActivated() && downloadService.isDownloadManagerActivated()) {
                DMIntentService.actionNotificationClicked(context, downloadIds);
            } else {
                Log.w(TAG, String.format("DownloadManager app disabled when receive %s event! Aborting...", action));
            }
        }
    }
}
