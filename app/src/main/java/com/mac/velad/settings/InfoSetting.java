package com.mac.velad.settings;

/**
 * Created by ruenzuo on 25/03/16.
 */
public class InfoSetting extends Setting {

    private String info;

    public InfoSetting(String title, SettingType settingType) {
        super(title, settingType);
    }

    public InfoSetting(String title, SettingType settingType, String info) {
        super(title, settingType);
        this.info = info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
