package com.android.opp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.opp.R;
import com.android.opp.activities.CommentsActivity;
import com.android.opp.activities.IndividualActivity;
import com.android.opp.activities.ZoomImageActivity;
import com.android.opp.helpers.HttpHelper;
import com.android.opp.helpers.MyDbHelper;
import com.android.opp.models.Item;
import com.cuboid.cuboidcirclebutton.CuboidButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TreeSet;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class ListAdapter extends ArrayAdapter<Item> {

    private TreeSet<Integer> mSeparatorSet = new TreeSet<Integer>();

    /**
     * The part of the location string from the USGS service that we use to determine
     * whether or not there is a location offset present ("5km N of Cairo, Egypt").
     */
    private static final String LOCATION_SEPARATOR = " of ";

    /**
     * Constructs a new {@link Item}.
     *
     * @param context of the app
     * @param items   is the list of earthquakes, which is the data source of the adapter
     */
    public ListAdapter(Context context, List<Item> items) {
        super(context, 0, items);
    }

    /**
     * Returns a list item view that displays information about the earthquake at the given position
     * in the list of earthquakes.
     */


    int widthPixels;
    int heightPixels;
    boolean votacion=false;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        final Item currentItem = getItem(position);


        int type = getItemViewType(position);
//        currentItem.setVoting(false);

        int grilla = currentItem.getGrilla();

        if (listItemView == null) {


            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, null);


        }


        LinearLayout contenedor_imagenes = (LinearLayout) listItemView.findViewById(R.id.contenedor_imagenes);

        contenedor_imagenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), IndividualActivity.class);
                //intent.putExtra("item", currentItem);

               // getContext().startActivity(intent);
            }
        });

        LinearLayout comment_container = (LinearLayout) listItemView.findViewById(R.id.comment_container);

        TextView user_comment = (TextView) listItemView.findViewById(R.id.user_comment);
        TextView comment_content = (TextView) listItemView.findViewById(R.id.comment_content);
        TextView comment_time_ago = (TextView) listItemView.findViewById(R.id.comment_time_ago);

        final TextView count_likes = (TextView) listItemView.findViewById(R.id.count_likes);

        final ImageView hearh_icon = (ImageView) listItemView.findViewById(R.id.hearh_icon);

        if (currentItem.getLike_user() != 0) {
            hearh_icon.setImageResource(R.drawable.zzz_heart);
            hearh_icon.setColorFilter(ContextCompat.getColor(getContext(), R.color.red_flat));

        } else {
            hearh_icon.setImageResource(R.drawable.zzz_heart_outline);
            hearh_icon.setColorFilter(ContextCompat.getColor(getContext(), R.color.black));
        }


        hearh_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeDislikeImage(currentItem.getId(), hearh_icon, currentItem, count_likes);
            }
        });


        ImageView send_icon = (ImageView) listItemView.findViewById(R.id.send_icon);

        send_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                Uri uri = Uri.parse(currentItem.getImages().get(0).getUrl());
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, String.valueOf(uri));
                getContext().startActivity(intent);
            }
        });

        ImageView comment_icon = (ImageView) listItemView.findViewById(R.id.comment_icon);

        comment_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CommentsActivity.class);
                intent.putExtra("id_post", currentItem.getId());

                getContext().startActivity(intent);
            }
        });


        count_likes.setText(currentItem.getCount_likes() + " me gusta");
        user_comment.setText(currentItem.getUser().getName());
        comment_content.setText(currentItem.getDescription());

        try {
            Date dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(currentItem.getCreated_at());
            PrettyTime p = new PrettyTime(new Locale("es"));

            comment_time_ago.setText(p.format(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        RelativeLayout container_image = (RelativeLayout) listItemView.findViewById(R.id.container_image);
        LinearLayout container_image_1 = (LinearLayout) listItemView.findViewById(R.id.container_image_1);
        LinearLayout container_image_2 = (LinearLayout) listItemView.findViewById(R.id.container_image_2);
        LinearLayout container_image_3 = (LinearLayout) listItemView.findViewById(R.id.container_image_3);


        container_image.setVisibility(View.GONE);
        container_image_1.setVisibility(View.GONE);
        container_image_2.setVisibility(View.GONE);
        container_image_3.setVisibility(View.GONE);
        TextView UserName = (TextView) listItemView.findViewById(R.id.user_name);


        CircleImageView userImage = (CircleImageView) listItemView.findViewById(R.id.user_image);

        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(30)
                .oval(false)
                .build();

Log.v("dum",currentItem.toString());

        Picasso.with(getContext()).load(currentItem.getUser().getImage()).
                transform(transformation).

                into(userImage);

        widthPixels = getDeviceWidth(getContext());


        if (grilla == 1) {


            ImageView imagePropiedad = (ImageView) listItemView.findViewById(R.id.image_item);
            Picasso.with(getContext()).load(currentItem.getImages().get(0).getUrlThumbail()).
                     into(imagePropiedad);
            container_image.setVisibility(View.VISIBLE);
        }

        if (grilla == 2) {



            int thumbHeightOne = Integer.parseInt(currentItem.getImages().get(0).getThumbHeight());
            int thumbWidthOne = Integer.parseInt(currentItem.getImages().get(0).getThumbWidth());

            int thumbHeightTwo = Integer.parseInt(currentItem.getImages().get(1).getThumbHeight());
            int thumbWidthTwo = Integer.parseInt(currentItem.getImages().get(1).getThumbWidth());

            Log.v("aquiianchouno","thumbHeightOne "+thumbHeightOne+" thumbWidthOne "+thumbWidthOne);
            Log.v("url uno",currentItem.getImages().get(0).getUrlThumbail());


            Log.v("aquiianchodos","thumbHeightTwo "+thumbHeightTwo+" thumbWidthTwo "+thumbWidthTwo);
            Log.v("url dos",currentItem.getImages().get(1).getUrlThumbail());

            boolean isHorizontalOne = true;
            boolean isHorizontalTwo = true;
            if (thumbHeightOne > thumbWidthOne) {
                isHorizontalOne = false;
            }
            if (thumbHeightTwo > thumbWidthTwo) {
                isHorizontalTwo = false;
            }
            Random ran = new Random();

            CuboidButton vote2_1 = (CuboidButton) listItemView.findViewById(R.id.vote2_1);

            if(currentItem.isVoting(1)){
                vote2_1.setText(currentItem.getCantidadVotos());
                vote2_1.setCircle_color(getContext().getResources().getColor(R.color.colorHover));

            }

            vote2_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!currentItem.isVoting(1) && currentItem.getCantidadVotos()=="0"){
                        currentItem.setCantidadVotos(5 + (int)(Math.random() * ((123 - 5) + 1))+"");
                        vote2_1.setText(currentItem.getCantidadVotos());
                        currentItem.setVoting(true,1);
                        vote2_1.setCircle_color(getContext().getResources().getColor(R.color.colorHover));

                    }
                }
            });
            CuboidButton vote2_2 = (CuboidButton) listItemView.findViewById(R.id.vote2_2);

            if(currentItem.isVoting(2)){
                vote2_2.setText(currentItem.getCantidadVotos());
                vote2_2.setCircle_color(getContext().getResources().getColor(R.color.colorHover));

            }else{
                vote2_2.setText("OPP\nME");
            }
            vote2_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!currentItem.isVoting(2) && currentItem.getCantidadVotos()=="0") {
                        currentItem.setCantidadVotos(5 + (int)(Math.random() * ((123 - 5) + 1))+"");
                        vote2_2.setCircle_color(getContext().getResources().getColor(R.color.colorHover));

                        vote2_2.setText(currentItem.getCantidadVotos());
                        currentItem.setVoting(true,2);

                    }
                }
            });


            ImageView imagePropiedad = (ImageView) listItemView.findViewById(R.id.image_item_1);
            ImageView imagePropiedad_two = (ImageView) listItemView.findViewById(R.id.image_item_two);

            imagePropiedad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ZoomImageActivity.class);
                    intent.putExtra("url", currentItem.getImages().get(0).getUrlThumbail());
                    getContext().startActivity(intent);

                }
            });


            imagePropiedad_two.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ZoomImageActivity.class);
                    intent.putExtra("url", currentItem.getImages().get(1).getUrlThumbail());
                    getContext().startActivity(intent);

                }
            });

            RelativeLayout container_image_item_1 = (RelativeLayout) listItemView.findViewById(R.id.container_image_item_1);
            RelativeLayout container_image_item_two = (RelativeLayout) listItemView.findViewById(R.id.container_image_item_two);

            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) imagePropiedad.getLayoutParams();
            p.setMargins(0, 0, 0, 0);
            imagePropiedad.requestLayout();
            if (!isHorizontalOne && !isHorizontalTwo) {

                Log.v("aquii uno","uno");
                container_image_item_1.getLayoutParams().width = (widthPixels * 50) / 100;
                container_image_item_two.getLayoutParams().width = (widthPixels * 50) / 100;
                container_image_1.setOrientation(LinearLayout.HORIZONTAL);

            }

            if (isHorizontalOne && !isHorizontalTwo) {
                Log.v("aquii uno","dos");

                container_image_item_1.getLayoutParams().width = (widthPixels * 65) / 100;
                container_image_item_two.getLayoutParams().width = (widthPixels * 35) / 100;
                container_image_1.setOrientation(LinearLayout.HORIZONTAL);
                container_image_item_1.setPadding(0, 0, 2, 0);
                container_image_item_two.setPadding(2, 0, 0, 0);
            }


            if (!isHorizontalOne && isHorizontalTwo) {
                Log.v("aquii uno","tres");

                container_image_item_1.getLayoutParams().width = (widthPixels * 35) / 100;
                container_image_item_two.getLayoutParams().width = (widthPixels * 65) / 100;
                container_image_1.setOrientation(LinearLayout.HORIZONTAL);
                container_image_item_1.setPadding(0, 0, 2, 0);
                container_image_item_two.setPadding(2, 0, 0, 0);
            }

            if (isHorizontalOne && isHorizontalTwo) {
                Log.v("aquii uno","cuatrod");

                container_image_item_1.getLayoutParams().width = (widthPixels * 100) / 100;
                container_image_item_two.getLayoutParams().width = (widthPixels * 100) / 100;
                container_image_1.setOrientation(LinearLayout.VERTICAL);
                container_image_item_1.setPadding(0, 0, 0, 0);
                container_image_item_two.setPadding(0, 0, 0, 0);

                ViewGroup.MarginLayoutParams ps = (ViewGroup.MarginLayoutParams) imagePropiedad.getLayoutParams();
                ps.setMargins(0, 0, 0, 4);
                imagePropiedad.requestLayout();

            }
            imagePropiedad.requestLayout();
            imagePropiedad_two.requestLayout();

            Picasso.with(getContext()).load(currentItem.getImages().get(0).getUrlThumbail()).
                     into(imagePropiedad);

            Picasso.with(getContext()).load(currentItem.getImages().get(1).getUrlThumbail()).
                     into(imagePropiedad_two);
            container_image_1.setVisibility(View.VISIBLE);


        }

        if (grilla == 3) {

            int thumbHeightOne = Integer.parseInt(currentItem.getImages().get(0).getThumbHeight());
            int thumbWidthOne = Integer.parseInt(currentItem.getImages().get(0).getThumbWidth());

            int thumbHeightTwo = Integer.parseInt(currentItem.getImages().get(1).getThumbHeight());
            int thumbWidthTwo = Integer.parseInt(currentItem.getImages().get(1).getThumbWidth());

            int thumbHeightThree = Integer.parseInt(currentItem.getImages().get(2).getThumbHeight());
            int thumbWidthThree = Integer.parseInt(currentItem.getImages().get(2).getThumbWidth());


            CuboidButton vote3_1 = (CuboidButton) listItemView.findViewById(R.id.vote3_1);
            if(currentItem.isVoting(1)){
                vote3_1.setText(currentItem.getCantidadVotos());
                vote3_1.setCircle_color(getContext().getResources().getColor(R.color.colorHover));

            }else{
                vote3_1.setText("OPP\nME");

            }
            vote3_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!currentItem.isVoting(1) && currentItem.getCantidadVotos()=="0") {
                        currentItem.setCantidadVotos(5 + (int)(Math.random() * ((123 - 5) + 1))+"");
                        vote3_1.setCircle_color(getContext().getResources().getColor(R.color.colorHover));

                        vote3_1.setText(currentItem.getCantidadVotos());
                        currentItem.setVoting(true,1);

                    }
                }
            });


            CuboidButton vote3_2 = (CuboidButton) listItemView.findViewById(R.id.vote3_2);
            if(currentItem.isVoting(2)){
                vote3_2.setText(currentItem.getCantidadVotos()+"");
                vote3_2.setCircle_color(getContext().getResources().getColor(R.color.colorHover));

            }else{
                vote3_2.setText("OPP\nME");

            }
            vote3_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!currentItem.isVoting(2) && currentItem.getCantidadVotos()=="0") {
                        currentItem.setCantidadVotos(5 + (int)(Math.random() * ((123 - 5) + 1))+"");
                        vote3_2.setCircle_color(getContext().getResources().getColor(R.color.colorHover));

                        vote3_2.setText(currentItem.getCantidadVotos());
                        currentItem.setVoting(true,2);

                    }
                }
            });


            CuboidButton vote3_3 = (CuboidButton) listItemView.findViewById(R.id.vote3_3);
            if(currentItem.isVoting(3)){
                vote3_3.setText(currentItem.getCantidadVotos()+"");
                vote3_3.setCircle_color(getContext().getResources().getColor(R.color.colorHover));

            }else{
                vote3_3.setText("OPP\nME");

            }
            vote3_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!currentItem.isVoting(3) && currentItem.getCantidadVotos()=="0") {
                        currentItem.setCantidadVotos(5 + (int)(Math.random() * ((123 - 5) + 1))+"");
                        vote3_3.setCircle_color(getContext().getResources().getColor(R.color.colorHover));

                        vote3_3.setText(currentItem.getCantidadVotos());
                        currentItem.setVoting(true,3);

                    }
                }
            });

            boolean isHorizontalOne = true;
            boolean isHorizontalTwo = true;
            boolean isHorizontalThree = true;
            if (thumbHeightOne > thumbWidthOne) {
                isHorizontalOne = false;
                Log.v("primera__", "verical__");
            }
            if (thumbHeightTwo > thumbWidthTwo) {
                isHorizontalTwo = false;
                Log.v("segunda__", "verical__");

            }
            if (thumbHeightThree > thumbWidthThree) {
                isHorizontalThree = false;
                Log.v("tercera__", "verical__");
            }

            Log.v("separador", "------___");
            ImageView imagePropiedad = (ImageView) listItemView.findViewById(R.id.image_item_2);
            ImageView imagePropiedad_two = (ImageView) listItemView.findViewById(R.id.image_item_two_2);
            ImageView imagePropiedad_three = (ImageView) listItemView.findViewById(R.id.image_item_three);

            imagePropiedad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ZoomImageActivity.class);
                    intent.putExtra("url", currentItem.getImages().get(0).getUrlThumbail());
                    getContext().startActivity(intent);

                }
            });

            imagePropiedad_two.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ZoomImageActivity.class);
                    intent.putExtra("url", currentItem.getImages().get(1).getUrlThumbail());
                    getContext().startActivity(intent);

                }
            });

            imagePropiedad_three.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ZoomImageActivity.class);
                    intent.putExtra("url", currentItem.getImages().get(2).getUrlThumbail());
                    getContext().startActivity(intent);

                }
            });



            LinearLayout container_image_item_2 = (LinearLayout) listItemView.findViewById(R.id.container_image_item_2);
            LinearLayout container_image_item_two_2_image_item_three = (LinearLayout) listItemView.findViewById(R.id.container_image_item_two_2_image_item_three);
            float density = getContext().getResources().getDisplayMetrics().density;


            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) imagePropiedad_two.getLayoutParams();
            p.setMargins(0, 0, 0, 4);
            imagePropiedad_two.requestLayout();

            container_image_item_2.setPadding(0, 0, 2, 0);
            container_image_item_two_2_image_item_three.setPadding(2, 0, 0, 0);
            Log.v("separador", "------___  tres");

            imagePropiedad.getLayoutParams().height = (int) (250 * density);
            imagePropiedad_two.getLayoutParams().height = (int) (125 * density);
            imagePropiedad_three.getLayoutParams().height = (int) (125 * density);
            container_image_item_2.getLayoutParams().width = (widthPixels * 50) / 100;
            container_image_item_two_2_image_item_three.getLayoutParams().height = (int) (250 * density);

            container_image_item_two_2_image_item_three.getLayoutParams().width = (widthPixels * 50) / 100;
            container_image_2.setOrientation(LinearLayout.HORIZONTAL);
            container_image_item_two_2_image_item_three.setOrientation(LinearLayout.VERTICAL);


            imagePropiedad_two.getLayoutParams().width = (widthPixels * 100) / 100;
            imagePropiedad_three.getLayoutParams().width = (widthPixels * 100) / 100;


            Picasso.with(getContext()).load(currentItem.getImages().get(0).getUrlThumbail()).
                    into(imagePropiedad);

            Picasso.with(getContext()).load(currentItem.getImages().get(1).getUrlThumbail()).
                    into(imagePropiedad_two);

            Picasso.with(getContext()).load(currentItem.getImages().get(2).getUrlThumbail()).
                    into(imagePropiedad_three);


            imagePropiedad.requestLayout();
            imagePropiedad_two.requestLayout();
            imagePropiedad_three.requestLayout();
            container_image_2.requestLayout();
            container_image_item_two_2_image_item_three.requestLayout();

            /*
            if (!isHorizontalOne && !isHorizontalTwo && !isHorizontalThree) {

                p.setMargins(0, 0, 0, 0);
                imagePropiedad_two.requestLayout();
                Log.v("separador", "------___  uno");

                container_image_item_2.setPadding(0, 0, 2, 0);
                container_image_item_two_2_image_item_three.setPadding(2, 0, 0, 0);

                container_image_item_2.getLayoutParams().width = (widthPixels * 33) / 100;

                container_image_item_two_2_image_item_three.setOrientation(LinearLayout.HORIZONTAL);
                container_image_item_two_2_image_item_three.getLayoutParams().width = (widthPixels * 67) / 100;
                container_image_item_two_2_image_item_three.getLayoutParams().height = (int) (250 * density);
                container_image_item_two_2_image_item_three.requestLayout();


                imagePropiedad.getLayoutParams().height = (int) (250 * density);

                imagePropiedad_two.getLayoutParams().height = (int) (250 * density);
                imagePropiedad_two.getLayoutParams().width = (container_image_item_two_2_image_item_three.getLayoutParams().width * 50) / 100;

                imagePropiedad_three.getLayoutParams().height = (int) (250 * density);
                imagePropiedad_three.getLayoutParams().width = (container_image_item_two_2_image_item_three.getLayoutParams().width * 50) / 100;


                container_image_2.setOrientation(LinearLayout.HORIZONTAL);


                imagePropiedad.requestLayout();
                imagePropiedad_two.requestLayout();
                imagePropiedad_three.requestLayout();
                container_image_2.requestLayout();
                container_image_item_two_2_image_item_three.requestLayout();

                Picasso.with(getContext()).load(currentItem.getImages().get(0).getUrlThumbail()).
                        into(imagePropiedad);

                Picasso.with(getContext()).load(currentItem.getImages().get(1).getUrlThumbail()).
                        into(imagePropiedad_two);

                Picasso.with(getContext()).load(currentItem.getImages().get(2).getUrlThumbail()).
                        into(imagePropiedad_three);

            } else if (!isHorizontalOne && !isHorizontalTwo && !isHorizontalThree) {


                Log.v("separador", "------___  dos");

                imagePropiedad.getLayoutParams().height = (int) (200 * density);
                imagePropiedad_two.getLayoutParams().height = (int) (200 * density);
                imagePropiedad_three.getLayoutParams().height = (int) (200 * density);

                container_image_item_2.getLayoutParams().width = (widthPixels * 100) / 100;
                container_image_item_two_2_image_item_three.getLayoutParams().width = (widthPixels * 100) / 100;


                container_image_item_two_2_image_item_three.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;


                container_image_2.setOrientation(LinearLayout.VERTICAL);
                container_image_item_two_2_image_item_three.setOrientation(LinearLayout.VERTICAL);

                imagePropiedad_two.getLayoutParams().width = (widthPixels * 100) / 100;


                imagePropiedad_three.getLayoutParams().width = (widthPixels * 100) / 100;

                Picasso.with(getContext()).load(currentItem.getImages().get(0).getUrlThumbail()).
                        into(imagePropiedad);

                Picasso.with(getContext()).load(currentItem.getImages().get(1).getUrlThumbail()).
                        into(imagePropiedad_two);

                Picasso.with(getContext()).load(currentItem.getImages().get(2).getUrlThumbail()).
                        into(imagePropiedad_three);

                container_image_item_2.setPadding(0, 0, 0, 2);
                container_image_item_two_2_image_item_three.setPadding(0, 2, 0, 0);


                imagePropiedad.requestLayout();
                imagePropiedad_two.requestLayout();
                imagePropiedad_three.requestLayout();
                container_image_2.requestLayout();
                container_image_item_two_2_image_item_three.requestLayout();
            } else if (!isHorizontalOne && isHorizontalTwo && isHorizontalThree) {

                container_image_item_2.setPadding(0, 0, 2, 0);
                container_image_item_two_2_image_item_three.setPadding(2, 0, 0, 0);
                Log.v("separador", "------___  tres");

                imagePropiedad.getLayoutParams().height = (int) (250 * density);
                imagePropiedad_two.getLayoutParams().height = (int) (125 * density);
                imagePropiedad_three.getLayoutParams().height = (int) (125 * density);
                container_image_item_2.getLayoutParams().width = (widthPixels * 50) / 100;
                container_image_item_two_2_image_item_three.getLayoutParams().height = (int) (250 * density);

                container_image_item_two_2_image_item_three.getLayoutParams().width = (widthPixels * 50) / 100;
                container_image_2.setOrientation(LinearLayout.HORIZONTAL);
                container_image_item_two_2_image_item_three.setOrientation(LinearLayout.VERTICAL);


                imagePropiedad_two.getLayoutParams().width = (widthPixels * 100) / 100;
                imagePropiedad_three.getLayoutParams().width = (widthPixels * 100) / 100;


                Picasso.with(getContext()).load(currentItem.getImages().get(0).getUrlThumbail()).
                        into(imagePropiedad);

                Picasso.with(getContext()).load(currentItem.getImages().get(1).getUrlThumbail()).
                        into(imagePropiedad_two);

                Picasso.with(getContext()).load(currentItem.getImages().get(2).getUrlThumbail()).
                        into(imagePropiedad_three);


                imagePropiedad.requestLayout();
                imagePropiedad_two.requestLayout();
                imagePropiedad_three.requestLayout();
                container_image_2.requestLayout();
                container_image_item_two_2_image_item_three.requestLayout();

            } else if (isHorizontalOne && !isHorizontalTwo && isHorizontalThree) {
                Log.v("separador", "------___  cuatro");

                container_image_item_2.setPadding(0, 0, 2, 0);
                container_image_item_two_2_image_item_three.setPadding(2, 0, 0, 0);

                imagePropiedad.getLayoutParams().height = (int) (250 * density);
                imagePropiedad_two.getLayoutParams().height = (int) (125 * density);
                imagePropiedad_three.getLayoutParams().height = (int) (125 * density);
                container_image_item_2.getLayoutParams().width = (widthPixels * 50) / 100;
                container_image_item_two_2_image_item_three.getLayoutParams().width = (widthPixels * 50) / 100;
                container_image_item_two_2_image_item_three.getLayoutParams().height = (int) (250 * density);

                container_image_2.setOrientation(LinearLayout.HORIZONTAL);
                container_image_item_two_2_image_item_three.setOrientation(LinearLayout.VERTICAL);

                Picasso.with(getContext()).load(currentItem.getImages().get(0).getUrlThumbail()).
                        into(imagePropiedad_two);

                Picasso.with(getContext()).load(currentItem.getImages().get(1).getUrlThumbail()).
                        into(imagePropiedad);

                Picasso.with(getContext()).load(currentItem.getImages().get(2).getUrlThumbail()).
                        into(imagePropiedad_three);


                imagePropiedad_two.getLayoutParams().width = (widthPixels * 100) / 100;
                imagePropiedad_three.getLayoutParams().width = (widthPixels * 100) / 100;


                imagePropiedad.requestLayout();
                imagePropiedad_two.requestLayout();
                imagePropiedad_three.requestLayout();
                container_image_2.requestLayout();
                container_image_item_two_2_image_item_three.requestLayout();
            } else if (isHorizontalOne && isHorizontalTwo && !isHorizontalThree) {


                container_image_item_2.setPadding(0, 0, 2, 0);
                container_image_item_two_2_image_item_three.setPadding(2, 0, 0, 0);
                Log.v("separador", "------___  cinco");


                imagePropiedad.getLayoutParams().height = (int) (250 * density);
                imagePropiedad_two.getLayoutParams().height = (int) (125 * density);
                imagePropiedad_three.getLayoutParams().height = (int) (125 * density);
                container_image_item_2.getLayoutParams().width = (widthPixels * 50) / 100;
                container_image_item_two_2_image_item_three.getLayoutParams().width = (widthPixels * 50) / 100;
                container_image_item_two_2_image_item_three.getLayoutParams().height = (int) (250 * density);

                container_image_2.setOrientation(LinearLayout.HORIZONTAL);
                container_image_item_two_2_image_item_three.setOrientation(LinearLayout.VERTICAL);

                Picasso.with(getContext()).load(currentItem.getImages().get(0).getUrlThumbail()).
                        into(imagePropiedad_three);

                Picasso.with(getContext()).load(currentItem.getImages().get(1).getUrlThumbail()).
                        into(imagePropiedad_two);

                Picasso.with(getContext()).load(currentItem.getImages().get(2).getUrlThumbail()).
                        into(imagePropiedad);

                imagePropiedad_two.getLayoutParams().width = ViewGroup.LayoutParams.FILL_PARENT;
                imagePropiedad_three.getLayoutParams().width = ViewGroup.LayoutParams.FILL_PARENT;


                imagePropiedad.requestLayout();
                imagePropiedad_two.requestLayout();
                imagePropiedad_three.requestLayout();
                container_image_2.requestLayout();
                container_image_item_two_2_image_item_three.requestLayout();
            } else if (!isHorizontalOne && isHorizontalTwo && !isHorizontalThree) {


                container_image_item_2.setPadding(0, 0, 2, 0);
                container_image_item_two_2_image_item_three.setPadding(2, 0, 0, 0);
                Log.v("separador", "------___  seis");


                imagePropiedad.getLayoutParams().height = (int) (250 * density);
                imagePropiedad_two.getLayoutParams().height = (int) (125 * density);


                imagePropiedad_three.getLayoutParams().height = (int) (125 * density);
                container_image_item_2.getLayoutParams().width = (widthPixels * 50) / 100;
                container_image_item_two_2_image_item_three.getLayoutParams().width = (widthPixels * 50) / 100;
                container_image_item_two_2_image_item_three.getLayoutParams().height = (int) (250 * density);

                container_image_2.setOrientation(LinearLayout.HORIZONTAL);
                container_image_item_two_2_image_item_three.setOrientation(LinearLayout.VERTICAL);

                imagePropiedad_two.getLayoutParams().width = (container_image_item_two_2_image_item_three.getLayoutParams().width * 100) / 100;
                imagePropiedad_three.getLayoutParams().width = (container_image_item_two_2_image_item_three.getLayoutParams().width * 100) / 100;


                Picasso.with(getContext()).load(currentItem.getImages().get(0).getUrlThumbail()).
                         into(imagePropiedad_three);

                Picasso.with(getContext()).load(currentItem.getImages().get(1).getUrlThumbail()).
                         into(imagePropiedad_two);

                Picasso.with(getContext()).load(currentItem.getImages().get(2).getUrlThumbail()).
                         into(imagePropiedad);


                imagePropiedad.requestLayout();
                imagePropiedad_two.requestLayout();
                imagePropiedad_three.requestLayout();
                container_image_2.requestLayout();
                container_image_item_two_2_image_item_three.requestLayout();
            } else

            {


                container_image_item_2.setPadding(0, 0, 2, 0);
                container_image_item_two_2_image_item_three.setPadding(2, 0, 0, 0);
                Log.v("separador", "------___  siete");
                Log.v("boleanos __", "isHorizontalOne " + isHorizontalOne + " isHorizontalTwo " + isHorizontalTwo + " isHorizontalThree " + isHorizontalThree);
                imagePropiedad.getLayoutParams().height = (int) (200 * density);
                imagePropiedad_two.getLayoutParams().height = (int) (200 * density);
                imagePropiedad_three.getLayoutParams().height = (int) (200 * density);
                container_image_2.setOrientation(LinearLayout.VERTICAL);

                Picasso.with(getContext()).load(currentItem.getImages().get(0).getUrlThumbail()).
                         into(imagePropiedad);

                Picasso.with(getContext()).load(currentItem.getImages().get(1).getUrlThumbail()).
                         into(imagePropiedad_two);

                Picasso.with(getContext()).load(currentItem.getImages().get(2).getUrlThumbail()).
                         into(imagePropiedad_three);

                imagePropiedad_two.getLayoutParams().width = (widthPixels * 100) / 100;
                imagePropiedad_three.getLayoutParams().width = (widthPixels * 100) / 100;


                imagePropiedad.requestLayout();
                imagePropiedad_two.requestLayout();
                imagePropiedad_three.requestLayout();
                container_image_2.requestLayout();
                container_image_item_two_2_image_item_three.requestLayout();
            }
**/
            container_image_2.setVisibility(View.VISIBLE);

        }


        if (grilla == 4) {

            int thumbHeightOne = Integer.parseInt(currentItem.getImages().get(0).getThumbHeight());
            int thumbWidthOne = Integer.parseInt(currentItem.getImages().get(0).getThumbWidth());

            int thumbHeightTwo = Integer.parseInt(currentItem.getImages().get(1).getThumbHeight());
            int thumbWidthTwo = Integer.parseInt(currentItem.getImages().get(1).getThumbWidth());

            int thumbHeightThree = Integer.parseInt(currentItem.getImages().get(2).getThumbHeight());
            int thumbWidthThree = Integer.parseInt(currentItem.getImages().get(2).getThumbWidth());

            int thumbHeightFour = Integer.parseInt(currentItem.getImages().get(3).getThumbHeight());
            int thumbWidthFour = Integer.parseInt(currentItem.getImages().get(3).getThumbWidth());



            CuboidButton vote4_1 = (CuboidButton) listItemView.findViewById(R.id.vote4_1);
            if(currentItem.isVoting(1)){
                vote4_1.setText(currentItem.getCantidadVotos()+"");
                vote4_1.setCircle_color(getContext().getResources().getColor(R.color.colorHover));

            }else{
                vote4_1.setText("OPP\nME");
            }
            vote4_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!currentItem.isVoting(1) && currentItem.getCantidadVotos()=="0") {
                        currentItem.setCantidadVotos(5 + (int)(Math.random() * ((123 - 5) + 1))+"");

                        vote4_1.setText(currentItem.getCantidadVotos());
                        vote4_1.setCircle_color(getContext().getResources().getColor(R.color.colorHover));
                        currentItem.setVoting(true,1);

                    }
                }
            });


            CuboidButton vote4_2 = (CuboidButton) listItemView.findViewById(R.id.vote4_2);
            if(currentItem.isVoting(2)){
                vote4_2.setText(currentItem.getCantidadVotos()+"");
                vote4_2.setCircle_color(getContext().getResources().getColor(R.color.colorHover));

            }else{
                vote4_2.setText("OPP\nME");
            }
            vote4_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!currentItem.isVoting(2) && currentItem.getCantidadVotos()=="0") {
                        currentItem.setCantidadVotos(5 + (int)(Math.random() * ((123 - 5) + 1))+"");
                        vote4_2.setCircle_color(getContext().getResources().getColor(R.color.colorHover));

                        vote4_2.setText(currentItem.getCantidadVotos());
                        currentItem.setVoting(true,2);

                    }
                }
            });

            CuboidButton vote4_3 = (CuboidButton) listItemView.findViewById(R.id.vote4_3);
            if(currentItem.isVoting(3)){
                vote4_3.setText(currentItem.getCantidadVotos()+"");
                vote4_3.setCircle_color(getContext().getResources().getColor(R.color.colorHover));

            }else{
                vote4_3.setText("OPP\nME");
            }
            vote4_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!currentItem.isVoting(3) && currentItem.getCantidadVotos()=="0") {
                        currentItem.setCantidadVotos(5 + (int)(Math.random() * ((123 - 5) + 1))+"");
                        vote4_3.setCircle_color(getColorWrapper(getContext(),R.color.colorPrimaryDark));
                        vote4_3.setCircle_border_color(getColorWrapper(getContext(),R.color.colorPrimaryDark));
                        vote4_3.invalidate();
                        vote4_3.setText(currentItem.getCantidadVotos());
                        currentItem.setVoting(true,3);

                    }
                }
            });
            CuboidButton vote4_4 = (CuboidButton) listItemView.findViewById(R.id.vote4_4);
            if(currentItem.isVoting(4)){
                vote4_4.setCircle_color(getContext().getResources().getColor(R.color.colorHover));
                vote4_4.setText(currentItem.getCantidadVotos()+"");
            }else{
                vote4_4.setText("OPP\nME");
            }
            vote4_4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!currentItem.isVoting(4) && currentItem.getCantidadVotos()=="0") {
                        currentItem.setCantidadVotos(5 + (int)(Math.random() * ((123 - 5) + 1))+"");
                        vote4_4.setCircle_color(getColorWrapper(getContext(),R.color.colorPrimaryDark));
                        vote4_4.setCircle_border_color(getColorWrapper(getContext(),R.color.colorPrimaryDark));
                        vote4_4.postInvalidate();

                        vote4_4.setText(currentItem.getCantidadVotos());
                        currentItem.setVoting(true,4);

                    }
                }
            });



            boolean isHorizontalOne = true;
            boolean isHorizontalTwo = true;
            boolean isHorizontalThree = true;
            boolean isHorizontalFour = true;

            if (thumbHeightOne > thumbWidthOne) {
                isHorizontalOne = false;
                Log.v("primera__", "verical__");
            }
            if (thumbHeightTwo > thumbWidthTwo) {
                isHorizontalTwo = false;
                Log.v("segunda__", "verical__");

            }
            if (thumbHeightThree > thumbWidthThree) {
                isHorizontalThree = false;
                Log.v("tercera__", "verical__");
            }
            if (thumbHeightFour > thumbWidthFour) {
                isHorizontalFour = false;
                Log.v("tercera__", "verical__");
            }

            ImageView imagePropiedad = (ImageView) listItemView.findViewById(R.id.image_item_3);
            ImageView imagePropiedad_two = (ImageView) listItemView.findViewById(R.id.image_item_two_3);
            ImageView imagePropiedad_three = (ImageView) listItemView.findViewById(R.id.image_item_three_3);
            ImageView imagePropiedad_four = (ImageView) listItemView.findViewById(R.id.image_item_four);



            imagePropiedad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ZoomImageActivity.class);
                    intent.putExtra("url", currentItem.getImages().get(0).getUrlThumbail());
                    getContext().startActivity(intent);

                }
            });

            imagePropiedad_two.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ZoomImageActivity.class);
                    intent.putExtra("url", currentItem.getImages().get(1).getUrlThumbail());
                    getContext().startActivity(intent);

                }
            });

            imagePropiedad_three.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ZoomImageActivity.class);
                    intent.putExtra("url", currentItem.getImages().get(2).getUrlThumbail());
                    getContext().startActivity(intent);

                }
            });




            imagePropiedad_four.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ZoomImageActivity.class);
                    intent.putExtra("url", currentItem.getImages().get(3).getUrlThumbail());
                    getContext().startActivity(intent);

                }
            });

            Picasso.with(getContext()).load(currentItem.getImages().get(0).getUrlThumbail()).
                    into(imagePropiedad);

            Picasso.with(getContext()).load(currentItem.getImages().get(1).getUrlThumbail()).
                    into(imagePropiedad_two);

            Picasso.with(getContext()).load(currentItem.getImages().get(2).getUrlThumbail()).
                    into(imagePropiedad_three);

            Picasso.with(getContext()).load(currentItem.getImages().get(3).getUrlThumbail()).
                    into(imagePropiedad_four);

            container_image_3.setVisibility(View.VISIBLE);


            LinearLayout container_four_one=  (LinearLayout)  listItemView.findViewById(R.id.container_four_one);
            LinearLayout container_four_two=  (LinearLayout)  listItemView.findViewById(R.id.container_four_two);

            RelativeLayout container_image_four_one= (RelativeLayout) listItemView.findViewById(R.id.container_image_four_one);
            RelativeLayout container_image_four_two= (RelativeLayout) listItemView.findViewById(R.id.container_image_four_two);
            RelativeLayout container_image_four_three= (RelativeLayout) listItemView.findViewById(R.id.container_image_four_three);
            RelativeLayout container_image_four_four= (RelativeLayout) listItemView.findViewById(R.id.container_image_four_four);

            container_four_one.setPadding(0, 0, 0, 0);
            container_four_two.setPadding(0, 0, 0, 0);

            container_image_four_one.setPadding(0, 0, 2, 2);
            container_image_four_two.setPadding(2, 0, 0, 2);
            container_image_four_three.setPadding(0, 2, 2, 0);
            container_image_four_four.setPadding(2, 2, 0, 0);

            if(!isHorizontalOne && isHorizontalTwo && isHorizontalThree && isHorizontalFour){
                Log.v("four__","one");

                imagePropiedad.getLayoutParams().width = (widthPixels * 33) / 100;
                imagePropiedad_two.getLayoutParams().width = (widthPixels * 67) / 100;
                imagePropiedad_three.getLayoutParams().width = (widthPixels * 100) / 100;
                imagePropiedad_four.getLayoutParams().width = (widthPixels * 100) / 100;
            }else


            if(isHorizontalOne && !isHorizontalTwo && isHorizontalThree && isHorizontalFour){
                Log.v("four__","two");

                imagePropiedad.getLayoutParams().width = (widthPixels * 67) / 100;
                imagePropiedad_two.getLayoutParams().width = (widthPixels * 33) / 100;
                imagePropiedad_three.getLayoutParams().width = (widthPixels * 100) / 100;
                imagePropiedad_four.getLayoutParams().width = (widthPixels * 100) / 100;
            }else

            if(isHorizontalOne && isHorizontalTwo && !isHorizontalThree && isHorizontalFour){
                Log.v("four__","three");

                imagePropiedad.getLayoutParams().width = (widthPixels * 100) / 100;
                imagePropiedad_two.getLayoutParams().width = (widthPixels * 100) / 100;
                imagePropiedad_three.getLayoutParams().width = (widthPixels * 33) / 100;
                imagePropiedad_four.getLayoutParams().width = (widthPixels * 67) / 100;
            }else

            if(isHorizontalOne && isHorizontalTwo && !isHorizontalThree && isHorizontalFour){
                Log.v("four__","four");

                imagePropiedad.getLayoutParams().width = (widthPixels * 100) / 100;
                imagePropiedad_two.getLayoutParams().width = (widthPixels * 100) / 100;
                imagePropiedad_three.getLayoutParams().width = (widthPixels * 67) / 100;
                imagePropiedad_four.getLayoutParams().width = (widthPixels * 33) / 100;
            }else


            if(!isHorizontalOne && isHorizontalTwo && !isHorizontalThree && isHorizontalFour){
                Log.v("four__","five");

                imagePropiedad.getLayoutParams().width = (widthPixels * 33) / 100;
                imagePropiedad_two.getLayoutParams().width = (widthPixels * 67) / 100;
                imagePropiedad_three.getLayoutParams().width = (widthPixels * 67) / 100;
                imagePropiedad_four.getLayoutParams().width = (widthPixels * 33) / 100;

                Picasso.with(getContext()).load(currentItem.getImages().get(0).getUrlThumbail()).
                        into(imagePropiedad);

                Picasso.with(getContext()).load(currentItem.getImages().get(1).getUrlThumbail()).
                        into(imagePropiedad_two);

                Picasso.with(getContext()).load(currentItem.getImages().get(2).getUrlThumbail()).
                        into(imagePropiedad_four);

                Picasso.with(getContext()).load(currentItem.getImages().get(3).getUrlThumbail()).
                        into(imagePropiedad_three);

            }else

            if(isHorizontalOne && !isHorizontalTwo && isHorizontalThree && !isHorizontalFour){
                Log.v("four__","six");

                imagePropiedad.getLayoutParams().width = (widthPixels * 67) / 100;
                imagePropiedad_two.getLayoutParams().width = (widthPixels * 33) / 100;
                imagePropiedad_three.getLayoutParams().width = (widthPixels * 67) / 100;
                imagePropiedad_four.getLayoutParams().width = (widthPixels * 33) / 100;
            }else if(isHorizontalOne && isHorizontalTwo && !isHorizontalThree && !isHorizontalFour){
                Log.v("four__","seven isHorizontalOne"+isHorizontalOne+" isHorizontalTwo "+isHorizontalTwo+" isHorizontalThree"+isHorizontalThree+" isHorizontalFour "+isHorizontalFour);

                imagePropiedad.getLayoutParams().width = (widthPixels * 67) / 100;
                imagePropiedad_two.getLayoutParams().width = (widthPixels * 33) / 100;
                imagePropiedad_three.getLayoutParams().width = (widthPixels * 67) / 100;
                imagePropiedad_four.getLayoutParams().width = (widthPixels * 33) / 100;



                Picasso.with(getContext()).load(currentItem.getImages().get(0).getUrlThumbail()).
                        into(imagePropiedad);

                Picasso.with(getContext()).load(currentItem.getImages().get(1).getUrlThumbail()).
                        into( imagePropiedad_three);

                Picasso.with(getContext()).load(currentItem.getImages().get(2).getUrlThumbail()).
                        into(imagePropiedad_two);

                Picasso.with(getContext()).load(currentItem.getImages().get(3).getUrlThumbail()).
                        into(imagePropiedad_four);


            }else if(isHorizontalOne && !isHorizontalTwo && !isHorizontalThree && isHorizontalFour){
                Log.v("four__","eight isHorizontalOne"+isHorizontalOne+" isHorizontalTwo "+isHorizontalTwo+" isHorizontalThree"+isHorizontalThree+" isHorizontalFour "+isHorizontalFour);

                imagePropiedad.getLayoutParams().width = (widthPixels * 33) / 100;
                imagePropiedad_two.getLayoutParams().width = (widthPixels * 67) / 100;
                imagePropiedad_three.getLayoutParams().width = (widthPixels * 67) / 100;
                imagePropiedad_four.getLayoutParams().width = (widthPixels * 33) / 100;



                Picasso.with(getContext()).load(currentItem.getImages().get(0).getUrlThumbail()).
                        into(imagePropiedad_two);

                Picasso.with(getContext()).load(currentItem.getImages().get(1).getUrlThumbail()).
                        into( imagePropiedad);

                Picasso.with(getContext()).load(currentItem.getImages().get(2).getUrlThumbail()).
                        into(imagePropiedad_four);

                Picasso.with(getContext()).load(currentItem.getImages().get(3).getUrlThumbail()).
                        into(imagePropiedad_three);
            }else if(!isHorizontalOne && isHorizontalTwo && isHorizontalThree && !isHorizontalFour){
                Log.v("four__","nine isHorizontalOne"+isHorizontalOne+" isHorizontalTwo "+isHorizontalTwo+" isHorizontalThree"+isHorizontalThree+" isHorizontalFour "+isHorizontalFour);

                imagePropiedad.getLayoutParams().width = (widthPixels * 33) / 100;
                imagePropiedad_two.getLayoutParams().width = (widthPixels * 67) / 100;
                imagePropiedad_three.getLayoutParams().width = (widthPixels * 33) / 100;
                imagePropiedad_four.getLayoutParams().width = (widthPixels * 67) / 100;



                Picasso.with(getContext()).load(currentItem.getImages().get(0).getUrlThumbail()).
                        into( imagePropiedad);

                Picasso.with(getContext()).load(currentItem.getImages().get(1).getUrlThumbail()).
                        into( imagePropiedad_two);

                Picasso.with(getContext()).load(currentItem.getImages().get(2).getUrlThumbail()).
                        into(imagePropiedad_four);

                Picasso.with(getContext()).load(currentItem.getImages().get(3).getUrlThumbail()).
                        into( imagePropiedad_three);
            }else{
                Log.v("four__","ten isHorizontalOne"+isHorizontalOne+" isHorizontalTwo "+isHorizontalTwo+" isHorizontalThree"+isHorizontalThree+" isHorizontalFour "+isHorizontalFour);
                 imagePropiedad.getLayoutParams().width = (widthPixels * 50) / 100;
                imagePropiedad_two.getLayoutParams().width = (widthPixels * 50) / 100;
                imagePropiedad_three.getLayoutParams().width = (widthPixels * 50) / 100;
                imagePropiedad_four.getLayoutParams().width = (widthPixels * 50) / 100;



            }



        }
        UserName.setText(currentItem.getUser().getName());
        return listItemView;
    }

    public ViewGroup.LayoutParams setImageWidth(RelativeLayout imageView, int percent) {


        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();


        layoutParams.width = (widthPixels * percent) / 100;

        return layoutParams;
    }

    public static int getDeviceWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        return width;
    }

    public void likeDislikeImage(String post, final ImageView image, final Item currentItem, final TextView count_likes) {


        HttpHelper httpHelper = new HttpHelper();
        AsyncHttpClient client = new AsyncHttpClient();

        MyDbHelper helperDb = new MyDbHelper(getContext());

        String iduser = helperDb.getUser().getId();

        client.get(httpHelper.getUrlApi() + "setlike?user=1&post=" + post, null, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                Log.v("aquii activo", currentItem.getLike_user() + "");
                if (currentItem.getLike_user() == 0) {
                    image.setImageResource(R.drawable.zzz_heart);
                    image.setColorFilter(ContextCompat.getColor(getContext(), R.color.red_flat));
                    currentItem.setLike_user(1);

                    Log.v("countlikes", currentItem.getCount_likes() + "---");

                } else {
                    image.setImageResource(R.drawable.zzz_heart_outline);
                    image.setColorFilter(ContextCompat.getColor(getContext(), R.color.black));
                    currentItem.setLike_user(0);


                }

                //   dialog.show();
            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                try {
                    JSONObject likes = response.getJSONObject("likes");
                    int like = likes.getInt("active");
                    if (like == 1) {
                        image.setImageResource(R.drawable.zzz_heart);
                        image.setColorFilter(ContextCompat.getColor(getContext(), R.color.red_flat));
                        int likesint = (currentItem.getCount_likes()) + 1;
                        currentItem.setCount_likes(likesint);
                        count_likes.setText(likesint + " me gusta");
                    } else {
                        image.setImageResource(R.drawable.zzz_heart_outline);
                        image.setColorFilter(ContextCompat.getColor(getContext(), R.color.black));
                        int likesint = (currentItem.getCount_likes()) - 1;
                        currentItem.setCount_likes(likesint);

                        count_likes.setText(likesint + " me gusta");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int error, Header[] header, String resp, Throwable errors) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

        });
    }

    public static int getColorWrapper(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(id);
        } else {
            //noinspection deprecation
            return context.getResources().getColor(id);
        }
    }
}