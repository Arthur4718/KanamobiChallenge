package devarthur.post.gitrepos.adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import devarthur.post.gitrepos.R;
import devarthur.post.gitrepos.model.PullDataModel;

public class PullViewAdapter extends RecyclerView.Adapter<PullViewAdapter.PullViewHolder> {

    //Member variables
    private Context mContext;
    private List<PullDataModel> mData;
    private RequestOptions requestOptions;


    public PullViewAdapter(Context mContext, List<PullDataModel> mData) {
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
    public PullViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.pull_item_cardview, viewGroup, false);

        return new PullViewHolder(view);
    }

    @Override
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

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class PullViewHolder extends RecyclerView.ViewHolder{

        TextView pullRequest_tv;
        TextView pullDesc_tv;
        ImageView pullUserThumb;
        TextView pullUserName_tv;
        TextView pullFullname;
        CardView pullItemCardview;

        public PullViewHolder(View itemView) {
            super(itemView);

            pullRequest_tv = (TextView) itemView.findViewById(R.id.pullRequestText);
            pullDesc_tv = (TextView) itemView.findViewById(R.id.pullRequestDesc);
            pullUserThumb = (ImageView) itemView.findViewById(R.id.pullUserThumb);
            pullUserName_tv = (TextView) itemView.findViewById(R.id.pullUsername);
            pullFullname = (TextView) itemView.findViewById(R.id.pullFullName);
            pullItemCardview = (CardView) itemView.findViewById(R.id.cardview_pull);


        }
    }
}
