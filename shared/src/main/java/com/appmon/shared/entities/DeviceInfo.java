package com.appmon.shared.entities;

/**
 * Info about a device that will be put into a database
 */
public class DeviceInfo {
    private String name;

    public DeviceInfo() {
    }

    public  DeviceInfo(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
