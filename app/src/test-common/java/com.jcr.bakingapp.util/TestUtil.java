package com.jcr.bakingapp;

import com.jcr.bakingapp.data.models.Ingredient;
import com.jcr.bakingapp.data.models.Recipe;
import com.jcr.bakingapp.data.models.Step;

import java.util.ArrayList;
import java.util.List;

public class TestUtil {

    public static Recipe createRecipe(String name, int id) {
        return new Recipe(null, 5,
                name, createIngredients(3, "ingredient"), id, createSteps(3));
    }

    public static List<Recipe> createRecipes(int count, String name) {
        List<Recipe> repos = new ArrayList<>();
        for(int i = 0; i < count; i ++) {
            repos.add(createRecipe(name + i, i));
        }
        return repos;
    }

    public static Ingredient createIngredient(String name) {
        return new Ingredient(1, null, name);
    }

    public static List<Ingredient> createIngredients(int count, String name) {
        List<Ingredient> ingredients = new ArrayList<>();
        for(int i = 0; i < count; i ++) {
            ingredients.add(createIngredient(name + i));
        }
        return ingredients;
    }

    public static Step createStep(int id) {
        return new Step(null, null, id, null, null);
    }

    public static List<Step> createSteps(int count) {
        List<Step> steps = new ArrayList<>();
        for(int i = 0; i < count; i ++) {
            steps.add(createStep(i));
        }
        return steps;
    }
}
