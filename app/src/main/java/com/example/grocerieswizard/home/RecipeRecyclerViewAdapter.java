package com.example.grocerieswizard.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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

            return rowHolder;

        } else {
            RecyclerViewMenuBinding menuBinding = RecyclerViewMenuBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            RecyclerView.ViewHolder menuHolder = new MenuViewHolder(menuBinding, recipeInterface);

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
        RecipeUi item = recipeUiList.get(position);

        if (holder instanceof RowViewHolder) {
            RowViewHolder rowViewHolder = (RowViewHolder) holder;
            rowViewHolder.bind(item);
        } else if (holder instanceof MenuViewHolder) {
            MenuViewHolder menuViewHolder = (MenuViewHolder) holder;
            menuViewHolder.bind(item);
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
            oldRecipeUi.setImageBitmap(editedRecipeUi.getImageBitmap());
            String TAG = "RecipeAdapter";
            Log.d(TAG, "editRecipe: editedRecipe" + editedRecipeUi.getImageBitmap());
            Log.d(TAG, "editRecipe: oldRecipe" + oldRecipeUi.getImageBitmap());

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


    // Add a new recipe to the list and notify the adapter
    public void addRecipe(RecipeUi recipeUi) {
        recipeUiList.add(recipeUi);
        notifyItemInserted(recipeUiList.size() - 1);
        recipeInterface.insertRecipe(recipeUi);
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
        recipeInterface.deleteRecipe(recipeUi.getId());
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

    public static class RowViewHolder extends RecyclerView.ViewHolder {
        RecipeInterface recipeInterface;
        RecyclerViewRowBinding binding;
        private static final String TAG = "RowViewHolder";

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

            binding.getRoot().setOnLongClickListener(v -> {
                boolean x = recipeInterface.onLongClick(getAdapterPosition());
                if (x) {
                    //selected
                    binding.getRoot().setBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.gray));
                } else
                    //unselected
                    binding.getRoot().setBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), android.R.color.transparent));
                return true;
            });
        }

        public void bind(RecipeUi recipeUi) {
            binding.textView.setText(recipeUi.getRecipeName());

            if (recipeUi.getImageBitmap() != null) {
                binding.defaultCardRecipeImage.setImageBitmap(recipeUi.getImageBitmap());
            } else {
                binding.defaultCardRecipeImage.setImageResource(R.drawable.recipe_image_default);
            }


            if (recipeInterface.isRecipeFavorite(recipeUi.getId())) {
                // Set the favorite icon if it is favorite
                binding.favIcon.setImageResource(R.drawable.baseline_favorite_24);
            } else {
                // Set the non-favorite icon if it is not favorite
                binding.favIcon.setImageResource(R.drawable.baseline_unfavorite_border_24);
            }

            if (recipeInterface.isRecipeSelected(recipeUi.getId())) {
                Log.d(TAG, "bind: recipe already selected make it gray " + recipeUi.getId());
                binding.getRoot().setBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.gray));

            } else {
                Log.d(TAG, "bind: unselected make it transparent " + recipeUi.getId());
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), android.R.color.transparent));
            }
        }
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        private final RecipeInterface recipeInterface;
        private RecyclerViewMenuBinding binding;
        private static final String TAG = "MenuViewHolder";

        public MenuViewHolder(RecyclerViewMenuBinding binding, RecipeInterface recipeInterface) {
            super(binding.getRoot());
            setBinding(binding);
            this.recipeInterface = recipeInterface;
        }

        public void bind(RecipeUi recipeUi) {
            if (recipeInterface.isRecipeSelected(recipeUi.getId())) {
                Log.d(TAG, "bind: swiped, selected " + recipeUi.getId());
                binding.getRoot().setBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.gray));
            } else {
                Log.d(TAG, "bind: swiped, unselected " + recipeUi.getId());
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), android.R.color.transparent));
            }
        }

        public void setBinding(RecyclerViewMenuBinding binding) {
            this.binding = binding;
        }
    }
}