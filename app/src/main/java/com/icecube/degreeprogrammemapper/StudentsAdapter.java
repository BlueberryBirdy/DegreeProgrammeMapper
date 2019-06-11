package com.icecube.degreeprogrammemapper;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.StudentsAdapterViewHolder>
{
    public class StudentsAdapterViewHolder extends RecyclerView.ViewHolder
    {
        TextView studentName;
        TextView studentID;
        TextView studentDegree;
        TextView studentPathway;
        CardView studentCard;

        public StudentsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.label_StudentRow_Name);
            studentID = itemView.findViewById(R.id.label_StudentRow_ID);
            studentDegree = itemView.findViewById(R.id.label_StudentRow_Degree);
            studentPathway = itemView.findViewById(R.id.label_StudentRow_Pathway);
            studentCard = itemView.findViewById(R.id.card_StudentRow);
        }
    }

    List<Student> studentList;
    onClickIDListener click;
    onClickIDListener longClick;

    public StudentsAdapter(List<Student> studentList, onClickIDListener click, onClickIDListener longClick) {
        this.studentList = studentList;
        this.click = click;
        this.longClick = longClick;
    }

    @NonNull
    @Override
    public StudentsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View StudentView;
        StudentView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_student, viewGroup, false);
        return new StudentsAdapterViewHolder(StudentView);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentsAdapterViewHolder viewHolder, int i) {
        final Student curStudent = studentList.get(i);
        viewHolder.studentName.setText(curStudent.getStudentName());
        viewHolder.studentID.setText(String.valueOf(curStudent.getStudentID()));
        //viewHolder.studentDegree.setText(curStudent.getDegree());
        viewHolder.studentPathway.setText(curStudent.getPathway().toString());
        viewHolder.studentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.onClick(v, curStudent.getStudentID());
            }
        });
        viewHolder.studentCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longClick.onClick(v, curStudent.getStudentID());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

}
