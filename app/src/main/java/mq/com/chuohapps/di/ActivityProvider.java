package mq.com.chuohapps.di;

import mq.com.chuohapps.ui.xbase.BaseActivity;

/**
 * Created by nguyen.dang.tho on 2/23/2018.
 */

public class ActivityProvider {
    public static <A extends BaseActivity> Object provide(Class<A> classType) {
        throw new IllegalArgumentException("Add [" + classType.getCanonicalName() + "]" + " to Activity Provider!!!");
    }
}
