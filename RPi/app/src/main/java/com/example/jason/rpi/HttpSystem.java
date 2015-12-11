package com.example.jason.rpi;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class HttpSystem {
    private static HttpSystem instance = null;
    private static Context ctx = null;
    private static RequestQueue rq = null;
    // for now the url is saved here
    // move to db later
    private final String url = "";

    private HttpSystem(Context c) {
        ctx = c;
    }

    public static HttpSystem getInstance(Context c) {
        if (instance == null) {
            instance = new HttpSystem(c);
        }
        return instance;
    }

    private RequestQueue getRequestQueue() {
        if (rq == null) {
            rq = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return rq;
    }
    // set audio output
    public void setAudioOutput(String a, Response.Listener<JSONObject> s, Response.ErrorListener f) {
        String endpoint = url + "audio/" + a;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, endpoint, null, s, f);
        addToRequestQueue(request);
    }
    // get audio output
    public void getAudioOutput(Response.Listener<JSONObject> s, Response.ErrorListener f) {
        String endpoint = url + "audio/";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, endpoint, null, s, f);
        addToRequestQueue(request);
    }
    // add request to the requestqueue
    private void addToRequestQueue(Request<JSONObject> request) {
        getRequestQueue().add(request);
    }
}
