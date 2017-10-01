package com.android.opp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.opp.R;
import com.android.opp.models.CityText;

import java.util.List;

/**
 * Created by camilo on 17/6/17.
 */



public class TextCityAdapter extends ArrayAdapter {
    public TextCityAdapter(Context context, List objects) {
        super(context, 0,objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.individual_city, parent, false);
        }

        CityText current = (CityText) getItem(position);


        TextView CityNameText= (TextView) listItemView.findViewById(R.id.text_city_name);
        CityNameText.setText(current.getCountry()+" - "+current.getName());
        return listItemView;

    }


}