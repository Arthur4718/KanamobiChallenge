package devarthur.post.gitrepos.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.entity.ContentType;
import devarthur.post.gitrepos.R;
import devarthur.post.gitrepos.adapter.RecyclerViewAdapter;
import devarthur.post.gitrepos.model.GitrepoDataModel;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Member Variables.
    private List<GitrepoDataModel> GitRepoList;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter myRecyclerViewAdapter;
    private int datalenght;

    //Constants
    private static final String GITAPI_URL = "https://api.github.com/search/repositories?q=language:Java&sort=stars&page=1";

    //TODO check for the correct base URL in the doc, see the resources on trello for more info


    //TODO create a login to populate recycler view with local data.


    //TODO create a logic to populate the recycler view with data from GIT API


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
    }


    private void getDataFromGit() {


        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();


        client.addHeader("User-Agent", "android4718");

        client.get(getApplicationContext(), GITAPI_URL,params,  new JsonHttpResponseHandler() {
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
                        e.printStackTrace();
                    }
                }

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

        if (id == R.id.nav_camera) {
            //TODO Create new item navigations
            getDataFromGit();

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {


        }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
