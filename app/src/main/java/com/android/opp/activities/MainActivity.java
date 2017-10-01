package com.android.opp.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.opp.R;
import com.android.opp.camera.ImagePickerActivity;
import com.android.opp.fragments.BaseFragment;
import com.android.opp.fragments.HomeFragment;
import com.android.opp.fragments.NewsFragment;
import com.android.opp.fragments.ProfileFragment;
import com.android.opp.fragments.SearchFragment;
import com.android.opp.utils.FragmentHistory;
import com.android.opp.utils.Utils;
import com.android.opp.views.FragNavController;
import com.karan.churi.PermissionManager.PermissionManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends BaseActivity implements BaseFragment.FragmentNavigation, FragNavController.TransactionListener, FragNavController.RootFragmentListener {

    private static final int INTENT_REQUEST_GET_IMAGES = 13;

    @BindView(R.id.content_frame)
    FrameLayout contentFrame;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    PermissionManager permission;
    static final int PICK_CONTACT_REQUEST = 21;  // The request code

    private int[] mTabIconsSelected = {
            R.drawable.tab_home,
            R.drawable.tab_search,
            R.drawable.tab_share,
            R.drawable.tab_news,
            R.drawable.tab_profile};


    @BindArray(R.array.tab_name)
    String[] TABS;

    @BindView(R.id.bottom_tab_layout)
    TabLayout bottomTabLayout;

    private FragNavController mNavController;

    private FragmentHistory fragmentHistory;
    Context contex;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contex=this;
        ButterKnife.bind(this);


       // http://freegeoip.net/json/
        getCurrentLocation();
        initToolbar();

        initTab();

        fragmentHistory = new FragmentHistory();


        ImageView camera_icon = (ImageView) findViewById(R.id.camera_icon);

        camera_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCamera();
            }
        });

        mNavController = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.content_frame)
                .transactionListener(this)
                .rootFragmentListener(this, TABS.length)
                .build();


        switchTab(0);

        bottomTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                fragmentHistory.push(tab.getPosition());

                switchTab(tab.getPosition());


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                mNavController.clearStack();

                switchTab(tab.getPosition());


            }
        });
        getSupportActionBar().setDisplayShowTitleEnabled(false);


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

    }





    public void getCurrentLocation(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://freegeoip.net/json/", new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"

                JSONObject object= response;
                try {

                    SharedPreferences sharedpreferences = getSharedPreferences("mispreferencias", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("city", response.getString("city"));
                    editor.putString("latitude", response.getString("latitude"));
                    editor.putString("longitude", response.getString("longitude"));
                    editor.putString("country_code", response.getString("country_code"));
                    editor.putString("country_name", response.getString("country_name"));
                    editor.putString("time_zone", response.getString("time_zone"));
                    editor.putString("region_name", response.getString("region_name"));
                    editor.commit();


                    Log.v("citye",sharedpreferences.getString("city",""));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);

    }

    private void initTab() {
        if (bottomTabLayout != null) {
            for (int i = 0; i < TABS.length; i++) {
                bottomTabLayout.addTab(bottomTabLayout.newTab());
                TabLayout.Tab tab = bottomTabLayout.getTabAt(i);
                if (tab != null)
                    tab.setCustomView(getTabView(i));
            }
        }
    }


    private View getTabView(int position) {
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.tab_item_bottom, null);
        ImageView icon = (ImageView) view.findViewById(R.id.tab_icon);
        icon.setImageDrawable(Utils.setDrawableSelector(MainActivity.this, mTabIconsSelected[position], mTabIconsSelected[position]));

        icon.setColorFilter(ContextCompat.getColor(contex, R.color.white));

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {

        super.onStop();
    }


    public  void showCamera(){
        Intent intent  = new Intent(this, ImagePickerActivity.class);
        intent.putExtra("cantidad",4);

        startActivityForResult(intent,INTENT_REQUEST_GET_IMAGES);
    }

    private void switchTab(int position) {


        Log.v("positio--",position+" ");

        if(position==2){


            Intent intent  = new Intent(this, ImagePickerActivity.class);
            intent.putExtra("cantidad",4);

            startActivityForResult(intent,INTENT_REQUEST_GET_IMAGES);
        }else{
            mNavController.switchTab(position);
        }
        //  mNavController.switchTab(position);


//        updateToolbarTitle(position);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {


            case android.R.id.home:


                onBackPressed();
                return true;
        }


        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {

        if (!mNavController.isRootFragment()) {
            mNavController.popFragment();
        } else {

            if (fragmentHistory.isEmpty()) {
                super.onBackPressed();
            } else {


                if (fragmentHistory.getStackSize() > 1) {

                    int position = fragmentHistory.popPrevious();

                    switchTab(position);

                    updateTabSelection(position);

                } else {

                    switchTab(0);

                    updateTabSelection(0);

                    fragmentHistory.emptyStack();
                }
            }

        }
    }


    private void updateTabSelection(int currentTab){

        for (int i = 0; i <  TABS.length; i++) {
            TabLayout.Tab selectedTab = bottomTabLayout.getTabAt(i);
            if(currentTab != i) {
                selectedTab.getCustomView().setSelected(false);
            }else{
                selectedTab.getCustomView().setSelected(true);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mNavController != null) {
            mNavController.onSaveInstanceState(outState);
        }
    }

    @Override
    public void pushFragment(Fragment fragment) {
        if (mNavController != null) {
            mNavController.pushFragment(fragment);
        }
    }


    @Override
    public void onTabTransaction(Fragment fragment, int index) {
        // If we have a backstack, show the back button
        if (getSupportActionBar() != null && mNavController != null) {


            updateToolbar();

        }
    }

    public void updateToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(!mNavController.isRootFragment());
        getSupportActionBar().setDisplayShowHomeEnabled(!mNavController.isRootFragment());
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
    }


    @Override
    public void onFragmentTransaction(Fragment fragment, FragNavController.TransactionType transactionType) {
        //do fragmentty stuff. Maybe change title, I'm not going to tell you how to live your life
        // If we have a backstack, show the back button
        if (getSupportActionBar() != null && mNavController != null) {

            updateToolbar();

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);


        if (requestCode == INTENT_REQUEST_GET_IMAGES && resuleCode == Activity.RESULT_OK ) {

            ArrayList<Uri> image_uris = intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);

            //do something
            Log.v("respuesta ","aqui_"+requestCode);

            Intent intentStep = new Intent(contex, StepsActivity.class);

            ArrayList listImages= new ArrayList();

            for (int i = 0; i <image_uris.size() ; i++) {
                listImages.add(image_uris.get(i).toString());
            }
            intentStep.putStringArrayListExtra("images",listImages);
            startActivityForResult(intentStep,PICK_CONTACT_REQUEST);
        }
        else if (requestCode == PICK_CONTACT_REQUEST && resuleCode == Activity.RESULT_OK ){
            Log.v("aqui volvio","volvio del editor");
        }
    }

    @Override
    public Fragment getRootFragment(int index) {
        switch (index) {

            case FragNavController.TAB1:
                return new HomeFragment();
            case FragNavController.TAB2:
                return new SearchFragment();

            case FragNavController.TAB3:
                // return new ShareFragment();
               /* Intent intent  = new Intent(this, ImagePickerActivity.class);
                startActivityForResult(intent,INTENT_REQUEST_GET_IMAGES);
*/
            case FragNavController.TAB4:
                return new NewsFragment();
            case FragNavController.TAB5:
                return new ProfileFragment();


        }
        throw new IllegalStateException("Need to send an index that we know");
    }


//    private void updateToolbarTitle(int position){
//
//
//        getSupportActionBar().setTitle(TABS[position]);
//
//    }


    public void updateToolbarTitle(String title) {


        getSupportActionBar().setTitle(title);

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



}
