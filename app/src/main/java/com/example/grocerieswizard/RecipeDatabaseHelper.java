package com.example.grocerieswizard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class RecipeDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recipes.db";
    private static final int DATABASE_VERSION = 1;

    // Table and column names for recipes
    private static final String TABLE_RECIPES = "recipes";
    private static final String COLUMN_RECIPE_ID = "_id";
    private static final String COLUMN_RECIPE_NAME = "recipe_name";
    private static final String COLUMN_RECIPE_INSTRUCTIONS = "recipe_instructions";
    private static final String COLUMN_RECIPE_IMAGE_URI = "image_uri";

    // Table and column names for ingredients
    private static final String TABLE_INGREDIENTS = "ingredients";
    private static final String COLUMN_INGREDIENT_ID = "_id";
    private static final String COLUMN_INGREDIENT_UNIT = "ingredient_unit";
    private static final String COLUMN_INGREDIENT_QUANTITY = "ingredient_quantity";
    private static final String COLUMN_INGREDIENT_NAME = "ingredient_name";
    private static final String COLUMN_RECIPE_ID_FK = "recipe_id";

    // Database creation SQL statements
    private static final String DATABASE_CREATE_RECIPES = "create table " + TABLE_RECIPES
            + "(" + COLUMN_RECIPE_ID + " integer primary key autoincrement, "
            + COLUMN_RECIPE_NAME + " text not null, "
            + COLUMN_RECIPE_INSTRUCTIONS + " text, "  // Added COLUMN_RECIPE_INSTRUCTIONS column
            + COLUMN_RECIPE_IMAGE_URI + " text);";

    private static final String DATABASE_CREATE_INGREDIENTS = "create table " + TABLE_INGREDIENTS
            + "(" + COLUMN_INGREDIENT_ID + " integer primary key autoincrement, "
            + COLUMN_INGREDIENT_NAME + " text not null, "
            + COLUMN_INGREDIENT_QUANTITY + " real not null, "
            + COLUMN_INGREDIENT_UNIT + " text not null, "  // Here's the ingredient_unit column
            + COLUMN_RECIPE_ID_FK + " integer not null, "
            + "FOREIGN KEY(" + COLUMN_RECIPE_ID_FK + ") REFERENCES " + TABLE_RECIPES + "(" + COLUMN_RECIPE_ID + "));";

    // Table and column names for selected recipes
    private static final String TABLE_SELECTED = "selected";
    private static final String COLUMN_SELECTED_ID = "_id";
    private static final String COLUMN_RECIPE_ID_SELECTED = "recipe_id";

    // Database creation SQL statement for selected recipes
    private static final String DATABASE_CREATE_SELECTED = "create table " + TABLE_SELECTED
            + "(" + COLUMN_SELECTED_ID + " integer primary key autoincrement, "
            + COLUMN_RECIPE_ID_SELECTED + " integer not null);";


    public RecipeDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_RECIPES);
        database.execSQL(DATABASE_CREATE_INGREDIENTS);
        database.execSQL(DATABASE_CREATE_SELECTED);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_SELECTED);
        onCreate(database);
    }

    // Recipe CRUD operations

    public void insertRecipe(RecipeModel recipeModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPE_NAME, recipeModel.getRecipeName());
        values.put(COLUMN_RECIPE_INSTRUCTIONS, recipeModel.getInstructions());
        values.put(COLUMN_RECIPE_IMAGE_URI, "null"); // TODO: URI HANDLE
        long recipeId = db.insert(TABLE_RECIPES, null, values);
        recipeModel.setId((int) recipeId);
        ArrayList<IngredientModel> ingredients = new ArrayList<>(recipeModel.getIngredients());
        for (IngredientModel ingredient : ingredients) {
            insertIngredient(ingredient, recipeId);
            ingredient.setRecipeId(recipeId);
        }

    }


    public int updateRecipe(int recipeId, RecipeModel recipeModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPE_NAME, recipeModel.getRecipeName());
        values.put(COLUMN_RECIPE_INSTRUCTIONS, recipeModel.getInstructions());
        values.put(COLUMN_RECIPE_IMAGE_URI, "null"); // TODO: URI HANDLE

        // Update the recipe
        int updatedRecipeRows = db.update(TABLE_RECIPES, values, COLUMN_RECIPE_ID + " = ?", new String[]{String.valueOf(recipeId)});

        // Update the ingredients
        ArrayList<IngredientModel> updatedIngredients = new ArrayList<>(recipeModel.getIngredients());
        for (IngredientModel ingredient : updatedIngredients) {
            updateIngredient(ingredient);
        }

        return updatedRecipeRows;
    }


    public void deleteRecipe(int recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECIPES, COLUMN_RECIPE_ID + " = ?", new String[]{String.valueOf(recipeId)});
    }

    // Ingredient CRUD operations

    public void insertIngredient(IngredientModel ingredient, long recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPE_ID_FK, recipeId); // Assign the recipe ID as a foreign key
        values.put(COLUMN_INGREDIENT_NAME, ingredient.getName());
        values.put(COLUMN_INGREDIENT_QUANTITY, ingredient.getQuantity());
        values.put(COLUMN_INGREDIENT_UNIT, ingredient.getUnit());
        db.insert(TABLE_INGREDIENTS, null, values); // Insert ingredient into the ingredients table
    }

    public void updateIngredient(IngredientModel ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_INGREDIENT_NAME, ingredient.getName());
        values.put(COLUMN_INGREDIENT_QUANTITY, ingredient.getQuantity());
        values.put(COLUMN_INGREDIENT_UNIT, ingredient.getUnit());
        db.update(TABLE_INGREDIENTS, values, COLUMN_INGREDIENT_ID + " = ?", new String[]{String.valueOf(ingredient.getId())});
    }


    public void deleteIngredient(int ingredientId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_INGREDIENTS, COLUMN_INGREDIENT_ID + " = ?", new String[]{String.valueOf(ingredientId)});
    }

    // Get all recipes

    public ArrayList<RecipeModel> getAllRecipesFromDB() {
        ArrayList<RecipeModel> recipes = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int recipeId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_ID));
                String recipeName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_NAME));
                String recipeInstructions = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_INSTRUCTIONS));
                Uri recipeUri = Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_IMAGE_URI)));
                ArrayList<IngredientModel> ingredients = getAllIngredientsForRecipeFromDB(recipeId);

                RecipeModel recipeModel = new RecipeModel(recipeName, ingredients, recipeInstructions, recipeUri);
                recipeModel.setId(recipeId);

                recipes.add(recipeModel);
            } while (cursor.moveToNext());
        }

        cursor.close();
        Log.d("RecipeDatabaseHelper", "Retrieved recipes: " + recipes);
        return recipes;
    }

    public ArrayList<IngredientModel> getAllIngredientsForRecipeFromDB(int recipeId) {
        ArrayList<IngredientModel> ingredients = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_INGREDIENTS, null, COLUMN_RECIPE_ID_FK + " = ?", new String[]{String.valueOf(recipeId)}, null, null, null);

        while (cursor.moveToNext()) {
            String ingredientName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INGREDIENT_NAME));
            String ingredientUnit = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INGREDIENT_UNIT));
            double ingredientQuantity = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_INGREDIENT_QUANTITY));

            IngredientModel ingredientModel = new IngredientModel(ingredientName, ingredientQuantity, ingredientUnit);
            ingredients.add(ingredientModel);
        }

        cursor.close();


        return ingredients;
    }

    public void insertSelectedRecipe(int recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPE_ID_SELECTED, recipeId);
        db.insert(TABLE_SELECTED, null, values);

    }

    public void deleteSelectedRecipe(int recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("helper: ", recipeId + "is deleted");
        db.delete(TABLE_SELECTED, COLUMN_RECIPE_ID_SELECTED + " = ?", new String[]{String.valueOf(recipeId)});

    }


    public ArrayList<RecipeModel> getSelectedRecipes() {
        ArrayList<RecipeModel> selectedRecipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Get recipe IDs from the selected table
        Cursor cursor = db.query(TABLE_SELECTED, new String[]{COLUMN_RECIPE_ID_SELECTED}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int recipeId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_ID_SELECTED));

            // Get recipe information from the recipes table
            Cursor recipeCursor = db.query(TABLE_RECIPES, new String[]{COLUMN_RECIPE_ID, COLUMN_RECIPE_NAME, COLUMN_RECIPE_INSTRUCTIONS, COLUMN_RECIPE_IMAGE_URI}, COLUMN_RECIPE_ID + "=?", new String[]{String.valueOf(recipeId)}, null, null, null);
            if (recipeCursor.moveToFirst()) {
                RecipeModel recipe = new RecipeModel(null,null,null,null);
                recipe.setId(recipeCursor.getInt(recipeCursor.getColumnIndexOrThrow(COLUMN_RECIPE_ID)));
                recipe.setRecipeName(recipeCursor.getString(recipeCursor.getColumnIndexOrThrow(COLUMN_RECIPE_NAME)));
                recipe.setIngredients(getAllIngredientsForRecipeFromDB(recipeId));
                recipe.setInstructions(recipeCursor.getString(recipeCursor.getColumnIndexOrThrow(COLUMN_RECIPE_INSTRUCTIONS)));
                recipe.setRecipeImageUri(Uri.parse(recipeCursor.getString(recipeCursor.getColumnIndexOrThrow(COLUMN_RECIPE_IMAGE_URI))));

                // Add recipe to the list
                selectedRecipes.add(recipe);

            }
            recipeCursor.close();
        }
        cursor.close();

        return selectedRecipes;
    }

}
