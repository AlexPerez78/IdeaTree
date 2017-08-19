package com.example.alexperez.ideatree;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private ListView lv;
    private ArrayAdapter<String> adapter;
    Map<String, ArrayList<String>> song_Information = new HashMap<>();
    private List<String> link_images;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart(){
        super.onStart();
        FetchSongTask task = new FetchSongTask();
        task.execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            FetchSongTask task = new FetchSongTask();
            task.execute();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        lv = (ListView) rootView.findViewById(R.id.listview);

        ArrayList<String> track_names = new ArrayList<>();

        adapter = new ArrayAdapter(getActivity(), R.layout.row, R.id.trackTitle, track_names);
        lv.setAdapter(adapter);
        lv.setFastScrollEnabled(true);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(getContext(), adapter.getItem(i), Toast.LENGTH_LONG).show();
                Intent musicInfo = new Intent(getActivity(), MusicInfo.class);
                musicInfo.putExtra("track_title", adapter.getItem(i)); //Get the items name
                musicInfo.putExtra("artist",song_Information.get(adapter.getItem(i)).get(0));
                musicInfo.putExtra("album",song_Information.get(adapter.getItem(i)).get(1));
                musicInfo.putExtra("photo", song_Information.get(adapter.getItem(i)).get(2));
                musicInfo.putExtra("date", song_Information.get(adapter.getItem(i)).get(3));

                System.out.println("ArrayList: " + song_Information.get(adapter.getItem(i)));
                startActivity(musicInfo);
            }
        });

        return rootView;
    }

    class FetchSongTask extends AsyncTask<Void, Void, String[]> {
        public static final String LOG_TAG = "fetchsongtask";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String[] doInBackground(Void... voids) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String songJsonStr = null;
            String[] results = null;
            String[] images = null;

            try {

                String baseUrl = "https://itunes.apple.com/search?term=Michael+jackson";
                URL url = new URL(baseUrl);


                Log.d(LOG_TAG, "URL Returned: " + url);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                songJsonStr = buffer.toString();
                Log.d(LOG_TAG, "JSON returned: " + songJsonStr);
                results = getSongTitleDataFromJson(songJsonStr,50);
                //images = getSongArtworkDataFromJson(songJsonStr,50);

                //link_images = Arrays.asList(images); //Add the links from the images function into a Arraylist to de-complile?

//                for (String s : results) {
//                    Log.d(LOG_TAG, s);
//                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return results;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            adapter.clear();
            adapter.addAll(new ArrayList<>(Arrays.asList(strings)));
        }

        private String[] getSongTitleDataFromJson(String songJsonStr, int numOfSongs)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String JSON_results = "results";
            final String JSON_TITLE = "trackName";
            final String JSON_ARTIST = "artistName";
            final String JSON_ALBUM = "collectionName";
            final String JSON_ARTWORK = "artworkUrl100";
            final String JSON_RELEASE = "releaseDate";

            JSONObject root = new JSONObject(songJsonStr);
            JSONArray songArray = root.getJSONArray(JSON_results);
            String[] resultStrs = new String[numOfSongs];
            for(int i = 0; i < songArray.length(); i++){

                JSONObject firstTitle = songArray.getJSONObject(i);

                String artwork = firstTitle.getString(JSON_ARTWORK);
                String title = firstTitle.getString(JSON_TITLE);
                String date = firstTitle.getString(JSON_RELEASE);
                String artist = "";
                String album = "";
                if(firstTitle.has(JSON_ARTIST)){
                    artist = firstTitle.getString(JSON_ARTIST);
                }
                if(firstTitle.has(JSON_ALBUM)){
                    album = firstTitle.getString(JSON_ALBUM);
                    System.out.println("Track Title: " + title +  "\n Album: " + album);
                }
                song_Information.put(title, new ArrayList<>(Arrays.asList(artist, album, artwork, date)));

                resultStrs[i] = title;
            }

            for(String s: resultStrs){
                Log.v(LOG_TAG, "Song Title: " + s);
            }

            return resultStrs;
        }

//        private String[] getSongArtworkDataFromJson(String songJsonStr, int numOfSongs)
//                throws JSONException {
//
//            // These are the names of the JSON objects that need to be extracted.
//            final String JSON_results = "results";
//            final String JSON_ARTWORK = "artworkUrl60";
//
//            JSONObject root = new JSONObject(songJsonStr);
//            JSONArray songArray = root.getJSONArray(JSON_results);
//
//            String[] resultImgs = new String[numOfSongs];
//            for(int i = 0; i < songArray.length(); i++){
//
//                JSONObject firstTitle = songArray.getJSONObject(i);
//
//                String artwork = firstTitle.getString(JSON_ARTWORK);
//
//
//                resultImgs[i] = artwork;
//            }
//
//            for(String s: resultImgs){
//                Log.v(LOG_TAG, "Link Img: " + s);
//            }
//
//            return resultImgs;
//        }
    }
}
