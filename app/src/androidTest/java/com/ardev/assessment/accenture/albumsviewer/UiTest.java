package com.ardev.assessment.accenture.albumsviewer;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentTransaction;

import com.android21buttons.fragmenttestrule.FragmentTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.Throws;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class UiTest {

    private final String dataItemsAsJson = "[\n" +
            "  {\n" +
            "    \"userId\": 1,\n" +
            "    \"id\": 1,\n" +
            "    \"title\": \"First Album\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"userId\": 1,\n" +
            "    \"id\": 2,\n" +
            "    \"title\": \"Second Album\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"userId\": 1,\n" +
            "    \"id\": 3,\n" +
            "    \"title\": \"Third Album\"\n" +
            "  }]";

    private final ArrayList<Album> albumsList = new ArrayList<>();

    @Rule
    public ActivityTestRule<AlbumsListActivity> activityRule = new ActivityTestRule<>(AlbumsListActivity.class);
    @Rule
    public FragmentTestRule<?, AlbumsListFragment> fragmentTestRule = FragmentTestRule.create(AlbumsListFragment.class);

    @Before
    public void init() {
        FragmentTransaction fragmentTransaction = activityRule.getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContentContainer, AlbumsListFragment.newInstance()).commit();
    }

    @Test
    public void testEmptyListDisplay() {
        onView(withId(R.id.recyclerErrorMsgTextView))
                .check(matches(isDisplayed()));
    }
/*
    @Test
    @Throws(exceptionClasses = IllegalStateException.class)
    public void testEmptyListView() {
        onView(withId(R.id.contentRecyclerView))
                .perform(
                        RecyclerViewActions.actionOnItemAtPosition(0, click())
                );
    }*/

    @Test
    public void validateViewModel () {
        AlbumsViewModel viewModel = ViewModelProviders.of(activityRule.getActivity()).get(AlbumsViewModel.class);
       assertTrue(viewModel.getAlbums().hasObservers());
    }

    private List<Album> getNoList() {
        return null;
    }

    private List<Album> getEmptyList() {
        return new ArrayList<>();
    }


}
