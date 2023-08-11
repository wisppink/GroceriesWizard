package com.example.grocerieswizard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private String title;
    private String ingredients;
    private Uri selectedImage;

    private String howtoprepare;

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null && data.hasExtra("recipe")) {
                    RecipeModel recipe = data.getParcelableExtra("recipe");
                    if (recipe != null) {
                        title = recipe.getRecipeName();
                        List<IngredientModel> ingredientList = recipe.getIngredients(); // Get the list of ingredients
                        howtoprepare = recipe.getHowToPrepare();
                        selectedImage = recipe.getRecipeImageUri();

                        // Update UI with recipe information
                        TextView titleTextView = findViewById(R.id.show_title);
                        titleTextView.setText(title);

                        TextView ingredientsTextView = findViewById(R.id.show_ingredients);

                        // Format the list of ingredients as a comma-separated string
                        StringBuilder ingredientsBuilder = new StringBuilder();
                        for (IngredientModel ingredient : ingredientList) {
                            String ingredientLine = ingredient.getName() + " " + ingredient.getQuantity() + " " + ingredient.getUnit();
                            ingredientsBuilder.append(ingredientLine).append(", ");
                        }
                        // Remove the trailing comma and space
                        if (ingredientsBuilder.length() > 0) {
                            ingredientsBuilder.setLength(ingredientsBuilder.length() - 2);
                        }
                        String formattedIngredients = ingredientsBuilder.toString();

                        ingredientsTextView.setText(formattedIngredients);

                        ImageView imageRecipe = findViewById(R.id.showRecipeImage);
                        imageRecipe.setImageURI(selectedImage);
                    }
                }
            }
    );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        Intent intent = getIntent();

        RecipeModel recipe = intent.getParcelableExtra("recipe");
        if (recipe != null) {
            title = recipe.getRecipeName();
            List<IngredientModel> ingredientList = recipe.getIngredients(); // Get the list of ingredients
            howtoprepare = recipe.getHowToPrepare();
            selectedImage = recipe.getRecipeImageUri();

            TextView titleTextView = findViewById(R.id.show_title);
            titleTextView.setText(title);

            TextView ingredientsTextView = findViewById(R.id.show_ingredients);

            // Format the list of ingredients as a comma-separated string
            StringBuilder ingredientsBuilder = new StringBuilder();
            for (IngredientModel ingredient : ingredientList) {
                String ingredientLine = ingredient.getName() + " " + ingredient.getQuantity() + " " + ingredient.getUnit();
                ingredientsBuilder.append(ingredientLine).append(", ");
            }
            // Remove the trailing comma and space
            if (ingredientsBuilder.length() > 0) {
                ingredientsBuilder.setLength(ingredientsBuilder.length() - 2);
            }
            String formattedIngredients = ingredientsBuilder.toString();

            ingredientsTextView.setText(formattedIngredients);

            TextView prepareTextView = findViewById(R.id.show_how_to_prepare);
            prepareTextView.setText(howtoprepare);

            ImageView imageRecipe = findViewById(R.id.showRecipeImage);
            imageRecipe.setImageURI(selectedImage);
        }
    }

}

