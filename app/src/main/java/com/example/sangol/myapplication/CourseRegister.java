package com.example.sangol.myapplication;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Timer;
import java.util.logging.Handler;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CourseRegister extends AppCompatActivity {
    String studentId;
    Spinner spinnerSelectCourse;
    Spinner spinnerSelectSchool;
    Spinner spinnerSelectProgram;
    TextView creditHours;
    TextView lecturer;
    SQLiteDatabase db;
    String MatNoHolder;
    String courseId="";
    private static ListView listView;
    public static final String CourseNameArray = "cName";
    public static final String creditHour  = "creditHours";
    public static final String lecturerName = "lecturer";
    public static final String JSON_ARRAY = "result";
    private JSONArray result;
    HttpParse httpParse = new HttpParse();
    ConnectionManager conManager = new ConnectionManager();
    String value="";
    public static final String studentMatno = "";
    ArrayAdapter<String> adapter;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    String HttpURL = conManager.urlPath+"CourseReg.php";
    String finalResult;
    ProgressBar Bar;
    String codes="";
    long regDaysLeft=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_register);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        db = openOrCreateDatabase("Student_Register_Course", Context.MODE_PRIVATE, null);

        Intent intent = getIntent();
        MatNoHolder = intent.getStringExtra(DashboardActivity.studentMatno);

        creditHours = (TextView) findViewById(R.id.creditHours);
        lecturer = (TextView) findViewById(R.id.lecturerName);
        spinnerSelectCourse = (Spinner) findViewById(R.id.spinnerCourse);
        spinnerSelectSchool = (Spinner) findViewById(R.id.spinnerSchool);
        spinnerSelectProgram = (Spinner) findViewById(R.id.spinnerProgram);

        dropTables();
        createTables();
        getCourseDataSchool();
        getCourseDataProgram();
        getCourseDataCourse();
        spinnerSelectSchoolMethod();
        spinnerSelectProgramMethod();
        spinnerSelectCourseMethod();
        delayTime();


        //getStudentId();

        //Bar = (ProgressBar)findViewById(R.id.progressBar1);
        //Bar.setVisibility(View.VISIBLE); //normally set to invisible

        //matNo = (TextView) findViewById(R.id.matNoView);
        ;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);



    }

    public void onBackPressed() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                // TODO put your code here to respond to the button tap
                //Toast.makeText(getApplicationContext(), "About", Toast.LENGTH_LONG).show();
                String developerBuildInfo = HttpParse.getDeveloperInfo();
                httpParse.showMessage("About",developerBuildInfo,this);
                return true;

            case R.id.action_updateStudentCredentials:
                Intent intent = new Intent(this, UpdateStudentCredentials.class);
                intent.putExtra(studentMatno,MatNoHolder);
                finish();
                startActivity(intent);
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






    public void spinnerSelectSchoolMethod() {

        try {

            //clearText();


            spinnerSelectSchool.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    //getCourseDataProgram(parentView.getItemAtPosition(position).toString());
                    //httpParse.showMessage("Value:", parentView.getItemAtPosition(position).toString(),CourseRegister.this);
                    getProgramListSpinner(position);
                    //httpParse.showMessage("Value:", parentView.getItemAtPosition(position).toString(),CourseRegister.this);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here

                    // creditHours.setText("");
                    //lecturer.setText("");
                }

            });
        } catch (Exception e) {
            httpParse.showMessage("Error:", e.toString(),CourseRegister.this);
        }
    }


    public void spinnerSelectProgramMethod() {

        try {

            spinnerSelectProgram.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    getCourseListSpinner(parentView.getItemAtPosition(position).toString());

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {

                }

            });
        } catch (Exception e) {
            httpParse.showMessage("Error:", e.toString(),CourseRegister.this);
        }
    }


    public void spinnerSelectCourseMethod() {

        try {


            spinnerSelectCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    getCreditHour_Lecturer(parentView.getItemAtPosition(position).toString());

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here

                    // creditHours.setText("");
                    //lecturer.setText("");
                }

            });
        } catch (Exception e) {
            httpParse.showMessage("Error:", e.toString(),CourseRegister.this);
        }
    }



    public void dropTables(){
        db.execSQL("DROP TABLE IF EXISTS schools");
        db.execSQL("DROP TABLE IF EXISTS programs");
        db.execSQL("DROP TABLE IF EXISTS coursesN");

    }


      public void createTables(){
        db.execSQL("CREATE TABLE IF NOT EXISTS schools (id INTEGER PRIMARY KEY,name VARCHAR);");
        db.execSQL("CREATE TABLE IF NOT EXISTS programs (id INTEGER PRIMARY KEY,name VARCHAR,school_id INTEGER);");
        db.execSQL("CREATE TABLE IF NOT EXISTS coursesN (id INTEGER PRIMARY KEY,programName VARCHAR,courseCode VARCHAR,courseName VARCHAR,creditHours VARCHAR,Lecturer VARCHAR,end_date DATE);");

    }

    public void deleteTableSchools(){
        db.execSQL("DELETE FROM schools");
    }


    public void deleteTablePrograms(){
        db.execSQL("DELETE FROM programs");
    }


    public void deleteTableCourses(){;
        db.execSQL("DELETE FROM coursesN");
    }




    public void insertIntoSchool(JSONArray j)
    {
        deleteTableSchools();
        String id="";
        String name="";
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject json = j.getJSONObject(i);
                //json.getString("school")
                id = json.getString("id");
                name = json.getString("name");
                db.execSQL("INSERT INTO schools (id,name) VALUES('" +id+ "', '" +name+ "');");
            } catch (JSONException e) {
                httpParse.showMessage("Exception CourseDetails", e.toString(),this);
                e.printStackTrace();
            }
            //Toast.makeText(getApplicationContext(), "insertedSchool "+name, Toast.LENGTH_LONG).show();
        }

        getSchoolListSpinner();
    }





    public void insertIntoProgram(JSONArray j)
    {

        deleteTablePrograms();
        String name = "";
        String id="";
        String schoolId="";
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject json = j.getJSONObject(i);
                id = json.getString("id");
                name = json.getString("name");
                schoolId = json.getString("school_id");
                db.execSQL("INSERT INTO programs (id,name,school_id) VALUES('" +id+ "', '" +name+ "', '" +schoolId+ "');");
            } catch (JSONException e) {
                httpParse.showMessage("Exception CourseDetails", e.toString(),this);
                e.printStackTrace();
            }
            //Toast.makeText(getApplicationContext(), "insertedProgram "+name, Toast.LENGTH_LONG).show();
        }
        //getProgramListSpinner();
    }


    public void insertIntoCourses(JSONArray j)
    {


        deleteTableCourses();
        String id="";
        String programName="";
        String courseCode="";
        String creditHours="";
        String courseName = "";
        String lecturer="";
        String end_date="";
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject json = j.getJSONObject(i);
                id = json.getString("id");
                programName = json.getString("program");
                courseCode = json.getString("code");
                courseName = json.getString("cName");
                creditHours = json.getString("creditHours");
                lecturer = json.getString("lecturer");
                end_date = json.getString("end_date");
                db.execSQL("INSERT INTO coursesN (id,programName,courseCode,courseName,creditHours,lecturer,end_date) VALUES('" +id+ "', '" +programName+ "', '" +courseCode+ "', '" +courseName+ "', '" +creditHours+ "', '" +lecturer+ "', '" +end_date+ "');");
            } catch (JSONException e) {
                httpParse.showMessage("Exception CourseDetails", e.toString(),this);
                e.printStackTrace();
            }

            //Toast.makeText(getApplicationContext(), "insertedCourse "+courseName, Toast.LENGTH_LONG).show();
        }
        //();

    }



    public void registerCourse(View v) {
        //showMessage("matNo ",matNo);
        try {

            //db.execSQL("DROP TABLE IF EXISTS courseregister");
            //db.execSQL("DELETE FROM courses");
            //db.execSQL("DELETE FROM courseregister");
            db.execSQL("CREATE TABLE IF NOT EXISTS courseregister (id INTEGER PRIMARY KEY AUTOINCREMENT,MatNo VARCHAR,CourseCodeName VARCHAR,CreditHours VARCHAR,Lecturer VARCHAR,timestamp datetime,isSync INTEGER);");
            creditHours = (TextView) findViewById(R.id.creditHours);
            String selectCourse = spinnerSelectCourse.getSelectedItem().toString();
            //CodeValidation cVal = new CodeValidation();

            int count = totalCourses();

            // if (isInternetWorking()) {
            if (selectCourse != "Select Course") {
                if (!checkIfCourseExist(spinnerSelectCourse.getSelectedItem().toString(),MatNoHolder)) {
                    if (count <= 5) {
                        db.execSQL("INSERT INTO courseregister (MatNo, CourseCodeName,CreditHours,Lecturer,timestamp,isSync) VALUES('" + MatNoHolder+ "', '" + spinnerSelectCourse.getSelectedItem().toString() + "'" +
                                ",'" + creditHours.getText() + "','" + lecturer.getText() + "',CURRENT_TIMESTAMP,0);");
                        //showMessage("Success", "Record added successfully");

                    } else {
                        httpParse.showMessage("Error", "Maximum of 6 Courses Allowed",CourseRegister.this);
                    }
                } else {
                    httpParse.showMessage("Error", "Course Already Registered",CourseRegister.this);
                }

            } else {
                httpParse.showMessage("Error", "Please select course",CourseRegister.this);
            }
            //}
            // else{showMessage("Error", "Please make sure internet is connected");}
        } catch (Exception ex) {
            httpParse.showMessage("Exception", ex.toString(),CourseRegister.this);
        }

        clearText();
        listView();

        //deleteTableSchools();
        //deleteTablePrograms();
        //deleteTableCourses();
    }




    public ArrayList<String> getCourseList() {
        ArrayList<String> array1 = new ArrayList<String>();
        try {
            Cursor c = db.rawQuery("SELECT CourseCodeName FROM courseregister where matNo = '" + MatNoHolder + "'", null);

            while (c.moveToNext()) {
                array1.add(c.getString(0));
            }

            //clearText();
        } catch (Exception ex) {
            httpParse.showMessage("Exception getCourseList", ex.toString(),CourseRegister.this);
        }

        return array1;
    }



    public void getSchoolListSpinner() {
        ArrayList<String> schoolList = new ArrayList<String>();
        schoolList.add("Select School");

        try {
            Cursor c = db.rawQuery("SELECT name FROM schools", null);

            while (c.moveToNext()) {
                schoolList.add(c.getString(0));
            }

            //clearText();
        } catch (Exception ex) {
            httpParse.showMessage("Exception getSchoolList", ex.toString(),CourseRegister.this);
        }

        spinnerSelectSchool.setAdapter(new ArrayAdapter<String>(CourseRegister.this, android.R.layout.simple_spinner_dropdown_item, schoolList));

    }




    public void getProgramListSpinner(int id) {
        ArrayList<String> programList = new ArrayList<String>();
        programList.add("Select Program");
        try {
            Cursor c = db.rawQuery("SELECT name FROM programs where school_id = '"+id+"'", null);

            while (c.moveToNext()) {
                programList.add(c.getString(0));
            }

            //clearText();
        } catch (Exception ex) {
            httpParse.showMessage("Exception getProgramList", ex.toString(),CourseRegister.this);
        }

        spinnerSelectProgram.setAdapter(new ArrayAdapter<String>(CourseRegister.this, android.R.layout.simple_spinner_dropdown_item, programList));

    }



    public void getCourseListSpinner(String programName) {
        ArrayList<String> courseList = new ArrayList<String>();
        courseList.add("Select Course");
        //Toast.makeText(getApplicationContext(), "SelectedCourses2Begin"+value, Toast.LENGTH_LONG).show();
        String value ="";
        try {
            Cursor c = db.rawQuery("SELECT courseCode, courseName FROM coursesN where programName = '" + programName+ "'", null);

            while (c.moveToNext()) {
                courseList.add(c.getString(0)+" "+c.getString(1));
                //Toast.makeText(getApplicationContext(), "SelectedCourses "+c.getString(0), Toast.LENGTH_LONG).show();
                value = c.getString(0);
            }

            //clearText();
        } catch (Exception ex) {
            httpParse.showMessage("Exception getCourseList", ex.toString(),CourseRegister.this);


        }

        //Toast.makeText(getApplicationContext(), "SelectedCourses2"+value, Toast.LENGTH_LONG).show();
        spinnerSelectCourse.setAdapter(new ArrayAdapter<String>(CourseRegister.this, android.R.layout.simple_spinner_dropdown_item, courseList));

    }


    public void getListView (){

        ArrayList<String> arrayGet = getCourseList();
        listView = (ListView) findViewById(R.id.listViewCourses);
        adapter = new ArrayAdapter<String>(this, R.layout.listcourses, arrayGet);
        listView.setAdapter(adapter);
    }

    public void listView() {
        //String[] CourseNames = new String[]{"Test1", "Test2", "Test3", "Test4"};
        getListView ();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    value = (String) listView.getItemAtPosition(position);

                    try {

                        AlertDialog.Builder alert = new AlertDialog.Builder(CourseRegister.this);
                        alert.setTitle("Delete Registered Course");
                        alert.setMessage("Are you sure you want to delete?");
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.execSQL("DELETE FROM courseregister where courseCodeName = '" + value + "'");
                                getListView();
                            }
                        });

                        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        alert.show();
                    } catch (Exception e) {
                        httpParse.showMessage("Error", e.toString(), CourseRegister.this);
                    }
                } catch (Exception e) {
                    httpParse.showMessage("Error", e.toString(),CourseRegister.this);
                }

            }
        });
    }



    public void syncing(View v) {
        //final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar1);
        //progressBar.setVisibility(View.VISIBLE);
        ConnectionManager conManager = new ConnectionManager();
        try {
            if (conManager.isNetworkAvailable(getApplicationContext())) {
                //the insert function contains the classes and methods to sent data
                // to the server asynchronously
               if (exceedRegistrationDeadline()==true) {
                   sync();
               }
               else {httpParse.showMessage("Registration Closed!","Sorry Registration has closed",CourseRegister.this);}

            }
            else{httpParse.showMessage("Network","Connection is not Available",CourseRegister.this);}
        }


        catch (Exception e) {
            httpParse.showMessage("Error", e.toString(),CourseRegister.this);
        }
    }
    public int totalCourses() {

        int count = 0;
        try {
            Cursor c = db.rawQuery("SELECT CourseCodeName FROM courseregister where matNo = '" + MatNoHolder + "'", null);

            while (c.moveToNext()) {
                count++;
            }

            //clearText();
        } catch (Exception ex) {
            httpParse.showMessage("Exception getCourseList", ex.toString(),CourseRegister.this);
        }

        return count;
    }

    public void clearText() {
        Spinner dropdown = (Spinner) findViewById(R.id.spinnerCourse);
        dropdown.setSelection(0);
        creditHours.setText("");
        lecturer.setText("");
        /**
        spinnerSelectCourse.requestFocus();
**/
    }

    public void updateSync(String matNo, String courseCodeName) {
        try {
            db.execSQL("UPDATE courseregister SET isSync=1 WHERE matNo='" + matNo + "' and courseCodeName='" + courseCodeName + "'");
            //showMessage("Success", "Update Successful");
        }
        catch(Exception e){httpParse.showMessage("Exception",e.toString(),CourseRegister.this);}

    }


    public void emptyDB() {
        try {
            db.execSQL("DELETE FROM courseregister");
            //showMessage("Success", "Update Successful");
        }
        catch(Exception e){httpParse.showMessage("Exception",e.toString(),CourseRegister.this);}

    }



    public void sync() {
        //String courseI;
        int count = totalCourses();
        String[] addCourses = new String[count];
        String courseSplitValue="";
        String courseNameCode="";
        int i=0;
        try {
            Cursor c = db.rawQuery("SELECT CourseCodeName FROM courseregister where matNo = '" + MatNoHolder + "' and isSync=0", null);
            while (c.moveToNext()) {
                courseNameCode = c.getString(0);
                String[] courseSplit = courseNameCode.split(" ");
                courseSplitValue = courseSplit[0];
                addCourses[i] = courseSplitValue;
                ++i;
            }
            StudentCourseRegisterFunction(MatNoHolder,addCourses);
            //listView();
            //Toast.makeText(getApplicationContext(), MatNoHolder+" "+codeSplitValue, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            httpParse.showMessage("Msg", e.toString(),CourseRegister.this);
            return;

        }
        emptyDB();
        getListView();
    }

//syncing method --note--
  /** public void send(View v) {
        ProgressDialog.show(CourseRegister.this, "Sending..", null, true, true);
   }**/

    public void StudentCourseRegisterFunction(final String matno, final String[] courseCode){

        class StudentCourseRegisterFunctionClass extends AsyncTask<String,Void,String> {
            //final ProgressBar Bar = (ProgressBar)findViewById(R.id.progressBar1);
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //Bar.setVisibility(View.INVISIBLE);

                progressDialog = ProgressDialog.show(CourseRegister.this,"Sending..",null,true,true);
                //progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);
                //Bar.setVisibility(View.INVISIBLE);
                progressDialog.dismiss();  //uncomment later
                //progressDialog = ProgressDialog.show(CourseRegister.this,"Registration Process..",null,true,true);
                //Toast.makeText(CourseRegister.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    for (int i = 0; i < courseCode.length; i++) {
                        hashMap.put("matno", matno);
                        hashMap.put("courseCode", courseCode[i]);

                        //Toast.makeText(CourseRegister.this, "CodeName " + courseCode[i].toString(), Toast.LENGTH_LONG).show();

                        finalResult = httpParse.postRequest(hashMap, HttpURL);

                        //codes +=courseCode[i].toString();

                    }
                }

                catch (Exception ex){httpParse.showMessage("Exception loopCourse", ex.toString(),CourseRegister.this);}

                return finalResult;
            }
        }

        StudentCourseRegisterFunctionClass studentCourseRegisterFunctionClass = new StudentCourseRegisterFunctionClass();

        studentCourseRegisterFunctionClass.execute();
        //Toast.makeText(CourseRegister.this, "CodeName " + codes, Toast.LENGTH_LONG).show();

    }


    public boolean checkIfCourseExist(String courseCodeName,String matNoVal) {

        boolean exist = false;


        try {
            Cursor c = db.rawQuery("SELECT CourseCodeName FROM courseregister where CourseCodeName = '" + courseCodeName + "' and matNo = '" + matNoVal + "'", null);

            if (c.getCount() == 0) {
                exist = false;
            } else {
                exist = true;
            }
            //clearText();
        } catch (Exception ex) {
            httpParse.showMessage("Exception CourseExist", ex.toString(),CourseRegister.this);
        }

        return exist;
    }


    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            return !ipAddr.equals("");
        } catch (Exception e) {
            return false;
        }
    }




    public JSONArray getData(){

        return result;
    }


    private void getCourseDataSchool() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,conManager.urlPath+"CourseDetailsNew.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            result = j.getJSONArray(JSON_ARRAY); //allows you to get Json object data by key
                            //httpParse.showMessage("Response:", response.toString(),CourseRegister.this);
                            //showMessage("Result", result.toString());


                            insertIntoSchool(result);
                        } catch (Exception e) {
                            httpParse.showMessage("Exception", e.toString(),CourseRegister.this);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        httpParse.showMessage("ExceptionVolley", error.toString(),CourseRegister.this);
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("schoolName", "school");
                params.put("programName", "non");

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }





    private void getCourseDataProgram() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,conManager.urlPath+"CourseDetailsNew.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            result = j.getJSONArray(JSON_ARRAY); //allows you to get Json object data by key
                            //httpParse.showMessage("ResponseProgram:", response.toString(),CourseRegister.this);
                            //showMessage("Result", result.toString());


                            insertIntoProgram(result);
                        } catch (Exception e) {
                            httpParse.showMessage("ExceptionProgram", e.toString(),CourseRegister.this);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        httpParse.showMessage("ExceptionVolley", error.toString(),CourseRegister.this);
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("schoolName", "school");
                params.put("programName", "program");
                //params.put("xyz", "pass xyz");
                // Pass more params as needed in your rest API
                // Example you may want to pass user input from EditText as a parameter
                // editText.getText().toString().trim()
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    private void getCourseDataCourse() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,conManager.urlPath+"CourseDetailsNew.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(response);
                            result = j.getJSONArray(JSON_ARRAY); //allows you to get Json object data by key
                            //httpParse.showMessage("ResponseCourses:", response.toString(),CourseRegister.this);
                            //showMessage("Result", result.toString());


                            insertIntoCourses(result);
                        } catch (Exception e) {
                            httpParse.showMessage("ExceptionCourses", e.toString(),CourseRegister.this);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        httpParse.showMessage("ExceptionVolley", error.toString(),CourseRegister.this);
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("schoolName", "courseSchool");
                params.put("programName", "programSchool");
                //params.put("xyz", "pass xyz");
                // Pass more params as needed in your rest API
                // Example you may want to pass user input from EditText as a parameter
                // editText.getText().toString().trim()
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    public void getCreditHour_Lecturer(String courseCodeName) {
        String[] courseSplit = courseCodeName.split(" ");
        String courseCode = courseSplit[0];

        try {
            Cursor c = db.rawQuery("SELECT creditHours,lecturer FROM coursesN where courseCode = '" + courseCode + "'", null);

            while (c.moveToNext()) {
                creditHours.setText(c.getString(0));
                lecturer.setText(c.getString(1));
            }

            //clearText();
        } catch (Exception ex) {
            httpParse.showMessage("Exception getSchoolList", ex.toString(),CourseRegister.this);
        }
    }

    public boolean exceedRegistrationDeadline() {

        boolean outcome = false;
        String assertt = "false";
        Date end_date = null;
        String modifiedDate=null;
        try {
                Date date = new Date();
            modifiedDate  = new SimpleDateFormat("yyyy-MM-dd").format(date);;


            Cursor c = db.rawQuery("SELECT end_date FROM coursesN where end_date >= '" + modifiedDate + "' order by end_date desc LIMIT 1", null);

            while (c.moveToNext()) {;
                    outcome = true;
                    assertt=c.getString(0);
                }

        } catch (Exception ex) {
            httpParse.showMessage("Exception Exceed Registration", ex.toString(),CourseRegister.this);
        }
        //httpParse.showMessage("Exception ExceededDate",assertt+" "+modifiedDate,CourseRegister.this);
        return outcome;
    }

    public void sendNotification() {

        //Get an instance of NotificationManager//
        long daysLeft = countDaysLeft();

        if (daysLeft > 0) {

            NotificationCompat.Builder mBuilder = (android.support.v7.app.NotificationCompat.Builder)
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.utg_logo)
                            .setContentTitle("Registration Deadline!")
                            .setContentText("Note: "+daysLeft+" Days left");

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            //NotificationManager.notify();
            mNotificationManager.notify(001, mBuilder.build());
        }

        else {
            NotificationCompat.Builder mBuilder = (android.support.v7.app.NotificationCompat.Builder)
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.utg_logo)
                            .setContentTitle("Registration Deadline!")
                            .setContentText("Note: Registration has closed");
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            //NotificationManager.notify();
            mNotificationManager.notify(001, mBuilder.build());

        }

        playSound();

    }



    public long countDaysLeft() {
        Date today = null;
        Date end_date = null;
        String result = "";
        long diff=0;
        try {
            Date date = new Date();
            String modifiedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
            today = new SimpleDateFormat("yyyy-MM-dd").parse(modifiedDate);


            Cursor c = db.rawQuery("SELECT end_date FROM coursesN LIMIT 1", null);

            while (c.moveToNext()) {
                //regDaysLeft
                result = c.getString(0).toString();


            }
            if (result.equals(""))
                end_date = new SimpleDateFormat("yyyy-MM-dd").parse(modifiedDate);
            else
                end_date = new SimpleDateFormat("yyyy-MM-dd").parse(result);

            //long diff =  Math.abs(end_date.getTime() - today.getTime());
            if ( end_date.getTime() < today.getTime()) {
                diff = 0;
                regDaysLeft = diff;
                //Toast.makeText(getApplicationContext(), "first "+end_date, Toast.LENGTH_LONG).show();
            }
            else{
                diff =  end_date.getTime() - today.getTime();
                regDaysLeft = diff / (24 * 60 * 60 * 1000);
                //Toast.makeText(getApplicationContext(), "second "+end_date, Toast.LENGTH_LONG).show();
            }
            //httpParse.showMessage("Print", today + "---" + end_date + "--" + regDaysLeft, this);

        } catch (Exception ex) {
            httpParse.showMessage("Exception Exceed Registration", ex.toString() + "---" + today + " " + end_date, this);
        }
        return regDaysLeft;
    }
    //httpParse.showMessage("Exception ExceededDate",assertt+" "+modifiedDate,CourseRegister.this);

    public void delayTime() {
     final android.os.Handler handler= new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
             sendNotification();
            }
        },10000);
        }

        public void playSound(){
            ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM,100); //volume
            toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD,700); //time in ms

        }

}