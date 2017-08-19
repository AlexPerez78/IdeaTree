package com.example.alexperez.ideatree;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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


        String track_info= intent.getStringExtra("track_title");
        String artist_info = intent.getStringExtra("artist");
        String album_info = intent.getStringExtra("album");
        String date_info = intent.getStringExtra("date");

        TextView track_tv = (TextView)rootView.findViewById(R.id.title_of_song);
        TextView artist_tv = (TextView)rootView.findViewById(R.id.artist_name);
        TextView album_tv = (TextView)rootView.findViewById(R.id.album_name);
        TextView date_tv = (TextView)rootView.findViewById(R.id.year);
        track_tv.setText(track_info);
        artist_tv.setText("Artist: " + artist_info);
        album_tv.setText("Album: " + album_info);
        date_tv.setText("Year: " + date_info);

        String photo_info = intent.getStringExtra("photo");
        ImageView photo_album = (ImageView)rootView.findViewById(R.id.imageView);
        Picasso.with(getContext()).load(photo_info).into(photo_album);

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
