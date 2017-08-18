package com.example.alexperez.ideatree;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MusicInfoFragment extends Fragment {

    public MusicInfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_music_info, container, false);

        Intent intent = getActivity().getIntent();
        String detailString = intent.getStringExtra("track_title");

        TextView tv = (TextView)rootView.findViewById(R.id.title_of_song);
        tv.setText(detailString);

        Button home = (Button) rootView.findViewById(R.id.home);

        home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

    return rootView;
    }
}
