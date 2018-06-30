package com.example.mayankagarwal.thechatapp;

import android.content.Context;
import android.content.SharedPreferences;


public class IntroManager {

    static SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    public IntroManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences("first", 0 );
        editor = pref.edit();
    }

    public void setFirst(Boolean isFirst){
        editor.putBoolean("check", isFirst);
        editor.commit();
    }

    public static boolean Check(){
        return pref.getBoolean("check",true);
    }
}
