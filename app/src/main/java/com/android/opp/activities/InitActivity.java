package com.android.opp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.opp.R;
import com.android.opp.helpers.MyDbHelper;
import com.android.opp.models.User;

public class InitActivity extends AppCompatActivity {

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);


        context=this;


        MyDbHelper helperDb= new MyDbHelper(context);

       User usr= helperDb.getUser();
        if(usr.getId()==null){
            Intent intent = new Intent(InitActivity.this, LoginActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(InitActivity.this, MainActivity.class);
            startActivity(intent);
        }

    }
}
