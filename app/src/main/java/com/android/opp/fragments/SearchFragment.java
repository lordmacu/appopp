package com.android.opp.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.opp.R;
import com.android.opp.activities.MainActivity;
import com.android.opp.adapters.SaveAdaptersImages;
import com.android.opp.helpers.HttpHelper;
import com.android.opp.models.Comment;
import com.android.opp.models.ImageItem;
import com.android.opp.models.Interest;
import com.android.opp.models.Item;
import com.android.opp.models.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;


public class SearchFragment extends BaseFragment{



    ArrayList itemsSaved;
    SaveAdaptersImages saveAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        ButterKnife.bind(this, view);

        ( (MainActivity)getActivity()).updateToolbarTitle("Profile");

        TextView cargando_text= (TextView)view.findViewById(R.id.cargando_text);
        GridView gridsaved= (GridView) view.findViewById(R.id.grid_saves_surveys);
        gridsaved.setEmptyView(cargando_text);
        itemsSaved= new ArrayList();


        saveAdapter= new SaveAdaptersImages(getContext(),itemsSaved);
        gridsaved.setAdapter(saveAdapter);
        getPosts("");

        TextView texto_toolbar_header= (TextView) ((MainActivity) getActivity()).findViewById(R.id.texto_toolbar_header);
        texto_toolbar_header.setText("PERFIL");

        ImageView camera_icon= (ImageView) ((MainActivity) getActivity()).findViewById(R.id.camera_icon);
        camera_icon.setVisibility(View.GONE);

        LinearLayout container_search_toolbar= (LinearLayout) ((MainActivity) getActivity()).findViewById(R.id.container_search_toolbar);
        final EditText searchEdit= (EditText) ((MainActivity) getActivity()).findViewById(R.id.editMobileNo);



        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getPosts(searchEdit.getText().toString());
            }
        });

        container_search_toolbar.setVisibility(View.VISIBLE);

        LinearLayout container_general_toolbar= (LinearLayout) ((MainActivity) getActivity()).findViewById(R.id.container_general_toolbar);
        container_general_toolbar.setVisibility(View.GONE);

        return view;
    }

    public void getPosts(String search){

        HttpHelper httpHelper= new HttpHelper();
        AsyncHttpClient client = new AsyncHttpClient();

        Log.v("aquii",httpHelper.getUrlApi()+"search?q="+search);
        client.get(httpHelper.getUrlApi()+"search?q="+search, null, new JsonHttpResponseHandler() {


            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                itemsSaved.clear();
                try {
                    JSONArray posts= response.getJSONArray("posts");


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

            }

            @Override
            public void onFailure(int error, Header[] header, String resp, Throwable errors) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)

            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });

    }


}
