package com.example.grocerieswizard.Fav;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerieswizard.R;
import com.example.grocerieswizard.databinding.FavItemRowBinding;
import com.example.grocerieswizard.ui.model.RecipeUi;

import java.util.ArrayList;
import java.util.List;

public class FavRecyclerViewAdapter extends RecyclerView.Adapter<FavRecyclerViewAdapter.FavViewHolder> {
    private static final String TAG = "FavRecyclerViewAdapter";
    public ArrayList<RecipeUi> favList = new ArrayList<>();
    private FavInterface favInterface;

    public FavRecyclerViewAdapter(FavInterface favInterface) {
        this.favInterface = favInterface;
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FavItemRowBinding binding = FavItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FavViewHolder(binding, favInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder holder, int position) {
        RecipeUi recipeUi = favList.get(position);
        holder.bind(recipeUi);
    }

    @Override
    public int getItemCount() {
        return favList.size();
    }

    public ArrayList<RecipeUi> getFavList() {
        return favList;
    }

    public void setFavList(List<RecipeUi> recipeUis) {
        for (RecipeUi recipeUi : recipeUis) {
            favList.add(recipeUi);
            notifyItemInserted(favList.size() - 1);
        }
    }

    public void setFavInterface(FavInterface favInterface) {
        this.favInterface = favInterface;
    }

    public void removeItem(int pos) {
        favList.remove(favList.get(pos));
        notifyItemRemoved(pos);
    }

    public void updateIt(int adapterPosition) {
        notifyItemChanged(adapterPosition);
    }

    public static class FavViewHolder extends RecyclerView.ViewHolder {
        RecipeUi recipeUi;
        private FavItemRowBinding favItemRowBinding;

        FavInterface favInterface;

        public void setFavInterface(FavInterface favInterface) {
            this.favInterface = favInterface;
        }

        public FavViewHolder(FavItemRowBinding binding, FavInterface favInterface) {
            super(binding.getRoot());
            setFavItemRowBinding(binding);
            setFavInterface(favInterface);
            binding.unFav.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                builder.setMessage(R.string.unfav_recipe_dialog_ask)
                        .setPositiveButton(R.string.yes, (dialog, which) -> {
                            int adapterPosition = getAdapterPosition();
                            if (adapterPosition != RecyclerView.NO_POSITION) {
                                favInterface.onRemoveFromFavorites(recipeUi);
                            }
                        })
                        .setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();


            });

        }

        public void bind(RecipeUi recipeUi) {
            favItemRowBinding.favRecipeTitle.setText(recipeUi.getRecipeName());
            if (favInterface.isRecipeSelected(recipeUi.getId())) {
                favItemRowBinding.cardView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.gray));
            } else {
                favItemRowBinding.cardView.setBackgroundColor(Color.TRANSPARENT);
            }

            itemView.setOnClickListener(v -> {
                Log.d(TAG, "bind: clicked!");
                if (!favInterface.isRecipeSelected(recipeUi.getId())) {
                    favItemRowBinding.cardView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.gray));
                    favInterface.insertSelectedRecipe(recipeUi.getId());
                } else {
                    favInterface.removeSelectedRecipe(recipeUi.getId());
                    itemView.setBackgroundColor(Color.TRANSPARENT);
                }
                favInterface.updateIt(getAdapterPosition());
            });
            this.recipeUi = recipeUi;
        }

        public void setFavItemRowBinding(FavItemRowBinding favItemRowBinding) {
            this.favItemRowBinding = favItemRowBinding;
        }
    }
}