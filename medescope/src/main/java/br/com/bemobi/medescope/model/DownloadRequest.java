package br.com.bemobi.medescope.model;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luis.fernandez on 7/5/15.
 */
public class DownloadRequest implements Serializable {

    private String id;
    private String uri;
    private String fileName;
    private String downloadName;
    private String downloadDescription;
    private String clientPayload;
    private boolean shouldDownloadOnlyInWifi;
    private Map<String, String> customHeaders;

    public DownloadRequest() {
        this.customHeaders = new HashMap<>();
    }

    public DownloadRequest(String id, String uri, String fileName, String downloadName, String downloadDescription, String clientPayload, boolean shouldDownloadOnlyInWifi, Map<String, String> customHeaders) {
        this.id = id;
        this.uri = uri;
        this.fileName = fileName;
        this.downloadName = downloadName;
        this.downloadDescription = downloadDescription;
        this.clientPayload = clientPayload;
        this.shouldDownloadOnlyInWifi = shouldDownloadOnlyInWifi;
        this.customHeaders = customHeaders;
    }

    public boolean isValid() {
        return !TextUtils.isEmpty(id) &&
                !TextUtils.isEmpty(uri) &&
                !TextUtils.isEmpty(fileName) &&
                !TextUtils.isEmpty(downloadName) &&
                !TextUtils.isEmpty(downloadDescription) &&
                !TextUtils.isEmpty(clientPayload);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDownloadName() {
        return downloadName;
    }

    public void setDownloadName(String downloadName) {
        this.downloadName = downloadName;
    }

    public String getDownloadDescription() {
        return downloadDescription;
    }

    public void setDownloadDescription(String downloadDescription) {
        this.downloadDescription = downloadDescription;
    }

    public String getClientPayload() {
        return clientPayload;
    }

    public void setClientPayload(String clientPayload) {
        this.clientPayload = clientPayload;
    }

    public Map<String, String> getCustomHeaders() {
        return customHeaders;
    }

    public void setCustomHeaders(Map<String, String> customHeaders) {
        this.customHeaders = customHeaders;
    }

    public boolean shouldDownloadOnlyInWifi() {
        return shouldDownloadOnlyInWifi;
    }

    public void setShouldDownloadOnlyInWifi(boolean shouldDownloadOnlyInWifi) {
        this.shouldDownloadOnlyInWifi = shouldDownloadOnlyInWifi;
    }

    @Override
    public String toString() {
        return "Download{" +
                "id='" + id + '\'' +
                ", uri='" + uri + '\'' +
                ", fileName='" + fileName + '\'' +
                ", downloadName='" + downloadName + '\'' +
                ", downloadDescription='" + downloadDescription + '\'' +
                ", clientPayload='" + clientPayload + '\'' +
                ", shouldDownloadOnlyInWifi=" + shouldDownloadOnlyInWifi +
                ", customHeaders=" + customHeaders +
                '}';
    }
}
