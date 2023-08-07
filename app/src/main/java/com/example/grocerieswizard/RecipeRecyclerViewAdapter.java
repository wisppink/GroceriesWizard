package com.example.grocerieswizard;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.RecipeViewHolder> {

    private ArrayList<RecipeModel> recipeList = new ArrayList<>();
    private  RecyclerViewInterface recyclerViewInterface;

    @NonNull
    @Override
    public RecipeRecyclerViewAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecipeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row, parent, false), recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeRecyclerViewAdapter.RecipeViewHolder holder, int position) {
        RecipeModel recipeModel = recipeList.get(holder.getAdapterPosition());
        holder.bind(recipeModel);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public void addRecipe(RecipeModel recipe) {
        recipeList.add(recipe);
        notifyItemInserted(recipeList.size()-1);
    }

    public RecipeModel getItemAtPosition(int position) {
        if (position >= 0 && position < recipeList.size()) {
            return recipeList.get(position);
        }
        return null;
    }
    public void setRecyclerViewInterface(RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
    }


    public static class RecipeViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ImageButton starButton;
        public RecipeViewHolder(View itemView, RecyclerViewInterface  recyclerViewInterface) {
            super(itemView);
            title = itemView.findViewById(R.id.textView);
            starButton = itemView.findViewById(R.id.fav_icon);
            itemView.setOnClickListener(v -> {
                if (recyclerViewInterface != null) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        recyclerViewInterface.onItemClick(pos);
                    }

                }
            });

            itemView.setOnLongClickListener(v -> {
                Toast.makeText(itemView.getContext(),"Long clicked " + title.getText(), Toast.LENGTH_SHORT).show();
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
            popupMenu.inflate(R.menu.item_long_tap_popup_menu); // Inflate your menu resource here
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                popupMenu.setForceShowIcon(true);
            }

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_delete) {
                    deleteRecipe(position);
                    popupMenu.dismiss();
                    return true;
                } else if (item.getItemId() == R.id.menu_edit) {
                    editRecipe(position);
                    popupMenu.dismiss();
                    return true;
                } else if (item.getItemId() == R.id.menu_unFav) {
                    //TODO:UnFAV but first do the fav list
                    Toast.makeText(itemView.getContext(), "unfavorite clicked", Toast.LENGTH_SHORT).show();
                    popupMenu.dismiss();
                    return false; //for now

                }
                else
                return false;
            });
            popupMenu.show();
        }

        private void editRecipe(int position) {
            Toast.makeText(itemView.getContext(), "editRecipe clicked", Toast.LENGTH_SHORT).show();
        }

        private void deleteRecipe(int position) {
            // Delete the message at the given position from the list
            // Notify the adapter to update the list view
            // Perform any other necessary actions
            Toast.makeText(itemView.getContext(), "deleteRecipe clicked", Toast.LENGTH_SHORT).show();
        }

        public void bind(RecipeModel recipeModel) {
            title.setText(recipeModel.getRecipeName());
        }
    }
}
