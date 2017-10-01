package com.android.opp.activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import com.android.opp.R;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.squareup.picasso.Picasso;

public class ZoomImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);

        Bundle extras = getIntent().getExtras();

        Log.v("extras-->",extras.getString("url"));

        ImageView zoomImage= (ImageView) findViewById(R.id.zoomImage);
        zoomImage.setOnTouchListener(new ImageMatrixTouchHandler(getBaseContext()));

        Picasso.
                with(getBaseContext())
                .load( extras.getString("url"))
                .into(zoomImage)

        ;

    }
}
