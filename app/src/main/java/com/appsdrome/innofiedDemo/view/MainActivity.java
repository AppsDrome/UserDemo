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
    //private int mCurrentPage=1;
    //private int mTotalPage;
    ProgressBar progressBar;
    RecyclerView.LayoutManager layoutManager;
    private int page_number=1;
    private int item_count=5;
    GetUserPostServiceData getUserPostServiceData;

    //variable for pagination
    private boolean isLoading= true;
    private int pastVisibleItems,visibleItemCount,TotalItemCount,previousTotal=0;
    private int view_threshold=5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        connectionDetector = new ConnectionDetector(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBarWeb);
        getUserPostServiceData = RetrofitInstance.getUserPostServiceData();
        userRecyclerView = (RecyclerView) findViewById(R.id.rv_user_list);
        layoutManager = new LinearLayoutManager(this);
        userRecyclerView.setHasFixedSize(true);
        userRecyclerView.setLayoutManager(layoutManager);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Users");
        if (!connectionDetector.hasConnection()) {
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }else{
            getUsers();
        }


        userRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = layoutManager.getChildCount();
                TotalItemCount = layoutManager.getItemCount();
                pastVisibleItems = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                if(dy>0){
                    if(isLoading){
                        if(TotalItemCount>previousTotal){
                            isLoading = false;
                            previousTotal = TotalItemCount;
                        }
                    }

                    if(!isLoading && (TotalItemCount-visibleItemCount)<=(pastVisibleItems+view_threshold)){
                        page_number++;
                        pagination();
                        isLoading = true;
                    }
                }

            }
        });

        swipeRefreshLayout=findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                page_number=1;
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

            Call<Post> call = getUserPostServiceData.getResults(page_number, item_count);

            call.enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {
                    progressBar.setVisibility(View.GONE);
                    Post post = response.body();
                    results = (ArrayList<Data>) post.getData();
                    usersAdapter = new UsersAdapter(results,MainActivity.this);
                    userRecyclerView.setAdapter(usersAdapter);
                    usersAdapter.notifyDataSetChanged();

                    swipeRefreshLayout.setRefreshing(false);

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

    private void pagination(){
        progressBar.setVisibility(View.VISIBLE);
        Call<Post> call = getUserPostServiceData.getResults(page_number, item_count);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                progressBar.setVisibility(View.GONE);
                Post post = response.body();
                results = (ArrayList<Data>) post.getData();
                if(results!= null && !results.isEmpty()){
                usersAdapter.AddUser(results);

                    Toast.makeText(MainActivity.this, "page_no"+page_number+"is loading", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(MainActivity.this, "No more User Available", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}