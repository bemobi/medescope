package br.com.bemobi.medescope.exception;

/**
 * Created by bkosawa on 10/07/15.
 */
public class PathNotFoundException extends DownloadBaseException {

    public PathNotFoundException() {
    }

    public PathNotFoundException(String detailMessage) {
        super(detailMessage);
    }

    public PathNotFoundException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public PathNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
