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

package org.studio.maizi.ripend.impl;

import android.widget.AdapterView;

import org.studio.maizi.ripend.IAdapter;
import org.studio.maizi.ripend.dto.ActionParams;
import org.studio.maizi.ripend.exception.VIRuntimeException;
import org.studio.maizi.ripend.util.StringFormatter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * an implementation of IAdapter.<br />
 * Powered by Maizi-Studio.<br />
 * Design by maizi.<br />
 * Created on 15-11-5.
 */
@SuppressWarnings("all")
public class AdapterSetter implements IAdapter {

    private static final String NOT_ADAPTER_VIEW = "Field: %s is not a sub class of AdapterView, please check your code...";
    private static final String NO_MATCH_ADAPTER = "Field: %s's annotation of Adapter which it's param class have no empty-constructor or your forget pass it instance manually...";
    private static final String INSTACE_ERROR = "Field : %s's RegistListener annotation param %s.class have no empty-params constructor or it is a illegal parameter, please check your code...";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void action(ActionParams params) {
        org.studio.maizi.ripend.anno.Adapter annoAdpt = null;
        if ((annoAdpt = params.getField().getAnnotation(org.studio.maizi.ripend.anno.Adapter.class)) != null) {
            setAdapter(params.getTransfer(), params.getObj(), params.getField(), annoAdpt.value(), params.getListeners());
        }
    }

    @Override
    public void setAdapter(Object obj, Object adapter, Field field, Class<?> cls, Object... adapter_listeners) {
        try {
            Constructor<?> constructor = null;
            int paramsLen = 0;
            Object target = null;
            if (!(field.get(obj) instanceof AdapterView))
                throw new VIRuntimeException(StringFormatter.format(NOT_ADAPTER_VIEW, field.toGenericString()));
            Constructor<?>[] declaredConstructors = cls.getDeclaredConstructors();
            for (Constructor<?> _constructor : declaredConstructors) {
                _constructor.setAccessible(true);
                Class<?>[] params = _constructor.getParameterTypes();
                if (params.length == 1) {
                    if (params[0] == obj.getClass()) {
                        constructor = _constructor;
                        paramsLen = 1;
                        break;
                    }
                } else if (params.length == 0) {
                    constructor = _constructor;
                    paramsLen = 0;
                    break;
                }
            }
            if (constructor == null)
                throw new InstantiationException(StringFormatter.format(INSTACE_ERROR, field.toString(), cls.getSimpleName()));
            if (paramsLen == 0)
                target = constructor.newInstance();
            else if (paramsLen == 1)
                target = constructor.newInstance(obj);
            AdapterView.class.cast(field.get(obj)).setAdapter(android.widget.Adapter.class.cast(target));
        } catch (IllegalAccessException e) {
            //can't reach
            e.printStackTrace();
        } catch (InstantiationException e) {//represent no matching empty-constructor,attempt to find instance obj pass by user manually.
            boolean isFind = false;
            android.widget.Adapter target = null;
            for (Object object : adapter_listeners) {
                if (object instanceof android.widget.Adapter) {
                    target = android.widget.Adapter.class.cast(object);
                    try {
                        AdapterView.class.cast(field.get(obj)).setAdapter(target);
                    } catch (IllegalAccessException e1) {
                        //can't reach
                        e1.printStackTrace();
                    }
                    isFind = true;
                    break;
                }
            }
            if (!isFind)
                throw new VIRuntimeException(StringFormatter.format(NO_MATCH_ADAPTER, field.getType().getName()));
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            //can't reach
            e.printStackTrace();
        }
    }
}
