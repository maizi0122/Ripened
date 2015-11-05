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

package org.studio.maizi.viewinjectiondemo;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.studio.maizi.viewinjection.anno.ContentView;
import org.studio.maizi.viewinjection.anno.EventTarget;
import org.studio.maizi.viewinjection.anno.RegistListener;
import org.studio.maizi.viewinjection.anno.ResId;
import org.studio.maizi.viewinjection.impl.EventBinder;
import org.studio.maizi.viewinjection.impl.ViewInjection;
import org.studio.maizi.viewinjectiondemo.fragment.Fragment_Maizi;

@ContentView(R.layout.activity_main)
@SuppressWarnings("all")
public class MainActivity extends Activity implements View.OnClickListener {

    //--define your view field like this--
    @ResId(R.id.ac_main_bt1)
    @RegistListener(listeners = {MainActivity.class})
    private Button ac_main_bt1;
    //--and add annotation ResId like this--

    @ResId(R.id.ac_main_bt2)
    @RegistListener(listeners = {MyOnClickListener2.class})
    private Button ac_main_bt2;

    @ResId(R.id.ac_main_bt3)
    @RegistListener(listeners = {MyOnClickListener3.class})
    private Button ac_main_bt3;

    @ResId(R.id.ac_main_bt4)
    @RegistListener(listeners = {MyOnClickListener4.class})
    private Button ac_main_bt4;

    @ResId(R.id.ac_main_bt5)
    @RegistListener(listeners = {CustomOnClickListener1.class})
    private Button ac_main_bt5;

    @ResId(R.id.ac_main_bt6)
    @RegistListener(listeners = {CustomOnClickListener2.class})
    private Button ac_main_bt6;

    @ResId(R.id.ac_main_bt7)
    private Button ac_main_bt7;

    @ResId(R.id.maizi_contaniner)
    private RelativeLayout maizi_contaniner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //--invoke auto view injection like this--
        new ViewInjection(new EventBinder()).initView(this, new MyOnClickListener4("...fourth way...\nhello maizi"), new CustomOnClickListener2("...sixth way...\nhello maizi"));

        ac_main_bt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "...seventh way...\nhello maizi", Toast.LENGTH_SHORT).show();
            }
        });

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.maizi_contaniner, new Fragment_Maizi());
        ft.commit();
    }

    @Override
    @EventTarget(targets = {R.id.ac_main_bt1})
    public void onClick(View v) {
        Toast.makeText(this, "...first way...\nhello maizi", Toast.LENGTH_SHORT).show();
    }

    private class MyOnClickListener2 implements View.OnClickListener {

        @Override
        @EventTarget(targets = {R.id.ac_main_bt2})
        public void onClick(View v) {
            Toast.makeText(MainActivity.this, "...second way...\nhello maizi", Toast.LENGTH_SHORT).show();
        }
    }

    private class MyOnClickListener3 extends CustomOnClickListener1 {

        @Override
        @EventTarget(targets = {R.id.ac_main_bt3})
        public void onClick(View v) {
            Toast.makeText(MainActivity.this, "...third way...\nhello maizi", Toast.LENGTH_SHORT).show();
        }
    }

    private class MyOnClickListener4 implements View.OnClickListener {

        private String text;

        public MyOnClickListener4(String text) {
            this.text = text;
        }

        @Override
        @EventTarget(targets = {R.id.ac_main_bt4})
        public void onClick(View v) {
            Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
        }
    }
}
