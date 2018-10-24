package devarthur.post.gitrepos.Service;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import devarthur.post.gitrepos.model.GitrepoDataModel;

public class GitDataClient {

    private static final String GITAPI_URL = "https://api.github.com/search/repositories?q=language:Java&sort=stars&page=";
    AsyncHttpClient client = new AsyncHttpClient();
    RequestParams params = new RequestParams();
    private Context context;
    private OnLoopjCompleted listener;
    private int datalenght;


    public GitDataClient(AsyncHttpClient client, RequestParams params, Context context, OnLoopjCompleted listener, int datalenght) {
        this.client = client;
        this.params = params;
        this.context = context;
        this.listener = listener;
        this.datalenght = datalenght;
    }

    public GitDataClient() {

    }

    public void getRepoData(int page){
        client.addHeader("User-Agent", "android4718");
        String REQUEST_URL = GITAPI_URL + String.valueOf(page);


        client.get(REQUEST_URL, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Log.i("APP", "onSuccess: " + response.toString());

                //listener.taskCompleted("hi")
                //ToDo  reset your steps and check the interface
                //https://code.tutsplus.com/tutorials/an-introduction-to-loopj--cms-26781
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.i("APP", "On Failure: " + String.valueOf(statusCode) + " " + errorResponse.toString());
            }
        });


    }


}
