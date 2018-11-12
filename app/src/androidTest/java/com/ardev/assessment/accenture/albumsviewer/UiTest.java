package com.ardev.assessment.accenture.albumsviewer;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;

import com.android21buttons.fragmenttestrule.FragmentTestRule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

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
    public FragmentTestRule<?, AlbumsListFragment> fragmentTestRule =
            FragmentTestRule.create(AlbumsListFragment.class);

    @Before
    public void init() {
        FragmentTransaction fragmentTransaction = activityRule.getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContentContainer, AlbumsListFragment.newInstance()).commit();
    }

    @Test
    public void testEmptyListDisplay() {
//        recyclerView.setAdapter(new AlbumsListAdapter(getEmptyList()));
//        int itemCount = Objects.requireNonNull(recyclerView.getAdapter()).getItemCount();
//        Assert.assertEquals(itemCount, 0);
        onView(withId(R.id.recyclerErrorMsgTextView)/*, withText(activityRule.getActivity().getString(R.string.noDataAvailable))*/)
                .check(matches(isDisplayed()));
    }

    @Test
    public void testFilledList() {
         onView(withId(R.id.contentRecyclerView))
                .perform(
                        RecyclerViewActions.actionOnItemAtPosition(0, click())
                );
    }

    /*
            int itemCount = Objects.requireNonNull(recyclerView.getAdapter()).getItemCount();
            */
    private List<Album> getNoList() {
        return null;
    }

    private List<Album> getEmptyList() {
        return new ArrayList<>();
    }


}
