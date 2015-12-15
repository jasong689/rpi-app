package com.example.jason.rpi;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Module {
    private static final String baseUrl = "";
    private final String resourceUrl;
    private Http http;
    private Context ctx;

    public Module(String url, Context c) {
        if (url == null || c == null) throw new NullPointerException("url or context cannot be null");
        resourceUrl = url;
        ctx = c;
    }

    private Http getHttpInstance() {
        if (http == null) {
            http = Http.getInstance(ctx);
        }
        return http;
    }

    private String getResourceUrl() {
        return baseUrl + resourceUrl;
    }

    public void sendGetTo(String relative, Response.Listener<JSONObject> s, Response.ErrorListener f) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getResourceUrl() + relative, null, s, f);
        getHttpInstance().addToRequestQueue(request);
    }

    public void sendGetTo(Response.Listener<JSONObject> s, Response.ErrorListener f) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getResourceUrl(), null, s, f);
        getHttpInstance().addToRequestQueue(request);

    }

    public void sendPostTo(String relative, String json, Response.Listener<JSONObject> s, Response.ErrorListener f) {
        try {
            JSONObject obj = new JSONObject(json);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, getResourceUrl() + relative, obj, s, f);
            getHttpInstance().addToRequestQueue(request);
        } catch (JSONException ex) {
            return;
        }
    }

    public void sendPostTo(String json, Response.Listener<JSONObject> s, Response.ErrorListener f) {
        try {
            JSONObject obj = new JSONObject(json);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, getResourceUrl(), obj, s, f);
            getHttpInstance().addToRequestQueue(request);
        } catch (JSONException ex) {
            return;
        }
    }
}
