package com.example.grocerieswizard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ShoppingMenu extends AppCompatActivity implements RecyclerViewInterface {

    private ShoppingRecyclerViewAdapter adapter;
    Context context;
    ArrayList<RecipeModel> receivedList;
    Map<String, List<String>> recipeIngredientsMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_menu);

        RecyclerView shoppingRecyclerView = findViewById(R.id.shopping_cart);
        // Retrieve the list of selected recipes from the previous activity
        Intent intent = getIntent();
        receivedList = intent.getParcelableArrayListExtra("list");
        Log.d("ShoppingMenu", "list: " + receivedList);


        // Generate shopping list data from received recipes
        if (receivedList != null) {
            for (RecipeModel recipe : receivedList) {
                for (IngredientModel ingredient : recipe.getIngredients()) {
                    String key = ingredient.getName();
                    double ingredientQuantity = ingredient.getQuantity();
                    String ingredientUnit = ingredient.getUnit();
                    String recipeName = recipe.getRecipeName();

                    String value= recipeName + " " + ingredientQuantity + " " + ingredientUnit;


                    // Check if the key already exists in the map
                    if (recipeIngredientsMap.containsKey(key)) {
                        Objects.requireNonNull(recipeIngredientsMap.get(key)).add(value);
                        Log.d("ShoppingMenu","value already exist: " + value + "ingredient name: " + ingredient.getName());
                    } else {
                        // If the key is new, create a new list for the values
                        List<String> values = new ArrayList<>();
                        values.add(value);
                        Log.d("ShoppingMenu","value new: " + value);
                        recipeIngredientsMap.put(key, values);
                    }


                }

            }
        }

        adapter = new ShoppingRecyclerViewAdapter(this, recipeIngredientsMap);
        shoppingRecyclerView.setAdapter(adapter);
        shoppingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged(); // Move this line here
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemDelete(int position) {

    }

    @Override
    public void onItemEdit(int position) {

    }


}