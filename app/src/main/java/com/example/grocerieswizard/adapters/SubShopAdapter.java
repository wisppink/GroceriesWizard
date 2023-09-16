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
import java.util.Collections;
import java.util.List;

public class SubShopAdapter extends RecyclerView.Adapter<SubShopAdapter.ViewHolder> {


    private final ArrayList<SubShoppingItem> shoppingItems;
    private final List<Boolean> checkBoxes;


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
    public void checkAllSubItems(boolean isChecked) {
        Collections.fill(checkBoxes, isChecked);
        notifyDataSetChanged();
    }


    public void setShoppingItems(ShoppingItem shoppingItem, boolean isChecked) {
        this.shoppingItems.addAll(shoppingItem.getSubShoppingItems().keySet());
        checkBoxes.clear();

        for (int i = 0; i < shoppingItems.size(); i++) {
            checkBoxes.add(isChecked);
        }

        notifyDataSetChanged();
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

            ingredientCB.setOnCheckedChangeListener(null);

            int position = getAdapterPosition();
            ingredientCB.setChecked(checkBoxes.get(position));
            ingredientCB.setOnCheckedChangeListener((buttonView, isChecked) -> checkBoxes.set(position, isChecked));
        }
    }

}
