package com.icecube.degreeprogrammemapper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class SelectPathwayActivity extends AppCompatActivity
{
    public static final String TARGET_PATHWAY = "targetPathway";
    public static final String TARGET_NETWORKING = "Networking";
    public static final String TARGET_SOFTWARE = "Software";
    public static final String TARGET_DATABASE = "Database";
    public static final String TARGET_MEDIA = "Media";

    CardView cardSelectPathwayNetwork;
    CardView cardSelectPathwaySoftware;
    CardView cardSelectPathwayDatabase;
    CardView cardSelectPathwayMedia;
    CardView cardSelectPathwayAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pathway);

        cardSelectPathwayNetwork = findViewById(R.id.card_SelectPathway_Network);
        cardSelectPathwayNetwork.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                launchEditPathwayActivity(Pathway.NETWORKING);
            }
        });
        cardSelectPathwaySoftware = findViewById(R.id.card_SelectPathway_Software);
        cardSelectPathwaySoftware.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                launchEditPathwayActivity(Pathway.SOFTWARE);
            }
        });
        cardSelectPathwayDatabase = findViewById(R.id.card_SelectPathway_Database);
        cardSelectPathwayDatabase.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                launchEditPathwayActivity(Pathway.DATABASE);
            }
        });
        cardSelectPathwayMedia = findViewById(R.id.card_SelectPathway_Media);
        cardSelectPathwayMedia.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                launchEditPathwayActivity(Pathway.MULTIMEDIA_WEB);
            }
        });
        cardSelectPathwayAbout = findViewById(R.id.card_SelectPathway_About);
        cardSelectPathwayAbout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                launchEditAboutIntent();
            }
        });
    }

    private void launchEditPathwayActivity(Pathway pathwayToEdit)
    {
        Intent I = new Intent(this, EditPathwayModulesActivity.class);
        switch (pathwayToEdit)
        {
            case NETWORKING:
                I.putExtra(TARGET_PATHWAY, TARGET_NETWORKING);
                break;
            case SOFTWARE:
                I.putExtra(TARGET_PATHWAY, TARGET_SOFTWARE);
                break;
            case DATABASE:
                I.putExtra(TARGET_PATHWAY, TARGET_DATABASE);
                break;
            case MULTIMEDIA_WEB:
                I.putExtra(TARGET_PATHWAY, TARGET_MEDIA);
                break;
        }
        startActivity(I);
    }

    private void launchEditAboutIntent()
    {
        Intent I = new Intent(this, EditAboutActivity.class);
        startActivity(I);
    }
}
