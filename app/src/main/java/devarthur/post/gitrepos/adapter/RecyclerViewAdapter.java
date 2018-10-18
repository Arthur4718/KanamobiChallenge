package devarthur.post.gitrepos.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import devarthur.post.gitrepos.activity.RepoPullLIst;
import devarthur.post.gitrepos.model.GitrepoDataModel;
import devarthur.post.gitrepos.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    //Member variables
    private Context mContext;
    private List<GitrepoDataModel> mData;
    private RequestOptions requestOptions;


    public RecyclerViewAdapter(Context mContext, List<GitrepoDataModel> mData) {
        this.mContext = mContext;
        this.mData = mData;

        //Glide setup
        requestOptions = new RequestOptions()
                .centerInside()
                .placeholder(R.drawable.ic_more_horiz_black_24dp)
                .error(R.drawable.ic_error_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.gitrepo_item_cardview, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int i) {


        holder.repoName_tv.setText(mData.get(i).getRepoName());
        holder.repoDesc_tv.setText(mData.get(i).getRepoDesc());
        holder.forkCount_tv.setText(mData.get(i).getForkCount());
        holder.starCount_tv.setText(mData.get(i).getStarCount());
        holder.username.setText(mData.get(i).getUsername());
        holder.fullname.setText(mData.get(i).getFullname());

        Glide
                .with(mContext)
                .load(mData.get(i).getAvatar_url())
                .apply(requestOptions)
                .into(holder.avatarThumb);


        //Sets an onclick listener to every cardview
        // Also adds the listener to the description

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OpenPullList();

            }
        });

        holder.repoDesc_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenPullList();
            }
        });
    }

    private void OpenPullList(){
        //TODO USE INTENT PUT EXTRA TO PASS INFORMATION TO THE NEXT SCREEN.

        //Set data to be passed through intent
        Intent intent = new Intent(mContext, RepoPullLIst.class);
        
        mContext.startActivity(intent);


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView repoName_tv;
        TextView repoDesc_tv;
        TextView forkCount_tv;
        TextView starCount_tv;
        TextView username;
        TextView fullname;
        ImageView avatarThumb;
        CardView cardview;

        //TODO CREATE A LAYOUT FOR LANDSCAPE MODE WICH ENABLES MORE ESPACE FOR DESCRIPTIONS


        public MyViewHolder(View itemView) {
            super(itemView);

            repoName_tv = (TextView) itemView.findViewById(R.id.repoName);
            repoDesc_tv = (TextView) itemView.findViewById(R.id.repoDesc);
            forkCount_tv = (TextView) itemView.findViewById(R.id.repoForkCount);
            starCount_tv = (TextView) itemView.findViewById(R.id.repoStarCount);
            username = (TextView) itemView.findViewById(R.id.username);
            fullname = (TextView) itemView.findViewById(R.id.userFullname);
            avatarThumb = (ImageView) itemView.findViewById(R.id.profile_image);
            cardview = (CardView) itemView.findViewById(R.id.cardview_repo);
        }
    }
}
