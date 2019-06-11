package com.icecube.degreeprogrammemapper;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity
{
    SharedPreferences sharedPreferences;
    TextView labelAppVersion;
    TextView labelOS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        labelAppVersion = findViewById(R.id.label_About_AppVersion);
        labelOS = findViewById(R.id.label_About_OS);

        sharedPreferences = this.getSharedPreferences(ViewStudentsActivity.SHARED_PREFERENCES, MODE_PRIVATE);

        labelAppVersion.setText(sharedPreferences.getString(ViewStudentsActivity.PREF_VERSION, ViewStudentsActivity.DEFAULT_VERSION));
        labelOS.setText(Build.VERSION.RELEASE);
    }
}
