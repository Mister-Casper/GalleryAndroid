package com.journaldev.mvpdagger2;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.journaldev.mvpdagger2.view.activity.MainActivity;
import com.journaldev.mvpdagger2.view.adapter.ImagesAdapter;

import junit.framework.Assert;

import org.hamcrest.Matcher;
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
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class ImagesAdapterTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    RecyclerView recyclerView;
    ImagesAdapter imagesAdapter;

    @Before
    public void setUp() {
        onView(allOf(isDisplayed(), withId(R.id.DataList))).check(matches(isDisplayed()));
        recyclerView = mActivityRule.getActivity().findViewById(R.id.DataList);
        imagesAdapter = (ImagesAdapter) recyclerView.getAdapter();
    }


    @Test
    public void testChangeSelectable() throws Throwable {
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imagesAdapter.setSelectable(true);
                assertTrue(imagesAdapter.isSelectable());
            }
        });
    }


    @Test
    public void testGoneVisibilityCheckBoxByDoubleLongClick() {
        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, longClickChildViewWithId(R.id.picture)));
        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, longClickChildViewWithId(R.id.picture)));

        CheckBox checkBox = mActivityRule.getActivity().findViewById(R.id.checked_text_item);

        assertFalse(checkBox.isChecked());
    }

    @Test
    public void testVisibilityCheckBoxByLongClick() {
        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, longClickChildViewWithId(R.id.picture)));

        CheckBox checkBox = mActivityRule.getActivity().findViewById(R.id.checked_text_item);

        assertTrue(checkBox.isChecked());
    }

    @Test
    public void testAddSelectableByCheckBoxClick() {
        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, longClickChildViewWithId(R.id.picture)));

        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(2, clickChildViewWithId(R.id.checked_text_item)));

        assertEquals(imagesAdapter.getSelectedItems().size(), 2);
    }


    @Test
    public void testNoSelectableByCheckBoxClick() {
        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, longClickChildViewWithId(R.id.picture)));

        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.checked_text_item)));

        assertEquals(imagesAdapter.getSelectedItems().size(), 0);
    }

    @Test
    public void testViewSelectableMenu() {
        android.support.constraint.ConstraintLayout menu = mActivityRule.getActivity().findViewById(R.id.selectablemenu);
        Assert.assertEquals(menu.getVisibility(), View.GONE);
    }

    @Test
    public void testViewSelectableMenuAfterLongClick() {
        android.support.constraint.ConstraintLayout menu = mActivityRule.getActivity().findViewById(R.id.selectablemenu);
        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, longClickChildViewWithId(R.id.picture)));
        Assert.assertEquals(menu.getVisibility(), View.VISIBLE);
    }

    @Test
    public void testLongClickItem() {
        boolean startSelectable = imagesAdapter.isSelectable();
        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, longClickChildViewWithId(R.id.picture)));

        assertEquals(imagesAdapter.isSelectable(), !startSelectable);
    }

    @Test
    public void testAddSelectableByLongClickItem() {
        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, longClickChildViewWithId(R.id.picture)));

        assertEquals(imagesAdapter.getSelectedItems().size(), 1);
    }

    @Test
    public void testOnSelectableMode() {
        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, longClickChildViewWithId(R.id.picture)));

        assertTrue(imagesAdapter.isSelectable());
    }

    @Test
    public void testAddSomeSelectableItem() {
        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, longClickChildViewWithId(R.id.picture)));

        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(1, clickChildViewWithId(R.id.picture)));
        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(2, clickChildViewWithId(R.id.picture)));
        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(3, clickChildViewWithId(R.id.picture)));

        assertEquals(imagesAdapter.getSelectedItems().size(), 4);
    }

    @Test
    public void testNoHaveSelectableItem() {
        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, longClickChildViewWithId(R.id.picture)));

        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.picture)));

        assertEquals(imagesAdapter.getSelectedItems().size(), 0);
    }

    @Test
    public void testOffSelectableMode() {
        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, longClickChildViewWithId(R.id.picture)));

        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, longClickChildViewWithId(R.id.picture)));

        assertFalse(imagesAdapter.isSelectable());
    }

    @Test
    public void testExitFromSelectableMode() {
        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, longClickChildViewWithId(R.id.picture)));

        onView(withId(R.id.exitButton)).
                perform(click());

        assertFalse(imagesAdapter.isSelectable());
    }

    @Test
    public void testCounterItemSelectedChange() {
        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, longClickChildViewWithId(R.id.picture)));

        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(1, clickChildViewWithId(R.id.picture)));
        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(2, clickChildViewWithId(R.id.picture)));
        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.picture)));

        TextView text = mActivityRule.getActivity().findViewById(R.id.itemSelected);
        int itemSelected = Integer.parseInt(text.getText().toString());

        imagesAdapter.getSelectedItems();
        assertEquals(itemSelected, 2);

    }

    @Test
    public void testAllSelect(){
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                imagesAdapter.setItemsSelectable(true);
                Assert.assertEquals(imagesAdapter.getSelectedItems().size(), imagesAdapter.getItemCount());
            }
        });
    }

    @Test
    public void testOffAllSelect(){
        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, longClickChildViewWithId(R.id.picture)));

        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(1, clickChildViewWithId(R.id.picture)));
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                imagesAdapter.setItemsSelectable(false);
                Assert.assertEquals(imagesAdapter.getSelectedItems().size(),0);
            }
        });
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
