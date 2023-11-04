package com.example.grocerieswizard.data.local;

import com.example.grocerieswizard.data.local.dao.CartDao;
import com.example.grocerieswizard.data.local.dao.FavDao;
import com.example.grocerieswizard.data.local.dao.IngredientDao;
import com.example.grocerieswizard.data.local.dao.RecipeDao;
import com.example.grocerieswizard.data.local.model.CartItem;
import com.example.grocerieswizard.data.local.model.FavItem;
import com.example.grocerieswizard.data.local.model.IngredientItem;
import com.example.grocerieswizard.data.local.model.RecipeItem;
import com.example.grocerieswizard.ui.model.RecipeUi;

import java.util.List;

public class RecipeLocalDataSourceImpl implements RecipeLocalDataSource {
    private final CartDao cartDao;
    private final FavDao favDao;
    private final IngredientDao ingredientDao;
    private final RecipeDao recipeDao;

    public RecipeLocalDataSourceImpl(CartDao cartDao, FavDao favDao, IngredientDao ingredientDao, RecipeDao recipeDao) {
        this.cartDao = cartDao;
        this.favDao = favDao;
        this.ingredientDao = ingredientDao;
        this.recipeDao = recipeDao;
    }

    @Override
    public List<RecipeItem> getAllRecipes() {
        return recipeDao.getAllRecipes();
    }

    @Override
    public void insertRecipe(RecipeItem recipe) {
        recipeDao.insert(recipe);
    }

    @Override
    public void deleteRecipe(RecipeUi recipeUi) {
        recipeDao.delete(recipeUi.getId());
    }

    @Override
    public int updateRecipe(int oldRecipeId, RecipeItem recipe) {
        return recipeDao.updateRecipe(oldRecipeId, recipe.getName(), recipe.getInstructors(), recipe.getIngredientList(), recipe.isFav(), recipe.isCart());
    }

    @Override
    public RecipeItem getRecipeByRecipeId(int recipeId) {
        return recipeDao.getRecipeById(recipeId);
    }


    @Override
    public List<CartItem> getCartItems() {
        return cartDao.getAllCartItems();
    }

    @Override
    public void insertCartItem(CartItem cartItem) {
        cartDao.insert(cartItem);
    }

    @Override
    public void deleteCartItem(RecipeUi recipeUi) {
        cartDao.deleteCartItem(recipeUi.getId());
    }

    @Override
    public Boolean isRecipeInCart(int recipeId) {
        return cartDao.isRecipeInCart(recipeId);
    }


    @Override
    public List<FavItem> getFavoriteRecipes() {
        return favDao.getAllFavItems();
    }

    @Override
    public void insertRecipeFav(RecipeUi recipeUi) {
        FavItem favItem = new FavItem(recipeUi.getId());
        favDao.insert(favItem);
    }

    @Override
    public void deleteRecipeFav(int recipeId) {
        favDao.deleteFavItem(recipeId);
    }

    @Override
    public void deleteIngredient(IngredientItem ingredientItem) {
        ingredientDao.delete(ingredientItem);
    }

    @Override
    public void insertIngredient(IngredientItem ingredient) {
        ingredientDao.insert(ingredient);
    }
}