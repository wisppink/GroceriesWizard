package com.example.grocerieswizard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>  {

    private List<IngredientModel> ingredientList;
    private RecyclerViewInterface recyclerViewInterface;
    private Context context;

    public IngredientAdapter(Context context, List<IngredientModel> ingredientList) {
        this.context = context;
        this.ingredientList = ingredientList;
    }


    @NonNull
    @Override
    public IngredientAdapter.IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient,parent,false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.IngredientViewHolder holder, int position) {
        IngredientModel ingredient = ingredientList.get(position);
        holder.bindIngredient(ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }
    public IngredientModel getItemAtPosition(int position) {
        if (position >= 0 && position < ingredientList.size()) {
            return ingredientList.get(position);
        }
        return null;
    }
    public void addIngredient(IngredientModel ingredientModel) {
        ingredientList.add(ingredientModel);
        notifyItemInserted(ingredientList.size() - 1);
    }

    public void setRecyclerViewInterface(RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public void removeIngredient(IngredientModel ingredientModel) {
        // Find the position of the recipe in the list
        int pos = ingredientList.indexOf(ingredientModel);

        // Show a confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete " + ingredientModel.getName()+" recipe?");
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

    public void editIngredient(int position) {
        notifyItemChanged(position);

    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView ingredientName;
        TextView quantity;
        TextView unit;
        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.ingredientName);
            quantity = itemView.findViewById(R.id.quantity);
            unit = itemView.findViewById(R.id.unit);

            itemView.setOnLongClickListener(v -> {
                showPopUpMenu(v, getAdapterPosition());
                return false;
            });
        }

        private void showPopUpMenu(View v, int adapterPosition) {
            PopupMenu popupMenu = new PopupMenu(itemView.getContext(), v);
            popupMenu.inflate(R.menu.popup_menu_ingredient); // Create a menu resource file

            // Set click listeners for menu items
            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_edit) {// Implement the edit action here
                    if (recyclerViewInterface != null) {
                        recyclerViewInterface.onItemEdit(adapterPosition);
                        popupMenu.dismiss();
                    }
                    return true;
                } else if (itemId == R.id.menu_delete) {// Implement the delete action here
                    if (recyclerViewInterface != null) {
                        recyclerViewInterface.onItemDelete(adapterPosition);
                        popupMenu.dismiss();
                    }
                    return true;
                }
                return false;
            });

            popupMenu.show();
        }

        public void bindIngredient(IngredientModel ingredient) {
            ingredientName.setText(ingredient.getName());
            quantity.setText(String.valueOf(ingredient.getQuantity()));
            unit.setText(ingredient.getUnit());
        }
    }
}
