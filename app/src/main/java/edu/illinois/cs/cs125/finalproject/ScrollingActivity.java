package edu.illinois.cs.cs125.finalproject;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.*;

public class ScrollingActivity extends AppCompatActivity {
    Button getLyrics;
    EditText artistName;
    EditText songName;
    TextView scrollingLyrics;
    private static final String TAG = "Final Project";
    private static RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        requestQueue= Volley.newRequestQueue(this);

        artistName=(EditText) findViewById(R.id.editText_artistName);
        songName= (EditText) findViewById(R.id.editText_songName);
        getLyrics=(Button) findViewById(R.id.button);
        scrollingLyrics= (TextView) findViewById(R.id.scrollView_lyrics);

        getLyrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String songNameStr = songName.getText().toString();
                String artistNameStr = artistName.getText().toString();
            startAPICall(songNameStr, artistNameStr);
            }
        });
        }

    void startAPICall(final String song, final String artist) {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://orion.apiseeds.com/api/music/lyric/"
                            + artist + "/" + song
                            + "?apikey=" +
                            BuildConfig.API_KEY,
                    (String) null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            try {
                                Log.d(TAG, response.toString(2));
                                JSONObject childObject = response.getJSONObject("result");
                                JSONObject child2Object = childObject.getJSONObject("track");
                                String answer = child2Object.get("text").toString();
                                scrollingLyrics.setText(answer);
                            } catch (JSONException ignored) {
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.e(TAG, error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
