package com.jcr.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.jcr.bakingapp.R;


public class IngredientsListWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String recipeTitle, String ingredients) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_list_widget);
        views.setTextViewText(R.id.recipe_widget_title, recipeTitle);

        views.setTextViewText(R.id.ingredients_widget_text, ingredients);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        IngredientsListService.startActionUpdateIngredientsList(context);
    }

    public static void updateIngredientsListWidgets(Context context, AppWidgetManager appWidgetManager,
                                                    int[] appWidgetIds, String recipeTitle, String ingredients) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipeTitle, ingredients);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first ingredients_list_widget_preview is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last ingredients_list_widget_preview is disabled
    }
}

