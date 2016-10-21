package ru.semper_viventem.chromeor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private LinearLayout contenner;
    private WorkDB mDatabaseHelper;
    private SQLiteDatabase sdb;
    private File dbfile;

    public static Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contenner = (LinearLayout)findViewById(R.id.mainContanner);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchCP();
            }
        });

        String PACKAGE_NAME = getApplicationContext().getPackageName();
        final String DB_DESTINATION = "/data/data/" + PACKAGE_NAME + "/databases";
        final String DB_NAME = "login_data.db";
        final String DB_BASE_DEST = DB_DESTINATION + File.separator + DB_NAME;
        dbfile = new File(DB_BASE_DEST);

        //delete database if exist
        if (dbfile.exists())
            dbfile.delete();

        mDatabaseHelper = new WorkDB(MainActivity.this, dbfile.getPath(), null, 1);
        sdb = mDatabaseHelper.getWritableDatabase();

        //google analytics
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mTracker.setScreenName("MainActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    public void searchCP() {

        String PACKAGE_NAME = getApplicationContext().getPackageName();

        AsyncCommand ac = new AsyncCommand(MainActivity.this, "cat /data/data/com.android.chrome/app_chrome/Default/Login\\ Data > /data/data/" + PACKAGE_NAME + "/databases/login_data.db \n"
                + "chmod 777 /data/data/" + PACKAGE_NAME + "/databases/login_data.db \n"
                + "chmod 777 /data/user/0/" + PACKAGE_NAME + "/databases/login_data.db \n") {

            ProgressDialog pd;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                contenner.removeAllViews();
                pd = new ProgressDialog(context);
                pd.setTitle(getString(R.string.login));
                pd.setMessage(getString(R.string.get_database));
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.setIndeterminate(true);
                pd.show();
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);

                if (exitCode == 255) {
                    Dialog.showDialog(
                            MainActivity.this,
                            getString(R.string.oh_no),
                            getString(R.string.have_root));
                } else {

                    try {
                        readDB(sdb);
                        Toast.makeText(MainActivity.this, R.string.is_loaded, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Dialog.showDialog(
                                MainActivity.this,
                                getString(R.string.oh_no),
                                getString(R.string.unknow_error));
                    }
                }

                pd.cancel();
            }
        };
        ac.execute();
    }

    private void readDB (SQLiteDatabase sdb) {
        Cursor cursor = sdb.query("logins", new String[]{WorkDB.action_url, WorkDB.origin_url, WorkDB.username_value, WorkDB.password_value},
                null, null,
                null, null, null);

        cursor.moveToFirst();

        while (cursor.moveToNext()) {
            final String action_url = cursor.getString(cursor.getColumnIndex(WorkDB.action_url));
            final String origin_url = cursor.getString(cursor.getColumnIndex(WorkDB.origin_url));
            final String username_value = cursor.getString(cursor.getColumnIndex(WorkDB.username_value));

            byte[] blob = cursor.getBlob(cursor.getColumnIndex(WorkDB.password_value));
            final String password_value = new String(blob);

            Div div = new Div(MainActivity.this, action_url, origin_url, username_value, password_value);
            contenner.addView(div.getThisContanner());
        }


        cursor.close();

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("getPass")
                .setAction("getting password list")
                .build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


}
