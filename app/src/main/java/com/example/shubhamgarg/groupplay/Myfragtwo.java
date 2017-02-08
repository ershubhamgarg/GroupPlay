package com.example.shubhamgarg.groupplay;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Shubham on 28-06-2016.
 */
public class Myfragtwo extends Fragment {
    Fragment f = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
    View v = inflater.inflate(R.layout.myfrag2,container,false);
        return v;

    }
}
