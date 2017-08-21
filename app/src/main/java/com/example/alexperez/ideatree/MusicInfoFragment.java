package com.example.alexperez.ideatree;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

        //Gathering the data that was sent from the MainActivityFragment
        String track_info= intent.getStringExtra("track_title");
        String artist_info = intent.getStringExtra("artist");
        String album_info = intent.getStringExtra("album");
        String date_info = intent.getStringExtra("date");

        //Locating and setting the data from the MainActivityFragment to the MusicInfoFragment UI
        TextView track_tv = (TextView)rootView.findViewById(R.id.title_of_song);
        TextView artist_tv = (TextView)rootView.findViewById(R.id.artist_name);
        TextView album_tv = (TextView)rootView.findViewById(R.id.album_name);
        TextView date_tv = (TextView)rootView.findViewById(R.id.released);
        track_tv.setText(track_info);
        artist_tv.setText("Artist: " + artist_info);
        album_tv.setText("Album: " + album_info);
        date_tv.setText("Released: " + date_info);

        //Handling the Photo from URL to Imageview using Picasso
        String photo_info = intent.getStringExtra("photo");
        ImageView photo_album = (ImageView)rootView.findViewById(R.id.imageView);
        Picasso.with(getContext()).load(photo_info).into(photo_album);

        //Home Button for user to have the option to head back home to MainActivityFragment
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
