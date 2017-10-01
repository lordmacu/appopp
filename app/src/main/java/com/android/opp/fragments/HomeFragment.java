package com.android.opp.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.opp.R;
import com.android.opp.activities.MainActivity;
import com.android.opp.adapters.ListAdapter;
import com.android.opp.helpers.HttpHelper;
import com.android.opp.helpers.MyDbHelper;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;


public class HomeFragment extends BaseFragment {


    int fragCount;

    @BindView(R.id.cargando_text)
    TextView cargando_text;

    @BindView(R.id.ListFeed)
    ListView ListFeed;
    ArrayList listItems;
    ListAdapter itemList;
    public static HomeFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);


        ButterKnife.bind(this, view);

        Bundle args = getArguments();
        if (args != null) {
            fragCount = args.getInt(ARGS_INSTANCE);
        }

        getPosts();
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getContext());

        IntentFilter intentFilter = new IntentFilter("update");
        broadcastManager.registerReceiver(receiver, intentFilter);

        return view;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


         listItems= new ArrayList();

        itemList = new ListAdapter(getContext(),listItems);

        ListFeed.setAdapter(itemList);
        ListFeed.setEmptyView(cargando_text);
        ListFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Log.v("aquiiclick","mano");
            }
        });



        TextView texto_toolbar_header= (TextView) ((MainActivity) getActivity()).findViewById(R.id.texto_toolbar_header);
        texto_toolbar_header.setText("");

        ImageView camera_icon= (ImageView) ((MainActivity) getActivity()).findViewById(R.id.camera_icon);
        camera_icon.setVisibility(View.VISIBLE);

        LinearLayout container_search_toolbar= (LinearLayout) ((MainActivity) getActivity()).findViewById(R.id.container_search_toolbar);
        container_search_toolbar.setVisibility(View.GONE);

        LinearLayout container_general_toolbar= (LinearLayout) ((MainActivity) getActivity()).findViewById(R.id.container_general_toolbar);
        container_general_toolbar.setVisibility(View.VISIBLE);

    }


    public void getPosts(){


        MyDbHelper helperDb= new MyDbHelper(getContext());

        String iduser=helperDb.getUser().getId();
        HttpHelper httpHelper= new HttpHelper();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(httpHelper.getUrlApi()+"getposts?user="+iduser, null, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                try {
                    JSONArray posts= response.getJSONArray("posts");
                    if(posts.length()==0){
                        cargando_text.setText("No se encontraron registros");

                    }else{
                        cargando_text.setText("Cargando...");

                    }

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

                        JSONArray imagesArray= posts.getJSONObject(i).getJSONArray("images");

                        ArrayList images= new ArrayList();
                        for (int j = 0; j < imagesArray.length() ; j++) {

                            ImageItem imageIt= new ImageItem();
                            imageIt.setType(1);
                            imageIt.setId(imagesArray.getJSONObject(j).getInt("id"));
                            imageIt.setUrl(imagesArray.getJSONObject(j).getString("src"));
                            imageIt.setHeight(imagesArray.getJSONObject(j).getString("height"));
                            imageIt.setWidth(imagesArray.getJSONObject(j).getString("width"));
                            imageIt.setThumbHeight(imagesArray.getJSONObject(j).getString("heightThumb"));
                            imageIt.setThumbWidth(imagesArray.getJSONObject(j).getString("widthThumb"));
                            images.add(imageIt);
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


                        listItems.add(item);

                        Log.v("item",item.toString());
                    }

                    itemList.notifyDataSetChanged();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if(action != null && action.equals("update")) {
                listItems.clear();
                itemList.notifyDataSetChanged();

                getPosts();
            }

        }
    };
}
