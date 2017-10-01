package com.android.opp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.opp.R;
import com.android.opp.helpers.HttpHelper;
import com.android.opp.helpers.MyDbHelper;
import com.android.opp.models.User;
import com.jaychang.slm.SocialLoginManager;
import com.jaychang.slm.SocialUser;
import com.karan.churi.PermissionManager.PermissionManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {
    PermissionManager permission;
    Context context;
    EditText login_text;
    String email;
    String password;
    EditText edit_text_password_login;
    int typeRegister=1;
    SocialUser social;
    private static final String TAG = SelectRegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        context=this;



        permission=new PermissionManager() {

            @Override
            public List<String> setPermission() {
                // If You Don't want to check permission automatically and check your own custom permission
                // Use super.setPermission(); or Don't override this method if not in use
                List<String> customPermission=new ArrayList<>();
                customPermission.add(Manifest.permission.GET_ACCOUNTS);

                return customPermission;
            }
        };

        //To initiate checking permission
        permission.checkAndRequestPermissions(this);


        LinearLayout register_container = (LinearLayout) findViewById(R.id.register_container);
        register_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SelectRegisterActivity.class);
                startActivity(intent);

            }
        });

        login_text = (EditText) findViewById(R.id.login_text);
        edit_text_password_login = (EditText) findViewById(R.id.edit_text_password_login);
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(context);

        IntentFilter intentFilter = new IntentFilter("loginmail");
        broadcastManager.registerReceiver(receiver, intentFilter);

        Button button_register_facebook= (Button) findViewById(R.id.button_register_facebook);
        button_register_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeRegister=2;
                loginByFacebook();
            }
        });

        Button button_login_email = (Button) findViewById(R.id.button_login_email);
        button_login_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email= login_text.getText().toString();
                password=edit_text_password_login.getText().toString();

                int flagLogin=0;
                if(TextUtils.isEmpty(login_text.getText().toString())){
                    login_text.setError("Tienes que ingresar tu E-mail");
                    flagLogin=1;
                }

                if(TextUtils.isEmpty(edit_text_password_login.getText().toString())){
                    edit_text_password_login.setError("Tienes que ingresar tu Contraseña");
                    flagLogin=1;
                }
                if(flagLogin==0){
                    typeRegister=1;
                    login();

                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        permission.checkResult(requestCode,permissions, grantResults);
        //To get Granted Permission and Denied Permission
        ArrayList<String> granted=permission.getStatus().get(0).granted;
        ArrayList<String> denied=permission.getStatus().get(0).denied;

        Log.v("granted",granted.toString());
        Log.v("denied",denied.toString());
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if(action != null && action.equals("loginmail")) {
                login_text.setText(intent.getExtras().getString("mail"));
             }

        }
    };


    private void loginByFacebook() {
        SocialLoginManager.getInstance(this)
                .facebook()
                .login()
                .subscribe(socialUser -> {


                    Log.v("social",socialUser.toString());
                            social = socialUser;
                            email=socialUser.profile.email;
                            password=socialUser.userId;
                            login();
                        },
                        error -> {
                            Log.d(TAG, "error: " + error.getMessage());
                        });
    }

    public void loginTemp(){

        User user = new User();
        user.setName("USUARIO DEMO");
        user.setImage("imagendemo");
        user.setUser_id("23243243");
        user.setId("343434");
        user.setType("1");

        user.setUser_id("23243243");

        MyDbHelper helperDb = new MyDbHelper(context);
        helperDb.insertUser(user);


        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void login(){

            RequestParams params = new RequestParams();
            params.put("email",email);
            params.put("password",password);
            params.put("type",typeRegister);
            HttpHelper httpHelper= new HttpHelper();
            AsyncHttpClient client = new AsyncHttpClient();

            final ProgressDialog dialog = new ProgressDialog(context); // this = YourActivity
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Estamos ingresando, espera un momento...");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);

            client.post(httpHelper.getUrlApi()+"login", params, new AsyncHttpResponseHandler() {

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

                        if(responseSuccess.has("login")){

                            if (responseSuccess.getJSONObject("login").getInt("action") == 2) {
                                JSONObject userResponse = responseSuccess.getJSONObject("login").getJSONObject("data");
                                User user = new User();
                                user.setName(userResponse.getString("name"));
                                user.setImage(userResponse.getString("image"));
                                user.setUser_id(userResponse.getString("user_id_social"));
                                user.setId(userResponse.getInt("id") + "");
                                user.setType(userResponse.getInt("type") + "");

                                if(userResponse.getInt("user_id_social")!=0){
                                    user.setUser_id(userResponse.getInt("user_id_social") + "");
                                }
                                Log.v("uer", user.toString());
                                MyDbHelper helperDb = new MyDbHelper(context);
                                helperDb.insertUser(user);


                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);

                            }else{
                                Toast.makeText(context, "Hay un error en el login, puedes intentar mas adelante", Toast.LENGTH_LONG).show();
                            }

                        }else{
                            Toast.makeText(context, "Hay un error en el login, puedes intentar mas adelante", Toast.LENGTH_LONG).show();
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
                        Log.v("error",new String(responseBody));

                        if(responseError.has("login")){
                            Toast.makeText(context, responseError.getJSONObject("login").getString("error"), Toast.LENGTH_LONG).show();
                            if(responseError.getJSONObject("login").getInt("type")==2){
                                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                                intent.putExtra("userId",social.userId);
                                intent.putExtra("photoUrl",social.photoUrl);
                                intent.putExtra("accessToken",social.accessToken);
                                intent.putExtra("name",social.profile.name);
                                intent.putExtra("email",social.profile.email);
                                startActivity(intent);
                            }

                        }else{
                            Toast.makeText(context, "Hay un error en el login, puedes intentar mas adelante", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();

                }
            });

    }

}
