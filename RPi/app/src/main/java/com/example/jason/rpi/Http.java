package com.example.jason.rpi;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class Http {
    private static Http instance = null;
    private static Context ctx = null;
    private static RequestQueue rq = null;
    // for now the url is saved here
    // move to db later
    private final String url = "";

    private Http(Context c) {
        ctx = c;
    }

    public static Http getInstance(Context c) {
        if (instance == null) {
            instance = new Http(c);
        }
        return instance;
    }

    private RequestQueue getRequestQueue() {
        if (rq == null) {
            rq = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return rq;
    }

    // add request to the requestqueue
    public void addToRequestQueue(Request<JSONObject> request) {
        getRequestQueue().add(request);
    }
}
