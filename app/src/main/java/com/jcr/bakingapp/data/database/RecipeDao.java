package com.jcr.bakingapp.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.jcr.bakingapp.data.models.Recipe;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(List<Recipe> recipes);

    @Query("SELECT * FROM Recipes")
    Flowable<List<Recipe>> getRecipesList();

    @Query("SELECT * FROM Recipes WHERE id = :recipeId")
    Flowable<Recipe> getRecipe(int recipeId);
}
