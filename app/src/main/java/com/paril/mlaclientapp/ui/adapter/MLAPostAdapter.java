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
import com.paril.mlaclientapp.model.MLAPostDetails;
import com.paril.mlaclientapp.model.MlAFriendUsers;

import java.util.List;

/**
 * Created by paril on 7/16/2017.
 */
public class MLAPostAdapter extends BaseAdapter {
    List<MLAPostDetails> list ;
    Context mContext;

    public MLAPostAdapter(Context context, List<MLAPostDetails> list) {
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
            rowView = inflater.inflate(R.layout.user_item_layout3,parent,false);
            dataAdapter = new MLADataAdapter();
            dataAdapter.txtName = (TextView) rowView.findViewById(R.id.mla_usrdisplay_txtName);
            dataAdapter.txtMsg = (TextView) rowView.findViewById(R.id.mla_usrdisplay_txtEmail);

            rowView.setTag(dataAdapter);
            rowView.setTag(R.id.mla_usrdisplay_txtName, dataAdapter.txtName);
            rowView.setTag(R.id.mla_usrdisplay_txtEmail, dataAdapter.txtMsg);

        }


        else  {
            dataAdapter = (MLADataAdapter) rowView.getTag();
        }

        MLAPostDetails userDisplayProvider;
        userDisplayProvider = (MLAPostDetails) this.getItem(position);
        //System.out.println("///////"+userDisplayProvider.getOwner());
        dataAdapter.txtMsg.setText(userDisplayProvider.getPost());
        dataAdapter.txtName.setText(userDisplayProvider.getusername());


        return rowView;
    }

    static class MLADataAdapter
    {
        //CheckBox chckBox;
        TextView txtName;
        TextView txtMsg;
    }
}