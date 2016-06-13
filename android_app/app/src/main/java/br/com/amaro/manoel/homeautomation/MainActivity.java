package br.com.amaro.manoel.homeautomation;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import br.com.amaro.manoel.homeautomation.service.MqttService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AuthActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "MainActivity";

    @BindView(R.id.toolbar)
    View mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.device_io_recyclerview)
    RecyclerView mDeviceIoRecyclerView;

    private MqttService mService;

    @Override
    protected void onCreateAuthenticated(Bundle savedInstanceState) {
        super.onCreateAuthenticated(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        BitmapDrawable bitmapDrawable = null;

        int mToolbarBackgroundRes = R.drawable.bedroom;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            bitmapDrawable = (BitmapDrawable) getDrawable(mToolbarBackgroundRes);
        } else {
            getResources().getDrawable(mToolbarBackgroundRes);
        }

        bitmapDrawable.mutate();
        bitmapDrawable.setTileModeXY(Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        mToolbar.setBackground(bitmapDrawable);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Palette.from(bitmapDrawable.getBitmap())
                    .generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        int defaultColor = MainActivity.this.getResources().getColor(R.color.colorPrimaryDark);
                        int darkColor = palette.getDarkVibrantColor(defaultColor);
                        int lightColor = palette.getLightVibrantColor(defaultColor);
                        MainActivity.this.getWindow().setStatusBarColor(darkColor);
                        MainActivity.this.getWindow().setNavigationBarColor(darkColor);
                    }
                }
            });

        }


        mNavigationView.setNavigationItemSelectedListener(this);

        mDeviceIoRecyclerView.setAdapter(new RecyclerViewAdapter());

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MqttService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mService != null)
            unbindService(mServiceConnection);
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
    }

    @Override
    public void onCheckedChanged(CompoundButton v, boolean isChecked) {
        String value = isChecked ? "1" : "0";
        this.mService.publish("/c58e576ab2c47cb30247ba039bf33a45249dd452/buttons/button01", value);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((MqttService.MqttServiceBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.Holder> {
        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.tile_layout, null);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        class Holder extends RecyclerView.ViewHolder {

            public Holder(View itemView) {
                super(itemView);
            }
        }
    }
}
