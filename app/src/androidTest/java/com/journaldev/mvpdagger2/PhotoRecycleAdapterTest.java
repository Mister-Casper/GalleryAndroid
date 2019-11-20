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

import com.journaldev.mvpdagger2.activity.MainActivity;
import com.journaldev.mvpdagger2.adapters.PhotosAdapter;

import junit.framework.Assert;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.anything;
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
        Thread.sleep(650);
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

        assertEquals(photosAdapter.getSelectedItems().size(), 2);
    }


    @Test
    public void testNoSelectableByCheckBoxClick() {
        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, longClickChildViewWithId(R.id.picture)));

        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.checked_text_item)));

        assertEquals(photosAdapter.getSelectedItems().size(), 0);
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
        boolean startSelectable = photosAdapter.isSelectable();
        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, longClickChildViewWithId(R.id.picture)));

        assertEquals(photosAdapter.isSelectable(), !startSelectable);
    }

    @Test
    public void testAddSelectableByLongClickItem() {
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
    public void testAddSomeSelectableItem() {
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
    public void testNoHaveSelectableItem() {
        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, longClickChildViewWithId(R.id.picture)));

        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.picture)));

        assertEquals(photosAdapter.getSelectedItems().size(), 0);
    }

    @Test
    public void testOffSelectableMode() {
        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, longClickChildViewWithId(R.id.picture)));

        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, longClickChildViewWithId(R.id.picture)));

        assertFalse(photosAdapter.isSelectable());
    }

    @Test
    public void testExitFromSelectableMode() {
        onView(withId(R.id.DataList)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, longClickChildViewWithId(R.id.picture)));

        onView(withId(R.id.exitButton)).
                perform(click());

        assertFalse(photosAdapter.isSelectable());
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

        photosAdapter.getSelectedItems();
        assertEquals(itemSelected, 2);

    }

    @Test
    public void testAllSelect(){
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                photosAdapter.setItemsSelectable(true);
                Assert.assertEquals(photosAdapter.getSelectedItems().size(), photosAdapter.getItemCount());
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
                photosAdapter.setItemsSelectable(false);
                Assert.assertEquals(photosAdapter.getSelectedItems().size(),0);
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
