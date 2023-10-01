package com.example.grocerieswizard.shop.subshop;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerieswizard.databinding.SubRecipeBinding;
import com.example.grocerieswizard.shop.ShopAdapter;
import com.example.grocerieswizard.shop.ShoppingItem;

import java.util.ArrayList;
import java.util.Set;

public class SubShopAdapter extends RecyclerView.Adapter<SubShopAdapter.SubShopViewHolder> {

    private final SubShopOnCheckedChangeListener listener;
    SubRecipeBinding binding;
    private final ArrayList<SubShoppingItem> shoppingItems;
    private SubShoppingItem checkedSubItem;
    private static final String TAG = "SubShopAdapter";


    public SubShopAdapter(SubShopOnCheckedChangeListener listener) {
        shoppingItems = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public SubShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = SubRecipeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        SubShopViewHolder holder = new SubShopViewHolder(binding);
        binding.subIngredientCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SubShoppingItem item = shoppingItems.get(holder.getAdapterPosition());
            item.setChecked(isChecked);
            listener.onCheckedChangeListener(item);
        });
        return holder;
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
        private final SubRecipeBinding binding;

        public SubShopViewHolder(SubRecipeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(SubShoppingItem subShoppingItem) {
            binding.subRecipeName.setText(subShoppingItem.getRecipeName());
            binding.subIngredientUnit.setText(subShoppingItem.getIngredientUnit());
            binding.subIngredientQuantity.setText(String.format(String.valueOf(subShoppingItem.getIngredientQuantity())));
            binding.subIngredientCheckbox.setOnCheckedChangeListener(null);
            binding.subIngredientCheckbox.setChecked(subShoppingItem.getChecked());
            Log.d(TAG, "bind: setCheckedSubItem: " + subShoppingItem.getRecipeName());
        }
    }

}
