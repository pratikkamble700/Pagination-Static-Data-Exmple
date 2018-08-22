package com.pratik.pagginglibrary;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 3;
    private int currentPage = PAGE_START;

    RecyclerView rvPaginationDemo;
    ProgressBar main_progress;
    LinearLayoutManager layoutManager;
    PaginationAdapter mPaginationAdapter;
    private List<Movie> movies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvPaginationDemo = (RecyclerView)findViewById(R.id.rvPaginationDemo);
        main_progress = (ProgressBar)findViewById(R.id.main_progress);

        mPaginationAdapter = new PaginationAdapter(this);
        layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        rvPaginationDemo.setLayoutManager(layoutManager);
        rvPaginationDemo.setItemAnimator(new DefaultItemAnimator());
        rvPaginationDemo.setAdapter(mPaginationAdapter);

// Pagination
        rvPaginationDemo.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });


        // mocking network delay for API call
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadFirstPage();
            }
        }, 1000);

    }

    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");
        if(movies != null)movies.clear();
        movies = Movie.createMovies(mPaginationAdapter.getItemCount());
        main_progress.setVisibility(View.GONE);
        mPaginationAdapter.addAll(movies);

        if (currentPage <= TOTAL_PAGES) mPaginationAdapter.addLoadingFooter();
        else isLastPage = true;

    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);
        if(movies != null) movies.clear();
        movies = Movie.createMovies(mPaginationAdapter.getItemCount());

        mPaginationAdapter.removeLoadingFooter();
        isLoading = false;

        mPaginationAdapter.addAll(movies);

        if (currentPage != TOTAL_PAGES) mPaginationAdapter.addLoadingFooter();
        else isLastPage = true;
    }

}
