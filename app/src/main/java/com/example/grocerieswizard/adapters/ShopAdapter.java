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
import java.util.Map;

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
            Map<SubShoppingItem, Boolean> subShoppingItemBooleanMap = shoppingItem.getSubShoppingItems();
            Log.d(TAG, "bind: every shop Item has sub item Map: " + subShoppingItemBooleanMap);
            binding.shoppingCartIngredientName.setText(shoppingItem.getIngredientName());

            SubShopAdapter subShopAdapter = new SubShopAdapter(shoppingItem.getAdapter());
            binding.subRecipeRecycler.setAdapter(subShopAdapter);
            subShopAdapter.setShoppingItems(shoppingItem);
            binding.subRecipeRecycler.setLayoutManager(new LinearLayoutManager(context));

            boolean allValuesTrue = true;
            for (Boolean value : subShoppingItemBooleanMap.values()) {
                if (!value) {
                    allValuesTrue = false;
                    break;
                }
            }
            binding.isFinished.setChecked(allValuesTrue);
            binding.isFinished.setOnClickListener(v -> {
                Log.d(TAG, "CheckBox clicked");
                subShopAdapter.checkAllSubItems(binding.isFinished.isChecked());

                for (Map.Entry<SubShoppingItem, Boolean> entry : subShoppingItemBooleanMap.entrySet()) {
                    entry.setValue(binding.isFinished.isChecked());
                }
                binding.total.setText(shoppingItem.getTotal(subShoppingItemBooleanMap));
            });

            SubShoppingItem subShoppingItem = subShopAdapter.getSubItem();

            if (subShoppingItem != null) {
                subShoppingItemBooleanMap.put(subShoppingItem, subShoppingItem.getChecked());
                subShopAdapter.checkAllSubItems(binding.isFinished.isChecked());
            }
            binding.total.setText(shoppingItem.getTotal(subShoppingItemBooleanMap));
        }

        public void setBinding(ShoppingItemRowBinding binding) {
            this.binding = binding;
        }
    }


}
