package com.android.opp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.opp.R;
import com.android.opp.activities.InteresesActivity;
import com.android.opp.activities.MainActivity;
import com.android.opp.adapters.SaveAdaptersImages;
import com.android.opp.helpers.HttpHelper;
import com.android.opp.helpers.MyDbHelper;
import com.android.opp.models.Comment;
import com.android.opp.models.ImageItem;
import com.android.opp.models.Interest;
import com.android.opp.models.Item;
import com.android.opp.models.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;


public class ProfileFragment extends BaseFragment{



    ArrayList itemsSaved;
    SaveAdaptersImages saveAdapter;
    TextView numero_publicacion;

    ImageView profile_image;
    TextView user_name_profile;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this, view);

        ( (MainActivity)getActivity()).updateToolbarTitle("Profile");
        user_name_profile = (TextView) view.findViewById(R.id.user_name_profile);

        GridView gridsaved= (GridView) view.findViewById(R.id.grid_saves_surveys);
        profile_image =  (ImageView) view.findViewById(R.id.profile_image);
        itemsSaved= new ArrayList();
        numero_publicacion=(TextView) view.findViewById(R.id.numero_publicacion);

        Button edit_profile= (Button) view.findViewById(R.id.edit_profile);
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), InteresesActivity.class);
                intent.putExtra("desde",1);
                startActivity(intent);

            }
        });

        saveAdapter= new SaveAdaptersImages(getContext(),itemsSaved);
        gridsaved.setAdapter(saveAdapter);
        getPosts();

        TextView texto_toolbar_header= (TextView) ((MainActivity) getActivity()).findViewById(R.id.texto_toolbar_header);
        texto_toolbar_header.setText("PERFIL");

        ImageView camera_icon= (ImageView) ((MainActivity) getActivity()).findViewById(R.id.camera_icon);
        camera_icon.setVisibility(View.GONE);

        LinearLayout container_search_toolbar= (LinearLayout) ((MainActivity) getActivity()).findViewById(R.id.container_search_toolbar);
        container_search_toolbar.setVisibility(View.GONE);

        LinearLayout container_general_toolbar= (LinearLayout) ((MainActivity) getActivity()).findViewById(R.id.container_general_toolbar);
        container_general_toolbar.setVisibility(View.VISIBLE);

        return view;
    }

    public void getPosts(){
        final ProgressDialog dialog = new ProgressDialog(getContext()); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Cargnando Perfil...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        HttpHelper httpHelper= new HttpHelper();
        AsyncHttpClient client = new AsyncHttpClient();

        MyDbHelper helperDb= new MyDbHelper(getContext());

        String iduser=helperDb.getUser().getId();
        client.get(httpHelper.getUrlApi()+"getpostsuser?user_id="+iduser, null, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                dialog.show();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"

                try {
                    JSONArray posts= response.getJSONArray("posts");
                    JSONObject user= response.getJSONObject("user");

                    MyDbHelper helperDb= new MyDbHelper(getContext());

Log.v("imagenperfil",helperDb.getUser().getImage());
                    Picasso.with(getContext()).load(helperDb.getUser().getImage()).
                        into(profile_image);

                    user_name_profile.setText(user.getString("name"));

                    numero_publicacion.setText(posts.length()+"");

                    for (int i = 0; i <posts.length() ; i++) {
                        Item item= new Item();
                        item.setId(posts.getJSONObject(i).getString("id"));
                        item.setDescription(posts.getJSONObject(i).getString("description"));
                        item.setEdad_desde(posts.getJSONObject(i).getString("edad_desde"));
                        item.setEdad_hasta(posts.getJSONObject(i).getString("edad_hasta"));
                        item.setGenero(posts.getJSONObject(i).getString("genero"));
                        item.setAlcance(posts.getJSONObject(i).getString("alcance"));
                        item.setLatitude(posts.getJSONObject(i).getString("latitude_range"));
                        item.setLongitude(posts.getJSONObject(i).getString("longitude_range"));
                        item.setCity(posts.getJSONObject(i).getString("city"));
                        item.setCountry(posts.getJSONObject(i).getString("country"));
                        item.setCreated_at(posts.getJSONObject(i).getString("created_at"));
                        item.setCount_likes(posts.getJSONObject(i).getInt("likes_count"));
                        item.setLike_user(posts.getJSONObject(i).getInt("like_user"));
                        item.setLike_user(posts.getJSONObject(i).getInt("like_user"));


                        JSONObject userObject= posts.getJSONObject(i).getJSONObject("user");


                        User userPost= new User();
                        userPost.setId(userObject.getString("id"));
                        userPost.setName(userObject.getString("name"));
                        userPost.setImage(userObject.getString("image"));
                        userPost.setType(userObject.getString("type"));
                        item.setUser(userPost);


                        item.setUser(userPost);

                        JSONArray imagesArray= posts.getJSONObject(i).getJSONArray("images");

                        ArrayList images= new ArrayList();
                        for (int j = 0; j < imagesArray.length() ; j++) {
                            images.add(new ImageItem(1,imagesArray.getJSONObject(j).getInt("id"),imagesArray.getJSONObject(j).getString("src")));
                        }
                        item.setImages(images);
                        item.setGrilla(images.size());

                        JSONArray interestArray= posts.getJSONObject(i).getJSONArray("interests");

                        ArrayList interests= new ArrayList();
                        for (int j = 0; j < interestArray.length() ; j++) {
                            interests.add(new Interest(interestArray.getJSONObject(j).getString("id"),interestArray.getJSONObject(j).getString("name")));
                        }
                        item.setInterests(interests);


                        JSONArray commentsArray= posts.getJSONObject(i).getJSONArray("commentsfirst");

                        ArrayList comments= new ArrayList();
                        for (int j = 0; j < commentsArray.length() ; j++) {

                            JSONObject userObjectComment=commentsArray.getJSONObject(j).getJSONObject("user");

                            comments.add(new Comment(commentsArray.getJSONObject(j).getInt("post_id"),commentsArray.getJSONObject(j).getInt("id"),commentsArray.getJSONObject(j).getString("message"),new User(userObjectComment.getString("id"),userObjectComment.getString("name"),userObjectComment.getString("image")),commentsArray.getJSONObject(j).getString("created_at")));
                        }
                        item.setComments(comments);


                        itemsSaved.add(item);

                        Log.v("item",item.toString());
                    }

                    saveAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();

            }

            @Override
            public void onFailure(int error, Header[] header, String resp, Throwable errors) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                dialog.dismiss();

            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });

    }

}
