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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CodeValidation extends AppCompatActivity {
    SQLiteDatabase db;
    TextView welcome;
    String success="";
    String fName="";
    String matNoText="";
    EditText codeValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_validation);
        db=openOrCreateDatabase("Student_Register_Course", Context.MODE_PRIVATE, null);
        Bundle bundle = getIntent().getExtras();
        welcome = (TextView)findViewById(R.id.welcome);
//Extract the data…

        matNoText = bundle.getString("stuff");
        fName = getFullName(matNoText);
        welcome.setText("Hello " + fName+"!");

        db=openOrCreateDatabase("Student_Register_Course", Context.MODE_PRIVATE, null);


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
                String developerBuildInfo = HttpParse.getDeveloperInfo();
                Toast.makeText(getApplicationContext(),developerBuildInfo, Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_logout:
                Intent i = new Intent(this, StudentLoginActivity.class);
                finish();
                startActivity(i);
                return true;
            case R.id.action_exit:
                //System.exit(0);
                android.os.Process.killProcess(android.os.Process.myPid());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    public String getFullName(String matNo) {
        // TODO Auto-generated method stub
        String matNoReturn = "";

        try {
            Cursor c = db.rawQuery("SELECT fullname FROM " +
                            "studentLogin where matNo= '" + matNo + "'"
                    , null);
            while (c.moveToNext()) {
                matNoReturn = c.getString(0).toString();

            }

        } catch (Exception ex) {
            showMessage("Exception", ex.toString());
        }

        return matNoReturn;
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


    public void validateCodeMthd(View v){
      codeValue = (EditText)findViewById(R.id.codeValue);
        // TODO Auto-generated method stub
        Cursor c=db.rawQuery("SELECT codeValue FROM studentCodeValidation where matNo = '"+matNoText+"' and codeValue='"+codeValue.getText()+"'"
                , null);

        if(c.getCount()==0)
        {
            showMessage("Error", "Pin Code not Valid");
            return;
        }
        else{

            Intent i = new Intent(this, CourseRegister.class);
            String matNoVal=matNoText;
            Bundle bundle = new Bundle();
            bundle.putString("MatNo", matNoVal);
            i.putExtras(bundle);
            finish();
            startActivity(i);

        }




    }
}

