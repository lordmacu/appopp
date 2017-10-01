package com.android.opp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.android.opp.R;
import com.android.opp.models.ImageItem;

import java.util.List;

/**
 * Created by camilo on 3/6/17.
 */



public class ListImageAdapter extends ArrayAdapter {
    public ListImageAdapter(Context context, List objects) {
        super(context, 0,objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.image_individual, parent, false);
        }

        ImageItem currentPlan = (ImageItem) getItem(position);


        return listItemView;

    }


}