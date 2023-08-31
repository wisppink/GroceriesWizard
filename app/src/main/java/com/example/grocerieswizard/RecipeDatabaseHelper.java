package com.example.grocerieswizard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.net.Uri;
import java.net.URI;
import java.util.ArrayList;

public class RecipeDatabaseHelper extends SQLiteOpenHelper {

    RecipeRecyclerViewAdapter adapter;

    private static final String DATABASE_NAME = "recipes.db";
    private static final int DATABASE_VERSION = 1;

    // Table and column names for recipes
    private static final String TABLE_RECIPES = "recipes";
    private static final String COLUMN_RECIPE_ID = "_id";
    private static final String COLUMN_RECIPE_NAME = "recipe_name";
    private static final String COLUMN_RECIPE_INSTRUCTIONS = "recipe_instructions";
    private static final String COLUMN_RECIPE_IMAGE_URI = "image_uri";
    private static final String COLUMN_INGREDIENT_LIST = "ingredient_list";

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
            + COLUMN_RECIPE_ID_FK + " integer not null, "
            + "FOREIGN KEY(" + COLUMN_RECIPE_ID_FK + ") REFERENCES " + TABLE_RECIPES + "(" + COLUMN_RECIPE_ID + "));";

    public RecipeDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_RECIPES);
        database.execSQL(DATABASE_CREATE_INGREDIENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(database);
    }

    // Recipe CRUD operations

    public void insertRecipe(RecipeModel recipeModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPE_NAME, recipeModel.getRecipeName());
        values.put(COLUMN_RECIPE_INSTRUCTIONS, recipeModel.getHowToPrepare());
        values.put(COLUMN_RECIPE_IMAGE_URI, "null"); // TODO: URI HANDLE
        db.insert(TABLE_RECIPES, null, values);
    }


    public int updateRecipe(int recipeId, String newRecipeName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPE_NAME, newRecipeName);
        return db.update(TABLE_RECIPES, values, COLUMN_RECIPE_ID + " = ?", new String[]{String.valueOf(recipeId)});
    }

    public void deleteRecipe(int recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECIPES, COLUMN_RECIPE_ID + " = ?", new String[]{String.valueOf(recipeId)});
    }

    // Ingredient CRUD operations

    public long addIngredient(int recipeId, String ingredientName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPE_ID_FK, recipeId);
        values.put(COLUMN_INGREDIENT_NAME, ingredientName);
        return db.insert(TABLE_INGREDIENTS, null, values);
    }

    public int updateIngredient(int ingredientId, String newIngredientName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_INGREDIENT_NAME, newIngredientName);
        return db.update(TABLE_INGREDIENTS, values, COLUMN_INGREDIENT_ID + " = ?", new String[]{String.valueOf(ingredientId)});
    }

    public void deleteIngredient(int ingredientId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_INGREDIENTS, COLUMN_INGREDIENT_ID + " = ?", new String[]{String.valueOf(ingredientId)});
    }

    // Get all recipes

    public ArrayList<RecipeModel> getAllRecipes() {
        ArrayList<RecipeModel> recipes = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int recipeId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_ID));
                String recipeName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_NAME));
                String recipeInstructions = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_INSTRUCTIONS));
                Uri recipeUri = Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_IMAGE_URI)));
                ArrayList<IngredientModel> ingredients = getAllIngredientsForRecipe(recipeId);

                RecipeModel recipeModel = new RecipeModel(recipeName, ingredients, recipeInstructions, recipeUri);
                recipeModel.setId(recipeId);

                recipes.add(recipeModel);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        Log.d("RecipeDatabaseHelper", "Retrieved recipes: " + recipes.toString());
        return recipes;
    }

    public ArrayList<IngredientModel> getAllIngredientsForRecipe(int recipeId) {
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
        db.close();

        return ingredients;
    }


}
