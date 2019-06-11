package com.icecube.degreeprogrammemapper;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EditAboutActivity extends AppCompatActivity
{
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    //Contents
    EditText fieldAppVersion;
    TextView labelOS;
    Button buttonChangePassword;

    //Footer
    Button buttonSave;
    Button buttonCancel;

    //Change password dialogue
    FrameLayout layoutChangePassword;
    EditText fieldPassword;
    EditText fieldPasswordConfirm;
    Button buttonChangePasswordConfirm;
    Button buttonChangePasswordCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_about);

        //Read and write password and app version
        sharedPreferences = this.getSharedPreferences(ViewStudentsActivity.SHARED_PREFERENCES, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //Contents
        fieldAppVersion = findViewById(R.id.field_EditAbout_AppVersion);
        labelOS = findViewById(R.id.label_EditAbout_OS);
        buttonChangePassword = findViewById(R.id.button_EditAbout_ChangePassword);

        fieldAppVersion.setText(sharedPreferences.getString(ViewStudentsActivity.PREF_VERSION, ViewStudentsActivity.DEFAULT_VERSION));
        labelOS.setText(Build.VERSION.RELEASE);
        buttonChangePassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showChangePassword();
            }
        });

        //Footer Buttons
        buttonSave = findViewById(R.id.button_EditAbout_Save);
        buttonCancel = findViewById(R.id.button_EditAbout_Cancel);

        buttonSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                save();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        //Change Password layout
        layoutChangePassword = findViewById(R.id.layout_EditAbout_ChangePassword);
        fieldPassword = findViewById(R.id.field_EditAbout_Password);
        fieldPasswordConfirm = findViewById(R.id.field_EditAbout_PasswordConfirm);
        buttonChangePasswordConfirm = findViewById(R.id.button_EditAbout_ChangePasswordConfirm);
        buttonChangePasswordCancel = findViewById(R.id.button_EditAbout_ChangePasswordCancel);

        layoutChangePassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                hideChangePassword();
            }
        });
        buttonChangePasswordConfirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                changePassword();
            }
        });
        buttonChangePasswordCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                hideChangePassword();
            }
        });

    }

    private void showChangePassword()
    {
        layoutChangePassword.setVisibility(View.VISIBLE);
    }

    private void hideChangePassword()
    {
        layoutChangePassword.setVisibility(View.GONE);
    }

    private void changePassword()
    {
        String password = fieldPassword.getText().toString();
        String passwordConfirm = fieldPasswordConfirm.getText().toString();
        if (password.equals(passwordConfirm) && !password.equals(""))
        {
            editor.putString(ViewStudentsActivity.PREF_PASSWORD, password);
            editor.apply();
            Toast.makeText(this, "Password Changed!", Toast.LENGTH_LONG).show();
            hideChangePassword();
        }
        else
        {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        }
    }

    private void save()
    {
        editor.putString(ViewStudentsActivity.PREF_VERSION, fieldAppVersion.getText().toString());
        editor.apply();
        setResult(RESULT_OK);
        finish();
    }

    private void cancel()
    {
        setResult(RESULT_CANCELED);
        finish();
    }
}
