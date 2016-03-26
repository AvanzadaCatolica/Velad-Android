package com.mac.velad.settings;

/**
 * Created by ruenzuo on 25/03/16.
 */
public class Setting {

    public enum SettingType {
        SETTING_TYPE_NORMAL, SETTING_TYPE_INFO, SETTING_TYPE_BOOLEAN, SETTING_TYPE_ACTION
    }

    private String title;
    private SettingType settingType;
    private String details;

    public Setting(String title, SettingType settingType) {
        this.title = title;
        this.settingType = settingType;
    }

    public String getTitle() {
        return title;
    }

    public SettingType getSettingType() {
        return settingType;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}

