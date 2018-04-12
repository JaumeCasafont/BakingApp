package com.jcr.bakingapp;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import com.jcr.bakingapp.data.RecipesRepository;
import com.jcr.bakingapp.data.models.Recipe;
import com.jcr.bakingapp.ui.recipes.RecipesListViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import io.reactivex.Flowable;

import static org.mockito.Mockito.when;

/**
 * Unit test for {@link RecipesListViewModel}
 */
public class RecipesListViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private RecipesRepository mRepository;

    private RecipesListViewModel mViewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mViewModel = new RecipesListViewModel(mRepository);
    }

    @Test
    public void getRecipes_whenResultIsEmpty() throws InterruptedException {
        when(mRepository.getRecipesList()).thenReturn(Flowable.empty());

        mViewModel.getRecipesList()
                .test()
                .assertNoValues();
    }

    @Test
    public void getLastRecipeName_whenResultOK() throws InterruptedException {
        List<Recipe> recipesMock = com.jcr.bakingapp.TestUtil.createRecipes(10, "recipe");
        when(mRepository.getRecipesList()).thenReturn(Flowable.just(recipesMock));

        mViewModel.getRecipesList()
                .test()
                .assertValue(recipes -> recipes != null && recipes.size() == 10 &&
                        recipes.get(9).getName().equals("recipe9"));
    }
}

