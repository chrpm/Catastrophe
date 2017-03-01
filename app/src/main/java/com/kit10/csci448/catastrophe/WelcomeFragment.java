package com.kit10.csci448.catastrophe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by Adrien on 3/1/2017.
 */

public class WelcomeFragment extends Fragment {

    private ImageButton mStartButton;

    public static WelcomeFragment newInstance() {
        Log.d(WelcomeActivity.LOG_TAG, "WelcomeFragment : new instance");
        Bundle args = new Bundle();

        WelcomeFragment fragment = new WelcomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(WelcomeActivity.LOG_TAG, "WelcomeFragment : onCreateView");
        //return super.onCreateView(inflater, container, savedInstanceState);
        // TODO: create layout and implement this function
        View v = inflater.inflate(R.layout.welcome_fragment, container, false);

        mStartButton = (ImageButton) v.findViewById(R.id.start_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(WelcomeActivity.LOG_TAG, "WelcomeFragment : starting game");
                startActivityForResult(GameActivity.newIntent(getActivity()), WelcomeActivity.REQUEST_CODE_GAME);
            }
        });

        return v;

    }
}
