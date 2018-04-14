package com.jcr.bakingapp.data.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcr.bakingapp.data.models.Ingredient;
import com.jcr.bakingapp.data.models.Step;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class Converters {

    static Gson gson = new Gson();

    @TypeConverter
    public static List<Ingredient> stringToIngredientsList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Ingredient>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String ingredientsListToString(List<Ingredient> ingredients) {
        return gson.toJson(ingredients);
    }

    @TypeConverter
    public static List<Step> stringToStepsList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Step>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String stepsListToString(List<Step> steps) {
        return gson.toJson(steps);
    }
}
