package br.com.bemobi.medescope.receiver;

import android.content.Context;
import android.content.Intent;

import br.com.bemobi.medescope.log.IntentLogger;
import br.com.bemobi.medescope.log.Logger;

/**
 * Created by luis.fernandez on 6/27/15.
 */
public class BroadcastReceiverLogger {

    private static final String TAG = "BroadcastReceiverLogger";

    private String from;
    private String feature;

    public BroadcastReceiverLogger(String from, String feature) {
        this.from = from;
        this.feature = feature;
    }

    public void onReceive(Context context, Intent intent) {
        Logger.debug(TAG, feature, "");
        Logger.debug(TAG, feature, String.format(">>>>>>>>>>>>>>>>>>>>>>>>>> DUMPING BROADCAST INTENT from: %s", from));
        Logger.debug(TAG, feature, String.format("            [%s] = %s", "ACTION", intent.getAction()));

        new IntentLogger(from, feature).logIntent(intent);
    }
}
