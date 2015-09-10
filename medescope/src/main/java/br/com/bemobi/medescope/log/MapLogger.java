package br.com.bemobi.medescope.log;

import java.util.Map;

/**
 * Created by luis.fernandez on 7/5/15.
 */
public class MapLogger {

    private static final String TAG = "MapLogger";
    private String feature;

    public MapLogger(String feature) {
        this.feature = feature;
    }

    public void log(Map<String, String> map) {
        for (String key : map.keySet()) {
            Logger.debug(TAG, feature, String.format("            [%s] = %s", key, map.get(key)));
        }
        Logger.debug(TAG, feature, "\n");
    }

    public void logStringLong(Map<String, Long> map) {
        for (String key : map.keySet()) {
            Logger.debug(TAG, feature, String.format("            [%s] = %s", key, map.get(key)));
        }
        Logger.debug(TAG, feature, "\n");
    }

    public void logLongString(Map<Long, String> map) {
        for (Long key : map.keySet()) {
            Logger.debug(TAG, feature, String.format("            [%s] = %s", key, map.get(key)));
        }
        Logger.debug(TAG, feature, "\n");
    }
}
