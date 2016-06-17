package br.com.amaro.manoel.homeautomation.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

import br.com.amaro.manoel.homeautomation.AppDatabase;

/**
 * Created by manoel on 14/06/16.
 */

@Table(database = AppDatabase.class)
public class Device extends BaseModel {

    @PrimaryKey(autoincrement = true)
    private long id;

    @Column
    private String deviceId;

    @Column
    private String name;

    @Column
    private String localIp;

    @Column
    private int localPort;

    private List<Widget> widgets;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalIp() {
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public List<Widget> getWidgets() {
        if (widgets == null || widgets.isEmpty()) {
            widgets = SQLite.select()
                    .from(Widget.class)
                    .where(Widget_Table.device_id.eq(id))
                    .queryList();
        }
        return widgets;
    }

    public void setWidgets(List<Widget> widgets) {
        this.widgets = widgets;
    }
}
