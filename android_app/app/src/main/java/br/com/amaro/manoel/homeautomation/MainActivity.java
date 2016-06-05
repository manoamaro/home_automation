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
import android.widget.Button;
import android.widget.CompoundButton;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.Listener;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

import java.net.URISyntaxException;

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

    private CallbackConnection mMqttConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        MQTT mqtt = new MQTT();
        try {
            mqtt.setHost("tcp://192.168.0.20:1883");
            mqtt.setClientId("10");

            mMqttConnection = mqtt.callbackConnection();

            mMqttConnection.listener(new Listener() {
                @Override
                public void onConnected() {

                }

                @Override
                public void onDisconnected() {

                }

                @Override
                public void onPublish(UTF8Buffer topic, Buffer body, Runnable ack) {
                    ack.run();
                }

                @Override
                public void onFailure(Throwable value) {

                }
            });

            mMqttConnection.connect(new Callback<Void>() {
                @Override
                public void onSuccess(Void value) {
                }

                @Override
                public void onFailure(Throwable value) {
                }
            });


        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

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
                mMqttConnection.publish("TESTE", "TEST".getBytes(), QoS.AT_LEAST_ONCE, false, null);
                break;
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button3:
                mMqttConnection.publish("/c58e576ab2c47cb30247ba039bf33a45249dd452/buttons/button01", "1".getBytes(), QoS.EXACTLY_ONCE, false, null);
                break;
            case R.id.button4:
                mMqttConnection.publish("/c58e576ab2c47cb30247ba039bf33a45249dd452/buttons/button02", "1".getBytes(), QoS.EXACTLY_ONCE, false, null);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton v, boolean isChecked) {
        String value = isChecked ? "1" : "0";
        switch (v.getId()) {
            case R.id.button3:
                mMqttConnection.publish("/c58e576ab2c47cb30247ba039bf33a45249dd452/buttons/button01", value.getBytes(), QoS.EXACTLY_ONCE, false, null);
                break;
            case R.id.button4:
                mMqttConnection.publish("/c58e576ab2c47cb30247ba039bf33a45249dd452/buttons/button02", value.getBytes(), QoS.EXACTLY_ONCE, false, null);
                break;
        }
    }
}
