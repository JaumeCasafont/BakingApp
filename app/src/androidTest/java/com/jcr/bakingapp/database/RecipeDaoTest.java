package com.jcr.bakingapp.database;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.jcr.bakingapp.TestUtil;
import com.jcr.bakingapp.data.database.RecipesDatabase;
import com.jcr.bakingapp.data.models.Recipe;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * Test the implementation of {@link com.jcr.bakingapp.data.database.RecipeDao}
 */
@RunWith(AndroidJUnit4.class)
public class RecipeDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private RecipesDatabase mDatabase;

    @Before
    public void initDb() throws Exception {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                RecipesDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() throws Exception {
        mDatabase.close();
    }

    @Test
    public void bulkInsertAndGetRecipes() {
        // When inserting a new recipes in the data source
        List<Recipe> recipesMock = TestUtil.createRecipes(10, "recipe");

        mDatabase.beginTransaction();
        try {
            mDatabase.recipeDao().bulkInsert(recipesMock);
            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }

        // When subscribing to the emissions of the recipes
        mDatabase.recipeDao().getRecipesList()
                .test()
                // assertValue asserts that there was only one emission of the user
                .assertValue(recipes -> {
                    // The emitted recipes are the expected ones
                    return recipes != null && recipes.size() == 10 &&
                            recipes.get(9).getName().equals("recipe9");
                });
    }
}

