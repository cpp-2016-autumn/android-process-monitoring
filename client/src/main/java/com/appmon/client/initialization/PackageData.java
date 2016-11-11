package com.appmon.client.initialization;

/**
 * Data about a package that will be put into the database.
 */
public class PackageData {
    public String name;
    public boolean blocked;

    public PackageData(){}

    public PackageData(String name, boolean blocked){
        this.name = name;
        this.blocked = blocked;
    }
}
