package com.kit10.csci448.catastrophe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Adrien on 3/1/2017.
 */

public class GameFragment extends Fragment {
    private CanvasView mCanvasView;
    private Button mClearButton;

    public static GameFragment newInstance() {
        Log.d(GameActivity.LOG_TAG, "GameFragment : new instance");
        Bundle args = new Bundle();

        GameFragment fragment = new GameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.activity_game, container, false);

        mCanvasView = (CanvasView) v.findViewById(R.id.canvas_view);

        mClearButton = (Button) v.findViewById(R.id.clear_button);
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCanvasView.clearCanvas();
            }
        });

        return v;
    }
}
