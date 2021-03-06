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
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.Arrays;
import java.util.List;

import br.com.amaro.manoel.homeautomation.model.Widget;
import br.com.amaro.manoel.homeautomation.model.WidgetType;
import br.com.amaro.manoel.homeautomation.model.Widget_Table;
import br.com.amaro.manoel.homeautomation.service.MqttService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AuthActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
    private RecyclerViewAdapter mAdapter;

    @Override
    protected void onCreateAuthenticated(Bundle savedInstanceState) {
        super.onCreateAuthenticated(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        BitmapDrawable bitmapDrawable;

        int mToolbarBackgroundRes = R.drawable.city;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            bitmapDrawable = (BitmapDrawable) getDrawable(mToolbarBackgroundRes);
        } else {
            bitmapDrawable = (BitmapDrawable) getResources().getDrawable(mToolbarBackgroundRes);
        }

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

        mDeviceIoRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter = new RecyclerViewAdapter();
        mDeviceIoRecyclerView.setAdapter(mAdapter);

        SQLite.select()
                .from(Widget.class).where(Widget_Table.device_id.eq(1))
                .async()
                .queryListResultCallback(new QueryTransaction.QueryResultListCallback<Widget>() {
                    @Override
                    public void onListQueryResult(QueryTransaction transaction, @Nullable List<Widget> tResult) {
                        mAdapter.setWidgets(tResult);
                    }
                });
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

        List<Widget> widgets = Arrays.asList(
                new Widget(WidgetType.SWITCH, "Switch 01", "switch01"),
                new Widget(WidgetType.SWITCH, "Switch 02", "switch02"),
                new Widget(WidgetType.SWITCH, "Switch 03", "switch03"),
                new Widget(WidgetType.SWITCH, "Switch 04", "switch04"),
                new Widget(WidgetType.SWITCH, "Switch 05", "switch05")
        );

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.tile_layout, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.setWidget(widgets.get(position));
        }

        @Override
        public int getItemCount() {
            return widgets.size();
        }

        public void setWidgets(List<Widget> widgets) {
            this.widgets = widgets;
            this.notifyDataSetChanged();
        }

        class Holder extends RecyclerView.ViewHolder {

            Widget widget;
            final TextView widgetName;

            Holder(View itemView) {
                super(itemView);
                widgetName = (TextView) itemView.findViewById(R.id.widget_name);
            }

            void setWidget(Widget widget) {
                this.widget = widget;
                this.widgetName.setText(widget.getName());
            }
        }

    }

}
