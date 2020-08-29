package com.example.sangol.myapplication;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class DashboardActivity extends AppCompatActivity {

    Button validateBtn;
    String MatNoHolder;
    String[] MatNoHolderSplit;
    String StudentNameHolder;
    EditText matno, code;
    TextView welcomeStudent_;
    public static final String studentMatno = "";
    String finalResult;
    Boolean CheckEditText;
    ProgressDialog progressDialog;
    SQLiteDatabase db;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    ConnectionManager conManager = new ConnectionManager();
    String HttpURL = conManager.urlPath + "ValidateStudent.php";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        db = openOrCreateDatabase("Student_Register_Course", Context.MODE_PRIVATE, null);
        validateBtn = (Button) findViewById(R.id.button);
        //matno = (EditText)findViewById(R.id.matno);
        code = (EditText) findViewById(R.id.validateInput);
        welcomeStudent_ = (TextView) findViewById(R.id.welcomeStudent);

        Intent intent = getIntent();

        MatNoHolderSplit = intent.getStringExtra(StudentLoginActivity.studentMatno).split(",");
        MatNoHolder = MatNoHolderSplit[0];

        StudentNameHolder = MatNoHolderSplit[1];
        welcomeStudent_.setText("Welcome" + " " + StudentNameHolder);

        //Toast.makeText(DashboardActivity.this, "Matno: "+MatNoHolder, Toast.LENGTH_LONG).show();
        //Toast.makeText(DashboardActivity.this, "Name: "+StudentNameHolder, Toast.LENGTH_LONG).show();

        validateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StudentValidateFunction(MatNoHolder, code.getText().toString());

                //finish();

                //Intent intent = new Intent(DashboardActivity.this, StudentLoginActivity.class);

                // startActivity(intent);

                //Toast.makeText(DashboardActivity.this, "Log Out Successfully", Toast.LENGTH_LONG).show();


            }
        });
    }
    public void onBackPressed() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        MenuItem menusUpdate = menu.findItem(R.id.action_updateStudentCredentials);
        menusUpdate.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                // TODO put your code here to respond to the button tap
                String developerBuildInfo = HttpParse.getDeveloperInfo();
                //Toast.makeText(getApplicationContext(), developerBuildInfo, Toast.LENGTH_LONG).show();
                httpParse.showMessage("About",developerBuildInfo,this);
                return true;
            case R.id.action_logout:
                Intent i = new Intent(this, StudentLoginActivity.class);
                finish();
                startActivity(i);
                return true;
            case R.id.action_exit:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void StudentValidateFunction(final String matno, final String code) {

        class StudentValidateClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(DashboardActivity.this, "Validating Token..", null, true, true);
            }


            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);
                //Toast.makeText(DashboardActivity.this, httpResponseMsg, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

                if (conManager.isNetworkAvailable(getApplicationContext())) {
                if (httpResponseMsg.equalsIgnoreCase("Data Matched")) {
                    //Toast.makeText(DashboardActivity.this, "heyaaa", Toast.LENGTH_LONG).show();
                    finish();

                    Intent intent = new Intent(DashboardActivity.this, CourseRegister.class);
                    intent.putExtra(studentMatno, MatNoHolder);
                    startActivity(intent);

                } else {

                    //finish();
                    //httpParse.showMessage("Invalid Token", "Token is Invalid",DashboardActivity.this);
                    //progressDialog = ProgressDialog.show(DashboardActivity.this,"Validating Token..",null,true,true);
                    Toast.makeText(DashboardActivity.this, httpResponseMsg, Toast.LENGTH_LONG).show();
                }
                } else {httpParse.showMessage("Network","Connection is not Available",DashboardActivity.this);}
            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("matno", params[0]);
                hashMap.put("code", params[1]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        StudentValidateClass studentValidateClass = new StudentValidateClass();
        ConnectionManager conManager = new ConnectionManager();
            studentValidateClass.execute(matno, code);


    }



}