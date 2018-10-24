package devarthur.post.gitrepos.service;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;


import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;


public class GitDataClient {

    private static final String GITAPI_URL = "https://api.github.com/search/repositories?q=language:Java&sort=stars&page=";
    private AsyncHttpClient client;
    private RequestParams params;
    private Context context;
    private OnLoopjCompleted loopjListener;

    public GitDataClient(Context context, OnLoopjCompleted listener) {

        client = new AsyncHttpClient();
        params = new RequestParams();
        this.context = context;
        this.loopjListener = listener;
    }

    public GitDataClient() {

    }

    public void getRepoData(int page){
        client.addHeader("User-Agent", "android4718");
        String REQUEST_URL = GITAPI_URL + String.valueOf(page);


        client.get(REQUEST_URL, null, new JsonHttpResponseHandler() {
            @Override
            public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
                super.onPostProcessResponse(instance, response);

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                loopjListener.taskCompleted(response.toString());
                Log.i("APP", "on Success: " + statusCode);
               
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.i("APP", "On Failure: " + String.valueOf(statusCode) + " " + errorResponse.toString());
                
            }
        });


    }


}
