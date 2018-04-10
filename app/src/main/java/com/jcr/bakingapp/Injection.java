package com.jcr.bakingapp;

import android.content.Context;

import com.jcr.bakingapp.data.RecipesRepository;
import com.jcr.bakingapp.data.database.RecipesDatabase;
import com.jcr.bakingapp.data.network.RecipesNetworkDataSource;
import com.jcr.bakingapp.ui.recipes.RecipesListViewModelFactory;

/**
 * Enables injection of data sources.
 */
public class Injection {

    public static RecipesRepository provideRepository(Context context) {
        RecipesDatabase database = RecipesDatabase.getInstance(context.getApplicationContext());
        RecipesNetworkDataSource networkDataSource = RecipesNetworkDataSource.getInstance();
        return RecipesRepository.getInstance(networkDataSource, database.recipeDao());
    }

    public static RecipesListViewModelFactory provideRecipesListViewModel(Context context) {
        return new RecipesListViewModelFactory(provideRepository(context));
    }

}
