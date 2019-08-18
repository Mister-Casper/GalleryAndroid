package com.journaldev.mvpdagger2;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.UiThread;
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
import static android.support.test.espresso.action.ViewActions.actionWithAssertions;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.greaterThan;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class PhotoRecycleAdapterTest extends InstrumentationTestCase {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    RecyclerView recyclerView;
    PhotosAdapter photosAdapter;


    @Test
    public void testChandgeSelecable(){
        recyclerView = mActivityRule.getActivity().findViewById(R.id.DataList);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                photosAdapter = (PhotosAdapter) recyclerView.getAdapter();
                photosAdapter.setSelectable(true);
                assertTrue(photosAdapter.isSelectable());
                photosAdapter.setSelectable(false);
                assertFalse(photosAdapter.isSelectable());
            }
        });
    }

    @Test
    public void textClickItem() {
        recyclerView = mActivityRule.getActivity().findViewById(R.id.DataList);
        photosAdapter = (PhotosAdapter) recyclerView.getAdapter();
        boolean startSelectable = photosAdapter.isSelectable();
        onView(withId(R.id.DataList)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.picture)));
        assertEquals(photosAdapter.isSelectable(), !startSelectable);
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
                v.performLongClick();
            }
        };
    }


}
