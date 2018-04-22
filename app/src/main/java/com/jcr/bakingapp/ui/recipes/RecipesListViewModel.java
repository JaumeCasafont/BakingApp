package com.jcr.bakingapp.ui.recipes;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.jcr.bakingapp.SimpleIdlingResource;
import com.jcr.bakingapp.data.RecipesRepository;
import com.jcr.bakingapp.data.models.Recipe;
import com.jcr.bakingapp.data.network.RecipesNetworkDataSource;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Action;

public class RecipesListViewModel extends ViewModel {

    private final RecipesRepository mRepository;

    private List<Recipe> mRecipes;

    public RecipesListViewModel(RecipesRepository repository) {
        mRepository = repository;
    }

    public Flowable<List<Recipe>> getRecipesList(@Nullable SimpleIdlingResource idlingResource) {
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        return mRepository.getRecipesList()
                .map(recipes -> {
                    if (idlingResource != null) {
                        idlingResource.setIdleState(true);
                    }
                    mRecipes = recipes;
                    return mRecipes;
                })
                .retry(1);

    }
}
