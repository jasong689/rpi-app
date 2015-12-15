package com.example.jason.rpi;

import android.content.Context;

import com.android.volley.Response;

import org.json.JSONObject;

public class SystemModule extends Module {
    public SystemModule(Context c) {
        super("system/",c);
    }

    public void setAudioOutput(String a, Response.Listener<JSONObject> s, Response.ErrorListener f) {
        sendGetTo("audio/" + a, s, f);
    }

    public void getAudioOutput(Response.Listener<JSONObject> s, Response.ErrorListener f) {
        sendGetTo(s, f);
    }
}
