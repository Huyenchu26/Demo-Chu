package mq.com.chuohapps.di;

import mq.com.chuohapps.ui.xbase.BaseFragment;

/**
 * Created by nguyen.dang.tho on 2/23/2018.
 */

public class FragmentProvider {
    public static <F extends BaseFragment> Object provide(Class<F> classType) {
        throw new IllegalArgumentException("Add [" + classType.getCanonicalName() + "]" + " to Fragment Provider!!!");
    }
}
