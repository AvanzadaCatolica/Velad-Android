package com.mac.velad.settings;

public class BooleanSetting extends Setting {

    private Boolean value;

    public BooleanSetting(String title, SettingType settingType) {
        super(title, settingType);
    }



    public BooleanSetting(String title, SettingType settingType, Boolean value) {
        super(title, settingType);
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }
}
