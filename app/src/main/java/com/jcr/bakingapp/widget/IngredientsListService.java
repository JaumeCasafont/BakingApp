package com.jcr.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.jcr.bakingapp.Injection;
import com.jcr.bakingapp.data.RecipesRepository;
import com.jcr.bakingapp.data.models.Ingredient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class IngredientsListService extends IntentService {

    public static final String ACTION_UPDATE = "com.jcr.bakingapp.ingredients_list_widget_preview.extra.UPDATE";

    private RecipesRepository mRepository;

    public IngredientsListService() {
        super("IngredientsListService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mRepository = Injection.provideRepository(this);
    }

    public static void startActionUpdateIngredientsList(Context context) {
        Intent intent = new Intent(context, IngredientsListService.class);
        intent.setAction(ACTION_UPDATE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case ACTION_UPDATE:
                        handleActionUpdateIngredientsListWidget();
                        break;
                    default:
                        //ignore
                        break;
                }
            }
        }
    }

    private void handleActionUpdateIngredientsListWidget() {
        mRepository.getCurrentRecipe()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recipe -> {
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
                    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsListWidgetProvider.class));
                    StringBuilder stringBuilder = new StringBuilder();
                    for (Ingredient ingredient : recipe.getIngredients()) {
                        stringBuilder.append("· ").append(ingredient.toString()).append("\n");
                    }
                    IngredientsListWidgetProvider.updateIngredientsListWidgets(
                            this, appWidgetManager, appWidgetIds, recipe.getName(), stringBuilder.toString());
                });
    }
}
