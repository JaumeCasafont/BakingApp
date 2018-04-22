package com.jcr.bakingapp.ui.steps;

import android.arch.lifecycle.ViewModel;

import com.jcr.bakingapp.data.RecipesRepository;
import com.jcr.bakingapp.data.models.Recipe;
import com.jcr.bakingapp.data.models.Step;

import java.util.List;

import io.reactivex.Flowable;

public class StepsListViewModel extends ViewModel {

    private final RecipesRepository mRepository;

    private int mRecipeId;
    private Recipe mRecipe;

    public StepsListViewModel(RecipesRepository repository, int recipeId) {
        mRepository = repository;
        mRecipeId = recipeId;
    }

    public Flowable<Recipe> getRecipe() {
        return mRepository.getRecipe(mRecipeId)
                .map(recipe -> {
                    mRecipe = recipe;
                    return mRecipe;
                });
    }

    public Recipe getRetainedRecipe() {
        return mRecipe;
    }
}
