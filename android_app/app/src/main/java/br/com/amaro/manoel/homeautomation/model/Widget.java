package br.com.amaro.manoel.homeautomation.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import br.com.amaro.manoel.homeautomation.AppDatabase;

/**
 * Created by manoel on 14/06/16.
 */

@Table(database = AppDatabase.class)
public class Widget extends BaseModel {

    @PrimaryKey(autoincrement = true)
    private long id;
    @Column
    private String name;
    @Column
    private String specificId;
    private WidgetType type;
    @ForeignKey(tableClass = Device.class)
    private Device device;

    public Widget() {
    }

    public Widget(WidgetType type, String name, String specificId) {
        this.type = type;
        this.name = name;
        this.specificId = specificId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public WidgetType getType() {
        return type;
    }

    public void setType(WidgetType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecificId() {
        return specificId;
    }

    public void setSpecificId(String specificId) {
        this.specificId = specificId;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
