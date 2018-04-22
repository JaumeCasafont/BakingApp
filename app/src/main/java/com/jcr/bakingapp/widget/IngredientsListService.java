package com.jcr.bakingapp.widget;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.jcr.bakingapp.Injection;
import com.jcr.bakingapp.R;
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

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startForegroundService() {
        String CHANNEL_ID = "my_channel_01";
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                getResources().getString(R.string.appwidget_notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT);

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("")
                .setContentText("").build();

        startForeground(1, notification);
    }

    public static void startActionUpdateIngredientsList(Context context) {
        Intent intent = new Intent(context, IngredientsListService.class);
        intent.setAction(ACTION_UPDATE);
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            context.startService(intent);
        } else {
            context.startForegroundService(intent);
        }
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
