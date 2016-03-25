package com.mac.velad.settings;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.mac.velad.R;

import java.util.List;

/**
 * Created by ruenzuo on 25/03/16.
 */
public class SettingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Setting> dataSet;
    private Context context;

    public SettingAdapter(Context context, List<Setting> dataSet) {
        this.context = context;
        this.dataSet = dataSet;
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
        switch (setting.getSettingType()) {
            case SETTING_TYPE_NORMAL: {
                SettingViewHolder viewHolder = (SettingViewHolder) holder;
                viewHolder.textViewTitle.setText(setting.getTitle());
                break;
            }
            case SETTING_TYPE_ACTION: {
                ActionSettingViewHolder viewHolder = (ActionSettingViewHolder) holder;
                viewHolder.textViewTitle.setText(setting.getTitle());
                break;
            }
            case SETTING_TYPE_BOOLEAN: {
                BooleanSettingViewHolder viewHolder = (BooleanSettingViewHolder) holder;
                viewHolder.textViewTitle.setText(setting.getTitle());
                viewHolder.switchValue.setChecked(((BooleanSetting)setting).getValue());
                break;
            }
            case SETTING_TYPE_INFO: {
                InfoSettingViewHolder viewHolder = (InfoSettingViewHolder) holder;
                viewHolder.textViewTitle.setText(setting.getTitle());
                viewHolder.textViewInfo.setText(((InfoSetting) setting).getInfo());
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class SettingViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle;

        public SettingViewHolder(View root) {
            super(root);
            this.textViewTitle = (TextView) root.findViewById(R.id.text_view_title);
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
    }

}
