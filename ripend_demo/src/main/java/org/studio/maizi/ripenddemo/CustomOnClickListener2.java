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

package org.studio.maizi.ripenddemo;

import android.view.View;
import android.widget.Toast;

import org.studio.maizi.ripend.anno.EventTarget;

/**
 * Powered by Maizi-Studio.<br />
 * Design by maizi.<br />
 * Created on 15-11-4.
 */
public class CustomOnClickListener2 implements View.OnClickListener {

    private String text;

    public CustomOnClickListener2(String text) {
        this.text = text;
    }

    @Override
    @EventTarget(targets = {R.id.ac_main_bt6, R.id.ac_main_frag1_bt6})
    public void onClick(View v) {
        Toast.makeText(v.getContext(), text, Toast.LENGTH_SHORT).show();
    }
}
