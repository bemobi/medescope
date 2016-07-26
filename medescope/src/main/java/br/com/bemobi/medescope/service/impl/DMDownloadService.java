package br.com.bemobi.medescope.service.impl;

import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.util.Map;

import br.com.bemobi.medescope.DownloadFileUtils;
import br.com.bemobi.medescope.log.Logger;
import br.com.bemobi.medescope.model.DownloadInfo;
import br.com.bemobi.medescope.repository.DMRepository;
import br.com.bemobi.medescope.service.DownloadService;
import br.com.bemobi.medescope.wrapper.DownloadInfoWrapper;
import br.com.bemobi.medescope.wrapper.impl.DMDownloadInfoWrapper;

import static br.com.bemobi.medescope.constant.DownloadConstants.LOG_FEATURE_DOWNLOAD;

/**
 * Created by bkosawa on 30/06/15.
 */
public class DMDownloadService implements DownloadService {

    private static final String TAG = DMDownloadService.class.getSimpleName();

    private static final String PACKAGE_DOWNLOAD_MANAGER = "com.android.providers.downloads";
    private static final String PACKAGE_DOWNLOAD_MANAGER_UI = "com.android.providers.downloads.ui";

    private static DMDownloadService instance;
    private static DownloadManager downloadManager;
    private DMRepository repository;
    private Context mContext;
    private boolean isHidden = false;
    private DownloadInfoWrapper<Integer, Integer> downloadInfoWrapper;

    private DMDownloadService(Context context) {
        this.mContext = context;
        this.repository = DMRepository.getInstance(context);
        this.downloadInfoWrapper = new DMDownloadInfoWrapper();
    }

    public static DMDownloadService getInstance(Context context) {
        if(instance == null) {
            instance = new DMDownloadService(context);
        }
        return instance;
    }

    private static DownloadManager getDMInstance(Context context) {
        if (downloadManager == null) {
            downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        }
        return downloadManager;
    }

    @Override
    public void shutdown() {
        Log.d(TAG, "shutdown");
        downloadManager = null;
    }

    @Override
    public boolean enqueue(String downloadId, String uri, String fileName, String title, String description, String data, boolean shouldDownloadOnlyInWifi, Map<String, String> customHeaders) {
        if( isDownloadManagerDeactivated(mContext) ){
            return false;
        }
        if (DownloadFileUtils.isExternalStorageWritable()) {
            DownloadManager.Request request;
            request = new DownloadManager.Request(Uri.parse(uri));
            request.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS, fileName);
            request.setTitle(title);
            request.setDescription(description);
            request.setAllowedOverRoaming(false);
            setAllowedNetworks(shouldDownloadOnlyInWifi, request);

            if (customHeaders != null && !customHeaders.isEmpty()) {
                // TODO setar variavel no build para logar somente em debug http://toastdroid.com/2014/03/28/customizing-your-build-with-gradle/

                for (String key : customHeaders.keySet()) {
                    request.addRequestHeader(key, customHeaders.get(key));
                }
            }

            request = setHiddenDownload(request);
            Long dmId = getDMInstance(mContext).enqueue(request);
            repository.persistIds(downloadId, dmId);
            return dmId >= 0;
        }

        return false;
    }

    @Override
    public boolean isDownloadManagerUiActivated() {
        return !this.isDownloadManagerUiDeactivated();
    }

    @Override
    public boolean isDownloadManagerUiDeactivated(){
        try {
            int state = mContext.getPackageManager().getApplicationEnabledSetting(PACKAGE_DOWNLOAD_MANAGER_UI);
            return this.isDisabledState(state);
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    @Override
    public boolean isDownloadManagerActivated() {
        return !this.isDownloadManagerDeactivated(mContext);
    }

    @Override
    public boolean isDownloadManagerDeactivated(Context context) {
        try {
            int state = context.getPackageManager().getApplicationEnabledSetting(PACKAGE_DOWNLOAD_MANAGER);
            return this.isDisabledState(state);
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    private boolean isDisabledState(int state) {
        return state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED ||
                state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER ||
                state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED;
    }

    private void setAllowedNetworks(boolean shouldDownloadOnlyInWifi, DownloadManager.Request request) {
        if(shouldDownloadOnlyInWifi) {
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        } else {
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        }
    }

    private DownloadManager.Request setHiddenDownload(DownloadManager.Request request){
        if(isHidden) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            } else {
                request.setShowRunningNotification(false);
            }
        }
        return request;
    }

    @Override
    public boolean cancel(String downloadId) {
        return getDMInstance(mContext).remove(repository.getDMId(downloadId)) > 0;
    }

    @Override
    public void cleanupId(String downloadId) {
        repository.removeId(downloadId);
    }

    @Override
    public void notificationClicked(String[] downloadIds) {
        DownloadCommandService.actionNotificationClicked(mContext, downloadIds);
    }

    @Override
    public DownloadInfo getDownloadInfo(String downloadId) {
        DownloadInfo downloadManagerInfo = null;
        Long dmDownloadId = repository.getDMId(downloadId);
        if (dmDownloadId != null) {
            Cursor cursor = getDMInstance(mContext).query(new DownloadManager.Query().setFilterById(repository.getDMId(downloadId)));

            if (cursor != null) {
                int status;
                int reason;
                String filename;
                String localURI;
                long lastModified;
                long downloadedSoFar;
                long totalSize;
                int progress = 0;

                if (cursor.moveToFirst()) {

                    //column for status
                    int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    status = cursor.getInt(columnIndex);

                    //column for reason code if the download failed or paused
                    int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
                    reason = cursor.getInt(columnReason);

                    //get the download filename
                    filename = "";
                    int filenameIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                    String cursorString = cursor.getString(filenameIndex);

                    if (cursorString != null) {
                        Logger.debug(TAG, LOG_FEATURE_DOWNLOAD, "                     [ cursor.getString(filenameIndex) ] = " + cursor.getString(filenameIndex));
                        Uri uri = Uri.parse(cursorString);

                        if (uri != null) {
                            Logger.debug(TAG, LOG_FEATURE_DOWNLOAD, "[ Uri.parse(cursor.getString(filenameIndex)).getPath() ] = " + Uri.parse(cursor.getString(filenameIndex)).getPath());
                            filename = uri.getPath();
                        }
                    }

                    int columnLocalURI = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                    localURI = cursor.getString(columnLocalURI);

                    int columnLastModified = cursor.getColumnIndex(DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP);
                    lastModified = cursor.getLong(columnLastModified);


                    int columnDownloadedSoFar = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                    downloadedSoFar = cursor.getLong(columnDownloadedSoFar);

                    int columnTotalSize = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                    totalSize = cursor.getLong(columnTotalSize);

                    if (status == DownloadManager.STATUS_RUNNING) {
                        progress = (int) Math.round(((1.0 * downloadedSoFar) / totalSize) * 100);
                    } else if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        progress = 100;
                    }

                    downloadManagerInfo = new DownloadInfo(downloadInfoWrapper.translateStatus(status), downloadInfoWrapper.translateReason(reason), filename, localURI, lastModified, downloadedSoFar, totalSize, progress);
                }
                cursor.close();
            }
        }
        return downloadManagerInfo;
    }
}
