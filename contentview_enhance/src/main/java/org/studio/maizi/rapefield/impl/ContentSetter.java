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

package org.studio.maizi.rapefield.impl;

import android.app.Activity;

import org.studio.maizi.rapefield.IContent;
import org.studio.maizi.rapefield.anno.ContentView;
import org.studio.maizi.rapefield.dto.ActionParams;
import org.studio.maizi.rapefield.exception.VIRuntimeException;
import org.studio.maizi.rapefield.util.StringFormatter;

/**
 * an implementation of IContent.
 * Powered by Maizi-Studio.<br />
 * Design by maizi.<br />
 * Created on 15-11-5.
 */
@SuppressWarnings("all")
public class ContentSetter implements IContent {

    private static final String LAYOUT_RES_NAME = "layout";
    private static final String NOT_AN_LAYOUT_RES = "the layout resource reference by R.id.%s at class: %s is not an correct layout resource id, please check your code...";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void action(ActionParams params) {
        if (!(params.getObj() instanceof Activity))
            return;
        Activity context = Activity.class.cast(params.getObj());
        ContentView annoCV = null;
        if (context != null && (annoCV = context.getClass().getAnnotation(ContentView.class)) != null) {//inject the content view.
            int resId = annoCV.value();
            setContentView(context, resId);
        }
    }


    @Override
    public void setContentView(Activity context, int layoutResId) {
        if (!context.getResources().getResourceTypeName(layoutResId).equals(LAYOUT_RES_NAME))
            throw new VIRuntimeException(StringFormatter.format(NOT_AN_LAYOUT_RES, context.getResources().getResourceName(layoutResId).split("/")[1], context.getClass().getName()));
        context.setContentView(layoutResId);
    }

}
