package ru.semper_viventem.chromeor.presentation.view.about;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.analytics.HitBuilders;

import ru.semper_viventem.chromeor.R;
import ru.semper_viventem.chromeor.presentation.view.main.MainActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        MainActivity.mTracker.setScreenName("About");
        MainActivity.mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
