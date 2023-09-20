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
import com.example.grocerieswizard.databinding.FavItemRowBinding;
import com.example.grocerieswizard.interfaces.FavInterface;
import com.example.grocerieswizard.models.RecipeModel;

import java.util.ArrayList;

public class FavRecyclerViewAdapter extends RecyclerView.Adapter<FavRecyclerViewAdapter.FavViewHolder> {
    private static final String TAG = "FavRecyclerViewAdapter";

    public ArrayList<RecipeModel> favList = new ArrayList<>();
    FavItemRowBinding binding;
    private FavInterface favInterface;
    public FavRecyclerViewAdapter() {
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = FavItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        View view = binding.getRoot();
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

    public ArrayList<RecipeModel> getFavList() {
        return favList;
    }

    public void setFavList(ArrayList<RecipeModel> recipes) {
        for (RecipeModel recipe : recipes) {
            favList.add(recipe);
            notifyItemInserted(favList.size()-1);
        }
    }

    public void setFavInterface(FavInterface favInterface) {
        this.favInterface = favInterface;
    }

    public void removeItem(int pos) {
        favList.remove(favList.get(pos));
        notifyItemRemoved(pos);
    }

    public class FavViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final CardView background;

        public FavViewHolder(@NonNull View itemView) {
            super(itemView);
            title = binding.favRecipeTitle;
            ImageView favButton = binding.unFav;
            background = binding.cardView;

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
