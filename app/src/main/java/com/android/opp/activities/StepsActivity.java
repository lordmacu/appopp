package com.android.opp.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.opp.R;
import com.android.opp.adapters.MyStepperAdapter;
import com.karan.churi.PermissionManager.PermissionManager;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;

public class StepsActivity extends AppCompatActivity implements StepperLayout.StepperListener{

    private StepperLayout mStepperLayout;
    PermissionManager permission;

    boolean flagSlide=true;
    ArrayList<String> imagesArray;
    RelativeLayout containerImages;
    Animation animSlide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        ArrayList listImages=new ArrayList();
        Intent intent = getIntent();


        if(intent.getExtras().getStringArrayList("images")!=null){
            listImages=intent.getExtras().getStringArrayList("images");

        }

        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);

        mStepperLayout.setAdapter(new MyStepperAdapter(getSupportFragmentManager(), this,listImages));

        permission=new PermissionManager() {

            @Override
            public List<String> setPermission() {
                // If You Don't want to check permission automatically and check your own custom permission
                // Use super.setPermission(); or Don't override this method if not in use
                List<String> customPermission=new ArrayList<>();
                customPermission.add(Manifest.permission.CAMERA);
                customPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                customPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                return customPermission;
            }
        };

        //To initiate checking permission
        permission.checkAndRequestPermissions(this);



        Log.v("lsitimages",listImages.toString());
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

    public void startAnimation(boolean flag){

        if(flag){
            Animation RightSwipe = AnimationUtils.loadAnimation(StepsActivity.this, R.anim.left);
            RightSwipe.setFillAfter(true);

            containerImages.startAnimation(RightSwipe);
            flagSlide=false;
        }else{
            Animation RightSwipe = AnimationUtils.loadAnimation(StepsActivity.this, R.anim.right);
            RightSwipe.setFillAfter(true);

            containerImages.startAnimation(RightSwipe);
            flagSlide=true;
        }






    }


    @Override
    public void onCompleted(View completeButton) {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onError(VerificationError verificationError) {
        Toast.makeText(this, "onError! -> " + verificationError.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {

    }
}


