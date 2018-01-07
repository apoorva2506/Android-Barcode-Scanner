package com.example.apoor.barcodescanner;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

public class SurveyActivity extends AppCompatActivity {

    TextView txtid, txtname, txtdate, txtscale;
    EditText edtname, edtdate, edtscale;
    Button survbtn;
    DatePickerDialog datePickerDialog;
    InputStream is=null;
    String result=null;
    String line=null;
    int code;

    private static String insertUrl = "http://192.168.1.106/insertaud.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        txtid = (TextView) findViewById(R.id.textView2);
        txtname = (TextView) findViewById(R.id.textView3);
        txtdate = (TextView) findViewById(R.id.textView4);
        txtscale = (TextView) findViewById(R.id.textView5);

        edtname = (EditText) findViewById(R.id.editText);
        edtdate = (EditText) findViewById(R.id.editText2);
        edtscale = (EditText) findViewById(R.id.editText3);
        survbtn = (Button) findViewById(R.id.button2);

        //Log.d("1", "here");
        edtdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(SurveyActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                edtdate.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        survbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String isno = txtid.getText().toString();
                String iname = edtname.getText().toString();
                String idate = edtdate.getText().toString();
                String iscale = edtscale.getText().toString();


                insertAudit(isno, iname, idate, iscale);
            }
        });

    }



    public void insertAudit(final String isno, final String iname, final String idate, final String iscale)
    {

        class SendPostReqAsyncTask extends AsyncTask<String, String, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            protected String doInBackground(String... args) {


                // Building Parameters
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("sno", isno));
                nameValuePairs.add(new BasicNameValuePair("name", iname));
                nameValuePairs.add(new BasicNameValuePair("date", idate));
                nameValuePairs.add(new BasicNameValuePair("scale", iscale));

                // getting JSON Object
                // Note that create product url accepts POST method
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(insertUrl);
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    is = entity.getContent();
                    Log.e("pass 1", "connection success ");
                } catch (Exception e) {
                    Log.e("Fail 1", e.toString());
                    Toast.makeText(getApplicationContext(), "Invalid IP Address",
                            Toast.LENGTH_LONG).show();
                }

                try {
                    BufferedReader reader = new BufferedReader
                            (new InputStreamReader(is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    result = sb.toString();
                    // Log.d("res", result);
                    Log.e("pass 2", "connection success ");
                } catch (Exception e) {
                    Log.e("Fail 2", e.toString());
                }

                try {
                    JSONObject json_data = new JSONObject(result);
                    code = (json_data.getInt("code"));



                } catch (Exception e) {
                    Log.e("Fail 3", e.toString());
                }

                return null;
            }


            protected void onPostExecute(String result) {
                // dismiss the dialog once done
                if (code == 1) {
                    Toast.makeText(getBaseContext(), "Inserted Successfully",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "Sorry, Try Again",
                            Toast.LENGTH_LONG).show();
                }

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(isno, iname, idate, iscale);

    }
}
