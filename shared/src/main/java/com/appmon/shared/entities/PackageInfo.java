package com.appmon.shared.entities;

/**
 * Info about a package that will be put into a database.
 */
public class PackageInfo {
    private String name;
    private String packageName;
    private boolean isBlocked;

    public PackageInfo() {
    }

    public PackageInfo(String name, String packageName, boolean blocked) {
        this.name = name;
        this.packageName = packageName;
        this.isBlocked = blocked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
}
