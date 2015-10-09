package br.com.bemobi.medescope.exception;

/**
 * Created by bkosawa on 10/07/15.
 */
public class DirectoryNotMountedException extends DownloadBaseException {

    public DirectoryNotMountedException() {
    }

    public DirectoryNotMountedException(String detailMessage) {
        super(detailMessage);
    }

    public DirectoryNotMountedException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public DirectoryNotMountedException(Throwable throwable) {
        super(throwable);
    }

}
