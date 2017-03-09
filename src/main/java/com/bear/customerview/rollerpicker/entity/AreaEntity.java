package com.bear.customerview.rollerpicker.entity;

/**
 * Created by liuyuxing on 2016/7/6.
 */
public class AreaEntity {
    private String area_id;//区域id
    private String area_name;//区域名称

    public AreaEntity(String area_id, String area_name) {
        this.area_id = area_id;
        this.area_name = area_name;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }
}
