package com.android.opp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.android.opp.R;
import com.android.opp.models.ImageItem;

import java.util.List;

/**
 * Created by camilo on 3/6/17.
 */



public class ImageAdapter extends ArrayAdapter {
    public ImageAdapter(Context context, List objects) {
        super(context, 0,objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.image_individual, parent, false);
        }

        ImageItem current = (ImageItem) getItem(position);
        ImageView image_preview= (ImageView)  listItemView.findViewById(R.id.imagen_preview);

        if(current.getType()==2){
            image_preview.setImageResource(R.mipmap.add_image);
        }else {
            Bitmap bitmap = BitmapFactory.decodeFile(current.getUrl());
            image_preview.setImageBitmap(bitmap);
        }

        Log.v("aqui type",current.getUrl()+"");
        return listItemView;

    }


}