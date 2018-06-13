package mq.com.chuohapps.ui.xbase;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import mq.com.chuohapps.AppConfigs;
import mq.com.chuohapps.R;
import mq.com.chuohapps.customview.CustomProgressDialog;
import mq.com.chuohapps.di.PresenterProvider;
import mq.com.chuohapps.ui.home.HomeFragment;
import mq.com.chuohapps.utils.AppLogger;
import mq.com.chuohapps.utils.AppUtils;
import mq.com.chuohapps.utils.functions.MessageUtils;
import mq.com.chuohapps.utils.functions.NetworkUtils;
import mq.com.chuohapps.utils.views.FragmentUtils;
import mq.com.chuohapps.utils.views.ImageUtils;
import mq.com.chuohapps.utils.views.KeyboardUtils;


public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {

    /*MEMORY MANAGEMENT*/
    protected boolean needRestart = false;
    private boolean isFirstFragment = false;
    private P presenter;
    /*DETECT WHAT SCREEN IS SHOWING*/
    private String activeFragmentTag = AppConfigs.EMPTY;
    /*SCREEN UTILS*/
    private CustomProgressDialog dialog;
    /*SENDING DATA BETWEEN SCREENS*/
    private List<Request> requests = new ArrayList<>();
    /**
     * Use for store runnable of request permission.
     */
    private Runnable runAfterRequestPermission;
    private boolean firstTimeStartApp = true;

    public void activeFragment(String name) {
        this.activeFragmentTag = name;
    }

    /**
     * @return it's presenter
     */
    public
    @NonNull
    P getPresenter() {
        return presenter;
    }

    /*ACTIVITY LIFECYCLE*/
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setBackgroundDrawableResource(R.color.colorTextWhite);
        super.onCreate(savedInstanceState);
        setContentView(provideLayout());
        if (presenter == null)
            presenter = setPresenter();
        presenter.onAttachView(this);
        presenter.onAttachUseCase();
        detectFirstFragment();
        ButterKnife.bind(this);
        setupViews();
        if (savedInstanceState == null) beginFlow();
        else onLoadSaveData(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Bundle saveData = setSaveData();
        if (saveData != null) outState.putAll(saveData);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDetachView();
            presenter.onDetachUseCase();
        }
        presenter = null;
        MessageUtils.release();
    }

    @Override
    public void onBackPressed() {
        BaseFragment baseFragment = getTopFragment();
        if (baseFragment != null) {
            if (baseFragment.onBackPressed()) {
                return;
            }
        }
        if (!isSearchBarOpening()) {
            if (isFirstFragment || isFirstFragment()) {
                AppUtils.runOutOfApp(this);
            } else super.onBackPressed();
        } else super.onBackPressed();
    }

    private boolean isSearchBarOpening() {
        try {
            BaseFragment currentFragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(getTopFragmentName());
            if (currentFragment != null && currentFragment.isSearchBarShowing) {
                currentFragment.hideSearchBar();
                return true;
            } else
                return false;
        } catch (Exception ignored) {
        }
        return false;
    }

    private BaseFragment getTopFragment() {
        return (BaseFragment) getSupportFragmentManager().findFragmentByTag(getTopFragmentName());
    }

    protected String getTopFragmentName() {
        return getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
    }

    private boolean isFirstFragment() {
        try {
            return getSupportFragmentManager().getBackStackEntryCount() == 1;
        } catch (Exception e) {

        }
        return false;
    }

    private void detectFirstFragment() {
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                isFirstFragment = getSupportFragmentManager().getBackStackEntryCount() == 1;
            }
        });
    }

    @Override
    public void showLoading() {
        if (dialog == null)
            dialog = createDialogLoading();
        dialog.show();
    }

    private CustomProgressDialog createDialogLoading() {
        CustomProgressDialog dialog = null;
        dialog = new CustomProgressDialog(this, R.style.NewLoadingTheme);
//        dialog = new CustomProgressDialog(this, R.style.MyTheme);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                onHidedLoading();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    /*SCREEN CREATION*/

    @Override
    public void hideLoading() {
        if (dialog != null) dialog.dismiss();
    }

    protected void onHidedLoading() {

    }

    @Override
    public void showKeyboard(EditText editText) {
        //  MessageUtils.cancelAll(this);
        KeyboardUtils.showKeyboard(editText, this);
    }

    @Override
    public void hideKeyboard() {
        KeyboardUtils.hideKeyboard(this);
    }

    @Override
    public boolean isNetworkConnected() {
        return getApplicationContext() != null && NetworkUtils.isNetworkConnected(getApplicationContext());
    }

    /**
     * @return layout resource
     */
    protected abstract int provideLayout();

    /**
     * @return fragment's presenter
     */
    private P setPresenter() {
        return (P) PresenterProvider.provide(providePresenter());
    }

    protected abstract Class<P> providePresenter();

    /**
     * Use for handle all action in views
     */
    protected abstract void setupViews();

    /**
     * Call at first time create an activity to start flow, put logic here if needed.
     */
    protected abstract void beginFlow();

    /**
     * save state of screen when leave screen
     *
     * @return data
     */
    protected Bundle setSaveData() {
        return null;
    }

    /**
     * Restore state for screen when come back
     *
     * @param data ...
     */
    protected void onLoadSaveData(Bundle data) {

    }

    public void putRequest(String fromScreen, String toScreen, Object data) {
        requests.add(new Request(fromScreen, toScreen, data));
    }

    public List<Request> getRequest(String toScreen) {
        List<Request> requests = new ArrayList<>();
        for (int i = 0; i < this.requests.size(); i++) {
            if (this.requests.get(i).toScreen.equals(toScreen)) {
                requests.add(this.requests.get(i));
                this.requests.remove(i);
                i--;
            }
        }
        return requests;
    }

    /*SCREENS MOVING*/
    public <F extends BaseFragment> void makeNewScreenFlow(@NonNull F fragment) {
        goToScreen(FragmentUtils.CONTAINER_MAIN, fragment, FragmentUtils.FLAG_NEW_TASK);
    }

    public <F extends BaseFragment> void goToScreen(@NonNull F fragment) {
        goToScreen(FragmentUtils.CONTAINER_MAIN, fragment, FragmentUtils.FLAG_ADD);
    }

    public <F extends BaseFragment> void goToScreen(int containerLayoutResource,
                                                    @NonNull F fragment,
                                                    int actionFlag) {
        goToScreen(containerLayoutResource, fragment, actionFlag, null);
    }

    public <F extends BaseFragment> void goToScreen(int containerLayoutResource,
                                                    @NonNull F fragment,
                                                    int actionFlag, View element) {
        hideKeyboard();
        if (actionFlag == FragmentUtils.FLAG_ADD) {
            FragmentUtils.replace(getSupportFragmentManager(), containerLayoutResource, fragment, true, element);
        } else if (actionFlag == FragmentUtils.FLAG_REPLACE) {
            FragmentUtils.replace(getSupportFragmentManager(), containerLayoutResource, fragment, false, element);
        } else if (actionFlag == FragmentUtils.FLAG_NEW_TASK) {
            FragmentUtils.clearBackStack(getSupportFragmentManager());
            FragmentUtils.replace(getSupportFragmentManager(), containerLayoutResource, fragment, true, element);
        }
    }

    public <F extends BaseFragment> void showFragmentAsDialog(int containerLayoutResource,
                                                              @NonNull F fragment) {
        FragmentUtils.add(getSupportFragmentManager(), containerLayoutResource, fragment, true, null);
    }

    /*REQUEST PERMISSION*/

    public void backToScreen(String screenName) {
        hideKeyboard();
        FragmentUtils.backToFragment(getSupportFragmentManager(), screenName);
    }

    public void moveBack() {
        hideKeyboard();
        FragmentUtils.moveBack(getSupportFragmentManager(), this);
    }

    protected void clearAllScreens() {
        FragmentUtils.clearBackStack(getSupportFragmentManager());
    }

    /**
     * Request permission for specify use case
     *
     * @param permission                what permission want to granted.
     * @param runAfterRequestPermission what logic doing after request permission.
     */
    public void requestPermission(@NonNull String permission, Runnable runAfterRequestPermission) {
        if (AppUtils.requestPermission(this, permission, AppConfigs.REQUEST_CODE_PERMISSION))
            runFlowAfterRequestPermission(runAfterRequestPermission);
        else this.runAfterRequestPermission = runAfterRequestPermission;

    }

    public void requestPermission(@NonNull String[] permissions, Runnable runAfterRequestPermission) {
        if (AppUtils.requestPermissions(this, permissions, AppConfigs.REQUEST_CODE_PERMISSION))
            runFlowAfterRequestPermission(runAfterRequestPermission);
        else this.runAfterRequestPermission = runAfterRequestPermission;

    }

    /**
     * Doing logic after request permission done and granted
     *
     * @param runnable what to do
     */
    private void runFlowAfterRequestPermission(Runnable runnable) {
        if (runnable != null) {
            new Handler().postDelayed(runnable, 0);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            runFlowAfterRequestPermission(runAfterRequestPermission);
            runAfterRequestPermission = null;
        }
    }

    /*ACTIVITY RESULTS*/
    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BaseFragment activeFragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(activeFragmentTag);

        if (activeFragment == null) return;
        List<OnResultEvent> resultEventList = activeFragment.onResultEventList;
        if (!resultEventList.isEmpty())
            for (OnResultEvent onResultEvent : resultEventList) {
                onResultEvent.onResult(requestCode, resultCode, data);
            }
        List<String> childTags = activeFragment.childTags;
        if (childTags != null && !childTags.isEmpty()) {
            for (String tag : childTags) {
                BaseFragment childFragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(tag);
                if (childFragment != null) {
                    List<OnResultEvent> resultEventListOfChild = childFragment.onResultEventList;
                    if (!resultEventListOfChild.isEmpty())
                        for (OnResultEvent onResultEvent : resultEventListOfChild) {
                            try {
                                onResultEvent.onResult(requestCode, resultCode, data);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                }
            }
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_COMPLETE) {
            AppLogger.memory("TRIM_MEMORY_COMPLETE=" + level);
        } else if (level == TRIM_MEMORY_UI_HIDDEN) {
            AppLogger.memory("TRIM_MEMORY_UI_HIDDEN=" + level);
        } else if (level == TRIM_MEMORY_BACKGROUND) {
            AppLogger.memory("TRIM_MEMORY_BACKGROUND=" + level);
        } else if (level == TRIM_MEMORY_MODERATE) {
            AppLogger.memory("TRIM_MEMORY_MODERATE=" + level);
        } else if (level == TRIM_MEMORY_RUNNING_CRITICAL) {
            AppLogger.memory("TRIM_MEMORY_RUNNING_CRITICAL=" + level);
        } else if (level == TRIM_MEMORY_RUNNING_LOW) {
            AppLogger.memory("TRIM_MEMORY_RUNNING_LOW=" + level);
        } else if (level == TRIM_MEMORY_RUNNING_MODERATE) {
            AppLogger.memory("TRIM_MEMORY_RUNNING_MODERATE=" + level);
        } else {
            AppLogger.memory("TRIM_MEMORY_UNKNOWN=" + level);
        }
        if (firstTimeStartApp) {
            firstTimeStartApp = false;
        } else {
//            if (level >= 80) {
//
//                needRestart = true;
//            } else if (level >= 20 && level < 80) {
            ImageUtils.clear(this);
//                System.gc();
//            }
        }

//        https://developer.android.com/topic/performance/memory.html
    }

    /*LOGGER*/
    protected <T> void logError(T message) {
        AppLogger.error(this.getClass().getSimpleName(), message);
    }

    protected <T> void logDebug(T message) {
        AppLogger.debug(this.getClass().getSimpleName(), message);
    }

    /*RESTART APP SCREEN*/
    @Override
    public void restart() {
        makeNewScreenFlow(HomeFragment.newInstance());
    }


    /*MESSAGE AND NOTIFICATION*/
    @Override
    public <M> void showMessage(M message) {
        MessageUtils.show(this, message);
    }

    @Override
    public <M> void showMessage(M message, int code) {
        MessageUtils.show(this, message, code);
    }

    @Override
    public <M> void showMessageMain(M message) {
        showMessage(message);
    }

    @Override
    public <M> void showMessageMain(M message, int code) {
        showMessage(message, code);
    }

    /*EVENT BUS*/
    protected void registerEventBus() {
        EventBus.getDefault().register(this);
    }

    protected void unregisterEventBus() {
        EventBus.getDefault().unregister(this);
    }


    public interface OnResultEvent {
        void onResult(int requestCode, int resultCode, Intent data);
    }

    class Request {
        public Object data;
        String fromScreen = "";
        String toScreen = "";

        Request(String fromScreen, String toScreen, Object data) {
            this.fromScreen = fromScreen;
            this.toScreen = toScreen;
            this.data = data;
        }
    }

}