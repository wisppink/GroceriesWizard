package com.example.grocerieswizard.ui.fav;

import com.example.grocerieswizard.ui.model.RecipeUi;

import java.util.List;

public interface FavContract {
    interface View {
        void showFavList(List<RecipeUi> recipeUis);

        void removeFromFavorites(RecipeUi recipeUi);
    }

    interface Presenter {

        void bindView(View view);

        void setFavList();

        void removeFromFavorites(RecipeUi recipeUi);

        boolean isRecipeInCart(int id);

        void insertCartItem(RecipeUi id);

        void removeFromCart(RecipeUi id);
    }
}
