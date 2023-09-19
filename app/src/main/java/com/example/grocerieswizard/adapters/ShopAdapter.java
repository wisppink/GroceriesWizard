package com.example.grocerieswizard.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerieswizard.R;
import com.example.grocerieswizard.interfaces.ShopInterface;
import com.example.grocerieswizard.models.RecipeModel;
import com.example.grocerieswizard.models.ShoppingItem;
import com.example.grocerieswizard.models.SubShoppingItem;

import java.util.ArrayList;
import java.util.Map;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShoppingViewHolder> {
    private static final String TAG = "ShopAdapter";
    private final Context context;
    private final ArrayList<ShoppingItem> shoppingItems = new ArrayList<>();
    private ShopInterface shopInterface;


    public ShopAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ShoppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_item_row, parent, false);
        return new ShoppingViewHolder(itemView);
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
            shoppingItems.add(shoppingItem);
            notifyItemInserted(shoppingItems.size()-1);
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


    public class ShoppingViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox checkBox;
        private final TextView ingredient_name;
        private final RecyclerView subRecipe;
        private final TextView totalTV;

        public ShoppingViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredient_name = itemView.findViewById(R.id.shopping_cart_ingredient_name);
            subRecipe = itemView.findViewById(R.id.sub_recipe_recycler);
            totalTV = itemView.findViewById(R.id.total);
            checkBox = itemView.findViewById(R.id.is_finished);
        }

        public void bind(ShoppingItem shoppingItem) {
            Map<SubShoppingItem, Boolean> subShoppingItemBooleanMap = shoppingItem.getSubShoppingItems();
            Log.d(TAG, "bind: every shop Item has sub item Map: " + subShoppingItemBooleanMap);
            ingredient_name.setText(shoppingItem.getIngredientName());

            SubShopAdapter subShopAdapter = new SubShopAdapter(ShopAdapter.this);
            subRecipe.setAdapter(subShopAdapter);
            subShopAdapter.setShoppingItems(shoppingItem);
            subRecipe.setLayoutManager(new LinearLayoutManager(context));

            boolean allValuesTrue = true;
            for (Boolean value : subShoppingItemBooleanMap.values()) {
                if (!value) {
                    allValuesTrue = false;
                    break;
                }
            }
            checkBox.setChecked(allValuesTrue);
            checkBox.setOnClickListener(v -> {
                Log.d(TAG, "CheckBox clicked");
                subShopAdapter.checkAllSubItems(checkBox.isChecked());

                for (Map.Entry<SubShoppingItem, Boolean> entry : subShoppingItemBooleanMap.entrySet()) {
                    entry.setValue(checkBox.isChecked());
                }
                totalTV.setText(shopInterface.generateTotal(subShoppingItemBooleanMap));
            });

            SubShoppingItem subShoppingItem = subShopAdapter.getSubItem();

            if (subShoppingItem != null) {
                subShoppingItemBooleanMap.put(subShoppingItem, subShoppingItem.getChecked());
                subShopAdapter.checkAllSubItems(checkBox.isChecked());
            }
            totalTV.setText(shopInterface.generateTotal(subShoppingItemBooleanMap));
        }


    }


}
