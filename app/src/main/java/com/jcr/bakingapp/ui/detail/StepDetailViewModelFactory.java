package com.jcr.bakingapp.ui.detail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.jcr.bakingapp.data.RecipesRepository;

public class StepDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {


    private final RecipesRepository mRepository;
    private int mRecipeId;
    private int mStepId;

    public StepDetailViewModelFactory(RecipesRepository repository, int recipeId, int stepId) {
        this.mRepository = repository;
        mRecipeId = recipeId;
        mStepId = stepId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(StepDetailViewModel.class)) {

            return (T) new StepDetailViewModel(mRepository, mRecipeId, mStepId);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }

}
