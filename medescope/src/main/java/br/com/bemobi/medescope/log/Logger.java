package br.com.bemobi.medescope.log;

import android.content.Context;
import android.util.Log;

/**
 * Created by brunocosta on 9/10/15.
 */
public class Logger {

    private static final String TAG_tag = Logger.class.getSimpleName();

    public static void setContext(Context mContext) {
        LogConfig.getInstance().setContext(mContext);
    }

    private static String getString(String word) {
        return (word == null) ? "null" : word;
    }

    public static void debug(String tag, String msg) {
        if (LogConfig.getInstance().isDebug()) {
            Log.d(TAG_tag, getString(tag) + ":" + getString(msg));
        }
    }

    public static void debug(String tag, String feature, String msg) {
        if (LogConfig.getInstance().isDebug()) {
            if (LogConfig.getInstance().isDetailed()) {
                Log.d(TAG_tag, getString(tag) + " [FEATURE:] " + getString(feature) + " > " + getString(msg));
            } else {
                Log.d(TAG_tag, getString(feature) + " > " + getString(msg));
            }
        }
    }

    public static void debug(String tag, String msg, Throwable throwable) {
        if (LogConfig.getInstance().isDebug()) {
            Log.d(TAG_tag, getString(tag) + ":" + getString(msg), throwable);
        }
    }

    public static void error(String tag, String msg) {
        if (LogConfig.getInstance().isError()) {
            Log.e(TAG_tag, getString(tag) + ":" + getString(msg));
        }
    }

    public static void error(String tag, String feature, String msg) {
        if (LogConfig.getInstance().isDebug()) {
            Log.e(TAG_tag, getString(tag) + " [FEATURE:] " + getString(feature) + " > " + getString(msg));
        }
    }

    public static void error(String tag, String msg, Throwable th) {
        if (LogConfig.getInstance().isError()) {
            Log.e(TAG_tag, getString(tag) + ":" + getString(msg), th);
        }
    }

    public static class LogConfig {
        private boolean detailed = false;

        private boolean debug = false;

        private boolean error = false;

        private int showWarnCount = 0;

        private static LogConfig instance;

        private Context mContext;

        protected static LogConfig getInstance() {
            if (instance == null) {
                instance = new LogConfig();
            }
            return instance;
        }

        protected void setContext(Context mContext) {
            this.mContext = mContext.getApplicationContext();
            init();
        }

        private void init() {
            initDetailedLog();
            initDebugLog();
            initErrorLog();
        }

        private void initDetailedLog() {
            int id = mContext.getResources().getIdentifier("mcare_detailed_log", "bool", mContext.getPackageName());
            if (id > 0) {
                detailed = mContext.getResources().getBoolean(id);
            }
        }

        private void initDebugLog() {
            int id = mContext.getResources().getIdentifier("mcare_debug_log", "bool", mContext.getPackageName());
            if (id > 0) {
                debug = mContext.getResources().getBoolean(id);
            }
        }

        private void initErrorLog() {
            int id = mContext.getResources().getIdentifier("mcare_error_log", "bool", mContext.getPackageName());
            if (id > 0) {
                error = mContext.getResources().getBoolean(id);
            }
        }

        protected boolean isDetailed() {
            warningNullContext();
            return detailed;
        }

        protected boolean isDebug() {
            warningNullContext();
            return debug;
        }

        protected boolean isError() {
            warningNullContext();
            return error;
        }

        private void warningNullContext() {
            if (mContext == null) {
                if (showWarnCount < 1) {
                    Log.w(">>>> MCareLogUtil",
                            "Use LogUtil.setContext(mContext) para que sua configuração no XML tenha efeito. Default: Todos os logs desligados.\n"
                                    + "Exemplo de XML:\n" + "<bool name=\"mcare_debug_log\">true</bool>\n"
                                    + "<bool name=\"mcare_detailed_log\">true</bool>\n" + "<bool name=\"mcare_error_log\">true</bool>");
                    showWarnCount++;
                }
            }
        }
    }

}
