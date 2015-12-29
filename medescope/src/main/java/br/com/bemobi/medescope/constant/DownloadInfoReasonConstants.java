package br.com.bemobi.medescope.constant;

/**
 * Created by raphael on 7/7/15.
 */
public class DownloadInfoReasonConstants {

    /**
     * Unknown error. :P
     */
    //public final static int REASON_UNKNOWN_ERROR = 0;
    public final static int PAUSED_GENERIC_ERROR = 2000;


    /**
     * Value of COLUMN_REASON when the download is paused because some network error occurred and the download manager is waiting before retrying the request.
     */
    //public final static int REASON_PAUSED_WAITING_TO_RETRY = 1;
    public final static int PAUSED_WAITING_NETWORK_AFTER_ERROR = 2001;

    /**
     * Value of COLUMN_REASON when the download is waiting for network connectivity to proceed.
     */
    //public final static int REASON_PAUSED_WAITING_FOR_NETWORK = 2;
    public final static int PAUSED_WAITING_NETWORK = 2002;

    /**
     * Value of COLUMN_REASON when the download exceeds a size limit for downloads over the mobile network and the download manager is waiting for a Wi-Fi connection to proceed.
     */
    //public final static int PAUSED_QUEUED_FOR_WIFI = 3;
    public final static int PAUSED_QUEUED_TO_DOWNLOAD_ON_WIFI = 2003;

    /**
     * Value of COLUMN_REASON when the download is paused for some other reason.
     */
    //public final static int REASON_PAUSED_UNKNOWN = 4;
    public final static int PAUSED_GENERIC_REASON = 2004;







    /**
     * Value of COLUMN_REASON when a storage issue arises which doesn't fit under any other error code.
     */
    //public final static int REASON_ERROR_FILE_ERROR = 1001;
    public final static int ERROR_FILE_GENERIC_PROBLEM = 3001;

    /**
     * Value of COLUMN_REASON when an HTTP code was received that download manager can't handle.
     */
    //public final static int REASON_ERROR_UNHANDLED_HTTP_CODE = 1002;
    public final static int ERROR_UNHANDLED_HTTP_CODE = 3002;

    /**
     * Value of COLUMN_REASON when an error receiving or processing data occurred at the HTTP level.
     */
    //public final static int REASON_ERROR_HTTP_DATA_ERROR = 1004;
    public final static int ERROR_HTTP_DATA_ERROR = 3004;

    /**
     * Value of COLUMN_REASON when there were too many redirects.
     */
    //public final static int REASON_ERROR_TOO_MANY_REDIRECTS = 1005;
    public final static int ERROR_TOO_MANY_REDIRECTS = 3005;

    /**
     * Value of COLUMN_REASON when there was insufficient storage space.
     */
    //public final static int REASON_ERROR_INSUFFICIENT_SPACE = 1006;
    public final static int ERROR_INSUFFICIENT_STORAGE = 3006;

    /**
     * Value of COLUMN_REASON when no external storage device was found.
     */
    //public final static int REASON_ERROR_DEVICE_NOT_FOUND = 1007;
    public final static int ERROR_STORAGE_NOT_FOUND = 3007;

    /**
     * Value of COLUMN_REASON when some possibly transient error occurred but we can't resume the download.
     */
    //public final static int REASON_ERROR_CANNOT_RESUME = 1008;
    public final static int ERROR_GENERIC = 3008;

    /**
     * Value of COLUMN_REASON when the requested destination file already exists (the download manager will not overwrite an existing file).
     */
    //public final static int REASON_ERROR_FILE_ALREADY_EXISTS = 1009;
    public final static int ERROR_ALREADY_EXISTING_FILE = 3009;

    /**
     * Value of COLUMN_REASON when a storage issue arises which doesn't fit under any other error code.
     */
    //public final static int REASON_ERROR_BLOCKED = 1010;
    public final static int ERROR_GENERIC_STORAGE_PROBLEM = 3010;

    /**
     * Value of COLUMN_REASON There is no permission to write on file.
     */
    public final static int ERROR_PERMISSION_NOT_GRANTED = 3011;

    public static String getDownloadReasonText(int reason) {
        String reasonStr = "";

        switch(reason) {
            case DownloadInfoReasonConstants.ERROR_GENERIC: {
                reasonStr = "ERROR_GENERIC";
                break;
            }
            case DownloadInfoReasonConstants.ERROR_STORAGE_NOT_FOUND: {
                reasonStr = "ERROR_STORAGE_NOT_FOUND";
                break;
            }
            case DownloadInfoReasonConstants.ERROR_ALREADY_EXISTING_FILE: {
                reasonStr = "ERROR_ALREADY_EXISTING_FILE";
                break;
            }
            case DownloadInfoReasonConstants.ERROR_FILE_GENERIC_PROBLEM: {
                reasonStr = "ERROR_FILE_GENERIC_PROBLEM";
                break;
            }
            case DownloadInfoReasonConstants.ERROR_HTTP_DATA_ERROR: {
                reasonStr = "ERROR_HTTP_DATA_ERROR";
                break;
            }
            case DownloadInfoReasonConstants.ERROR_INSUFFICIENT_STORAGE: {
                reasonStr = "ERROR_INSUFFICIENT_STORAGE";
                break;
            }
            case DownloadInfoReasonConstants.ERROR_TOO_MANY_REDIRECTS: {
                reasonStr = "ERROR_TOO_MANY_REDIRECTS";
                break;
            }
            case DownloadInfoReasonConstants.ERROR_UNHANDLED_HTTP_CODE: {
                reasonStr = "ERROR_UNHANDLED_HTTP_CODE";
                break;
            }
            case DownloadInfoReasonConstants.PAUSED_QUEUED_TO_DOWNLOAD_ON_WIFI: {
                reasonStr = "PAUSED_QUEUED_TO_DOWNLOAD_ON_WIFI";
                break;
            }
            case DownloadInfoReasonConstants.PAUSED_GENERIC_REASON: {
                reasonStr = "PAUSED_GENERIC_REASON";
                break;
            }
            case DownloadInfoReasonConstants.PAUSED_WAITING_NETWORK: {
                reasonStr = "PAUSED_WAITING_NETWORK";
                break;
            }
            case DownloadInfoReasonConstants.PAUSED_WAITING_NETWORK_AFTER_ERROR: {
                reasonStr = "PAUSED_WAITING_NETWORK_AFTER_ERROR";
                break;
            }
        }

        return reasonStr;
    }
}
