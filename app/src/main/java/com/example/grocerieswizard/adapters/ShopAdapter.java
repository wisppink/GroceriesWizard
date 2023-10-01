package com.example.grocerieswizard.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerieswizard.databinding.ShoppingItemRowBinding;
import com.example.grocerieswizard.interfaces.ShopInterface;
import com.example.grocerieswizard.models.RecipeModel;
import com.example.grocerieswizard.models.ShoppingItem;
import com.example.grocerieswizard.models.SubShoppingItem;

import java.util.ArrayList;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShoppingViewHolder> {
    ShoppingItemRowBinding binding;
    private static final String TAG = "ShopAdapter";
    private final ArrayList<ShoppingItem> shoppingItems = new ArrayList<>();
    private ShopInterface shopInterface;
    Context context;

    public ShopAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ShoppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ShoppingItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ShoppingViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull ShoppingViewHolder holder, int position) {
        ShoppingItem shoppingItem = shoppingItems.get(position);
        holder.bind(shoppingItem);
    }


    @Override
    public int getItemCount() {
        return shoppingItems.size();
    }

    public void setSelectedRecipeList(ArrayList<RecipeModel> selectedRecipeList) {
        ArrayList<ShoppingItem> tempList = shopInterface.generateShoppingItems(selectedRecipeList);
        for (ShoppingItem shoppingItem : tempList) {
            shoppingItem.setAdapter(this);
            shoppingItem.setShopInterface(shopInterface);
            shoppingItems.add(shoppingItem);
            notifyItemInserted(shoppingItems.size() - 1);
        }
    }

    public void setShopInterface(ShopInterface shopInterface) {
        this.shopInterface = shopInterface;
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

        public void bind(ShoppingItem shoppingItem) {
            //show ingredient name
            binding.shoppingCartIngredientName.setText(shoppingItem.getIngredientName());
            Log.d(TAG, "bind: name " + shoppingItem.getIngredientName());
            //subAdapter
            SubShopAdapter subShopAdapter = new SubShopAdapter(shoppingItem.getAdapter());
            binding.subRecipeRecycler.setAdapter(subShopAdapter);
            subShopAdapter.setShoppingItems(shoppingItem);
            binding.subRecipeRecycler.setLayoutManager(new LinearLayoutManager(context));

            binding.isFinished.setChecked(shoppingItem.ControlAllValuesTrue());

            binding.isFinished.setOnClickListener(v -> {
                subShopAdapter.checkAllSubItems(binding.isFinished.isChecked());
                shoppingItem.setEverySubValue(binding.isFinished.isChecked());
                binding.total.setText(shoppingItem.getTotal(shoppingItem.getSubShoppingItems()));
            });

            SubShoppingItem subShoppingItem = subShopAdapter.getSubItem();
            //control item check
            if (subShoppingItem != null) {
                shoppingItem.getSubShoppingItems().put(subShoppingItem, subShoppingItem.getChecked());
                subShopAdapter.checkAllSubItems(binding.isFinished.isChecked());
            }

            binding.total.setText(shoppingItem.getTotal(shoppingItem.getSubShoppingItems()));
        }

        public void setBinding(ShoppingItemRowBinding binding) {
            this.binding = binding;
        }
    }


}
