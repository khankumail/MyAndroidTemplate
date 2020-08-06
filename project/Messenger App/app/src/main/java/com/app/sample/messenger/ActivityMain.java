package com.app.sample.messenger;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.app.sample.messenger.adapter.PageFragmentAdapter;
import com.app.sample.messenger.data.Tools;
import com.app.sample.messenger.fragment.PageCallFragment;
import com.app.sample.messenger.fragment.PageFriendFragment;
import com.app.sample.messenger.fragment.PageGroupFragment;
import com.app.sample.messenger.fragment.PageRecentFragment;
import com.app.sample.messenger.fragment.PageSettingFragment;

public class ActivityMain extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private View parent_view;
    private FloatingActionButton fab;

    private PageFragmentAdapter adapter;

    private PageRecentFragment f_recent;
    private PageCallFragment f_call;
    private PageGroupFragment f_group;
    private PageFriendFragment f_friend;
    private PageSettingFragment f_setting;
    private static int[] imageResId = {
            R.drawable.ic_tab_recent,
            R.drawable.ic_tab_call,
            R.drawable.ic_tab_group,
            R.drawable.ic_tab_friends,
            R.drawable.ic_tab_setting
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parent_view = findViewById(R.id.viewpager);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        setupTabClick();
        onFabClick();

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    private void onFabClick() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = viewPager.getCurrentItem();
                switch (index) {
                    case 0:
                        Snackbar.make(parent_view, "New Chat Clicked", Snackbar.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Snackbar.make(parent_view, "New Call Clicked", Snackbar.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Snackbar.make(parent_view, "Add Group Clicked", Snackbar.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Snackbar.make(parent_view, "Add Friend Clicked", Snackbar.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }


    private void setupViewPager(ViewPager viewPager) {
        adapter = new PageFragmentAdapter(getSupportFragmentManager());
        if (f_recent == null) {
            f_recent = new PageRecentFragment();
        }
        if (f_call == null) {
            f_call = new PageCallFragment();
        }
        if (f_group== null) {
            f_group = new PageGroupFragment();
        }
        if (f_friend == null) {
            f_friend = new PageFriendFragment();
        }
        if (f_setting == null) {
            f_setting = new PageSettingFragment();
        }
        adapter.addFragment(f_recent, getString(R.string.tab_recent));
        adapter.addFragment(f_call, getString(R.string.tab_call));
        adapter.addFragment(f_group, getString(R.string.tab_group));
        adapter.addFragment(f_friend, getString(R.string.tab_friend));
        adapter.addFragment(f_setting, getString(R.string.tab_setting));
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(imageResId[0]);
        tabLayout.getTabAt(1).setIcon(imageResId[1]);
        tabLayout.getTabAt(2).setIcon(imageResId[2]);
        tabLayout.getTabAt(3).setIcon(imageResId[3]);
        tabLayout.getTabAt(4).setIcon(imageResId[4]);
    }

    private void setupTabClick() {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if(position == 4){//tab setting
                    fab.hide();
                }else{
                    fab.show();
                }
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public void actionClick(View v) {
        f_setting.actionClick(v);
    }
}
