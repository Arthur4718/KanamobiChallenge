package devarthur.post.gitrepos;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import devarthur.post.gitrepos.adapter.RecyclerViewAdapter;
import devarthur.post.gitrepos.model.GitrepoDataModel;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Member Variables.
    private List<GitrepoDataModel> GitRepoList;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter myRecyclerViewAdapter;

    //Constants
    private static final String GITAPI_URL = "";
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

        //Holds all data models in a Array List
        GitRepoList = new ArrayList<>();
        populateRecyclerView();


    }

    private void getDataFromNetWork(){

        //TODO Use the design pattern from this https://www.youtube.com/watch?v=nqty1cT69yk


    }

    private void populateRecyclerView(){
        //TODO remove after setting up the GET method from GIT hub API.

        for(int i = 0; i < 10; i++){

            GitrepoDataModel gitItem = new GitrepoDataModel();
            gitItem.setRepoName("Repo: " + String.valueOf(i));
            gitItem.setRepoDesc("Description: " + String.valueOf(i));
            gitItem.setForkCount("forkCount: " + String.valueOf(i * 10));
            gitItem.setStarCount("starCount: " + String.valueOf(i * 10));
            gitItem.setUsername("Username: " + String.valueOf(i));
            gitItem.setFullname("fullname" + String.valueOf(i));
            gitItem.setHtml_url("https://github.com/Arthur4718");
            gitItem.setPull_url("https://github.com/Arthur4718");
            gitItem.setAvatar_url("https://s.gravatar.com/avatar/398bcf1fa067a7056950d4124b4c9124?s=80");
            gitItem.setLanguague("Java");
            GitRepoList.add(gitItem);

        }
        //Used to test the recycler while there is not connection to the api.

        feedRecyclerView(GitRepoList);

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
