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

package org.studio.maizi.rapefielddemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.studio.maizi.rapefield.anno.Anim;
import org.studio.maizi.rapefield.anno.EventTarget;
import org.studio.maizi.rapefield.anno.RegistListener;
import org.studio.maizi.rapefield.anno.ResId;
import org.studio.maizi.rapefield.impl.RapeField;
import org.studio.maizi.rapefielddemo.AdapterViewActivity1;
import org.studio.maizi.rapefielddemo.AdapterViewActivity2;
import org.studio.maizi.rapefielddemo.AdapterViewActivity3;
import org.studio.maizi.rapefielddemo.CustomOnClickListener1;
import org.studio.maizi.rapefielddemo.CustomOnClickListener2;
import org.studio.maizi.rapefielddemo.R;

/**
 * Powered by Maizi-Studio.<br />
 * Design by maizi.<br />
 * Created on 15-11-4.
 */
@SuppressWarnings("all")
public class Fragment_Maizi extends android.app.Fragment implements View.OnClickListener {

    @ResId(R.id.ac_main_frag1_bt1)
    @RegistListener(listeners = {Fragment_Maizi.class})
    private Button ac_main_frag1_bt1;

    @ResId(R.id.ac_main_frag1_bt2)
    @RegistListener(listeners = {MyOnClickListener2.class})
    private Button ac_main_frag1_bt2;

    @ResId(R.id.ac_main_frag1_bt3)
    @RegistListener(listeners = {MyOnClickListener3.class})
    private Button ac_main_frag1_bt3;

    @ResId(R.id.ac_main_frag1_bt4)
    @RegistListener(listeners = {MyOnClickListener4.class})
    private Button ac_main_frag1_bt4;

    @ResId(R.id.ac_main_frag1_bt5)
    @RegistListener(listeners = {CustomOnClickListener1.class})
    private Button ac_main_frag1_bt5;

    @ResId(R.id.ac_main_frag1_bt6)
    @RegistListener(listeners = {CustomOnClickListener2.class})
    private Button ac_main_frag1_bt6;

    @ResId(R.id.ac_main_frag1_bt7)
    private Button ac_main_frag1_bt7;

    @ResId(R.id.ac_main_frag1_bt8)
    @RegistListener(listeners = {Fragment_Maizi.class})
    private Button ac_main_frag1_bt8;

    @ResId(R.id.ac_main_frag1_bt9)
    @RegistListener(listeners = {Fragment_Maizi.class})
    private Button ac_main_frag1_bt9;

    @ResId(R.id.ac_main_frag1_bt10)
    @RegistListener(listeners = {Fragment_Maizi.class})
    private Button ac_main_frag1_bt10;

    @ResId(R.id.ac_main_frag_root)
    @Anim(animResId = R.anim.slide_in_bottom_self, duration = 2000, interpolator = android.R.interpolator.accelerate_quad)
    private RelativeLayout ac_main_frag_root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.layout_fragment, container, false);
        new RapeField().inject(this, root,
                new MyOnClickListener4("fragment\n...fourth way...\nhello maizi"),
                new CustomOnClickListener2("fragment\n...sixth way...\nhello maizi"));

        ac_main_frag1_bt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Fragment_Maizi.this.getActivity(), "fragment\n...seventh way...\nhello maizi", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    @Override
    @EventTarget(targets = {R.id.ac_main_frag1_bt1, R.id.ac_main_frag1_bt8, R.id.ac_main_frag1_bt9, R.id.ac_main_frag1_bt10})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ac_main_frag1_bt1:
                Toast.makeText(this.getActivity(), "fragment\n...first way...\nhello maizi", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ac_main_frag1_bt8:
                getActivity().startActivity(new Intent(getActivity(), AdapterViewActivity1.class));
                break;
            case R.id.ac_main_frag1_bt9:
                getActivity().startActivity(new Intent(getActivity(), AdapterViewActivity2.class));
                break;
            case R.id.ac_main_frag1_bt10:
                getActivity().startActivity(new Intent(getActivity(), AdapterViewActivity3.class));
                break;
        }
    }

    private class MyOnClickListener2 implements View.OnClickListener {

        @Override
        @EventTarget(targets = {R.id.ac_main_frag1_bt2})
        public void onClick(View v) {
            Toast.makeText(Fragment_Maizi.this.getActivity(), "fragment\n...second way...\nhello maizi", Toast.LENGTH_SHORT).show();
        }
    }

    private class MyOnClickListener3 extends CustomOnClickListener1 {

        @Override
        @EventTarget(targets = {R.id.ac_main_frag1_bt3})
        public void onClick(View v) {
            Toast.makeText(Fragment_Maizi.this.getActivity(), "fragment\n...third way...\nhello maizi", Toast.LENGTH_SHORT).show();
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
