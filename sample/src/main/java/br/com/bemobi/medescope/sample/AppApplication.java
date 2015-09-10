package br.com.bemobi.medescope.sample;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by luisfernandez on 7/6/15.
 */
public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }
}
