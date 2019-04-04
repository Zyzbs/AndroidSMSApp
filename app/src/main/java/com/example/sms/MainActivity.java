package com.example.sms;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnSend;
    private EditText etPhone;
    private EditText etContent;

    private String number;
    private String contents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSend=(Button) findViewById(R.id.btn_send);
        etPhone=(EditText) findViewById(R.id.et_phone);
        etContent=(EditText) findViewById(R.id.et_content);
        btnSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        number=etPhone.getText().toString().trim();
        contents=etContent.getText().toString();
        switch (v.getId()){
            case R.id.btn_send:
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);
                }else{
                    sendMsg();
                    break;
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"拒绝权限无法使用程序",Toast.LENGTH_LONG).show();
                }else{
                    sendMsg();
                }
        }
    }
    public void sendMsg(){
        if(TextUtils.isEmpty(number)||TextUtils.isEmpty(contents)){
            Toast.makeText(this,"电话内容或内容不能为空",Toast.LENGTH_LONG).show();
            return;
        }else {
            SmsManager smsManager = SmsManager.getDefault();
            String[] phones = number.split(",");
            ArrayList<String> strings = smsManager.divideMessage(contents);
            for (int i = 0; i < phones.length; i++) {
                for (String str : strings) {
                    smsManager.sendTextMessage(phones[i], null, str, null, null);
                }
            }
        }
    }
}
