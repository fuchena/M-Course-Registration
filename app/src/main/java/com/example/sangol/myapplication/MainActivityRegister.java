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

public class MainActivityRegister extends AppCompatActivity {

    Button register, log_in;
    EditText matno, fullname, major, email,password,reTypePass;
    String matnoHolder, fullnameHolder, majorHolder, emailHolder,PasswordHolder,reTypePassHolder;
    String finalResult ;
    Boolean CheckEditText ;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    ConnectionManager conManager = new ConnectionManager();
    String HttpURL = conManager.urlPath+"StudentRegistration.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register);

        //Assign Id'S
        matno = (EditText)findViewById(R.id.editTextMatNo);
        fullname = (EditText)findViewById(R.id.editTextFullName);
        email = (EditText)findViewById(R.id.editTextEmail);
        major = (EditText)findViewById(R.id.editTextMajor);
        password = (EditText)findViewById(R.id.editTextPassword);
        reTypePass = (EditText)findViewById(R.id.Retypepassword);

        register = (Button)findViewById(R.id.Submit);
        log_in = (Button)findViewById(R.id.Login);

        //Adding Click Listener on button.
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Checking whether EditText is Empty or Not
                CheckEditTextIsEmptyOrNot();

                if(CheckEditText){

                    // If EditText is not empty and CheckEditText = True then this block will execute.
                   if(httpParse.checkPasswordMatch(PasswordHolder,reTypePassHolder)) {
                        StudentRegisterFunction(matnoHolder, fullnameHolder, majorHolder, emailHolder, PasswordHolder);
                    }
                    else { Toast.makeText(MainActivityRegister.this, "Please confirm passwords are the same", Toast.LENGTH_LONG).show();}

                }
                else {

                    // If EditText is empty then this block will execute .
                    Toast.makeText(MainActivityRegister.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();

                }

           //reset fields later...
            }
        });

        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivityRegister.this,StudentLoginActivity.class);
                startActivity(intent);

            }
        });

    }


    public void CheckEditTextIsEmptyOrNot(){

        matnoHolder = matno.getText().toString();
        fullnameHolder = fullname.getText().toString();
        majorHolder = major.getText().toString();
        emailHolder = email.getText().toString();
        PasswordHolder = password.getText().toString();
        reTypePassHolder = reTypePass.getText().toString();


        if(TextUtils.isEmpty(matnoHolder) || TextUtils.isEmpty(fullnameHolder) || TextUtils.isEmpty(majorHolder)
                || TextUtils.isEmpty(emailHolder) || TextUtils.isEmpty(PasswordHolder) || TextUtils.isEmpty(reTypePassHolder))
        {

            CheckEditText = false;

        }
        else {

            CheckEditText = true ;
        }

    }


    public void resetFields(){
        matno.setText("");
        fullname.setText("");
        major.setText("");
        email.setText("");
        password.setText("");
        reTypePass.setText("");
    }
    public void StudentRegisterFunction(final String matno, final String fullname, final String major,
                                        final String email,final String password){

        class StudentRegisterFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(MainActivityRegister.this,"Registration Process..",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(MainActivityRegister.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("matno",params[0]);
                hashMap.put("fullname",params[1]);
                hashMap.put("major",params[2]);
                hashMap.put("email",params[3]);
                hashMap.put("password",params[4]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        StudentRegisterFunctionClass studentRegisterFunctionClass = new StudentRegisterFunctionClass();

        studentRegisterFunctionClass.execute(matno,fullname,major,email,password);
        resetFields();
    }

}