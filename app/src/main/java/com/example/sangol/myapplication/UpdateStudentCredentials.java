package com.example.sangol.myapplication;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class UpdateStudentCredentials extends AppCompatActivity {
    ProgressDialog progressDialog;
    String finalResult ;
    HashMap<String,String> hashMap = new HashMap<>();
    EditText matno, pass,reTypePass,major;
    Button UpdateStudent;
    String matnoHolderPass;
    HttpParse httpParse = new HttpParse();
    ConnectionManager conManager = new ConnectionManager();
    String HttpURL = conManager.urlPath+"UpdateStudentCredentials.php";
    String matnoHolder, passHolder,reTypePassHolder,majorHolder;
    Boolean CheckEditText ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_student_credentials);

        matno = (EditText)findViewById(R.id.matno);
        pass = (EditText)findViewById(R.id.password);
        major = (EditText)findViewById(R.id.major);
        reTypePass = (EditText)findViewById(R.id.reTypepassword);


        UpdateStudent = (Button)findViewById(R.id.UpdateButton);

        // Receive matno Send by previous Dashboard Activity.
        Intent intent = getIntent();
        matnoHolderPass = intent.getStringExtra(CourseRegister.studentMatno);

        //matnoHolderPass = getIntent().getStringExtra("studentMatno");


        // Setting Received matno  into EditText.
        matno.setText(matnoHolderPass);

        // Adding click listener to update button .
        UpdateStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckEditTextIsEmptyOrNot();
                  if (CheckEditText){
                    if (httpParse.checkPasswordMatch(passHolder, reTypePassHolder)) {

                        //Toast.makeText(getApplicationContext(), "Major: " + majorHolder, Toast.LENGTH_LONG).show();
                        // Sending Student matno, password, major to method to update on server.
                        StudentRecordUpdate(matnoHolderPass, matnoHolder, majorHolder, passHolder);

                    } else {
                        Toast.makeText(UpdateStudentCredentials.this, "Please confirm passwords are the same", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                else{ Toast.makeText(UpdateStudentCredentials.this, "Please fill all form fields.", Toast.LENGTH_LONG).show(); return;}
                redirectLogin();
            }
        });


    }


    public void redirectLogin(){
        Intent i = new Intent(this, StudentLoginActivity.class);
        finish();
        startActivity(i);
    }

    public void CheckEditTextIsEmptyOrNot() {

        matnoHolder = matno.getText().toString();
        passHolder = pass.getText().toString();
        majorHolder = major.getText().toString();
        reTypePassHolder = reTypePass.getText().toString();


        if(TextUtils.isEmpty(passHolder) || TextUtils.isEmpty(majorHolder)|| TextUtils.isEmpty(reTypePassHolder) )

            {
                CheckEditText = false;
            }
        else {

                CheckEditText = true ;
            }
    }


    public void resetFields(){
        matno.setText("");
        pass.setText("");
        major.setText("");
        reTypePass.setText("");
    }

    // Method to Update Student Record.
    public void StudentRecordUpdate(final String matnoHolderPass, final String matno,final String major,final String pass){

        class StudentRecordUpdateClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(UpdateStudentCredentials.this,"Updating...",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(UpdateStudentCredentials.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("matnoHolderPass",params[0]);
                hashMap.put("matno",params[1]);
                hashMap.put("major",params[2]);
                hashMap.put("pass",params[3]);
                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        StudentRecordUpdateClass studentRecordUpdateClass = new StudentRecordUpdateClass();
        studentRecordUpdateClass.execute(matnoHolderPass,matno,major,pass);
        resetFields();
    }


}