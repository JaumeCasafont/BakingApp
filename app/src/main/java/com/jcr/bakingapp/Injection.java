package com.jcr.bakingapp;

import com.jcr.bakingapp.data.network.RecipesDataSource;
import com.jcr.bakingapp.ui.recipes.RecipesListViewModelFactory;

/**
 * Enables injection of data sources.
 */
public class Injection {

    public static RecipesListViewModelFactory provideRecipesListViewModel() {
        return new RecipesListViewModelFactory(new RecipesDataSource());
    }
}
