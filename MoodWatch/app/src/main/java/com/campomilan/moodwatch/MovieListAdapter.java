package com.campomilan.moodwatch;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.LinkedList;
import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    private Context mContext;
    private List<APIModelMovie> mData;

    public MovieListAdapter(Context context, List<APIModelMovie> mData){
        this.mData = mData;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.movie_item, parent, false);

        return new MovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        //id and title bind
        holder.id.setText(mData.get(position).getId());
        holder.title.setText(mData.get(position).getTitle());

        //img bind
        Glide.with(mContext)
                .load(mData.get(position).getImgURL())
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder{

        TextView id;
        TextView title;
        ImageView img;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id_txt);
            title = itemView.findViewById(R.id.title_txt);
            img = itemView.findViewById(R.id.imageView);
        }

    }
}


