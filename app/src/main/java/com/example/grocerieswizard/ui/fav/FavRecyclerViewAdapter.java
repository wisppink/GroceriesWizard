package com.example.grocerieswizard.ui.fav;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerieswizard.R;
import com.example.grocerieswizard.databinding.FavItemRowBinding;
import com.example.grocerieswizard.ui.model.RecipeUi;

import java.util.ArrayList;
import java.util.List;

public class FavRecyclerViewAdapter extends RecyclerView.Adapter<FavRecyclerViewAdapter.FavViewHolder> {
    public ArrayList<RecipeUi> favList = new ArrayList<>();
    private FavInterface favInterface;

    public FavRecyclerViewAdapter(FavInterface favInterface) {
        this.favInterface = favInterface;
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FavItemRowBinding binding = FavItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        FavViewHolder holder = new FavViewHolder(binding);
        binding.unFav.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            RecipeUi recipeUi = favList.get(position);
            if (position != RecyclerView.NO_POSITION) {
                favInterface.toggleFavoriteRecipe(recipeUi);
            }
        });
        binding.unCart.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            RecipeUi recipeUi = favList.get(position);
            if (position != RecyclerView.NO_POSITION) {
                favInterface.toggleCartRecipe(recipeUi);
            }
        });
        return holder;
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

    public void setFavList(List<RecipeUi> recipeUis) {
        for (RecipeUi recipeUi : recipeUis) {
            favList.add(recipeUi);
            notifyItemInserted(favList.size() - 1);
        }
    }

    public void itemChanged(int position) {
        notifyItemChanged(position);
    }
    public void removeRecipe(RecipeUi recipeUi) {
        // Find the position of the recipe in the list
        recipeUi.setSwiped(false);
        int pos = favList.indexOf(recipeUi);
        notifyItemChanged(pos);
        favList.remove(pos);
        notifyItemRemoved(pos);
    }

    public int getPositionForRecipe(RecipeUi recipe) {
        for (int i = 0; i < favList.size(); i++) {
            if (favList.get(i).getId() == recipe.getId()) {
                return i;
            }
        }
        return -1;
    }

    public void setFavInterface(FavInterface favInterface) {
        this.favInterface = favInterface;
    }


    public static class FavViewHolder extends RecyclerView.ViewHolder {

        private FavItemRowBinding binding;

        public FavViewHolder(FavItemRowBinding binding) {
            super(binding.getRoot());
            setBinding(binding);
        }

        public void bind(RecipeUi recipeUi) {
            binding.favRecipeTitle.setText(recipeUi.getRecipeName());

            if (recipeUi.isCart()) {
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
