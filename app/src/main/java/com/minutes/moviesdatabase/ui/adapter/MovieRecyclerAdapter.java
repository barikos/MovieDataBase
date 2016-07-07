package com.minutes.moviesdatabase.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.minutes.moviesdatabase.Const;
import com.minutes.moviesdatabase.R;
import com.minutes.moviesdatabase.api.Movie;
import com.minutes.moviesdatabase.ui.listener.OnLoadMoreListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by barikos on 15.06.16.
 */
public class MovieRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private Context mContext;
    private List<Movie> mMovieList;

    private int mVisibleThreshold = 3;
    private int mLastVisibleItem, mTotalItemCount;
    private boolean mLoading;
    private OnLoadMoreListener mOnLoadMoreListener;

    public MovieRecyclerAdapter(Context context, List<Movie> movieList, RecyclerView recyclerView) {
        mContext = context;
        mMovieList = movieList;

        final LinearLayoutManager layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                mTotalItemCount = layoutManager.getItemCount();
                mLastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (!mLoading && mTotalItemCount <= (mLastVisibleItem + mVisibleThreshold)){
                    if (mOnLoadMoreListener != null){
                        mOnLoadMoreListener.onLoadMore();
                    }
                    mLoading = true;
                }
            }
        });
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivMovie;
        public TextView tvTitle, tvRating, tvDate, tvVote;

        public MovieViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.movie_card_txt_main);
            tvRating = (TextView) itemView.findViewById(R.id.movie_card_txt_rating);
            tvDate = (TextView) itemView.findViewById(R.id.movie_card_txt_date);
            tvVote = (TextView) itemView.findViewById(R.id.movie_card_txt_vote);
            ivMovie = (ImageView) itemView.findViewById(R.id.movie_card_img);
        }
    }

    static class ProgressViewHolder extends RecyclerView.ViewHolder{
        public ProgressBar progressBar;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar)itemView.findViewById(R.id.progress_bar);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View itemView;
        if (viewType == VIEW_ITEM){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card,parent,false);
            viewHolder = new MovieViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item,parent,false);
            viewHolder = new ProgressViewHolder(itemView);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MovieViewHolder){
            Movie movie = mMovieList.get(position);
            ((MovieViewHolder)holder).tvTitle.setText(movie.getTitle());
            ((MovieViewHolder)holder).tvRating.setText(String.valueOf(movie.getPopularity()));
            ((MovieViewHolder)holder).tvVote.setText(String.valueOf(movie.getVoteCount()));
            ((MovieViewHolder)holder).tvDate.setText(movie.getReleaseDate());
            String imgPath = Const.BASE_URL_IMAGE + Const.IMAGE_SIZE + movie.getPosterPath();
            Picasso.with(mContext).load(imgPath).into(((MovieViewHolder)holder).ivMovie);
        }
        else {
            ((ProgressViewHolder)holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mMovieList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener){
        this.mOnLoadMoreListener = onLoadMoreListener;
    }

    public void setLoaded(){
        mLoading = false;
    }
}
