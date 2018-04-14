package com.jcr.bakingapp.data.network;

import com.jcr.bakingapp.data.models.Recipe;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipesNetworkDataSource {

    static final String RECIPE_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    private static RecipesNetworkDataSource sInstance;
    private final RecipeService mRecipeService;

    private RecipesNetworkDataSource() {
        mRecipeService = buildRetrofit();
    }

    private RecipeService buildRetrofit() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RECIPE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(RecipeService.class);
    }

    public static RecipesNetworkDataSource getInstance() {
        if (sInstance == null) {
            synchronized (RecipesNetworkDataSource.class) {
                if (sInstance == null) {
                    sInstance = new RecipesNetworkDataSource();
                }
            }
        }
        return sInstance;
    }

    public Flowable<List<Recipe>> getRecipesList() {
        return mRecipeService.getRecipesList();
    }
}
