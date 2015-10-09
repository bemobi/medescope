package br.com.bemobi.medescope.exception;

/**
 * Created by bkosawa on 10/07/15.
 */
public class DownloadBaseException extends Exception {

    public DownloadBaseException() {
    }

    public DownloadBaseException(String detailMessage) {
        super(detailMessage);
    }

    public DownloadBaseException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public DownloadBaseException(Throwable throwable) {
        super(throwable);
    }

}
