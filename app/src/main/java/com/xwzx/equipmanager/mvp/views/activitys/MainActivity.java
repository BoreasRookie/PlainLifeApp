package com.xwzx.equipmanager.mvp.views.activitys;

import android.content.Intent;
import android.os.Message;
import android.view.Gravity;
import android.view.MenuItem;


import com.lzf.easyfloat.EasyFloat;
import com.lzf.easyfloat.enums.ShowPattern;
import com.lzf.easyfloat.enums.SidePattern;
import com.lzf.easyfloat.utils.DisplayUtils;
import com.orhanobut.logger.Logger;
import com.xwzx.equipmanager.App;
import com.xwzx.equipmanager.Constant;
import com.xwzx.equipmanager.R;
import com.xwzx.equipmanager.adapter.ViewPagerAdapter;
import com.xwzx.equipmanager.base.BaseActivity;
import com.xwzx.equipmanager.base.BaseFragment;
import com.xwzx.equipmanager.base.BaseResponse;
import com.xwzx.equipmanager.databinding.ActivityMainBinding;
import com.xwzx.equipmanager.framwork.ClickProxy;
import com.xwzx.equipmanager.internal.components.DaggerMainActivityComponent;
import com.xwzx.equipmanager.internal.modules.MainActivityModule;
import com.xwzx.equipmanager.mq.MessageCallBack;
import com.xwzx.equipmanager.mq.RabbitMQConfiguration;
import com.xwzx.equipmanager.mq.ResqonCallBack;
import com.xwzx.equipmanager.mvp.presenters.presenterimpl.MainActivityPresenter;
import com.xwzx.equipmanager.mvp.views.fragments.MainEquipFragment;
import com.xwzx.equipmanager.mvp.views.fragments.MainIndexKeeperFragment;
import com.xwzx.equipmanager.mvp.views.fragments.MainIndexOperatorFragment;
import com.xwzx.equipmanager.mvp.views.fragments.MainPerCenterKeeperFragment;
import com.xwzx.equipmanager.mvp.views.fragments.MainPerCenterOperatorFragment;
import com.xwzx.equipmanager.mvp.views.viewinterfaces.MainActivityInterface;
import com.xwzx.equipmanager.utils.RxTimer;
import com.xwzx.equipmanager.utils.ToastUtil;
import com.xwzx.equipmanager.widget.viewpager.OnPageChangeListener;

import java.util.ArrayList;

import javax.inject.Inject;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements MainActivityInterface {

    @Inject
    MainActivityPresenter presenter;
    private ArrayList<BaseFragment> fragments;
    private RabbitMQConfiguration rabbitMQConfiguration;

    @Override
    public int setContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.setSwipeBackEnable(false);
        int role = getIntent().getIntExtra(Constant.ROLE, Constant.OPERATOR_ROLE);
        fragments = new ArrayList<>();
        if (role == Constant.KEEPER_ROLE) {
            fragments.add(new MainIndexKeeperFragment());
            fragments.add(new MainEquipFragment());
            fragments.add(new MainPerCenterKeeperFragment());
        } else {
            fragments.add(new MainIndexOperatorFragment());
            fragments.add(new MainEquipFragment());
            fragments.add(new MainPerCenterOperatorFragment());
        }
        this.binding.container.setOffscreenPageLimit(3);
        this.binding.container.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        //初始化悬浮球
        initFloatBall();
    }

    private MenuItem lastMenuItem;

    @Override
    protected void initListener() {
        this.binding.navigationView.setOnNavigationItemSelectedListener(menuItem -> {
            this.lastMenuItem = menuItem;
            switch (menuItem.getItemId()) {
                case R.id.main_index:
                    this.binding.container.setCurrentItem(0);
                    return true;
                case R.id.main_equip:
                    this.binding.container.setCurrentItem(1);
                    return true;
                case R.id.main_center:
                    this.binding.container.setCurrentItem(2);
                    return true;
            }
            return false;
        });
        this.binding.container.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (lastMenuItem != null) {
                    lastMenuItem.setChecked(false);
                } else {
                    binding.navigationView.getMenu().getItem(0).setChecked(false);
                }
                binding.navigationView.getMenu().getItem(position).setChecked(true);
            }
        });
    }

    private void initFloatBall() {
        EasyFloat.with(this)
                .setFilter(ShoppingActivity.class,
                        SubmitApplyActivity.class, SubmitApplyOperationActivity.class)
                // 设置浮窗xml布局文件，并可设置详细信息
                .setLayout(R.layout.floatball_layout, view -> {
                    //   TextView textView =  view.findViewById(R.id.tv_shopping_counts);
                    view.findViewById(R.id.iv_sp_cart).setOnClickListener(new ClickProxy(v -> {
                        Intent intent = new Intent(this, ShoppingActivity.class);
                        startActivity(intent);
                    }));
                })
                // 设置浮窗显示类型，默认只在当前Activity显示，可选一直显示、仅前台显示、仅后台显示
                .setShowPattern(ShowPattern.FOREGROUND)
                // 设置吸附方式，共15种模式，详情参考SidePattern
                .setSidePattern(SidePattern.RESULT_HORIZONTAL)
                // 设置浮窗的标签，用于区分多个浮窗
                .setTag("floatBall")
                // 设置浮窗是否可拖拽，默认可拖拽
                .setDragEnable(true)
                // 系统浮窗是否包含EditText，仅针对系统浮窗，默认不包含
                .hasEditText(false)
                // 设置浮窗固定坐标，ps：设置固定坐标，Gravity属性和offset属性将无效
//                .setLocation(100, 200)
                // 设置浮窗的对齐方式和坐标偏移量
                .setGravity(Gravity.END | Gravity.CENTER_VERTICAL, 0, 200)
                // 设置宽高是否充满父布局，直接在xml设置match_parent属性无效
                .setMatchParent(false, false)
                // 设置Activity浮窗的出入动画，可自定义，实现相应接口即可（策略模式），无需动画直接设置为null
                .setAnimator(null)
                // 设置系统浮窗的出入动画，使用同上
                .setAppFloatAnimator(null)
                // 设置系统浮窗的不需要显示的页面
                // .setFilter(MainActivity:: class.java, SecondActivity:: class.java)
                // 设置系统浮窗的有效显示高度（不包含虚拟导航栏的高度），基本用不到，除非有虚拟导航栏适配问题
                .setDisplayHeight(DisplayUtils.INSTANCE::rejectedNavHeight)
//    .setDisplayHeight(OnDisplayHeight {
//            context -> DisplayUtils.rejectedNavHeight(context)
//        })
                // 浮窗的一些状态回调，如：创建结果、显示、隐藏、销毁、touchEvent、拖拽过程、拖拽结束。
                // ps：通过Kotlin DSL实现的回调，可以按需复写方法，用到哪个写哪个

                // 创建浮窗（这是关键哦😂）
                .show();
    }


    @Override
    protected void initComponent() {
        DaggerMainActivityComponent.builder()
                .netComponent(App.getInstance().getmNetComponent())
                .mainActivityModule(new MainActivityModule(this))
                .appComponent(App.getInstance().getmAppComponent())
                .build()
                .inject(this);
    }

    @Override
    protected void initData() {
        this.rabbitMQConfiguration = RabbitMQConfiguration.getInstance();
        this.handlerReceiverPositionMessage();
        RxTimer hbRxTimer = new RxTimer();
        hbRxTimer.interval(5000, number -> {
            this.rabbitMQConfiguration.basicPublish(() -> "测试内容");
        });
    }
    private void handlerReceiverPositionMessage(){
        rabbitMQConfiguration.basicConsumer(new ResqonCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                System.out.println("handlerReceiverPositionMessage : "+jsonString);
            }
        });
    }

    @Override
    protected void onDestroy() {
        presenter.onDestory();
        //悬浮球销毁，需要重新实例化
        EasyFloat.dismissAppFloat("floatBall");
        super.onDestroy();
    }

    /**
     * 数据请求成功回调
     */
    @Override
    public void onSuccess(BaseResponse<String> s) {
        Logger.e(s.toString());
    }

    /**
     * 数据请求失败回调
     */
    @Override
    public void onFailed() {

    }

    /**
     * 没有网络状态回调
     */
    @Override
    public void noNetWork() {

    }

    /**
     * 无数据状态回调
     */
    @Override
    public void noData() {

    }

    /**
     * 消除LoadingDialog
     */
    @Override
    public void onDisLoadingDialog() {
        this.dismissLoadingDialog();
    }

    @Override
    public void onShowLoadingDialog() {
        this.showLoadingDialog();
    }

    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtil.showBottom(this, getResources().getString(R.string.login_activity_exiting));
            exitTime = System.currentTimeMillis();
            return;
        } else {
            finish();
        }
        super.onBackPressed();
    }
}
