package com.example.apoor.barcodescanner;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {

    private Button scanBtn;
    private TextView formatTxt, contentTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanBtn = (Button)findViewById(R.id.scan_button);
        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);

        scanBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.scan_button){
//scan
                    IntentIntegrator scanIntegrator = new IntentIntegrator(MainActivity.this);
                    scanIntegrator.initiateScan();



                }
            }

        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
//we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            formatTxt.setText("FORMAT: " + scanFormat);
            contentTxt.setText("CONTENT: " + scanContent);

            Intent i = new Intent(MainActivity.this, ShowDetails.class);
            i.putExtra("scansno",scanContent);
            startActivity(i);
         /*   String type = "scan";

            String scansno= contentTxt.getText().toString();
           //BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        //  backgroundWorker.execute(type, scansno);

*/
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
