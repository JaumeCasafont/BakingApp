package com.jcr.bakingapp.ui.recipes;

import android.arch.lifecycle.ViewModel;

import com.jcr.bakingapp.data.RecipesRepository;
import com.jcr.bakingapp.data.models.Recipe;
import com.jcr.bakingapp.data.network.RecipesNetworkDataSource;

import java.util.List;

import io.reactivex.Flowable;

public class RecipesListViewModel extends ViewModel {

    private final RecipesRepository mRepository;

    private Flowable<List<Recipe>> mRecipes;

    public RecipesListViewModel(RecipesRepository repository) {
        mRepository = repository;
        mRecipes = mRepository.getRecipesList();
    }

    public Flowable<List<Recipe>> getRecipesList() {
        return mRecipes;
    }
}
