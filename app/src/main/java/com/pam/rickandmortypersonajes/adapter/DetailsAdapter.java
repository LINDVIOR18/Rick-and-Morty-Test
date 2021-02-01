package com.pam.rickandmortypersonajes.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.pam.rickandmortypersonajes.DetailsActivity;
import com.pam.rickandmortypersonajes.R;
import com.pam.rickandmortypersonajes.entity.ResultCharacter;
import com.pam.rickandmortypersonajes.entity.ResultEpisode;
import com.pam.rickandmortypersonajes.util.RetrofitGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.CharacterViewHolder> {

    private List<ResultCharacter> characterResults;
    private Context context;

    public DetailsAdapter(Context context) {
        this.characterResults = new ArrayList<>();
        this.context = context;
    }

    public void addCharacterResultsLocation(ResultCharacter characterResults) {
//        clear();
        this.characterResults.add(characterResults);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CharacterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View card = inflater.inflate(R.layout.character_list_adapter, parent, false);
        return new CharacterViewHolder(card);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterViewHolder holder, int position) {
        ResultCharacter character = characterResults.get(position);
        holder.bind(character);

        holder.itemView.setOnClickListener(view -> {
            String url = characterResults.get(position).url;
            String[] divURL = url.split("/");
            String charID = divURL[divURL.length - 1];

            String locationURL = characterResults.get(position).location.url;
            String[] locationDivURL = locationURL.split("/");
            String locationID = locationDivURL[divURL.length - 1];

            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("urlCharacter", charID);
            intent.putExtra("urlLocation", locationID);
            context.startActivity(intent);
        });
    }

    //    public void clear() {
//        int size = characterResults.size();
//        characterResults.clear();
//        notifyItemRangeRemoved(0, size);
//    }
    @Override
    public int getItemCount() {
        return characterResults.size();
    }

    static class CharacterViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewLocation, textViewEpisode;

        public CharacterViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(ResultCharacter result) {

            ImageView imageViewProfile = itemView.findViewById(R.id.imageProfileAdapter);
            textViewName = itemView.findViewById(R.id.name);
            textViewLocation = itemView.findViewById(R.id.location);
            textViewEpisode = itemView.findViewById(R.id.aboutEpisode);

            textViewName.setText(result.name);
            textViewLocation.setText(result.location.name);
            getEpisodeData(getFirstEpisode(result.episode));

            Glide.with(itemView)
                    .load(result.image)
                    .into(imageViewProfile);

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
                        }
                    });
        }

        private void updateFirstEpisodeElement(String name) {
            textViewEpisode.setText(name);
        }
    }
}

