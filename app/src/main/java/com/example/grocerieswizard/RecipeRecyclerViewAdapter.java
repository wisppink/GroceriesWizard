package com.example.grocerieswizard;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.RecipeViewHolder> {

    private ArrayList<RecipeModel> recipeList = new ArrayList<>();
    private RecyclerViewInterface recyclerViewInterface;
    private Context context;
    public RecipeRecyclerViewAdapter(Context context) {
        this.context = context;
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
        holder.bind(recipeModel);
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

        // Show a confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete " + recipeModel.getRecipeName()+" recipe?");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            // User confirmed deletion, remove the recipe and update the RecyclerView
            recipeList.remove(recipeModel);
            notifyItemRemoved(pos);
            dialog.dismiss();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            // User canceled deletion, dismiss the dialog
            dialog.dismiss();
        });

        // Create and show the dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    // Set the interface for handling RecyclerView interactions
    public void setRecyclerViewInterface(RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;}

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
                showPopUpMenu(v, getAdapterPosition());
                return false;
            });

            starButton.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    Toast.makeText(itemView.getContext(), "Star button clicked for " + title.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void showPopUpMenu(View v, int position) {
            PopupMenu popupMenu = new PopupMenu(itemView.getContext(), v, GravityCompat.END);
            popupMenu.inflate(R.menu.item_long_tap_popup_menu);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                popupMenu.setForceShowIcon(true);
            }

            popupMenu.setOnMenuItemClickListener(item -> {
                int itemPosition = getAdapterPosition();
                if (itemPosition != RecyclerView.NO_POSITION) {
                    if (item.getItemId() == R.id.menu_delete) {
                        recyclerViewInterface.onItemDelete(itemPosition);
                        popupMenu.dismiss();
                        return true;
                    } else if (item.getItemId() == R.id.menu_edit) {
                        recyclerViewInterface.onItemEdit(itemPosition);
                        Toast.makeText(itemView.getContext(), "edit clicked" + title, Toast.LENGTH_SHORT).show();
                        popupMenu.dismiss();
                        return true;
                    } else if (item.getItemId() == R.id.menu_unFav) {
                        // TODO: Unfavorite
                        Toast.makeText(itemView.getContext(), "Unfavorite clicked", Toast.LENGTH_SHORT).show();
                        popupMenu.dismiss();
                        return true;
                    }
                }
                return false;
            });
            popupMenu.show();
        }

        public void bind(RecipeModel recipeModel) {

            title.setText(recipeModel.getRecipeName());
            if (recipeModel.getRecipeImageUri() != null) {
                resImage.setImageURI(recipeModel.getRecipeImageUri());
            } else {
                resImage.setImageResource(R.drawable.recipe_image_default);
            }


        }
    }
}
