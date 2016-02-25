package vickypatel.ca.androidtestexample;

import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.Set;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by VickyPatel on 2016-01-03.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void ViewVisibilityTest() {
        onView(withId(R.id.userName)).check(matches(isDisplayed()));
        onView(withId(R.id.password)).check(matches(isDisplayed()));
    }

    @Test
    public void buttonClickTest() throws NoSuchFieldException {
        onView(withId(R.id.userName)).perform(typeText("Vicky"));
        onView(withId(R.id.password)).perform(typeText("Patel"));
        onView(withId(R.id.submit)).perform(click());

        VolleyIdlingResource volleyResources;
        volleyResources = new VolleyIdlingResource("VolleyCalls");
        registerIdlingResources(volleyResources);


        onView(withId(R.id.emailAndPassword)).check(matches(isDisplayed()));
        onView(withId(R.id.emailAndPassword)).check(matches(withText("Vicky Patel")));
    }

    public final class VolleyIdlingResource implements IdlingResource {
        private static final String TAG = "VolleyIdlingResource";
        private final String resourceName;

        // written from main thread, read from any thread.
        private volatile ResourceCallback resourceCallback;

        private Field mCurrentRequests;
        private RequestQueue mVolleyRequestQueue;

        public VolleyIdlingResource(String resourceName) throws SecurityException, NoSuchFieldException {
            this.resourceName = resourceName;

            mVolleyRequestQueue = VolleySingleton.getInstance().getRequestQueue();

            mCurrentRequests = RequestQueue.class.getDeclaredField("mCurrentRequests");
            mCurrentRequests.setAccessible(true);
        }

        @Override
        public String getName() {
            return resourceName;
        }

        @Override
        public boolean isIdleNow() {
            try {
                Set<Request> set = (Set<Request>) mCurrentRequests.get(mVolleyRequestQueue);
                int count = set.size();
                if (set != null) {

                    if (count == 0) {
                        Log.d(TAG, "Volley is idle now! with: " + count);
                        resourceCallback.onTransitionToIdle();
                    } else {
                        Log.d(TAG, "Not idle... " + count);
                    }
                    return count == 0;
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Log.d(TAG, "Eita porra.. ");
            return true;
        }

        @Override
        public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
            this.resourceCallback = resourceCallback;
        }

    }

}
