package com.android.opp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.opp.R;
import com.jaychang.slm.SocialLoginManager;

public class SelectRegisterActivity extends AppCompatActivity {
    private static final String TAG = SelectRegisterActivity.class.getSimpleName();
    public static Activity activityRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_register);
        activityRegister=this;
        LinearLayout container_close_register= (LinearLayout) findViewById(R.id.container_close_register);
        container_close_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button email_button= (Button) findViewById(R.id.email_button);
        email_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectRegisterActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        Button facebook_button= (Button) findViewById(R.id.facebook_button);
        facebook_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginByFacebook();
            }
        });
    }


    private void loginByFacebook() {
        SocialLoginManager.getInstance(this)
                .facebook()
                .login()
                .subscribe(socialUser -> {

                            Intent intent = new Intent(SelectRegisterActivity.this,RegisterActivity.class);
                            intent.putExtra("userId",socialUser.userId);
                            intent.putExtra("photoUrl",socialUser.photoUrl);
                            intent.putExtra("accessToken",socialUser.accessToken);
                            intent.putExtra("name",socialUser.profile.name);
                            intent.putExtra("email",socialUser.profile.email);
                            startActivity(intent);

                        },
                        error -> {
                            Log.d(TAG, "error: " + error.getMessage());
                        });
    }

}
