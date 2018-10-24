package devarthur.post.gitrepos.activity;

import android.os.Bundle;

import android.os.Parcelable;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;


import devarthur.post.gitrepos.R;
import devarthur.post.gitrepos.service.GitDataClient;
import devarthur.post.gitrepos.service.OnLoopjCompleted;
import devarthur.post.gitrepos.adapter.RecyclerViewAdapter;
import devarthur.post.gitrepos.model.GitrepoDataModel;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnLoopjCompleted{


    //Member Variables.
    private List<GitrepoDataModel> GitRepoList;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter myRecyclerViewAdapter;
    private SwipeRefreshLayout swipeContainer;
    private ProgressBar progressBar;
    private GitDataClient gitClient;
    private int datalenght;
    private int page;
    private boolean isListBottom;
    private boolean updateList;
    private OnLoopjCompleted listener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Seting up data client with the context of this Activity and its listener
        gitClient = new GitDataClient(this , this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mRecyclerView = findViewById(R.id.myRecyclerView);
        GitRepoList = new ArrayList<>();
        page = 1;
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                     Toast.makeText(getApplicationContext(), "Bottom...", Toast.LENGTH_LONG).show();
                     progressBar = (ProgressBar) findViewById(R.id.progressBar);
                     progressBar.setVisibility(View.VISIBLE);

                     getDataFromService(page);

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
                getDataFromService(page);


            }
        });

        //Load the very first page when the app loads
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        getDataFromService(page);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO save the state of the list when the user rotates the app
    }

    private void feedRecyclerView(List<GitrepoDataModel>  dataList) {


            RecyclerViewAdapter myRecyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), dataList);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            mRecyclerView.setAdapter(myRecyclerViewAdapter);

            if (updateList){
                myRecyclerViewAdapter.notifyDataSetChanged();
            }


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



        if (id == R.id.nav_appInfo) {




        } else if (id == R.id.nav_report) {


        } else if (id == R.id.nav_infoAuthor) {


        } else if (id == R.id.nav_settings) {
            //

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void taskCompleted(String results) {

        updateRecyclerView(results);
    }

    private void updateRecyclerView(String results) {

        JSONObject response = null;
        try {
            response = new JSONObject(results);
            datalenght =  response.getJSONArray("items").length();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < datalenght; i++){
            GitrepoDataModel gitItem = new GitrepoDataModel();

            try {
                gitItem.setRepoName(response.getJSONArray("items").getJSONObject(i).getString("name"));
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
                Toast.makeText(getApplicationContext(),"Please check your connection", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        swipeContainer.setRefreshing(false);
        page = page + 1;


    }
    private void getDataFromService(int page){

        gitClient.getRepoData(page);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //todo use this method to store the recycler view state
        super.onSaveInstanceState(outState);
    }
}
