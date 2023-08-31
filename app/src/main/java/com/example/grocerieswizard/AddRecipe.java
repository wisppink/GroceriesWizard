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

public class AddRecipe extends AppCompatActivity implements RecyclerViewInterface {

    private Uri selectedImageUri;
    private EditText editRecipeName;
    private EditText editRecipeHowToPrepare;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private IngredientAdapter ingredientAdapter;
    private List<IngredientModel> ingredientList = new ArrayList<>();
    private RecipeModel recipe;
    private boolean editMode = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipe);

        editRecipeName = findViewById(R.id.edit_recipe_name);
        editRecipeHowToPrepare = findViewById(R.id.edit_recipe_how_to_prepare);

        Button btnSaveRecipe = findViewById(R.id.save_recipe_button);
        ImageButton dischargeRecipe = findViewById(R.id.discharge_recipe);

        ingredientAdapter = new IngredientAdapter(this, ingredientList);
        ingredientAdapter.setRecyclerViewInterface(this);
        RecyclerView ingredientsRecyclerView = findViewById(R.id.recyclerView_ingredients);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ingredientsRecyclerView.setAdapter(ingredientAdapter);

        RecipeRecyclerViewAdapter recipeRecyclerViewAdapter = new RecipeRecyclerViewAdapter(this);

        Button addIngredientButton = findViewById(R.id.addIngredientButton);
        addIngredientButton.setOnClickListener(v -> {
            IngredientModel newIngredient = new IngredientModel(null, 0, null);
            showAddIngredientDialog(newIngredient);
        });


        ImageView addImage = findViewById(R.id.add_image);
        Uri defaultImageUri = Uri.parse("android.resource://com.example.grocerieswizard/" + R.drawable.recipe_image_default);

        //create an empty recipe
        recipe = new RecipeModel(null, null, null, selectedImageUri);

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

        addImage.setOnClickListener(v -> {
            Intent pickImageIntent = new Intent(Intent.ACTION_PICK);
            pickImageIntent.setType("image/*");
            pickImageLauncher.launch(pickImageIntent);
        });

        Intent intent = getIntent();
        editMode = intent.getBooleanExtra("editRecipe", false);
        int position = intent.getIntExtra("position", -1);

        if (editMode) {
            RecipeModel recipeModel = intent.getParcelableExtra("recipeModel");
            //get old ones to show user
            editRecipeName.setText(recipeModel.getRecipeName());
            editRecipeHowToPrepare.setText(recipeModel.getHowToPrepare());
            ingredientList.addAll(recipeModel.getIngredients());
            selectedImageUri = recipeModel.getRecipeImageUri();
            //get new ones to update
            String recipeName = editRecipeName.getText().toString();
            String howToPrepare = editRecipeHowToPrepare.getText().toString();
            Uri newPhoto = selectedImageUri;

            RecipeModel editedRecipe = new RecipeModel(recipeName,ingredientList,howToPrepare,newPhoto);
            recipeRecyclerViewAdapter.editRecipe(position, editedRecipe);
            ingredientAdapter.notifyItemChanged(position);
        }

        btnSaveRecipe.setOnClickListener(v -> {
            String recipeName = editRecipeName.getText().toString();
            String howToPrepare = editRecipeHowToPrepare.getText().toString();
            List<IngredientModel> mylist = ingredientList;

            if (recipeName.isEmpty() || ingredientList.isEmpty() || howToPrepare.isEmpty()) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedImageUri != null) {
                recipe.setRecipeName(recipeName);
                recipe.setHowToPrepare(howToPrepare);
                recipe.setRecipeImageUri(selectedImageUri);
                recipe.setIngredients(mylist);
            } else {
                recipe.setRecipeName(recipeName);
                recipe.setHowToPrepare(howToPrepare);
                recipe.setRecipeImageUri(defaultImageUri);
                recipe.setIngredients(mylist);
            }
            recipe.setSwiped(false);


            if (editMode) {
                Intent editintent = new Intent();
                editintent.putExtra("edited", true);
                editintent.putExtra("new_recipe", recipe);
                editintent.putExtra("position", position);
                setResult(RESULT_OK, editintent);
            }
            else{
                Intent resultIntent = new Intent();
                resultIntent.putExtra("recipe", recipe);
                setResult(RESULT_OK, resultIntent);
            }

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

        // Check if the activity is opened for editing


    }

    private void showAddIngredientDialog(IngredientModel ingredientModel) {
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

            ingredientModel.setName(name);
            ingredientModel.setUnit(unit);
            ingredientModel.setQuantity(quantity);
            ingredientAdapter.addIngredient(ingredientModel);

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

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemDelete(int position) {
        IngredientModel ingredientModel = ingredientAdapter.getItemAtPosition(position);
        if (ingredientModel != null) {
            ingredientAdapter.removeIngredient(ingredientModel);
        }

    }

    @Override
    public void onItemEdit(int position) {
        IngredientModel ingredientModel = ingredientAdapter.getItemAtPosition(position);
        if (ingredientModel != null) {
            showEditIngredientDialog(ingredientModel, position);

        }
    }

    public void showEditIngredientDialog(IngredientModel ingredientModel, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_ingredient, null);
        builder.setView(dialogView);

        EditText ingredientNameEditText = dialogView.findViewById(R.id.ingredientNameEditText);
        EditText ingredientQuantityEditText = dialogView.findViewById(R.id.ingredientQuantityEditText);
        EditText ingredientUnitEditText = dialogView.findViewById(R.id.ingredientUnitEditText);

        // Pre-fill the dialog's fields with the existing ingredient's information
        ingredientNameEditText.setText(ingredientModel.getName());
        ingredientQuantityEditText.setText(String.valueOf(ingredientModel.getQuantity()));
        ingredientUnitEditText.setText(ingredientModel.getUnit());

        builder.setPositiveButton("Save", (dialog, which) -> {
            // User clicked "Save," update the ingredient model with the new values
            String name = ingredientNameEditText.getText().toString();
            String quantityStr = ingredientQuantityEditText.getText().toString();
            double quantity = Double.parseDouble(quantityStr);
            String unit = ingredientUnitEditText.getText().toString();
            ingredientAdapter.editIngredient(name,unit,quantity,position);
            dialog.dismiss();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
