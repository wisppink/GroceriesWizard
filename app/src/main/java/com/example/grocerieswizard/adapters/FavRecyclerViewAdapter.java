package com.example.grocerieswizard.adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerieswizard.R;
import com.example.grocerieswizard.interfaces.FavInterface;
import com.example.grocerieswizard.models.RecipeModel;

import java.util.ArrayList;

public class FavRecyclerViewAdapter extends RecyclerView.Adapter<FavRecyclerViewAdapter.FavViewHolder> {
    private static final String TAG = "FavRecyclerViewAdapter";
    public ArrayList<RecipeModel> favList = new ArrayList<>();
    private FavInterface favInterface;

    public FavRecyclerViewAdapter() {
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_item_row, parent, false);
        return new FavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder holder, int position) {
        RecipeModel recipeModel = favList.get(position);
        holder.bind(recipeModel);
    }

    @Override
    public int getItemCount() {
        return favList.size();
    }

    public void setFavList(ArrayList<RecipeModel> recipes) {
        this.favList = recipes;
    }

    public void setFavInterface(FavInterface favInterface) {
        this.favInterface = favInterface;
    }

    public class FavViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final CardView background;

        public FavViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.fav_recipe_title);
            ImageView favButton = itemView.findViewById(R.id.unFav);
            background = itemView.findViewById(R.id.cardView);

            favButton.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                builder.setMessage(R.string.unfav_recipe_dialog_ask)
                        .setPositiveButton(R.string.yes, (dialog, which) -> {
                            int adapterPosition = getAdapterPosition();
                            if (adapterPosition != RecyclerView.NO_POSITION) {
                                favInterface.onRemoveFromFavorites(favList.get(adapterPosition));
                            }
                        })
                        .setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();


            });

        }

        public void bind(RecipeModel recipeModel) {
            title.setText(recipeModel.getRecipeName());
            recipeModel.setFavorite(true);
            if (favInterface.isRecipeSelected(recipeModel.getId())) {
                recipeModel.setSelected(true);
                background.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.gray));
            } else {
                background.setBackgroundColor(Color.TRANSPARENT);
            }

            itemView.setOnClickListener(v -> {
                Log.d(TAG, "bind: clicked!");
                if (!recipeModel.isSelected()) {
                    recipeModel.setSelected(true);
                    background.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.gray));
                    favInterface.insertSelectedRecipe(recipeModel.getId());
                } else {
                    recipeModel.setSelected(false);
                    favInterface.removeSelectedRecipe(recipeModel.getId());
                    itemView.setBackgroundColor(Color.TRANSPARENT);
                }
                notifyItemChanged(getAdapterPosition());
            });
        }
    }
}
