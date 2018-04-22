package com.jcr.bakingapp.ui;

import android.support.annotation.NonNull;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.jcr.bakingapp.R;
import com.jcr.bakingapp.RecyclerViewMatcher;
import com.jcr.bakingapp.ui.recipes.RecipesListActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RecipesListActivityTest {

    @Rule
    public ActivityTestRule<RecipesListActivity> activityRule =
            new ActivityTestRule<>(RecipesListActivity.class, true, true);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = activityRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void loadResults() {
        onView(withId(R.id.recipes_rv)).perform(RecyclerViewActions.scrollToPosition(3));
        onView(listMatcher().atPosition(3)).check(matches(isDisplayed()));
    }

    @Test
    public void onRecipeClick_openRecipeSteps() {
        onView(listMatcher().atPosition(0)).perform(click());
        onView(withId(R.id.ingredients_title_tv)).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

    @NonNull
    private RecyclerViewMatcher listMatcher() {
        return new RecyclerViewMatcher(R.id.recipes_rv);
    }
}

