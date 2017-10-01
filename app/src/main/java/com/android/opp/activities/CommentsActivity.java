package com.android.opp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.opp.R;
import com.android.opp.adapters.ListCommentsAdapter;
import com.android.opp.helpers.HttpHelper;
import com.android.opp.helpers.MyDbHelper;
import com.android.opp.models.Comment;
import com.android.opp.models.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class CommentsActivity extends AppCompatActivity {


    ArrayList commentsArray;
    ListCommentsAdapter commetsAdapter;
    EditText commentario_text;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        context=this;

        Intent intent = getIntent();
        final Bundle extras = intent.getExtras();



        ListView list_commets = (ListView) findViewById(R.id.list_commets);

        //TextView emptyText= (TextView) findViewById(R.id.emptyText);

        //list_commets.setEmptyView(emptyText);

         commentsArray= new ArrayList();

        commetsAdapter= new ListCommentsAdapter(getBaseContext(),commentsArray);

        list_commets.setAdapter(commetsAdapter);

        getComments(extras.getString("id_post"));



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        ImageButton comment_button= (ImageButton) findViewById(R.id.comment_button);

         commentario_text= (EditText) findViewById(R.id.commentario_text);

        comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(commentario_text.getText().toString())){
                    sendComment(extras.getString("id_post"),commentario_text.getText().toString(),"1");
                }else{
                    commentario_text.setError("Tienes que escribir un comentario primero");
                    return;
                }
            }
        });


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Comentarios");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("??");
                finish();
            }
        });

    }


    public void sendComment(final String post, String comment, String user){




        final ProgressDialog dialog = new ProgressDialog(this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        HttpHelper httpHelper= new HttpHelper();
        AsyncHttpClient client = new AsyncHttpClient();
        MyDbHelper helperDb= new MyDbHelper(context);

        String iduser=helperDb.getUser().getId();
         client.get(httpHelper.getUrlApi()+"sendComment?post="+post+"&user="+iduser+"&comment="+comment, null, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                dialog.show();

            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                Toast.makeText(CommentsActivity.this, "Se ha enviado el comentario con éxito, en unos intantes aparecerá", Toast.LENGTH_SHORT).show();
                commentsArray.clear();
                commetsAdapter.notifyDataSetChanged();
                commentario_text.setText("");
                dialog.dismiss();
                getComments(post);
            }

            @Override
            public void onFailure(int error, Header[] header, String resp, Throwable errors) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)

                Toast.makeText(CommentsActivity.this, "Hay un error en el envio, intenta mas tarde", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }

        });



    }


    public void getComments(String post){


        HttpHelper httpHelper= new HttpHelper();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(httpHelper.getUrlApi()+"getComments?post="+post, null, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {

            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                try {
                    JSONArray comments=response.getJSONArray("comments");
                    Log.v("comments",comments.toString());

                    for (int i = 0; i <comments.length() ; i++) {

                        JSONObject oc=comments.getJSONObject(i);
                        JSONObject uo=oc.getJSONObject("user");

                        Log.v("user",uo.getString("name"));
                        commentsArray.add(new Comment(oc.getInt("post_id"),oc.getInt("id"),oc.getString("message"),new User(uo.getString("id"),uo.getString("name"),uo.getString("image")),oc.getString("created_at")));
                    }
                    commetsAdapter.notifyDataSetChanged();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }



}
