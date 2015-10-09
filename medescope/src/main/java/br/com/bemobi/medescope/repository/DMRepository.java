package br.com.bemobi.medescope.repository;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import br.com.bemobi.medescope.constant.DownloadConstants;

/**
 * Created by bkosawa on 01/07/15.
 */
public class DMRepository {

    private static final String TAG = DMRepository.class.getSimpleName();

    private Context mContext;
    private static DMRepository instance;

    private Map<String, Long> mapIdsClientToDM;
    private Map<Long, String> mapIdsDMToClient;

    private DMRepository(Context context) {
        this.mContext = context;
        this.mapIdsClientToDM = new HashMap<>();
        this.mapIdsDMToClient = new HashMap<>();
        this.loadMaps();
    }

    public static DMRepository getInstance(Context context) {
        if(instance == null){
            instance = new DMRepository(context);
        }
        return instance;
    }

    public void persistIds(String downloadId, Long dmDownloadId) {
        if( !mapIdsDMToClient.containsKey(dmDownloadId) && !mapIdsClientToDM.containsKey(downloadId)) {
            mapIdsDMToClient.put(dmDownloadId, downloadId);
            mapIdsClientToDM.put(downloadId, dmDownloadId);
            persistMaps();
        }

        //TODO Should we give a feedback?
    }

    public void removeId(String downloadId){
        Long dmDownloadId = mapIdsClientToDM.get(downloadId);
        mapIdsDMToClient.remove(dmDownloadId);
        mapIdsClientToDM.remove(downloadId);
        persistMaps();
    }

    public void removeId(Long dmDownloadId){
        String downloadId = mapIdsDMToClient.get(dmDownloadId);
        mapIdsDMToClient.remove(dmDownloadId);
        mapIdsClientToDM.remove(downloadId);
        persistMaps();
    }

    public Long getDMId(String clientId){
        return mapIdsClientToDM.get(clientId);
    }

    public String getClientId(Long dmDownloadId){
        return mapIdsDMToClient.get(dmDownloadId);
    }

    private void loadMaps(){
        String mapIdsLibToDMStr = PreferencesUtils.getStringPreference(mContext.getApplicationContext(), DownloadConstants.DM_STRING_IDS_LIB_TO_DMIDS_MAP_PREF, "");
        if(!TextUtils.isEmpty(mapIdsLibToDMStr)) {
            Gson gson = new Gson();
            mapIdsClientToDM = gson.fromJson(mapIdsLibToDMStr, new TypeToken<HashMap<String, Long>>(){}.getType());
        } else {
            mapIdsClientToDM = new HashMap<>();
        }

        String mapIdsDMtoLibStr = PreferencesUtils.getStringPreference(mContext.getApplicationContext(), DownloadConstants.DM_STRING_IDS_DMIDS_TO_LIB_MAP_PREF, "");
        if(!TextUtils.isEmpty(mapIdsDMtoLibStr)) {
            Gson gson = new Gson();
            mapIdsDMToClient = gson.fromJson(mapIdsDMtoLibStr, new TypeToken<HashMap<Long, String>>(){}.getType());
        } else {
            mapIdsDMToClient = new HashMap<>();
        }
    }

    private void persistMaps(){
        PreferencesUtils.savePreference(mContext.getApplicationContext(), DownloadConstants.DM_STRING_IDS_LIB_TO_DMIDS_MAP_PREF, new Gson().toJson(mapIdsClientToDM));
        PreferencesUtils.savePreference(mContext.getApplicationContext(), DownloadConstants.DM_STRING_IDS_DMIDS_TO_LIB_MAP_PREF, new Gson().toJson(mapIdsDMToClient));
    }
}
