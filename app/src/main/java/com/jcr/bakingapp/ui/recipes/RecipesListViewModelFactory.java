package com.jcr.bakingapp.ui.recipes;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.jcr.bakingapp.data.network.RecipesNetworkDataSource;

public class RecipesListViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final RecipesNetworkDataSource mDataSource;

    public RecipesListViewModelFactory(RecipesNetworkDataSource dataSource) {
        this.mDataSource = dataSource;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RecipesListViewModel.class)) {
            return (T) new RecipesListViewModel(mDataSource);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
