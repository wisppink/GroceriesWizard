package com.example.grocerieswizard.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.example.grocerieswizard.R;
import com.example.grocerieswizard.RecyclerViewInterface;
import com.example.grocerieswizard.adapters.IngredientAdapter;
import com.example.grocerieswizard.adapters.RecipeRecyclerViewAdapter;
import com.example.grocerieswizard.models.IngredientModel;
import com.example.grocerieswizard.models.RecipeModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class AddRecipeActivity extends AppCompatActivity implements RecyclerViewInterface {

    private Bitmap selectedImageBitmap;
    private EditText editRecipeName;
    private EditText editRecipeHowToPrepare;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private IngredientAdapter ingredientAdapter;
    private List<IngredientModel> ingredientList = new ArrayList<>();
    private RecipeModel recipe;
    private boolean editMode = false;
    private Bitmap imageToStore;
    private Bitmap defaultImageBitmap;
    private final String TAG = "AddRecipe";

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
        try {
            defaultImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), defaultImageUri);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        //create an empty recipe
        recipe = new RecipeModel(null, null, null, null);

        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        Log.d(TAG, "onCreate: selectedImageUri : " + selectedImageUri);
                        try {
                            Picasso.get()
                                    .load(selectedImageUri)
                                    .resize(150, 150)
                                    .centerCrop()
                                    .into(new Target() {
                                        @Override
                                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                            addImage.setImageBitmap(bitmap);
                                            recipe.setImageBitmap(bitmap);
                                        }

                                        @Override
                                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                            Toast.makeText(AddRecipeActivity.this, "image error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                                        }
                                    });

                        } catch (Exception e) {
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            imageToStore = defaultImageBitmap;
                            addImage.setImageBitmap(defaultImageBitmap);
                            recipe.setImageBitmap(defaultImageBitmap);
                        }
                    } else {
                        imageToStore = defaultImageBitmap;
                        addImage.setImageBitmap(defaultImageBitmap);
                        recipe.setImageBitmap(defaultImageBitmap);
                    }
                });
        addImage.setImageURI(defaultImageUri);

        addImage.setOnClickListener(v -> {
            Intent pickImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
            pickImageIntent.setType("image/*");
            pickImageLauncher.launch(pickImageIntent);
        });

        Intent intent = getIntent();
        editMode = intent.getBooleanExtra("editRecipe", false);
        int position = intent.getIntExtra("position", -1);

        // Check if the activity is opened for editing
        if (editMode) {
            RecipeModel recipeModel = intent.getParcelableExtra("recipeModel");
            //get old ones to show user
            assert recipeModel != null;
            editRecipeName.setText(recipeModel.getRecipeName());
            editRecipeHowToPrepare.setText(recipeModel.getInstructions());
            ingredientList.addAll(recipeModel.getIngredients());
            selectedImageBitmap = recipeModel.getImageBitmap();
            //get new ones to update
            String recipeName = editRecipeName.getText().toString();
            String howToPrepare = editRecipeHowToPrepare.getText().toString();
            Bitmap newPhoto = selectedImageBitmap;

            RecipeModel editedRecipe = new RecipeModel(recipeName, ingredientList, howToPrepare, newPhoto);
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
            if (recipe.getImageBitmap() == null)
                recipe.setImageBitmap(defaultImageBitmap);
            recipe.setRecipeName(recipeName);
            recipe.setInstructions(howToPrepare);
            recipe.setSwiped(false);
            recipe.setIngredients(mylist);


            if (editMode) {
                Intent editintent = new Intent();
                editintent.putExtra("edited", true);
                editintent.putExtra("new_recipe", recipe);
                editintent.putExtra("position", position);
                setResult(RESULT_OK, editintent);
            } else {
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
            ingredientAdapter.editIngredient(name, unit, quantity, position);
            dialog.dismiss();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
