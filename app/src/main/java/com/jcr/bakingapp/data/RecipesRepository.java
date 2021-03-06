package com.jcr.bakingapp.data;

import com.jcr.bakingapp.data.database.RecipeDao;
import com.jcr.bakingapp.data.models.Recipe;
import com.jcr.bakingapp.data.models.Step;
import com.jcr.bakingapp.data.network.RecipesNetworkDataSource;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RecipesRepository {

    private static RecipesRepository sInstance;
    private final RecipesNetworkDataSource mNetworkDataSource;
    private final RecipeDao mDao;
    private boolean shouldRefresh = true;
    private int currentRecipeId;

    private RecipesRepository(RecipesNetworkDataSource dataSource,
                              RecipeDao dao) {
        mNetworkDataSource = dataSource;
        mDao = dao;
    }

    public synchronized static RecipesRepository getInstance(RecipesNetworkDataSource dataSource,
                                                             RecipeDao dao) {
        if (sInstance == null) {
            synchronized (RecipesNetworkDataSource.class) {
                if (sInstance == null) {
                    sInstance = new RecipesRepository(dataSource, dao);
                }
            }
        }
        return sInstance;
    }

    public Flowable<List<Recipe>> getRecipesList() {
        return mDao.getRecipesList()
                .flatMap(localResult -> {
                    if (localResult.isEmpty() || shouldRefresh) {
                        shouldRefresh = false;
                        return mNetworkDataSource.getRecipesList()
                                .subscribeOn(Schedulers.io())
                                .doOnNext(mDao::bulkInsert)
                                .flatMap(x -> mDao.getRecipesList())
                                .observeOn(AndroidSchedulers.mainThread());
                    } else {
                        return Flowable.just(localResult);
                    }
                });
    }

    public Flowable<Recipe> getRecipe(int recipeId) {
        currentRecipeId = recipeId;
        return mDao.getRecipe(currentRecipeId);
    }

    public Flowable<Recipe> getCurrentRecipe() {
        return mDao.getRecipe(currentRecipeId);
    }

    public Flowable<Step> getStep(int recipeId, int stepId) {
        return mDao.getRecipe(recipeId)
                .map(recipe -> recipe.getSteps().get(stepId));
    }
}

