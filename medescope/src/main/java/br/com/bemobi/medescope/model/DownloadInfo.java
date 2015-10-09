package br.com.bemobi.medescope.model;

import android.os.Parcel;
import android.os.Parcelable;

import br.com.bemobi.medescope.constant.DownloadInfoStatusConstants;

/**
 * Created by bkosawa on 30/06/15.
 */
public class DownloadInfo implements Parcelable{

    private int status;

    private int reason;

    private String filename;

    private String localURI;

    private long lastModified;

    private long downloadedSoFar;

    private long totalSize;

    private int progress;

    public DownloadInfo() {
    }

    public DownloadInfo(int status, int reason, String filename, String localURI, long lastModified, long downloadedSoFar, long totalSize, int progress) {
        this.status = status;
        this.reason = reason;
        this.filename = filename;
        this.localURI = localURI;
        this.lastModified = lastModified;
        this.downloadedSoFar = downloadedSoFar;
        this.totalSize = totalSize;
        this.progress = progress;
    }

    public DownloadInfo(int status, int reason) {
        this.status = status;
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "DownloadInfo{" +
                "status=" + status +
                ", reason=" + reason +
                ", filename='" + filename + '\'' +
                ", localURI='" + localURI + '\'' +
                ", lastModified=" + lastModified +
                ", downloadedSoFar=" + downloadedSoFar +
                ", totalSize=" + totalSize +
                ", progress=" + progress +
                '}';
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setLocalURI(String localURI) {
        this.localURI = localURI;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public void setDownloadedSoFar(long downloadedSoFar) {
        this.downloadedSoFar = downloadedSoFar;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getReason() {
        return reason;
    }

    public String getFilename() {
        return filename;
    }

    public String getLocalURI() {
        return localURI;
    }

    public long getLastModified() {
        return lastModified;
    }

    public long getDownloadedSoFar() {
        return downloadedSoFar;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public int getProgress() {
        return progress;
    }

    public boolean isPaused() {
        return status == DownloadInfoStatusConstants.PAUSED_STATUS;
    }

    public boolean isInProgress() {
        return status == DownloadInfoStatusConstants.IN_PROGRESS_STATUS;
    }

    public boolean hasFinishedWithSuccess() {
        return status == DownloadInfoStatusConstants.SUCCESSFUL_STATUS;
    }

    public boolean hasFinishedWithError() {
        return status == DownloadInfoStatusConstants.FAILED_STATUS;
    }

    public boolean hasFinished() {
        return status == DownloadInfoStatusConstants.SUCCESSFUL_STATUS
                || status == DownloadInfoStatusConstants.FAILED_STATUS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DownloadInfo that = (DownloadInfo) o;

        if (status != that.status) return false;
        if (reason != that.reason) return false;
        return (Double.compare(that.progress, progress) != 0);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.status);
        out.writeInt(this.reason);
        out.writeString(this.filename);
        out.writeString(this.localURI);
        out.writeLong(this.lastModified);
        out.writeLong(this.downloadedSoFar);
        out.writeLong(this.totalSize);
        out.writeInt(this.progress);
    }

    private DownloadInfo(Parcel in) {
        this.status = in.readInt();
        this.reason = in.readInt();
        this.filename = in.readString();
        this.localURI = in.readString();
        this.lastModified = in.readLong();
        this.downloadedSoFar = in.readLong();
        this.totalSize = in.readLong();
        this.progress = in.readInt();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public DownloadInfo createFromParcel(Parcel in) {
            return new DownloadInfo(in);
        }

        public DownloadInfo[] newArray(int size) {
            return new DownloadInfo[size];
        }
    };
}
