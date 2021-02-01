package com.pam.rickandmortypersonajes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.pam.rickandmortypersonajes.adapter.DetailsAdapter;
import com.pam.rickandmortypersonajes.entity.CharacterDetails;
import com.pam.rickandmortypersonajes.entity.ResultCharacter;
import com.pam.rickandmortypersonajes.entity.ResultEpisode;
import com.pam.rickandmortypersonajes.entity.ResultLastLocation;
import com.pam.rickandmortypersonajes.util.RetrofitGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private final static String TAG = "DetailsActivity";
    private static List<Integer> charactersIDs = new ArrayList<>();
    private TextView name, status, location, episode, personagesAlsoFrom;
    private ImageView imageView;
    private boolean loading;
    private DetailsAdapter detailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.character);

        dataSetPersonage();

        Button buttonBack = findViewById(R.id.textButton);
        buttonBack.setOnClickListener(view -> {
            Intent intent = new Intent(DetailsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        RecyclerView recyclerView = findViewById(R.id.character_by_location);
        detailsAdapter = new DetailsAdapter(this);
        recyclerView.setAdapter(detailsAdapter);
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
                            personageInfoByLocation();
                        }
                    }
                }
            }
        });
        personageInfoByLocation();
        loading = true;
    }

    public void dataSetPersonage() {
        int idPersonage = Integer.parseInt(getIntent().getExtras().getString("urlCharacter"));

        name = findViewById(R.id.personage_name);
        status = findViewById(R.id.aboutStatus);
        location = findViewById(R.id.locationInfo);
        imageView = findViewById(R.id.profileImage);
        episode = findViewById(R.id.firstSeen);
        personagesAlsoFrom = findViewById(R.id.also);

        dataGetPersonages(idPersonage);
    }

    private void dataGetPersonages(int id) {
        RetrofitGenerator.getApiService().getCharacterDetails(id)
                .enqueue(new Callback<CharacterDetails>() {
                    @Override
                    public void onResponse(@NonNull Call<CharacterDetails> call, @NonNull Response<CharacterDetails> response) {
                        if (response.body() != null) {
                            configUIElements(response.body());
                            getEpisodeData(getFirstEpisode(response.body().episode));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CharacterDetails> call, @NonNull Throwable t) {
                        Log.e(TAG, t.getMessage(), t);
                    }
                });
    }

    private String getFirstEpisode(List<String> episode) {
        return episode.get(0);
    }

    private void getEpisodeData(String url) {
        String[] divURL = url.split("/");
        String charID = divURL[divURL.length - 1];

        RetrofitGenerator.getApiService().getEpisodeDetails(Integer.parseInt(charID))
                .enqueue(new Callback<ResultEpisode>() {
                    @Override
                    public void onResponse(@NonNull Call<ResultEpisode> call, @NonNull Response<ResultEpisode> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                updateFirstEpisodeElement(response.body().name);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResultEpisode> call, @NonNull Throwable t) {
                        Log.e(TAG, t.getMessage(), t);
                    }
                });
    }

    public void personagesInfo(int id) {
        RetrofitGenerator.getApiService().getCharacterDetailsByID(id)
                .enqueue(new Callback<ResultCharacter>() {
                    @Override
                    public void onResponse(@NonNull Call<ResultCharacter> call, @NonNull Response<ResultCharacter> response) {
                        loading = true;
                        detailsAdapter.addCharacterResultsLocation(response.body());
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResultCharacter> call, @NonNull Throwable t) {
                        loading = true;
                        Log.e(TAG, t.getMessage(), t);
                    }
                });
    }

    private void getUserIds(List<String> resultLastLocation) {

        for (String s : resultLastLocation) {
            String[] divURL = s.split("/");
            String charID = divURL[divURL.length - 1];
            charactersIDs.add(Integer.valueOf(charID));
        }
    }

    public void personageInfoByLocation() {
        int idLocation = Integer.parseInt(getIntent().getExtras().getString("urlLocation"));

        RetrofitGenerator.getApiService().getLocations(idLocation)
                .enqueue(new Callback<ResultLastLocation>() {
                    @Override
                    public void onResponse(@NonNull Call<ResultLastLocation> call, @NonNull Response<ResultLastLocation> response) {
                        assert response.body() != null;
                        getUserIds(response.body().residents);

                        for (int i : charactersIDs) {
                            personagesInfo(i);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResultLastLocation> call, @NonNull Throwable t) {
                        loading = true;
                        Log.e(TAG, t.getMessage(), t);
                    }
                });
    }


    private void configUIElements(CharacterDetails data) {
        String alsoFrom = "Also from \"" + data.location.name + "\"";
        String someTestSpace = " " + data.status;

        name.setText(data.name);
        status.setText(someTestSpace);
        location.setText(data.location.name);
        personagesAlsoFrom.setText(alsoFrom);

        Glide.with(imageView)
                .load(data.image)
                .into(imageView);

        switch (data.status) {
            case "Alive":
                status.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.status_green, 0, 0, 0);
                break;
            case "Dead":
                status.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.status_red, 0, 0, 0);
                break;
            default:
                status.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.status_grey, 0, 0, 0);
                break;
        }
    }

    private void updateFirstEpisodeElement(String name) {
        episode.setText(name);
    }
}
