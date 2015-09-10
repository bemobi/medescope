package br.com.bemobi.medescope.constant;

/**
 * Created by raphael on 7/7/15.
 */
public class DownloadInfoStatusConstants {
    public final static int NOT_ENQUEUED_STATUS = -1;

    public final static int PENDING_STATUS = 1 << 0;

    public final static int IN_PROGRESS_STATUS = 1 << 1;

    public final static int PAUSED_STATUS = 1 << 2;

    public final static int SUCCESSFUL_STATUS = 1 << 3;

    public final static int FAILED_STATUS = 1 << 4;

    public final static int UNKNOWN_ERROR = 1000;
}
