package com.boreas.plainlife.mvp.views.activitys;

import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.boreas.plainlife.R;
import com.boreas.plainlife.adapter.TabLayoutViewPagerAdapter;
import com.boreas.plainlife.base.BaseActivity;
import com.boreas.plainlife.base.BaseFragment;

import com.boreas.plainlife.databinding.ActivityKeeperApplingBinding;
import com.boreas.plainlife.framwork.ClickProxy;
import com.boreas.plainlife.mvp.views.fragments.KeeperApplingFragment;
import com.boreas.plainlife.mvp.views.fragments.KeeperCompletedFragment;

import java.util.ArrayList;

/**
 * 库管，我的申请
 */
public class KeeperApplingActivity extends BaseActivity<ActivityKeeperApplingBinding> {

    private ArrayList<BaseFragment> tabFragments;
    private String[] titles = new String[]{"审批中 5", "已完成 5"};

    @Override
    public int setContentView() {
        return R.layout.activity_keeper_appling;
    }

    @Override
    protected void initView() {
        this.initTabFragments();
        this.binding.headLayout.headTitle.setText(getResources().getString(R.string.my_appling));
        this.binding.headLayout.rightTitle.setVisibility(View.GONE);
        this.binding.headLayout.rightBtn.setVisibility(View.GONE);
        TabLayoutViewPagerAdapter viewPagerAdapter = new TabLayoutViewPagerAdapter(getSupportFragmentManager(), this.tabFragments, titles);
        this.binding.viewPager.setOffscreenPageLimit(this.tabFragments.size());
        this.binding.viewPager.setAdapter(viewPagerAdapter);
        this.binding.tabLayout.setupWithViewPager(this.binding.viewPager);
        for (int i = 0; i < this.binding.tabLayout.getTabCount(); i++) {
            TabLayout.Tab tabAt = this.binding.tabLayout.getTabAt(i);
            View view = getLayoutInflater().inflate(R.layout.tab_item, null);
            TextView tab = view.findViewById(R.id.tab);
            tab.setText(titles[i]);
            tabAt.setCustomView(view);
        }
    }

    private void initTabFragments() {
        this.tabFragments = new ArrayList<>();
        this.tabFragments.add(new KeeperApplingFragment());
        this.tabFragments.add(new KeeperCompletedFragment());
    }

    @Override
    protected void initListener() {
        this.binding.headLayout.back.setOnClickListener(new ClickProxy(v -> {
            finish();
        }));

    }

    @Override
    protected void initComponent() {

    }

    @Override
    protected void initData() {

    }
}