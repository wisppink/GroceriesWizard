package com.example.grocerieswizard.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.grocerieswizard.data.local.dao.CartDao;
import com.example.grocerieswizard.data.local.dao.FavDao;
import com.example.grocerieswizard.data.local.dao.IngredientDao;
import com.example.grocerieswizard.data.local.dao.RecipeDao;
import com.example.grocerieswizard.data.local.model.CartItem;
import com.example.grocerieswizard.data.local.model.FavItem;
import com.example.grocerieswizard.data.local.model.IngredientItem;
import com.example.grocerieswizard.data.local.model.RecipeItem;
import com.example.grocerieswizard.data.local.typeConverter.IngredientDescConverter;
import com.example.grocerieswizard.data.local.typeConverter.IngredientItemListConverter;

@Database(entities = {CartItem.class, FavItem.class, IngredientItem.class, RecipeItem.class}, version = 1)
@TypeConverters({IngredientDescConverter.class, IngredientItemListConverter.class})
public abstract class GWDatabase extends RoomDatabase {

    private static GWDatabase instance;

    public abstract CartDao cartDao();

    public abstract FavDao favDao();

    public abstract IngredientDao ingredientDao();

    public abstract RecipeDao recipeDao();

    public static synchronized GWDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            GWDatabase.class, "gw_database")
                    .fallbackToDestructiveMigration().allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
