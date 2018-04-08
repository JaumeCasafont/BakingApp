package com.jcr.bakingapp.data.network;

import com.jcr.bakingapp.data.models.Recipe;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;

public interface RecipeService {

    @GET("baking.json")
    Flowable<List<Recipe>> getRecipes();
}
