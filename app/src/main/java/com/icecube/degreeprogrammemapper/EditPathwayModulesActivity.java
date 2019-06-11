package com.icecube.degreeprogrammemapper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class EditPathwayModulesActivity extends AppCompatActivity
{

    Button buttonSave;
    Button buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pathway_modules);
    }

    private void save()
    {
        setResult(RESULT_OK);
        finish();
    }

    private void cancel()
    {
        setResult(RESULT_CANCELED);
        finish();
    }
}
