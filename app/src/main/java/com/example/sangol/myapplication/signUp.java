package com.example.sangol.myapplication;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class signUp extends AppCompatActivity {
    EditText matNo,fullName,pass;
    Button signUpButton;
    SQLiteDatabase db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void signUpMain(View v){

        matNo = (EditText)findViewById(R.id.matNoView);
        fullName = (EditText)findViewById(R.id.welcome);
        pass = (EditText)findViewById(R.id.pass);
        signUpButton = (Button)findViewById(R.id.signUp);

        db=openOrCreateDatabase("Student_Register_Course", Context.MODE_PRIVATE, null);
        //db.execSQL("DROP TABLE IF EXISTS studentLogin");
        db.execSQL("CREATE TABLE IF NOT EXISTS studentLogin (matNo INTEGER,fullname VARCHAR,pass VARCHAR,timestamp datetime);");
        if(matNo.getText().toString().trim().length()==0||
                fullName.getText().toString().trim().length()==0||
                pass.getText().toString().trim().length()==0)
        {
            showMessage("Error", "Please enter all values");
            return;
        }
        db.execSQL("INSERT INTO studentLogin VALUES('"+matNo.getText()+"','"+fullName.getText()+
                "','"+pass.getText()+"',CURRENT_TIMESTAMP);");
        //showMessage("Success", "Record added successfully");
        Intent i = new Intent(this, Login.class);
        finish();
        startActivity(i);
        clearText();
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
        matNo.setText("");
        fullName.setText("");
        pass.setText("");
        matNo.requestFocus();
    }
}
