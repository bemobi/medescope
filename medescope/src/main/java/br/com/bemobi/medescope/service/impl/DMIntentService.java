package br.com.bemobi.medescope.service.impl;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import br.com.bemobi.medescope.model.DownloadInfo;
import br.com.bemobi.medescope.repository.DMRepository;
import br.com.bemobi.medescope.service.DownloadService;

/**
 * Created by bkosawa on 01/07/15.
 */
public class DMIntentService extends IntentService {

    private static final String ACTION_DM_FINISH = "br.com.bemobi.medescope.ACTION_DM_FINISH";
    private static final String ACTION_DM_NOTIFICATION_CLICKED = "br.com.bemobi.medescope.ACTION_DM_NOTIFICATION_CLICKED";
    private static final String TAG = "DMIntentService";

    public DMIntentService() {
        super(DMIntentService.class.getName());
    }

    public static void actionFinish(Context context, long downloadId) {
        Intent intent = new Intent(context, DMIntentService.class);
        intent.setAction(ACTION_DM_FINISH);
        intent.putExtra(DownloadManager.EXTRA_DOWNLOAD_ID, downloadId);
        context.startService(intent);
    }

    public static void actionNotificationClicked(Context context, long[] downloadIds) {
        Intent serviceIntent = new Intent(context, DMIntentService.class);
        serviceIntent.setAction(ACTION_DM_NOTIFICATION_CLICKED);
        if (downloadIds != null) {
            serviceIntent.putExtra(DownloadManager.EXTRA_DOWNLOAD_ID, downloadIds);
        }
        context.startService(serviceIntent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            DownloadService downloadService = DMDownloadService.getInstance(this);
            String action = intent.getAction();
            if (ACTION_DM_FINISH.equals(action) ) {
                Long dmDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if(dmDownloadId >= 0) {
                    String libId = DMRepository.getInstance(getApplicationContext()).getClientId(dmDownloadId);

                    DownloadInfo downloadInfo = downloadService.getDownloadInfo(libId);

                    DownloadCommandService.actionFinishDownload(this, libId, downloadInfo);
                }
            } else if (ACTION_DM_NOTIFICATION_CLICKED.equals(action)) {
                String[] downloadIds = {};
                long[] ids = {};
                if(intent.hasExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS)) {
                    ids = intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
                }

                if(ids != null && ids.length > 0) {
                    for (int i = 0; i < ids.length; i++) {
                        downloadIds[i] = DMRepository.getInstance(getApplicationContext()).getClientId(ids[i]);
                    }
                }

                if (downloadService.isDownloadManagerUiActivated() && downloadService.isDownloadManagerActivated()) {
                    downloadService.notificationClicked(downloadIds);
                } else {
                    Log.w(TAG, "DownloadManager app disabled before trying to open the Download List screen! Aborting...");
                }
            }
        }
    }
}
