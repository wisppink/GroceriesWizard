package com.example.grocerieswizard.home;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerieswizard.R;
import com.example.grocerieswizard.databinding.RecyclerViewMenuBinding;
import com.example.grocerieswizard.databinding.RecyclerViewRowBinding;

import java.util.ArrayList;

public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_SWIPE = 1;
    private static final int VIEW_TYPE_ROW = 0;
    private final ArrayList<RecipeModel> recipeList = new ArrayList<>();
    private RecipeInterface recipeInterface;


    public RecipeRecyclerViewAdapter() {
    }

    @Override
    public int getItemViewType(int position) {
        RecipeModel recipeModel = recipeList.get(position);
        return recipeModel.isSwiped() ? VIEW_TYPE_SWIPE : VIEW_TYPE_ROW;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ROW) {
            RecyclerViewRowBinding rowBinding = RecyclerViewRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            RecyclerView.ViewHolder rowHolder = new RowViewHolder(rowBinding, recipeInterface);
            rowBinding.favIcon.setOnClickListener(v -> {
                int position = rowHolder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    RecipeModel recipeModel = recipeList.get(position);
                    int recipeId = recipeModel.getId();
                    boolean isCurrentlyFavorite = recipeInterface.isRecipeFavorite(recipeModel.getId());
                    if (!isCurrentlyFavorite) {
                        rowBinding.favIcon.setImageResource(R.drawable.baseline_favorite_24);
                        recipeInterface.insertRecipeFav(recipeId);
                        Toast.makeText(rowBinding.getRoot().getContext(), "Added to Favorites", Toast.LENGTH_SHORT).show();
                    } else {
                        rowBinding.favIcon.setImageResource(R.drawable.baseline_unfavorite_border_24);
                        recipeInterface.deleteRecipeFav(recipeId);
                        Toast.makeText(rowBinding.getRoot().getContext(), "Removed from Favorites", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            rowBinding.addCart.setOnClickListener(v -> {
                int position = rowHolder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    RecipeModel recipeModel = recipeList.get(position);
                    boolean isItInCart = recipeInterface.isRecipeSelected(recipeModel.getId());
                    if (!isItInCart) {
                        rowBinding.addCart.setImageResource(R.drawable.baseline_remove_shopping_cart_24);
                        recipeInterface.insertSelectedRecipe(recipeModel.getId());
                        Toast.makeText(rowBinding.getRoot().getContext(), R.string.added_to_cart, Toast.LENGTH_SHORT).show();
                    } else {
                        rowBinding.addCart.setImageResource(R.drawable.add_cart);
                        recipeInterface.deleteSelectedRecipe(recipeModel.getId());
                        Toast.makeText(rowBinding.getRoot().getContext(), R.string.removed_from_cart, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return rowHolder;

        } else {
            RecyclerViewMenuBinding menuBinding = RecyclerViewMenuBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            RecyclerView.ViewHolder menuHolder = new MenuViewHolder(menuBinding);

            menuBinding.editIcon.setOnClickListener(v -> recipeInterface.onItemEdit(menuHolder.getAdapterPosition()));
            menuBinding.shareIcon.setOnClickListener(v -> {
                recipeInterface.onItemShare(menuHolder.getAdapterPosition());
                Toast.makeText(menuBinding.getRoot().getContext(), "Share icon clicked", Toast.LENGTH_SHORT).show();
            });
            menuBinding.deleteIcon.setOnClickListener(v -> recipeInterface.onItemDelete(menuHolder.getAdapterPosition()));

            return menuHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecipeModel item = recipeList.get(position);
        if (holder instanceof RowViewHolder) {
            RowViewHolder rowViewHolder = (RowViewHolder) holder;
            rowViewHolder.bind(item);
        }
    }


    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public RecipeModel editRecipe(int position, RecipeModel editedRecipe) {
        if (position >= 0 && position < recipeList.size()) {
            RecipeModel oldRecipe = getItemAtPosition(position);
            oldRecipe.setRecipeName(editedRecipe.getRecipeName());
            oldRecipe.setImageBitmap(editedRecipe.getImageBitmap());
            String TAG = "RecipeAdapter";
            Log.d(TAG, "editRecipe: editedRecipe" + editedRecipe.getImageBitmap());
            Log.d(TAG, "editRecipe: oldRecipe" + oldRecipe.getImageBitmap());

            oldRecipe.setInstructions(editedRecipe.getInstructions());
            oldRecipe.setIngredients(editedRecipe.getIngredients());
            editedRecipe.setSwiped(false);
            int updatedRows = recipeInterface.updateRecipe(oldRecipe);
            if (updatedRows > 0) {
                notifyItemChanged(position);
            }
        }

        return editedRecipe;
    }


    // Add a new recipe to the list and notify the adapter
    public void addRecipe(RecipeModel recipe) {
        recipeList.add(recipe);
        notifyItemInserted(recipeList.size() - 1);
        recipeInterface.insertRecipe(recipe);

    }

    public RecipeModel getItemAtPosition(int position) {
        if (position >= 0 && position < recipeList.size()) {
            return recipeList.get(position);
        }
        return null;
    }

    // Remove a recipe from the list and notify the adapter
    public void removeRecipe(RecipeModel recipeModel) {
        // Find the position of the recipe in the list
        recipeModel.setSwiped(false);
        int pos = recipeList.indexOf(recipeModel);
        notifyItemChanged(pos);
        recipeList.remove(pos);
        notifyItemRemoved(pos);
        recipeInterface.deleteRecipe(recipeModel.getId());
    }

    public void setRecyclerViewInterface(RecipeInterface recipeInterface) {
        this.recipeInterface = recipeInterface;
    }

    public void setRecipeList(ArrayList<RecipeModel> rList) {
        for (RecipeModel recipeModel : rList) {
            recipeList.add(recipeModel);
            notifyItemInserted(recipeList.size() - 1);
        }

    }

    public void itemChanged(int position) {
        notifyItemChanged(position);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList() {
        recipeList.clear();
        notifyDataSetChanged();
    }


    public static class RowViewHolder extends RecyclerView.ViewHolder {
        RecipeInterface recipeInterface;
        RecyclerViewRowBinding binding;

        public void setBinding(RecyclerViewRowBinding binding) {
            this.binding = binding;
        }

        public RowViewHolder(RecyclerViewRowBinding binding, RecipeInterface recipeInterface) {
            super(binding.getRoot());
            setBinding(binding);
            this.recipeInterface = recipeInterface;

            binding.getRoot().setOnClickListener(
                    v -> recipeInterface.onItemClick(getAdapterPosition())
            );
        }

        public void bind(RecipeModel recipeModel) {
            binding.textView.setText(recipeModel.getRecipeName());

            if (recipeModel.getImageBitmap() != null) {
                binding.defaultCardRecipeImage.setImageBitmap(recipeModel.getImageBitmap());
            } else {
                binding.defaultCardRecipeImage.setImageResource(R.drawable.recipe_image_default);
            }


            if (recipeInterface.isRecipeFavorite(recipeModel.getId())) {
                // Set the favorite icon if it is favorite
                binding.favIcon.setImageResource(R.drawable.baseline_favorite_24);
            } else {
                // Set the non-favorite icon if it is not favorite
                binding.favIcon.setImageResource(R.drawable.baseline_unfavorite_border_24);
            }


            if (recipeInterface.isRecipeSelected(recipeModel.getId())) {
                // Set the cart icon if it is selected
                binding.addCart.setImageResource(R.drawable.baseline_remove_shopping_cart_24);
            } else {
                // Set the add to cart icon if it is not selected
                binding.addCart.setImageResource(R.drawable.add_cart);
            }


        }
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        public MenuViewHolder(RecyclerViewMenuBinding binding) {
            super(binding.getRoot());
        }
    }
}
