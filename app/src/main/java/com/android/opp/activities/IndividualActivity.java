package com.android.opp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.bakerj.infinitecards.AnimationTransformer;
import com.bakerj.infinitecards.CardItem;
import com.bakerj.infinitecards.InfiniteCardView;
import com.bakerj.infinitecards.ZIndexTransformer;
import com.bakerj.infinitecards.transformer.DefaultCommonTransformer;
import com.bakerj.infinitecards.transformer.DefaultTransformerToBack;
import com.bakerj.infinitecards.transformer.DefaultTransformerToFront;
import com.bakerj.infinitecards.transformer.DefaultZIndexTransformerCommon;
import com.android.opp.R;
import com.android.opp.adapters.CardAdapter;
import com.android.opp.helpers.OnSwipeTouchListener;
import com.android.opp.models.Item;
import com.nineoldandroids.view.ViewHelper;
import com.scottyab.HeartBeatView;

public class IndividualActivity extends Activity {

    private InfiniteCardView mCardView;
    private float x1,x2;
    static final int MIN_DISTANCE = 150;
    HeartBeatView heartbeat;
    CardAdapter mAdapter1;
    private int[] resId = {R.mipmap.imagen, R.mipmap.imagen, R.mipmap.imagen, R.mipmap
            .imagen, R.mipmap.imagen};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual2);


        Item itemIndividual= (Item) getIntent().getSerializableExtra("item");
    Log.v("aquii",itemIndividual.toString());

        mCardView = (InfiniteCardView) findViewById(R.id.view);
        mAdapter1 = new CardAdapter(itemIndividual.getImages(),IndividualActivity.this);

        TextView text_individual = (TextView) findViewById(R.id.text_individual);
        text_individual.setText(itemIndividual.getDescription());
        mCardView.setAdapter(mAdapter1);
        setStyle3();


        ConstraintLayout container_swipe= (ConstraintLayout) findViewById(R.id.container_swipe);


         heartbeat = (HeartBeatView) findViewById(R.id.heartbeat);
        heartbeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heartbeat.start();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        heartbeat.stop();
                        Toast.makeText(IndividualActivity.this, "Votacion Éxitosa", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, 1000);
            }
        });

        container_swipe.setOnTouchListener(new OnSwipeTouchListener(this) {

            public void onSwipeRight() {
               left();

            }
            public void onSwipeLeft() {
               right();

            }

        });




    }






    private void setStyle2() {
        mCardView.setClickable(true);
        mCardView.setAnimType(InfiniteCardView.ANIM_TYPE_SWITCH);
        mCardView.setAnimInterpolator(new OvershootInterpolator(-18));
        mCardView.setTransformerToFront(new DefaultTransformerToFront());
        mCardView.setTransformerToBack(new AnimationTransformer() {
            @Override
            public void transformAnimation(View view, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {
                int positionCount = fromPosition - toPosition;
                float scale = (0.8f - 0.1f * fromPosition) + (0.1f * fraction * positionCount);
                ViewHelper.setScaleX(view, scale);
                ViewHelper.setScaleY(view, scale);
                if (fraction < 0.5) {
                    ViewCompat.setRotationX(view, 180 * fraction);
                } else {
                    ViewCompat.setRotationX(view, 180 * (1 - fraction));
                }
            }

            @Override
            public void transformInterpolatedAnimation(View view, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {
                int positionCount = fromPosition - toPosition;
                float scale = (0.8f - 0.1f * fromPosition) + (0.1f * fraction * positionCount);
                ViewHelper.setTranslationY(view, -cardHeight * (0.8f - scale) * 0.5f - cardWidth * (0.02f *
                        fromPosition - 0.02f * fraction * positionCount));
            }
        });
        mCardView.setZIndexTransformerToBack(new ZIndexTransformer() {
            @Override
            public void transformAnimation(CardItem card, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {
                if (fraction < 0.4f) {
                    card.zIndex = 1f + 0.01f * fromPosition;
                } else {
                    card.zIndex = 1f + 0.01f * toPosition;
                }
            }

            @Override
            public void transformInterpolatedAnimation(CardItem card, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {

            }
        });
    }
    private void setStyle1() {
        mCardView.setClickable(true);
        mCardView.setAnimType(InfiniteCardView.ANIM_TYPE_FRONT);
        mCardView.setAnimInterpolator(new LinearInterpolator());
        mCardView.setTransformerToFront(new DefaultTransformerToFront());
        mCardView.setTransformerToBack(new DefaultTransformerToBack());
        mCardView.setZIndexTransformerToBack(new DefaultZIndexTransformerCommon());
    }


    private void setStyle3() {
        mCardView.setClickable(true);
        mCardView.setAnimType(InfiniteCardView.ANIM_TYPE_FRONT_TO_LAST);
        mCardView.setAnimInterpolator(new OvershootInterpolator(-8));
        mCardView.setTransformerToFront(new DefaultCommonTransformer());
        mCardView.setTransformerToBack(new AnimationTransformer() {
            @Override
            public void transformAnimation(View view, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {
                int positionCount = fromPosition - toPosition;
                float scale = (0.8f - 0.1f * fromPosition) + (0.1f * fraction * positionCount);
                ViewHelper.setScaleX(view, scale);
                ViewHelper.setScaleY(view, scale);
                if (fraction < 0.5) {
                    ViewCompat.setTranslationX(view, cardWidth * fraction * 1.5f);
                    ViewCompat.setRotationY(view, -45 * fraction);
                } else {
                    ViewCompat.setTranslationX(view, cardWidth * 1.5f * (1f - fraction));
                    ViewCompat.setRotationY(view, -45 * (1 - fraction));
                }
            }

            @Override
            public void transformInterpolatedAnimation(View view, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {
                int positionCount = fromPosition - toPosition;
                float scale = (0.8f - 0.1f * fromPosition) + (0.1f * fraction * positionCount);
                ViewHelper.setTranslationY(view, -cardHeight * (0.8f - scale) * 0.5f - cardWidth * (0.02f *
                        fromPosition - 0.02f * fraction * positionCount));
            }
        });
        mCardView.setZIndexTransformerToBack(new ZIndexTransformer() {
            @Override
            public void transformAnimation(CardItem card, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {
                if (fraction < 0.5f) {
                    card.zIndex = 1f + 0.01f * fromPosition;
                } else {
                    card.zIndex = 1f + 0.01f * toPosition;
                }
            }

            @Override
            public void transformInterpolatedAnimation(CardItem card, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {

            }
        });
    }



    public void left(){
        mCardView.bringCardToFront(1);
        Log.v("left","let");
    }


    public void right(){
        mCardView.bringCardToFront(mAdapter1.getCount() - 1);
        Log.v("right","right");

    }

}
