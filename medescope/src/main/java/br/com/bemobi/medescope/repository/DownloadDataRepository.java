package br.com.bemobi.medescope.repository;

import java.util.Map;

/**
 * Created by bkosawa on 02/07/15.
 */
public interface DownloadDataRepository {

    Map<String, String> getDownloadsMap();

    void setDownloadsMap(Map<String, String> downloadsMap);

    void putDownloadData(String downloadId, String data);

    String getDownloadData(String downloadId);

    void removeDownloadData(String downloadId);

    boolean isEmptyDownloadData();

    boolean containsDownloadDataKey(String key);

    void persistSubscribedId(String downloadId);

    String recoverSubscribedId();

    void removeSubscribedId();
}
