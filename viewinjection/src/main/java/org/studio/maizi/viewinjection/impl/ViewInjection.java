/*
 * $HomePage: https://github.com/maizi0122/ $
 * $Revision: 000001 $
 * $Date: 2015-10-18 09:05:31 -0000 (Sun, 18 Oct 2015) $
 *
 * ====================================================================
 * Copyright (C) 2011 The Maizi-Studio Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This project powered by Maizi-Studio, but works with the
 * license of apache,so you should abide by this license.
 * Any question contacting with email below:
 * maizi0122@gmail.com
 */

package org.studio.maizi.viewinjection.impl;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import org.studio.maizi.viewinjection.IViewInjection;
import org.studio.maizi.viewinjection.R;
import org.studio.maizi.viewinjection.anno.ResId;

import java.lang.reflect.Field;
import java.util.Formatter;

/**
 * auto view injection<br />
 * Powered by Maizi-Studio.<br />
 * Design by maizi.<br />
 * Created on 15-10-30.
 */
public class ViewInjection implements IViewInjection {

    @Override
    public void initView(Activity context) {
        if (context == null)
            throw new RuntimeException("context can not be null,please check your code...");
        Class<? extends Context> clazz = context.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            ResId anno = field.getAnnotation(ResId.class);
            if (anno != null) {
                int resId = anno.id();
                View viewById = context.findViewById(resId);
                if (viewById == null) {
                    throw new RuntimeException(new Formatter().format(context.getResources().getString(R.string.illegal_id), field.toString(), Integer.toHexString(resId)).toString());
                }
                Class superClass = field.getType();
                boolean isFind = false;
                while ((superClass = superClass.getSuperclass()) != null) {
                    if (superClass == View.class) {
                        isFind = true;
                        break;
                    }
                }
                if (!isFind)
                    throw new RuntimeException(new Formatter().format(context.getResources().getString(R.string.illegal_filed), field.toString()).toString());
                try {
                    field.set(context, field.getType().cast(viewById));
                } catch (RuntimeException e) {
                    throw new RuntimeException(new Formatter().format(context.getResources().getString(R.string.cast_exception), field.toString(), e.getMessage()).toString());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
