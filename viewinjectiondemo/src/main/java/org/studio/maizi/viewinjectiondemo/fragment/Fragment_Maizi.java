/*
 *  $HomePage: https://github.com/maizi0122/ $
 *  $Revision: 000001 $
 *  $Date: 2015-10-18 09:05:31 -0000 (Sun, 18 Oct 2015) $
 *
 *  ====================================================================
 *  Copyright (C) 2011 The Maizi-Studio Open Source Project
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

package org.studio.maizi.viewinjectiondemo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.studio.maizi.viewinjection.anno.EventTarget;
import org.studio.maizi.viewinjection.anno.RegistListener;
import org.studio.maizi.viewinjection.anno.ResId;
import org.studio.maizi.viewinjection.impl.EventBinder;
import org.studio.maizi.viewinjection.impl.ViewInjection;
import org.studio.maizi.viewinjectiondemo.CustomOnClickListener1;
import org.studio.maizi.viewinjectiondemo.CustomOnClickListener2;
import org.studio.maizi.viewinjectiondemo.R;

/**
 * Powered by Maizi-Studio.<br />
 * Design by maizi.<br />
 * Created on 15-11-4.
 */
public class Fragment_Maizi extends android.app.Fragment implements View.OnClickListener {

    @ResId(id = R.id.ac_main_frag1_bt1)
    @RegistListener(listeners = {Fragment_Maizi.class})
    private Button ac_main_frag1_bt1;

    @ResId(id = R.id.ac_main_frag1_bt2)
    @RegistListener(listeners = {MyOnClickListener2.class})
    private Button ac_main_frag1_bt2;

    @ResId(id = R.id.ac_main_frag1_bt3)
    @RegistListener(listeners = {MyOnClickListener3.class})
    private Button ac_main_frag1_bt3;

    @ResId(id = R.id.ac_main_frag1_bt4)
    @RegistListener(listeners = {MyOnClickListener4.class})
    private Button ac_main_frag1_bt4;

    @ResId(id = R.id.ac_main_frag1_bt5)
    @RegistListener(listeners = {CustomOnClickListener1.class})
    private Button ac_main_frag1_bt5;

    @ResId(id = R.id.ac_main_frag1_bt6)
    @RegistListener(listeners = {CustomOnClickListener2.class})
    private Button ac_main_frag1_bt6;

    @ResId(id = R.id.ac_main_frag1_bt7)
    private Button ac_main_frag1_bt7;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.layout_fragment_1, container, false);
        new ViewInjection(new EventBinder()).initView(this, root, new MyOnClickListener4("方式四被点击了....."), new CustomOnClickListener2("方式六被点击了....."));

        ac_main_frag1_bt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Fragment_Maizi.this.getActivity(), "方式七被点击了.....", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    @Override
    @EventTarget(targets = {R.id.ac_main_frag1_bt1})
    public void onClick(View v) {
        Toast.makeText(this.getActivity(), "方式一被点击了.....", Toast.LENGTH_SHORT).show();
    }

    private class MyOnClickListener2 implements View.OnClickListener {

        @Override
        @EventTarget(targets = {R.id.ac_main_frag1_bt2})
        public void onClick(View v) {
            Toast.makeText(Fragment_Maizi.this.getActivity(), "方式二被点击了.....", Toast.LENGTH_SHORT).show();
        }
    }

    private class MyOnClickListener3 extends CustomOnClickListener1 {

        @Override
        @EventTarget(targets = {R.id.ac_main_frag1_bt3})
        public void onClick(View v) {
            Toast.makeText(Fragment_Maizi.this.getActivity(), "方式三被点击了.....", Toast.LENGTH_SHORT).show();
        }
    }

    private class MyOnClickListener4 implements View.OnClickListener {

        private String text;

        public MyOnClickListener4(String text) {
            this.text = text;
        }

        @Override
        @EventTarget(targets = {R.id.ac_main_frag1_bt4})
        public void onClick(View v) {
            Toast.makeText(Fragment_Maizi.this.getActivity(), text, Toast.LENGTH_SHORT).show();
        }
    }


}
