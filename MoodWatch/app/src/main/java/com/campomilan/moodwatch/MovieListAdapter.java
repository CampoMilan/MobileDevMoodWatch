package com.campomilan.moodwatch;

import android.content.Context;
import android.graphics.Movie;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class MovieListAdapter extends RecyclerView.Adapter<MovieViewHolder> {
    protected final LinkedList<String> mMovieList;
    private LayoutInflater mInflater;

    public MovieListAdapter(Context context, LinkedList<String> movieList){
        mInflater = LayoutInflater.from(context);
        this.mMovieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mMovieView = mInflater.inflate(R.layout.movielist_item, parent, false);
        return new MovieViewHolder(mMovieView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        String mCurrent = mMovieList.get(position);
        holder.movieItemView.setText(mCurrent);
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }
}

class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public final TextView movieItemView;
    final MovieListAdapter mAdapter;

    public MovieViewHolder(@NonNull View itemView, MovieListAdapter mAdapter) {
        super(itemView);
        movieItemView = itemView.findViewById(R.id.movie);
        this.mAdapter = mAdapter;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int mPosition = getLayoutPosition();
        String element = mAdapter.mMovieList.get(mPosition);

        mAdapter.mMovieList.set(mPosition, "Clicked!" + element);

        mAdapter.notifyDataSetChanged();
    }
}
