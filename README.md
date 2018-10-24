# KanamobiChallenge
App designed to consume Git Hub api and list a recycler view with the top rated Java repositories.

# Libraries used in the project

* Glide. 
* Loopj
* CircleCardView


# Design pattern. 

* MVC
* Material Design. 

# Download APK.

You should be able to grab an apk [by clicking here](https://drive.google.com/file/d/152GVCY2gRUlCasc2oKYEMcfJ4mUPILV_/view?usp=sharing).

# About the project

Created in Android Studio 3.2.1

Compatible with Android 7.0 ( api level 24). 

It shows a list of the top rated Java repositories on Github. Each repo when clicked will display the pulls request list from that repo.


# Listing repositories. 

In the main actity we set up or view elements, set up the recycler view adapter and swipe refresh. Every time the list is refreshing the user will see a progress bar, however when the list updates the user gets back to the first entry. 
'''Java
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

'''

The getDataFromService is call to the method created inside the service package, on the GitDataClient. This method was applied in order to remove the client get method from the Activity. This recomendation comes from loopj library documentation and from the MVC design pattern. 

'''Java
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



'''

Unfortunately i was not able to implemen a Presenter class wich will remove some code from the main activity. Using the presenter as a middle man the class would be litghter and easier to read. In the main activity class the method responsible for updates in the view is the updateRecyclerView method, it receiveis the data from the GitDataClient interface as a callback. 

'''Java
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

'''

Every item added to the recycler view has its own onclick listener. I added both cardview and textview listeners to prevent the user from clicking in the description and receiving no feedback. The repo name and the owner are passed by intent extra do the next screen.
In order to better visualize the json i created the structore of the Json Array response on this link. When the list is done updating i added page++, so when the user tries to update the list he will receive the data from more pages. 

Json Mate its a handy too

http://www.jsonmate.com/permalink/5bc7b62a85da04b10bcf7bb5



# Listing pulls list.

Every pull list item has its own onclick listener. The OpenPullBrowser its the method responsible for opening the pull url in the device browser. 


'''Java
 
    public void onBindViewHolder(PullViewHolder holder, final int i) {


        holder.pullRequest_tv.setText(mData.get(i).getPullTitle());
        holder.pullDesc_tv.setText(mData.get(i).getPullDescription());

        Glide
                .with(mContext)
                .load(mData.get(i).getPullAvatarUrl())
                .apply(requestOptions)
                .into(holder.pullUserThumb);

        holder.pullUserName_tv.setText(mData.get(i).getPullUsername());
        holder.pullFullname.setText(mData.get(i).getPullFullname());

        //Sets an onclick listener to every cardview
        // Also adds the listener to the description

        holder.pullItemCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OpenPullInBrowser(i);

            }
        });

        holder.pullDesc_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenPullInBrowser(i);
            }
        });
    }

    private void OpenPullInBrowser(int position){

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mData.get(position).getPullURL()));
        mContext.startActivity(browserIntent);


    }
'''

In the RepoPullList activity will you find the client call to the endpoint, which is formed with data from the get intent, and will also find the Activity does not have a class to separe the logic from the UI. 

'''Java
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

'''
# Layout 

I used Constraint layout because its the easier way(in my point of view) to construct views that suport portrait and landscape mode. I used the standard layouts from Android SDK, Navigation Drawer and Basic Activity, since there is no need to a more detailed application of UX patterns in this project.  

'''XML

  <?xml version="1.0" encoding="utf-8"?>
<android.support.v7.widget.CardView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    app:cardElevation="6dp"
    app:cardCornerRadius="6dp"
    android:layout_marginTop="8dp"
    android:layout_marginLeft="8dp"
    android:layout_marginRight="8dp"
    android:layout_marginBottom="8dp"
    android:id="@+id/cardview_repo"

    >

    <android.support.constraint.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">


        <TextView
            android:id="@+id/repoName"
            android:layout_width="wrap_content"
            android:layout_height="0dp"
            android:layout_marginStart="8dp"
            android:layout_marginLeft="8dp"
            android:layout_marginTop="16dp"
            android:text="TextView"
            android:textColor="@android:color/black"
            android:textStyle="bold"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            tools:text="Nome do Repositorio" />

        <TextView
            android:id="@+id/repoDesc"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_marginStart="8dp"
            android:layout_marginLeft="8dp"
            android:layout_marginTop="8dp"
            android:layout_marginEnd="8dp"
            android:layout_marginRight="8dp"
            android:layout_marginBottom="8dp"
            android:inputType="textMultiLine"
            android:text="Description \n of the project \n here"
            app:layout_constraintBottom_toTopOf="@+id/repoForkCount"
            app:layout_constraintEnd_toStartOf="@+id/profile_image"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/repoName" />

        <TextView
            android:id="@+id/repoForkCount"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="8dp"
            android:layout_marginLeft="8dp"
            android:layout_marginBottom="16dp"
            android:drawableLeft="@drawable/ic_share_black_24dp"
            android:gravity="center"
            android:text="500"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintStart_toStartOf="parent" />

        <TextView
            android:id="@+id/repoStarCount"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="16dp"
            android:layout_marginLeft="16dp"
            android:layout_marginBottom="16dp"
            android:drawableLeft="@drawable/ic_star_black_24dp"
            android:gravity="center"
            android:text="20"
            android:textAlignment="center"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintStart_toEndOf="@+id/repoForkCount" />


        <de.hdodenhof.circleimageview.CircleImageView xmlns:app="http://schemas.android.com/apk/res-auto"
            android:id="@+id/profile_image"
            android:layout_width="64dp"
            android:layout_height="64dp"
            android:layout_marginTop="4dp"
            android:layout_marginEnd="8dp"
            android:layout_marginRight="8dp"
            android:layout_marginBottom="8dp"
            android:src="@drawable/ic_person_black_24dp"
            app:civ_border_color="#fff"
            app:civ_border_width="2dp"
            app:layout_constraintBottom_toTopOf="@+id/userFullname"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/username" />

        <TextView
            android:id="@+id/username"
            android:layout_width="0dp"
            android:layout_height="17dp"
            android:layout_marginStart="8dp"
            android:layout_marginLeft="8dp"
            android:layout_marginTop="16dp"
            android:layout_marginEnd="8dp"
            android:layout_marginRight="8dp"
            android:gravity="end"
            android:inputType="textMultiLine"
            android:text="Username"
            android:textColor="@android:color/black"
            android:textStyle="bold"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toEndOf="@+id/repoName"
            app:layout_constraintTop_toTopOf="parent" />

        <TextView
            android:id="@+id/userFullname"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_marginStart="8dp"
            android:layout_marginLeft="8dp"
            android:layout_marginTop="8dp"
            android:layout_marginEnd="8dp"
            android:layout_marginRight="8dp"
            android:layout_marginBottom="16dp"
            android:gravity="end"
            android:text="Nome Sobrenome"
            android:textAlignment="viewEnd"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toEndOf="@+id/repoStarCount"
            app:layout_constraintTop_toBottomOf="@+id/repoDesc" />

    </android.support.constraint.ConstraintLayout>




</android.support.v7.widget.CardView>


'''

# Next steps.

Some challenges could not be applied in the project:

* Store list data and position between layout rotations. 
* Automated tests. 
* Application of MVP(Model View Presenter) pattern which is better suited for automated tests that MVC. 


Please. Send feedback about this challenge to arthur.gomes_4718@hotmail.com

