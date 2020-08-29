package com.example.sangol.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    EditText matNo,passValue;
    Button loginButton;
    SQLiteDatabase db;
    String success = "Login Successfull";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                // TODO put your code here to respond to the button tap
                //Toast.makeText(getApplicationContext(), "About", Toast.LENGTH_LONG).show();
                StringBuffer buffer = new StringBuffer();
                buffer.append("Developer " +"Fred Sangol Uche" + "\n");
                buffer.append("Version: " + "V2.0" + "\n");
                buffer.append("Year: " + "2017" + "\n");
                Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
                return true;
           /** case R.id.action_administrator:
                Intent j=new Intent(this,AdminAddCourses.class);
                startActivity(j);
                return true;
            **/
            case R.id.action_exit:
            //System.exit(0);
                android.os.Process.killProcess(android.os.Process.myPid());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void login(){
        matNo = (EditText)findViewById(R.id.matNoView);
        passValue = (EditText)findViewById(R.id.pass);
        loginButton = (Button)findViewById(R.id.login);
        db=openOrCreateDatabase("Student_Register_Course", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS studentLogin (matNo INTEGER,fullname VARCHAR,pass VARCHAR,timestamp date);");


 /*       loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        });*/

    }

    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


    public void SignUpClick(View v)
    {
        Intent i=new Intent(this,signUp.class);
        //finish();
        startActivity(i);
    }


    public void validate(View v)
    {
        Intent i=new Intent(this,AdminAddStudentCodes.class);
        //finish();
        startActivity(i);
    }


    public void adminAdd(View v)
    {
        Intent i=new Intent(this,AdminAddCourses.class);
        //finish();
        startActivity(i);
    }



    public void validateLogin(View v)
    {
        // TODO Auto-generated method stub
        Cursor c=db.rawQuery("SELECT matNo,pass FROM studentLogin where matNo = '"+matNo.getText()+"' and pass='"+passValue.getText()+"'"
                , null);

        if(c.getCount()==0)
        {
            success="";
            showMessage("Error", "Username/Password is not correct!");
            return;
        }

        else {
            Intent i = new Intent(this, CodeValidation.class);
            matNo = (EditText) findViewById(R.id.matNoView);
            String matNoVal = matNo.getText().toString();

//Create the bundle
            Bundle bundle = new Bundle();

//Add your data to bundle
            bundle.putString("stuff", matNoVal);

//Add the bundle to the intent
            i.putExtras(bundle);

//Fire that second activity
     /*   i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);*/
            startActivity(i);
            //finish();
        }
    }






}
