package com.campomilan.moodwatch;

import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private final LinkedList<String> mMovieList = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private MovieListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int movieListSize = mMovieList.size();
                mMovieList.addLast("+ Movie" + movieListSize);
                mRecyclerView.getAdapter().notifyItemInserted(movieListSize);
                mRecyclerView.smoothScrollToPosition(movieListSize);
            }
        });

        //Test films toevoegen
        for (int i = 0; i < 20; i++){
            mMovieList.addLast("Movie" + i);
        }

        mRecyclerView = findViewById(R.id.recyclerview);
        mAdapter = new MovieListAdapter(this, mMovieList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //tijdelijke oplossing om naar andere activities te navigeren
        switch (item.getItemId()){
            case R.id.action_moods:
                Intent intent = new Intent(MainActivity.this, SelectionActivity.class);
                startActivity(intent);
                return true;
            default:
                // Do nothing

        }

        return super.onOptionsItemSelected(item);
    }
}