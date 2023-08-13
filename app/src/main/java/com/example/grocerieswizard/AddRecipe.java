package com.example.grocerieswizard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class AddRecipe extends AppCompatActivity {

    private Uri selectedImageUri;
    private EditText editRecipeName;
    private EditText editRecipeHowToPrepare;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private IngredientAdapter ingredientAdapter;
    private List<IngredientModel> ingredientList = new ArrayList<>(); // Add this line
    private RecipeModel recipe; // Add this line


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipe);

        editRecipeName = findViewById(R.id.edit_recipe_name);
        editRecipeHowToPrepare = findViewById(R.id.edit_recipe_how_to_prepare);

        Button btnSaveRecipe = findViewById(R.id.save_recipe_button);
        ImageButton dischargeRecipe = findViewById(R.id.discharge_recipe);


        ingredientAdapter = new IngredientAdapter(ingredientList);
        RecyclerView ingredientsRecyclerView = findViewById(R.id.recyclerView_ingredients);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ingredientsRecyclerView.setAdapter(ingredientAdapter);

        Button addIngredientButton = findViewById(R.id.addIngredientButton);
        addIngredientButton.setOnClickListener(v -> showAddIngredientDialog());

        ImageView addImage = findViewById(R.id.add_image);
        Uri defaultImageUri = Uri.parse("android.resource://com.example.grocerieswizard/" + R.drawable.recipe_image_default);


        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        addImage.setImageURI(selectedImageUri);
                        //TODO: change the resolution
                    } else {
                        // User didn't choose an image, display the default image
                        selectedImageUri = defaultImageUri;
                        addImage.setImageURI(selectedImageUri);
                    }
                });
        addImage.setImageURI(defaultImageUri);
        Log.d("DefaultImageURI", defaultImageUri.toString());

        addImage.setOnClickListener(v -> {
            Intent pickImageIntent = new Intent(Intent.ACTION_PICK);
            pickImageIntent.setType("image/*");
            pickImageLauncher.launch(pickImageIntent);
        });

        btnSaveRecipe.setOnClickListener(v -> {
            String recipeName = editRecipeName.getText().toString();
            String howToPrepare = editRecipeHowToPrepare.getText().toString();
            List<IngredientModel> mylist = ingredientList;

            if (recipeName.isEmpty() || ingredientList.isEmpty() || howToPrepare.isEmpty()) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedImageUri != null) {
                recipe = new RecipeModel(recipeName, mylist, howToPrepare, selectedImageUri);
                Log.d("addrecipe",mylist.get(1).getName());
            } else {
                recipe = new RecipeModel(recipeName, mylist, howToPrepare, defaultImageUri);
            }
            Intent resultIntent = new Intent();
            resultIntent.putExtra("recipe", recipe);
            setResult(RESULT_OK, resultIntent);
            finish();
        });


        dischargeRecipe.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm Discharge")
                    .setMessage("Are you sure you want to discharge this recipe?")
                    .setPositiveButton("Yes", (dialog, which) -> finish())
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
        });

    }

    private void showAddIngredientDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_ingredient, null);
        builder.setView(dialogView);

        EditText ingredientNameEditText = dialogView.findViewById(R.id.ingredientNameEditText);
        EditText ingredientQuantityEditText = dialogView.findViewById(R.id.ingredientQuantityEditText);
        EditText ingredientUnitEditText = dialogView.findViewById(R.id.ingredientUnitEditText);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = ingredientNameEditText.getText().toString();
            String quantityStr = ingredientQuantityEditText.getText().toString();
            double quantity = Double.parseDouble(quantityStr);
            String unit = ingredientUnitEditText.getText().toString();

            IngredientModel newIngredient = new IngredientModel(name, quantity, unit);
            Log.d("AddRecipe","new ingredient added: " + name);
            ingredientList.add(newIngredient);
            ingredientAdapter.notifyDataSetChanged();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if (unsavedChangesExist()) {
            showUnsavedChangesDialog();
        } else {
            super.onBackPressed();
        }
    }

    private boolean unsavedChangesExist() {
        // Check if there are any unsaved changes (e.g., fields are not empty)
        String recipeName = editRecipeName.getText().toString();
        List<IngredientModel> ingredients = ingredientList;
        String howToPrepare = editRecipeHowToPrepare.getText().toString();

        return !recipeName.isEmpty() || !ingredients.isEmpty() || !howToPrepare.isEmpty();
    }

    private void showUnsavedChangesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Unsaved Changes")
                .setMessage("You have unsaved changes. Are you sure you want to discard them?")
                .setPositiveButton("Discard", (dialog, which) -> finish())
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

}
