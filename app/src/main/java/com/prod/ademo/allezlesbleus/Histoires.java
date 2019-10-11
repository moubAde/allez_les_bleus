package com.prod.ademo.allezlesbleus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Histoires extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histoire);
    }

    public void histoires(View v){
        System.out.print("//////////////////////////////////////////////");
        System.out.print(v.getId());
        //Intent intent = new Intent(mContext,Palmares.class);
        //startActivityForResult(intent, 0);
    }
}
