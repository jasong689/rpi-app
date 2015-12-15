package com.example.jason.rpi;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MopidyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final MopidyModule module = new MopidyModule(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mopidy);

        final TextView title = (TextView) findViewById(R.id.mopidy_song_title);
        final TextView artist = (TextView) findViewById(R.id.mopidy_song_artist);
        final ImageView albumCover = (ImageView) findViewById(R.id.mopidy_album_small);

        // first hide them
        title.setVisibility(View.GONE);
        artist.setVisibility(View.GONE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.mopidy_toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.mopidy_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_mopidy);
        navigationView.setNavigationItemSelectedListener(this);

        module.sendGetTo("player/current", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray result;
                try {
                    result = response.getJSONArray("response");
                    for (int i = 0;i < result.length();i++) {
                        JSONObject row = result.getJSONObject(i);
                        if (row.has("X-AlbumImage")) {
                            Picasso.with(albumCover.getContext()).load(row.getString("X-AlbumImage")).into(albumCover);
                        } else if (row.has("Title")) {
                            title.setText(row.getString("Title"));
                            title.setVisibility(View.VISIBLE);
                        } else if (row.has("Artist")) {
                            artist.setText(row.getString("Artist"));
                            artist.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException ex) {
                    // do nothing for now
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                return;
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent go = new Intent();

        if (id == R.id.nav_home) {
            go.setClass(this, MainActivity.class);
        } else if (id == R.id.nav_mopidy) {
            //go.setClass(this, MopidyActivity.class);
        } else if (id == R.id.nav_kodi) {
            //go.setClass(this, KodiActivity.class);
        } else if (id == R.id.nav_settings) {
            //go.setClass(this, SettingsActivity.class);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.mopidy_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        item.setChecked(false);
        startActivity(go);
        return true;
    }
}
