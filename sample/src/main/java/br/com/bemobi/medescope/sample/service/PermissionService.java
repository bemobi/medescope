package br.com.bemobi.medescope.sample.service;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import br.com.bemobi.medescope.sample.MainActivity;

/**
 * Created by mac on 18/01/18.
 */

public class PermissionService {
    public static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 2018;

    public static void requestPermissions(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
        }
    }
}
