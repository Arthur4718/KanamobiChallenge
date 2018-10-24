package devarthur.post.gitrepos.Service;

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
    AsyncHttpClient client = new AsyncHttpClient();
    RequestParams params = new RequestParams();
    private Context context;
    private int datalenght;

    public GitDataClient(AsyncHttpClient client, RequestParams params, Context context, int datalenght) {
        this.client = client;
        this.params = params;
        this.context = context;
        this.datalenght = datalenght;
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
                //listener.taskCompleted(response.toString());
                Log.i("APP", "onSuccess: " + response.toString());
               
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.i("APP", "On Failure: " + String.valueOf(statusCode) + " " + errorResponse.toString());
                
            }
        });


    }


}
