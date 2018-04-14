package com.jcr.bakingapp.ui.steps;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.jcr.bakingapp.data.RecipesRepository;

public class StepsListViewModelFactory extends ViewModelProvider.NewInstanceFactory {


    private final RecipesRepository mRepository;
    private final int mRecipeId;

    public StepsListViewModelFactory(RecipesRepository repository, int recipeId) {
        this.mRepository = repository;
        this.mRecipeId = recipeId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(StepsListViewModel.class)) {

            return (T) new StepsListViewModel(mRepository, mRecipeId);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }

}
