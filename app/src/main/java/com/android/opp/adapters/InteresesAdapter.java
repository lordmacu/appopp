package com.android.opp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.opp.R;
import com.android.opp.activities.InteresesActivity;
import com.android.opp.fragments.StepFragment;
import com.android.opp.models.ImageGrid;
import com.github.zagum.switchicon.SwitchIconView;

import java.util.List;

/**
 * Created by camilo on 10/6/17.
 */

public class InteresesAdapter extends ArrayAdapter {

    StepFragment yourActivity=null;
    InteresesActivity interesActivity=null;

    public InteresesAdapter(Context context, List objects,StepFragment yourActivity) {

        super(context, 0,objects);

        this.yourActivity= yourActivity;

    }

    public InteresesAdapter(Context context, List objects,InteresesActivity yourActivity) {

        super(context, 0,objects);

        this.interesActivity= yourActivity;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_intereses, parent, false);
        }

        final ImageGrid current = (ImageGrid) getItem(position);
        final int positionIcon=position;

        final SwitchIconView swichicon = (SwitchIconView) listItemView.findViewById(R.id.switchIconView1);

        TextView text_grid_item= (TextView) listItemView.findViewById(R.id.text_grid_item);

        text_grid_item.setText(current.getTitle());
        swichicon.setImageResource(current.getResource());

        if(current.getState()==1){
            swichicon.setIconEnabled(false);
        }else{
            swichicon.setIconEnabled(true);
        }

        LinearLayout contenedorItemGrid= (LinearLayout) listItemView.findViewById(R.id.contenedorItemGrid);

        contenedorItemGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swichicon.switchState();

                if(yourActivity!=null){
                    yourActivity.onYourClick(swichicon.isIconEnabled(),positionIcon,(ImageGrid) getItem(positionIcon));
                }else{
                    interesActivity.onYourClick(swichicon.isIconEnabled(),positionIcon,(ImageGrid) getItem(positionIcon));

                }
            }
        });

        return listItemView;

    }


}