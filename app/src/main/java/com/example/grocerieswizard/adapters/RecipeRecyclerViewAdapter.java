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
import com.example.grocerieswizard.models.RecipeModel;
import com.example.grocerieswizard.RecyclerViewInterface;

import java.util.ArrayList;

public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.RecipeViewHolder> {

    private ArrayList<RecipeModel> recipeList = new ArrayList<>();
    private RecyclerViewInterface recyclerViewInterface;
    private RecipeDatabaseHelper recipeDatabaseHelper;
    private Context context;


    public RecipeRecyclerViewAdapter(Context context) {
        this.context = context;
        recipeDatabaseHelper = new RecipeDatabaseHelper(context);
    }

    public void editRecipe(int position, RecipeModel editedRecipe) {
        if (position >= 0 && position < recipeList.size()) {
            RecipeModel oldRecipe = getItemAtPosition(position);
            oldRecipe.setRecipeName(editedRecipe.getRecipeName());
            oldRecipe.setRecipeImageUri(editedRecipe.getRecipeImageUri());
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
            holder.bindSwipedLayout(recipeModel);
        } else {
            View itemView = LayoutInflater.from(holder.itemView.getContext()).inflate(R.layout.recycler_view_row, (ViewGroup) holder.itemView, false);
            ((ViewGroup) holder.itemView).removeAllViews();
            ((ViewGroup) holder.itemView).addView(itemView);
            TextView title = itemView.findViewById(R.id.textView);
            ImageView resImage = itemView.findViewById(R.id.default_card_recipe_image);
            title.setText(recipeModel.getRecipeName());

           /* if (recipeModel.getRecipeImageUri() != null) {
                resImage.setImageURI(recipeModel.getRecipeImageUri());
            } else {
                resImage.setImageResource(R.drawable.recipe_image_default);
            }*/
            resImage.setImageResource(R.drawable.recipe_image_default);
        }


    }

    @Override
    public int getItemCount() {
        return recipeList.size();
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
        private ImageButton starButton;
        private ImageView resImage;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textView);
            starButton = itemView.findViewById(R.id.fav_icon);
            resImage = itemView.findViewById(R.id.default_card_recipe_image);


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
                        Log.d("recipe adapter", "before delete from selected: " + recipeDatabaseHelper.getSelectedRecipes().size());

                        recipeDatabaseHelper.deleteSelectedRecipe(recipeId);
                        Log.d("recipe adapter", "after delete from selected: " + recipeDatabaseHelper.getSelectedRecipes().size());
                        itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), android.R.color.transparent));
                    } else {
                        // Select
                        recipeModel.setSelected(true);
                        Log.d("recipe adapter", "before insert to selected: " + recipeDatabaseHelper.getSelectedRecipes().size());
                        recipeDatabaseHelper.insertSelectedRecipe(recipeId);
                        Log.d("recipe adapter", "after insert to selected: " + recipeDatabaseHelper.getSelectedRecipes().size());
                        itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.gray));
                    }
                }
                return true;
            });



            starButton.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    Toast.makeText(itemView.getContext(), "Star button clicked for " + title.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }


        public void bindSwipedLayout(RecipeModel recipeModel) {
            View swipedView = LayoutInflater.from(itemView.getContext()).inflate(R.layout.recycler_view_menu, null);

            ImageView editIcon = swipedView.findViewById(R.id.edit_icon);
            ImageView shareIcon = swipedView.findViewById(R.id.share_icon);
            ImageView deleteIcon = swipedView.findViewById(R.id.delete_icon);


            editIcon.setOnClickListener(v -> {
                recyclerViewInterface.onItemEdit(getAdapterPosition());

            });

            shareIcon.setOnClickListener(v -> {
                // TODO: Handle share icon click
                Toast.makeText(itemView.getContext(), "Share icon clicked", Toast.LENGTH_SHORT).show();
            });

            deleteIcon.setOnClickListener(v -> {
                recyclerViewInterface.onItemDelete(getAdapterPosition());
            });

            ((ViewGroup) itemView).removeAllViews();
            ((ViewGroup) itemView).addView(swipedView);

            title.setText(recipeModel.getRecipeName());
            if (recipeModel.getRecipeImageUri() != null) {
                //TODO:Image Handle :((
                //resImage.setImageURI(recipeModel.getRecipeImageUri());
            } else {
                resImage.setImageResource(R.drawable.recipe_image_default);
            }

        }


    }
}
