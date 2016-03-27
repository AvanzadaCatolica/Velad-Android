package com.mac.velad.settings;

public class BooleanSetting extends Setting {

    private Boolean value;
    private String key;

    public BooleanSetting(String title, SettingType settingType) {
        super(title, settingType);
    }

    public BooleanSetting(String title, SettingType settingType, Boolean value, String key) {
        super(title, settingType);
        this.value = value;
        this.key = key;
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
