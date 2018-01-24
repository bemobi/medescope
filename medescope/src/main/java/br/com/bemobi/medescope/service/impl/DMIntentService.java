package br.com.bemobi.medescope.service.impl;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import br.com.bemobi.medescope.model.DownloadInfo;
import br.com.bemobi.medescope.repository.DMRepository;
import br.com.bemobi.medescope.service.DownloadService;

/**
 * Created by bkosawa on 01/07/15.
 */
public class DMIntentService implements Runnable {

    private static final String ACTION_DM_FINISH = "br.com.bemobi.medescope.ACTION_DM_FINISH";
    private static final String ACTION_DM_NOTIFICATION_CLICKED = "br.com.bemobi.medescope.ACTION_DM_NOTIFICATION_CLICKED";
    private static final String TAG = "DMIntentService";

    private Intent sIntent;
    private Context mContext;

    public DMIntentService(Intent sIntent, Context mContext) {
        this.sIntent = sIntent;
        this.mContext = mContext;
    }

    public static void actionFinish(Context context, long downloadId) {
        Intent intent = new Intent(context, DMIntentService.class);
        intent.setAction(ACTION_DM_FINISH);
        intent.putExtra(DownloadManager.EXTRA_DOWNLOAD_ID, downloadId);
        new Thread(new DMIntentService(intent, context)).start();
    }

    public static void actionNotificationClicked(Context context, long[] downloadIds) {
        Intent serviceIntent = new Intent(context, DMIntentService.class);
        serviceIntent.setAction(ACTION_DM_NOTIFICATION_CLICKED);
        if (downloadIds != null) {
            serviceIntent.putExtra(DownloadManager.EXTRA_DOWNLOAD_ID, downloadIds);
        }
        new Thread(new DMIntentService(serviceIntent, context)).start();
    }

    @Override
    public void run() {
        if (sIntent != null) {
            DownloadService downloadService = DMDownloadService.getInstance(mContext);
            String action = sIntent.getAction();
            if (ACTION_DM_FINISH.equals(action) ) {
                Long dmDownloadId = sIntent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if(dmDownloadId >= 0) {
                    String libId = DMRepository.getInstance(mContext).getClientId(dmDownloadId);

                    DownloadInfo downloadInfo = downloadService.getDownloadInfo(libId);

                    DownloadCommandService.actionFinishDownload(mContext, libId, downloadInfo);
                }
            } else if (ACTION_DM_NOTIFICATION_CLICKED.equals(action)) {
                String[] downloadIds = {};
                long[] ids = {};
                if(sIntent.hasExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS)) {
                    ids = sIntent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
                }

                if(ids != null && ids.length > 0) {
                    for (int i = 0; i < ids.length; i++) {
                        downloadIds[i] = DMRepository.getInstance(mContext).getClientId(ids[i]);
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
