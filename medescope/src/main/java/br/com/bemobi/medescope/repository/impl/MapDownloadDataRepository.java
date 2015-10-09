package br.com.bemobi.medescope.repository.impl;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import br.com.bemobi.medescope.constant.DownloadConstants;
import br.com.bemobi.medescope.repository.DownloadDataRepository;
import br.com.bemobi.medescope.repository.PreferencesUtils;

/**
 * Created by bkosawa on 01/07/15.
 */
public class MapDownloadDataRepository implements DownloadDataRepository {

    private static final String TAG = MapDownloadDataRepository.class.getSimpleName();

    private static DownloadDataRepository downloadDataRepository;

    private Map<String, String> downloadsMap = new HashMap<>();

    private Context mContext;

    public static DownloadDataRepository getInstance(Context context) {
        if(downloadDataRepository == null) {
            downloadDataRepository = new MapDownloadDataRepository(context);
        }
        return downloadDataRepository;
    }

    private MapDownloadDataRepository(Context context) {
        this.mContext = context.getApplicationContext();
        load();
    }

    @Override
    public Map<String, String> getDownloadsMap() {
        return downloadsMap;
    }

    @Override
    public void setDownloadsMap(Map<String, String> downloadsMap) {
        this.downloadsMap = downloadsMap;
    }

    @Override
    public void putDownloadData(String downloadId, String data) {
        this.downloadsMap.put(downloadId, data);
        persistDownloadData();
    }

    @Override
    public String getDownloadData(String downloadId) {
        return this.downloadsMap.get(downloadId);
    }

    @Override
    public void removeDownloadData(String downloadId) {

        this.downloadsMap.remove(downloadId);
        persistDownloadData();
    }

    @Override
    public boolean isEmptyDownloadData() {
        return this.downloadsMap.isEmpty();
    }

    @Override
    public boolean containsDownloadDataKey(String key) {
        return this.downloadsMap.containsKey(key);
    }

    @Override
    public void persistSubscribedId(String downloadId) {
        PreferencesUtils.savePreference(mContext.getApplicationContext(), DownloadConstants.PREF_SUBSCRIBED_ID, downloadId);
    }

    @Override
    public String recoverSubscribedId() {
        return PreferencesUtils.getStringPreference(mContext.getApplicationContext(), DownloadConstants.PREF_SUBSCRIBED_ID, "");
    }

    @Override
    public void removeSubscribedId() {
        PreferencesUtils.removePreference(mContext.getApplicationContext(), DownloadConstants.PREF_SUBSCRIBED_ID);
    }

    private void load() {
        String map = PreferencesUtils.getStringPreference(mContext.getApplicationContext(), DownloadConstants.DATA_MAP_PREF, "");
        if(!TextUtils.isEmpty(map)) {
            Gson gson = new Gson();
            downloadsMap = gson.fromJson(map, new TypeToken<HashMap<String, String>>(){}.getType());
        } else {
            downloadsMap = new HashMap<>();
        }
    }

    private void persistDownloadData()
    {
        PreferencesUtils.savePreference(mContext.getApplicationContext(), DownloadConstants.DATA_MAP_PREF, new Gson().toJson(downloadsMap));
    }
}
