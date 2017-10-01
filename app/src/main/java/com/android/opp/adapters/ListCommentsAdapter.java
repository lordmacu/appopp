package com.android.opp.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.opp.R;
import com.android.opp.models.Comment;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by camilo on 2/7/17.
 */




public class ListCommentsAdapter extends ArrayAdapter {
    public ListCommentsAdapter(Context context, List objects) {
        super(context, 0,objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_comment, parent, false);
        }

        Comment currentItem = (Comment) getItem(position);

        ImageView profile_image= (ImageView) listItemView.findViewById(R.id.profile_image);

        TextView message= (TextView) listItemView.findViewById(R.id.message);

        Picasso.with(getContext()).load(currentItem.getUser().getImage()).
                centerCrop().fit().
                into(profile_image);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            message.setText(Html.fromHtml("<b>"+currentItem.getUser().getName()+"</b> "+currentItem.getComment(),Html.FROM_HTML_MODE_LEGACY));

        } else {
            message.setText(Html.fromHtml("<b>"+currentItem.getUser().getName()+"</b> "+currentItem.getComment()));
        }



        try {
            Date dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(currentItem.getCreate_at());
            PrettyTime p = new PrettyTime(new Locale("es"));

            TextView created_at=(TextView) listItemView.findViewById(R.id.created_at);
            created_at.setText(p.format(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return listItemView;

    }




}