package com.bear.customerview.rollerpicker.entity;

/**
 * Created by liuyuxing on 2016/7/6.
 */
public class CityEntity {
    private String city_id;//城市id
    private String city_name;//城市名称

    public CityEntity(String city_id, String city_name) {
        this.city_id = city_id;
        this.city_name = city_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }
}
