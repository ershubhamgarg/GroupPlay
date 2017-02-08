package com.example.shubhamgarg.groupplay;

import java.io.File;
import java.util.ArrayList;

public class MySongLoader {
    private static File f =  new File(System.getenv("EXTERNAL_STORAGE"));
    ;
    private static ArrayList<File> arr1=new ArrayList<>();
    static MySongLoader obj=null;
    private MySongLoader(){}
    public static MySongLoader getInstance()
    {
        if(obj==null)
        {
            obj=new MySongLoader();
            loadFiles(f);
        }
        return obj;
    }
    private static ArrayList<File> loadFiles(File f)
    {
        for (File ff : f.listFiles()) {
            if (ff.isDirectory()) {
                loadFiles(ff);
            } else {
                if (ff.getName().toString().endsWith(".mp3") || ff.getName().toString().endsWith(".MP3")) {

                    if (ff != null) {
                        arr1.add(ff);
System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$"+ arr1.size());
                    }
                }
            }
        }
   return arr1;
    }
    public ArrayList<File> getFilesAll(){
        return arr1;
    }
}
