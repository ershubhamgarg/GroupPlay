package com.example.shubhamgarg.groupplay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Shubham on 27-06-2016.
 */
public class MyListActivity extends Activity {
    static ListView list = null;
    ImageButton backbtn = null;
   File file;

MainActivity mainactivity;
    static ArrayList<File> arr = new ArrayList<File>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.musiclist);
         backbtn = (ImageButton) findViewById(R.id.backbtn);
        list = (ListView) findViewById(R.id.list);


        arr = MySongLoader.getInstance().getFilesAll();
        System.out.println("*****************************"+arr.size());


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(MyListActivity.this, MainActivity.class);
                i.putExtra("songIndex", position);

                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(i);
                finish();
            }
        });



        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MyListActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                startActivity(i);
finish();

            }
        });
        MyAdapter adap = new MyAdapter(this, R.layout.listitem, arr);

        list.setAdapter(adap);
    }


}

class MyAdapter extends ArrayAdapter {
    Context c = null;
    int resId;
    ArrayList<File> data1;

    MyAdapter(Context c, int resId, ArrayList<File> data) {
        super(c, resId, data);
        this.c = c;
        this.data1 = data;
        this.resId = resId;
    }

    public View getView(int position, View contentView, ViewGroup parent) {
        Activity obj = (Activity) c;
        View view = obj.getLayoutInflater().inflate(resId, parent, false);
        TextView nameoftrack = (TextView) view.findViewById(R.id.nameoftrack);
        nameoftrack.setText(data1.get(position).getName());

        return view;
    }
}