package com.pam.rickandmortypersonajes;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.pam.rickandmortypersonajes.adapter.CharacterAdapter;
import com.pam.rickandmortypersonajes.entity.ResultList;
import com.pam.rickandmortypersonajes.util.RetrofitGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";
    private int page = 1;
    private boolean loading;
    private CharacterAdapter characterAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.Character);
        characterAdapter = new CharacterAdapter(this);
        recyclerView.setAdapter(characterAdapter);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastItem = layoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if (visibleItemCount + pastItem >= totalItemCount) {
                            loading = false;
                            page++;
                            personagesInfo(page);
                        }
                    }
                }
            }
        });

        loading = true;
        personagesInfo(page);
    }

    public void personagesInfo(int page) {

        RetrofitGenerator.getApiService().getCharacters(page)
                .enqueue(new Callback<ResultList>() {
                    @Override
                    public void onResponse(@NonNull Call<ResultList> call, @NonNull Response<ResultList> response) {
                        loading = true;
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                characterAdapter.addCharacterResults(response.body().results);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResultList> call, @NonNull Throwable t) {
                        loading = true;
                        Log.e(TAG, t.getMessage(), t);
                    }
                });
    }
}