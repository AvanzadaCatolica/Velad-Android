package com.mac.velad.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.mac.velad.R;
import com.mac.velad.general.CalendarHelper;

import java.util.List;

/**
 * Created by ruenzuo on 25/03/16.
 */
public class SettingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnSettingClickListener, OnBooleanSettingCheckedChangeListener {

    private List<Setting> dataSet;
    private Context context;
    private FragmentManager fragmentManager;

    public SettingAdapter(Context context, FragmentManager fragmentManager, List<Setting> dataSet) {
        this.context = context;
        this.dataSet = dataSet;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getItemViewType(int position) {
        Setting setting = dataSet.get(position);
        return setting.getSettingType().ordinal();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (Setting.SettingType.values()[viewType]) {
            case SETTING_TYPE_NORMAL: {
                View view = LayoutInflater.from(context).inflate(R.layout.view_setting, parent, false);
                SettingViewHolder viewHolder = new SettingViewHolder(view);
                return viewHolder;
            }
            case SETTING_TYPE_ACTION: {
                View view = LayoutInflater.from(context).inflate(R.layout.view_action_setting, parent, false);
                SettingViewHolder viewHolder = new ActionSettingViewHolder(view);
                return viewHolder;
            }
            case SETTING_TYPE_BOOLEAN: {
                View view = LayoutInflater.from(context).inflate(R.layout.view_boolean_setting, parent, false);
                SettingViewHolder viewHolder = new BooleanSettingViewHolder(view);
                return viewHolder;
            }
            case SETTING_TYPE_INFO: {
                View view = LayoutInflater.from(context).inflate(R.layout.view_info_setting, parent, false);
                SettingViewHolder viewHolder = new InfoSettingViewHolder(view);
                return viewHolder;
            }
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Setting setting = dataSet.get(position);
        SettingViewHolder baseHolder = (SettingViewHolder) holder;
        baseHolder.bind(setting, this);
        baseHolder.textViewTitle.setText(setting.getTitle());
        if (setting.getDetails() == null) {
            baseHolder.infoButton.setVisibility(View.GONE);
        } else {
            baseHolder.infoButton.setVisibility(View.VISIBLE);
        }
        switch (setting.getSettingType()) {
            case SETTING_TYPE_ACTION: {
                ActionSettingViewHolder viewHolder = (ActionSettingViewHolder) holder;
                viewHolder.textViewTitle.setText(setting.getTitle());
                break;
            }
            case SETTING_TYPE_BOOLEAN: {
                BooleanSettingViewHolder viewHolder = (BooleanSettingViewHolder) holder;
                BooleanSetting booleanSetting = (BooleanSetting) setting;
                viewHolder.switchValue.setChecked(booleanSetting.getValue());
                viewHolder.bind(booleanSetting, (OnBooleanSettingCheckedChangeListener) this);
                break;
            }
            case SETTING_TYPE_INFO: {
                InfoSettingViewHolder viewHolder = (InfoSettingViewHolder) holder;
                viewHolder.textViewInfo.setText(((InfoSetting) setting).getInfo());
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public void onDetailsClick(Setting item) {
        InfoDialogFragment dialogFragment = InfoDialogFragment.newInstance(item.getDetails());
        dialogFragment.show(fragmentManager, InfoDialogFragment.class.toString());
    }

    @Override
    public void onSettingChange(BooleanSetting item) {
        item.setValue(!item.getValue());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(CalendarHelper.SHARED_PREFERENCES_START_MONDAY, item.getValue());
        editor.apply();
    }

    public static class SettingViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle;
        public ImageButton infoButton;

        public SettingViewHolder(View root) {
            super(root);
            this.textViewTitle = (TextView) root.findViewById(R.id.text_view_title);
            this.infoButton = (ImageButton) root.findViewById(R.id.button_info);
        }

        public void bind(final Setting setting, final OnSettingClickListener listener) {
            this.infoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDetailsClick(setting);
                }
            });
        }
    }

    public static class ActionSettingViewHolder extends SettingViewHolder {

        public ActionSettingViewHolder(View root) {
            super(root);
        }
    }

    public static class InfoSettingViewHolder extends SettingViewHolder {
        public TextView textViewInfo;

        public InfoSettingViewHolder(View root) {
            super(root);
            this.textViewInfo = (TextView) root.findViewById(R.id.text_view_info);
        }
    }

    public static class BooleanSettingViewHolder extends SettingViewHolder {
        public Switch switchValue;

        public BooleanSettingViewHolder(View root) {
            super(root);
            this.switchValue = (Switch) root.findViewById(R.id.switch_value);
        }

        public void bind(final BooleanSetting setting, final OnBooleanSettingCheckedChangeListener listener) {
            this.switchValue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    listener.onSettingChange(setting);
                }
            });
        }
    }

}
