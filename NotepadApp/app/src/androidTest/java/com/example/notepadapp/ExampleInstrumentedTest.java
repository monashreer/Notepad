package com.example.notepadapp;

import android.os.IBinder;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.Root;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.runner.AndroidJUnit4;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.junit.runner.RunWith;

import android.support.test.rule.ActivityTestRule;
import android.view.WindowManager;

import org.junit.Rule;
import org.junit.runners.MethodSorters;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;



/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private Random random = new Random();

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void saveNote(){
        int randValue = random.nextInt(40) + 1;
        String title = "SaveNoteTitle" + randValue;
        String content = "SaveNoteContent" + randValue;

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(Calendar.getInstance().getTime());

        createNote(title, content);

        onView(withText(title)).check(matches(isDisplayed()));
        onView(withText(content)).check(matches(isDisplayed()));
        onView(withText(timeStamp.substring(11, 16))).check(matches(isDisplayed()));
        deleteNoteWithTitle(title);
    }

    @Test
    public void deleteNote(){
        int randValue = random.nextInt(40) + 1;
        String title = "DeleteNoteTitle" + randValue;
        String content = "DeleteNoteContent" + randValue;

        createNote(title, content);

        onView(withText(title)).perform(click());
        onView(withId(R.id.deleteButton)).perform(click());
        onView(withText("Are you sure if you want to delete this note?")).check(matches(isDisplayed()));
        onView(withText("Yes")).perform(click());
    }

    @Test
    public void editNote(){

        int randValue = random.nextInt(40) + 1;
        String title = "EditNoteTitle" + randValue;
        String content = "EditNoteContent" + randValue;
        createNote(title, content);

        onView(withText(title)).perform(click());
        onView(withId(R.id.editButton)).perform(click());
        onView(withId(R.id.note_Title)).perform(typeText(title));
        onView(withId(R.id.note_Content)).perform(typeText(content));
        closeSoftKeyboard();
        onView(withId(R.id.editOption)).perform(click());

        onView(withText(title+title)).perform(click());
        onView(withText(title+title)).check(matches(isDisplayed()));
        onView(withText(content+content)).check(matches(isDisplayed()));

        deleteNoteWithTitle(title+title);
    }

    @Test
    public void saveNoteWithoutTitle(){
        int randValue = random.nextInt(40) + 1;
        String title = "SaveNoteWithoutTitle" + randValue;

        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.note_Content)).perform(typeText(title));
        closeSoftKeyboard();
        onView(withId(R.id.saveOption)).perform(click());

        //Check for toast message
        onView(withText(R.string.save_note_without_title)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    @Test
    public void checkSortOrderPersistence(){
        int randValue = random.nextInt(40) + 1;
        String title1 = "AaaaaaaaTitle1" + randValue;
        String content1 = "AaaaaaaaContent1" + randValue;

        String title2 = "AaaaaaaaTitle2" + randValue;
        String content2 = "AaaaaaaaContent2" + randValue;

        String title3 = "BbbbbbbTitle1" + randValue;
        String content3 = "BbbbbbContent1" + randValue;

        createNote(title1,content1);
        createNote(title2,content2);

        onView(withContentDescription("More options")).perform(click());
        onView(withText(R.string.action_sortName)).perform(click());

        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withText(title1)).check(matches(isDisplayed()));
        onView(withText(content1)).check(matches(isDisplayed()));
        Espresso.pressBack();

        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withText(title2)).check(matches(isDisplayed()));
        onView(withText(content2)).check(matches(isDisplayed()));
        Espresso.pressBack();

        createNote(title3,content3);
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
        onView(withText(title3)).check(matches(isDisplayed()));
        onView(withText(content3)).check(matches(isDisplayed()));
        Espresso.pressBack();

        deleteNoteWithTitle(title1);
        deleteNoteWithTitle(title2);
        deleteNoteWithTitle(title3);
    }

    @Test
    public void checkSortByDate(){

        int randValue = random.nextInt(40) + 1;
        String title1 = "FirstNoteTitle" + randValue;
        String content1 = "FirstNoteContent" + randValue;
        createNote(title1,content1);

        String title2 = "SecondNoteTitle" + randValue;
        String content2 = "SecondNoteContent" + randValue;
        createNote(title2,content2);

        onView(withContentDescription("More options")).perform(click());
        onView(withText(R.string.action_sortDate)).perform(click());

        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withText(title2)).check(matches(isDisplayed()));
        onView(withText(content2)).check(matches(isDisplayed()));
        Espresso.pressBack();

        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withText(title1)).check(matches(isDisplayed()));
        onView(withText(content1)).check(matches(isDisplayed()));
        Espresso.pressBack();

        deleteNoteWithTitle(title1);
        deleteNoteWithTitle(title2);
    }

    @Test
    public void checkSortByName(){

        int randValue = random.nextInt(40) + 1;
        String title1 = "AaaaaaaaTitle1" + randValue;
        String content1 = "AaaaaaaaContent1" + randValue;
        createNote(title1,content1);

        String title2 = "AaaaaaaaTitle2" + randValue;
        String content2 = "AaaaaaaaContent2" + randValue;
        createNote(title2,content2);

        onView(withContentDescription("More options")).perform(click());
        onView(withText(R.string.action_sortName)).perform(click());

        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withText(title1)).check(matches(isDisplayed()));
        onView(withText(content1)).check(matches(isDisplayed()));
        Espresso.pressBack();

        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withText(title2)).check(matches(isDisplayed()));
        onView(withText(content2)).check(matches(isDisplayed()));
        Espresso.pressBack();

        deleteNoteWithTitle(title1);
        deleteNoteWithTitle(title2);
    }

    @Test
    public void readNote(){
        int randValue = random.nextInt(40) + 1;
        String title = "ReadNoteTitle" + randValue;
        String content = "ReadNoteContent" + randValue;

        createNote(title, content);
        onView(withText(title)).perform(click());
        onView(withText(title)).check(matches(isDisplayed()));
        onView(withText(content)).check(matches(isDisplayed()));

        deleteNoteWithTitle(title);

    }

    @Test
    public void checkMenuItems(){
        onView(withContentDescription("More options")).perform(click());
        onView(withText(R.string.action_sortDate)).check(matches(isDisplayed()));
        onView(withText(R.string.action_sortName)).check(matches(isDisplayed()));
    }


    public void createNote(String title, String content){
        onView(withId(R.id.floatingActionButton)).perform(click());
        onView(withId(R.id.note_Title)).perform(typeText(title));
        onView(withId(R.id.note_Content)).perform(typeText(content));
        closeSoftKeyboard();
        onView(withId(R.id.saveOption)).perform(click());
        onView(withText("No")).perform(click());
    }

    public void deleteNoteWithTitle(String title){
        onView(withText(title)).perform(click());
        onView(withId(R.id.deleteButton)).perform(click());
        onView(withText("Yes")).perform(click());
    }

    public class ToastMatcher extends TypeSafeMatcher<Root> {

        @Override
        public void describeTo(org.hamcrest.Description description) {
            description.appendText("is toast");
        }

        @Override
        public boolean matchesSafely(Root root) {
            int type = root.getWindowLayoutParams().get().type;
            if ((type == WindowManager.LayoutParams.TYPE_TOAST)) {
                IBinder windowToken = root.getDecorView().getWindowToken();
                IBinder appToken = root.getDecorView().getApplicationWindowToken();
                if (windowToken == appToken) {
                    return true;
                }
            }
            return false;
        }
    }

}
