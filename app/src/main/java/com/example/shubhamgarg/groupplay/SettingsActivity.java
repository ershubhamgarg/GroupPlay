package com.example.shubhamgarg.groupplay;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by Shubham on 05-07-2016.
 */
public class SettingsActivity extends Activity {
    EditText username,password;
    Button okbtn;
    ImageButton backbtn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingslayout);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        okbtn = (Button)findViewById(R.id.okbtn);
backbtn2 = (ImageButton)findViewById(R.id.backbtn2);


        okbtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN) {
                    okbtn.setBackgroundColor(Color.GRAY);
                }else if(event.getAction()==MotionEvent.ACTION_UP){
                    okbtn.setBackgroundColor(Color.TRANSPARENT);
                }
                return false;
            }
        });


        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                if (user != null && pass.length() > 6)
                {
                    Intent i = new Intent(SettingsActivity.this, MainActivity.class);
                    i.putExtra("username", user);
                    i.putExtra("password", pass);
                    startActivity(i);
                    finish();

                }
                else if (user == null) {
                    Toast.makeText(SettingsActivity.this, "Username is Empty", Toast.LENGTH_SHORT).show();
                } else if (pass.length() < 6) {
                    Toast.makeText(SettingsActivity.this, "password too short", Toast.LENGTH_SHORT).show();
                } else if (pass == null) {
                    Toast.makeText(SettingsActivity.this, "Password is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        backbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(SettingsActivity.this, MainActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(in);
                finish();
            }
        });
    }
}
