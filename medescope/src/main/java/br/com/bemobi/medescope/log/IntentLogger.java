package br.com.bemobi.medescope.log;

import android.content.Intent;
import android.os.Bundle;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by luisfernandez on 7/3/15.
 */
public class IntentLogger {
    private static final String TAG = "IntentLogger";

    private String from;
    private String feature;

    public IntentLogger(String from, String feature) {
        this.from = from;
        this.feature = feature;
    }

    public void logIntent(Intent intent) {
        if (intent != null) {
            this.logBundle(intent.getExtras());
        }
    }

    public void logBundle(Bundle bundle) {
        Logger.debug(TAG, feature, String.format(">>>>>>>>>>>>>>>>>>>>>>>>>> DUMPING BUNDLE from: %s", from));

        if (bundle != null) {
            Set<String> keys = bundle.keySet();
            for (String key : keys) {
                Object object = bundle.get(key);

                if (object != null) {
                    String[] values = object.toString().split(", ");

                    for (String value : values) {
                        Logger.debug(TAG, feature, String.format("            [%s] = %s", key, value));
                    }
                }

            }

            Logger.debug(TAG, feature, "\n");
        }
    }
}
