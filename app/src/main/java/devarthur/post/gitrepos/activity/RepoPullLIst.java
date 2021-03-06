package devarthur.post.gitrepos.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import devarthur.post.gitrepos.R;
import devarthur.post.gitrepos.adapter.PullViewAdapter;
import devarthur.post.gitrepos.model.PullDataModel;

public class RepoPullLIst extends AppCompatActivity {

    //Member Variables
    private List<PullDataModel> PullDataList;
    private RecyclerView mRecyclerView;
    private PullViewAdapter myRecyclerViewAdapter;
    private ProgressBar progressBar;
    private String pullListURL;
    private SwipeRefreshLayout swipeList;
    private TextView pullsOpen;
    private TextView pullsClosed;
    private int pullsOpenCount = 0;
    private int pullsClosedCount = 0;

    //Constants
    private static String BASE_URL = "https://api.github.com/repos/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_pull_list);
        //Get our list to display our content
        mRecyclerView = findViewById(R.id.pullListRecycler);

        //Set repo name in the title
        String repo = getIntent().getExtras().getString("RepoName");
        getSupportActionBar().setTitle(repo);
        //Set the return Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //Set the count for open and closed pulls


        //Set user name
        String username = getIntent().getExtras().getString("Owner");

        //Create the url to list the pull requests
        pullListURL = BASE_URL + username + "/" + repo + "/pulls";
        //Create a list to hold all objects
        PullDataList = new ArrayList<>();

        swipeList = (SwipeRefreshLayout) findViewById(R.id.swipePullList);
        swipeList.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        //Load the first page
        getDataFromNetwork(pullListURL);

        //Reload on user action
        swipeList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getApplicationContext(), "Resolving pulls list",Toast.LENGTH_SHORT ).show();
                getDataFromNetwork(pullListURL);
            }
        });
    }


    private void getDataFromNetwork(String URL){


        AsyncHttpClient cli = new AsyncHttpClient();

        cli.addHeader("User-Agent", "android4718");

        cli.get(getApplicationContext(), URL,null,  new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);


                for (int i = 0; i < response.length(); i++){
                    PullDataModel pullItem = new PullDataModel();
                    try {

                        //Fetch items
                        pullItem.setPullTitle(response.getJSONObject(i).getString("title"));
                        pullItem.setPullDescription(response.getJSONObject(i).getString("body"));
                        pullItem.setPullAvatarUrl(response.getJSONObject(i).getJSONObject("user").getString("avatar_url"));
                        pullItem.setPullUsername(response.getJSONObject(i).getJSONObject("user").getString("login"));
                        pullItem.setPullFullname(pullItem.getPullUsername() + " " + pullItem.getPullTitle());
                        pullItem.setPullURL(response.getJSONObject(i).getString("html_url"));
                        String status = response.getJSONObject(i).getString("state");
                        if(status.equals("open")){
                            pullsOpenCount++;
                        }else{
                            pullsClosedCount++;
                        }
                        //Update the list
                        PullDataList.add(pullItem);
                        feedRecyclerView(PullDataList);
                        TextView pullsOpen = findViewById(R.id.pullsOpened);
                        TextView pullsClosed =  findViewById(R.id.pullsClosed);
                        pullsOpen.setText(String.valueOf(pullsOpenCount) + " " + "Opened");
                        pullsClosed.setText(String.valueOf(pullsClosedCount) + " " + "Closed");

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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



}
