package com.icecube.degreeprogrammemapper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ViewStudentsActivity extends AppCompatActivity
{
    //Name of the shared preferences for this app and its properties and their defaults
    public static final String SHARED_PREFERENCES = "DPM";
    public static final String PREF_VERSION = "Version";
    public static final String DEFAULT_VERSION = "1.0";
    public static final String PREF_PASSWORD = "Password";
    public static final String DEFAULT_PASSWORD = "password";

    //Result code for delete button pressed
    public static final int RESULT_DELETE = 1;

    //Keys for bundle data
    public static final String MODE = "mode";
    public static final String STUDENT_ID = "studentID";
    public static final String STUDENT_NAME = "studentName";
    public static final String STUDENT_PATHWAY = "studentPathway";

    //Values for bundle data
    public static final String MODE_ADD = "add";
    public static final String MODE_EDIT = "edit";

    //Request codes for activities
    private static final int ADD_STUDENT = 500;
    private static final int EDIT_STUDENT = 501;
    private static final int VIEW_STUDENT = 502;

    //DPM_DBHandler for saving/loading
    private DPM_DBHandler dbHandler;
    //DBs to use for reading and writing respectively
    private SQLiteDatabase readableDB;
    private SQLiteDatabase writableDB;

    //SharedPrerenences used to load the password
    private SharedPreferences sharedPreferences;
    private String storedPassword;


    //List of students and access method
    private List<Student> studentList;
    private Student getStudentByID(int id)
    {
        for (Student student:studentList)
        {
            if (student.getStudentID() == id)
            {
                return student;
            }
        }
        return null;
    }

    //Initialize interfaces to pass to the recyclerview
    private onClickIDListener openEditStudentIntent = new onClickIDListener() {
        @Override
        public void onClick(View v, int id) {
            launchEditStudentIntent(id);
        }
    };
    private onClickIDListener openViewModulesIntent = new onClickIDListener() {
        @Override
        public void onClick(View v, int id){
            launchViewStudentModulesIntent(id);
        }
    };

    ConstraintLayout layoutDisclaimer;

    FrameLayout layoutAuthenticate;
    EditText fieldPassword;
    Button buttonAuthenticateAccept;
    Button buttonAuthenticateCancel;

    //Filter objects
    EditText filterName;
    EditText filterID;
    Spinner filterPathway;

    //List of students and its adapter
    RecyclerView listStudents;
    StudentsAdapter studentsAdapter;

    //'Add student' card under other students
    CardView cardAddStudent;

    //Footer buttons
    Button buttonAbout;
    Button buttonAuthenticate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students);

        //Since we've changed the theme here for the splash screen in the manifest, change it back here
        setTheme(R.style.AppTheme);

        //Load students from DB (or get placeholder data instead)
        dbHandler = new DPM_DBHandler(this, null);
        loadStudents();

        //Load password so we can check the user's getting it right
        sharedPreferences = this.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        storedPassword = sharedPreferences.getString(PREF_PASSWORD, DEFAULT_PASSWORD);

        //Layout of the disclaimer, included this way to stop us having an activity under the 'main' activity
        layoutDisclaimer = findViewById(R.id.layout_ViewStudents_disclaimer);

        layoutDisclaimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDisclaimer();
            }
        });

        //Objects in the log in screen
        layoutAuthenticate = findViewById(R.id.layout_ViewStudents_Authenticate);
        fieldPassword = findViewById(R.id.field_ViewStudents_Password);
        buttonAuthenticateAccept = findViewById(R.id.button_ViewStudents_AuthenticateAccept);
        buttonAuthenticateCancel = findViewById(R.id.button_ViewStudents_AuthenticateCancel);

        layoutAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideAuthenticate();
            }
        });
        buttonAuthenticateAccept.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkPassword();
            }
        });
        buttonAuthenticateCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideAuthenticate();
            }
        });

        //Objects in the filter section
        filterName = findViewById(R.id.field_ViewStudents_Name);
        filterID = findViewById(R.id.field_ViewStudents_ID);
        filterPathway = findViewById(R.id.spinner_ViewStudents_Pathway);

        ArrayAdapter filterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Pathway.values());
        filterPathway.setAdapter(filterAdapter);

        //List of students
        listStudents = findViewById(R.id.list_ViewStudents_Students);

        //Initialise the list of students
        studentsAdapter = new StudentsAdapter(studentList, openViewModulesIntent, openEditStudentIntent);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        listStudents.setAdapter(studentsAdapter);
        listStudents.setLayoutManager(layoutManager);

        //Add student card
        cardAddStudent = findViewById(R.id.card_ViewStudents_AddStudent);

        cardAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAddStudentIntent();
            }
        });

        //Footer buttons
        buttonAbout = findViewById(R.id.button_ViewStudents_About);
        buttonAuthenticate = findViewById(R.id.button_ViewStudents_Authenticate);

        buttonAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAboutIntent();
            }
        });
        buttonAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAuthenticate();
            }
        });
    }

    //Hide the disclaimer
    private void hideDisclaimer()
    {
        layoutDisclaimer.setVisibility(View.GONE);
    }

    //Check the password entered matches the stored password
    private void checkPassword()
    {
        String enteredPassword = fieldPassword.getText().toString();
        if (enteredPassword.equals(storedPassword))
        {
            launchSelectPathwayIntent();
            Toast.makeText(this, "Password Accepted", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this, "Incorrect Password", Toast.LENGTH_SHORT).show();
        }

    }

    //Show the authentication screen
    private void showAuthenticate(){
        layoutAuthenticate.setVisibility(View.VISIBLE);
    }

    //Hide the authentication screen
    private void hideAuthenticate(){
        fieldPassword.setText("");
        layoutAuthenticate.setVisibility(View.GONE);
    }

    //Load student information from the database
    private void loadStudents(){
        studentList = new ArrayList<Student>();
        List<Student> loadedStudents;

        readableDB = dbHandler.getReadableDatabase();
        loadedStudents = dbHandler.loadStudents(readableDB);
        studentList.addAll(loadedStudents);
        readableDB.close();

        studentList.add(new Student("Joe Bloggs", 12345678, Pathway.DATABASE));
        studentList.add(new Student("Joan Bloggly", 23456789, Pathway.MULTIMEDIA_WEB));
        studentList.add(new Student("Jobe Blag", 34567890, Pathway.SOFTWARE));
    }

    //Create a new entry in the students list and save it to the DB
    private void addStudent(int studentID, String studentName, Pathway pathway){
        Student newStudent = new Student(studentName, studentID, pathway);
        studentList.add(newStudent);

        //Update the list
        studentsAdapter.notifyItemChanged(studentList.indexOf(newStudent));

        //Update the DB
        writableDB = dbHandler.getWritableDatabase();
        dbHandler.addStudent(newStudent, writableDB);
        writableDB.close();
    }

    //Updates a student in the list and the DB
    private void updateStudent(int studentID, String studentName, Pathway pathway){
        Student targetStudent = getStudentByID(studentID);
        targetStudent.setStudentName(studentName);
        targetStudent.setPathway(pathway);

        //Update list
        studentsAdapter.notifyItemChanged(studentList.indexOf(targetStudent));

        //Update database
        writableDB = dbHandler.getWritableDatabase();
        dbHandler.updateStudent(targetStudent, writableDB);
        writableDB.close();
    }

    //Removes a student from the screen and the DB
    private void deleteStudent(int studentID)
    {
        Student targetStudent = getStudentByID(studentID);
        studentList.remove(getStudentByID(studentID));

        //Update the list
        studentsAdapter.notifyDataSetChanged();

        //Update the DB
        writableDB = dbHandler.getWritableDatabase();
        dbHandler.deleteStudent(targetStudent, writableDB);
        writableDB.close();
    }

    //Launch the launchEditStudentIntent activity to get information for a new student
    private void launchAddStudentIntent()
    {
        Intent i = new Intent(this, EditStudentActivity.class);
        i.putExtra(MODE, MODE_ADD);
        startActivityForResult(i, ADD_STUDENT);
    }

    //Launch the launchEditStudentIntent activity to edit an existing student
    private void launchEditStudentIntent(int studentID)
    {
        Student targetStudent = getStudentByID(studentID);
        Intent i = new Intent(this, EditStudentActivity.class);
        i.putExtra(MODE, MODE_EDIT);
        i.putExtra(STUDENT_ID, studentID);
        i.putExtra(STUDENT_NAME, targetStudent.getStudentName());
        i.putExtra(STUDENT_PATHWAY, targetStudent.getPathway().ordinal());
        startActivityForResult(i, EDIT_STUDENT);
    }

    //Launch the ViewModules activity to view a student's Modules
    private void launchViewStudentModulesIntent(int studentID)
    {
        Intent i = new Intent(this, ViewStudentModulesActivity.class);
        i.putExtra(STUDENT_ID, studentID);
        startActivityForResult(i, VIEW_STUDENT);
    }

    //Launch the About activity
    private void launchAboutIntent()
    {
        Intent i = new Intent(this, AboutActivity.class);
        startActivity(i);
    }

    //After authorization, select a pathway to edit (or the 'about' screen for more options)
    private void launchSelectPathwayIntent()
    {
        Intent i = new Intent(this, SelectPathwayActivity.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_STUDENT)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                int targetStudentID = data.getIntExtra(STUDENT_ID, 0);
                String targetStudentName = data.getStringExtra(STUDENT_NAME);
                Pathway targetPathway = Pathway.values()[data.getIntExtra(STUDENT_PATHWAY, 0)];
                addStudent(targetStudentID, targetStudentName, targetPathway);
            }
        }
        else if (requestCode == EDIT_STUDENT)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                int targetStudentID = data.getIntExtra(STUDENT_ID, 0);
                String targetStudentName = data.getStringExtra(STUDENT_NAME);
                Pathway targetPathway = Pathway.values()[data.getIntExtra(STUDENT_PATHWAY, 0)];
                updateStudent(targetStudentID, targetStudentName, targetPathway);
            }
            else if (resultCode == RESULT_DELETE)
            {
                int targetStudentID = data.getIntExtra(STUDENT_ID, 0);
                deleteStudent(targetStudentID);
            }
        }
    }
}
