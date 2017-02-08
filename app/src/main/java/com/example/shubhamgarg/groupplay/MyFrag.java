package com.example.shubhamgarg.groupplay;

import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.zip.Inflater;

/**
 * Created by Shubham on 27-06-2016.
 */
public class MyFrag extends Fragment {
    Fragment f = null;
    @Override
    public View onCreateView(LayoutInflater i, ViewGroup g, Bundle b) {

        // return super.onCreateView(inflater, container, savedInstanceState);
        View v = i.inflate(R.layout.albumart,g, false);
        return v;
    }
}
