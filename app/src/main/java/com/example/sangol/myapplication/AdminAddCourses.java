package com.example.sangol.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AdminAddCourses extends AppCompatActivity {
    EditText courseCode;
    EditText courseName;
    EditText creditHours;
    EditText lecturerName;
    EditText website;
    Button addCourseButton;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_courses);
        db=openOrCreateDatabase("Student_Register_Course", Context.MODE_PRIVATE, null);
        addCourse();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                Intent i = new Intent(this, Login.class);
                finish();
                startActivity(i);
                return true;
            case R.id.action_exit:
                android.os.Process.killProcess(android.os.Process.myPid());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void addCourse(){
        courseCode = (EditText)findViewById(R.id.courseCode);
        courseName = (EditText)findViewById(R.id.courseName);
        creditHours= (EditText)findViewById(R.id.creditHours);
        lecturerName= (EditText)findViewById(R.id.lecturerName);
        website= (EditText)findViewById(R.id.website);
        addCourseButton = (Button)findViewById(R.id.AddCoursebtn);

        //db.execSQL("DROP TABLE IF EXISTS courses");
        db.execSQL("CREATE TABLE IF NOT EXISTS courses (courseCode VARCHAR,courseName VARCHAR,creditHours VARCHAR,lecturerName VARCHAR,website VARCHAR,timestamp datetime);");

        addCourseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(courseCode.getText().toString().trim().length()==0||
                        courseName.getText().toString().trim().length()==0||
                        creditHours.getText().toString().trim().length()==0 ||
                        website.getText().toString().trim().length()==0)
                {
                    showMessage("Error", "Please enter all values");
                    return;
                }


                db.execSQL("INSERT INTO courses VALUES('"+courseCode.getText()+"','"+courseName.getText()+
                        "','"+creditHours.getText()+"','"+lecturerName.getText()+"','"+website.getText()+"',CURRENT_TIMESTAMP);");
                showMessage("Success", "Record added successfully");
                clearText();
            }
        });

    }

    public void addStudentCodesMthd(View v){

        Intent i = new Intent(this, AdminAddStudentCodes.class);
        //finish();
        clearText();
        startActivity(i);

    }


    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void clearText()
    {
        courseCode.setText("");
        courseName.setText("");
        lecturerName.setText("");
        creditHours.setText("");
        website.setText("");
        courseCode.requestFocus();
    }



}


