package com.example.grocerieswizard.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerieswizard.R;
import com.example.grocerieswizard.models.ShoppingItem;
import com.example.grocerieswizard.models.SubShoppingItem;

import java.util.ArrayList;
import java.util.List;

public class SubShopAdapter extends RecyclerView.Adapter<SubShopAdapter.ViewHolder> {


    private final ArrayList<SubShoppingItem> shoppingItems;
    private final List<Boolean> checkBoxes;
    private OnSubItemCheckListener onSubItemCheckListener;


    public SubShopAdapter() {
        shoppingItems = new ArrayList<>();
        checkBoxes = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_recipe, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SubShoppingItem subShoppingItem = shoppingItems.get(position);
        holder.bind(subShoppingItem);

    }

    @Override
    public int getItemCount() {
        return shoppingItems.size();
    }

    public void setShoppingItems(ShoppingItem shoppingItem) {
        this.shoppingItems.addAll(shoppingItem.getSubShoppingItems().keySet());
        for (SubShoppingItem ignored : shoppingItems) {
            checkBoxes.add(false);
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView recipeTitle;
        TextView ingredientQuantity;
        TextView ingredientUnit;
        CheckBox ingredientCB;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeTitle = itemView.findViewById(R.id.sub_recipe_name);
            ingredientQuantity = itemView.findViewById(R.id.sub_ingredient_quantity);
            ingredientUnit = itemView.findViewById(R.id.sub_ingredient_unit);
            ingredientCB = itemView.findViewById(R.id.sub_ingredient_checkbox);
        }

        public void bind(SubShoppingItem subShoppingItem) {
            recipeTitle.setText(subShoppingItem.getRecipeName());
            ingredientUnit.setText(subShoppingItem.getIngredientUnit());
            ingredientQuantity.setText(String.format(String.valueOf(subShoppingItem.getIngredientQuantity())));
            ingredientCB.setOnCheckedChangeListener(null); // Remove the listener
            int position = getAdapterPosition();
            ingredientCB.setChecked(checkBoxes.get(position));
            ingredientCB.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Update the checkbox state for the current item
                checkBoxes.set(position, isChecked);

                // Check if all checkboxes are checked
                boolean isAllChecked = true;
                for (boolean checked : checkBoxes) {
                    if (!checked) {
                        isAllChecked = false;
                        break;
                    }
                }
                // Notify the listener about checkbox changes
                if (onSubItemCheckListener != null) {
                    onSubItemCheckListener.onSubItemChecked(isAllChecked);
                }
            });
        }
    }

    // Set a listener for checkbox changes
    public void setOnSubItemCheckListener(OnSubItemCheckListener listener) {
        this.onSubItemCheckListener = listener;
    }

    // Interface for notifying checkbox changes
    public interface OnSubItemCheckListener {
        void onSubItemChecked(boolean isAllChecked);

    }
}
