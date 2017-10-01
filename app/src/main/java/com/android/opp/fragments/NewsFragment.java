package com.android.opp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.opp.R;
import com.android.opp.activities.MainActivity;

import butterknife.ButterKnife;


public class NewsFragment extends BaseFragment{



   /* @BindView(R.id.btn_click_me)
    Button btnClickMe;
*/
    int fragCount;


    public static NewsFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_news, container, false);

        ButterKnife.bind(this, view);

        Bundle args = getArguments();
        if (args != null) {
            fragCount = args.getInt(ARGS_INSTANCE);
        }



        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


     /*   btnClickMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFragmentNavigation != null) {
                    mFragmentNavigation.pushFragment(NewsFragment.newInstance(fragCount + 1));


                }
            }
        });

*/

        LinearLayout container_search_toolbar= (LinearLayout) ((MainActivity) getActivity()).findViewById(R.id.container_search_toolbar);
        container_search_toolbar.setVisibility(View.GONE);

        ( (MainActivity)getActivity()).updateToolbarTitle((fragCount == 0) ? "News" : "Sub News "+fragCount);


    }
}
