package com.customCRM;

/**
 * Created by User on 26/07/2016.
 */
public class MyShortcut_Data
{
    private String value;
    private String key;
    private String label;

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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return value;
    }
}
