package com.jcr.bakingapp.ui.recipes;

import android.arch.lifecycle.ViewModel;

import com.jcr.bakingapp.data.models.Recipe;
import com.jcr.bakingapp.data.network.RecipesDataSource;

import java.util.List;

import io.reactivex.Flowable;

public class RecipesListViewModel extends ViewModel {

    private final RecipesDataSource mDataSource;

    private List<Recipe> mRecipes;

    public RecipesListViewModel(RecipesDataSource dataSource) {
        mDataSource = dataSource;
    }

    public Flowable<List<Recipe>> getRecipes() {
        return mDataSource.getRecipies()
                .map(recipes -> {
                    mRecipes = recipes;
                    return mRecipes;
                });
    }
}
