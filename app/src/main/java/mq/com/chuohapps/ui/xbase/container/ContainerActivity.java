package mq.com.chuohapps.ui.xbase.container;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import mq.com.chuohapps.R;
import mq.com.chuohapps.ui.home.HomeFragment;
import mq.com.chuohapps.ui.xbase.BaseActivity;
import mq.com.chuohapps.utils.views.FragmentUtils;



public class ContainerActivity extends BaseActivity<ContainerContract.Presenter> implements ContainerContract.View {

    @BindView(FragmentUtils.CONTAINER_MAIN)
    View container;

    @Override
    protected int provideLayout() {
        return R.layout.activity_container;
    }

    @Override
    protected Class<ContainerContract.Presenter> providePresenter() {
        return ContainerContract.Presenter.class;
    }

    @Override
    protected void setupViews() {
        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard();
                return false;
            }
        });

    }

    @Override
    protected void beginFlow() {
        startScreens();
    }

    private void startScreens() {
        goToScreen(HomeFragment.newInstance());
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerEventBus();
        if (needRestart) {
            needRestart = false;
            clearAllScreens();
            startScreens();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterEventBus();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Object event) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
