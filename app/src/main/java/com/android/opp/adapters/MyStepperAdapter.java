package com.android.opp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.opp.R;
import com.android.opp.fragments.StepFragment;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

import java.util.ArrayList;

public  class MyStepperAdapter extends AbstractFragmentStepAdapter {
    private ArrayList listImages=new ArrayList();

    public MyStepperAdapter(android.support.v4.app.FragmentManager fm, Context context, ArrayList list) {
        super(fm, context);
        listImages=list;
    }

    public MyStepperAdapter(android.support.v4.app.FragmentManager fm, Context context) {
        super(fm, context);
    }


    @Override
    public Step createStep(int position) {
        final StepFragment step = new StepFragment();

        Bundle b = new Bundle();
        b.putInt("current", position);
        b.putStringArrayList("list",listImages);
        step.setArguments(b);
        return step;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        //Override this method to set Step title for the Tabs, not necessary for other stepper types

        Log.v("posiito",position+" ");

        String titulo="";

        if(position==0){
            titulo="Pregunta";
        }else if(position==1){
            titulo="Publico";
        }else if(position==2){
            titulo="Ubicación";
        }else if (position==3){
            titulo="Listo";
        }

        StepViewModel.Builder builder = new StepViewModel.Builder(context)
                .setTitle(titulo);

        switch (position) {
            case 0:
                builder
                        .setNextButtonLabel("Siguiente")
                        .setBackButtonLabel("Cancelar")
                        .setNextButtonEndDrawableResId(R.drawable.ms_ic_chevron_right)
                        .setBackButtonStartDrawableResId(StepViewModel.NULL_DRAWABLE);
                break;
            case 1:
                builder
                        .setNextButtonLabel("Siguiente")
                        .setBackButtonLabel("Anterior")
                        .setBackButtonStartDrawableResId(R.drawable.ms_ic_chevron_left);
                break;
            case 2:
                builder
                        .setNextButtonLabel("Siguiente")
                        .setBackButtonLabel("Anterior")
                        .setBackButtonStartDrawableResId(R.drawable.ms_ic_chevron_left);
                break;
            case 3:
                builder.setBackButtonLabel("Verificar");
                break;
            default:
                throw new IllegalArgumentException("Unsupported position: " + position);
        }

        return builder.create();

    }
}