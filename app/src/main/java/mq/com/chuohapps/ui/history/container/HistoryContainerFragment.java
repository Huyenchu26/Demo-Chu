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
import mq.com.chuohapps.ui.home.dialog.DateDialog;
import mq.com.chuohapps.ui.xbase.BaseFragment;
import mq.com.chuohapps.utils.AppLogger;
import mq.com.chuohapps.utils.data.DateUtils;
import mq.com.chuohapps.utils.functions.MessageUtils;

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
    @BindView(R.id.texttime)
    TextView textTime;
    @BindView(R.id.textTitle)
    TextView textTitle;

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
        setupHeader();
        setupViewPager();
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
        getPresenter().getHistory(imei.substring(1), startDate, endDate);
//        fakeData();
    }

    private void fakeData() {
        Vehicle vehicle1 = new Vehicle(new Vehicle.Data("123456321", "26-06-1996", "120", "120",
                "1", "0", "1", "1", "1", "1", "0", "1", "12",
                "12", "ffffffffff010100035b7189ffffffff010100035b8894ffffffffff01010003ab7129ffffffffff01" +
                "0100035be694ffffffffff010100035b7129ffffffffff010100035bcc94ffffffffff010100035b7129ffffffffff810100035bc" +
                "c94ffffffffff01010003597129ffffffffff010100066d3194ffffffffff01010003", "1", "1", "1", "123"));
        Vehicle vehicle2 = new Vehicle(new Vehicle.Data("123456321", "26-06-1996", "120", "120",
                "1", "0", "1", "1", "0", "1", "0", "1", "16",
                "12", "ffffffffff010100035b7189ffffffff010100035b8894ffffffffff01010003ab7129ffffffffff01" +
                "0100035be694ffffffffff010100035b7129ffffffffff010100035bcc94ffffffffff010100035b7129ffffffffff810100035bc" +
                "c94ffffffffff01010003597129ffffffffff010100066d3194ffffffffff01010003", "1", "1", "1", "165"));
        Vehicle vehicle3 = new Vehicle(new Vehicle.Data("123456321", "26-06-1996", "120", "120",
                "1", "0", "1", "1", "1", "1", "0", "1", "16",
                "12", "ffffffffff010100035b7189ffffffff010100035b8894ffffffffff01010003ab7129ffffffffff01" +
                "0100035be694ffffffffff010100035b7129ffffffffff010100035bcc94ffffffffff010100035b7129ffffffffff810100035bc" +
                "c94ffffffffff01010003597129ffffffffff010100066d3194ffffffffff01010003", "1", "1", "1", "369"));
        Vehicle vehicle4 = new Vehicle(new Vehicle.Data("123456321", "26-06-1996", "120", "120",
                "1", "0", "1", "1", "0", "1", "0", "1", "16",
                "12", "ffffffffff010100035b7189ffffffff010100035b8894ffffffffff01010003ab7129ffffffffff01" +
                "0100035be694ffffffffff010100035b7129ffffffffff010100035bcc94ffffffffff010100035b7129ffffffffff810100035bc" +
                "c94ffffffffff01010003597129ffffffffff010100066d3194ffffffffff01010003", "1", "1", "1", "025"));
        Vehicle vehicle5 = new Vehicle(new Vehicle.Data("123456321", "26-06-1996", "120", "120",
                "1", "0", "1", "1", "0", "1", "0", "1", "16",
                "12", "ffffffffff010100035b7189ffffffff010100035b8894ffffffffff01010003ab7129ffffffffff01" +
                "0100035be694ffffffffff010100035b7129ffffffffff010100035bcc94ffffffffff010100035b7129ffffffffff810100035bc" +
                "c94ffffffffff01010003597129ffffffffff010100066d3194ffffffffff01010003", "1", "1", "1", "169"));
        Vehicle vehicle6 = new Vehicle(new Vehicle.Data("123456321", "26-06-1996", "120", "120",
                "1", "0", "1", "1", "1", "1", "0", "1", "1",
                "12", "ff020180035b7129ffffffffff010100035bcc94ffffffff000100035b7129ffffffffff010100035b71" +
                "29ffffffffff010100035bcc94ffffffffff010100035b7129ffffffffff010100035bcccaffffffffff010100035bb129ffffffff" +
                "ff010100035bcc94ffffffffff010100035b7129ffffffffff010100035bcc94ff", "1", "1", "1", "1102"));
        Vehicle vehicle7 = new Vehicle(new Vehicle.Data("123456321", "26-06-1996", "120", "120",
                "1", "0", "1", "1", "1", "1", "0", "1", "1",
                "12", "ffffffffff010100035b7189ffffffff010100035b8894ffffffffff01010003ab7129ffffffffff01" +
                "0100035be694ffffffffff010100035b7129ffffffffff010100035bcc94ffffffffff010100035b7129ffffffffff810100035bc" +
                "c94ffffffffff01010003597129ffffffffff010100066d3194ffffffffff01010003", "1", "1", "1", "178"));
        Vehicle vehicle8 = new Vehicle(new Vehicle.Data("123456321", "26-06-1996", "120", "120",
                "1", "0", "1", "1", "0", "1", "0", "1", "1",
                "12", "ffffffffff010100035b7129ffffffffff010100035bcc94ffffffffff010100035b7129ffffffffff010" +
                "100022b7bcaffffffffff010100b39ecaffffffffff010100035bcc94ffffffffff010100035b71f9ffffffffff010100035bcc94f" +
                "fffffffff010100035b7129ffffffffff810100035bcc94ffffffffff01010003", "1", "1", "1", "1983"));

        vehicleList.add(vehicle1);
        vehicleList.add(vehicle2);
        vehicleList.add(vehicle3);
        vehicleList.add(vehicle4);
        vehicleList.add(vehicle5);
        vehicleList.add(vehicle6);
        vehicleList.add(vehicle7);
        vehicleList.add(vehicle8);
        vehicleList.add(vehicle2);
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
            public void onDone(String startDate_, String endDate_) {
                // TODO: 4/19/2018 some thing with dates
                startDate = startDate_;
                endDate = endDate_;
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
            else return CPUFragment.newInstance(vehicleList);
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
