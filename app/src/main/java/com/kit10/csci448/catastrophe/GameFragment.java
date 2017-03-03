package com.kit10.csci448.catastrophe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by Adrien on 3/1/2017.
 */

public class GameFragment extends Fragment {
    private CanvasView mCanvasView;
    private Button mClearButton;
    private ImageButton mOptionsButton;
    private LinearLayout mPowerupToolbar;

    public static GameFragment newInstance() {
        Log.d(WelcomeActivity.LOG_TAG, "GameFragment : new instance");
        Bundle args = new Bundle();

        GameFragment fragment = new GameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(WelcomeActivity.LOG_TAG, "GameFragment : onCreateView");
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.activity_game, container, false);

        mCanvasView = (CanvasView) v.findViewById(R.id.canvas_view);

        mOptionsButton = (ImageButton) v.findViewById(R.id.options_button);
        mOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(WelcomeActivity.LOG_TAG, "WelcomeFragment : starting options");
                startActivityForResult(OptionsActivity.newIntent(getActivity()), WelcomeActivity.REQUEST_CODE_OPTIONS);
            }
        });

        mPowerupToolbar = (LinearLayout) v.findViewById(R.id.powerup_toolbar);
        populatePowerupToolbar();


        return v;
    }

    public void populatePowerupToolbar() {
        // temporary code for demo purposes only
        for(int i = 0; i < 3; i++) {
            final ImageButton powerupButton = new ImageButton(getActivity());
            powerupButton.setBackgroundResource(R.drawable.meowmix);
            powerupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Powerup Selected!", Toast.LENGTH_SHORT).show();
                    mPowerupToolbar.removeView(powerupButton);
                }
            });
            mPowerupToolbar.addView(powerupButton);
        }
    }
}
