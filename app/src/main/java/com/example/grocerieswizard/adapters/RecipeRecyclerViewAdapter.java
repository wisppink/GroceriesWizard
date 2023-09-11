package com.example.grocerieswizard.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerieswizard.R;
import com.example.grocerieswizard.RecipeDatabaseHelper;
import com.example.grocerieswizard.RecyclerViewInterface;
import com.example.grocerieswizard.models.RecipeModel;

import java.util.ArrayList;

public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.RecipeViewHolder> {

    private ArrayList<RecipeModel> recipeList = new ArrayList<>();
    private RecyclerViewInterface recyclerViewInterface;
    private final RecipeDatabaseHelper recipeDatabaseHelper;
    private final Context context;
    private final String TAG = "RecipeAdapter";


    public RecipeRecyclerViewAdapter(Context context) {
        this.context = context;
        recipeDatabaseHelper = new RecipeDatabaseHelper(context);
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row, parent, false);
        return new RecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        RecipeModel recipeModel = recipeList.get(position);

        if (recipeModel.isSwiped()) {
            View swipedView = LayoutInflater.from(holder.itemView.getContext()).inflate(R.layout.recycler_view_menu, (ViewGroup) holder.itemView, false);
            ((ViewGroup) holder.itemView).removeAllViews();
            ((ViewGroup) holder.itemView).addView(swipedView);
            holder.bindSwipedLayout(recipeModel, swipedView);
        } else {
            View itemView = LayoutInflater.from(holder.itemView.getContext()).inflate(R.layout.recycler_view_row, (ViewGroup) holder.itemView, false);
            ((ViewGroup) holder.itemView).removeAllViews();
            ((ViewGroup) holder.itemView).addView(itemView);
            holder.bind(recipeModel);

        }
        if (recipeDatabaseHelper.isRecipeSelected(recipeModel.getId())) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.gray));
            recipeModel.setSelected(true);
        }


    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public void editRecipe(int position, RecipeModel editedRecipe) {
        if (position >= 0 && position < recipeList.size()) {
            RecipeModel oldRecipe = getItemAtPosition(position);
            oldRecipe.setRecipeName(editedRecipe.getRecipeName());
            oldRecipe.setImageBitmap(editedRecipe.getImageBitmap());
            oldRecipe.setInstructions(editedRecipe.getInstructions());
            oldRecipe.setIngredients(editedRecipe.getIngredients());
            editedRecipe.setSwiped(false);
            int updatedRows = recipeDatabaseHelper.updateRecipe(oldRecipe.getId(), oldRecipe);
            if (updatedRows > 0) {
                notifyItemChanged(position);
            } else
                Toast.makeText(context,
                        "couldn't edited:" + oldRecipe.getRecipeName(),
                        Toast.LENGTH_SHORT).show();
        }
    }


    // Add a new recipe to the list and notify the adapter
    public void addRecipe(RecipeModel recipe) {
        recipeList.add(recipe);
        notifyItemInserted(recipeList.size() - 1);
        recipeDatabaseHelper.insertRecipe(recipe);
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
        int pos = recipeList.indexOf(recipeModel);
        recipeList.remove(pos);
        notifyItemRemoved(pos);
        recipeDatabaseHelper.deleteRecipe(recipeModel.getId());
    }

    public void removeRecipeAtPosition(int position) {
        if (position >= 0 && position < recipeList.size()) {
            recipeDatabaseHelper.deleteRecipe(getItemAtPosition(position).getId());
            recipeList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void setRecyclerViewInterface(RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public void setRecipeList(ArrayList<RecipeModel> recipeList) {
        this.recipeList = recipeList;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ImageButton favButton;
        private ImageView resImage;

        public RecipeViewHolder(View itemView) {
            super(itemView);


            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    recyclerViewInterface.onItemClick(pos);
                }
            });

            itemView.setOnLongClickListener(v -> {
                RecipeModel recipeModel = recipeList.get(getAdapterPosition());
                if (recipeModel != null) {
                    int recipeId = recipeModel.getId();
                    if (recipeModel.isSelected()) {
                        // Already selected, unselect
                        recipeModel.setSelected(false);
                        recipeDatabaseHelper.deleteSelectedRecipe(recipeId);
                        itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), android.R.color.transparent));
                    } else {
                        // Select
                        recipeModel.setSelected(true);
                        recipeDatabaseHelper.insertSelectedRecipe(recipeId);
                        itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.gray));
                    }
                }
                return true;
            });

        }


        public void bindSwipedLayout(RecipeModel recipeModel, View swipedView) {

            ImageView editIcon = swipedView.findViewById(R.id.edit_icon);
            ImageView shareIcon = swipedView.findViewById(R.id.share_icon);
            ImageView deleteIcon = swipedView.findViewById(R.id.delete_icon);

            editIcon.setOnClickListener(v -> recyclerViewInterface.onItemEdit(getAdapterPosition()));

            shareIcon.setOnClickListener(v -> {
                // TODO: Handle share icon click
                Toast.makeText(itemView.getContext(), "Share icon clicked", Toast.LENGTH_SHORT).show();
            });

            deleteIcon.setOnClickListener(v -> recyclerViewInterface.onItemDelete(getAdapterPosition()));

            ((ViewGroup) itemView).removeAllViews();
            ((ViewGroup) itemView).addView(swipedView);

        }


        public void bind(RecipeModel recipeModel) {
            favButton = itemView.findViewById(R.id.fav_icon);
            resImage = itemView.findViewById(R.id.default_card_recipe_image);
            title = itemView.findViewById(R.id.textView);

            title.setText(recipeModel.getRecipeName());

            if (recipeModel.getImageBitmap() != null) {
                resImage.setImageBitmap(recipeModel.getImageBitmap());
            } else {
                resImage.setImageResource(R.drawable.recipe_image_default);
            }

            boolean isFavorite = recipeDatabaseHelper.isRecipeFavorite(recipeModel.getId());
            if (isFavorite) {
                // Set the favorite icon if it is favorite
                favButton.setImageResource(R.drawable.baseline_favorite_24);
            } else {
                // Set the non-favorite icon if it is not favorite
                favButton.setImageResource(R.drawable.baseline_unfavorite_border_24);
            }

            favButton.setOnClickListener(v -> {
                Log.d(TAG, "fav is working");
                int recipeId = recipeModel.getId();
                // Toggle the favorite state (add/remove from favorites)
                boolean isCurrentlyFavorite = recipeDatabaseHelper.isRecipeFavorite(recipeId);
                recipeModel.setFavorite(!isCurrentlyFavorite);

                // Update the UI and database based on the new favorite state
                if (!isCurrentlyFavorite) {
                    favButton.setImageResource(R.drawable.baseline_favorite_24);
                    Toast.makeText(itemView.getContext(), "Added to Favorites", Toast.LENGTH_SHORT).show();
                    recipeDatabaseHelper.insertRecipeFav(recipeId);
                } else {
                    favButton.setImageResource(R.drawable.baseline_unfavorite_border_24);
                    Toast.makeText(itemView.getContext(), "Removed from Favorites", Toast.LENGTH_SHORT).show();
                    recipeDatabaseHelper.deleteRecipeFav(recipeId);
                }
                Log.d(TAG, "Recipe " + recipeModel.getRecipeName() + " isFavorite: " + !isCurrentlyFavorite);
            });

        }
    }
}
