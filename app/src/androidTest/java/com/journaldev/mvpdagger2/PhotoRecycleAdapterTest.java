package com.journaldev.mvpdagger2;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.UiThread;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.test.InstrumentationTestCase;
import android.test.mock.MockContext;
import android.view.View;

import com.journaldev.mvpdagger2.Data.ItemPhotoData;
import com.journaldev.mvpdagger2.Data.SelectableItemPhotoData;
import com.journaldev.mvpdagger2.activity.MainActivity;
import com.journaldev.mvpdagger2.adapters.PhotosAdapter;
import com.journaldev.mvpdagger2.adapters.SelectableViewHolder;
import com.journaldev.mvpdagger2.fragments.ViewAllImagesByDate;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.actionWithAssertions;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class PhotoRecycleAdapterTest {


    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    RecyclerView recyclerView;
    PhotosAdapter photosAdapter;

    @Before
    public void start() throws InterruptedException {
        Thread.sleep(1000);
        recyclerView = mActivityRule.getActivity().findViewById(R.id.DataList);
        photosAdapter = (PhotosAdapter) recyclerView.getAdapter();
    }

    @Test
    public void testChandgeSelecable() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                photosAdapter.setSelectable(true);
                assertTrue(photosAdapter.isSelectable());
                photosAdapter.setSelectable(false);
                assertFalse(photosAdapter.isSelectable());
            }
        });
    }

    @Test
    public void testClickItem() {
        boolean startSelectable = photosAdapter.isSelectable();
        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, longClickChildViewWithId(R.id.picture)));

        assertEquals(photosAdapter.isSelectable(), !startSelectable);
    }

    @Test
    public void testAddSelectableAtClickItem() {
        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, longClickChildViewWithId(R.id.picture)));

        assertEquals(photosAdapter.getSelectedItems().size(), 1);
    }

    @Test
    public void testOnSelectableMode() {
        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, longClickChildViewWithId(R.id.picture)));

       assertTrue(photosAdapter.isSelectable());
    }

    @Test
    public void testAddSomeSelectableAtClickItem() {
        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, longClickChildViewWithId(R.id.picture)));

        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(1, clickChildViewWithId(R.id.picture)));
        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(2, clickChildViewWithId(R.id.picture)));
        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(3, clickChildViewWithId(R.id.picture)));

        assertEquals(photosAdapter.getSelectedItems().size(), 4);
    }

    @Test
    public void testNoHaveSelectableAtClickItem() {
        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, longClickChildViewWithId(R.id.picture)));

        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.picture)));

        assertEquals(photosAdapter.getSelectedItems().size(), 0);
    }

    @Test
    public void testOffSelectableAtClickItem() {
        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, longClickChildViewWithId(R.id.picture)));

        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, longClickChildViewWithId(R.id.picture)));

       assertFalse(photosAdapter.isSelectable());
    }

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }

    public static ViewAction longClickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performLongClick();
            }
        };
    }
}
