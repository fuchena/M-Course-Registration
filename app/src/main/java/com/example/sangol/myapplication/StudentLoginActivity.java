package com.example.sangol.myapplication;
import android.app.Notification;
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
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class StudentLoginActivity extends AppCompatActivity {

    EditText matno, Password;
    Button LogIn ;
    String matnoHolder, passwordHolder;
    String finalResult ;
    ConnectionManager conManager = new ConnectionManager();
    //String HttpURL = conManager.urlPath+"StudentLogin.php";

    String HttpURL = conManager.urlPath+"StudentLogin.php";
    Boolean CheckEditText ;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    public static final String studentMatno = "";
    public static final String studentName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        matno = (EditText)findViewById(R.id.matno);
        Password = (EditText)findViewById(R.id.password);
        LogIn = (Button)findViewById(R.id.Login);

        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckEditTextIsEmptyOrNot();

                if(CheckEditText){

                    if (conManager.isNetworkAvailable(getApplicationContext())) {
                        StudentLoginFunction(matnoHolder, passwordHolder);
                    }
                    else{ Toast.makeText(StudentLoginActivity.this, "Please check network connectivity", Toast.LENGTH_LONG).show();}
                }
                else {

                    Toast.makeText(StudentLoginActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();

                }

            }
        });
    }


    public void onBackPressed() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        MenuItem menusLogout = menu.findItem(R.id.action_logout);
        MenuItem menusUpdate = menu.findItem(R.id.action_updateStudentCredentials);
        menusLogout.setVisible(false);
        menusUpdate.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                // TODO put your code here to respond to the button tap
                //Toast.makeText(getApplicationContext(), "About", Toast.LENGTH_LONG).show();
                String developerBuildInfo = HttpParse.getDeveloperInfo();
                //Toast.makeText(getApplicationContext(),developerBuildInfo, Toast.LENGTH_LONG).show();
                httpParse.showMessage("About",developerBuildInfo,this);
                return true;
            case R.id.action_logout:
                Intent i = new Intent(this, StudentLoginActivity.class);
                finish();
                startActivity(i);
                return true;
          /**  case R.id.action_administrator:
                Intent j = new Intent(this, CourseRegister.class);
                startActivity(j);
                return true;
           **/
            case R.id.action_exit:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void CheckEditTextIsEmptyOrNot(){

        matnoHolder = matno.getText().toString();
        passwordHolder = Password.getText().toString();

        if(TextUtils.isEmpty(matnoHolder) || TextUtils.isEmpty(passwordHolder))
        {
            CheckEditText = false;
        }
        else {

            CheckEditText = true ;
        }
    }

    public void StudentLoginFunction(final String matno, final String password){

        class StudentLoginClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(StudentLoginActivity.this,"Login Process..",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                try {

                    Toast.makeText(StudentLoginActivity.this, httpResponseMsg, Toast.LENGTH_LONG).show();
                    if (httpResponseMsg.equals("<br />"))
                        httpResponseMsg = "0";

                if(!httpResponseMsg.equalsIgnoreCase("0")) {

                   if (!httpResponseMsg.equalsIgnoreCase("Something Went Wrong")) {
                       finish();

                       Intent intent = new Intent(StudentLoginActivity.this, DashboardActivity.class);
                       intent.putExtra(studentMatno, matno + "," + httpResponseMsg);
                       //intent.putExtra(studentName, httpResponseMsg);
                       startActivity(intent);
                   }
                   else{ Toast.makeText(StudentLoginActivity.this, "Cannot Connect to Server", Toast.LENGTH_LONG).show();}

                    }

                    else{

                    Toast.makeText(StudentLoginActivity.this, "Incorrect username or password", Toast.LENGTH_LONG).show();
                    }

            }

                catch(Exception ex){ Toast.makeText(StudentLoginActivity.this, ex.getMessage() + " " + "Exception!", Toast.LENGTH_LONG).show();}

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("matno",params[0]);

                hashMap.put("password",params[1]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);
                return finalResult;
            }
        }

        StudentLoginClass studentLoginClass = new StudentLoginClass();

        studentLoginClass.execute(matno,password);
    }


}