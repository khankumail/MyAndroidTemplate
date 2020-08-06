package com.app.sample.mailbox;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.app.sample.mailbox.data.Constant;
import com.app.sample.mailbox.data.GlobalVariable;
import com.app.sample.mailbox.data.Tools;
import com.app.sample.mailbox.fragment.HelpFragment;
import com.app.sample.mailbox.fragment.PeoplesFragment;
import com.app.sample.mailbox.fragment.InboxFragment;
import com.app.sample.mailbox.fragment.OutboxFragment;
import com.app.sample.mailbox.fragment.SettingFragment;
import com.app.sample.mailbox.fragment.TrashFragment;

public class ActivityMain extends AppCompatActivity{

    private Toolbar toolbar;
    public ActionBar actionBar;
    private NavigationView navigationView;
    private GlobalVariable global;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        global = (GlobalVariable) getApplication();

        initData();

        initToolbar();
        initDrawerMenu();

        // set home view
        actionBar.setTitle(getString(R.string.str_nav_inbox));
        displayContentView(R.id.nav_inbox);

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }
    private void initDrawerMenu(){
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            public void onDrawerOpened(View drawerView) {
                updateDrawerCounter();
                hideKeyboard();
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawer.closeDrawers();
                actionBar.setTitle(menuItem.getTitle());
                displayContentView(menuItem.getItemId());
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            doExitApp();
        }
    }

    @Override
    protected void onResume() {
        updateDrawerCounter();
        super.onResume();
    }

    private void updateDrawerCounter(){
        setMenuAdvCounter(R.id.nav_inbox, global.getInbox().size());
        setMenuStdCounter(R.id.nav_trash, global.getTrash().size());
    }

    //set counter in drawer
    private void setMenuAdvCounter(@IdRes int itemId, int count) {
        TextView view = (TextView) navigationView.getMenu().findItem(itemId).getActionView().findViewById(R.id.counter);
        view.setText(count > 0 ? String.valueOf(count) : null);
    }
    //set counter in drawer
    private void setMenuStdCounter(@IdRes int itemId, int count) {
        TextView view = (TextView) navigationView.getMenu().findItem(itemId).getActionView();
        view.setText(count > 0 ? String.valueOf(count) : null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void displayContentView(int id) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        switch (id) {
            case R.id.nav_inbox:
                fragment = new InboxFragment();
                break;
            case R.id.nav_outbox:
                fragment = new OutboxFragment();
                break;
            case R.id.nav_people:
                fragment = new PeoplesFragment();
                break;
            case R.id.nav_trash:
                fragment = new TrashFragment();
                break;
            case R.id.nav_setting:
                fragment = new SettingFragment();
                break;
            case R.id.nav_help:
                fragment = new HelpFragment();
                break;
        }
        fragment.setArguments(bundle);
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_content, fragment);
            fragmentTransaction.commit();
        }
    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void initData(){
        global.setPeoples(Constant.getPeopleData(this));
        global.setInbox(Constant.getInboxData(this));
        global.setOutbox(Constant.getOutboxData(this));
    }
    private long exitTime = 0;

    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, R.string.press_again_exit_app, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

}
