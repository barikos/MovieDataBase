package com.minutes.moviesdatabase.ui;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.minutes.moviesdatabase.Const;
import com.minutes.moviesdatabase.R;
import com.minutes.moviesdatabase.api.ApiClient;
import com.minutes.moviesdatabase.api.Movie;
import com.minutes.moviesdatabase.api.MovieDBService;
import com.minutes.moviesdatabase.api.MovieResponse;
import com.minutes.moviesdatabase.ui.adapter.MovieRecyclerAdapter;
import com.minutes.moviesdatabase.ui.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_nav_view)
    private NavigationView mNavigationView;
    @BindView(R.id.main_drawer)
    private DrawerLayout mDrawerLayout;
    @BindView(R.id.main_tool_bar)
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    MovieRecyclerAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

    private List<Movie> mMovieList = new ArrayList<>();

    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.main_rec_view);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.main_drawer);
        mNavigationView = (NavigationView)findViewById(R.id.main_nav_view);
        mToolbar = (Toolbar)findViewById(R.id.main_tool_bar);
        setSupportActionBar(mToolbar);

    }

    @Override
    protected void onResume() {
        super.onResume();

        mNavigationView.setCheckedItem(R.id.nav_movies);
        mNavigationView.setNavigationItemSelectedListener(getNavigationListener());
        mRecyclerView.setLayoutManager(mLayoutManager);
       // mRecyclerView.setOnScrollListener(getScrollListener());
        mAdapter = new MovieRecyclerAdapter(MainActivity.this,mMovieList,mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        MovieDBService service = ApiClient.getClient().create(MovieDBService.class);
        Call<MovieResponse> call = service.getTopRatedMovies(Const.API_KEY,page++);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    mMovieList.addAll(response.body().getMovies());
                    Log.d(Const.LOG_TAG, "Activity" + String.valueOf(mMovieList.size()));
                    mAdapter.notifyDataSetChanged();
                    //You must rewrite this!!!! Don't forget!!
                    page++;
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d(Const.LOG_TAG, "fail");
            }
        });

        mAdapter.setOnLoadMoreListener(getOnLoadMoreListener());

    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private NavigationView.OnNavigationItemSelectedListener getNavigationListener(){
        return new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Toast.makeText(MainActivity.this, item.getTitle() +  " pressed", Toast.LENGTH_SHORT).show();
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        };
    }

  /*  private EndlessRecyclerOnScrollListener getScrollListener(){
        return  new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore(int currentPage) {
                MovieDBService service = ApiClient.getClient().create(MovieDBService.class);

                Call<MovieResponse> call = service.getTopRatedMovies(Const.API_KEY,++page);
                call.enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful()) {
                            mMovieList.addAll(response.body().getMovies());
                            Log.d(Const.LOG_TAG, "Activity" + String.valueOf(mMovieList.size()));
                            mAdapter.notifyDataSetChanged();
                            //You must rewrite this!!!! Don't forget!!

                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        Log.d(Const.LOG_TAG, "fail");
                    }
                });
            }
        };
    }*/

    private OnLoadMoreListener getOnLoadMoreListener(){
        return new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                Log.d(Const.LOG_TAG,"load more");
                mMovieList.add(null);
                mAdapter.notifyItemInserted(mMovieList.size() - 1);

                MovieDBService service = ApiClient.getClient().create(MovieDBService.class);

                Call<MovieResponse> call = service.getTopRatedMovies(Const.API_KEY,page++);
                call.enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful()) {
                            List<Movie> movieList = response.body().getMovies();

                            mMovieList.remove(mMovieList.size() - 1);
                            mAdapter.notifyItemRemoved(mMovieList.size());

                            mMovieList.addAll(movieList);
                            Log.d(Const.LOG_TAG, "Activity" + String.valueOf(mMovieList.size()));
                            mAdapter.notifyDataSetChanged();
                            mAdapter.setLoaded();
                            //You must rewrite this!!!! Don't forget!!

                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        Log.d(Const.LOG_TAG, "fail");
                        mMovieList.remove(mMovieList.size() - 1);
                        mAdapter.notifyItemRemoved(mMovieList.size());
                    }
                });
            }
        };
    }


}

