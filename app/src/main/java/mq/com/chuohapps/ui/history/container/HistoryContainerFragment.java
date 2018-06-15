package mq.com.chuohapps.ui.history.container;


import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.facebook.stetho.common.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import mq.com.chuohapps.R;
import mq.com.chuohapps.customview.OnClickListener;
import mq.com.chuohapps.data.helpers.network.response.Vehicle;
import mq.com.chuohapps.ui.history.CPUTime.CPUFragment;
import mq.com.chuohapps.ui.history.Trunk.TrunkFragment;
import mq.com.chuohapps.ui.history.event.ChangeDateEvent;
import mq.com.chuohapps.ui.home.dialog.DateDialog;
import mq.com.chuohapps.ui.xbase.BaseFragment;
import mq.com.chuohapps.utils.AppLogger;
import mq.com.chuohapps.utils.data.DateUtils;

public class HistoryContainerFragment extends BaseFragment<HistoryContainerContract.Presenter>
        implements HistoryContainerContract.View {

    private static int TAB_COUNT = 2;

    @BindView(R.id.tabOption)
    TabLayout tabOption;
    @BindView(R.id.viewpagerOption)
    ViewPager viewpagerOption;
    @BindView(R.id.imageBack)
    ImageView imageBack;
    @BindView(R.id.imageRight)
    ImageView imageRight;

    private ViewPagerAdapter adapter;

    private List<Vehicle> vehicleList = new ArrayList<>();

    String imei;
    String startDate = null;
    String endDate = null;

    public static HistoryContainerFragment newInstance(String imei) {
        return new HistoryContainerFragment().setImei(imei);
    }

    public HistoryContainerFragment setImei(String imei) {
        this.imei = imei;
        return this;
    }

    @Override
    public void onStart() {
        super.onStart();
        registerEventBus();
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterEventBus();
    }

    @Override
    protected int provideLayout() {
        return R.layout.fragment_history_container;
    }

    @Override
    protected Class<HistoryContainerContract.Presenter> providePresenter() {
        return HistoryContainerContract.Presenter.class;
    }

    @Override
    protected void setupNavigationTitle() {

    }

    @Override
    protected void setupViews(@NonNull View view) {
        setupDate();
        setupViewPager();
        setupHeader();
    }

    @Override
    protected void beginFlow(@NonNull View view) {

    }


    private void setupDate() {
        Date dateCurrent = Calendar.getInstance().getTime();
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        startDate = DateUtils.dateToStringSent(new Date(dateCurrent.getTime() - (3 * DAY_IN_MS)));
        endDate = DateUtils.dateToStringSent(dateCurrent);
        doLoadData();
    }

    private void doLoadData() {
        getPresenter().getHistory(imei, startDate, endDate);
    }

    private void setupHeader() {
        imageBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onDelayedClick(View v) {
                getActivity().onBackPressed();
            }
        });
        imageRight.setVisibility(View.VISIBLE);
        imageRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onDelayedClick(View v) {
                openDateDialog();
            }
        });
    }

    @Override
    public void onStartGetHistory() {
        showLoading();
    }

    @Override
    public void getHistorySuccess(List<Vehicle> vehicleList) {
        hideLoading();
        vehicleList.clear();
        vehicleList.addAll(vehicleList);
        LogUtil.e("isSuccessful: " + vehicleList.size());
    }

    @Override
    public void getHistoryError(String message) {
        hideLoading();
        AppLogger.error("load history Error: " + message);
    }

    DateDialog dateDialog;

    private void openDateDialog() {
        if (dateDialog != null && dateDialog.isShowing()) return;
        dateDialog = new DateDialog(getContext());
        dateDialog.setCanceledOnTouchOutside(true);
        dateDialog.setOnChooseListener(new DateDialog.OnChooseListener() {
            @Override
            public void onDone(String startDate_, String endDate_) {
                // TODO: 4/19/2018 some thing with dates
                startDate = startDate_;
                endDate = endDate_;
                doLoadData();
                EventBus.getDefault().post(new ChangeDateEvent(startDate, endDate));
            }
        });
        dateDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dateDialog.release();
            }
        });
        dateDialog.show();
    }

    private void setupViewPager() {
        tabOption.setupWithViewPager(viewpagerOption);
        viewpagerOption.setOffscreenPageLimit(TAB_COUNT);
        viewpagerOption.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                // TODO: 4/18/2018 set text filter time
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (vehicleList != null) {
                    adapter = new ViewPagerAdapter(getChildFragmentManager());
                    adapter.addTab("Trunk");
                    adapter.addTab("CPU time");
                    viewpagerOption.setAdapter(adapter);
                }
            }
        }, 2 * 1000);

    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return TrunkFragment.newInstance(imei, vehicleList, startDate, endDate);
            else return CPUFragment.newInstance(imei, vehicleList, startDate, endDate);
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }

        void addTab(String title) {
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    // TODO: 6/5/2018 keep change date
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(ChangeDateEvent event) {

    }
}
