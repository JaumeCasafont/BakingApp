package com.jcr.bakingapp.ui.detail;

import android.arch.lifecycle.ViewModel;

import com.jcr.bakingapp.data.RecipesRepository;
import com.jcr.bakingapp.data.models.Recipe;
import com.jcr.bakingapp.data.models.Step;

import java.util.List;

import io.reactivex.Flowable;

public class StepDetailViewModel extends ViewModel {

    private final RecipesRepository mRepository;

    private Step mStep;
    private int mRecipeId;
    private int mStepId;

    public StepDetailViewModel(RecipesRepository repository, int recipeId, int stepId) {
        mRepository = repository;
        mRecipeId = recipeId;
        mStepId = stepId;
    }

    public Flowable<Step> getStep() {
        return mRepository.getStep(mRecipeId, mStepId)
                .map(step -> {
                    mStep = step;
                    return mStep;
                });
    }

    public Step getRetainedStep() {
        return mStep;
    }
}
