package devarthur.post.gitrepos.activity;

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;



import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import devarthur.post.gitrepos.R;
import devarthur.post.gitrepos.adapter.RecyclerViewAdapter;
import devarthur.post.gitrepos.model.GitrepoDataModel;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Member Variables.
    private List<GitrepoDataModel> GitRepoList;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter myRecyclerViewAdapter;
    private SwipeRefreshLayout swipeContainer;
    private ProgressBar progressBar;
    private int datalenght;
    private int page;
    private boolean isListBottom;

    //Constants
    //TODO create a class to handle the GET request and remove the code from this activity.
    private static final String GITAPI_URL = "https://api.github.com/search/repositories?q=language:Java&sort=stars&page=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mRecyclerView = findViewById(R.id.myRecyclerView);
        GitRepoList = new ArrayList<>();
        page = 1; // TODO Override the method on resume on this activity and store the state of the list
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                     Toast.makeText(getApplicationContext(), "List Updated...", Toast.LENGTH_LONG).show();
                    progressBar = (ProgressBar) findViewById(R.id.progressBar);
                    progressBar.setVisibility(View.VISIBLE);
                    isListBottom = true;
                    getDataFromGit();

                }
            }
        });

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                Toast.makeText(getApplicationContext(), "Resolving new Repos from page: " + String.valueOf(page),Toast.LENGTH_SHORT ).show();
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
                swipeContainer.setRefreshing(false);
                getDataFromGit();


            }
        });


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        getDataFromGit();

    }


    private void getDataFromGit() {


        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();


        client.addHeader("User-Agent", "android4718");
        String URL = GITAPI_URL + String.valueOf(page);

        client.get(getApplicationContext(), URL,params,  new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    datalenght = response.getJSONArray("items").length();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for(int i = 0; i < datalenght; i++){
                    GitrepoDataModel gitItem = new GitrepoDataModel();

                    try {
                        gitItem.setRepoName(response.getJSONArray("items").getJSONObject(i).getString("name") + " "+ "n: " +  String.valueOf(i));
                        gitItem.setRepoDesc(response.getJSONArray("items").getJSONObject(i).getString("description"));
                        gitItem.setForkCount(response.getJSONArray("items").getJSONObject(i).getString("forks"));
                        gitItem.setStarCount(response.getJSONArray("items").getJSONObject(i).getString("stargazers_count"));
                        gitItem.setUsername(response.getJSONArray("items").getJSONObject(i).getJSONObject("owner").getString("login"));
                        gitItem.setFullname(response.getJSONArray("items").getJSONObject(i).getString("full_name"));
                        gitItem.setHtml_url(response.getJSONArray("items").getJSONObject(i).getString("html_url"));
                        gitItem.setPull_url(response.getJSONArray("items").getJSONObject(i).getString("pulls_url"));
                        gitItem.setAvatar_url(response.getJSONArray("items").getJSONObject(i).getJSONObject("owner").getString("avatar_url"));
                        gitItem.setLanguague("Java");

                        GitRepoList.add(gitItem);
                        feedRecyclerView(GitRepoList);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (isListBottom){
                    mRecyclerView.smoothScrollToPosition(GitRepoList.size());
                    isListBottom = false;
                }

                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.INVISIBLE);
                swipeContainer.setRefreshing(false);
                page = page + 1;

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "On Failure:  " + String.valueOf(statusCode) + " " + throwable.toString(), Toast.LENGTH_SHORT).show();
                Log.e("GET", "On error " + throwable.toString());
            }
        });
    }



    private void feedRecyclerView(List<GitrepoDataModel>  dataList) {
        RecyclerViewAdapter myRecyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), dataList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setAdapter(myRecyclerViewAdapter);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //TODO use the drawer to show the user how many repos you are listing now

        if (id == R.id.nav_camera) {
            //TODO Create new item navigations - GO TO THE TOP OF THE LIST


        } else if (id == R.id.nav_gallery) {
            //TODO GO TO THE BOTTOM OF THE LIST

        } else if (id == R.id.nav_slideshow) {
            //TODO INFORMATION ABOUT THE APP

        } else if (id == R.id.nav_manage) {
            //

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {


        }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
