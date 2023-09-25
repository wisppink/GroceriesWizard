package com.example.grocerieswizard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerieswizard.R;
import com.example.grocerieswizard.databinding.ItemIngredientBinding;
import com.example.grocerieswizard.interfaces.AddInterface;
import com.example.grocerieswizard.models.IngredientModel;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    ItemIngredientBinding binding;
    private static List<IngredientModel> ingredientList;
    private AddInterface addInterface;

    public IngredientAdapter(List<IngredientModel> ingredientList) {
        IngredientAdapter.ingredientList = ingredientList;
    }


    @NonNull
    @Override
    public IngredientAdapter.IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemIngredientBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new IngredientViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.IngredientViewHolder holder, int position) {
        IngredientModel ingredient = ingredientList.get(position);
        ingredient.setInterface(addInterface);
        holder.bind(ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public static IngredientModel getItemAtPosition(int position) {
        if (position >= 0 && position < ingredientList.size()) {
            return ingredientList.get(position);
        }
        return null;
    }


    public void setRecyclerViewInterface(AddInterface addInterface) {
        this.addInterface = addInterface;
    }


    public void changeItem(int position) {
        notifyItemChanged(position);
    }


    public void addIngredient(IngredientModel ingredientModel) {
        ingredientList.add(ingredientModel);
        notifyItemInserted(ingredientList.size() - 1);
    }

    public void removeIngredient(IngredientModel ingredientModel, Context context) {
        // Find the position of the recipe in the list
        int pos = ingredientList.indexOf(ingredientModel);

        // Show a confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete " + ingredientModel.getName() + " recipe?");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            // User confirmed deletion, remove the recipe and update the RecyclerView
            ingredientList.remove(ingredientModel);
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


    public static class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView ingredientName;
        TextView quantity;
        TextView unit;

        public IngredientViewHolder(ItemIngredientBinding binding) {
            super(binding.getRoot());
            ingredientName = binding.ingredientName;
            quantity = binding.quantity;
            unit = binding.unit;

            itemView.setOnLongClickListener(v -> {
                showPopUpMenu(v, getItemAtPosition(getAdapterPosition()));
                return false;
            });
        }

        private void showPopUpMenu(View v, IngredientModel ingredientModel) {
            PopupMenu popupMenu = new PopupMenu(itemView.getContext(), v);
            popupMenu.inflate(R.menu.popup_menu_ingredient); // Create a menu resource file
            // Set click listeners for menu items
            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_edit) {// Implement the edit action here
                    if (ingredientModel.getInterface() != null) {
                        ingredientModel.getInterface().onItemEdit(ingredientModel);
                        popupMenu.dismiss();
                    }
                    return true;
                } else if (itemId == R.id.menu_delete) {// Implement the delete action here
                    if (ingredientModel.getInterface() != null) {
                        ingredientModel.getInterface().onItemDelete(ingredientModel);
                        popupMenu.dismiss();
                    }
                    return true;
                }
                return false;
            });

            popupMenu.show();
        }

        public void bind(IngredientModel ingredient) {
            ingredientName.setText(ingredient.getName());
            quantity.setText(String.valueOf(ingredient.getQuantity()));
            unit.setText(ingredient.getUnit());
        }
    }
}
