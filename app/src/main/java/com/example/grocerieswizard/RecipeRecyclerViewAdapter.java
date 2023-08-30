package com.example.grocerieswizard;

import android.content.Context;
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

import java.util.ArrayList;

public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.RecipeViewHolder> {

    private ArrayList<RecipeModel> recipeList = new ArrayList<>();
    private RecyclerViewInterface recyclerViewInterface;
    private Context context;

    public ArrayList<RecipeModel> sendRecipes;

    public RecipeRecyclerViewAdapter(Context context) {
        this.context = context;
        this.sendRecipes = new ArrayList<>();
    }


    public ArrayList<RecipeModel> getSendRecipes() {
        return sendRecipes;
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
    }


    // Set the interface for handling RecyclerView interactions
    public void setRecyclerViewInterface(RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public void removeRecipeAtPosition(int position) {
        if (position >= 0 && position < recipeList.size()) {
            recipeList.remove(position);
            notifyItemRemoved(position);
        }
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
                    if (sendRecipes.contains(recipeModel)) {
                        //already selected, unselect
                        sendRecipes.remove(recipeModel);
                        itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), android.R.color.white));
                        //TODO:unselect yapınca işlemleri geri almıo lol
                    } else {
                        // select
                        sendRecipes.add(recipeModel);
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
