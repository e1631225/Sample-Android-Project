package com.example.infolinetestapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.infolinetestapplication.R;
import com.example.infolinetestapplication.adapter.RecordAdapter;
import com.example.infolinetestapplication.entity.Record;
import com.example.infolinetestapplication.utils.DatabaseHelper;
import com.example.infolinetestapplication.utils.Variables;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by suleyman on 31.3.2016.
 * Login den sonra yönlendirilen eklenmiş listelerden oluşturulan liste
 */
public class ListActivity extends Activity {
    ListView myListView;
    DatabaseHelper databaseHelper = null;
    RecordAdapter recordAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        myListView = (ListView) findViewById(R.id.listView);
        databaseHelper = getHelper();
        initializeList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                menuItemAddClickHandler();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);

        MenuItem item = menu.findItem(R.id.action_add);
        item.setVisible(true);
        return true;
    }

    // listeye yeni item ekler
    // yeni item ın db de kaydını oluşturur
    // Snapshot ya da video dosyası daha önce inmediyse indirmeyi başlatır
    private void menuItemAddClickHandler() {
        int size = Variables.DOWNLOAD_LINKS.length;
        int randomVideoIndex = (int) Math.floor(Math.random() * size); // index size dan küçük olmalı
        int randomSnapshotIndex = (int) Math.floor(Math.random() * size); // index size dan küçük olmalı
        String videoLink = Variables.DOWNLOAD_LINKS[randomVideoIndex];
        String snapshotLink = Variables.SNAPSHOT_LINKS[randomSnapshotIndex];
        String videoName = videoLink.substring(videoLink.lastIndexOf("/"));
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
        sb.append(videoName);
        String videoFileName = sb.toString();

        String snapshotName = snapshotLink.substring(snapshotLink.lastIndexOf("/"));
        StringBuilder sb2 = new StringBuilder();
        sb2.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
        sb2.append(snapshotName);
        String snapshotFileName = sb2.toString();

        File videoFile = new File(videoFileName);
        if(!videoFile.exists()) {
            new DownloadFileFromURL(videoLink).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, videoFileName);
        }
        File snapshotFile = new File(snapshotFileName);
        if(!snapshotFile.exists()) {
            new DownloadFileFromURL(snapshotLink).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, snapshotFileName);
        }
        Record record = databaseHelper.createRecord(snapshotFileName, videoFileName);
        if(record != null) {
            recordAdapter.getRecordList().add(record);
            recordAdapter.notifyDataSetChanged();
        }

    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    // db de kayıtlarla kayıt listesini initializa eder
    private void initializeList() {
        List<Record> records = new ArrayList<>();
        try {
            records = databaseHelper.getRecordDao().queryForAll();
        } catch (Exception e) {

        }
        recordAdapter = new RecordAdapter(records, ListActivity.this);
        myListView.setAdapter(recordAdapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Record record = recordAdapter.getRecordList().get(position);
                Intent intent = new Intent(ListActivity.this, WatchActivity.class);
                intent.putExtra(Variables.VIDEO_URL_TAG, record.getVideoPath());
                startActivity(intent);
                finish();
            }
        });
    }

    // indirme işlenmini yapan asynctask
    // arka planda indirilecek video ve resim dosyalarını
    // telefonun indirilenler kısmına kaydeder
    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        String inputURL = null;

        public DownloadFileFromURL(String inputFile) {
            this.inputURL = inputFile;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(ListActivity.this, getResources().getString(R.string.downloading) + " " + inputURL,Toast.LENGTH_SHORT).show();
        }
        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... inputStrings) {
            int count;
            String outputURL = inputStrings[0];
            try {
                URL url = new URL(inputURL);
                URLConnection connection = url.openConnection();
                connection.connect();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                OutputStream output = new FileOutputStream(outputURL);
                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            Toast.makeText(ListActivity.this, getResources().getString(R.string.downloadComplete),Toast.LENGTH_SHORT).show();
            recordAdapter.notifyDataSetChanged();
        }

    }
}
