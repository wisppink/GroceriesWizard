package com.example.grocerieswizard.ui.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerieswizard.R;
import com.example.grocerieswizard.databinding.ShoppingItemRowBinding;
import com.example.grocerieswizard.ui.model.RecipeUi;
import com.example.grocerieswizard.ui.shop.subshop.SubShopAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShoppingViewHolder> {
    private final ArrayList<ShoppingItem> shoppingItems = new ArrayList<>();
    private final ShopHelper shopHelper;
    SubShopAdapter subShopAdapter = new SubShopAdapter();
    Context context;

    public ShopAdapter(Context context, ShopHelper shopHelper) {
        this.context = context;
        this.shopHelper = shopHelper;
    }

    @NonNull
    @Override
    public ShoppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ShoppingItemRowBinding binding = ShoppingItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        ShoppingViewHolder holder = new ShoppingViewHolder(binding);
        binding.subRecipeRecycler.setAdapter(subShopAdapter);
        binding.subRecipeRecycler.setLayoutManager(new LinearLayoutManager(context));

        binding.isFinished.setOnClickListener(v ->
        {
            ShoppingItem shoppingItem = shoppingItems.get(holder.getAdapterPosition());
            if (binding.isFinished.isChecked()) {
                binding.total.setText(R.string.done);
            } else {
                binding.total.setText(shopHelper.generateTotal(shoppingItem.getSubShoppingItems()));
            }

        });
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

    public void setSelectedRecipeList(List<RecipeUi> selectedRecipeListUi) {
        ArrayList<ShoppingItem> tempList = shopHelper.generateShoppingItems(selectedRecipeListUi);
        for (ShoppingItem shoppingItem : tempList) {
            shoppingItems.add(shoppingItem);
            notifyItemInserted(shoppingItems.size() - 1);
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
            binding.shoppingCartIngredientName.setText(shoppingItem.getIngredientName());
            binding.total.setText(total);

            SubShopAdapter subShopAdapter = new SubShopAdapter();
            binding.subRecipeRecycler.setAdapter(subShopAdapter);
            binding.subRecipeRecycler.setLayoutManager(new LinearLayoutManager(context));
            subShopAdapter.setShoppingItems(shoppingItem);
        }

        public void setBinding(ShoppingItemRowBinding binding) {
            this.binding = binding;
        }
    }
}