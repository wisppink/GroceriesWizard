package com.example.grocerieswizard.Fav;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerieswizard.R;
import com.example.grocerieswizard.databinding.FavItemRowBinding;
import com.example.grocerieswizard.home.RecipeModel;

import java.util.ArrayList;

public class FavRecyclerViewAdapter extends RecyclerView.Adapter<FavRecyclerViewAdapter.FavViewHolder> {
    public ArrayList<RecipeModel> favList = new ArrayList<>();
    private FavInterface favInterface;

    public FavRecyclerViewAdapter(FavInterface favInterface) {
        this.favInterface = favInterface;
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FavItemRowBinding binding = FavItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        FavViewHolder holder = new FavViewHolder(binding,favInterface);
        binding.unFav.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(binding.getRoot().getContext());
            RecipeModel recipeModel = favList.get(holder.getAdapterPosition());
            builder.setMessage(R.string.unfav_recipe_dialog_ask)
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        int adapterPosition = holder.getAdapterPosition();
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            favInterface.onRemoveFromFavorites(recipeModel);
                        }
                    })
                    .setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        });
        binding.unCart.setOnClickListener(v -> {
            RecipeModel recipeModel = favList.get(holder.getAdapterPosition());
            if (!favInterface.isRecipeSelected(recipeModel.getId())) {
                binding.unCart.setImageResource(R.drawable.baseline_remove_shopping_cart_24);
                favInterface.insertSelectedRecipe(recipeModel.getId());
                Toast.makeText(binding.getRoot().getContext(), R.string.added_to_cart, Toast.LENGTH_SHORT).show();
            } else {
                binding.unCart.setImageResource(R.drawable.add_cart);
                favInterface.removeSelectedRecipe(recipeModel.getId());
                Toast.makeText(binding.getRoot().getContext(), R.string.removed_from_cart, Toast.LENGTH_SHORT).show();
            }
            favInterface.updateIt(holder.getAdapterPosition());
        });
        return holder;
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

        private FavItemRowBinding binding;

        FavInterface favInterface;

        public void setFavInterface(FavInterface favInterface) {
            this.favInterface = favInterface;
        }

        public FavViewHolder(FavItemRowBinding binding,FavInterface favInterface) {
            super(binding.getRoot());
            setBinding(binding);
            setFavInterface(favInterface);
        }

        public void bind(RecipeModel recipeModel) {
            binding.favRecipeTitle.setText(recipeModel.getRecipeName());

            if (favInterface.isRecipeSelected(recipeModel.getId())) {
                // Set the cart icon if it is selected
                binding.unCart.setImageResource(R.drawable.baseline_remove_shopping_cart_24);
            } else {
                // Set the add to cart icon if it is not selected
                binding.unCart.setImageResource(R.drawable.add_cart);
            }
        }

        public void setBinding(FavItemRowBinding binding) {
            this.binding = binding;
        }
    }
}
