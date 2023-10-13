package com.example.grocerieswizard.addRecipe;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerieswizard.R;
import com.example.grocerieswizard.databinding.ItemIngredientBinding;
import com.example.grocerieswizard.ui.model.IngredientUi;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    private static List<IngredientUi> ingredientList;
    private AddInterface addInterface;
    private static final String TAG = "IngredientAdapter";

    public IngredientAdapter(List<IngredientUi> ingredientList) {
        IngredientAdapter.ingredientList = ingredientList;
    }

    @NonNull
    @Override
    public IngredientAdapter.IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemIngredientBinding binding = ItemIngredientBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new IngredientViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.IngredientViewHolder holder, int position) {
        IngredientUi ingredient = ingredientList.get(position);
        ingredient.setInterface(addInterface);
        holder.bind(ingredient);
        Log.d(TAG, "onBindViewHolder: received ingredient: " + ingredient.getName());
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public static IngredientUi getItemAtPosition(int position) {
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


    public void addIngredient(IngredientUi ingredientUi) {
        ingredientList.add(ingredientUi);
        notifyItemInserted(ingredientList.size() - 1);
    }

    public void removeIngredient(IngredientUi ingredientUi, Context context) {
        // Find the position of the recipe in the list
        int pos = ingredientList.indexOf(ingredientUi);

        // Show a confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete " + ingredientUi.getName() + " recipe?");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            // User confirmed deletion, remove the recipe and update the RecyclerView
            ingredientList.remove(ingredientUi);
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
        private ItemIngredientBinding binding;

        public IngredientViewHolder(ItemIngredientBinding binding) {
            super(binding.getRoot());
            setBinding(binding);

            itemView.setOnLongClickListener(v -> {
                showPopUpMenu(getItemAtPosition(getAdapterPosition()));
                return false;
            });
        }

        private void setBinding(ItemIngredientBinding binding) {
            this.binding = binding;
        }

        private void showPopUpMenu(IngredientUi ingredientUi) {
            PopupMenu popupMenu = new PopupMenu(itemView.getContext(), binding.getRoot());
            popupMenu.inflate(R.menu.popup_menu_ingredient); // Create a menu resource file
            // Set click listeners for menu items
            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_edit) {// Implement the edit action here
                    if (ingredientUi.getInterface() != null) {
                        ingredientUi.getInterface().onItemEdit(ingredientUi);
                        popupMenu.dismiss();
                    }
                    return true;
                } else if (itemId == R.id.menu_delete) {// Implement the delete action here
                    if (ingredientUi.getInterface() != null) {
                        ingredientUi.getInterface().onItemDelete(ingredientUi);
                        popupMenu.dismiss();
                    }
                    return true;
                }
                return false;
            });

            popupMenu.show();
        }

        public void bind(IngredientUi ingredient) {
            binding.ingredientName.setText(ingredient.getName());
            binding.quantity.setText(String.valueOf(ingredient.getQuantity()));
            binding.unit.setText(ingredient.getUnit());
        }
    }
}
