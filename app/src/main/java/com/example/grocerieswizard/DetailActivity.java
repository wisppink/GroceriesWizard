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

public class DetailActivity extends AppCompatActivity {
    private String title;
    private String ingredients;
    private Uri selectedImage;

    private String howtoprepare;

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                RecipeModel recipe = null;
                if (data != null && data.hasExtra("recipe")) {
                    recipe = data.getParcelableExtra("recipe");
                }
                if (recipe != null) {
                    title = recipe.getRecipeName();
                    ingredients = recipe.getIngredients();
                    howtoprepare = recipe.getHowToPrepare();
                    selectedImage = recipe.getRecipeImageUri();

                    // Update UI with recipe information
                    TextView titleTextView = findViewById(R.id.show_title);
                    titleTextView.setText(title);

                    TextView ingredientsTextView = findViewById(R.id.show_ingredients);
                    ingredientsTextView.setText(ingredients);

                    ImageView imageRecipe = findViewById(R.id.showRecipeImage);
                    imageRecipe.setImageURI(selectedImage);
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
                ingredients = recipe.getIngredients();
                howtoprepare = recipe.getHowToPrepare();
                selectedImage = recipe.getRecipeImageUri();


                TextView titleTextView = findViewById(R.id.show_title);
                titleTextView.setText(title);

                TextView ingredientsTextView = findViewById(R.id.show_ingredients);
                ingredientsTextView.setText(ingredients);

                TextView prepareTextView = findViewById(R.id.show_how_to_prepare);
                prepareTextView.setText(howtoprepare);

                //ImageView imageRecipe = findViewById(R.id.showRecipeImage);
                //imageRecipe.setImageURI(selectedImage);
            }

    }
}

