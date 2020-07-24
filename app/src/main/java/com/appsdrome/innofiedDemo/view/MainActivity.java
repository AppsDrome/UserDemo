package com.appsdrome.innofiedDemo.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.appsdrome.innofiedDemo.R;
import com.appsdrome.innofiedDemo.adaptor.UsersAdapter;
import com.appsdrome.innofiedDemo.model.Data;
import com.appsdrome.innofiedDemo.model.Post;
import com.appsdrome.innofiedDemo.service.GetUserPostServiceData;
import com.appsdrome.innofiedDemo.service.RetrofitInstance;
import com.appsdrome.innofiedDemo.util.ConnectionDetector;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Data> results;
    private UsersAdapter usersAdapter;
    private RecyclerView userRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    ConnectionDetector connectionDetector;
    private int mCurrentPage=1;
    private int mTotalPage;
    ProgressBar progressBar;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        connectionDetector = new ConnectionDetector(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBarWeb);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Users");
        if (!connectionDetector.hasConnection()) {
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }else{
            getUsers();
        }
        userRecyclerView = (RecyclerView) findViewById(R.id.rv_user_list);
        userRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        userRecyclerView.setLayoutManager(layoutManager);

        userRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (isLastItemDisplaying(userRecyclerView) && mCurrentPage<mTotalPage) {

                    mCurrentPage = mCurrentPage + 1;

                    getUsers();
                    usersAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });

        swipeRefreshLayout=findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mCurrentPage=1;
                getUsers();

            }
        });
    }

    private void getUsers() {
        if (!connectionDetector.hasConnection()) {
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }else {
            progressBar.setVisibility(View.VISIBLE);
            GetUserPostServiceData getUserPostServiceData = RetrofitInstance.getUserPostServiceData();
            int mPerPage = 5;
            Call<Post> call = getUserPostServiceData.getResults(mCurrentPage, mPerPage);

            call.enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {
                    progressBar.setVisibility(View.GONE);
                    Post post = response.body();
                    mTotalPage=post.getTotalPages();
                    results = (ArrayList<Data>) post.getData();

                    swipeRefreshLayout.setRefreshing(false);
                    ShowData();
                }

                @Override
                public void onFailure(Call<Post> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    private void ShowData() {

        usersAdapter = new UsersAdapter(results,this);
        userRecyclerView.setAdapter(usersAdapter);
        usersAdapter.notifyDataSetChanged();

}

    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (Objects.requireNonNull(recyclerView.getAdapter()).getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findLastVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }

}

