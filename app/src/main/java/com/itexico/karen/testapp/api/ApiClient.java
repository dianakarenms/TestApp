package com.itexico.karen.testapp.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.itexico.karen.testapp.R;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.itexico.karen.testapp.utils.Repository.hideProgressDialog;
import static com.itexico.karen.testapp.utils.Repository.showProgressDialog;

/**
 * Created by dianakarenms on 5/28/17.
 */

public class ApiClient  {
    private static ApiClient mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;
    /*public enum Header {
        HEADER_DEFAULT,
        HEADER_FOR_ACCESS_TOKEN,
        HEADER_EMPTY
    }*/

    private ApiClient() {
        mRequestQueue = getRequestQueue();
    }

    public static synchronized ApiClient getInstance(Context context) {
        mCtx = context;
        if (mInstance == null) {
            mInstance = new ApiClient();
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(mCtx.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);

            // Don't forget to start the volley request queue
            mRequestQueue.start();
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag("TAG");
        getRequestQueue().add(req);
    }

    public void cancelPendingRequest(String tag){
        if(mRequestQueue != null) mRequestQueue.cancelAll(tag);
    }

    private void cancelAllRequest(){
        mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }

    //---------- REQUEST -------------------------------------------------------------------------->
    public static class GsonRequest<T> extends Request<T> {
        private final Gson gson = new Gson();
        private final Class<T> clazz;
        private final Map<String, String> headers;
        private final Map<String, String> params;
        private final Response.Listener<T> listener;
        private String activityName;
        private final boolean showProgress;

        /**
         * Make a GET request and return a parsed object from JSON.
         *
         * @param url     URL of the request to make
         * @param clazz   Relevant class object, for Gson's reflection
         * @param method  Method.GET or Method.POST
         * @param headers Map of request headers
         * @param params  Map of request parameters
         */
        public GsonRequest(String url, Class<T> clazz, int method, Map<String, String> headers, Map<String, String> params,
                           Response.Listener<T> listener, Response.ErrorListener errorListener, Boolean showDialog) {
            super(method, url, errorListener);
            this.clazz = clazz;
            this.headers = headers;
            this.params = params;
            this.listener = listener;
            this.activityName = mCtx.getClass().getSimpleName();
            this.showProgress = showDialog;

            setRetryPolicy(new DefaultRetryPolicy(5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            /*switch (headers) {
                case HEADER_DEFAULT:
                    this.headers = new LinkedHashMap<>();
                    this.headers.put("Content-Type", "application/json");
                    this.headers.put("trakt-api-key", ApiConfig.apiKeyTrakt); //mCtx.getResources().getString(R.string.trakt_api)
                    this.headers.put("trakt-api-version", "2");
                    break;
                default:
                    this.headers = new LinkedHashMap<>();
            }*/

            if(showDialog) {
                showProgressDialog(mCtx);
            }
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            return headers != null ? headers : super.getHeaders();
        }

        @Override
        public Map<String, String> getParams() throws AuthFailureError {
            return params != null ? params : super.getParams();
        }

        @Override
        protected void deliverResponse(T response) {
            listener.onResponse(response);
        }

        @Override
        protected Response<T> parseNetworkResponse(NetworkResponse response) {
            if(showProgress) {
                hideProgressDialog();
            }
            try {
                String json = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers));

                T result = gson.fromJson(json, clazz);
                Cache.Entry entry = HttpHeaderParser.parseCacheHeaders(response);
                Log.d(activityName, "Response for: " + getUrl() + "[ " + json + " ] ");

                return Response.success(result, entry);

            } catch (UnsupportedEncodingException e) {
                return Response.error(new VolleyError("Error parsing response"));
            } catch (JsonSyntaxException e) {
                return Response.error(new VolleyError("Error parsing response"));
            }
        }

        @Override
        protected VolleyError parseNetworkError(VolleyError error) {
            if(showProgress)
                hideProgressDialog();

            Log.e(activityName, "Error for " + getUrl() + ": " + error.toString());

            String message;

            // TmdbShow a default error message depending on error type
            if (error instanceof NetworkError) {
                message = mCtx.getString(R.string.error_network);
            } else if (error instanceof ServerError) {
                message = mCtx.getString(R.string.error_server);
            } else if (error instanceof AuthFailureError) {
                message = mCtx.getString(R.string.error_auth_failure);
            } else if (error instanceof ParseError) {
                message = mCtx.getString(R.string.error_parse);
            } else if (error instanceof NoConnectionError) {
                message = mCtx.getString(R.string.error_no_connection);
            } else if (error instanceof TimeoutError) {
                message = mCtx.getString(R.string.error_time_out);
            } else message = "";

            // Check if server returned data
            if (error.networkResponse != null && error.networkResponse.data != null) {
                // TODO: Handle this case
                Log.e("ApiClient:", "Network Response Data is null");
               /* ApiResponse errorResponse = null;
                String json = new String(error.networkResponse.data);
                if (error instanceof AuthFailureError){
                    if(!(mCtx instanceof LoginActivity)) {
                        sSession.logoutUser();
                        message = "Por favor inicie sesión.";
                    }
                } else {
                    errorResponse = gson.fromJson(json, ApiResponse.class);
                }
                try {
                    // Get error message to show to user if its available
                    if(errorResponse != null) {
                        if(errorResponse.getError() != null) {
                            message = errorResponse.getError().getUser();
                        }
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    message = mCtx.getString(R.string.error_server);
                }*/
            }

            error = new VolleyError(message);

            return super.parseNetworkError(error);
        }
    }
}
