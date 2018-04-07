package com.jcr.bakingapp.data.network;

import com.jcr.bakingapp.data.models.Recipe;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipesDataSource implements RecipeService {

    static final String RECIPE_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    private static RecipeService mRecipeService;

    public RecipesDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RECIPE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        mRecipeService = retrofit.create(RecipeService.class);
    }

    @Override
    public Flowable<List<Recipe>> getRecipies() {
        return mRecipeService.getRecipies();
    }
}
