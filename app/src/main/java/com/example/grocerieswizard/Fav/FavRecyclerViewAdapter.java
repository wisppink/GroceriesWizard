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
import com.example.grocerieswizard.home.RecipeModel;

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
        FavItemRowBinding binding = FavItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FavViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder holder, int position) {
        RecipeModel recipeModel = favList.get(position);
        recipeModel.setFavInterface(favInterface);
        recipeModel.setFavAdapter(this);
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

    public static class FavViewHolder extends RecyclerView.ViewHolder {
        RecipeModel recipeModel;
        private FavItemRowBinding favItemRowBinding;

        public FavViewHolder(FavItemRowBinding binding) {
            super(binding.getRoot());
            setFavItemRowBinding(binding);
            binding.unFav.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                builder.setMessage(R.string.unfav_recipe_dialog_ask)
                        .setPositiveButton(R.string.yes, (dialog, which) -> {
                            int adapterPosition = getAdapterPosition();
                            if (adapterPosition != RecyclerView.NO_POSITION) {
                                recipeModel.getFavInterface().onRemoveFromFavorites(recipeModel);
                            }
                        })
                        .setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();


            });

        }

        public void bind(RecipeModel recipeModel) {
            favItemRowBinding.favRecipeTitle.setText(recipeModel.getRecipeName());
            if (recipeModel.getFavInterface().isRecipeSelected(recipeModel.getId())) {
                recipeModel.setSelected(true);
                favItemRowBinding.cardView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.gray));
            } else {
                favItemRowBinding.cardView.setBackgroundColor(Color.TRANSPARENT);
            }

            itemView.setOnClickListener(v -> {
                Log.d(TAG, "bind: clicked!");
                if (!recipeModel.isSelected()) {
                    recipeModel.setSelected(true);
                    favItemRowBinding.cardView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.gray));
                    recipeModel.getFavInterface().insertSelectedRecipe(recipeModel.getId());
                } else {
                    recipeModel.setSelected(false);
                    recipeModel.getFavInterface().removeSelectedRecipe(recipeModel.getId());
                    itemView.setBackgroundColor(Color.TRANSPARENT);
                }
                recipeModel.getFavAdapter().notifyItemChanged(getAdapterPosition());
            });
            this.recipeModel = recipeModel;
        }

        public void setFavItemRowBinding(FavItemRowBinding favItemRowBinding) {
            this.favItemRowBinding = favItemRowBinding;
        }
    }
}
