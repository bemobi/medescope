package br.com.bemobi.medescope.wrapper;

/**
 * Created by raphael on 7/7/15.
 */
public interface DownloadInfoWrapper<STATUS, REASON> {
    int translateStatus(STATUS status);

    int translateReason(REASON reason);
}
