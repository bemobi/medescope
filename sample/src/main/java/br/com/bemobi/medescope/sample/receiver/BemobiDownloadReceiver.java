package br.com.bemobi.medescope.sample.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import br.com.bemobi.medescope.Medescope;
import br.com.bemobi.medescope.constant.DownloadConstants;
import br.com.bemobi.medescope.receiver.BroadcastReceiverLogger;
import br.com.bemobi.medescope.sample.R;
import br.com.bemobi.medescope.sample.model.NotificationData;
import br.com.goncalves.pugnotification.notification.PugNotification;

import static br.com.bemobi.medescope.constant.DownloadConstants.LOG_FEATURE_DOWNLOAD;

/**
 * Created by bkosawa on 02/07/15.
 */
public class BemobiDownloadReceiver extends BroadcastReceiver {

    private static final String TAG = BemobiDownloadReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        new BroadcastReceiverLogger(TAG, LOG_FEATURE_DOWNLOAD).onReceive(context, intent);

        if (intent != null) {
            String action = intent.getAction();
            if (Medescope.ACTION_BROADCAST_FINISH_WITH_SUCCESS.equals(action)) {
                String downloadId = intent.getStringExtra(DownloadConstants.EXTRA_STRING_DOWNLOAD_ID);
                String filePath = intent.getStringExtra(DownloadConstants.EXTRA_STRING_FILE_PATH);

                if (!TextUtils.isEmpty(downloadId)) {
                    String data = intent.getStringExtra(DownloadConstants.EXTRA_STRING_JSON_DATA);
                    NotificationData nData = new Gson().fromJson(data, NotificationData.class);

                    PugNotification.with(context).load()
                            .title(nData.getTitle())
                            .message(nData.getDesc())
                            .bigTextStyle(nData.getDesc())
                            .smallIcon(R.mipmap.ic_launcher)
                            .largeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.bemobi_download_ic_notif_done))
                            .autoCancel(true)
                            .simple()
                            .build();

                    Log.d(TAG, "File Path: " + filePath);
                }
            } else if (Medescope.ACTION_BROADCAST_FINISH_WITH_ERROR.equals(action)) {
                String downloadId = intent.getStringExtra(DownloadConstants.EXTRA_STRING_DOWNLOAD_ID);
                int errorMsg = intent.getIntExtra(DownloadConstants.EXTRA_INT_ERROR_REASON, -1);

                if (!TextUtils.isEmpty(downloadId)) {
                    String data = intent.getStringExtra(DownloadConstants.EXTRA_STRING_JSON_DATA);
                    NotificationData nData = new Gson().fromJson(data, NotificationData.class);

                    PugNotification.with(context).load()
                            .title(nData.getTitle())
                            .message(errorMsg + " - " + nData.getDesc())
                            .bigTextStyle(nData.getDesc())
                            .smallIcon(R.mipmap.ic_launcher)
                            .largeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.bemobi_download_ic_notif_error))
                            .autoCancel(true)
                            .simple()
                            .build();
                }
            }
        }
    }
}
