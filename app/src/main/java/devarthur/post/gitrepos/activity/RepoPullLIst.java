package devarthur.post.gitrepos.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import devarthur.post.gitrepos.R;
import devarthur.post.gitrepos.adapter.PullViewAdapter;
import devarthur.post.gitrepos.model.PullDataModel;

public class RepoPullLIst extends AppCompatActivity {

    //Member Variables

    private List<PullDataModel> PullDataList;
    private RecyclerView mRecyclerView;
    private PullViewAdapter myRecyclerViewAdapter;
    private String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent at ultrices turpis. Vivamus molestie faucibus velit, a lobortis enim accumsan a. Curabitur facilisis cursus rutrum";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_pull_list);

        mRecyclerView = findViewById(R.id.pullListRecycler);

        //Holds all data models in a Array List
         PullDataList = new ArrayList<>();
         populateRecyclerView();
    }

    private void getDataFromNetwork(){
        //TODO create a JSON request to consume the GIT API and populate te recycler view
    }

    private void populateRecyclerView() {

        for(int i=0; i < 10;i++){
            PullDataModel pullItem = new PullDataModel();
            pullItem.setPullTitle("Pull title request " + String.valueOf(i));
            pullItem.setPullDescription(text + String.valueOf(i));
            pullItem.setPullAvatarUrl("https://s.gravatar.com/avatar/398bcf1fa067a7056950d4124b4c9124?s=80");
            pullItem.setPullUsername("username: " + String.valueOf(i));
            pullItem.setPullFullname("full name user: " + String.valueOf(i));
            pullItem.setPullURL("https://github.com/Arthur4718");
            PullDataList.add(pullItem);
        }

        feedRecyclerView(PullDataList);
    }

    private void feedRecyclerView(List<PullDataModel>  dataList) {
        PullViewAdapter myRecyclerViewAdapter = new PullViewAdapter(getApplicationContext(), dataList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setAdapter(myRecyclerViewAdapter);
    }


}
