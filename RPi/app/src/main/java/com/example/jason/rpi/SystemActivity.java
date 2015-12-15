package com.example.jason.rpi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.ToggleButton;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public class SystemActivity extends Activity {
    private final SystemModule module = new SystemModule(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system);

        // click handler for audio
        ToggleButton output = (ToggleButton) findViewById(R.id.audio_output);
        output.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAudioOutput(v);
            }
        });
        Switch auto = (Switch) findViewById(R.id.enable_auto);
        auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAudioAuto(v);
            }
        });

        setAudioOutputStatus();
    }

    private void setAudioOutput(View v) {
        final ToggleButton button = (ToggleButton) v;
        if (!isOnline()) return;

        setAudioOutputAs(button.isChecked() ? "hdmi" : "analog");
    }

    private void setAudioOutputAs(String output) {
        module.setAudioOutput(output, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.w("System", response.toString());
                try {
                    int audio = response.getInt("value");
                    updateAudioButtonEnabled(audio);
                } catch (JSONException ex) {
                    // do nothing for now
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError err) {
            }
        });
    }

    private void setAudioAuto(View v) {
        final Switch sw = (Switch) v;
        final ToggleButton button = (ToggleButton) findViewById(R.id.audio_output);
        if (!isOnline()) return;

        String set = sw.isChecked() ? "auto" : (button.isChecked()? "hdmi":"analog");
        setAudioOutputAs(set);
    }

    private void setAudioOutputStatus() {
        if (!isOnline()) return;
        final ToggleButton button = (ToggleButton) findViewById(R.id.audio_output);
        final Switch autoSwitch = (Switch) findViewById(R.id.enable_auto);

        module.getAudioOutput(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int audio = response.getInt("value");
                    updateAudioButtonEnabled(audio);
                    if (audio == 0) {
                        autoSwitch.setChecked(true);
                    } else {
                        button.setChecked(audio == 2);
                    }
                } catch (JSONException ex) {
                    // do something
                    button.setChecked(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError err) {

            }
        });
    }

    private void updateAudioButtonEnabled(int audio) {
        ToggleButton button = (ToggleButton) findViewById(R.id.audio_output);
        button.setEnabled(audio > 0);
    }

    private Boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}
