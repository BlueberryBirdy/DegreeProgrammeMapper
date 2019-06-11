package com.icecube.degreeprogrammemapper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class EditStudentActivity extends AppCompatActivity {

    EditText fieldStudentID;
    EditText fieldStudentName;

    CardView cardNetwork;
    CardView cardSoftware;
    CardView cardDatabase;
    CardView cardMultimedia;

    RadioButton radioNetwork;
    RadioButton radioSoftware;
    RadioButton radioDatabase;
    RadioButton radioMultimedia;
    Pathway selectedPathway;

    View footer;
    Button buttonSave;
    Button buttonCancel;
    Button buttonDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        fieldStudentID = findViewById(R.id.field_AddStudent_ID);
        fieldStudentName = findViewById(R.id.field_AddStudent_Name);

        cardNetwork = findViewById(R.id.card_Network);
        cardSoftware = findViewById(R.id.card_Software);
        cardDatabase = findViewById(R.id.card_Database);
        cardMultimedia = findViewById(R.id.card_Multimedia);

        radioNetwork = findViewById(R.id.radio_Networking);
        radioSoftware = findViewById(R.id.radioButtonSoftware);
        radioDatabase = findViewById(R.id.radioButtonDatabase);
        radioMultimedia = findViewById(R.id.radioButtonMultimedia);

        footer = findViewById(R.id.editStudent_Footer);
        buttonSave = footer.findViewById(R.id.buttonSave);
        buttonCancel = footer.findViewById(R.id.buttonCancel);
        buttonDelete = footer.findViewById(R.id.buttonDelete);

        cardNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChecked(Pathway.NETWORKING);
            }
        });
        cardSoftware.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChecked(Pathway.SOFTWARE);
            }
        });
        cardDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChecked(Pathway.DATABASE);
            }
        });
        cardMultimedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChecked(Pathway.MULTIMEDIA_WEB);
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Save();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cancel();
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delete();
            }
        });

        Bundle bundle = getIntent().getExtras();
        String mode = bundle.getString(ViewStudentsActivity.MODE);
        if (mode.equals(ViewStudentsActivity.MODE_ADD))
        {
            buttonDelete.setVisibility(View.GONE);
        }
        else if (mode.equals(ViewStudentsActivity.MODE_EDIT))
        {
            //Load existing data from the bundle
            int loadedID = bundle.getInt(ViewStudentsActivity.STUDENT_ID);
            String loadedName = bundle.getString(ViewStudentsActivity.STUDENT_NAME);
            Pathway loadedPathway = Pathway.values()[bundle.getInt(ViewStudentsActivity.STUDENT_PATHWAY)];

            //Populate fields with the existing data
            fieldStudentID.setText(String.valueOf(loadedID));
            fieldStudentName.setText(loadedName);
            setChecked(loadedPathway);
        }
    }

    private void setChecked(Pathway pathway)
    {
        switch (pathway)
        {
            case NETWORKING:
                selectedPathway = Pathway.NETWORKING;
                radioNetwork.setChecked(true);
                radioSoftware.setChecked(false);
                radioDatabase.setChecked(false);
                radioMultimedia.setChecked(false);
                break;
            case SOFTWARE:
                selectedPathway = Pathway.SOFTWARE;
                radioNetwork.setChecked(false);
                radioSoftware.setChecked(true);
                radioDatabase.setChecked(false);
                radioMultimedia.setChecked(false);
                break;
            case DATABASE:
                selectedPathway = Pathway.DATABASE;
                radioNetwork.setChecked(false);
                radioSoftware.setChecked(false);
                radioDatabase.setChecked(true);
                radioMultimedia.setChecked(false);
                break;
            case MULTIMEDIA_WEB:
                selectedPathway = Pathway.MULTIMEDIA_WEB;
                radioNetwork.setChecked(false);
                radioSoftware.setChecked(false);
                radioDatabase.setChecked(false);
                radioMultimedia.setChecked(true);
                break;
        }
    }

    private void Save()
    {
        Intent i = new Intent(this, ViewStudentsActivity.class);
        int studentID = Integer.parseInt(fieldStudentID.getText().toString());
        String studentName = fieldStudentName.getText().toString();
        i.putExtra(ViewStudentsActivity.STUDENT_ID, studentID);
        i.putExtra(ViewStudentsActivity.STUDENT_NAME, studentName);
        i.putExtra(ViewStudentsActivity.STUDENT_PATHWAY, selectedPathway.ordinal());
        setResult(RESULT_OK, i);
        finish();
    }

    private void Cancel()
    {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void Delete()
    {
        Intent i = new Intent(this, ViewStudentsActivity.class);
        int studentID = Integer.parseInt(fieldStudentID.getText().toString());
        i.putExtra(ViewStudentsActivity.STUDENT_ID, studentID);
        setResult(ViewStudentsActivity.RESULT_DELETE, i);
        finish();
    }
}
