package br.com.bemobi.medescope.sample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.bemobi.medescope.Medescope;
import br.com.bemobi.medescope.sample.model.NotificationData;
import br.com.bemobi.medescope.callback.DownloadStatusCallback;
import br.com.bemobi.medescope.exception.DirectoryNotMountedException;
import br.com.bemobi.medescope.exception.PathNotFoundException;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String FILE_PATH = "FILE.zip";

    private String FILE_5MB = "http://download.thinkbroadband.com/5MB.zip";
    private String FILE_10MB = "http://download.thinkbroadband.com/10MB.zip";
    private String FILE_20MB = "http://download.thinkbroadband.com/20MB.zip";
    private String FILE_50MB = "http://download.thinkbroadband.com/50MB.zip";
    private String FILE_100MB = "http://download.thinkbroadband.com/100MB.zip";
    private String [] files = {FILE_5MB, FILE_10MB, FILE_20MB, FILE_50MB, FILE_100MB};

    private String downloadName = FILE_5MB;

    private Spinner spinner;
    private ProgressBar progressBar;
    private ArrayAdapter<String> adapter;

    private TextView textStatus;
    private TextView textAction;
    private Medescope mMedescope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> spinnerArray =  new ArrayList<>();
        spinnerArray.add("5MB File");
        spinnerArray.add("10MB File");
        spinnerArray.add("20MB File");
        spinnerArray.add("50MB File");
        spinnerArray.add("100MB File");

        textStatus = (TextView) findViewById(R.id.text);
        textAction = (TextView) findViewById(R.id.textAction);

        progressBar = (ProgressBar) findViewById(R.id.sample_progressBar);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = (Spinner) findViewById(R.id.download_spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int myPosition, long myID) {
                downloadName = files[myPosition];
                mMedescope.updateSubscriptionStatusId(MainActivity.this, "" + myID);
                hideProgress();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
        progressBar.setProgress(0);
        progressBar.setIndeterminate(false);
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        progressBar.setIndeterminate(true);
    }

    private void setPercentProgress(int progress) {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(progress);
        progressBar.setIndeterminate(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mMedescope = Medescope.getInstance(this);
        mMedescope.setApplicationName(getString(R.string.app_name));
        mMedescope.subscribeStatus(this, "" + spinner.getSelectedItemId(), new DownloadStatusCallback() {
            @Override
            public void onDownloadNotEnqueued(String downloadId) {
                textAction.setText("ACTION NOT ENQUEUED");
                textStatus.setText("NOT ENQUEUED:");
                hideProgress();
            }

            @Override
            public void onDownloadPaused(String downloadId, int reason) {
                textAction.setText("ACTION PAUSED");
                textStatus.setText(String.format("NOT ENQUEUED: [reason: %s ]", reason));
            }

            @Override
            public void onDownloadInProgress(String downloadId, int progress) {
                textAction.setText("ACTION IN PROGRESS");
                Log.d(TAG, "Received download progress: " + progress);
                setPercentProgress(progress);
                textStatus.setText(String.format("PROGRESS: [progress: %s ]", progress));
            }

            @Override
            public void onDownloadOnFinishedWithError(String downloadId, int reason, String data) {
                textAction.setText("ACTION FINISH WITH ERROR");
                hideProgress();
            }

            @Override
            public void onDownloadOnFinishedWithSuccess(String downloadId, String filePath, String data) {
                textAction.setText("ACTION  FINISH WITH SUCCESS");
                Log.d(TAG, "Received path: " + filePath);
                checkFilePath(filePath);
                hideProgress();
            }

            @Override
            public void onDownloadCancelled(String downloadId) {
                textAction.setText("ACTION  FINISH WITH SUCCESS");
                hideProgress();
            }
        });
    }

    private void checkFilePath(String filePath) {
        String originalFilePath = "";
        try {
            originalFilePath = mMedescope.getDownloadDirectoryToRead(FILE_PATH);
            Log.d(TAG, "Original path: " + originalFilePath);
        } catch (DirectoryNotMountedException | PathNotFoundException e) {
            e.printStackTrace();
        }

        if (filePath.equals(originalFilePath)) {
            Toast.makeText(MainActivity.this, "Path sent is the expected", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Medescope.getInstance(this).unsubscribeStatus(this);
    }

    public void download(View view) {
        NotificationData data = new NotificationData("" + spinner.getSelectedItemId(), downloadName, getString(R.string.app_name));

        boolean shouldDownloadOnlyInWiFi = isLargeFile(downloadName);

        mMedescope.enqueue(
                data.getId(),
                downloadName,
                FILE_PATH,
                (String) spinner.getSelectedItem(),
                data.toJson(),
                shouldDownloadOnlyInWiFi);
        showProgress();
    }

    public void cancel(View view) {
        mMedescope.cancel("" + spinner.getSelectedItemId());
        hideProgress();
    }

    private boolean isLargeFile(String selected) {
        return FILE_20MB.equals(selected) || FILE_50MB.equals(selected) || FILE_100MB.equals(selected);
    }
}
