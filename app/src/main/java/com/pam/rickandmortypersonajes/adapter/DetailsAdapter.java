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
import com.pam.rickandmortypersonajes.entity.CharacterDetails;

import java.util.ArrayList;
import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.CharacterViewHolder> {

    private List<CharacterDetails> characterResults;
    private Context context;

    public DetailsAdapter(Context context) {
        this.characterResults = new ArrayList<>();
        this.context = context;
    }

    public void addCharacterResultsLocation(CharacterDetails characterResults) {
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
        CharacterDetails character = characterResults.get(position);
        String name = "";
        holder.bind(character, name);

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

    @Override
    public int getItemCount() {
        return characterResults.size();
    }

    static class CharacterViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewLocation;

        public CharacterViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(CharacterDetails result, String name) {

            ImageView imageViewProfile = itemView.findViewById(R.id.imageProfileAdapter);
            textViewName = itemView.findViewById(R.id.name);
            textViewLocation = itemView.findViewById(R.id.location);


            textViewName.setText(result.name);
            textViewLocation.setText(result.location.name);

            Glide.with(itemView)
                    .load(result.image)
                    .into(imageViewProfile);

        }
    }
}

