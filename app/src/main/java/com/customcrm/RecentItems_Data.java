package com.customCRM;

/**
 * Created by User on 22/07/2016.
 */
public class RecentItems_Data
{
    private String value;
    private String key;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return value;
    }
}