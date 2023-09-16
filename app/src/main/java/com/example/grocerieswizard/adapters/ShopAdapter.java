package com.example.grocerieswizard.adapters;

import android.content.Context;
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

import java.util.ArrayList;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShoppingViewHolder> {
    private final Context context;
    private ArrayList<ShoppingItem> shoppingItems = new ArrayList<>();
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
        shoppingItems = shopInterface.generateShoppingItems(selectedRecipeList);
    }

    public void setShopInterface(ShopInterface shopInterface) {
        this.shopInterface = shopInterface;
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
            ingredient_name.setText(shoppingItem.getIngredientName());

            SubShopAdapter subRecipeAdapter = new SubShopAdapter();
            subRecipe.setAdapter(subRecipeAdapter);
            subRecipeAdapter.setShoppingItems(shoppingItem);
            subRecipe.setLayoutManager(new LinearLayoutManager(context));
            subRecipeAdapter.setOnSubItemCheckListener(checkBox::setChecked);
            totalTV.setText(R.string.not_available);
        }

    }

}
