package com.android.opp.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.opp.R;
import com.android.opp.adapters.ImageAdapter;
import com.android.opp.adapters.InteresesAdapter;
import com.android.opp.adapters.StepAdapterStep;
import com.android.opp.adapters.TextCityAdapter;
import com.android.opp.camera.ImagePickerActivity;
import com.android.opp.helpers.HttpHelper;
import com.android.opp.helpers.MyDbHelper;
import com.android.opp.models.CityText;
import com.android.opp.models.ImageGrid;
import com.android.opp.models.ImageItem;
import com.android.opp.models.ItemEncuesta;
import com.github.zagum.switchicon.SwitchIconView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

import cz.msebera.android.httpclient.Header;

public class StepFragment extends Fragment implements Step {
    Integer currentStep;
    private static final int INTENT_REQUEST_GET_IMAGES = 12;
    ArrayList listImages;
    StepAdapterStep imagesAadapter;
    GridView gridview;
    int contadorImagenes=4;
    ArrayList currentImages;
    private PopupWindow mPopupWindow;
    private boolean showGender = true;
    TextView resultAge;
    ArrayList citiesList= new ArrayList();
    String latitude="40.4347994";
    String logintude="-3.7188575";
    String zoom="11";
    String country_name;
    ArrayAdapter<String> adapterEdadHastaArray;
    private SwitchIconView switchIcon1;
    private SwitchIconView switchIcon2;
    private SwitchIconView switchIcon3;
    private View button1;
    private View button2;
    private View button3;
    InteresesAdapter adapterIntereses;
    ArrayList listIntereses;
    TextView AlcanceText;
    ArrayList hastaHastaArray;
    ArrayList desdeEdadArray;
    TextCityAdapter cityAdapter;
    private  int desde= 0;
    private  int hasta= 0;
    String nameCity="";
    CityText cityItem;
    Spinner spinnerdesde;
    Spinner spinnerhasta;
    boolean bandera;

    TextView interes_text;
    //////

    int countList;
    EditText text_step_text;
    int cantidadPersonas=50;
    int genderType=3;
    Object[] tempIntereses= new Object[12];
    String textEncuesta="";
    /////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v=null;

        Bundle bundle=getArguments();



        currentStep=bundle.getInt("current");
        currentImages=bundle.getStringArrayList("list");


        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("mispreferencias", Context.MODE_PRIVATE);

        latitude= sharedpreferences.getString("latitude","");
        logintude= sharedpreferences.getString("longitude","");
        nameCity= sharedpreferences.getString("city","");
        country_name= sharedpreferences.getString("country_name","");

        cityItem=  new CityText(latitude,logintude,nameCity,country_name);

        hideKeyboard(getContext());


        if(currentStep==0){
            v = inflater.inflate(R.layout.step_one, container, false);

            text_step_text= (EditText) v.findViewById(R.id.text_step_text);

            text_step_text.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    textEncuesta=s.toString();
                }
            });


            gridview = (GridView) v.findViewById(R.id.gridview);
            listImages= new ArrayList();
            imagesAadapter= new StepAdapterStep(getContext(),listImages);

            for (int o = 0; o <currentImages.size() ; o++) {
                imagesAadapter.add(new ImageItem(1,o,currentImages.get(o).toString()));

            }

            contadorImagenes=contadorImagenes-imagesAadapter.getCount();
            Log.v("currentImages "," "+contadorImagenes);

            if(listImages.size()<4){
                imagesAadapter.add(new ImageItem(2,""));
            }

            gridview.setAdapter(imagesAadapter);

            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ImageItem item = (ImageItem) parent.getItemAtPosition(position);


                    if(item.getType()==2){
                        Intent intent  = new Intent(getContext(), ImagePickerActivity.class);

                        intent.putExtra("cantidad",contadorImagenes);
                        startActivityForResult(intent,INTENT_REQUEST_GET_IMAGES);
                    }else{

                         countList=gridview.getCount();
                        ArrayList listtemp=new ArrayList();

                        for (int i = 0; i <countList ; i++) {
                            ImageItem itemIm = (ImageItem) gridview.getItemAtPosition(i);
                            if(position!= i){
                                if(itemIm.getType()==1){
                                    listtemp.add(itemIm);
                                }
                            }
                        }
                        contadorImagenes=contadorImagenes+1;

                        if(listtemp.size()<4){
                            listtemp.add(new ImageItem(2,999,""));
                        }
                        gridview.setAdapter(null);

                        ImageAdapter imagesAadapter= new ImageAdapter(getContext(),listtemp);

                        gridview.setAdapter(imagesAadapter);
                    }
                }
            });

        }else if(currentStep==1){




            LayoutInflater inflaterView = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // Inflate the custom layout/view
            final View customView = inflaterView.inflate(R.layout.custom_layout,null);


            v = inflater.inflate(R.layout.step_two, container, false);
            RelativeLayout FirstSquare= (RelativeLayout) v.findViewById(R.id.first_square);
            Calendar now = Calendar.getInstance();
            now.add(Calendar.YEAR, -13);


            Calendar maxYear = Calendar.getInstance();
            maxYear.add(Calendar.YEAR, -13);
            maxYear.add(Calendar.MONTH, 12);


            Calendar minYear = Calendar.getInstance();
            minYear.add(Calendar.YEAR, -99);
             resultAge = (TextView) v.findViewById(R.id.result_age);

            if(desde==0 && hasta==0){
                resultAge.setText("15 - 23");

            }else{
                resultAge.setText(desde+" - "+hasta);

            }

            desdeEdadArray= new ArrayList();
            for (int sd = 15; sd <99 ; sd++) {
                desdeEdadArray.add(sd);
            }

            hastaHastaArray= new ArrayList();
            for (int sh = 15; sh <99 ; sh++) {
                hastaHastaArray.add(sh);
            }

            FirstSquare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bandera=false;
                    LayoutInflater layoutInflater
                            = (LayoutInflater)getContext()
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View alertLayout = layoutInflater.inflate(R.layout.dates_popup, null);

                    spinnerhasta = (Spinner) alertLayout.findViewById(R.id.hasta_text);
                    spinnerdesde = (Spinner) alertLayout.findViewById(R.id.desde_text);





                    ArrayAdapter<String> adapterEdadDesdeArray = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, desdeEdadArray);
                    spinnerdesde.setAdapter(adapterEdadDesdeArray);




                     adapterEdadHastaArray = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, hastaHastaArray);
                    spinnerhasta.setAdapter(adapterEdadHastaArray);



                    if(desde==0){
                        spinnerdesde.setSelection(desdeEdadArray.indexOf(15));
                    }else{
                        spinnerdesde.setSelection(desdeEdadArray.indexOf(desde));
                    }
                    if(hasta==0){
                        spinnerhasta.setSelection(hastaHastaArray.indexOf(23));
                    }else{
                        spinnerhasta.setSelection(hastaHastaArray.indexOf(hasta));
                    }
                    Log.v("desde hasta","desde "+desde+" hasta"+hasta );



                    spinnerdesde.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            if(desde!=Integer.parseInt(spinnerdesde.getSelectedItem().toString())){


                                hastaHastaArray.clear();
                                int positionItem= (int)parent.getItemAtPosition(position);
                                for (int sdselected = positionItem; sdselected <99 ; sdselected++) {
                                    hastaHastaArray.add(sdselected);
                                }
                                adapterEdadHastaArray.notifyDataSetChanged();
                            }
                            desde =  Integer.parseInt(spinnerdesde.getSelectedItem().toString());
                            hasta =  Integer.parseInt(spinnerhasta.getSelectedItem().toString());


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            Log.v("nada seleccionado","desde");
                        }
                    });
                    spinnerhasta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            desde =  Integer.parseInt(spinnerdesde.getSelectedItem().toString());
                            hasta =  Integer.parseInt(spinnerhasta.getSelectedItem().toString());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            Log.v("nada seleccionado","hasta");

                        }
                    });


                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Rango de Edad");
                    // this is set the view from XML inside AlertDialog
                    alert.setView(alertLayout);
                    // disallow cancel of AlertDialog on click of back button and outside touch
                    alert.setCancelable(false);
                    alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            hideKeyboard(getContext());

                        }
                    });

                    alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            resultAge.setText(desde+" - "+hasta);
                            desde =  Integer.parseInt(spinnerdesde.getSelectedItem().toString());
                            hasta =  Integer.parseInt(spinnerhasta.getSelectedItem().toString());
                            hideKeyboard(getContext());

                        }
                    });
                    AlertDialog dialog = alert.create();
                    dialog.show();



                    if(resultAge.equals("")){
                        resultAge.setVisibility(View.GONE);

                    }else{
                        resultAge.setVisibility(View.VISIBLE);

                    }
                }
            });


            final RelativeLayout SecondSquare= (RelativeLayout) v.findViewById(R.id.second_square);
            SecondSquare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {





                    LayoutInflater layoutInflater
                            = (LayoutInflater)getContext()
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View popupView = layoutInflater.inflate(R.layout.custom_layout, null);



                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Selecciona Género");
                    alert.setView(popupView);
                    alert.setCancelable(false);
                    alert.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                        }
                    });

                    final TextView ResultGender= (TextView) v.findViewById(R.id.result_gender);

                   // Button ButtonAcept=(Button) popupView.findViewById(R.id.button_acept);

                    final ImageView femaleMale=  (ImageView) v.findViewById(R.id.male_female_fragment);
                    final ImageView Male=  (ImageView) v.findViewById(R.id.male_fragment);
                    final ImageView Female=  (ImageView) v.findViewById(R.id.female_fragment);
                    alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(genderType==1){
                                Male.setVisibility(View.VISIBLE);
                                Female.setVisibility(View.GONE);
                                femaleMale.setVisibility(View.GONE);
                                ResultGender.setText("Hombres");

                            }else if(genderType==2){
                                Male.setVisibility(View.GONE);
                                Female.setVisibility(View.VISIBLE);
                                femaleMale.setVisibility(View.GONE);
                                ResultGender.setText("Mujeres");

                            }else if(genderType==3){
                                Male.setVisibility(View.GONE);
                                Female.setVisibility(View.GONE);
                                femaleMale.setVisibility(View.VISIBLE);
                                ResultGender.setText("Ambos");

                            }
                            dialog.dismiss();
                            ResultGender.setVisibility(View.VISIBLE);
                            showGender=true;
                        }
                    });
                    final AlertDialog dialog = alert.create();
                    dialog.show();







                    switchIcon1 = (SwitchIconView) popupView.findViewById(R.id.switchIconView1);
                    switchIcon2 = (SwitchIconView) popupView.findViewById(R.id.switchIconView2);
                    switchIcon3 = (SwitchIconView) popupView.findViewById(R.id.switchIconView3);
                    button1 = popupView.findViewById(R.id.button1);
                    button2 = popupView.findViewById(R.id.button2);
                    button3 = popupView.findViewById(R.id.button3);
                    switchIcon3.setIconEnabled(true);

                    if(genderType==1){
                        switchIcon1.setIconEnabled(true);
                        switchIcon3.setIconEnabled(false);
                        switchIcon2.setIconEnabled(false);

                    }else if(genderType==2){
                        switchIcon2.setIconEnabled(true);
                        switchIcon1.setIconEnabled(false);
                        switchIcon3.setIconEnabled(false);
                    }else if(genderType==3) {
                        switchIcon3.setIconEnabled(true);
                        switchIcon1.setIconEnabled(false);
                        switchIcon2.setIconEnabled(false);

                    }

                    button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switchIcon3.setIconEnabled(false);
                            switchIcon1.setIconEnabled(true);
                            switchIcon2.setIconEnabled(false);
                            genderType=1;

                        }
                    });
                    button2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switchIcon3.setIconEnabled(false);
                            switchIcon1.setIconEnabled(false);
                            switchIcon2.setIconEnabled(true);
                            genderType=2;



                        }
                    });
                    button3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switchIcon3.setIconEnabled(true);
                            switchIcon1.setIconEnabled(false);
                            switchIcon2.setIconEnabled(false);
                            genderType=3;


                        }
                    });




                    if(showGender){

                        showGender=false;
                    }



                }
            });


            final RelativeLayout ThirdSquare= (RelativeLayout) v.findViewById(R.id.third_square);


            AlcanceText= (TextView) v.findViewById(R.id.alcance_text);
            interes_text= (TextView) v.findViewById(R.id.interes_text);

            AlcanceText.setText("50 personas");


            ThirdSquare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setTitle("Selecciona Género");
                        alert.setCancelable(false);


                        // add a list
                        final String[] NumberPerson = {"50 personas", "100 personas", "300 personas", "500 personas", "1000 personas"};
                        alert.setItems(NumberPerson, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                AlcanceText.setText(NumberPerson[which]);

                                if(which==0){
                                    cantidadPersonas=50;
                                }else if(which==1){
                                    cantidadPersonas=100;

                                }else if(which==2){
                                    cantidadPersonas=300;

                                }else if(which==3){
                                    cantidadPersonas=500;

                                }else if(which==4){
                                    cantidadPersonas=1000;

                                }

                            }
                        });

                        alert.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                            }
                        });


                        alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        final AlertDialog dialog = alert.create();
                        dialog.show();
                    }
                });



            final RelativeLayout FourSquare= (RelativeLayout) v.findViewById(R.id.four_square);


            FourSquare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    LayoutInflater layoutInflater
                            = (LayoutInflater)getContext()
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View alertLayout = layoutInflater.inflate(R.layout.intereses_popup, null);


                    GridView grid_intereses= (GridView) alertLayout.findViewById(R.id.grid_intereses);

                    listIntereses= new ArrayList();

                    final Integer[] stringArray = Arrays.copyOf(tempIntereses, tempIntereses.length, Integer[].class);

                    for (int sa = 0; sa <stringArray.length ; sa++) {
                        Log.v("saasasa",stringArray[sa]+"");

                        if(sa==0){
                            if(stringArray[sa]!=null){
                                listIntereses.add(new ImageGrid(1,"Fitness",R.drawable.zzz_run,2));
                            }else{
                                listIntereses.add(new ImageGrid(1,"Fitness",R.drawable.zzz_run,1));

                            }
                        }

                        if(sa==1){
                            if(stringArray[sa]!=null){
                                listIntereses.add(new ImageGrid(2,"Política",R.drawable.zzz_calendar,2));
                            }else{
                                listIntereses.add(new ImageGrid(2,"Política",R.drawable.zzz_calendar,1));

                            }
                        }

                        if(sa==2){
                            if(stringArray[sa]!=null){
                                listIntereses.add(new ImageGrid(3,"Moda",R.drawable.zzz_hanger,2));
                            }else{
                                listIntereses.add(new ImageGrid(3,"Moda",R.drawable.zzz_hanger,1));

                            }
                        }

                        if(sa==3){
                            if(stringArray[sa]!=null){
                                listIntereses.add(new ImageGrid(4,"Deportes",R.drawable.zzz_bike,2));
                            }else{
                                listIntereses.add(new ImageGrid(4,"Deportes",R.drawable.zzz_bike,1));

                            }
                        }

                        if(sa==4){
                            if(stringArray[sa]!=null){
                                listIntereses.add(new ImageGrid(5,"Conciertos",R.drawable.zzz_music_note,2));
                            }else{
                                listIntereses.add(new ImageGrid(5,"Conciertos",R.drawable.zzz_music_note,1));

                            }
                        }

                        if(sa==5){
                            if(stringArray[sa]!=null){
                                listIntereses.add(new ImageGrid(6,"Fiesta",R.drawable.zzz_emoticon_cool,2));
                            }else{
                                listIntereses.add(new ImageGrid(6,"Fiesta",R.drawable.zzz_emoticon_cool,1));

                            }
                        }

                        if(sa==6){
                            if(stringArray[sa]!=null){
                                listIntereses.add(new ImageGrid(7,"Cocina",R.drawable.zzz_food,2));
                            }else{
                                listIntereses.add(new ImageGrid(7,"Cocina",R.drawable.zzz_food,1));

                            }
                        }

                        if(sa==7){
                            if(stringArray[sa]!=null){
                                listIntereses.add(new ImageGrid(8,"Fotografia",R.drawable.zzz_image_album,2));
                            }else{
                                listIntereses.add(new ImageGrid(8,"Fotografia",R.drawable.zzz_image_album,1));

                            }
                        }

                        if(sa==8){
                            if(stringArray[sa]!=null){
                                listIntereses.add(new ImageGrid(9,"Aspecto Físico",R.drawable.zzz_tshirt_v,2));
                            }else{
                                listIntereses.add(new ImageGrid(9,"Aspecto Físico",R.drawable.zzz_tshirt_v,1));

                            }
                        }
                        if(sa==9){
                            if(stringArray[sa]!=null){
                                listIntereses.add(new ImageGrid(10,"Musica",R.drawable.zzz_headphones,2));
                            }else{
                                listIntereses.add(new ImageGrid(10,"Musica",R.drawable.zzz_headphones,1));

                            }
                        }
                        if(sa==10){
                            if(stringArray[sa]!=null){
                                listIntereses.add(new ImageGrid(11,"Cine",R.drawable.zzz_movie,2));
                            }else{
                                listIntereses.add(new ImageGrid(11,"Cine",R.drawable.zzz_movie,1));

                            }
                        }
                        if(sa==11){
                            if(stringArray[sa]!=null){
                                listIntereses.add(new ImageGrid(12,"Conocer Gente",R.drawable.zzz_account_multiple_plus,2));
                            }else{
                                listIntereses.add(new ImageGrid(12,"Conocer Gente",R.drawable.zzz_account_multiple_plus,1));

                            }
                        }
                    }


                    grid_intereses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            parent.getItemAtPosition(position);
                            Log.v("posicion"," ...."+parent.getItemAtPosition(position));
                        }
                    });


                    adapterIntereses= new InteresesAdapter(getContext(),listIntereses,StepFragment.this);
                    grid_intereses.setAdapter(adapterIntereses);

                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Selecciona Intereses");
                    // this is set the view from XML inside AlertDialog
                    alert.setView(alertLayout);
                    // disallow cancel of AlertDialog on click of back button and outside touch
                    alert.setCancelable(false);
                    alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });

                    alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Integer[] stringArray = Arrays.copyOf(tempIntereses, tempIntereses.length, Integer[].class);

                            int contadorintereses=0;
                            for (int cli = 0; cli <stringArray.length ; cli++) {

                                if(stringArray[cli]!=null){
                                    contadorintereses++;
                                }
                            }
                            Log.v("kjkjk__",contadorintereses+"");

                            interes_text.setText(contadorintereses+"");
                        }
                    });
                    AlertDialog dialog = alert.create();
                    dialog.show();



                }
            });

        }else if(currentStep==2){
            v = inflater.inflate(R.layout.step_trhee, container, false);
           final ImageView mapa_global= (ImageView) v.findViewById(R.id.mapa_global);

            final EditText city_name= (EditText) v.findViewById(R.id.edit_city);

            Button buttoncities=(Button) v.findViewById(R.id.buttoncities);

            buttoncities.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    getCities(city_name.getText().toString());

                    LayoutInflater layoutInflater
                            = (LayoutInflater)getContext()
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View popupView = layoutInflater.inflate(R.layout.list_cities, null);





                    cityAdapter= new TextCityAdapter(getContext(),citiesList);



                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Selecciona una localización");
                    alert.setView(popupView);
                    alert.setCancelable(false);
                    alert.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                        }
                    });



                    ListView ListCities= (ListView) popupView.findViewById(R.id.list_cities_list);

                    RelativeLayout empty_cities= (RelativeLayout) v.findViewById(R.id.empty_cities);
                    ListCities.setEmptyView(empty_cities);
                    ListCities.setAdapter(cityAdapter);

                    final AlertDialog dialog = alert.create();

                    ListCities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                            cityItem= (CityText) parent.getItemAtPosition(position);
                            logintude=cityItem.getLatitude();
                            latitude=cityItem.getLongitude();

                            String url="http://maps.google.com/maps/api/staticmap?center="+latitude+","+logintude+"&zoom="+zoom+"&size=400x300";
                            Picasso.with(getContext()).load(url).centerCrop().fit().into(mapa_global);

                            dialog.dismiss();
                        }
                    });


                    dialog.show();


                }
            });

            mapa_global.setVisibility(View.GONE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    String url="http://maps.google.com/maps/api/staticmap?center="+latitude+","+logintude+"&zoom="+zoom+"&size=400x300";
                    Picasso.with(getContext()).load(url).centerCrop().fit().into(mapa_global);

                    mapa_global.setVisibility(View.VISIBLE);

                }
            }, 1);





            city_name.setText(cityItem.getName());




        }else{
            v = inflater.inflate(R.layout.step_four, container, false);

        }

        return v;
    }


    public void onYourClick(boolean groupPosition, int position,ImageGrid interesObject){


        if(groupPosition){
            tempIntereses[position]=interesObject.getId();
            listIntereses.set(position,new ImageGrid(interesObject.getId(),interesObject.getTitle(),interesObject.getResource(),2));

        }else{
            tempIntereses[position]=null;
            listIntereses.set(position,new ImageGrid(interesObject.getId(),interesObject.getTitle(),interesObject.getResource(),1));

        }
        adapterIntereses.notifyDataSetChanged();


    }



    public void getCities(final String city){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://photon.komoot.de/api/?q="+city, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                citiesList.clear();

                cityAdapter.notifyDataSetChanged();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"

                JSONObject object= response;

                try {
                    String latitude="";
                    String longitude="";
                    String country="";
                    String name="";
                    citiesList.clear();

                    JSONArray features=response.getJSONArray("features");

                    if(features.length()>0){
                        for (int i = 0; i < 3 ; i++) {
                            JSONObject geometry= features.getJSONObject(i);

                            if(geometry.has("geometry")){
                                JSONObject geometryObject=geometry.getJSONObject("geometry");
                                if(geometryObject.has("coordinates")){
                                    JSONArray coordinates= geometryObject.getJSONArray("coordinates");

                                    latitude=coordinates.get(0).toString();
                                    longitude=coordinates.get(1).toString();
                                }

                            }

                            if(geometry.has("properties")){
                                JSONObject geometryObject=geometry.getJSONObject("properties");
                                country=geometryObject.getString("country");
                                name=geometryObject.getString("name");
                                CityText CityModel= new CityText();
                                CityModel.setName(name);
                                CityModel.setLatitude(latitude);
                                CityModel.setLongitude(longitude);
                                CityModel.setCountry(country);
                                citiesList.add(CityModel);


                            }

                            //String latitude=

                        }
                    }

                    cityAdapter.notifyDataSetChanged();


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


    public int getAge (int year, int month, int day) {

        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, noofyears;

        y = cal.get(Calendar.YEAR);// current year ,
        m = cal.get(Calendar.MONTH);// current month
        d = cal.get(Calendar.DAY_OF_MONTH);//current day
        cal.set(year, month, day);// here ur date
        noofyears = y - cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH))
                || ((m == cal.get(Calendar.MONTH)) && (d < cal
                .get(Calendar.DAY_OF_MONTH)))) {
            --noofyears;
        }
        if (noofyears < 0)
            throw new IllegalArgumentException("age < 0");
        System.out.println(noofyears);
        return noofyears;
    }


    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);

        if (requestCode == INTENT_REQUEST_GET_IMAGES && resuleCode == Activity.RESULT_OK ) {

            ArrayList<Uri> image_uris = intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);


            if(image_uris.size()!=0){



                 countList=gridview.getCount();
                ArrayList listtemp=new ArrayList();

                for (int i = 0; i <countList ; i++) {
                    ImageItem itemIm = (ImageItem) gridview.getItemAtPosition(i);

                    if(itemIm.getType()==1){
                        listtemp.add(itemIm);
                    }
                }



                gridview.setAdapter(null);



                ArrayList listImages= new ArrayList();

                ImageAdapter imagesAadapter= new ImageAdapter(getContext(),listImages);
                gridview.setAdapter(imagesAadapter);

                for (int i = 0; i <listtemp.size() ; i++) {

                    ImageItem image= (ImageItem) listtemp.get(i);

                    imagesAadapter.add(new ImageItem(1,i,image.getUrl()));

                }
                for (int j = 0; j <image_uris.size() ; j++) {
                    imagesAadapter.add(new ImageItem(1,j,image_uris.get(j).toString()));
                 }


                if(imagesAadapter.getCount()==4){
                    contadorImagenes=0;
                }

                if(imagesAadapter.getCount()==3){
                    contadorImagenes=1;
                }

                if(imagesAadapter.getCount()==2){
                    contadorImagenes=2;
                }

                if(imagesAadapter.getCount()==1){
                    contadorImagenes=3;
                }

                if(imagesAadapter.getCount()==0){
                    contadorImagenes=4;
                }

                if(listImages.size()<4){
                    imagesAadapter.add(new ImageItem(2,999,""));
                }

                imagesAadapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public VerificationError verifyStep() {


        if(currentStep==0){



            ArrayList listImages=new ArrayList();
            int contador=0;
            for (int i = 0; i <gridview.getCount() ; i++) {
                ImageItem itemIm = (ImageItem) gridview.getItemAtPosition(i);
                if(itemIm.getType()==1){
                    listImages.add(itemIm.getUrl());
                    contador++;
                }
            }

            JSONArray arrayImages = new JSONArray(listImages);




            if(TextUtils.isEmpty(textEncuesta)) {
                return new VerificationError("Selecciona una descripción para tu encuesta");
            }else if(contador<2){
                return new VerificationError("Necesitas al menos dos imagenes para tu encuesta");
            }else{

                SharedPreferences sharedpreferences = getActivity().getSharedPreferences("tempCreador", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("encuesta", textEncuesta+"");
                editor.putString("imagenes", arrayImages+"");

                editor.commit();


                return null;
            }
        }else if(currentStep==1){


            Integer[] stringArray = Arrays.copyOf(tempIntereses, tempIntereses.length, Integer[].class);

            int contadorIntereses=0;
            for (int ia = 0; ia <stringArray.length ; ia++) {
                if(stringArray[ia]!=null){
                    contadorIntereses++;
                }
            }




            if(contadorIntereses==0){
                return new VerificationError("Selecciona al menos un interés");

            }else{


                SharedPreferences sharedpreferences = getActivity().getSharedPreferences("tempCreador", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();


                if(desde==0 && hasta==0){
                    editor.putString("desde","15" );
                    editor.putString("hasta","23" );
                }else{
                    editor.putString("desde",desde+"" );
                    editor.putString("hasta",hasta+"" );
                }


                editor.putString("genderType",genderType+"" );
                editor.putString("cantidadPersonas",cantidadPersonas+"" );
                editor.putString("intereses", Arrays.toString(stringArray)+"");

                editor.commit();
                return null;
            }
           // return null;
        }else if(currentStep==2){


            SharedPreferences sharedpreferences = getActivity().getSharedPreferences("tempCreador", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("localization",cityItem.toJson()+"" );


            editor.commit();



            String encuesta= sharedpreferences.getString("encuesta","");
            String imagenes=sharedpreferences.getString("imagenes","");

            String desde=sharedpreferences.getString("desde","" );
            String hasta=sharedpreferences.getString("hasta","" );
            String gender=sharedpreferences.getString("genderType","" );
            String cantidad=sharedpreferences.getString("cantidadPersonas","" );
            String intereses=sharedpreferences.getString("intereses","");
            String localization=sharedpreferences.getString("localization","" );


            ItemEncuesta item= new ItemEncuesta();
            item.setEncuesta(encuesta);
            item.setImagenes(imagenes);
            item.setDesde(desde);
            item.setHasta(hasta);
            item.setGender(gender);
            item.setCantidadPersonas(cantidad);
            item.setIntereses(intereses);
            item.setLocalizacion(localization);
            saveEncuesta(item);

            MyDbHelper helperDb= new MyDbHelper(getContext());
            helperDb.insertNewAnuncio(item);



            return null;
        }else{

            Intent intent = new Intent("update");

            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getContext());
            broadcastManager.sendBroadcast(intent);



            getActivity().finish();
            return null;
        }

        //return new VerificationError("error");

        //return null if the user can go to the next step, create a new VerificationError instance otherwise
    }

    public File saveBitmapToFile(File file){
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }

    public void saveEncuesta(ItemEncuesta item){

        RequestParams params = new RequestParams();

        try {
            JSONArray imagesobject = new JSONArray(item.getImagenes());

            ArrayList listimages=new ArrayList();
            for (int i = 0; i <imagesobject.length() ; i++) {
                params.put("filedata["+i+"]",new File(imagesobject.getString(i)));
             }


            params.setUseJsonStreamer(false);



            params.put("comentario",item.getEncuesta());
            params.put("desde",item.getDesde());
            params.put("hasta",item.getHasta());
            params.put("gender",item.getGender());
            params.put("cantidad",item.getCantidadPersonas());

            JSONObject localizacion = new JSONObject(item.getLocalizacion());

            JSONArray getintereses = new JSONArray(item.getIntereses());

            ArrayList interesArray= new ArrayList();
            for (int j = 0; j <getintereses.length() ; j++) {
                if(!getintereses.getString(j).equals("null")){
                    interesArray.add(getintereses.getString(j));
                }
            }
            params.put("intereses",interesArray);

            MyDbHelper helperDb= new MyDbHelper(getContext());

            params.put("latitude",localizacion.get("latitude"));
            params.put("longitude",localizacion.get("longitude"));
            params.put("city",localizacion.get("name"));
            params.put("country",localizacion.get("country"));
            params.put("user",helperDb.getUser().getId());


            HttpHelper httpHelper= new HttpHelper();
            AsyncHttpClient client = new AsyncHttpClient();

            final ProgressDialog dialog = new ProgressDialog(getContext()); // this = YourActivity
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Publicando Encuesta. Por favor espera...");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);

            client.post(httpHelper.getUrlApi()+"setPost", params, new AsyncHttpResponseHandler() {

                @Override
                public void onStart() {
                    dialog.show();

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                          Log.v("success",new String(responseBody));

                    dialog.dismiss();

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.v("responseerrro",new String(responseBody));
                    dialog.dismiss();

                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onSelected() {
        //update UI when selected

    }

    @Override
    public void onError(@NonNull VerificationError error) {
        //handle error inside of the fragment, e.g. show error on EditText

        Toast.makeText(getContext(), error.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

}
