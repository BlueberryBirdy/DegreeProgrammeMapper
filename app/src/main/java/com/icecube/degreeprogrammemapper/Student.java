package com.icecube.degreeprogrammemapper;

public class Student {
    private String studentName;
    public String getStudentName()
    {
        return studentName;
    }
    public void setStudentName(String studentName)
    {
        this.studentName = studentName;
    }

    private int studentID;
    public int getStudentID()
    {
        return studentID;
    }

    private Pathway pathway;
    public Pathway getPathway()
    {
        return pathway;
    }
    public void setPathway(Pathway pathway)
    {
        this.pathway = pathway;
    }

//    private String degree;
//    public String getDegree()
//    {
//        return degree;
//    }

//    public Student(String studentName, int studentID, Pathway pathway, String degree) {
//        this.studentName = studentName;
//        this.studentID = studentID;
//        this.pathway = pathway;
//        this.degree = degree;
//    }

    public Student(String studentName, int studentID, Pathway pathway) {
        this.studentName = studentName;
        this.studentID = studentID;
        this.pathway = pathway;
    }
}
