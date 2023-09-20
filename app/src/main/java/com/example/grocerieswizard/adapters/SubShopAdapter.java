package com.example.grocerieswizard.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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

public class SubShopAdapter extends RecyclerView.Adapter<SubShopAdapter.ViewHolder> {

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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = SubRecipeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        View itemView = binding.getRoot();
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
            notifyItemInserted(shoppingItems.size() - 1);
        }
    }

    public SubShoppingItem getSubItem() {
        return checkedSubItem;
    }

    public void setCheckedSubItem(SubShoppingItem checkedSubItem) {
        this.checkedSubItem = checkedSubItem;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView recipeTitle;
        TextView ingredientQuantity;
        TextView ingredientUnit;
        CheckBox ingredientCB;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
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
            setCheckedSubItem(subShoppingItem);
            ingredientCB.setOnCheckedChangeListener((buttonView, isChecked) -> {
                subShoppingItem.setChecked(isChecked);
                parentAdapter.notifySubItemStateChanged(subShoppingItem);

            });
        }


    }

}
