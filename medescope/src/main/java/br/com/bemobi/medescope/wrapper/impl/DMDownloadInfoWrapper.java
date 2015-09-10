package br.com.bemobi.medescope.wrapper.impl;

import android.app.DownloadManager;

import br.com.bemobi.medescope.constant.DownloadInfoReasonConstants;
import br.com.bemobi.medescope.wrapper.DownloadInfoWrapper;

/**
 * Created by raphael on 7/7/15.
 */
public class DMDownloadInfoWrapper implements DownloadInfoWrapper<Integer, Integer> {

    @Override
    public int translateStatus(Integer status) {
        return status;
    }

    @Override
    public int translateReason(Integer reason) {
        int translatedReason;

        switch(reason){
            case DownloadManager.ERROR_CANNOT_RESUME:
                translatedReason = DownloadInfoReasonConstants.ERROR_GENERIC;
                break;
            case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                translatedReason = DownloadInfoReasonConstants.ERROR_STORAGE_NOT_FOUND;
                break;
            case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                translatedReason = DownloadInfoReasonConstants.ERROR_ALREADY_EXISTING_FILE;
                break;
            case DownloadManager.ERROR_FILE_ERROR:
                translatedReason = DownloadInfoReasonConstants.ERROR_FILE_GENERIC_PROBLEM;
                break;
            case DownloadManager.ERROR_HTTP_DATA_ERROR:
                translatedReason = DownloadInfoReasonConstants.ERROR_HTTP_DATA_ERROR;
                break;
            case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                translatedReason = DownloadInfoReasonConstants.ERROR_INSUFFICIENT_STORAGE;
                break;
            case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                translatedReason = DownloadInfoReasonConstants.ERROR_TOO_MANY_REDIRECTS;
                break;
            case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                translatedReason = DownloadInfoReasonConstants.ERROR_UNHANDLED_HTTP_CODE;
                break;
            case DownloadManager.ERROR_UNKNOWN:
                translatedReason = DownloadInfoReasonConstants.ERROR_GENERIC;
                break;
            case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                translatedReason = DownloadInfoReasonConstants.PAUSED_QUEUED_TO_DOWNLOAD_ON_WIFI;
                break;
            case DownloadManager.PAUSED_UNKNOWN:
                translatedReason = DownloadInfoReasonConstants.PAUSED_GENERIC_REASON;
                break;
            case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                translatedReason = DownloadInfoReasonConstants.PAUSED_WAITING_NETWORK;
                break;
            case DownloadManager.PAUSED_WAITING_TO_RETRY:
                translatedReason = DownloadInfoReasonConstants.PAUSED_WAITING_NETWORK_AFTER_ERROR;
                break;
            default:
                translatedReason = reason;
        }
        

        return translatedReason;
    }
}