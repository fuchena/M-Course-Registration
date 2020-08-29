package com.example.sangol.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AdminAddStudentCodes extends AppCompatActivity {
    EditText matNoVal;
    EditText codeVal;
    SQLiteDatabase db;
    Button codeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_admin_add_student_codes);
            db=openOrCreateDatabase("Student_Register_Course", Context.MODE_PRIVATE, null);
            addCode();

    }


    public void addCode() {

        try {

            matNoVal = (EditText) findViewById(R.id.matNo);
            codeVal = (EditText) findViewById(R.id.code);
            codeBtn = (Button) findViewById(R.id.codeBtn);

            //db.execSQL("DROP TABLE IF EXISTS studentCodeValidation");
            db.execSQL("CREATE TABLE IF NOT EXISTS studentCodeValidation (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "matNo VARCHAR,codeValue VARCHAR,timestamp datetime);");

            codeBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    // TODO Auto-generated method stub
                    if (matNoVal.getText().toString().trim().length() == 0 ||
                            codeVal.getText().toString().trim().length() == 0) {
                        showMessage("Error", "Please enter all values");
                        return;
                    }
                    db.execSQL("INSERT INTO studentCodeValidation (matNo,codeValue,timestamp) VALUES ('" + matNoVal.getText().toString()+ "','" + codeVal.getText().toString() + "',CURRENT_TIMESTAMP);");
                    showMessage("Success", "Record added successfully");
                    clearText();

                }
            });
        }

        catch (Exception ex) {
            showMessage("Exception", ex.toString());
        }

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
        matNoVal.setText("");
        codeVal.setText("");
        matNoVal.requestFocus();
    }
}
