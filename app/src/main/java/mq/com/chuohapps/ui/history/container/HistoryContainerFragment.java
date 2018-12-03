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
import android.widget.TextView;

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
import mq.com.chuohapps.ui.history.sos.SOSfragment;
import mq.com.chuohapps.ui.home.dialog.DateDialog;
import mq.com.chuohapps.ui.xbase.BaseFragment;
import mq.com.chuohapps.utils.AppLogger;
import mq.com.chuohapps.utils.data.DateUtils;
import mq.com.chuohapps.utils.functions.MessageUtils;

public class HistoryContainerFragment extends BaseFragment<HistoryContainerContract.Presenter>
        implements HistoryContainerContract.View {

    private static int TAB_COUNT = 3;

    @BindView(R.id.tabOption)
    TabLayout tabOption;
    @BindView(R.id.viewpagerOption)
    ViewPager viewpagerOption;
    @BindView(R.id.imageBack)
    ImageView imageBack;
    @BindView(R.id.imageRight)
    ImageView imageRight;
    @BindView(R.id.texttime)
    TextView textTime;
    @BindView(R.id.textTitle)
    TextView textTitle;

    private ViewPagerAdapter adapter;

    private List<Vehicle> vehicleList = new ArrayList<>();

    String imei;
    String startDate = null;
    String endDate = null;
    private int curMonth = Calendar.getInstance().getTime().getMonth();

    public static HistoryContainerFragment newInstance(String imei, int curMonth) {
        return new HistoryContainerFragment().setImei(imei, curMonth);
    }

    public HistoryContainerFragment setImei(String imei, int curMonth) {
        this.imei = imei;
        this.curMonth = curMonth;
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
        setupHeader();
        setupViewPager();
    }

    @Override
    protected void beginFlow(@NonNull View view) {

    }


    private void setupDate() {
        Date dateCurrent = Calendar.getInstance().getTime();
        long DAY_IN_MS = 3600000 * 24;
        startDate = DateUtils.dateToStringSent(new Date(dateCurrent.getTime() - (DAY_IN_MS)));
        endDate = DateUtils.dateToStringSent(dateCurrent);
        doLoadData();
    }

    private void doLoadData() {
        if (imei.length() > 0)
            getPresenter().getHistory(imei.substring(1), startDate, endDate);
        else return;
    }

    private void setupHeader() {
        enableHeader(imei);
        imageBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onDelayedClick(View v) {
                moveBack();
            }
        });
        imageRight.setVisibility(View.VISIBLE);
        imageRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onDelayedClick(View v) {
                openDateDialog();
            }
        });
        textTime.setVisibility(View.VISIBLE);
        textTime.setText("From: " + startDate + " - To: " + endDate);
    }

    @Override
    public void onStartGetHistory() {
        showLoading();
    }

    @Override
    public void getHistorySuccess(List<Vehicle> vehicleList) {
        hideLoading();
        this.vehicleList.clear();
        this.vehicleList.addAll(vehicleList);
        logError("vehicle list: " + vehicleList.size());
        showMessage("Success!");
        EventBus.getDefault().post(new ChangeDateEvent(startDate, endDate, this.vehicleList));
    }

    @Override
    public void getHistoryError(String message) {
        hideLoading();
        showMessage(message, MessageUtils.ERROR_CODE);
    }

    DateDialog dateDialog;

    private void openDateDialog() {
        if (dateDialog != null && dateDialog.isShowing()) return;
        dateDialog = new DateDialog(getContext());
        dateDialog.setCanceledOnTouchOutside(true);
        dateDialog.setOnChooseListener(new DateDialog.OnChooseListener() {
            @Override
            public void onDone(String startDate_) {
                // TODO: 4/19/2018 some thing with dates
                startDate = startDate_ + " 00:00:00";
                endDate = startDate_ + " 23:59:59";
                doLoadData();
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

        if (vehicleList != null) {
            adapter = new ViewPagerAdapter(getChildFragmentManager());
            adapter.addTab(getString(R.string.title_trunk));
            adapter.addTab(getString(R.string.title_cpu_time));
            adapter.addTab(getString(R.string.title_sos));
            if (viewpagerOption != null)
                viewpagerOption.setAdapter(adapter);
        }
        tabOption.setupWithViewPager(viewpagerOption);
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return TrunkFragment.newInstance(vehicleList);
            else if (position == 1)
                return CPUFragment.newInstance(vehicleList);
            else return SOSfragment.newInstance(vehicleList);
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
        textTime.setText("From: " + event.startDate + " - To: " + event.endDate);
    }
}
