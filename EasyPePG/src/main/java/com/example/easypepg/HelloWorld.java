package com.example.easypepg;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class HelloWorld {
    public static void main(String[] args){

    }
    public static void TestToast(String tag , String message){
//        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        Log.d(tag,message);
    }
}
