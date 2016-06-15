package br.com.amaro.manoel.homeautomation.model;

/**
 * Created by manoel on 14/06/16.
 */

public class Widget {
    private WidgetType type;
    private String name;
    private String specificId;

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
}
