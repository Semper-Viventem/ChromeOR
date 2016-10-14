package ru.semper_viventem.chromeor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        MainActivity.mTracker.setScreenName("About");
        MainActivity.mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
