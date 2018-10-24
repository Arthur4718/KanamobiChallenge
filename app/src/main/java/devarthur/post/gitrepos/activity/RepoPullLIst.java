package devarthur.post.gitrepos.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.HttpClient;
import devarthur.post.gitrepos.R;
import devarthur.post.gitrepos.adapter.PullViewAdapter;
import devarthur.post.gitrepos.model.PullDataModel;

public class RepoPullLIst extends AppCompatActivity {

    //Member Variables
    private List<PullDataModel> PullDataList;
    private RecyclerView mRecyclerView;
    private PullViewAdapter myRecyclerViewAdapter;
    private ProgressBar progressBar;
    private int datalenght;
    private String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent at ultrices turpis. Vivamus molestie faucibus velit, a lobortis enim accumsan a. Curabitur facilisis cursus rutrum";
    private String pullListURL;
    private SwipeRefreshLayout swipeList;

    //Constants
    private static String BASE_URL = "https://api.github.com/repos/";
    //https://api.github.com/repos/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_pull_list);

        mRecyclerView = findViewById(R.id.pullListRecycler);

        //Set repo name in the title
        String repo = getIntent().getExtras().getString("RepoName");
        getSupportActionBar().setTitle(repo);

        //Set user name =
        String username = getIntent().getExtras().getString("Owner");

        //Create the url to list the pull requests
        pullListURL = BASE_URL + username + "/" + repo + "/pulls";
        //Log.d("APP", "URL " + BASE_URL + username + "/" + repo);
        PullDataList = new ArrayList<>();

        //progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        //progressBar.setVisibility(View.VISIBLE);

        swipeList = (SwipeRefreshLayout) findViewById(R.id.swipePullList);
        swipeList.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        swipeList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getApplicationContext(), "Resolving pulls list",Toast.LENGTH_SHORT ).show();
                getDataFromNetwork(pullListURL);
            }
        });
    }



    private void getDataFromNetwork(String URL){
        //TODO create a JSON request to consume the GIT API and populate te recycler view

        AsyncHttpClient cli = new AsyncHttpClient();
        RequestParams params = new RequestParams();


        cli.addHeader("User-Agent", "android4718");

        cli.get(getApplicationContext(), URL,null,  new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);


                int size = response.length();
                for (int i = 0; i < size; i++){
                    PullDataModel pullItem = new PullDataModel();
                    try {

                        pullItem.setPullTitle(response.getJSONObject(i).getString("title"));
                        pullItem.setPullDescription(response.getJSONObject(i).getString("body"));
                        pullItem.setPullAvatarUrl(response.getJSONObject(i).getJSONObject("user").getString("avatar_url"));
                        pullItem.setPullUsername(response.getJSONObject(i).getJSONObject("user").getString("login"));
                        pullItem.setPullFullname(pullItem.getPullUsername() + " " + pullItem.getPullTitle());
                        PullDataList.add(pullItem);
                        feedRecyclerView(PullDataList);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);
                Toast.makeText(getApplicationContext(), "Response came as string.. ",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getApplicationContext(), "Request Failed ",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getApplicationContext(), "Request Failed ",Toast.LENGTH_SHORT).show();
            }

        });

        swipeList.setRefreshing(false);
    }
    private void feedRecyclerView(List<PullDataModel>  dataList) {
        PullViewAdapter myRecyclerViewAdapter = new PullViewAdapter(getApplicationContext(), dataList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setAdapter(myRecyclerViewAdapter);
    }


}
