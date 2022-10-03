package com.example.myapplication;
import java.util.List;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import org.json.JSONObject;
import android.content.Context;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class HandleBackendCalls {

    private static final String LP_TAG = "LP_TAG";
    private static final String SO_TAG = "SO_TAG";
    private static final String DETAILS_TAG = "DETAILS_TAG";
    private static final String TAG = HandleBackendCalls.class.getName();
    private static RequestQueue rQueue;
    private static boolean isInitialized;

    synchronized public static void initialize(Context context) {
        if (!isInitialized) {
            isInitialized = true;
            rQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
    }

    private static JsonObjectRequest buildRequest(String url, Response.Listener<JSONObject> listener,
                                                  Response.ErrorListener errorListener, String tag) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, listener, errorListener);
        request.setTag(tag);
        return request;
    }

    synchronized private static void addRequestToQueue(JsonObjectRequest request) {
        rQueue.add(request);
    }

    public static void fetchSearchOptions(String query, Response.Listener<JSONObject> listener,
                                                     Response.ErrorListener errorListener) {
        String url = "https://stocksdata-backend-88888.uw.r.appspot.com/api/search/"+ query;
        JsonObjectRequest request = buildRequest(url, listener, errorListener, SO_TAG);
        addRequestToQueue(request);
    }

    public static void makeLPFetchRequest(List<String> tickers, Response.Listener<JSONObject> listener,
                                                  Response.ErrorListener errorListener) {

            String sticker = String.join(",",tickers);
            String url = "https://stocksdata-backend-88888.uw.r.appspot.com/api/prices/"+sticker;
            JsonObjectRequest request = buildRequest(url, listener, errorListener, LP_TAG);
            addRequestToQueue(request);

    }
    public static void fetchInfo(String ticker, Response.Listener<JSONObject> listener,
                                              Response.ErrorListener errorListener) {

        String url = "https://stocksdata-backend-88888.uw.r.appspot.com/api/summary/"+ticker;
        JsonObjectRequest request = buildRequest(url, listener, errorListener, DETAILS_TAG);
        addRequestToQueue(request);
    }

    public static void fetchCurrentPrices(String ticker, Response.Listener<JSONObject> listener,
                                              Response.ErrorListener errorListener) {
        String url = "https://stocksdata-backend-88888.uw.r.appspot.com/api/details/"+ticker;
        JsonObjectRequest request = buildRequest(url, listener, errorListener, LP_TAG);
        addRequestToQueue(request);
    }

    public static void fetchNews(String ticker, Response.Listener<JSONObject> listener,
                                                 Response.ErrorListener errorListener) {
        String url = "https://stocksdata-backend-88888.uw.r.appspot.com/api/companynews/"+ticker;
        JsonObjectRequest request = buildRequest(url, listener, errorListener, LP_TAG);
        addRequestToQueue(request);
    }

    public static void fetchMentions(String ticker, Response.Listener<JSONObject> listener,
                                            Response.ErrorListener errorListener) {
        String url = "https://stocksdata-backend-88888.uw.r.appspot.com/api/mentions/"+ticker;
        JsonObjectRequest request = buildRequest(url, listener, errorListener, LP_TAG);
        addRequestToQueue(request);
    }

    public static void makePeersFetchRequest(String ticker, Response.Listener<JSONObject> listener,
                                                       Response.ErrorListener errorListener) {
        String url = "https://stocksdata-backend-88888.uw.r.appspot.com/api/peers/"+ticker;
        JsonObjectRequest request = buildRequest(url, listener, errorListener, LP_TAG);
        addRequestToQueue(request);
    }

    synchronized public static void cancelDetailFetchRequest() {
        rQueue.cancelAll(DETAILS_TAG);
    }
    synchronized public static void cancelSearchOptionsReq() {
        rQueue.cancelAll(SO_TAG);
    }
    synchronized public static void cancelLPRequest() {
        rQueue.cancelAll(LP_TAG);
    }

}
