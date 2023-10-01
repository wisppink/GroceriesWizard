package com.example.grocerieswizard.shop;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerieswizard.databinding.ShoppingItemRowBinding;
import com.example.grocerieswizard.models.RecipeModel;
import com.example.grocerieswizard.shop.subshop.SubShoppingItem;
import com.example.grocerieswizard.shop.subshop.SubShopAdapter;

import java.util.ArrayList;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShoppingViewHolder> {
    ShoppingItemRowBinding binding;
    private static final String TAG = "ShopAdapter";
    private final ArrayList<ShoppingItem> shoppingItems = new ArrayList<>();
    private ShopHelper shopHelper;
    SubShopAdapter subShopAdapter = new SubShopAdapter(this::notifySubItemStateChanged);
    Context context;

    public ShopAdapter(Context context, ShopHelper shopHelper) {
        this.context = context;
        this.shopHelper = shopHelper;
    }

    @NonNull
    @Override
    public ShoppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ShoppingItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        binding.subRecipeRecycler.setAdapter(subShopAdapter);
        binding.subRecipeRecycler.setLayoutManager(new LinearLayoutManager(context));
        ShoppingViewHolder holder = new ShoppingViewHolder(binding);
        ShoppingItem shoppingItem = shoppingItems.get(holder.getAdapterPosition());
        subShopAdapter.setShoppingItems(shoppingItem);
        binding.isFinished.setOnClickListener(v -> {
            subShopAdapter.checkAllSubItems(binding.isFinished.isChecked());
            shoppingItem.setEverySubValue(binding.isFinished.isChecked());
            binding.total.setText(shopHelper.generateTotal(shoppingItem.getSubShoppingItems()));
        });
        SubShoppingItem subShoppingItem = subShopAdapter.getSubItem();
        //control item check
        if (subShoppingItem != null) {
            shoppingItem.getSubShoppingItems().put(subShoppingItem, subShoppingItem.getChecked());
            subShopAdapter.checkAllSubItems(binding.isFinished.isChecked());
        }
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ShoppingViewHolder holder, int position) {
        ShoppingItem shoppingItem = shoppingItems.get(position);
        holder.bind(shoppingItem, shopHelper.generateTotal(shoppingItem.getSubShoppingItems()));
    }


    @Override
    public int getItemCount() {
        return shoppingItems.size();
    }

    public void setSelectedRecipeList(ArrayList<RecipeModel> selectedRecipeList) {
        ArrayList<ShoppingItem> tempList = shopHelper.generateShoppingItems(selectedRecipeList);
        for (ShoppingItem shoppingItem : tempList) {
            shoppingItems.add(shoppingItem);
            notifyItemInserted(shoppingItems.size() - 1);
        }
    }

    public void notifySubItemStateChanged(SubShoppingItem subShoppingItem) {
        for (int i = 0; i < shoppingItems.size(); i++) {
            ShoppingItem shoppingItem = shoppingItems.get(i);
            if (shoppingItem.getSubShoppingItems().containsKey(subShoppingItem)) {
                shoppingItem.getSubShoppingItems().put(subShoppingItem, subShoppingItem.getChecked());
                notifyItemChanged(i);
                break;
            }
        }
    }

    public static class ShoppingViewHolder extends RecyclerView.ViewHolder {

        private ShoppingItemRowBinding binding;
        Context context;

        public ShoppingViewHolder(ShoppingItemRowBinding binding) {
            super(binding.getRoot());
            setBinding(binding);
            context = binding.getRoot().getContext();
        }

        public void bind(ShoppingItem shoppingItem, String total) {
            //show ingredient name
            binding.shoppingCartIngredientName.setText(shoppingItem.getIngredientName());
            Log.d(TAG, "bind: name " + shoppingItem.getIngredientName());
            binding.isFinished.setChecked(shoppingItem.ControlAllValuesTrue());
            binding.total.setText(total);
        }

        public void setBinding(ShoppingItemRowBinding binding) {
            this.binding = binding;
        }
    }
}
