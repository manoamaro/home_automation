package br.com.amaro.manoel.homeautomation;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AuthActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "MainActivity";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.button3)
    SwitchCompat button3;
    @BindView(R.id.button4)
    SwitchCompat button4;
    @BindView(R.id.content_main)
    View contentMain;

    @Override
    protected void onCreateAuthenticated(Bundle savedInstanceState) {
        super.onCreateAuthenticated(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);

        button3.setOnCheckedChangeListener(this);
        button4.setOnCheckedChangeListener(this);

    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        switch (item.getItemId()) {
            case R.id.nav_manage:
                getmAuth().signOut();
                break;
            case R.id.nav_share:
                break;
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button3:
                break;
            case R.id.button4:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton v, boolean isChecked) {
        String value = isChecked ? "1" : "0";
        switch (v.getId()) {
            case R.id.button3:
                break;
            case R.id.button4:
                break;
        }
    }
}
