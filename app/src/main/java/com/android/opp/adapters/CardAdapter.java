package com.android.opp.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.opp.R;
import com.android.opp.activities.IndividualActivity;
import com.android.opp.helpers.OnSwipeTouchListener;
import com.android.opp.models.ImageItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by camilo on 3/7/17.
 */


public  class CardAdapter extends BaseAdapter {
    private ArrayList<ImageItem> resIds;
    private Activity mactivity;
    public CardAdapter(ArrayList<ImageItem>  resIds, Activity activity) {
        this.resIds = resIds;
        mactivity=activity;
    }

    @Override
    public int getCount() {
        return resIds.size();
    }

    @Override
    public ImageItem getItem(int position) {

        return resIds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.image_individual, parent, false);

        }
        Log.v("aquiii",resIds.get(position).toString());

        ImageView imagen_preview = (ImageView) convertView.findViewById(R.id.imagen_preview);
 ;


        imagen_preview.setOnTouchListener(new OnSwipeTouchListener(parent.getContext()) {

            public void onSwipeRight() {
                ((IndividualActivity) mactivity).left();

             }
            public void onSwipeLeft() {
                ((IndividualActivity) mactivity).right();

            }

        });


        Picasso.with(parent.getContext()).load(resIds.get(position).getUrlThumbail()).
                centerCrop().fit().
                into(imagen_preview);

        //convertView.setBackgroundResource(resIds[position]);
        return convertView;
    }
}