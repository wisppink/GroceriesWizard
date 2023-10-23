package com.example.grocerieswizard.ui.shop.subshop;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerieswizard.databinding.SubRecipeBinding;
import com.example.grocerieswizard.ui.shop.ShoppingItem;

import java.util.ArrayList;

public class SubShopAdapter extends RecyclerView.Adapter<SubShopAdapter.SubShopViewHolder> {
    private final ArrayList<SubShoppingItem> shoppingItems;
    private static final String TAG = "SubShopAdapter";

    public SubShopAdapter() {
        shoppingItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public SubShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SubRecipeBinding binding = SubRecipeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SubShopViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SubShopViewHolder holder, int position) {
        SubShoppingItem subShoppingItem = shoppingItems.get(position);
        holder.bind(subShoppingItem);
        Log.d(TAG, "onBindViewHolder: subShoppingItem " + subShoppingItem.getRecipeName() + " " + subShoppingItem.getIngredientUnit());
    }

    @Override
    public int getItemCount() {
        return shoppingItems.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setShoppingItems(ShoppingItem shoppingItem) {
        shoppingItems.addAll(shoppingItem.getSubShoppingItems());
        notifyDataSetChanged();
    }

    public static class SubShopViewHolder extends RecyclerView.ViewHolder {
        private final SubRecipeBinding binding;

        public SubShopViewHolder(SubRecipeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(SubShoppingItem subShoppingItem) {
            binding.subRecipeName.setText(subShoppingItem.getRecipeName());
            binding.subIngredientUnit.setText(subShoppingItem.getIngredientUnit());
            binding.subIngredientQuantity.setText(String.format(String.valueOf(subShoppingItem.getIngredientQuantity())));
        }
    }

}
