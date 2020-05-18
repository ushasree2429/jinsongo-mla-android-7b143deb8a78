package com.paril.mlaclientapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.paril.mlaclientapp.R;
import com.paril.mlaclientapp.model.MLAGroupDetails;
import com.paril.mlaclientapp.model.MlAFriendUsers;

import java.util.List;

public class MLADeleteGroupAdapter extends BaseAdapter {
    List<MLAGroupDetails> list ;
    Context mContext;

    public MLADeleteGroupAdapter(Context context, List<MLAGroupDetails> list) {
        this.list=list;
        this.mContext=context;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        rowView = convertView;
        MLADataAdapter dataAdapter;
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.user_item_layout5,parent,false);
            dataAdapter = new MLADataAdapter();
            dataAdapter.txtName = (TextView) rowView.findViewById(R.id.mla_usrdisplay_txtName);
            dataAdapter.chckBox = (CheckBox) rowView.findViewById(R.id.mla_chckbxusers_checkBx);
            rowView.setTag(dataAdapter);
            rowView.setTag(R.id.mla_usrdisplay_txtName, dataAdapter.txtName);
            rowView.setTag(R.id.mla_chckbxusers_checkBx, dataAdapter.chckBox);

            dataAdapter.chckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag();
                    MLAGroupDetails userDisplayCheckbxProvider;
                    userDisplayCheckbxProvider =  list.get(getPosition);
                    userDisplayCheckbxProvider.setCheck(buttonView.isChecked());
                }
            });
        }


        else  {
            dataAdapter = (MLADataAdapter) rowView.getTag();
        }
        dataAdapter.chckBox.setTag(position);
        MLAGroupDetails userDisplayProvider;
        userDisplayProvider = (MLAGroupDetails) this.getItem(position);
        dataAdapter.txtName.setText(userDisplayProvider.getGroupName());
        dataAdapter.chckBox.setChecked(userDisplayProvider.getCheck());


        return rowView;
    }

    static class MLADataAdapter
    {
        CheckBox chckBox;
        TextView txtName;
        TextView txtEmail;
    }
}