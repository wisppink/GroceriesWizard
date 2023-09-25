package com.example.grocerieswizard.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerieswizard.databinding.SubRecipeBinding;
import com.example.grocerieswizard.models.ShoppingItem;
import com.example.grocerieswizard.models.SubShoppingItem;

import java.util.ArrayList;
import java.util.Set;

public class SubShopAdapter extends RecyclerView.Adapter<SubShopAdapter.SubShopViewHolder> {

    SubRecipeBinding binding;
    private final ArrayList<SubShoppingItem> shoppingItems;
    private final ShopAdapter parentAdapter;

    private SubShoppingItem checkedSubItem;
    private static final String TAG = "SubShopAdapter";


    public SubShopAdapter(ShopAdapter parentAdapter) {
        this.parentAdapter = parentAdapter;
        shoppingItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public SubShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = SubRecipeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SubShopViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SubShopViewHolder holder, int position) {
        SubShoppingItem subShoppingItem = shoppingItems.get(position);
        holder.bind(subShoppingItem);
        setCheckedSubItem(subShoppingItem);

    }


    @Override
    public int getItemCount() {
        return shoppingItems.size();
    }

    public void checkAllSubItems(boolean isChecked) {
        for (int i = 0; i < shoppingItems.size(); i++) {
            SubShoppingItem subItem = shoppingItems.get(i);
            subItem.setChecked(isChecked);
            notifyItemChanged(i);
        }
    }


    public void setShoppingItems(ShoppingItem shoppingItem) {
        Set<SubShoppingItem> tempList = shoppingItem.getSubShoppingItems().keySet();
        for (SubShoppingItem subShoppingItem : tempList) {
            shoppingItems.add(subShoppingItem);
            subShoppingItem.setParentAdapter(parentAdapter);
            notifyItemInserted(shoppingItems.size() - 1);
        }
    }

    public SubShoppingItem getSubItem() {
        return checkedSubItem;
    }

    public void setCheckedSubItem(SubShoppingItem checkedSubItem) {
        this.checkedSubItem = checkedSubItem;
    }

    public static class SubShopViewHolder extends RecyclerView.ViewHolder {
        TextView recipeTitle;
        TextView ingredientQuantity;
        TextView ingredientUnit;
        CheckBox ingredientCB;

        public SubShopViewHolder(SubRecipeBinding binding) {
            super(binding.getRoot());
            recipeTitle = binding.subRecipeName;
            ingredientQuantity = binding.subIngredientQuantity;
            ingredientUnit = binding.subIngredientUnit;
            ingredientCB = binding.subIngredientCheckbox;

        }

        public void bind(SubShoppingItem subShoppingItem) {
            recipeTitle.setText(subShoppingItem.getRecipeName());
            ingredientUnit.setText(subShoppingItem.getIngredientUnit());
            ingredientQuantity.setText(String.format(String.valueOf(subShoppingItem.getIngredientQuantity())));
            ingredientCB.setOnCheckedChangeListener(null);
            ingredientCB.setChecked(subShoppingItem.getChecked());
            Log.d(TAG, "bind: setCheckedSubItem: " + subShoppingItem.getRecipeName());
            ingredientCB.setOnCheckedChangeListener((buttonView, isChecked) -> {
                subShoppingItem.setChecked(isChecked);
                subShoppingItem.getParentAdapter().notifySubItemStateChanged(subShoppingItem);

            });
        }


    }

}
