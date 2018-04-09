package com.jcr.bakingapp.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.jcr.bakingapp.data.models.Recipe;

@Database(entities = {Recipe.class}, version = 1)
@TypeConverters(Converters.class) 
public abstract class RecipesDatabase extends RoomDatabase {

    private static volatile RecipesDatabase sInstance;

    public abstract RecipeDao recipeDao();

    public static RecipesDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (RecipesDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            RecipesDatabase.class, "recipes.db")
                            .build();
                }
            }
        }
        return sInstance;
    }
}
