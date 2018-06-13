package mq.com.chuohapps.ui.xbase;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import mq.com.chuohapps.AppConfigs;
import mq.com.chuohapps.R;
import mq.com.chuohapps.customview.OnClickListener;
import mq.com.chuohapps.customview.TextChangedListener;
import mq.com.chuohapps.di.PresenterProvider;
import mq.com.chuohapps.error.NullActivityException;
import mq.com.chuohapps.receivers.NetworkChangeReceiver;
import mq.com.chuohapps.ui.xbase.container.ContainerActivity;
import mq.com.chuohapps.utils.AppLogger;
import mq.com.chuohapps.utils.functions.MessageUtils;
import mq.com.chuohapps.utils.views.FragmentUtils;
import mq.com.chuohapps.utils.views.ImageUtils;


public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements BaseView {

    private static final int DELAYED_TIME_FOR_PREPARE_SCREEN = 170;
    public List<String> childTags = new ArrayList<>();
    public boolean isSearchBarShowing = false;
    /*HANDLE REQUEST FROM ACTIVITY*/
    public List<BaseActivity.OnResultEvent> onResultEventList = new ArrayList<>();
    protected boolean isViewDestroyed = false;
    Unbinder unbinder;
    // private BaseActivity activity;
    private P presenter;
    private Toolbar toolbarHeader;
    private RelativeLayout relativeSearchLayout;
    private WeakReference<View> fragmentView;
    private ImageView imageSearchClear;
    private EditText editSearchQuery;
    private boolean isLowMemory = false;
    /*NETWORK STATUS LISTENING*/
    private NetworkChangeReceiver receiver;
    private boolean enableNetworkListener = false;

    protected ContainerActivity myActivity() {
        try {
            return (ContainerActivity) getActivity();
        } catch (Exception ignored) {
        }
        return null;
    }

    public void activeFragment(String name) {
        if (getActivity() != null) ((BaseActivity) getActivity()).activeFragment(name);
    }

    /*SETUP FOR HEADER BAR*/
    protected ViewGroup getHeaderView() {
        return toolbarHeader;
    }

    protected void enableHeader(String title) {
        if (fragmentView != null)
            toolbarHeader = (Toolbar) fragmentView.get().findViewById(R.id.toolbarHeader);
        //    if (activity != null) activity.setSupportActionBar(toolbarHeader);
        setupHeaderButtons(toolbarHeader, title, true);
    }

    protected void enableHeader(String title, int background) {
        if (fragmentView != null)
            toolbarHeader = (Toolbar) fragmentView.get().findViewById(R.id.toolbarHeader);
        toolbarHeader.setBackgroundResource(background);
        //    if (activity != null) activity.setSupportActionBar(toolbarHeader);
        setupHeaderButtons(toolbarHeader, title, true);
    }

    protected void setHeaderTextColor(int colorResource) {
        if (toolbarHeader == null) return;
        TextView textTitle = (TextView) toolbarHeader.findViewById(R.id.textTitle);
        if (textTitle != null) textTitle.setTextColor(getResources().getColor(colorResource));
    }

    protected void enableHeaderNoBackground(String title) {
        if (fragmentView != null)
            toolbarHeader = (Toolbar) fragmentView.get().findViewById(R.id.toolbarHeader);
        setupHeaderButtons(toolbarHeader, title, false);
    }

    private void setupHeaderButtons(Toolbar toolbar, String title, boolean canBack) {
        setHeaderTitle(title, true);
        ImageView imageBack = (ImageView) toolbar.findViewById(R.id.imageBack);
        if (!canBack) {
            imageBack.setVisibility(View.INVISIBLE);
            return;
        }
        imageBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onDelayedClick(View v) {
                moveBack();
            }
        });
    }

    protected void setHeaderLeftButton(int iconResource, OnClickListener onClickListener) {
        //  Preconditions.checkNotNull(toolbarHeader);
        ImageView imageBack = (ImageView) toolbarHeader.findViewById(R.id.imageBack);
        imageBack.setVisibility(View.VISIBLE);
        imageBack.setImageResource(iconResource);
        imageBack.setOnClickListener(onClickListener);
    }

    protected void setHeaderRightButton(int iconResource, OnClickListener onClickListener) {
//        Preconditions.checkNotNull(toolbarHeader);
        ImageView imageRight = (ImageView) toolbarHeader.findViewById(R.id.imageRight);
        imageRight.setVisibility(View.VISIBLE);
        imageRight.setImageResource(iconResource);
        imageRight.setOnClickListener(onClickListener);
    }

    protected void setHeaderTitle(String title, boolean isAnimate) {
        TextView textTitle = (TextView) toolbarHeader.findViewById(R.id.textTitle);
        textTitle.setVisibility(View.INVISIBLE);
        textTitle.setVisibility(View.VISIBLE);
        textTitle.setText(title == null ? "" : title.trim());
    }

    protected void enableSearch(final TextChangedListener listener) {
        if (toolbarHeader != null) {
            relativeSearchLayout = (RelativeLayout) toolbarHeader.findViewById(R.id.relativeSearchLayout);
            ImageView imageRight = (ImageView) toolbarHeader.findViewById(R.id.imageRight);
            ImageView imageSearchCancel = (ImageView) relativeSearchLayout.findViewById(R.id.imageSearchCancel);
            imageSearchClear = (ImageView) relativeSearchLayout.findViewById(R.id.imageSearchClearText);
            editSearchQuery = (EditText) relativeSearchLayout.findViewById(R.id.editSearchQuery);
            imageRight.setVisibility(View.VISIBLE);
            imageRight.setOnClickListener(new OnClickListener() {
                @Override
                public void onDelayedClick(View v) {
                    showSearchBar();
                }
            });

            imageSearchCancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onDelayedClick(View v) {
                    hideSearchBar();
                }
            });
            imageSearchClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editSearchQuery.setText(AppConfigs.EMPTY);
                }
            });
            editSearchQuery.addTextChangedListener(new TextChangedListener() {
                @Override
                public void onTextChanged(String textChanged) {
                    if (textChanged.equals(AppConfigs.EMPTY))
                        imageSearchClear.setVisibility(View.GONE);
                    else imageSearchClear.setVisibility(View.VISIBLE);
                    listener.onTextChanged(textChanged);
                }
            });


        }
    }

    public void hideSearchBar() {
        imageSearchClear.performClick();
        relativeSearchLayout.setVisibility(View.GONE);
        hideKeyboard();
        editSearchQuery.clearFocus();
        isSearchBarShowing = false;
    }

    public void showSearchBar() {
        relativeSearchLayout.setVisibility(View.VISIBLE);
        showKeyboard(editSearchQuery);
        isSearchBarShowing = true;
    }

    /**
     * @return it's presenter
     */
    public
    @NonNull
    P getPresenter() {
        if (presenter == null) {
            presenter = setPresenter();
            presenter.onAttachUseCase();
            presenter.onAttachView(this);
        }
        return presenter;
    }

    /*FRAGMENT LIFECYCLE*/
    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doOnCreateFragment();
    }

    private void doOnCreateFragment() {
        //  MainApplication.watcher.watch(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }
        //  presenter.onAttachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view;
        if (fragmentView != null && fragmentView.get() != null) {
            view = fragmentView.get();
        } else
            view = inflater.inflate(provideLayout(), container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onLoadSaveData(savedInstanceState);
        if (presenter != null) presenter.onAttachView(this);
        doOnViewCreated(getView());
    }

    private void doOnViewCreated(View view) {
        if (fragmentView == null || fragmentView.get() == null) {
            fragmentView = new WeakReference<>(view);
            onResultEventList.clear();
            setupNavigationTitle();
            setupViews(view);
            if (isLowMemory) isLowMemory = false;
            doBeginFlow();
        }
    }

    private void doBeginFlow() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (getView() != null)
                        beginFlow(getView());
                } catch (Exception e) {
                    logError("begin flow error");
                }
            }
        }, BaseFragment.DELAYED_TIME_FOR_PREPARE_SCREEN);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Bundle saveData = setSaveData();
        if (saveData != null) {
            if (outState != null)
                outState.putAll(saveData);
            else outState = saveData;
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        activeFragment(getTag());
        onResumeForChildOfFragment();
        listenToNetworkConnecting();
    }

    private void onResumeForChildOfFragment() {
        handleDataSendingBetweenScreens();
        if (!childTags.isEmpty()) {
            for (String tag : childTags) {
                BaseFragment childFragment = (BaseFragment) getFragmentManager().findFragmentByTag(tag);
                if (childFragment != null)
                    childFragment.onResume();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        onPauseForChildOfFragment();
        stopListeningNetWork();
    }

    private void onPauseForChildOfFragment() {
        if (!childTags.isEmpty()) {
            for (String tag : childTags) {
                BaseFragment childFragment = (BaseFragment) getFragmentManager().findFragmentByTag(tag);
                if (childFragment != null)
                    childFragment.onPause();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        onStartForChildOfFragment();
    }

    private void onStartForChildOfFragment() {
        if (!childTags.isEmpty()) {
            for (String tag : childTags) {
                BaseFragment childFragment = (BaseFragment) getFragmentManager().findFragmentByTag(tag);
                if (childFragment != null)
                    childFragment.onStart();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        onStopForChildOfFragment();
        ImageUtils.clear(getActivity());
    }

    private void onStopForChildOfFragment() {
        if (!childTags.isEmpty()) {
            for (String tag : childTags) {
                BaseFragment childFragment = (BaseFragment) getFragmentManager().findFragmentByTag(tag);
                if (childFragment != null)
                    childFragment.onStop();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        unbinder = null;
        if (hideMessageWhenDestroy)
            MessageUtils.cancelAll(getActivity());
        if (presenter != null) presenter.onDetachView();
        isViewDestroyed = true;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        clearViews();
    }

    private void clearViews() {
        fragmentView = null;
        toolbarHeader = null;
        relativeSearchLayout = null;
        isLowMemory = true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (presenter != null) presenter.onAttachUseCase();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (presenter != null) presenter.onDetachUseCase();
        presenter = null;
    }

    protected void enableNetworkListener() {
        enableNetworkListener = true;
    }

    private void listenToNetworkConnecting() {
        if (!enableNetworkListener) return;
        if (!isNetworkConnected()) {
            if (receiver == null)
                receiver = new NetworkChangeReceiver(new NetworkChangeReceiver.Listener() {
                    @Override
                    public void onConnected() {
                        onNetWorkConnected();
                    }
                });
            getActivity().registerReceiver(receiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        }
    }

    private void stopListeningNetWork() {
        if (!enableNetworkListener) return;
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
            receiver = null;
        }
    }

    protected void onNetWorkConnected() {

    }

    /**
     * handle onActivityResult of Activity
     *
     * @param onResultEvent
     */
    public void addOnResultEvent(BaseActivity.OnResultEvent onResultEvent) {
        if (onResultEventList.contains(onResultEvent)) return;
        onResultEventList.add(onResultEvent);
    }

    @Override
    public void showLoading() {
        if (getActivity() != null) ((BaseActivity) getActivity()).showLoading();
    }

    @Override
    public void hideLoading() {
        if (getActivity() != null) ((BaseActivity) getActivity()).hideLoading();
    }

    @Override
    public void showKeyboard(EditText editText) {
        if (getActivity() != null) ((BaseActivity) getActivity()).showKeyboard(editText);
    }

    @Override
    public void hideKeyboard() {
        if (getActivity() != null) ((BaseActivity) getActivity()).hideKeyboard();
    }

    @Override
    public boolean isNetworkConnected() {
        return getActivity() != null && ((BaseActivity) getActivity()).isNetworkConnected();
    }


    /*REQUEST PERMISSION*/

    /**
     * Request permission for specify use case
     *
     * @param permission                permission
     * @param runAfterRequestPermission what to do
     * @throws NullActivityException ...
     */
    protected void requestPermission(@NonNull String permission, Runnable runAfterRequestPermission) throws NullActivityException {
        if (getActivity() == null) throw new NullActivityException();
        ((BaseActivity) getActivity()).requestPermission(permission, runAfterRequestPermission);
    }

    protected void requestPermission(@NonNull String[] permissions, Runnable runAfterRequestPermission) throws NullActivityException {
        if (getActivity() == null) throw new NullActivityException();
        ((BaseActivity) getActivity()).requestPermission(permissions, runAfterRequestPermission);
    }

    /*SCREEN MOVING*/
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
        if (containerLayoutResource != FragmentUtils.CONTAINER_MAIN)
            if (!childTags.contains(FragmentUtils.getName(fragment.getClass())))
                childTags.add(FragmentUtils.getName(fragment.getClass()));
        if (getActivity() != null)
            ((BaseActivity) getActivity()).goToScreen(containerLayoutResource, fragment, actionFlag, element);
    }

    public <F extends BaseFragment> void showFragmentAsDialog(int containerLayoutResource,
                                                              @NonNull F fragment) {
        if (getActivity() != null)
            ((BaseActivity) getActivity()).showFragmentAsDialog(containerLayoutResource, fragment);
    }

    public void moveBack() {
        if (getActivity() != null) ((BaseActivity) getActivity()).moveBack();
    }

    /**
     * Back to screen with it's name
     *
     * @param screenName name of screen: fragment.getClass().getSimpleName()
     */
    public void backToScreen(@NonNull String screenName) {
        if (getActivity() != null) ((BaseActivity) getActivity()).backToScreen(screenName);
    }

    /**
     * Start new Activity
     *
     * @param screenClass class of Activity
     */
    public void startActivity(@NonNull Class screenClass) {
        startActivity(new Intent(getActivity(), screenClass));
    }


    /*SCREEN CREATION*/

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


    protected abstract void setupNavigationTitle();

    protected abstract void setupViews(@NonNull View view);

    /**
     * @param view fragment's view
     */
    protected abstract void beginFlow(@NonNull View view);

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

    /*SENDING DATA BETWEEN SCREENS*/
    protected void sendDataToScreen(String screenName, Object data) {
        if (getActivity() != null)
            ((BaseActivity) getActivity()).putRequest(getTag(), screenName, data);
    }

    public void handleDataSendingBetweenScreens() {
        if (getActivity() != null) {
            List<BaseActivity.Request> requests = ((BaseActivity) getActivity()).getRequest(getTag());
            if (!requests.isEmpty()) {
                for (BaseActivity.Request request : requests) {
                    receiveData(request.fromScreen, request.data);
                }

            }
            if (!childTags.isEmpty()) {
                for (String tag : childTags) {
                    BaseFragment childFragment = (BaseFragment) getFragmentManager().findFragmentByTag(tag);
                    if (childFragment != null)
                        childFragment.handleDataSendingBetweenScreens();
                }
            }
        }
    }

    protected void receiveData(String fromScreen, Object data) {

    }

    /*LOGGER*/
    protected <T> void logError(T message) {
        AppLogger.error(this.getClass().getSimpleName(), message);
    }

    protected <T> void logDebug(T message) {
        AppLogger.debug(this.getClass().getSimpleName(), message);
    }

    @Override
    public void restart() {
        hideLoading();
        if (getActivity() != null) ((BaseActivity) getActivity()).restart();
    }

    /*MESSAGE AND NOTIFICATION*/
    private boolean hideMessageWhenDestroy = true;

    @Override
    public <M> void showMessage(M message) {
        hideMessageWhenDestroy = true;
        if (getActivity() != null) ((BaseActivity) getActivity()).showMessage(message);
    }

    @Override
    public <M> void showMessage(M message, int code) {
        hideMessageWhenDestroy = true;
        if (getActivity() != null) ((BaseActivity) getActivity()).showMessage(message, code);
    }

    @Override
    public <M> void showMessageMain(M message) {
        hideMessageWhenDestroy = false;
        if (getActivity() != null) ((BaseActivity) getActivity()).showMessage(message);
    }

    @Override
    public <M> void showMessageMain(M message, int code) {
        hideMessageWhenDestroy = false;
        if (getActivity() != null) ((BaseActivity) getActivity()).showMessage(message, code);
    }

    /*EVENT BUS*/
    protected void registerEventBus() {
        EventBus.getDefault().register(this);

    }

    protected void unregisterEventBus() {
        EventBus.getDefault().unregister(this);
    }

    /**
     * Just use for one fragment in top of fragment's stack, not use for it's childs
     */
    protected boolean onBackPressed() {
        return false;
    }

}