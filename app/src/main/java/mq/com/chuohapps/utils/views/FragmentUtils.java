package mq.com.chuohapps.utils.views;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.view.View;



import mq.com.chuohapps.utils.AppUtils;


/**
 * Created by nguyen.dang.tho on 9/1/2017.
 */

public final class FragmentUtils {
    public static final int CONTAINER_MAIN = android.R.id.content;
    /**
     * Replace current fragment with new fragment and addData new fragment to back stack
     */
    public static final int FLAG_ADD = 0;
    /**
     * Replace current fragment with new one, not addData to back stack
     */
    public static final int FLAG_REPLACE = 1;
//    public static final int CONTAINER_MAIN = R.id.container_god;
    /**
     * Clear all fragments in back stack and addData new fragment
     */
    public static final int FLAG_NEW_TASK = 2;
    public static boolean sDisableFragmentAnimations = false;
    private FragmentUtils() {
    }

    public static void add(@NonNull FragmentManager fragmentManager,
                           int containerResource,
                           @NonNull Fragment fragment, boolean canBack, View element) {
        addOrReplace(fragmentManager, containerResource, fragment, element, canBack, false);
    }

    public static void replace(@NonNull FragmentManager fragmentManager,
                               int containerResource,
                               @NonNull Fragment fragment,
                               boolean canBack,
                               View element) {
        addOrReplace(fragmentManager, containerResource, fragment, element, canBack, true);
    }


    private static void addOrReplace(@NonNull FragmentManager fragmentManager,
                                     int containerResource,
                                     @NonNull Fragment fragment,
                                     View element,
                                     boolean canBack,
                                     boolean isReplace) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && element != null) {
            try {
                fragmentTransaction.addSharedElement(element, ViewCompat.getTransitionName(element));
            } catch (Exception e) {

                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            }

        } else {
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        }
        if (isReplace)
            fragmentTransaction.replace(containerResource, fragment, getName(fragment.getClass()));
        else fragmentTransaction.add(containerResource, fragment, getName(fragment.getClass()));
        if (canBack)
            fragmentTransaction.addToBackStack(getName(fragment.getClass()));
        fragmentTransaction.commit();
    }

    /**
     * Clear all fragments in back stack
     *
     * @param fragmentManager .
     */
    public static void clearBackStack(@NonNull FragmentManager fragmentManager) {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public static void backToFragment(@NonNull FragmentManager fragmentManager, @NonNull String fragmentName) {
        if (fragmentManager.getBackStackEntryCount() > 1)
            fragmentManager.popBackStack(fragmentName, 0);
    }

    public static void moveBack(@NonNull FragmentManager fragmentManager, Activity activity) {
        int count = fragmentManager.getBackStackEntryCount();
        if (count > 1) {
            fragmentManager.popBackStack();
        } else {
            AppUtils.runOutOfApp(activity);
        }
    }

    public static String getName(@NonNull Class<?> classType) {
        return classType.getCanonicalName();
    }

    public static void remove(FragmentManager manager, String fragmentName) {
        Fragment fragment = manager.findFragmentByTag(fragmentName);
        if (fragment != null)
            manager.beginTransaction().remove(fragment).commit();
    }
}
