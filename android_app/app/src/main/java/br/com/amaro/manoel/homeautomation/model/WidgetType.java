package br.com.amaro.manoel.homeautomation.model;

/**
 * Created by manoel on 14/06/16.
 */

public enum  WidgetType {

    BUTTON("/buttons"),
    SWITCH("/switches"),
    TEXT_TEMPERATURE("/info/temperature"),
    TEXT_INFORMATION("/info/info"),
    TEXT_HUMIDITY("/info/humidity");


    private String domain;
    WidgetType(String domain) {
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }
}
