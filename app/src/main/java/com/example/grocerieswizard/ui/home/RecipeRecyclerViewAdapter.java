package com.example.grocerieswizard.ui.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerieswizard.R;
import com.example.grocerieswizard.databinding.RecyclerViewMenuBinding;
import com.example.grocerieswizard.databinding.RecyclerViewRowBinding;
import com.example.grocerieswizard.ui.model.RecipeUi;

import java.util.ArrayList;
import java.util.List;

public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_SWIPE = 1;
    private static final int VIEW_TYPE_ROW = 0;
    private final ArrayList<RecipeUi> recipeUiList = new ArrayList<>();
    private RecipeInterface recipeInterface;


    public RecipeRecyclerViewAdapter() {
    }

    @Override
    public int getItemViewType(int position) {
        RecipeUi recipeUi = recipeUiList.get(position);
        return recipeUi.isSwiped() ? VIEW_TYPE_SWIPE : VIEW_TYPE_ROW;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ROW) {
            RecyclerViewRowBinding rowBinding = RecyclerViewRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            RecyclerView.ViewHolder rowHolder = new RowViewHolder(rowBinding, recipeInterface);

            rowBinding.getRoot().setOnClickListener(
                    v -> recipeInterface.onItemClick(recipeUiList.get(rowHolder.getAdapterPosition()))
            );
            rowBinding.favIcon.setOnClickListener(v -> {
                int position = rowHolder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    RecipeUi recipeUi = recipeUiList.get(position);
                    int recipeId = recipeUi.getId();
                    boolean isCurrentlyFavorite = recipeInterface.isRecipeFavorite(recipeUi.getId());
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
                    RecipeUi recipeUi = recipeUiList.get(position);
                    boolean isItInCart = recipeInterface.isRecipeSelected(recipeUi.getId());
                    if (!isItInCart) {
                        rowBinding.addCart.setImageResource(R.drawable.baseline_remove_shopping_cart_24);
                        recipeInterface.insertSelectedRecipe(recipeUi.getId());
                        Toast.makeText(rowBinding.getRoot().getContext(), R.string.added_to_cart, Toast.LENGTH_SHORT).show();
                    } else {
                        rowBinding.addCart.setImageResource(R.drawable.add_cart);
                        recipeInterface.deleteSelectedRecipe(recipeUi.getId());
                        Toast.makeText(rowBinding.getRoot().getContext(), R.string.removed_from_cart, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return rowHolder;

        } else {
            RecyclerViewMenuBinding menuBinding = RecyclerViewMenuBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            RecyclerView.ViewHolder menuHolder = new MenuViewHolder(menuBinding);

            menuBinding.editIcon.setOnClickListener(v -> recipeInterface.onItemEdit(recipeUiList.get(menuHolder.getAdapterPosition())));
            menuBinding.shareIcon.setOnClickListener(v -> {
                recipeInterface.onItemShare(recipeUiList.get((menuHolder.getAdapterPosition())));
                Toast.makeText(menuBinding.getRoot().getContext(), "Share icon clicked", Toast.LENGTH_SHORT).show();
            });
            menuBinding.deleteIcon.setOnClickListener(v -> recipeInterface.onItemDelete(recipeUiList.get(menuHolder.getAdapterPosition())));

            return menuHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecipeUi item = recipeUiList.get(position);
        if (holder instanceof RowViewHolder) {
            RowViewHolder rowViewHolder = (RowViewHolder) holder;
            rowViewHolder.bind(item);
        }
    }


    @Override
    public int getItemCount() {
        return recipeUiList.size();
    }

    public RecipeUi editRecipe(int position, RecipeUi editedRecipeUi) {
        if (position >= 0 && position < recipeUiList.size()) {
            RecipeUi oldRecipeUi = getItemAtPosition(position);
            oldRecipeUi.setRecipeName(editedRecipeUi.getRecipeName());
            //oldRecipeUi.setImageBitmap(editedRecipeUi.getImageBitmap());
            //Log.d(TAG, "editRecipe: editedRecipe" + editedRecipeUi.getImageBitmap());
            //Log.d(TAG, "editRecipe: oldRecipe" + oldRecipeUi.getImageBitmap());

            oldRecipeUi.setInstructions(editedRecipeUi.getInstructions());
            oldRecipeUi.setIngredients(editedRecipeUi.getIngredients());
            editedRecipeUi.setSwiped(false);
            int updatedRows = recipeInterface.updateRecipe(oldRecipeUi);
            if (updatedRows > 0) {
                notifyItemChanged(position);
            }
        }

        return editedRecipeUi;
    }

    public RecipeUi getItemAtPosition(int position) {
        if (position >= 0 && position < recipeUiList.size()) {
            return recipeUiList.get(position);
        }
        return null;
    }

    // Remove a recipe from the list and notify the adapter
    public void removeRecipe(RecipeUi recipeUi) {
        // Find the position of the recipe in the list
        recipeUi.setSwiped(false);
        int pos = recipeUiList.indexOf(recipeUi);
        notifyItemChanged(pos);
        recipeUiList.remove(pos);
        notifyItemRemoved(pos);
        recipeInterface.deleteRecipe(recipeUi);
    }

    public void setRecyclerViewInterface(RecipeInterface recipeInterface) {
        this.recipeInterface = recipeInterface;
    }

    public void setRecipeList(List<RecipeUi> rList) {
        recipeUiList.clear();
        for (RecipeUi recipeUi : rList) {
            recipeUiList.add(recipeUi);
            notifyItemInserted(recipeUiList.size() - 1);
        }
    }

    public void itemChanged(int position) {
        notifyItemChanged(position);
    }

    public int getPositionForRecipe(RecipeUi recipe) {
        for (int i = 0; i < recipeUiList.size(); i++) {
            if (recipeUiList.get(i).getId() == recipe.getId()) {
                return i;
            }
        }
        return -1;
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

        }

        public void bind(RecipeUi recipeUi) {
            binding.textView.setText(recipeUi.getRecipeName());

            /*if (recipeUi.getImageBitmap() != null) {
                binding.defaultCardRecipeImage.setImageBitmap(recipeUi.getImageBitmap());
            } else {
                binding.defaultCardRecipeImage.setImageResource(R.drawable.recipe_image_default);
            }*/


            if (recipeInterface.isRecipeFavorite(recipeUi.getId())) {
                // Set the favorite icon if it is favorite
                binding.favIcon.setImageResource(R.drawable.baseline_favorite_24);
            } else {
                // Set the non-favorite icon if it is not favorite
                binding.favIcon.setImageResource(R.drawable.baseline_unfavorite_border_24);
            }

            if (recipeInterface.isRecipeSelected(recipeUi.getId())) {
                binding.addCart.setImageResource(R.drawable.baseline_remove_shopping_cart_24);

            } else {
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