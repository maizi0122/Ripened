/*
 *  $HomePage: https://github.com/maizi0122/ $
 *  $Revision: 000001 $
 *  $Date: 2015-10-18 09:05:31 -0000 (Sun, 18 Oct 2015) $
 *
 *  ====================================================================
 *  Copyright (C) 2015 The Maizi-Studio Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *  ====================================================================
 *
 *  This project powered by Maizi-Studio, but works with the
 *  license of apache,so you should abide by this license.
 *  Any question contacting with email below:
 *  maizi0122@gmail.com
 */

package org.studio.maizi.rapefielddemo;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.studio.maizi.rapefield.anno.EventTarget;
import org.studio.maizi.rapefield.anno.RegistListener;
import org.studio.maizi.rapefield.anno.ResId;
import org.studio.maizi.rapefield.impl.RapeField;

import java.util.List;

/**
 * Powered by Maizi-Studio.<br />
 * Design by maizi.<br />
 * Created on 15-11-5.
 */
@SuppressWarnings("all")
public class CustomAdapter extends BaseAdapter implements View.OnLongClickListener {

    private List<String> list;

    //you have no empty-params constructor,you should make instance manually and pass it like : inject(this, new MyAdapter("maizi"));
    public CustomAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = null;
        MyHolder holder = null;
        if (convertView != null) {
            itemView = convertView;
            holder = MyHolder.class.cast(itemView.getTag());
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ac_sec_lv_item, parent, false);
            holder = new MyHolder();
            //-------------------------------------------------
            new RapeField().inject(this, itemView, holder, this);//-----------------attention last this,MyAdapter have been make instance auto,
            //-------------------------------------------------                         //because of annotation @Adapter(MyAdapter.class),we have helped you
            holder.ac_sec_lv_item_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);       //setAdapter automatic with instance creating, if your Adapter class
            itemView.setTag(holder);                                                    //have no empty-param constructor,you should pass the instance in
        }                                                                               //inject(Object... obj) manually,because we don't know what object
        holder.ac_sec_lv_item_iv.setImageResource(R.mipmap.ic_launcher);                //in params to create instance...
        holder.ac_sec_lv_item_tv.setText(list.get(position));
        return itemView;
    }

    @Override
    @EventTarget(targets = {R.id.ac_sec_lv_item_tv})
    public boolean onLongClick(View v) {
        String[] arrays = TextView.class.cast(v.findViewById(R.id.ac_sec_lv_item_tv)).getText().toString().split("\\(<\\-([a-z[\\p{Space}]]+)\\)");
        Toast.makeText(v.getContext(), new StringBuilder("TextView ").append(arrays[1]).append(arrays[2]).append("\n have been long clicked").toString(), Toast.LENGTH_SHORT).show();
        return false;
    }

    private class MyHolder {
        @ResId(R.id.ac_sec_lv_item_iv)
        ImageView ac_sec_lv_item_iv;
        @ResId(R.id.ac_sec_lv_item_tv)
        @RegistListener(listeners = {CustomAdapter.class})
        TextView ac_sec_lv_item_tv;
        @ResId(R.id.ac_sec_lv_item_root)
        View itemView;
    }

}
