package com.android.opp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.android.opp.R;
import com.android.opp.adapters.InteresesAdapter;
import com.android.opp.helpers.MyDbHelper;
import com.android.opp.models.ImageGrid;
import com.android.opp.models.Interest;

import java.util.ArrayList;
import java.util.Arrays;

public class InteresesActivity extends AppCompatActivity {
    ArrayList listIntereses;
    Object[] tempIntereses= new Object[12];
    Context context;
    InteresesAdapter adapterIntereses;
    int desde;
    ArrayList interesesSend= new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intereses);

        Intent intent= getIntent();
        Bundle extras=intent.getExtras();


        desde= extras.getInt("desde");

        Log.v("desde",desde+"");

        context=this;

        MyDbHelper helperDb= new MyDbHelper(context);

        ArrayList interesesArray=helperDb.getInterest();

        GridView grid_intereses= (GridView) findViewById(R.id.grid_intereses);

        listIntereses= new ArrayList();

        for (int o = 0; o <interesesArray.size() ; o++) {
            Interest interes= (Interest) interesesArray.get(o);
            tempIntereses[Integer.parseInt(interes.getId_interest())]=Integer.parseInt(interes.getId()) ;
        }



        final Integer[] stringArray = Arrays.copyOf(tempIntereses, tempIntereses.length, Integer[].class);



        ;

        for (int sa = 0; sa <stringArray.length ; sa++) {

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

             adapterIntereses= new InteresesAdapter(context,listIntereses,InteresesActivity.this);
            grid_intereses.setAdapter(adapterIntereses);
        }





        grid_intereses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parent.getItemAtPosition(position);
                Log.v("posicion"," ...."+parent.getItemAtPosition(position));
            }
        });


        Button button_register_intereses= (Button) findViewById(R.id.button_register_intereses);
        button_register_intereses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interesesSend.clear();
                MyDbHelper helperDbs= new MyDbHelper(context);
                helperDbs.removeInterests();
                for (int i = 0; i <tempIntereses.length ; i++) {

                    if(tempIntereses[i]!=null){
                        interesesSend.add(tempIntereses[i]);
                        MyDbHelper helperDbi= new MyDbHelper(context);
                        helperDbi.insertInterest(i+"");
                    }

                }
                Log.v("tempoin",interesesSend.toString());
                if(desde==1){
                    finish();
                }else{
                    Intent intent= new Intent(InteresesActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });
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
}
