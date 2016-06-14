package com.minutes.moviesdatabase.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.minutes.moviesdatabase.Const;
import com.minutes.moviesdatabase.R;
import com.minutes.moviesdatabase.api.ApiClient;
import com.minutes.moviesdatabase.api.Movie;
import com.minutes.moviesdatabase.api.MovieDBService;
import com.minutes.moviesdatabase.api.MovieResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by barikos on 09.06.16.
 */
public class GenreFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_genre,container,false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        MovieDBService service = ApiClient.getClient().create(MovieDBService.class);

        Call<MovieResponse> call = service.getTopRatedMovies(Const.API_KEY);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    List<Movie> movies = response.body().getMovies();
                    Log.d(Const.LOG_TAG, "FFragment onResponse" + String.valueOf(movies.size()));
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d(Const.LOG_TAG, "fail");
            }
        });

    }
}
