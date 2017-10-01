package com.android.opp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.opp.R;
import com.android.opp.activities.IndividualActivity;
import com.android.opp.models.Item;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by camilo on 20/6/17.
 */

public class SaveAdaptersImages extends ArrayAdapter {
    public SaveAdaptersImages(Context context, List objects) {
        super(context, 0,objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;


        final Item currentItem = (Item) getItem(position);

        final int type = getItemViewType(position);


        int grilla=currentItem.getGrilla();

        if (listItemView == null) {


            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.image_individual_saved, null);


        }


        TextView saved_text_edit = (TextView) listItemView.findViewById(R.id.saved_text_edit);
        saved_text_edit.setText(currentItem.getDescription());

        CircleImageView userImage = (CircleImageView) listItemView.findViewById(R.id.user_image);

        Picasso.with(getContext()).load(currentItem.getUser().getImage()).
                into(userImage);

        LinearLayout contenedor_imagenes = (LinearLayout) listItemView.findViewById(R.id.contenedor_imagenes);

        contenedor_imagenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(getContext(), IndividualActivity.class);
                intent.putExtra("item", currentItem);

                getContext().startActivity(intent);
            }
        });







        RelativeLayout container_image = (RelativeLayout) listItemView.findViewById(R.id.container_image);
        LinearLayout container_image_1 = (LinearLayout) listItemView.findViewById(R.id.container_image_1);
        LinearLayout container_image_2 = (LinearLayout) listItemView.findViewById(R.id.container_image_2);
        LinearLayout container_image_3 = (LinearLayout) listItemView.findViewById(R.id.container_image_3);




        container_image.setVisibility(View.GONE);
        container_image_1.setVisibility(View.GONE);
        container_image_2.setVisibility(View.GONE);
        container_image_3.setVisibility(View.GONE);





        if(grilla==1){


            ImageView imagePropiedad = (ImageView) listItemView.findViewById(R.id.image_item);
            Picasso.with(getContext()).load(currentItem.getImages().get(0).getUrlThumbail()).
                    centerCrop().fit().
                    into(imagePropiedad);
            container_image.setVisibility(View.VISIBLE);
        }

        if(grilla==2){
            ImageView imagePropiedad = (ImageView) listItemView.findViewById(R.id.image_item_1);
            Picasso.with(getContext()).load(currentItem.getImages().get(0).getUrlThumbail()).
                    centerCrop().fit().
                    into(imagePropiedad);

            ImageView imagePropiedad_two = (ImageView) listItemView.findViewById(R.id.image_item_two);
            Picasso.with(getContext()).load(currentItem.getImages().get(1).getUrlThumbail()).
                    centerCrop().fit().
                    into(imagePropiedad_two);
            container_image_1.setVisibility(View.VISIBLE);

        }

        if(grilla==3){

            ImageView imagePropiedad = (ImageView) listItemView.findViewById(R.id.image_item_2);
            Picasso.with(getContext()).load(currentItem.getImages().get(0).getUrlThumbail()).
                    centerCrop().fit().
                    into(imagePropiedad);

            ImageView imagePropiedad_two = (ImageView) listItemView.findViewById(R.id.image_item_two_2);
            Picasso.with(getContext()).load(currentItem.getImages().get(1).getUrlThumbail()).
                    centerCrop().fit().
                    into(imagePropiedad_two);

            ImageView imagePropiedad_three = (ImageView) listItemView.findViewById(R.id.image_item_three);
            Picasso.with(getContext()).load(currentItem.getImages().get(2).getUrlThumbail()).
                    centerCrop().fit().
                    into(imagePropiedad_three);
            container_image_2.setVisibility(View.VISIBLE);

        }


        if(grilla==4){

            ImageView imagePropiedad = (ImageView) listItemView.findViewById(R.id.image_item_3);
            Picasso.with(getContext()).load(currentItem.getImages().get(0).getUrlThumbail()).
                    centerCrop().fit().
                    into(imagePropiedad);

            ImageView imagePropiedad_two = (ImageView) listItemView.findViewById(R.id.image_item_two_3);
            Picasso.with(getContext()).load(currentItem.getImages().get(1).getUrlThumbail()).
                    centerCrop().fit().
                    into(imagePropiedad_two);

            ImageView imagePropiedad_three = (ImageView) listItemView.findViewById(R.id.image_item_three_3);
            Picasso.with(getContext()).load(currentItem.getImages().get(2).getUrlThumbail()).
                    centerCrop().fit().
                    into(imagePropiedad_three);

            ImageView imagePropiedad_four = (ImageView) listItemView.findViewById(R.id.image_item_four);
            Picasso.with(getContext()).load(currentItem.getImages().get(3).getUrlThumbail()).
                    centerCrop().fit().
                    into(imagePropiedad_four);

            container_image_3.setVisibility(View.VISIBLE);

        }

        return listItemView;

    }



}
