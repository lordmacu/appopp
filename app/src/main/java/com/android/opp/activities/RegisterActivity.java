package com.android.opp.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.opp.R;
import com.android.opp.camera.ImagePickerActivity;
import com.android.opp.helpers.HttpHelper;
import com.android.opp.helpers.MyDbHelper;
import com.android.opp.models.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class RegisterActivity extends AppCompatActivity {


    String userId="0";
    String name;
    String mail;
    String photoUrl;
    int type;
    EditText nombre_usuario;
    EditText email_text;
    EditText password_text;
    ImageView imagen_usuario;
    Context context;
    String imageTemp="";
    boolean changeimage=false;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent intent = getIntent();
        Bundle extras =intent.getExtras();
        context=this;

        nombre_usuario = (EditText) findViewById(R.id.nombre_usuario);



        email_text = (EditText) findViewById(R.id.email_text);

        password_text = (EditText) findViewById(R.id.password_text);

        password_text.setVisibility(View.VISIBLE);


        imagen_usuario= (ImageView) findViewById(R.id.imagen_usuario);
        RelativeLayout image_change_profile= (RelativeLayout) findViewById(R.id.image_change_profile);


        TextView text_description = (TextView) findViewById(R.id.text_description);
        if(!intent.hasExtra("userId")){
            type=1;

            String gmail = null;

            Pattern gmailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
            Account[] accounts = AccountManager.get(this).getAccounts();
            for (Account account : accounts) {
                if (gmailPattern.matcher(account.name).matches()) {
                    gmail = account.name;
                }
            }
                email_text.setText(gmail);
                text_description.setText("INGRESA TU E-MAIL, NOMBRE Y CONTRASEÑA");
            image_change_profile.setVisibility(View.GONE);

        }else{
            text_description.setText("VERIFICA TUS DATOS");


            nombre_usuario.setText(extras.getString("name"));
            name=extras.getString("name");

            email_text.setText(extras.getString("email"));
            mail=extras.getString("email");

            password_text.setText(extras.getString("userId"));
            password_text.setVisibility(View.GONE);
            userId=extras.getString("userId");


            type=2;
            photoUrl=extras.getString("photoUrl");
            imageTemp=photoUrl;
            Picasso.with(getBaseContext()).load(photoUrl).centerCrop().fit().into(imagen_usuario);


        }
        LinearLayout register_container = (LinearLayout) findViewById(R.id.register_container);
        register_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        Button register_button= (Button) findViewById(R.id.register_button);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int success=0;
                if(TextUtils.isEmpty(nombre_usuario.getText())){
                    nombre_usuario.setError("Tienes que ingresar tu nombre");
                    success=1;
                }

                if(TextUtils.isEmpty(email_text.getText())){
                    email_text.setError("Tienes que ingresar tu email");
                    success=1;

                }

                if(TextUtils.isEmpty(password_text.getText())){
                    password_text.setError("Tienes que ingresar tu contraseña");
                    success=1;

                }

                if(success==0){

                    name=nombre_usuario.getText().toString();
                    mail=email_text.getText().toString();
                    userId=password_text.getText().toString();



                    if(TextUtils.isEmpty(imageTemp)){
                        imagen_usuario.setImageResource(R.mipmap.empty_profile);
                        photoUrl="";
                        showCamera();
                    }else{
                        sendRegister();
                    }

                }
            }
        });


        image_change_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userId.equals("0")){
                    imagen_usuario.setImageResource(R.mipmap.empty_profile);
                    photoUrl="";
                    showCamera();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("¿Estas seguro que quieres cambiar la foto de usuario?")
                            .setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    imagen_usuario.setImageResource(R.mipmap.empty_profile);
                                    photoUrl="";
                                    showCamera();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }





            }
        });

    }


    public void showCamera(){
        Intent intent  = new Intent(this, ImagePickerActivity.class);
        intent.putExtra("cantidad",1);
        changeimage=true;
        startActivityForResult(intent,INTENT_REQUEST_GET_IMAGES);
    }

    public void sendRegister(){

        RequestParams params = new RequestParams();
        params.put("user_id_social",userId);
        params.put("name",name);
        params.put("mail",mail);
        params.put("image",photoUrl);
        params.put("type",type);

        if(changeimage){
            try {
                params.put("filedata[image]",new File(imageTemp));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


            HttpHelper httpHelper= new HttpHelper();
            AsyncHttpClient client = new AsyncHttpClient();

            final ProgressDialog dialog = new ProgressDialog(context); // this = YourActivity
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Estamos Registrandote, espera un momento...");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);

            client.post(httpHelper.getUrlApi()+"register", params, new AsyncHttpResponseHandler() {

                @Override
                public void onStart() {
                    dialog.show();

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    dialog.dismiss();

                    try {
                        Log.v("success",new String(responseBody));

                        JSONObject responseSuccess=new JSONObject(new String(responseBody));
                        Log.v("success",new String(responseBody));
                        if(responseSuccess.has("register")) {

                            if (responseSuccess.getJSONObject("register").getInt("action") == 2) {
                                JSONObject userResponse = responseSuccess.getJSONObject("register").getJSONObject("data");
                                User user = new User();
                                user.setName(userResponse.getString("name"));
                                user.setImage(userResponse.getString("image"));
                                user.setUser_id(userResponse.getString("user_id_social"));
                                user.setId(userResponse.getInt("id") + "");
                                user.setType(userResponse.getInt("type") + "");
                                if(userResponse.getInt("user_id_social")!=0){
                                    user.setUser_id(userResponse.getInt("user_id_social") + "");
                                }

                                MyDbHelper helperDb = new MyDbHelper(context);
                                helperDb.insertUser(user);


                                Intent intent = new Intent(RegisterActivity.this, InteresesActivity.class);
                                intent.putExtra("desde",2);
                                startActivity(intent);

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    try {

                        Log.v("error",new String(responseBody));
                        JSONObject responseError=new JSONObject(new String(responseBody));

                        if(responseError.has("register")){
                            if(responseError.getJSONObject("register").getInt("action")==1){
                                Toast.makeText(context, responseError.getJSONObject("register").getString("error"), Toast.LENGTH_LONG).show();
                               // Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                //startActivity(intent);

                                Intent intent = new Intent("loginmail");
                                intent.putExtra("mail",mail);
                                LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(context);
                                broadcastManager.sendBroadcast(intent);

                                SelectRegisterActivity.activityRegister.finish();
                                finish();
                            }else{
                                Toast.makeText(context, "Hay un error en el registro, puedes intentar mas adelante", Toast.LENGTH_LONG).show();
                            }

                        }else{
                            Toast.makeText(context, "Hay un error en el registro, puedes intentar mas adelante", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();

                }
            });
    }


    @Override
    protected void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);


        if (requestCode == INTENT_REQUEST_GET_IMAGES && resuleCode == Activity.RESULT_OK ) {

            ArrayList<Uri> image_uris = intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
            for (int i = 0; i <image_uris.size() ; i++) {
                imageTemp=image_uris.get(i).toString();
                Log.v("pathh",imageTemp);
                Bitmap bmImg = BitmapFactory.decodeFile(imageTemp);
                imagen_usuario.setImageBitmap(bmImg);


                int submitRegister=0;
                if(TextUtils.isEmpty(nombre_usuario.getText())){
                    nombre_usuario.setError("Tienes que ingresar tu nombre");
                    submitRegister=1;
                }

                if(TextUtils.isEmpty(email_text.getText())){
                    email_text.setError("Tienes que ingresar tu email");
                    submitRegister=1;

                }

                if(TextUtils.isEmpty(password_text.getText())){
                    password_text.setError("Tienes que ingresar tu contraseña");
                    submitRegister=1;

                }

                if(submitRegister==0){
                    sendRegister();
                }
            }
        }

    }
}
