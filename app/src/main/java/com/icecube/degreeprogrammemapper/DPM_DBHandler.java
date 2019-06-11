package com.icecube.degreeprogrammemapper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DPM_DBHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "DPM";

    //Note: Enum for pathways is stored in the DB with the following rule:
    //Base = 0
    //Networking = 1
    //Software = 2
    //Database = 3
    //Multimedia = 4

    private static final String TABLE_STUDENT = "student";
    private static final String STUDENT_ID = "student_id";
    private static final String STUDENT_NAME = "student_name";
    private static final String STUDENT_PATHWAY = "student_pathway";
    //-------------------------------------------------------------------
    private static final String TABLE_PATHWAY_MODULE = "pathway_module";
    private static final String PATHWAY_MODULE_CODE = "module_code";                //PK
    private static final String PATHWAY_ID = "pathway_id";  //ID of the pathway this module belongs to
    private static final String PATHWAY_MODULE_NAME = "module_name";
    private static final String PATHWAY_MODULE_CREDITS = "module_credits";
    private static final String PATHWAY_MODULE_STREAM = "module_stream";
    private static final String PATHWAY_MODULE_DESCRIPTION = "module_description";
    //-------------------------------------------------------------------
    private static final String TABLE_PATHWAY_MODULE_LINK = "pathway_module_link";
    private static final String PATHWAY_MODULE_LINK_ID = "link_id";
    private static final String PATHWAY_MODULE_LINK_REQUIREE = "requiree_code";
    private static final String PATHWAY_MODULE_LINK_REQUIRED = "required_code";
    //-------------------------------------------------------------------
    private static final String TABLE_STUDENT_MODULE = "student_module";
    private static final String STUDENT_MODULE_CODE = "module_code";                //PK
    //private static final String STUDENT_ID = "student_id";
    private static final String STUDENT_MODULE_NAME = "module_name";
    private static final String STUDENT_MODULE_CREDITS = "module_credits";
    private static final String STUDENT_MODULE_STREAM = "module_stream";
    private static final String STUDENT_MODULE_DESCRIPTION = "module_description";
    //-------------------------------------------------------------------
    private static final String TABLE_STUDENT_MODULE_LINK = "student_module_link";
    private static final String STUDENT_MODULE_LINK_ID = "link_id";
    private static final String STUDENT_MODULE_LINK_REQUIREE = "requiree_code";
    private static final String STUDENT_MODULE_LINK_REQUIRED = "required_code";


    public DPM_DBHandler(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, NAME, factory, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String studentQuery = String.format("CREATE TABLE %1$s (%2$s INTEGER PRIMARY KEY AUTOINCREMENT, %3$s TEXT, %4$s INTEGER);",
                TABLE_STUDENT, STUDENT_ID, STUDENT_NAME, STUDENT_PATHWAY);
        db.execSQL(studentQuery);

        String pathwayModuleQuery = String.format("CREATE TABLE %1$s (%2$s TEXT PRIMARY KEY, %3$s TEXT, %4$s INTEGER);",
                TABLE_PATHWAY_MODULE, PATHWAY_MODULE_CODE, PATHWAY_ID, PATHWAY_MODULE_NAME, PATHWAY_MODULE_CREDITS, PATHWAY_MODULE_STREAM, PATHWAY_MODULE_DESCRIPTION);
        db.execSQL(pathwayModuleQuery);

        String pathwayModuleLinkQuery = String.format("CREATE TABLE %1$s (%2$s INTEGER PRIMARY KEY AUTOINCREMENT, %3$s TEXT, %4$s INTEGER);",
                TABLE_PATHWAY_MODULE_LINK, PATHWAY_MODULE_LINK_ID, PATHWAY_MODULE_LINK_REQUIREE, PATHWAY_MODULE_LINK_REQUIRED);
        db.execSQL(pathwayModuleLinkQuery);

        String studentModuleQuery = String.format("CREATE TABLE %1$s (%2$s TEXT PRIMARY KEY, %3$s INTEGER, %4$s TEXT);",
                TABLE_STUDENT_MODULE, STUDENT_MODULE_CODE, STUDENT_ID, STUDENT_MODULE_NAME, STUDENT_MODULE_CREDITS, STUDENT_MODULE_STREAM, STUDENT_MODULE_DESCRIPTION);
        db.execSQL(studentModuleQuery);

        String studentModuleLinkQuery = String.format("CREATE TABLE %1$s (%2$s INTEGER PRIMARY KEY AUTOINCREMENT, %3$s TEXT, %4$s INTEGER);",
                TABLE_STUDENT_MODULE_LINK, STUDENT_MODULE_LINK_ID, STUDENT_MODULE_LINK_REQUIREE, STUDENT_MODULE_LINK_REQUIRED);
        db.execSQL(studentModuleLinkQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATHWAY_MODULE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATHWAY_MODULE_LINK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT_MODULE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT_MODULE_LINK);
        onCreate(db);
    }

    public List<Student> loadStudents(SQLiteDatabase readableDB)
    {
        List<Student> students = new ArrayList<Student>();
        Cursor cursorStudents = readableDB.query(TABLE_STUDENT, null, null, null, null, null, STUDENT_ID);
        cursorStudents.moveToFirst();
        while (!cursorStudents.isAfterLast())
        {
            int studentID = cursorStudents.getInt(cursorStudents.getColumnIndex(STUDENT_ID));
            String studentName = cursorStudents.getString(cursorStudents.getColumnIndex(STUDENT_NAME));
            int studentPathway = cursorStudents.getInt(cursorStudents.getColumnIndex(STUDENT_PATHWAY));
            cursorStudents.moveToNext();
        }
        return students;
    }

    public List<Integer> loadStudentIDs(SQLiteDatabase readableDB)
    {
        List<Integer> studentIDs = new ArrayList<Integer>();
        Cursor cursorIDs = readableDB.query(TABLE_STUDENT, new String[]{STUDENT_ID}, null, null, null, null, STUDENT_ID);
        cursorIDs.moveToFirst();
        while (!cursorIDs.isAfterLast())
        {
            studentIDs.add(cursorIDs.getInt(cursorIDs.getColumnIndex(STUDENT_ID)));
            cursorIDs.moveToNext();
        }
        return studentIDs;
    }

    public List<String> loadModuleCodes()
    {
        List<String> moduleCodes = new ArrayList<String>();

        return moduleCodes;
    }

    public void addStudent(Student student, SQLiteDatabase writableDB)
    {
        ContentValues studentValues = new ContentValues();
        studentValues.put(STUDENT_ID, student.getStudentID());
        studentValues.put(STUDENT_NAME, student.getStudentName());
        studentValues.put(STUDENT_PATHWAY, getPathwayInt(student.getPathway()));
        writableDB.insert(TABLE_STUDENT, null, studentValues);
    }

    public void updateStudent(Student student, SQLiteDatabase writableDB)
    {
        ContentValues studentValues = new ContentValues();
        studentValues.put(STUDENT_ID, student.getStudentID());
        studentValues.put(STUDENT_NAME, student.getStudentName());
        studentValues.put(STUDENT_PATHWAY, getPathwayInt(student.getPathway()));
        writableDB.update(TABLE_STUDENT, studentValues, STUDENT_ID + " LIKE ?", new String[]{String.valueOf(student.getStudentID())});
    }

    public void deleteStudent(Student student, SQLiteDatabase writableDB)
    {
        writableDB.delete(TABLE_STUDENT, STUDENT_ID + " LIKE ?", new String[]{String.valueOf(student.getStudentID())});
    }

    /**Converts from enum to a stable int
     * @param pathway Enum to convert to a stable int
     * @return Stable int representing Pathway enum
     */
    private Integer getPathwayInt(Pathway pathway)
    {
        Integer result = 0;
        switch (pathway)
        {
            case NETWORKING:
                result = 1;
                break;
            case SOFTWARE:
                result = 2;
                break;
            case DATABASE:
                result = 3;
                break;
            case MULTIMEDIA_WEB:
                result = 4;
                break;
        }
        return result;
    }

    private Pathway getIntPathway(int target)
    {
        Pathway result = Pathway.CORE;
        switch (target)
        {
            case 0:
                result = Pathway.CORE;
                break;
            case 1:
                result = Pathway.NETWORKING;
                break;
            case 2:
                result = Pathway.SOFTWARE;
                break;
            case 3:
                result = Pathway.DATABASE;
                break;
            case 4:
                result = Pathway.MULTIMEDIA_WEB;
                break;
        }
        return result;
    }
}
