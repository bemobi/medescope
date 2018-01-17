package br.com.bemobi.medescope.compat;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * Created by mac on 17/01/18.
 */

public class ContextCompat {

    public static void startService(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= 26) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

}
